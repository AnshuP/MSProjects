import java.util.Comparator;

/**
 * Point class
 * This class encapsulates a 2-dimensional point that is represented
 * by its coordinates on the X and Y axes respectively
 * 
 * This class also provides Comparator implementations for use by the 
 * Collections.sort in order to sort the points by their X or Y coordinate
 * 
 * @author Anshu
 */
public class Point {

	private double xCoordinate;
	
	private double yCoordinate;
	
	public Point()
	{
		
	}
	
	public Point(double xCoordinate, double yCoordinate) {
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	/**
	 * @return the xCoordinate
	 */
	public double getXCoordinate() 
	{
		return xCoordinate;
	}

	/**
	 * @return the yCoordinate
	 */
	public double getYCoordinate() 
	{
		return yCoordinate;
	}
	
	public static class XCoordinateOrder implements Comparator<Point> 
	{
		public int compare(Point p, Point q) {
	        if (p.xCoordinate < q.xCoordinate) return -1;
	        if (p.xCoordinate > q.xCoordinate) return 1;
	        return 0;
	    }
	}

	// compare points according to their y-coordinate
	public static class YCoordinateOrder implements Comparator<Point> 
	{
		public int compare(Point p, Point q) {
	        if (p.yCoordinate < q.yCoordinate) return -1;
	        if (p.yCoordinate > q.yCoordinate) return 1;
	        return 0;
	    }
	}
	
}


