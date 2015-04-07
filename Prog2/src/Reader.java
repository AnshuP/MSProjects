import java.io.*;

/**
 * Reader is an example user program that behaves as a JavaSpace client
 * and reads a double-type variable from the JavaSpace identified with
 * 239.255.255.255:50763. To run your own JavaSpace envrionment, you
 * need to change GroupAddress:Port.     
 *
 * @author  Munehiro Fukuda
 * @version %I% %G%
 * @since   1.0
 */
public class Reader {
    private final static String multicast_group = "239.255.255.255";
    private final static int port = 50763;

    public class Template extends JSpace.Entry {
	public double variable; // variable to read data from the space
	public void printVal( ) {
	    System.out.println( variable );
	}
    }

    /**
     * This constructor instantiates a JavaSpace client and a template,
     * thereafter access the space to read the variable data into
     * the template.
     */
    public Reader( ) {
	JSpace.Client client = new JSpace.Client( multicast_group, port );

	Template template = new  Template( );
	client.read( template );
	template.printVal( );	
    }

    /**
     * The actual program starts from the main( ) and instantiates a Reader    
     * object.
     *
     * @param args nothing to pass
     */
    public static void main( String[] args ) {
	new Reader();
    }
}
