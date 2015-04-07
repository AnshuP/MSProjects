import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class GraphFileParser {

    public String[] names;

    public int[][] graph;

    public void parseGraphFile(String filename) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(filename));

        int numNodes = sc.nextInt();
        // Additionally also account for two extra nodes 
        // to be added: source and sink
        int totalNodes = numNodes + 2;
        names = new String[totalNodes];
        graph = new int[totalNodes][totalNodes];

        names[0] = "source";
        names[numNodes+1] = "sink";
        for (int i = 0; i < numNodes; i++) {
            // Collect names
            names[i+1] = sc.next();
            if (i < numNodes/2) {
                graph[0][i+1] = 1;
            }
            if (i >= numNodes/2) {
                graph[i+1][numNodes+1] = 1;
            }
        }

        for (String name : names) {
            System.out.print(name + " ");
        }

        int numEdges = sc.nextInt();
        sc.useDelimiter("\\s+");
        for (int i = 0; i < numEdges; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            System.out.println("Adding edge [" + u + "," + v + "]");
            graph[u][v] = 1;
        }
        sc.close();
    }

}
