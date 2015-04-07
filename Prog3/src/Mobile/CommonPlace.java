package Mobile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Mobile.CommonPlace is the our mobile-agent execution platform that 
 * enables inter-agent communication via itself
 *
 * @version %I% %G$
 * @since   1.0
 */
public class CommonPlace extends UnicastRemoteObject implements CommonPlaceInterface {
	private AgentLoader loader = null;  // a loader to define a new agent class
    public static String usage = 
    		"usage: java -cp Mobile.jar Mobile.CommonPlace";
    private Map<Integer, String> messages = new HashMap<Integer, String>();
    private ArrayList<Integer> registeredAgents = new ArrayList<Integer>();

    /**
     * This constructor instantiates a Mobiel.AgentLoader object that
     * is used to define a new agent class coming from remotely.
     */
    public CommonPlace( ) throws RemoteException {
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
    	    CommonPlace.startRegistry(port);
    	    CommonPlace placeObj = new CommonPlace();
    	    // Bind the place
    	    Naming.rebind("rmi://localhost:" + port + "/CommonPlace", placeObj);
    	    System.out.println("Place Ready");
    	    placeObj.messages.put(0, "CommonPlace");
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

    /**
     * Stores the given msg in a local hashmap
     * msg is intended for the given agentID as the recipient
     */
	@Override
	public boolean sendMessage(int agentID, byte[] msg)
			throws RemoteException {
		// TODO Auto-generated method stub
		String message = new String(msg);
		messages.put(agentID, message);
		System.out.println("Common place has recorded message '" + message + "' intended for agentID " + agentID);
		return true;
	}

	/**
	 * Called by agents to retrieve msg stored against their agentID
	 * Messages are removed upon retrieval by agents
	 */
	@Override
	public byte[] fetchMessage(int agentID) throws RemoteException {
		// TODO Auto-generated method stub
		String message = messages.remove(agentID);
		if (message != null) {
			return message.getBytes();
		}
		return null;
	}

	/**
	 * Each agent upon injection calls this method to register itself
	 */
	@Override
	public void registerAgent(int agentID) throws RemoteException {
		registeredAgents.add(agentID);
		System.out.println("Common place has registered agentID: " + agentID);
	}

	/**
	 * Each agent during termination calls this method to unregister itself
	 */
	@Override
	public void unregisterAgent(int agentID) throws RemoteException {
		registeredAgents.remove(new Integer(agentID));
		System.out.println("Common place has unregistered agentID: " + agentID);
	}

	/**
	 * Provides the list of agents currently registered at the Common Place
	 */
	@Override
	public int[] getRegisteredAgents() throws RemoteException {
		// TODO Auto-generated method stub
		int[] agents = new int[registeredAgents.size()];
		int i = 0;
		for (Integer agentID : registeredAgents) {
			agents[i++] = agentID;
		}
		return agents;
	}
}