import java.io.FileNotFoundException;

/**
 * This class contains the main method
 * 
 * @author Anshu
 *
 */
public class Homework3 {

    /**
     * It uses the BipartiteMatchingGraphFileParser to parse the 
     * input graph file
     * 
     * It then invokes the match method of BipartiteMatching by passing
     * in the parsed input graph data
     * 
     * Finally it prints the matchings
     * @param args
     * @throws FileNotFoundException 
     */
    public static void main(String[] args) throws FileNotFoundException {
        BipartiteMatchingGraphFileParser bipartiteMatchinggraphFileParser 
            = new BipartiteMatchingGraphFileParser();
        bipartiteMatchinggraphFileParser.parseGraphFile("program3data.txt");
        String[] nodeNames = bipartiteMatchinggraphFileParser.getNodeNames();
        int[][] adjacencyMatrix = bipartiteMatchinggraphFileParser.getGraphAdjacencyMatrix();
        BipartiteMatching bipartiteMatching = new BipartiteMatching();
        int[] finalMatches = bipartiteMatching.match(nodeNames.length, adjacencyMatrix);
        
        // Final matching is printed
        for (int i = 0; i < finalMatches.length; i++) {
            if (finalMatches[i] != -1) {
                System.out.println(nodeNames[i] + " / " + nodeNames[finalMatches[i]]);
            }
        }
    }

}
