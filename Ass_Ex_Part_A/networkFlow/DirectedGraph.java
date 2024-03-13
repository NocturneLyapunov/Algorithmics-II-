package networkFlow;

import java.util.*;

/**
 * The Class DirectedGraph. Represents a directed graph using both adjacency
 * lists and an adjacency matrix.
 */
public class DirectedGraph {

    /**
     * Number of vertices in the graph.
     */
    protected int numVertices;

    /**
     * Defines the vertices in the graph.
     */
    protected Vertex[] vertices;

    /**
     * Defines the adjacency lists of the graph as an array, with an element for
     * each vertex v giving a linked list corresponding to v's adjacency list.
     */
    protected ArrayList<LinkedList<Vertex>> adjLists;

    /**
     * Defines the adjacency matrix of the graph as a matrix whose (i,j)th entry
     * is an edge - this is slightly non-standard but allows for efficient
     * look-up of flows and capacities.
     */
    protected Edge[][] adjMatrix;

    /**
     * Constructor for directed graph class.
     *
     * @param n the number of vertices in the graph
     */
    public DirectedGraph(int n) {
        // create the desired size of graph
        numVertices = n;

        vertices = new Vertex[numVertices];
        adjLists = new ArrayList<LinkedList<Vertex>>();
        adjMatrix = new Edge[numVertices][numVertices];

        // initialise all adjacency lists to be empty
        // initialise all entries in adjMatrix to be null
        for (int u = 0; u < numVertices; u++) {
            adjLists.add(new LinkedList<Vertex>());
            for (int v = 0; v < numVertices; v++) {
                adjMatrix[u][v] = null;
            }
        }
    }

    /**
     * Adds a new vertex to the graph with specified label.
     *
     * @param label the vertex label
     * @return the vertex
     */
    public Vertex addVertex(int label) {
        Vertex vv = new Vertex(label);
        vertices[label] = vv;
        return vv;
    }

    /**
     * Adds a new edge to the graph with specified source and target vertices
     *
     * @param sourceEndpoint the source vertex
     * @param targetEndpoint the target vertex
     */
    public void addEdge(Vertex sourceEndpoint, Vertex targetEndpoint) {
        Edge e = new Edge(sourceEndpoint, targetEndpoint);
        adjLists.get(sourceEndpoint.getLabel()).addLast(targetEndpoint);
        adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()] = e;
    }

    /**
     * Gets the number of vertices in the graph.
     *
     * @return the number of vertices in the graph
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Gets the adjacency list for a given vertex vv.
     *
     * @param vv the given vertex
     * @return the adjacency list
     */
    public LinkedList<Vertex> getAdjList(Vertex vv) {
        return adjLists.get(vv.getLabel());
    }

    /**
     * Gets the adjacency list for a given vertex with label v.
     *
     * @param v the vertex label
     * @return the adjacency list
     */
    public LinkedList<Vertex> getAdjListByLabel(int v) {
        return adjLists.get(v);
    }

    /**
     * Gets the adjacency matrix entry corresponding to vertices uu and vv.
     *
     * @param uu the first vertex
     * @param vv the second vertex
     * @return the adjacency matrix entry
     */
    public Edge getAdjMatrixEntry(Vertex uu, Vertex vv) {
        return adjMatrix[uu.getLabel()][vv.getLabel()];
    }

    /**
     * Gets the vertex at index i of the vertices array.
     *
     * @param i the index
     * @return the vertex at index i
     */
    public Vertex getVertexByIndex(int i) {
        return vertices[i];
    }
}
