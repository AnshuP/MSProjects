package JSpace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * JSpace.JSpace is the server program of our JavaSpace impelementation, 
 * It provides the servers with hashing algorithm as the join the multicast group
 * It also handles a user read(), write(), and take() operations, 
 *
 * @author  Anshu Priyadarshini
 */
public class JSpace {

	/**
	 * @param args
	 */
	private MulticastSocket mSocket = null; // a socket to multicast a packet
	private DatagramSocket dSocket = null;  // a socket to receive a UDP packet
	private int port = 0;
	private boolean amMaster = false;
	String command;
	Connection[] connection = null;
	private static Logger logger = Logger.getLogger("MyLog");
	private static FileHandler logFileHandler;
	private static volatile int[] rangeAssigned;
	private volatile ConcurrentHashMap<String, Entry> myHashMap = new ConcurrentHashMap<String, Entry>();


	 /**
     * Master conrtuctor contains main body of master server
     * @param connection ids of the servers
     * @param multicast group ip
     * @param port no
     */
	public JSpace(Connection[] connection,String multiCast_group, int port)
	{
		this.connection = connection;
		//main body of master server
		try {
			mSocket = new MulticastSocket(port); 
			InetAddress group 
			= InetAddress.getByName( multiCast_group );
			//master server joins the multicast group
			mSocket.joinGroup(group);
			dSocket = new DatagramSocket( ); 

			//Initializing the slave servers passing group ip,port,no of servers and id
			for(int i = 0; i<connection.length;i++)
			{
				connection[i].out.writeObject(multiCast_group);
				connection[i].out.flush();
				connection[i].out.writeObject(new Integer(port));
				connection[i].out.flush();
				connection[i].out.writeObject(new Integer(connection.length+1));
				connection[i].out.flush();
				connection[i].out.writeObject(new Integer(i+1));
				connection[i].out.flush();
				
				System.out.println((String)connection[i].in.readObject());

			}
			//Gets alphabetical range for hashing
			rangeAssigned = hashAlgorithmForRange(connection.length+1,0);
			
			//Starts the SoaceThread to listen to client
			SpaceThread st = new SpaceThread(rangeAssigned);
			new Thread(st).start();	

		} catch( IOException e ) {
			e.printStackTrace( );
			System.exit( -1 );
		}
		catch(ClassNotFoundException e){
			e.printStackTrace( );
		}
		this.port = port;
		
		//logger.info("Master first log");  

		//Read input from System administrator
		while(true) {
			System.out.println("%");
			System.out.flush();
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				command = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//Writes the command obtained to the Slaves servers
			for(int i = 0; i<connection.length;i++) {
				try {
					connection[i].out.writeObject(command);
					connection[i].out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			//Calls show() or quit() based on the command received
			if(command.equalsIgnoreCase("show"))
			{
				
				try {
					show();
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
			}
			else if (command.equalsIgnoreCase("quit"))
			{
				quit();
			}

		}
	}

	 /**
     * Slave conrtuctor contains main body of slave server
     * @param connection id of the server
     */
	
	public JSpace(Connection connection)
	{
		//JSpace.logFile();
		//logger.info("Slave first log");  		
		String command = null;
		try {
			//Receives all information from master server
			String mgroup = (String)connection.in.readObject();
			int port = (Integer)connection.in.readObject();
			int numTotal = (Integer)connection.in.readObject();
			int id = (Integer)connection.in.readObject();

			//logger.info("slave ClusterInfo " + port + " " + mgroup + " " + numTotal + " " + id);
			//logFileHandler.flush();
			connection.out.writeObject("Slave-" + id);
			connection.out.flush();

			//Join multicast group
			mSocket = new MulticastSocket(port); 
			InetAddress group 
			= InetAddress.getByName( mgroup );
			mSocket.joinGroup(group);
			dSocket = new DatagramSocket( ); 

			// call method to obtain range by providing numTotal and id
			rangeAssigned = hashAlgorithmForRange(numTotal,id);
			
			//logger.info("Range start = "+rangeAssigned[0]+ " and range end = " + rangeAssigned[1]);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		
		//Starts SpaceThread to listen to client
		SpaceThread st = new SpaceThread(rangeAssigned);
		new Thread(st).start();	

		//Reads command from System administrator
		while(true) {
			try {
				//logger.info("Slave second log");  

				command = (String)connection.in.readObject();
				//logger.info("Received command [" + command + "]");

				//Calls show or quit based on the command
				if(command.equalsIgnoreCase("show"))
				{
					//logger.info("Slave-I'm in Show");
					slaveShow(connection);

				}
				else if (command.equalsIgnoreCase("quit"))
				{
					slaveQuit();
				}
			}catch (ClassNotFoundException | IOException e) {
				//logger.info("Slave exception: " + e.getMessage());
			}
		}

	}

	 /**
     * Creates range for hashing 
     * @param total no of servers and the server id
     */
	public int[] hashAlgorithmForRange(int numTotal, int serverId)
	{
		//logger.info("numTotal: " + numTotal);
		int countInaGroup = 26/numTotal;
		// ASCII a-z
		int rangeStart = 97; // corresponds to 'a'
		int range[] = new int[2];
		int rangeDivision[] = new int[numTotal];
		int i = 0;
		for(i = 0; i < numTotal; i++)
		{
			rangeDivision[i] = rangeStart;
			rangeStart = rangeStart + countInaGroup;
		}
		
		//starting range
		range[0] = rangeDivision[serverId];
		//end range
		range[1] = range[0] + countInaGroup - 1;
		//Assigns 122 range limit to the last server that joins the group
		if (serverId == numTotal - 1) {
			range[1] = 122; // 122 corresponds to 'z'
		}

		return range;
	}

	/**
     * Shows the entry values of both master and slaves servers 
     * 
     */
	public void show() throws ClassNotFoundException, IOException
	{
		//show all hashtables
		System.out.println("Slave-0");

		//master's entries
		for (Entry entry : myHashMap.values()) {
			System.out.println(entry.getType() + " " + entry.getName() + " " + entry.getValue());
		}

		//slave servers entries
		for(int i= 0; i< connection.length;i++)
		{
			System.out.println("Slave-"+(i+1));
			System.out.println((String)connection[i].in.readObject());
		}
	}

	/**
     * retrieves and sends the entry values of slaves servers to the master for display
     * @param connection of the particular slave server
     */
	public void slaveShow(Connection connection) throws IOException
	{
		StringBuffer outputBuffer = new StringBuffer();
		
		for (Entry entry : myHashMap.values()) {
			//appends all entries into the buffer
			outputBuffer.append(entry.getType() + " " + entry.getName() + " " + entry.getValue() + "\n");
		}
		//sends the buffer entries to master server
		connection.out.writeObject(outputBuffer.toString());
	}

	/**
     * Terminates all connections
     */
	public void quit()
	{
		for(int i= 0; i< connection.length;i++) {
			connection[i].close();
		}

		System.exit(1);
	}

	/**
     * acknowledges master and terminates connection
     */
	public void slaveQuit()
	{
		//all slaves acknowledge master
		//all slaves terminate themselves
		System.exit(1);
	}
	

	/**
     * To create log handler and log files for each server.
     * Used for debugging
     */
	public static void logFile()
	{
		try {  	    
			String hostname = InetAddress.getLocalHost().getHostName();
			logFileHandler = new FileHandler("/net/metis/home2/anshup/Desktop/Prog2/MyLogFile" + hostname + ".log", false);
			logger.addHandler(logFileHandler);  
			logger.setLevel(Level.ALL);  
			SimpleFormatter formatter = new SimpleFormatter();  
			logFileHandler.setFormatter(formatter);  
			// the following statement is used to log any messages  
			//logger.info("Test init log");  

		} catch (UnknownHostException | SecurityException e) {  
			e.printStackTrace();  
		} catch (IOException e) {  
			e.printStackTrace();  
		}  

		 
	}
	
	
	/**
	 * SpaceThread class is spawned from all the slaves. 
	 * It continuously listens to the client.
	 * if client sends a packet, it starts another thread to handle it and continues listening
	 *
	  */
	private class SpaceThread implements Runnable
	{
		int[] rangeAssigned;

		
		public SpaceThread(int[] rangeAssigned)
		{
			this.rangeAssigned = rangeAssigned;
		}

		/**
	     * run() method listens for packet from client and deserializes it once received
	     */
		@Override
		public void run() {
			//always waits for UDP multicast message from client

			Entry template = null;
			InetAddress addr = null;
			try
			{
				String hostname = InetAddress.getLocalHost().getHostName();
				//logger.info("In SpaceThread run of host = "+hostname);
				while ( true ) {

					byte[] buf = new byte[1024];
					DatagramPacket p = new DatagramPacket( buf, buf.length );
					//logger.info("SpaceThread before receive");
					//listens for any packet from client
					mSocket.receive( p );
										
					template = Entry.deserialize( buf ); // deserialize it
					addr = p.getAddress();
										
					//instantiates new per request thread and passes the message to it
					new Thread( new SessionThread( template, addr , this.rangeAssigned) ).start( );
					//logFileHandler.flush();
				}
			}catch (IOException e) {
				e.printStackTrace();
				
			}catch(ClassNotFoundException e){
				e.printStackTrace();
				
			}
			
		}

	}

	/**
	 * SessionThread class is  
	 * It handles the client operation-read,write,take
	 *
	  */
	class SessionThread implements Runnable
	{
		int messageFromClient;
		InetAddress addr;
		String variableName;
		Entry msg;
		int[] rangeAssigned;

		public SessionThread(Entry msg, InetAddress addr, int[] rangeAssigned) 
		{
			this.msg = msg;
			this.messageFromClient = msg.getOperation();
			this.addr = addr;
			this.variableName = msg.getName();
			this.rangeAssigned = rangeAssigned;
			//logger.info("In SessionThread run "  + messageFromClient);
			//logFileHandler.flush();
		}

		/**
	     * run() method handles the client's request-read,write,take
	     */
		@Override
		public void run() {
			//If operation is read
			if(messageFromClient == 0)
			{
				char variableStart = variableName.toLowerCase().charAt(0);
				
				//To identify if the requested variable in the server's range or not
				if(variableStart >= this.rangeAssigned[0] && variableStart <= this.rangeAssigned[1]){
					
					Entry template = null;

					//Reads till it receives a template
					while (true) {
						try {
							template = myHashMap.get(variableName);
							if (template != null) {
								//logger.info("Found not null template. Breaking");
								//breaks when template received
								break;
							} else {
								//logger.info("Found null template. Sleeping");								
								Thread.sleep(50);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					try {
						byte[] buf = null;
						buf = Entry.serialize( template ); // serialize an entry
						DatagramPacket packet = new DatagramPacket( buf, buf.length, addr, port );
						//sends it to the client
						mSocket.send( packet );
					} catch (IOException e) {
						//logger.info(e.getMessage());
					}
				}

			}
			else if(messageFromClient == 1)//write operation
			{
				
				//logger.info("variableName [" + variableName + "]");
				//logFileHandler.flush();

				char variableStart = variableName.toLowerCase().charAt(0);
				try{
					//To identify if the requested variable in the server's range or not					
					if(variableStart >= this.rangeAssigned[0] && variableStart <= this.rangeAssigned[1])
					{
						//save it into an arraylist in java space
						String hostname = InetAddress.getLocalHost().getHostName();
						myHashMap.put(msg.getName(), msg);
						
					}
				}
				catch(IOException e)
				{
					//logger.info(e.getMessage());
				}

			}
			else if(messageFromClient == 2)//take operation
			{
				
				char variableStart = variableName.toLowerCase().charAt(0);
				//To identify if the requested variable in the server's range or not
				if(variableStart >= this.rangeAssigned[0] && variableStart <= this.rangeAssigned[1]){
					
					Entry template = null;
					//Loops till it gets the variable to read
					while (true) {
						try {
							template = myHashMap.remove(variableName);
							if (template != null) {
								//logger.info("Found not null template. Breaking");
								//breaks once the template is found
								break;
							} else {
								//logger.info("Found null template. Sleeping");		
								Thread.sleep(50);
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					try {
						byte[] buf = null;
						buf = Entry.serialize( template ); // serialize an entry
						DatagramPacket packet = new DatagramPacket( buf, buf.length, addr, port );
						//sends it to the client
						mSocket.send( packet );
					} catch (IOException e) {

					}
				}
			}

		}

	}



}


