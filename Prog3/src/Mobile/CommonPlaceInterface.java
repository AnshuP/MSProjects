package Mobile;

import java.rmi.*;

/**
 * Mobile.CommonPlaceInterface defines CommonPlace's RMI method that will be 
 * called from Mobile.Agent objects
 * Common Place is used to enable inter-agent communication
 *
 * @version %I% %G%
 * @since   1.0
 */
public interface CommonPlaceInterface extends Remote {

    /**
     * Stores the given msg in a local hashmap
     * msg is intended for the given agentID as the recipient
     */
	public boolean sendMessage(int agentID, byte[] msg) throws RemoteException;
    
	/**
	 * Called by agents to retrieve msg stored against their agentID
	 * Messages are removed upon retrieval by agents
	 */
    public byte[] fetchMessage(int agentID) throws RemoteException;

	/**
	 * Each agent upon injection calls this method to register itself
	 */
    public void registerAgent(int agentID) throws RemoteException;

	/**
	 * Each agent during termination calls this method to unregister itself
	 */
    public void unregisterAgent(int agentID) throws RemoteException;
    
	/**
	 * Provides the list of agents currently registered at the Common Place
	 */
    public int[] getRegisteredAgents() throws RemoteException;
    
}