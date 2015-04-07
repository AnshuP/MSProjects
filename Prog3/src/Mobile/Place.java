package Mobile;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

/**
 * Mobile.Place is the our mobile-agent execution platform that accepts an
 * agent transferred by Mobile.Agent.hop( ), deserializes it, and resumes it
 * as an independent thread.
 *
 * @author  Munehiro Fukuda
 * @version %I% %G$
 * @since   1.0
 */
public class Place extends UnicastRemoteObject implements PlaceInterface {
    private AgentLoader loader = null;  // a loader to define a new agent class
    private int agentSequencer = 0;     // a sequencer to give a unique agentId
    public static String usage = 
    		"usage: java -cp Mobile.jar Mobile.Place";
    private String sequencer = null;

    /**
     * This constructor instantiates a Mobiel.AgentLoader object that
     * is used to define a new agen class coming from remotely.
     */
    public Place( ) throws RemoteException {
	super( );
	loader = new AgentLoader( );
    }

    /**
     * deserialize( ) deserializes a given byte array into a new agent.
     *
     * @param buf a byte array to be deserialized into a new Agent object.
     * @return a deserialized Agent object
     */
    private Agent deserialize( byte[] buf ) 
	throws IOException, ClassNotFoundException {
	// converts buf into an input stream
        ByteArrayInputStream in = new ByteArrayInputStream( buf );

	// AgentInputStream identify a new agent class and deserialize
	// a ByteArrayInputStream into a new object
        AgentInputStream input = new AgentInputStream( in, loader );
        return ( Agent )input.readObject();
    }

    /**
     * transfer( ) accepts an incoming agent and launches it as an independent
     * thread.
     *
     * @param classname The class name of an agent to be transferred.
     * @param bytecode  The byte code of  an agent to be transferred.
     * @param entity    The serialized object of an agent to be transferred.
     * @return true if an agent was accepted in success, otherwise false.
     */
    public boolean transfer( String classname, byte[] bytecode, byte[] entity )
	throws RemoteException {
    	boolean success = false;
    	
    	//Register the calling agent into Agent Loader
    	
    	Class agentClass = loader.loadClass(classname, bytecode);
    	
    	try {
    		//Deserializes it
			Agent agent = deserialize(entity);
			
			//Set this agent identifier if it has not yet been set
			if(agent.getId() == -1)
			{
				// Creating unique ID for the agent
				// Unique ID is created by taking the IP address
				// of this host + sequence number
				// (Note that while taking the IP address, we remove the first
				// digit of the IP)
				sequencer = InetAddress.getLocalHost().getHostAddress();
				sequencer = sequencer.replaceAll("\\.", "");

				// Remove first digit of the IP to make the result fit in an int
				agent.setId(agentSequencer + Integer.parseInt(sequencer.substring(1)));
				
				agentSequencer = agentSequencer + 1;
			}
	    	//Instantiates a thread object as passing this agent to its constructor
			Thread r = new Thread(agent);
	    	//invoke thread.start()
			r.start();
			
			success = true;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	//return true if all well
    	return success;
    }

    /**
     * main( ) starts an RMI registry in local, instantiates a Mobile.Place
     * agent execution platform, and registers it into the registry.
     *
     * @param args receives a port, (i.e., 5001-65535).
     */
    public static void main( String args[] ) {

    	// If no args supplied, exit
    	if ( args.length < 1 ) {
    	    System.err.println( usage );
    	    System.exit( -1 );
    	}

    	int port = 0;
    	try {
    	    port = Integer.parseInt( args[0] );  // args[0] = port
    	    Place.startRegistry(port);
    	    Place placeObj = new Place();
    	    // Bind place
    	    Naming.rebind("rmi://localhost:" + port + "/MyPlace", placeObj);
    	    System.out.println("Place Ready");
    	} catch ( Exception e ) {
    	    e.printStackTrace( );
    	    System.err.println( usage );
    	    System.exit( -1 );
    	}    	

    }
    
    /**
     * startRegistry( ) starts an RMI registry process in local to this Place.
     * 
     * @param port the port to which this RMI should listen.
     */
    private static void startRegistry( int port ) throws RemoteException {
        try {
            Registry registry =
                LocateRegistry.getRegistry( port );
            registry.list( );
        }
        catch ( RemoteException e ) {
            Registry registry =
                LocateRegistry.createRegistry( port );
        }
    }
}