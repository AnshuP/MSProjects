import java.util.LinkedList;

/**
 * This class contains the algorithm that finds 
 * the maximal matching in a bipartite graph
 * 
 * It uses the output of the BipartiteMatchingGraphFileParser
 * which while parsing the graph file sets up the source and 
 * sink nodes and appropriate edges from/to them
 * 
 * @author Anshu
 *
 */
public class BipartiteMatching {

    private int totalNodes; // includes source and sink node
    private int[][] G; // graph represented as adjacency matrix

    /**
     * Finds maximal matching for the bipartite graph
     * 
     * @param numNodes 
     *      The total number of nodes (incl. source and sink)
     * @param inputAdjancencyMatrix
     *      Graph adjacency matrix inclusive of source and sink nodes
     * @return maximal matching
     */
    public int[] match(int numNodes, int[][] inputAdjancencyMatrix) {
        G = inputAdjancencyMatrix;
        // names include node names from the input files + two additional nodes
        // "source" at [0] and "sink" at [totalNodes - 1]
        totalNodes = numNodes;
        int s = 0;
        int t = totalNodes - 1;

        int finalMatches[] = new int[totalNodes/2];
        for (int i = 0; i < finalMatches.length; i++) {
            finalMatches[i] = -1;
        }

        // If this gets set to true and stays that way 
        // we'll get the final matching
        boolean backToSource = false;
        while(!backToSource) {
            // Uncomment to print the residual graph
            // printG(G);
            int matches[] = new int[totalNodes/2];
            for (int i = 0; i < matches.length; i++) {
                matches[i] = -1;
            }

            int inPath[] = new int[totalNodes];
            for (int i = 0; i < inPath.length; i++) {
                inPath[i] = -1;
            }

            // BFS queue
            LinkedList<Integer> queue = new LinkedList<Integer>();
            queue.add(s);
            
            while(!queue.isEmpty()) {
                int u = queue.poll();
                if (inPath[u] == -1) {
                    // u's adjacents
                    int u_adjs[] = G[u];
                    backToSource = true;
                    for (int i = 0; i < u_adjs.length; i++) {
                        if (u_adjs[i] == 1) {
                            if (i == s) {
                                // We are getting back at source
                                backToSource = true;
                            }
                            else if (i != t) {
                                backToSource = false;
                                queue.add(i);
                            } 
                            else {
                                // i is the sink node
                                backToSource = false;
                                int v = u;

                                // Find v's parent who is not already 
                                // in one of the pairs in matches
                                while(v != s && v != t) {
                                    int j = 0;
                                    // Used below to traverse back up
                                    int prev = v;
                                    for (j = 0; j < G.length; j++) {
                                        // find which j has edge to v
                                        if (G[j][v] == 1 && j != t) {
                                            prev = j;
                                            // Note that due to edge reversals in
                                            // updated residual graph, it maybe the 
                                            // case that j > v but we are only interested
                                            // in collection left to right paths. Hence
                                            // the check for j < v
                                            if (j != s && j < v && matches[j] == -1) {
                                                matches[j] = v;
                                                inPath[j] = 1;
                                                inPath[v] = 1;
                                                // Uncomment the below to see matching after each iteration
                                                // System.out.println("Found match: " + j + " " + v);
                                                // Other than v, whatever child nodes j 
                                                // added in the BFS queue remove those
                                                // Note that if j added k to the BFS queue
                                                // and some other node x also had an edge to k
                                                // which would result in k being added again to the 
                                                // BFS queue when x was traversed
                                                // 
                                                // We will only remove the k which is ahead in the queue
                                                // (added by j)
                                                int[] j_adjs = G[j];
                                                for (int k = 0; k < j_adjs.length; k++) {
                                                    if (k == v)
                                                        continue;
                                                    if (j_adjs[k] == 1) {
                                                        queue.remove(new Integer(k));
                                                    }
                                                }
                                                prev = j;
                                                break;
                                            }
                                        }
                                    }
                                    v = prev;
                                }
                            }
                        }
                    }
                }
            }

            // Collect matches found in this phase
            // and update the residual graph
            for (int i = 0; i < matches.length; i++) {
                if (matches[i] != -1) {
                	// Uncomment to see all the matches (only indexes will be printed)
                    // System.out.println(i + "->" + matches[i]);
                    finalMatches[i] = matches[i];

                    // Update residual graph
                    G[i][matches[i]] = 0;
                    G[matches[i]][i] = 1;
                    G[s][i] = 0;
                    G[i][s] = 1;
                    G[matches[i]][t] = 0;
                    G[t][matches[i]] = 1;
                }
            }
        }

        return finalMatches;
    }

    /**
     * Helper method to print the graph adjacency matrix
     * @param G
     */
    public void printG(int[][] G) {
        System.out.print("  ");
        for(int i = 0; i < G.length; i++) {
            System.out.print(i + " ");
        }
        for(int i = 0; i < G.length; i++) {
            System.out.println();
            System.out.print(i + " ");
            int[] adjs = G[i];
            for(int j = 0; j < adjs.length; j++) {
                System.out.print(G[i][j] + " ");
            }
        }
        System.out.println();        
    }

}
