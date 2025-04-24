// === CS400 File Header Information ===
// Name: <Ian Alumbaugh>
// Email: <alumbaugh@wisc.edu>
// Group and Team: <P2.3627>
// Lecturer: <Florian>
// Notes to Grader: <optional extra notes>

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.PriorityQueue;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class extends the BaseGraph data structure with additional methods for
 * computing the total cost and list of node data along the shortest path
 * connecting a provided starting to ending nodes. This class makes use of
 * Dijkstra's shortest path algorithm.
 */
public class DijkstraGraph<NodeType, EdgeType extends Number>
        extends BaseGraph<NodeType, EdgeType>
        implements GraphADT<NodeType, EdgeType> {

    /**
     * While searching for the shortest path between two nodes, a SearchNode
     * contains data about one specific path between the start node and another
     * node in the graph. The final node in this path is stored in its node
     * field. The total cost of this path is stored in its cost field. And the
     * predecessor SearchNode within this path is referened by the predecessor
     * field (this field is null within the SearchNode containing the starting
     * node in its node field).
     *
     * SearchNodes are Comparable and are sorted by cost so that the lowest cost
     * SearchNode has the highest priority within a java.util.PriorityQueue.
     */
    protected class SearchNode implements Comparable<SearchNode> {
        public Node node;
        public double cost;
        public SearchNode predecessor;

        public SearchNode(Node node, double cost, SearchNode predecessor) {
            this.node = node;
            this.cost = cost;
            this.predecessor = predecessor;
        }

        public int compareTo(SearchNode other) {
            if (cost > other.cost)
                return +1;
            if (cost < other.cost)
                return -1;
            return 0;
        }
    }

    /**
     * Constructor that sets the map that the graph uses.
     */
    public DijkstraGraph() {
        super(new HashtableMap<NodeType, Node>());
    }

    /**
     * This helper method creates a network of SearchNodes while computing the
     * shortest path between the provided start and end locations. The
     * SearchNode that is returned by this method is represents the end of the
     * shortest path that is found: it's cost is the cost of that shortest path,
     * and the nodes linked together through predecessor references represent
     * all of the nodes along that shortest path (ordered from end to start).
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return SearchNode for the final end node within the shortest path
     * @throws NoSuchElementException when no path from start to end is found
     *                                or when either start or end data do not
     *                                correspond to a graph node
     */
    protected SearchNode computeShortestPath(NodeType start, NodeType end) {
	//initialize the queue of paths taken, searchnode, and placeholdermap indicating if they have been visited
	PriorityQueue<SearchNode> paths = new PriorityQueue<SearchNode>();
	HashtableMap visitedNodes = new HashtableMap();
	SearchNode search = null;

	//if the start provided is valid, add it to the path queue and mark it as visited
	//otherwise, throw a NoSuchElementException
	if(nodes.containsKey(start)) {
		search = new SearchNode(nodes.get(start), 0, null);
		paths.add(search);
	} else { throw new NoSuchElementException(); }

	while(!paths.isEmpty()) {
		//take off min cost searchnode off priority queue, if its destination
		//matches the end we are looking for, return it
		search = paths.poll();
		if(search.node.data.equals(end))
			return search;
		//if the node hasnt been visited, mark it as visited and add all its outbound edges to priority queue
		if(!visitedNodes.containsKey(search.node)) {
			visitedNodes.put(search.node, 1);
			double cost = search.cost;
			for(Edge edges : search.node.edgesLeaving) {
				double edgeCost = edges.data.doubleValue();
				paths.add(new SearchNode(edges.successor, cost + edgeCost, search));
			}
		}
	}

	throw new NoSuchElementException();
    }

    /**
     * Returns the list of data values from nodes along the shortest path
     * from the node with the provided start value through the node with the
     * provided end value. This list of data values starts with the start
     * value, ends with the end value, and contains intermediary values in the
     * order they are encountered while traversing this shorteset path. This
     * method uses Dijkstra's shortest path algorithm to find this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return list of data item from node along this shortest path
     */
    public List<NodeType> shortestPathData(NodeType start, NodeType end) {
	//initialize the shortest path and a list of nodes on that path
        SearchNode nodes = computeShortestPath(start, end);
	List<NodeType> path = new LinkedList<NodeType>();
	path.add(nodes.node.data);

	//while predecessors exist on the path, add them to a list of nodes on that path
	while(nodes.predecessor != null) {
		path.addFirst(nodes.predecessor.node.data);
		nodes = nodes.predecessor;
	}

        return path;
    }

    /**
     * Returns the cost of the path (sum over edge weights) of the shortest
     * path freom the node containing the start data to the node containing the
     * end data. This method uses Dijkstra's shortest path algorithm to find
     * this solution.
     *
     * @param start the data item in the starting node for the path
     * @param end   the data item in the destination node for the path
     * @return the cost of the shortest path between these nodes
     */
    public double shortestPathCost(NodeType start, NodeType end) {
        return computeShortestPath(start, end).cost;
    }

   /**
    * makes a weighted graph we went over during lecture
    */
    private DijkstraGraph makeGraph() {
        DijkstraGraph test = new DijkstraGraph();
        test.insertNode("A");
        test.insertNode("B");
        test.insertNode("C");
        test.insertNode("D");
        test.insertNode("E");
        test.insertNode("F");
        test.insertNode("G");
        test.insertNode("H");
        test.insertEdge("A", "B", 4);
        test.insertEdge("A", "E", 15);
        test.insertEdge("A", "C", 2);
        test.insertEdge("B", "E", 10);
        test.insertEdge("B", "D", 1);
        test.insertEdge("C", "D", 5);
        test.insertEdge("D", "E", 3);
        test.insertEdge("D", "F", 0);
        test.insertEdge("F", "D", 2);
        test.insertEdge("F", "H", 4);
        test.insertEdge("G", "H", 4);
	return test;
    }

   /**
    * tests a dijkstra graph tracing we went over during lecture
    */
    @Test
    public void lectureTest() {
	//initalize testing graph
	DijkstraGraph test = makeGraph();

	//checks for proper pathing of the shortest path
	List<NodeType> path = test.shortestPathData("A", "H");
	if(path.get(0) != "A" || path.get(1) != "B" || path.get(2) != "D" || path.get(3) != "F" || path.get(4) != "H")
		 Assertions.fail("lectureTest() testing method failed when checking predecessors");

	//checks for proper cost of the shortest path
	if(Double.compare(test.shortestPathCost("A", "H"), 9) != 0)
		Assertions.fail("lectureTest() testing method failed when checking cost");
    }

   /**
    * tests running through the shortest path data for the above test's graph
    */
    @Test
    public void shortestPathLectureTest() {
	//initalize testing graph **I am using A for my starting node again since
	//in this lecture example (lecture 3 slides for Shortest Paths Week 9) starting at
	//any node other than A makes all shortest paths to another node pretty trivial
	DijkstraGraph test = makeGraph();

	//checks for proper pathing of the shortest path
	List<NodeType> path = test.shortestPathData("A", "E");
        if(path.get(0) != "A" || path.get(1) != "B" || path.get(2) != "D" || path.get(3) != "E") {
                 Assertions.fail("shortestPathLectureTest() testing method failed when checking predecessors");
        }

	//checks for proper cost of the shortest path
        if(Double.compare(test.shortestPathCost("A", "H"), 9) != 0)
                Assertions.fail("shortestPathLectureTest() testing method failed when checking cost");
    }

   /**
    * tests running through a path between two nodes that dont connect at all
    */
    @Test
    public void lostTest() {
	//intilialize testing graph and passing status
	DijkstraGraph test = makeGraph();
	boolean pass = false;

	//try to find an impossible path, if predicted exception is thrown pass the test
	try {
		SearchNode last = test.computeShortestPath("A", "G");
	}
	catch(NoSuchElementException e) {
		pass = true;
	}

	if(!pass)
		Assertions.fail("lostTest() testing method failed");
    }
}
