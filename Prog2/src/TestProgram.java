
public class TestProgram {

	/**
	 * @param args
	 */

	public final static String multicast_group = "239.255.255.255";
    public final static int port = 50763;
	public static void main( String[] args ) {
		TestWriter w = new TestWriter( );
	}

}	
class TestWriter {

	public class Data extends JSpace.Entry {
		String value;
		String name;
		public Data(String name, String value)
		{
			this.name =name;
			this.value = value;
		}
		public Data()
		{

		}
		public void printVal( ) {

		}
	}
	
	/**
	 * This constructor instantiates a JavaSpace client and a data entry,
	 * thereafter access the space to write a double value into the space.
	 */
	public TestWriter( ) {
		JSpace.Client client = new JSpace.Client( TestProgram.multicast_group, TestProgram.port );

		Data data = new Data("variable", "myString" );
		client.write( data );

		Data dataFirst = new Data("anonymous","anonyMousString" );
		client.write( data );

		Data dataSecond = new Data("hivariable","hiMyString" );
		client.write( data );

		Data dataThird = new Data("qvariable","QMyString" );
		client.write( data );

	}


}

class TestReader {
    

    public class Template extends JSpace.Entry {
	//public double variable; // variable to read data from the space
	String value;
	String name;
	public Template(String name, String value)
	{
		this.name =name;
		this.value = value;
	}
	public Template() {
		// TODO Auto-generated constructor stub
    }
	public void printVal( ) {
		
	    System.out.println( this.value );
	}
  }

    /**
     * This constructor instantiates a JavaSpace client and a template,
     * thereafter access the space to read the variable data into
     * the template.
     */
    public TestReader( ) {
	JSpace.Client client = new JSpace.Client( TestProgram.multicast_group, TestProgram.port );

	System.out.println( "Reading all values");
	Template template = new  Template("variable", "myString"  );
	client.read( template );
	template.printVal( );	
	
	Template templateFirst = new  Template("anonymous","anonyMousString" );
	client.read( template );
	template.printVal( );
	
	Template templateSecond = new  Template("hivariable","hiMyString" );
	client.read( template );
	template.printVal( );
	
	Template templateThird = new  Template("qvariable","QMyString" );
	client.read( template );
	template.printVal( );
	
	//Take Operation
	System.out.println( "Taking all values" );
	client.take( template );
	template.printVal( );
	
	client.take( templateFirst );
	template.printVal( );
	
	client.take( templateSecond );
	template.printVal( );
	
	client.take( templateThird );
	template.printVal( );
    }
}



