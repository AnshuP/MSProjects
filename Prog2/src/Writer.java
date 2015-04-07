import java.io.*;

/**
 * Writer is an example user program that behaves as a JavaSpace client
 * and writes a double-type value, (i.e., 1.7320508075) from the JavaSpace 
 * identified with 239.255.255.255:50763. To run your own JavaSpace 
 * envrionment, you need to change GroupAddress:Port.
 *
 * @author  Munehiro Fukuda
 * @version %I% %G%
 * @since   1.0
 */
public class Writer {
    private final static String multicast_group = "239.255.255.255";
    private final static int port = 50763;

    public class Data extends JSpace.Entry {
	public String variable = "myString"; // a value to write
	public void printVal( ) {
	    System.out.println( variable );
	}
    }

    /**
     * This constructor instantiates a JavaSpace client and a data entry,
     * thereafter access the space to write a double value, (i.e., 
     * 1.7320508075) into the space.
     */
    public Writer( ) {
	JSpace.Client client = new JSpace.Client( multicast_group, port );

	Data data = new Data( );
	client.write( data );
    }

    /**
     * The actual program starts from the main( ) and instantiates a Writer
     * object.
     *
     * @param args nothing to pass
     */

    public static void main( String[] args ) {
	new Writer( );
    }
}
