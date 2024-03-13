package networkFlow;


import java.util.*;

/**
 * The Class Network. Represents a network - inherits from DirectedGraph class.
 */
public class Network extends DirectedGraph {

    /**
     * The source vertex of the network.
     */
    protected Vertex source;

    /**
     * The label of the source vertex.
     */
    protected int sourceLabel;

    /**
     * The sink vertex of the network.
     */
    protected Vertex sink;

    /**
     * The label of the sink vertex.
     */
    protected int sinkLabel;

    /**
     * Instantiates a new network.
     *
     * @param n the number of vertices
     */
    public Network(int n) {
        super(n);

        // add the source vertex - assumed to have label 0
        sourceLabel = 0;
        source = addVertex(sourceLabel);
        // add the sink vertex - assumed to have label numVertices - 1
        sinkLabel = numVertices - 1;
        sink = addVertex(sinkLabel);

        // add the remaining vertices
        for (int i = 1; i <= numVertices - 2; i++) {
            addVertex(i);
        }
    }

    /**
     * Gets the source vertex.
     *
     * @return the source vertex
     */
    public Vertex getSource() {
        return source;
    }

    /**
     * Gets the sink vertex.
     *
     * @return the sink vertex
     */
    public Vertex getSink() {
        return sink;
    }

    /**
     * Adds the edge with specified source and target vertices and capacity.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param capacity the capacity of the edge
     */
    public void addEdge(Vertex sourceEndpoint, Vertex targetEndpoint, int capacity) {
        Edge e = new Edge(sourceEndpoint, targetEndpoint, capacity);
        adjLists.get(sourceEndpoint.getLabel()).addLast(targetEndpoint);
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()] = e;
    }

    /**
     * Set the flow on a given edge. This does not, and should not, do any
     * checking for validity of the input flow.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @param flow the flow of the edge
     */
    public void setFlow(Vertex sourceEndpoint, Vertex targetEndpoint, int flow) {
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].setFlow(flow);
    }

    /**
     * Get the capacity along a given edge.
     *
     * @param sourceEndpoint the source endpoint vertex
     * @param targetEndpoint the target endpoint vertex
     * @return the capacity of the given edge
     */
    public int getEdgeCapacity(Vertex sourceEndpoint, Vertex targetEndpoint) {
        return adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()].getCap();
    }

    /**
     * Calculates by how much the flow along the given path can be increased,
     * and then augments the network along this path by this amount.
     *
     * @param path a list of edges along which the flow should be augmented
     */
    public void augmentPath(List<Edge> path) {

        int minimumCapacity = Integer.MAX_VALUE;
        for(Edge edge : path){
            if(edge.getCap() < minimumCapacity)
                minimumCapacity = edge.getCap();
        }
        for(Edge edge : path){
            Vertex source = edge.getSourceVertex();
            Vertex target = edge.getTargetVertex();
            Edge networkEdge = this.getAdjMatrixEntry(source, target);
            if(this.getAdjList(source).contains(target) && networkEdge.getFlow() + minimumCapacity <= networkEdge.getCap()){
                setFlow(source, target, networkEdge.getFlow() + minimumCapacity);
            } else {
                setFlow(target, source, this.getAdjMatrixEntry(target, source).getFlow() - minimumCapacity);
            }
        }

    }

    /**
     * Returns true if and only if the assignment of integers to the flow fields
     * of each edge in the network is a valid flow.
     *
     * @return true, if the assignment is a valid flow
     */
    public boolean isFlow() {
        Boolean isFlow = true;

        for(int i = 0; i < adjMatrix.length; i++) {
            for(int j = 0; j < adjMatrix[i].length; j++) {
                if((adjMatrix[i][j] != null) && (adjMatrix[i][j].getFlow() > adjMatrix[i][j].getCap())) {
                    return false;
                }
            }
        }


        // check there is no incoming flow to the source vertex
        int inFlow = 0;
        for (int j = 0; j < adjMatrix.length; j++) {
            if(adjMatrix[j][source.getLabel()] != null) {
                inFlow += adjMatrix[j][source.getLabel()].getFlow();
            }
        }
        if(inFlow != 0) {
            return false;
        }

        //check no outgoing flow from the sink vertex


        int outFlow = 0;
        for (int j = 0; j < adjMatrix[sink.getLabel()].length; j++) {
            if(adjMatrix[sink.getLabel()][j] != null) {
                outFlow += adjMatrix[sink.getLabel()][j].getFlow();
            }
        }
        if(outFlow != 0) {
            return false;
        }
        int vertexLabel;
        int i = 1;
        while (i < vertices.length - 1) {
            vertexLabel = vertices[i].getLabel();

            outFlow = 0;
            for (int adjVertex = 0; adjVertex < adjMatrix[vertexLabel].length; adjVertex++) {
                if(adjMatrix[vertexLabel][adjVertex] != null) {
                    outFlow += adjMatrix[vertexLabel][adjVertex].getFlow();
                }
            }

            inFlow = 0;
            for (Edge[] matrix : adjMatrix) {
                if (matrix[vertexLabel] != null) {
                    inFlow += matrix[vertexLabel].getFlow();
                }
            }
            if(inFlow != outFlow) {
                return false;
            }
            i++;
        }
        return isFlow;
    }

    /**
     * Gets the value of the flow.
     *
     * @return the value of the flow
     */
    public int getValue() {
        int value = 0;

        ArrayList<Edge> edges = new ArrayList<>();

        for (Vertex vertex: adjLists.get(source.getLabel())) {
            edges.add(adjMatrix[source.getLabel()][vertex.getLabel()]);
        }

        for (Edge edge : edges) {
            value += edge.getFlow();
        }

        return value;
    }

    /**
     * Prints the flow. Display the flow through the network in the following
     * format: (u,v) c(u,v)/f(u,v) where (u,v) is an edge, c(u,v) is the
     * capacity of that edge and f(u,v) is the flow through that edge - one line
     * for each edge in the network
     */
    public void printFlow() {
        Set<Edge> flowEdges = new HashSet<>();

        for(int vertexLabel = 0; vertexLabel<adjLists.size(); vertexLabel++){
            for(Vertex adjVertex : adjLists.get(vertexLabel)){
                Edge currentEdge = adjMatrix[vertexLabel][adjVertex.getLabel()];
                if(currentEdge != null && flowEdges.add(currentEdge)){
                    System.out.printf("(%d,%d) %d/%d%n", vertexLabel, adjVertex.getLabel(), currentEdge.getCap(), currentEdge.getFlow());
                }
            }
        }
    }
}