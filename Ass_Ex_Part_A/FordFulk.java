import networkFlow.Edge;
import networkFlow.ResidualGraph;
import networkFlow.Vertex;
import networkFlow.Network;
import java.util.*;
import java.io.*;

/**
 * The Class FordFulk. Contains main part of the Ford-Fulkerson implementation
 * and code for file input
 */
public class FordFulk {

    /**
     * The name of the file that encodes the given network.
     */
    private final String filename;

    /**
     * The network on which the Ford-Fulkerson algorithm is to be run.
     */
    private Network net;

    /**
     * Instantiates a new FordFulk object.
     *
     * @param s the name of the input file
     */
    public FordFulk(String s) {
        filename = s; // store name of input file
    }

    /**
     * Read in network from file. See assessed exercise specification for the
     * file format.
     */
    public void readNetworkFromFile() {
        FileReader fr = null;
        Scanner in = null;
        // open file with name given by filename
        try {
            try {
                fr = new FileReader(filename);
                in = new Scanner(fr);

                // get number of vertices
                String line = in.nextLine();
                int numVertices = Integer.parseInt(line);

                // create new network with desired number of vertices
                net = new Network(numVertices);

                // now add the edges
                while (in.hasNextLine()) {
                    line = in.nextLine();
                    String[] tokens = line.split("[( )]+");
                    // this line corresponds to add vertices adjacent to vertex u
                    int u = Integer.parseInt(tokens[0]);
                    // get corresponding Vertex object
                    Vertex uu = net.getVertexByIndex(u);
                    int i = 1;
                    while (i < tokens.length) {
                        // get label of vertex v adjacent to u
                        int v = Integer.parseInt(tokens[i++]);
                        // get corresponding Vertex object
                        Vertex vv = net.getVertexByIndex(v);
                        // get capacity c of (uu,vv)
                        int c = Integer.parseInt(tokens[i++]);
                        // add edge (uu,vv) with capacity c to network 
                        net.addEdge(uu, vv, c);
                    }
                }
            } finally {
                if (fr != null) {
                    fr.close();
                }
                if (in != null) {
                    in.close();
                }
            }
        } catch (IOException e) {
            System.err.println("IO error:");
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Executes Ford-Fulkerson algorithm on the constructed network net.
     */
    public void fordFulkerson() {
        while(true){

            ResidualGraph residualGraph = new ResidualGraph(net);
            LinkedList<Edge> augmentingPath = residualGraph.findAugmentingPath();
            if(augmentingPath != null) {
                net.augmentPath(augmentingPath);
            }
            else {
                break;
            }

        }
    }

    /**
     * Get the maximum flow in the network. If fordFulkerson has not been
     * called, the return value of this function is zero.
     *
     * @return the flow in the network.
     */
    public int getFlow() {
        return net.getValue();
    }

    /**
     * Print the results of the execution of the Ford-Fulkerson algorithm.
     */
    public void printResults() {
        if (net.isFlow()) {
            System.out.println("The assignment is a valid flow");
            System.out.println("A maximum flow has value: " + getFlow());
            System.out.println("The flows along the edges are as follows:");
            net.printFlow();
        } else {
            System.out.println("The assignment is not a valid flow");
        }
    }
}