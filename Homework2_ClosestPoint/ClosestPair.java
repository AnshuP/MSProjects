import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ClosestPair class
 * This class contains the logic for the Closest Pair of Points problem as described 
 * in the homework
 * In order to instantiate this class, one needs to provide a list of Point objects
 * There has to be at least one pair in the list of Point objects, in other words,
 * the list must have two or more points
 * 
 * @author Anshu
 *
 */
public class ClosestPair {

    private List<Point> sortedByX;
    private List<Point> sortedByY;
    
    /**
     * The constructor method takes the list of points and initializes
     * the lists by sorting the input list by its X and Y coordinates.
     * It also checks whether or not the input list has sufficient data
     * for calculating the closest pair points distance. 
     * @param points
     * @throws Exception
     */
    public ClosestPair(List<Point> points) throws Exception 
    {
    	// There must at least one pair (i.e. two points) 
    	// to calculate the closest pair distance
    	if (points == null || points.size() < 2) {
    		throw new Exception("Insufficient points data");
    	}

        this.sortedByX = new ArrayList<Point>(points);
        this.sortedByY = new ArrayList<Point>(points);
        
        Collections.sort(this.sortedByX, new Point.XCoordinateOrder());
        Collections.sort(this.sortedByY, new Point.YCoordinateOrder());        
    }
    
    public void generateResults() 
    {
        this.closestPoint(this.sortedByX, this.sortedByY, 0, this.sortedByX.size() - 1);        
    }
    
    /**
     * 
     * @param sortedByX
     * @param sortedByY
     * @param first
     * @param last
     * @return
     */
    public double closestPoint(List<Point> sortedByX, List<Point> sortedByY, int first, int last) 
    {
        double min = Double.POSITIVE_INFINITY;
        int numPoints = (last - first) + 1;
        int middle = first + (last - first)/2;
        double narrowBandDistance = Double.POSITIVE_INFINITY;
        double smin = Double.POSITIVE_INFINITY;
        
        Point midLine = (Point)sortedByX.get(middle);
        
        // Base condition - if the number of points in the set is less than equal to 3 
        // then use brute force to find the minimum  distance.
        if (numPoints <= 3)
        {
            for (int i = first; i < last; ++i)
            {
                for (int j = i + 1; j <= last; ++j)
                {
                    double distance = distanceCalculator((Point)sortedByX.get(i), (Point)sortedByX.get(j));
                    if (distance < min)
                    {
                        min = distance;
                    }
                }
            }
            
        	displayPointRangeMinDistance(first, last, min);
            return min;
        }
        
        // Recursive call to get the minimum distance on the left and right sides.
        double leftMinDistance = closestPoint(sortedByX, sortedByY, first, middle);
        double rightMinDistance = closestPoint(sortedByX, sortedByY, middle+1, last);        
        
        min = Math.min(leftMinDistance, rightMinDistance);
        
        // This list will hold the points in the narrow band from middle line to +/- delta
        List<Point> narrowBand = new ArrayList<Point>();
        
        // Create the array with elements in the narrow band midLine +/- min
        for (Point pY : sortedByY) {
            if (Math.abs((pY.getXCoordinate() - midLine.getXCoordinate())) <= min) {
                narrowBand.add(pY);
            }
        }
           
        // To find the distance between closest points in the narrowband. 
        for (int i = 0; i < narrowBand.size(); i++)
        {
            for (int j = i + 1; j < i + 7 && j < narrowBand.size(); j++)
            {
                narrowBandDistance = distanceCalculator(narrowBand.get(i),narrowBand.get(j));

                if (narrowBandDistance < smin)
                {
                    smin = narrowBandDistance;
                }
            }
        }

        if (smin < min) {
        	displayPointRangeMinDistance(first, last, smin);
            return smin;
        } else {
        	displayPointRangeMinDistance(first, last, min);
            return min;
        }
        
    }
    
    public double distanceCalculator(Point p, Point q)
    {
        return Math.sqrt( 
                ((p.getXCoordinate() - q.getXCoordinate())*(p.getXCoordinate() - q.getXCoordinate())) 
                +
                ((p.getYCoordinate() - q.getYCoordinate())*(p.getYCoordinate() - q.getYCoordinate()))
            );
    }
    
    private void displayPointRangeMinDistance(int first, int last, double minDistance) {
        System.out.println("D[" + first + "," + last + "]: " + new DecimalFormat("#.####").format(minDistance));
        System.out.flush();
    }
    
}
