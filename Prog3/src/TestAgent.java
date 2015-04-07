import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Mobile.Agent;
import Mobile.CommonPlaceInterface;

public class TestAgent extends Agent {
	public int hopCount = 0;
	public String[] destination = null;

	/**
	 * The constructor receives a String array as an argument from 
	 * Mobile.Inject.
	 *
	 * @param args arguments passed from Mobile.Inject to this constructor
	 */
	public TestAgent( String[] args ) {
		destination = args;
		readCommonPlaceHostname();
	}

	/**
	 * Send messages to the given agentID (via Common Place) and also process responses from the same
	 * @param agentID
	 * @throws RemoteException
	 * @throws InterruptedException
	 */
	public void communicateWithAgent(int agentID) throws RemoteException, InterruptedException {
		String msg = new String(this.agentId + ":TestMessage");
		System.out.println("AgentID (TestAgent) " + this.agentId + " sending message '" + msg + "' to agentID " + agentID);
		// Send the message
		commonPlaceInterface.sendMessage(agentID, msg.getBytes());
		// Run until the programmed set of messages are delivered and received
		while(true) {
			Thread.sleep(200);
			byte[] msgBytes = commonPlaceInterface.fetchMessage(this.agentId);
			String message = null;
			if (msgBytes != null) {
				message = new String(msgBytes);
				System.out.println("AgentID (TestAgent) " + this.agentId + " received message '" + message + "'");
				// Send Quit message to the agent
				msg = new String(this.agentId + ":Quit");
				System.out.println("AgentID (TestAgent) " + this.agentId + " sending message '" + msg + "' to agentID " + agentID);
				commonPlaceInterface.sendMessage(agentID, msg.getBytes());
				// After instructing the agent to quit, break out of the while since
				// there will be no more message exchanges
				break;
			}
		}
	}
	
	
	/**
	 * init( ) is the default method called upon an agent inject.
	 */
	public void init( ) {
				
		System.out.println("AgentID (TestAgent) " + this.agentId + " started");
		try {
			commonPlaceInterface = (CommonPlaceInterface)Naming.lookup("rmi://" + commonPlaceHostname + ":" + _port + "/CommonPlace");
			int[] agents = commonPlaceInterface.getRegisteredAgents();
			for (int agentID : agents) {
				communicateWithAgent(agentID);
			}
		} catch (MalformedURLException | RemoteException | NotBoundException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
