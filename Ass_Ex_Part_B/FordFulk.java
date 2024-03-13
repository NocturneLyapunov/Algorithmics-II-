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

    private int numStudents;
    private int numProjects;
    private int numLecturers;

    /**
     * Instantiates a new FordFulk object.
     *
     * @param s the name of the input file
     */
    public FordFulk(String s) {
        filename = s; // store name of input file
    }

    public Network getNetwork() {
        return this.net;
    }

    /**
     * Read in network from file. See assessed exercise specification for the
     * file format.
     */
    public void readNetworkFromFile() {
        FileReader fr = null;
        Scanner in = null;
        boolean[] isSE;
        // open file with name given by filename
        try {
            try {
                fr = new FileReader(filename);
                in = new Scanner(fr);

                // get number of students
                String line = in.nextLine();
                this.numStudents = Integer.parseInt(line);
                // get number of projects
                line = in.nextLine();
                this.numProjects = Integer.parseInt(line);
                line = in.nextLine();
                // get number of lecturers
                this.numLecturers = Integer.parseInt(line);

                //calculate total vertices
                int numTotalVertices = numStudents + numProjects + numLecturers + 2;

                //initialise array to store what is SE
                isSE = new boolean[numStudents + numProjects + 1];

                //we want to keep the index of the array the same as the label name for students and projects so set 0 (the source) to be false (not an SE student)
                isSE[0] = false;

                // create new network with desired number of vertices
                net = new Network(numTotalVertices);

                // now add the edges between the (source and students) and (students and projects) without distinguishing between SE and non SE
                for(int i = 0; i < numStudents; i++) {
                    line = in.nextLine();
                    String[] tokens = line.split(" ");

                    //get student label
                    int label = Integer.parseInt(tokens[0]);

                    //set if student is SE
                    isSE[label] = (tokens[1].equals("Y"))? true : false;

                    //get student vertex
                    Vertex student = net.getVertexByIndex(label);

                    int j = 2;
                    while (j < tokens.length) {
                        // get label of project
                        int projectLabel = Integer.parseInt(tokens[j++]) + numStudents;
                        // get corresponding Vertex object
                        Vertex project = net.getVertexByIndex(projectLabel);
                        //get source
                        Vertex source = net.getSource();

                        //add edge from source to student
                        net.addEdge(source, student, 1);
                        // add edge (student, project) with capacity 1 to network
                        net.addEdge(student, project, 1);
                    }

                }
                //add edges between projects and lecturers
                for(int i = numStudents; i < numStudents + numProjects; i++) {
                    line = in.nextLine();
                    String[] tokens = line.split(" ");
                    //get project label
                    int label = Integer.parseInt(tokens[0]) + numStudents;
                    //store if project is SE
                    isSE[label] = (tokens[1].equals("Y"))? true : false;

                    //get project vertex
                    Vertex project = net.getVertexByIndex(label);

                    // get label of lecturer
                    int lecturerLabel = Integer.parseInt(tokens[2]) + numStudents + numProjects;
                    // get lecturer vertex
                    Vertex lecturer = net.getVertexByIndex(lecturerLabel);
                    //get capacity of project
                    int capacity = Integer.parseInt(tokens[3]);


                    // add edge (project, lecturer) with capacity of project to network
                    net.addEdge(project, lecturer, capacity);
                }

                //add edges from lecturers to target
                while (in.hasNextLine()) {
                    line = in.nextLine();
                    String[] tokens = line.split(" ");

                    // get lecturer label
                    int label = Integer.parseInt(tokens[0]) + numStudents + numProjects;

                    // get corresponding Vertex object
                    Vertex lecturer = net.getVertexByIndex(label);

                    // get capacity of lecturer
                    int capacity = Integer.parseInt(tokens[1]);
                    // get sink Vertex object
                    Vertex sink = net.getSink();
                    // add edge (lecturer, target) with capacity c to network
                    net.addEdge(lecturer, sink, capacity);

                }
                //remove unwanted edges for illegal projects between SE students and non-SE projects
                for(int i = 1; i < numStudents + 1; i++) {
                    if(isSE[i]) {
                        Vertex SEstudent = net.getVertexByIndex(i);
                        for(int j = 0; j < net.getAdjList(SEstudent).size(); j++) {
                            Vertex project = net.getAdjList(SEstudent).get(j);
                            int projectLabel = project.getLabel();
                            if(!isSE[projectLabel]) {
                                Edge edgeToDelete = net.getAdjMatrixEntry(SEstudent, project);
                                edgeToDelete.setCap(0); //effectively delete this edge by setting capacity to 0
                            }
                        }
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
     * Gets if an s should be added to student or not depending if there is one student or not
     *
     * @param edge
     * @return character to add to end of student
     */
    private String edgeToString(Edge edge) {
        if(edge.getFlow() == 1)
            return "";
        else
            return "s";
    }

    /**
     * Print the results of the execution of the Ford-Fulkerson algorithm.
     */
    public void printResults() {
        if(net.isFlow()){
            //looping through all vertices in the graph except source and sink
            for(int vertexLabel = 1; vertexLabel < net.getNumVertices() - 1; vertexLabel++) {

                Vertex sourceVertex = net.getVertexByIndex(vertexLabel);
                LinkedList<Vertex> adjacentVertices = net.getAdjList(sourceVertex);

                if(vertexLabel <= this.numStudents) {
                    boolean studentProjectAssign = false;
                    for(int j = 0; j < adjacentVertices.size(); j++) {
                        Vertex adjacentVertex = adjacentVertices.get(j);
                        if(net.getAdjMatrixEntry(sourceVertex, adjacentVertex).getFlow() == 1) {
                            System.out.printf("Student %d is assigned to project %d%n",
                                    sourceVertex.getLabel(),
                                    adjacentVertex.getLabel() - this.numStudents
                            );
                            studentProjectAssign = true;
                            break;
                        }
                    }
                    if(!studentProjectAssign) { //could replace with if j is at the end of range in for loop
                        System.out.printf("Student %d is unassigned%n",
                                sourceVertex.getLabel()
                        );
                    }
                }

                else  {
                    Vertex adjacentVertex = adjacentVertices.get(0);
                    Edge edge = net.getAdjMatrixEntry(sourceVertex, adjacentVertex);
                    if(vertexLabel <= this.numProjects + this.numStudents) {

                        System.out.printf("Project %d with capacity %d is assigned %d student%s%n",
                                sourceVertex.getLabel() - numStudents,
                                edge.getCap(),
                                edge.getFlow(),
                                edgeToString(edge)
                        );
                    }
                    else {
                        System.out.printf("Lecturer %d with capacity %d is assigned %d student%s%n",
                                sourceVertex.getLabel() - numStudents - numProjects,
                                edge.getCap(),
                                edge.getFlow(),
                                edgeToString(edge)
                        );
                    }
                }
                if(vertexLabel == this.numStudents || vertexLabel == this.numProjects + this.numStudents || vertexLabel == this.numProjects + this.numStudents + this.numLecturers)
                    System.out.println();

            }
        } else {
            System.out.println("The assignment is not a valid flow");
        }
    }
}