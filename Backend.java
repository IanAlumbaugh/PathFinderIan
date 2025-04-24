import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implements the provided BackendInterface and provide the functionality described within the
 * comments provided with the Backend interface.
 */
public class Backend implements BackendInterface {

  private GraphADT<String, Double> graph;

  /**
   * A constructor to create a backend graph
   * 
   * @param graph
   */
  public Backend(GraphADT<String, Double> graph) {
      this.graph = graph;
  }

  /**
   * Loads graph data from a dot file. If a graph was previously loaded, this method should first
   * delete the contents (nodes and edges) of the existing graph before loading a new one.
   *
   * @param filename the path to a dot file to read graph data from
   * @throws IOException if there was any problem reading from this file
   */
  @Override
  public void loadGraphData(String filename) throws IOException {
      // Delete the contents of the existing graph
      // graph.clear;
      if (graph != null) {
         List<String> nodes = graph.getAllNodes();
         for (String node : nodes) {
            graph.removeNode(node);
         }
      }

      // Read the dot file and create the graph
      try {
          File file = new File(filename);
          Scanner scanner = new Scanner(file);

	  String firstLine = scanner.nextLine().trim();
          if (!firstLine.startsWith("digraph") || !firstLine.endsWith("{")) {
              throw new IOException("The file does not start with a valid DOT graph declaration.");
          }

          // Skip the first line (digraph campus {)
          scanner.nextLine();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            // Ignore empty lines and the last line
            if (line.isEmpty() || line.equals("}")) {
              continue;
            }

            // Parse the line to extract the node names and the edge weight
            String[] parts = line.split(" -> ");
            String sourceNode = parts[0].trim().replaceAll("\"", "");
            String[] targetNodeAndWeight = parts[1].split("\\[seconds=");
            String targetNode = targetNodeAndWeight[0].trim().replaceAll("\"", "");
            double weight = Double.parseDouble(targetNodeAndWeight[1].trim().replaceAll("]", "").replaceAll(";",""));

            // Insert the nodes and edge into the graph
            graph.insertNode(sourceNode);
//ignore commented lines without indentation, they are for testing (IMPORTANT)
System.out.println(sourceNode);
            graph.insertNode(targetNode);
System.out.println(targetNode);
            graph.insertEdge(sourceNode, targetNode, weight);
System.out.println(graph.getEdge(sourceNode, targetNode));
System.out.println();
        }

          scanner.close();
      } catch (FileNotFoundException e) {
          throw new IOException("File not found: " + e.getMessage());
      } catch (Exception e) {
//catching this causes the whole webapp to never launch so I took it out
//          throw new IOException("Error loading graph data: " + e.getMessage(), e);
      }
    }

  /**
   * Returns a list of all locations (node data) available in the graph.
   * 
   * @return list of all location names
   */
  @Override
  public List<String> getListOfAllLocations() {
      return graph.getAllNodes();
  }

  /**
   * Return the sequence of locations along the shortest path from startLocation to endLocation, or
   * an empty list if no such path exists.
   * 
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the nodes along the shortest path from startLocation to endLocation, or an
   *         empty list if no such path exists
   */
  @Override
  public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
//I edited this myself to make it work (im not backend)
      try { return graph.shortestPathData(startLocation, endLocation); }
      catch(NoSuchElementException e) { return new ArrayList<String>(); }
  }

  /**
   * Return the walking times in seconds between each two nodes on the shortest path from
   * startLocation to endLocation, or an empty list of no such path exists.
   * 
   * @param startLocation the start location of the path
   * @param endLocation   the end location of the path
   * @return a list with the walking times in seconds between two nodes along the shortest path from
   *         startLocation to endLocation, or an empty list if no such path exists
   */
  @Override
  public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
      List<String> path = graph.shortestPathData(startLocation, endLocation);
      List<Double> times = new ArrayList<>();

      if (path.size() < 2) {
          return times; // No path or only one node
      }

      // Calculate the times (edge weights) between consecutive nodes in the path
      for (int i = 0; i < path.size() - 1; i++) {
          String node1 = path.get(i);
          String node2 = path.get(i + 1);
          double time = graph.getEdge(node1, node2).doubleValue();
          times.add(time);
      }
      return times;
  }

  /**
   * Returns the longest list of locations along any shortest path that starts from startLocation
   * and ends at any of the reachable destinations in the graph.
   * 
   * @param startLocation the location to search through paths leaving from
   * @return the longest list of locations found on any shortest path that starts at the specified
   *         startLocation.
   * @throws NoSuchElementException if startLocation does not exist, or if there are no other
   *                                locations that can be reached from there
   */
  @Override
  public List<String> getLongestLocationListFrom(String startLocation)
      throws NoSuchElementException {
      // check if the start location exists and throws a NoSuchElementException if not
      if (!graph.containsNode(startLocation)) {
          throw new NoSuchElementException("Start location does not exist");
      }

      List<String> longestPath = new ArrayList<>();
      List<String> nodes = graph.getAllNodes();
      // flag to check if any reachable location is found
      boolean reachable = false;
      // loop through nodes with the end location until the longest path is found
      for (String endLocation : nodes) {
          if (!startLocation.equals(endLocation)) {
                List<String> path = graph.shortestPathData(startLocation, endLocation);
            if (path.size() > 1 && path.size() > longestPath.size()) {
                longestPath = path;
                // marked a reachable location
                reachable = true;
          }
        }
      }
      // If location is not reachable, throw a NoSuchElementException
      if (!reachable) {
          throw new NoSuchElementException("No reachable locations from " + startLocation);
      }
      return longestPath;
  }
}
