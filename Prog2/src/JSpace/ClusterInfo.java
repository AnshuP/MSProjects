package JSpace;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class ClusterInfo implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1019L;

	public String group;
	public int port;
	public int numTotal;
	public int id;

	public ClusterInfo(String group, int port, int numTotal, int id) {
		this.group = group;
		this.port = port;
		this.numTotal = numTotal;
		this.id = id;
	}

	public static byte[] serialize( ClusterInfo clusterInfo ) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		ObjectOutputStream os = new ObjectOutputStream( out );
		os.writeObject( clusterInfo );
		return out.toByteArray( );
	}

	public static ClusterInfo deserialize( byte[] buf ) throws IOException, ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream( buf );
		ObjectInputStream is = new ObjectInputStream( in );
		return ( ClusterInfo )is.readObject();
	}

}
