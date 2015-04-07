package Mobile;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Mobile.Agent is the base class of all user-define mobile agents. It carries
 * an agent identifier, the next host IP and port, the name of the function to
 * invoke at the next host, arguments passed to this function, its class name,
 * and its byte code. It runs as an independent thread that invokes a given
 * function upon migrating the next host.
 *
 * @author  Munehiro Fukuda
 * @version %I% %G%
 * @since   1.0
 */
public class Agent implements Serializable, Runnable {
	// live data to carry with the agent upon a migration
	protected int agentId        = -1;    // this agent's identifier
	private String _hostname     = null;  // the next host name to migrate
	private String _function     = null;  // the function to invoke upon a move
	protected int _port          = 0;     // the next host port to migrate
	private String[] _arguments  = null;  // arguments pass to _function
	private String _classname    = null;  // this agent's class name
	private byte[] _bytecode     = null;  // this agent's byte code

	protected CommonPlaceInterface commonPlaceInterface;
	protected String commonPlaceHostname;

	/**
	 * setPort( ) sets a port that is used to contact a remote Mobile.Place.
	 * 
	 * @param port a port to be set.
	 */
	public void setPort( int port ) {
		this._port = port;
	}

	/**
	 * setId( ) sets this agent identifier: agentId.
	 *
	 * @param id an idnetifier to set to this agent.
	 */
	public void setId( int id ) {
		this.agentId = id;
	}

	/**
	 * getId( ) returns this agent identifier: agentId.
	 *
	 * @param this agent's identifier.
	 */
	public int getId( ) {
		return agentId;
	}

	/**
	 * getByteCode( ) reads a byte code from the file whosename is given in
	 * "classname.class".
	 *
	 * @param classname the name of a class to read from local disk.
	 * @return a byte code of a given class.
	 */
	public static byte[] getByteCode( String classname ) {
		// create the file name
		String filename = classname + ".class";

		// allocate the buffer to read this agent's bytecode in
		File file = new File( filename );
		byte[] bytecode = new byte[( int )file.length( )];

		// read this agent's bytecode from the file.
		try {
			BufferedInputStream bis =
					new BufferedInputStream( new FileInputStream( filename ) );
			bis.read( bytecode, 0, bytecode.length );
			bis.close( );
		} catch ( Exception e ) {
			e.printStackTrace( );
			return null;
		}

		// now you got a byte code from the file. just return it.
		return bytecode;	
	}

	/**
	 * getByteCode( ) reads this agent's byte code from the corresponding file.
	 *
	 * @return a byte code of this agent.
	 */
	public byte[] getByteCode( ) {
		if ( _bytecode != null ) // bytecode has been already read from a file
			return _bytecode; 

		// obtain this agent's class name and file name
		_classname = this.getClass( ).getName( );
		_bytecode = getByteCode( _classname );

		return _bytecode;
	}

	/**
	 * run( ) is the body of Mobile.Agent that is executed upon an injection
	 * or a migration as an independent thread. run( ) identifies the method 
	 * with a given function name and arguments and invokes it. The invoked
	 * method may include hop( ) that transfers this agent to a remote host or
	 * simply returns back to run( ) that termiantes the agent.
	 */
	public void run( ) {

		Method method;
		try {
			// Find the method to invoke
			if (_arguments == null) {
				method = this.getClass().getMethod(_function);
				method.invoke(this);
			} else {
				method = this.getClass().getMethod(_function, _arguments.getClass());
				method.invoke(this, (Object)_arguments);
			}
			Thread.currentThread().stop();
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvocationTargetException e2) {
			// print nothing 
		}

	}

	/**
	 * Read the hostname of the Common Place platform from the config file
	 * Common Place is used for inter-agent communication
	 */
	protected void readCommonPlaceHostname() {	
		BufferedReader fileReader = null;
		try {
			// Open a configuration file
			fileReader 
			= new BufferedReader( new InputStreamReader
					( new BufferedInputStream
							( new FileInputStream
									( new File( "config.txt" ) ) ) ) );
			commonPlaceHostname = fileReader.readLine( ); 
			fileReader.close( );
		} catch( Exception e ) {
			e.printStackTrace( );
		}
	}
	
	/**
	 * Periodically poll Common Place for any messages for this agent
	 */
	public void listenForMessages() {
		while(true) {
			try {
				Thread.sleep(100);
				getMessage();
			} catch (InterruptedException | RemoteException | MalformedURLException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Retrieve message for this agent from the Common Place
	 * @throws RemoteException
	 * @throws NotBoundException 
	 * @throws MalformedURLException 
	 */
	public void getMessage() throws RemoteException, MalformedURLException, NotBoundException {
		if (commonPlaceInterface == null) {
			commonPlaceInterface = (CommonPlaceInterface)Naming.lookup("rmi://" + commonPlaceHostname + ":" + _port + "/CommonPlace");
		}
		byte[] msgBytes = commonPlaceInterface.fetchMessage(agentId);
		String message = null;
		if (msgBytes != null) {
			message = new String(msgBytes);
			System.out.println("AgentID " + agentId + " received message '" + message + "'");

			// Inter-agent messages follow the format 'SenderAgentID:Message'
			
			String[] msgTokens = message.split(":");

			if (msgTokens.length == 2) {
				if (msgTokens[1].equalsIgnoreCase("TestMessage")) {
					// If TestMessage was received, send an Ack
					int senderAgentID = Integer.parseInt(msgTokens[0]);
					String ackMsg = new String(this.agentId + ":Ack");
					System.out.println("AgentID " + this.agentId + " sending message '" + ackMsg + "' to agentID " + senderAgentID);
					commonPlaceInterface.sendMessage(senderAgentID, ackMsg.getBytes());
				} else if (msgTokens[1].equalsIgnoreCase("Quit")) {
					// If Quit was received, then quit! (stop the thread)
					System.out.println("AgentID " + agentId + " quitting!");
					commonPlaceInterface.unregisterAgent(this.agentId);
					Thread.currentThread().stop();
				}
			}
		}
	}

	/**
	 * hop( ) transfers this agent to a given host, and invoeks a given
	 * function of this agent.
	 *
	 * @param hostname the IP name of the next host machine to migrate
	 * @param function the name of a function to invoke upon a migration
	 */    
	public void hop( String hostname, String function ) {
		hop( hostname, function, null );
	}

	/**
	 * hop( ) transfers this agent to a given host, and invoeks a given
	 * function of this agent as passing given arguments to it.
	 *
	 * @param hostname the IP name of the next host machine to migrate
	 * @param function the name of a function to invoke upon a migration
	 * @param args     the arguments passed to a function called upon a 
	 *                 migration.
	 */
	@SuppressWarnings( "deprecation" )
	public void hop( String hostname, String function, String[] args ) {

		try {
			_function = function;
			_arguments = args;

			// Check to see if there's any message for this agent at the Common Place
			getMessage();

			// Load this agent's bytecode into the memory
			// Serialize this agent into a byte array
			byte[] serializedAgent = this.serialize();
			// Find a remote place through Naming.lookup()
			PlaceInterface placeInterface = (PlaceInterface)Naming.lookup("rmi://" + hostname + ":" + _port + "/MyPlace");
			this.getByteCode();
			// Invoke an RMI call
			placeInterface.transfer(_classname, _bytecode, serializedAgent);

		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * serialize( ) serializes this agent into a byte array.
	 *
	 * @return a byte array to contain this serialized agent.
	 */
	private byte[] serialize( ) {
		try {
			// instantiate an object output stream.
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			ObjectOutputStream os = new ObjectOutputStream( out );

			// write myself to this object output stream
			os.writeObject( this );

			return out.toByteArray( ); // conver the stream to a byte array
		} catch ( IOException e ) {
			e.printStackTrace( );
			return null;
		}
	}
}