public class Main {

    /**
     * The main method.
     *
     * @param args the name of the file containing the input network.
     */
    public static void main(String args[]) {

        // construct FordFulk object passing filename to constructor
        FordFulk fordFulk = new FordFulk(args[0]);

        // read network from file given by first argument
        fordFulk.readNetworkFromFile();

        // apply Ford-Fulkerson algorithm to constructed network
        fordFulk.fordFulkerson();

        // check whether the assignment is a valid flow
        fordFulk.printResults();
    }
}
