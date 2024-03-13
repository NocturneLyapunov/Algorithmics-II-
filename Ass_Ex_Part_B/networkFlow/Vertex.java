package networkFlow;

/**
 * The Class Vertex. Represents a vertex in a directed graph.
 */
public class Vertex {

    /**
     * Stores an integer label associated with the vertex.
     */
    protected int label;

    /**
     * Instantiates a new vertex.
     *
     * @param i the vertex label
     */
    public Vertex(int i) {
        label = i;
    }

    /**
     * Gets the vertex label.
     *
     * @return the vertex label
     */
    public int getLabel() {
        return label;
    }
}
