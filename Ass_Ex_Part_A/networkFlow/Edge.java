package networkFlow;

/**
 * The Class Edge. Represents an edge (u,v) in a directed graph
 */
public class Edge {

    /**
     * The source endpoint of the edge. NB. the source endpoint of (uu,vv) is
     * uu.
     */
    private Vertex sourceEndpoint;

    /**
     * The target endpoint of the edge. NB. the target endpoint of (uu,vv) is
     * vv.
     */
    private Vertex targetEndpoint;

    /**
     * Stores the flow through the edge (uu,vv).
     */
    private int flow;

    /**
     * Stores the capacity of the edge (uu,vv).
     */
    private int capacity;

    /**
     * Instantiates a new edge. Constructor used by DirectedGraph class.
     *
     * @param uu the source vertex
     * @param vv the target vertex
     */
    public Edge(Vertex uu, Vertex vv) {
        sourceEndpoint = uu;
        targetEndpoint = vv;
    }

    /**
     * Instantiates a new edge. Constructor used by the Network class.
     *
     * @param uu the source vertex
     * @param vv the target vertex
     * @param c the capacity
     */
    public Edge(Vertex uu, Vertex vv, int c) {
        sourceEndpoint = uu;
        targetEndpoint = vv;
        flow = 0;
        capacity = c;
    }

    /**
     * Gets the source vertex.
     *
     * @return the source vertex
     */
    public Vertex getSourceVertex() {
        return sourceEndpoint;
    }

    /**
     * Gets the target vertex.
     *
     * @return the target vertex
     */
    public Vertex getTargetVertex() {
        return targetEndpoint;
    }

    /**
     * Gets the flow.
     *
     * @return the flow
     */
    public int getFlow() {
        return flow;
    }

    /**
     * Sets the flow.
     *
     * @param f the new flow
     */
    public void setFlow(int f) {
        flow = f;
    }

    /**
     * Gets the capacity.
     *
     * @return the capacity
     */
    public int getCap() {
        return capacity;
    }

    /**
     * Sets the capacity.
     *
     * @param c the new capacity
     */
    public void setCap(int c) {
        capacity = c;
    }
}
