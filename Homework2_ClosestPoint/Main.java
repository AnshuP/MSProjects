import java.util.List;

/**
 * Main class
 * This class instantiates PointsFileParser and invokes the parsePoints method 
 * on it by passing in the name of the file that contains points data
 * It then instantiates the ClosestPair class that contains the algorithm to 
 * solve Closest Pair of Points problem as described in the homework
 * 
 * @author Anshu
 *
 */
public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception 
    {
    	PointsFileParser pointsFileParser = new PointsFileParser();
    	List<Point> points = pointsFileParser.parsePoints("program2data.txt");
        ClosestPair cp = new ClosestPair(points);
        cp.generateResults();
    }

}
