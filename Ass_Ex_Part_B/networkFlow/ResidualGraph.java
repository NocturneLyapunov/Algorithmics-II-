package networkFlow;

import java.util.*;

/**
 * The Class ResidualGraph. Represents the residual graph corresponding to a
 * given network.
 */
import java.util.*;

/**
 * The Class ResidualGraph. Represents the residual graph corresponding to a
 * given network.
 */
public class ResidualGraph extends Network {

    /**
     * Instantiates a new ResidualGraph object. Builds the residual graph
     * corresponding to the given network net. Residual graph has the same
     * number of vertices as net.
     *
     * @param net the network
     */
    public ResidualGraph(Network net) {
        super(net.numVertices);
        Edge[][] residualAdjMatrix = net.adjMatrix;
        for (Vertex vertex : net.vertices) {
            for (Vertex u : net.getAdjList(vertex)) {
                int residualCapacity = residualAdjMatrix[vertex.getLabel()][u.getLabel()].getCap() - residualAdjMatrix[vertex.getLabel()][u.getLabel()].getFlow();
                if (residualCapacity > 0) {
                    addEdge(vertex, u, residualCapacity);
                }
                if (residualAdjMatrix[vertex.getLabel()][u.getLabel()].getFlow() > 0) {
                    addEdge(u, vertex, residualAdjMatrix[vertex.getLabel()][u.getLabel()].getFlow());
                }
            }
        }
    }




    /**
     * Find an augmenting path if one exists. Determines whether there is a
     * directed path from the source to the sink in the residual graph -- if so,
     * return a linked list containing the edges in the augmenting path in the
     * form (s,v_1), (v_1,v_2), ..., (v_{k-1},v_k), (v_k,t); if not, return an
     * empty linked list.
     *
     * @return the linked list
     */
    public LinkedList<Edge> findAugmentingPath() {

        Vertex currentVertex = source;
        LinkedList<Edge> augmentingPath = new LinkedList<>();
        Queue <Vertex> possiblePath = new LinkedList<>();
        Vertex[] predecessors = new Vertex[vertices.length];
        boolean foundTarget = false;


        while ((currentVertex.getLabel() != adjLists.size() - 1) && !foundTarget) {
            for (int i = 0; i < adjLists.get(currentVertex.getLabel()).size(); i++) {
                Vertex linkedVertex = adjLists.get(currentVertex.getLabel()).get(i);
                if(predecessors[linkedVertex.getLabel()] != null) {
                    continue;
                }
                possiblePath.add(linkedVertex);
                predecessors[linkedVertex.getLabel()] = currentVertex;
                if(linkedVertex.getLabel() == sink.getLabel()) {
                    foundTarget = true;
                    break;
                }
            }


            if(possiblePath.isEmpty()) {
                return null;
            }
            else {
                currentVertex = possiblePath.remove();
            }
        }


        Stack<Edge> reversePath = new Stack<>();
        Vertex current;
        current = vertices[sink.getLabel()];
        Vertex predecessor;
        predecessor = predecessors[current.getLabel()];

        while(true) {
            reversePath.push(adjMatrix[predecessor.getLabel()][current.getLabel()]);
            current = predecessor;
            if(current.getLabel() == 0) {
                break;
            }
            predecessor = predecessors[current.getLabel()];
        }

        while(!reversePath.isEmpty()) {
            augmentingPath.add(reversePath.pop());
        }

        return augmentingPath;
    }
}