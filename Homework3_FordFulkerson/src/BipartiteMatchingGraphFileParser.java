import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class parses the input file containing the bipartite
 * graph data
 * 
 * @author Anshu
 *
 */
public class BipartiteMatchingGraphFileParser {

    private String[] nodeNames;

    private int[][] graphAdjacencyMatrix;

    /**
     * Parses input bipartite graph file
     * 
     * Also sets up the source and sink nodes and adds appropriate
     * edges from/to the source and sink
     * 
     * @param filename
     * @throws FileNotFoundException
     */
    public void parseGraphFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));

        int numNodes = sc.nextInt();
        // Additionally also account for two extra nodes 
        // to be added: source and sink
        int totalNodes = numNodes + 2;
        nodeNames = new String[totalNodes];
        graphAdjacencyMatrix = new int[totalNodes][totalNodes];

        nodeNames[0] = "source";
        nodeNames[numNodes+1] = "sink";
        for (int i = 0; i < numNodes; i++) {
            // Collect names
            nodeNames[i+1] = sc.next();
            if (i < numNodes/2) {
                graphAdjacencyMatrix[0][i+1] = 1;
            }
            if (i >= numNodes/2) {
                graphAdjacencyMatrix[i+1][numNodes+1] = 1;
            }
        }

        // Uncomment the below block to print all node names
        // for (String name : nodeNames) {
        //    System.out.print(name + " ");
        // }

        if (sc.hasNextInt()) {
            int numEdges = sc.nextInt();
            sc.useDelimiter("\\s+");
            for (int i = 0; i < numEdges; i++) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                // Uncommet the below line to see all the edges that are being added
                // System.out.println("Adding edge [" + u + "," + v + "]");
                graphAdjacencyMatrix[u][v] = 1;
            }
            sc.close();
        }
    }

    public String[] getNodeNames() {
        return nodeNames;
    }

    public int[][] getGraphAdjacencyMatrix() {
        return graphAdjacencyMatrix;
    }

}
