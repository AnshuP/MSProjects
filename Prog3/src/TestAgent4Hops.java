import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import Mobile.Agent;
import Mobile.CommonPlaceInterface;

/**
 * TestAgent4Hops is a test mobile agent that is injected to the 1st Mobile.Place
 * platform to print the breath message, migrates to the 2nd platform to
 * say "Hola!", moves to the 3rd platform to say "Aloha!", and finally moves to 
 * the 4th platform to say "Bonjour!".
 * 
 * @version %I% %G%
 * @since   1.0
 */
public class TestAgent4Hops extends Agent {
	public int hopCount = 0;
	public String[] destination = null;

	/**
	 * The consturctor receives a String array as an argument from 
	 * Mobile.Inject.
	 *
	 * @param args arguments passed from Mobile.Inject to this constructor
	 */
	public TestAgent4Hops( String[] args ) {
		destination = args;
		readCommonPlaceHostname();
	}

	/**
	 * init( ) is the default method called upon an agent inject.
	 */
	public void init( ) {
		System.out.println( "agent (" + agentId + ") invoked init: " +
				"hop count = " + hopCount +
				", next dest = " + destination[hopCount] );
		String[] args = new String[1];
		args[0] = "Hola!";
		hopCount++;

		try {
			if (commonPlaceInterface == null) {
				commonPlaceInterface = (CommonPlaceInterface)Naming.lookup("rmi://" + commonPlaceHostname + ":" + _port + "/CommonPlace");
			}
			commonPlaceInterface.registerAgent(agentId);
		} catch (RemoteException | MalformedURLException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		hop( destination[0], "step", args );
	}

	/**
	 * step( ) is invoked upon an agent migration to destination[0] after 
	 * init( ) calls hop( ).
	 * 
	 * @param args arguments passed from init( ).
	 */
	public void step( String[] args ) {
		System.out.println( "agent (" + agentId + ") invoked step: " +
				"hop count = " + hopCount +
				", next dest = " + destination[hopCount] +
				", message = " + args[0] );
		args[0] = "Aloha!";
		hopCount++;
		hop( destination[1], "shift", args );

	}

	/**
	 * shift( ) is invoked upon an agent migration to destination[1] after
	 * step( ) calls hop( ).
	 *
	 * @param args arguments passed from step( ).
	 */
	public void shift( String[] args ) {
		System.out.println( "agent (" + agentId + ") invoked jump: " +
				"hop count = " + hopCount +
				", next dest = " + destination[hopCount] +
				", message = " + args[0] );
		args[0] = "Bonjour!";
		hopCount++;
		hop( destination[2], "jump", args );
	}

	/**
	 * jump( ) is invoked upon an agent migration to destination[2] after
	 * shift( ) calls hop( ).
	 *
	 * @param args arguments passed from step( ).
	 */
	public void jump( String[] args ) {
		System.out.println( "agent (" + agentId + ") invoked jump: " +
				"hop count = " + hopCount +
				", message = " + args[0] );
		
		listenForMessages();
	}
	
}