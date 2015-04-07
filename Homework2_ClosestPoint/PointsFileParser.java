import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * PointsFileParser class
 * This class parses the input file containing the number of points followed 
 * by the points coordinates
 * An example of a valid file format is:
 * 8
 *  0.1251 56.3585
 * 19.3304 80.8741
 * 58.5009 47.9873
 * 35.0291 89.5962
 * 82.2840 74.6605
 * 17.4108 85.8943
 * 71.0501 51.3535
 * 30.3995  1.4985
 * 
 * where the first line indicates the number of points on the following lines.
 * The example file above has '8' points
 * The points themselves are represented as X and Y coordinates with a space between them 
 * "X-coordinate Y-coordinate". Leading/trailing spaces around the X/Y coordinates are
 * ignored
 * 
 * @author Anshu
 *
 */
public class PointsFileParser {

	public List<Point> parsePoints(String filename) throws Exception 
	{
		String currentLine;
		ArrayList<String> fileData = new ArrayList<String>();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(filename));
			while ((currentLine = br.readLine()) != null) {
				fileData.add(currentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				br.close();
			}
		}

		int numPoints = Integer.parseInt(fileData.get(0).toString());
		if (fileData.size() != numPoints + 1) {
			throw new Exception("Invalid input file");            
		}

		List<Point> points = new ArrayList<Point>();

		for (int i = 1; i <= numPoints; i++) {
			String pointXYString = fileData.get(i).trim();
			String[] XYentries = pointXYString.split("\\s+");
			
			if (XYentries.length != 2) {
				throw new Exception("Invalid input file");
			}
			
			double Xvalue = Double.parseDouble(XYentries[0]);
			double Yvalue = Double.parseDouble(XYentries[1]);
			Point point = new Point(Xvalue, Yvalue);
			points.add(point);
		}

		return points;
	}
	
}
