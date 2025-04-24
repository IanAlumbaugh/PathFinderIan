import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * This class tests the Backend class.
 */
public class BackendTests {

  /**
   * Test 1: Tests loadGraphData and getListOfAllLocations method
   *
   * @return true if all tests pass, false if otherwise.
   * @throws IOException
   */
  @Test
  public void roleTest1() throws IOException{
    Backend backend = new Backend(new Graph_Placeholder());
    String filename = "campus.dot";

    // Test that no error is thrown when loading the graph data
    try {
        // Attempt to load the graph data
        backend.loadGraphData(filename);
    } catch (IOException e) {
        Assertions.fail("An IOException occurred: " + e.getMessage());
    }

    // Test that the graph contains the first node from the file
    List<String> nodes = backend.getListOfAllLocations();
    Assertions.assertTrue(nodes.contains("Union South"));
  }

  /**
   * Test 2: Tests findLocationsOnShortestPath method
   *
   * @return true if all tests pass, false if otherwise.
   */
  @Test
  public void roleTest2() {
    Backend backend = new Backend(new Graph_Placeholder());

    // Test 1: Valid path from "Union South" to "Atmospheric, Oceanic and Space Sciences"
    List<String> expectedPath = List.of("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
    List<String> actualPath = backend.findLocationsOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
    Assertions.assertEquals(expectedPath, actualPath, "Expected path was not returned.");

    // Test 2: Same start and end location, should return a list with the start location only
    expectedPath = List.of("Computer Sciences and Statistics");
    actualPath = backend.findLocationsOnShortestPath("Computer Sciences and Statistics", "Computer Sciences and Statistics");
    Assertions.assertEquals(expectedPath, actualPath, "Expected single-location path was not returned.");

    // Test 3: No path available from "Atmospheric, Oceanic and Space Sciences" to "Union South"
    List<String> actualEmptyPath = backend.findLocationsOnShortestPath("Atmospheric, Oceanic and Space Sciences", "Union South");

    // Check that "Atmospheric, Oceanic and Space Sciences" is returned (because it's the first location) but the list shouldn't contain any further locations.
    Assertions.assertTrue(actualEmptyPath.isEmpty() || actualEmptyPath.size() == 1 && actualEmptyPath.contains("Atmospheric, Oceanic and Space Sciences"),
                "Expected empty path or a path with only the starting location when no valid path exists.");
  }

  /**
   * Test 3: Tests findTimesOnShortestPath and getLongestLocationListFrom method
   *
   * @return true if all tests pass, false if otherwise.
   */
  @Test
  public void roleTest3() {
    Backend backend = new Backend(new Graph_Placeholder());

    // Test 1: Valid path from "Union South" to "Atmospheric, Oceanic and Space Sciences"
    List<Double> expectedTimes = List.of(1.0, 2.0); // Placeholder edge times, adjust based on actual expected behavior
    List<Double> actualTimes = backend.findTimesOnShortestPath("Union South", "Atmospheric, Oceanic and Space Sciences");
    Assertions.assertEquals(expectedTimes, actualTimes, "Expected walking times were not returned.");

    // Test 2: Same start and end location, should return an empty list as no path exists
    List<Double> emptyTimes = backend.findTimesOnShortestPath("Computer Sciences and Statistics", "Computer Sciences and Statistics");
    Assertions.assertTrue(emptyTimes.isEmpty(), "Expected empty times list for same start and end location.");

    // Test 3: No path available from "Atmospheric, Oceanic and Space Sciences" to "Union South"
    // The placeholder class should return an empty list of walking times if no valid path is found.
    List<Double> noPathTimes = backend.findTimesOnShortestPath("Atmospheric, Oceanic and Space Sciences", "Union South");
    Assertions.assertTrue(noPathTimes.isEmpty(), "Expected empty times list when no valid path exists.");

    // Test 4: Valid start location, "Union South"
    // The longest path from "Union South" should be from "Union South" to "Atmospheric, Oceanic and Space Sciences"
    List<String> expectedLongestPath = List.of("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
    List<String> actualLongestPath = backend.getLongestLocationListFrom("Union South");
    Assertions.assertEquals(expectedLongestPath, actualLongestPath, "The longest path was not found correctly.");

    // Test 5: Invalid start location, should throw NoSuchElementException
    // Testing for a non-existent start location
    Assertions.assertThrows(NoSuchElementException.class, () -> {
    backend.getLongestLocationListFrom("Non-existent Location");
        }, "Expected NoSuchElementException for invalid start location.");

    // Test 6: Valid start location with no reachable nodes, should throw NoSuchElementException
    // Here, "Atmospheric, Oceanic and Space Sciences" is not connected to any other node
    Assertions.assertThrows(NoSuchElementException.class, () -> {
    backend.getLongestLocationListFrom("Atmospheric, Oceanic and Space Sciences");
        }, "Expected NoSuchElementException for no reachable locations.");

  }
}
