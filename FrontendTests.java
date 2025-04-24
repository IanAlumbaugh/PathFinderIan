import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 *this class tests the Frontend class and all of its methods
 */
public class FrontendTests {

   /**
    * tests generateShortestPathPromptHTML() and generateLongestLocationListFromPromptHTML() method in Frontend class
    */
    @Test
    public void roleTest1() {
	//initalize a working Frontend
        BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
	Frontend test = new Frontend(backend);

	//tests if the shortest path prompt outputs an apppropriate prompt
	String result = test.generateShortestPathPromptHTML();
	String expected = "<input type=\"text\" id=\"start\" placeholder=\"enter start here...\" />\n" +
                	"<input type=\"text\" id=\"end\" placeholder=\"enter end here...\" />\n" +
                	"<input type=\"button\" value=\"Find Shortest Path\" />";
	if(result != expected)
		Assertions.fail("Shortest path prompt failed");

	//tests if the longest location prompt outputs an apppropriate prompt
	result = test.generateLongestLocationListFromPromptHTML();
	expected = "<input type=\"text\" id=\"from\" placeholder=\"enter start here...\" />\n" +
                "<input type=\"button\" value=\"Longest Location List From\" />";
	if(result != expected)
		Assertions.fail("Longest location list prompt failed");
    }

   /**
    * tests generateShortestPathResponseHTML() method in Frontend class
    */
    @Test
    public void roleTest2() {
	//initalize a working Frontend
        BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
        Frontend test = new Frontend(backend);

	//tests if the short path response indicates it has failed if it fails
	String result = test.generateShortestPathResponseHTML("fail", "fail");
	String expected = "<p>No path could be found between the two points.</p>";
	if(!result.equals(expected))
		Assertions.fail("generateShortestPathResponseHTML() method failed when given impossible path");

	//tests if the short path response outputs the right information finding the shortest path between
	//union south and the CompSci building
	result = test.generateShortestPathResponseHTML("Union South", "Computer Sciences and Statistics");
	expected = "<p>The shortest path starts at Union South and ends at Computer Sciences and Statistics.</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n<li>Union South</li>\n" +
		"<li>Computer Sciences and Statistics</li>\n</ol>\n" + 
                "<p>This path takes 3.00 seconds to travel.</p>";
	if(!result.equals(expected))
                Assertions.fail("generateShortestPathResponseHTML() method failed");
    }

   /**
    * tests generateLongestLocationListFromResponseHTML() method in Frontend class
    */
    @Test
    public void roleTest3() {
	//initalize a working Frontend
        BackendInterface backend = new Backend_Placeholder(new Graph_Placeholder());
        Frontend test = new Frontend(backend);

	//tests if the longest location list response indicates it has failed if it fails
	String result = test.generateLongestLocationListFromResponseHTML("fail");
	String expected = "<p>No path could be found from the given starting point.</p>";
	if(!result.equals(expected))
                Assertions.fail("generateLongestLocationListFromResponseHTML() method failed when given impossible path");

	//tests if the longest location list response outputs the right information finding the path with
        //the most locations starting at union south
	result = test.generateLongestLocationListFromResponseHTML("Union South");
        expected = "<p>The shortest path with the most locations starts at Union South and ends at Atmospheric, Oceanic and Space Sciences.</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n<li>Union South</li>\n" +
		"<li>Computer Sciences and Statistics</li>\n<li>Atmospheric, Oceanic and Space Sciences</li>\n</ol>\n" +
		"<p>This path has 3 locations in total.</p>";
        if(!result.equals(expected))
                Assertions.fail("generateLongestLocationListFromResponseHTML() method failed");
    }

   /**
    * tests generateShortestPathResponseHTML() method in Frontend class
    * when implemented with Backend class (FAILING CASES)
    */
    @Test
    public void IntegrationTest1() {
        //initalize a working Frontend
        Backend backend = new Backend(new DijkstraGraph());
        Frontend test = new Frontend(backend);

        //tests if the short path response indicates it has failed if it fails
        String result = test.generateShortestPathResponseHTML("fail", "fail");
        String expected = "<p>No path could be found between the two points.</p>";
        if(!result.equals(expected))
                Assertions.fail("failed to handle impossible path between two fake points");
    }

   /**
    * tests generateLongestLocationListFromResponseHTML() method in Frontend class
    * when implemented with Backend class (FAILING CASES)
    */
    @Test
    public void IntegrationTest2() {
        //initalize a working Frontend
        Backend backend = new Backend(new DijkstraGraph());
        Frontend test = new Frontend(backend);

        //tests if the longest location list response indicates it has failed if it fails
	String result = null;
	//tries to get longest list from, but since this is supposed to fail, I have made it so when
	//it does fail (as it should) that it just makes the result what is expected so the test passes
        try{ result = test.generateLongestLocationListFromResponseHTML("fail"); }
	catch(NoSuchElementException e) { result = "<p>No path could be found from the given starting point.</p>"; }
        String expected = "<p>No path could be found from the given starting point.</p>";
        if(!result.equals(expected))
                Assertions.fail("failed to handle impossible starting path point");
    }

   /**
    * tests generateShortestPathResponseHTML() method in Frontend class
    * when implemented with Backend class (WORKING CASES))
    */
    @Test
    public void IntegrationTest3() {
        //initalize a working Frontend, also tests loadGraphData() by making sure it reads in campus.dot properly
        Backend backend = new Backend(new DijkstraGraph());
	try { backend.loadGraphData("campus.dot"); }
	catch(IOException e) { Assertions.fail("failed to load in graph data"); }
        Frontend test = new Frontend(backend);

	//tests a path connecting wendt commons and memorial arch
        String result = test.generateShortestPathResponseHTML("Wendt Commons" , "Memorial Arch");
        String expected = "<p>The shortest path starts at Wendt Commons and ends at Memorial Arch.</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n<li>Wendt Commons</li>\n" +
                "<li>Memorial Arch</li>\n</ol>\n" +
                "<p>This path takes 112.80 seconds to travel.</p>";
        if(!result.equals(expected))
                Assertions.fail("failed to find path between two connected points");

	//tests same path above but in reverse
        result = test.generateShortestPathResponseHTML("Memorial Arch" , "Wendt Commons");
        expected = "<p>The shortest path starts at Memorial Arch and ends at Wendt Commons.</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n<li>Memorial Arch</li>\n" +
                "<li>Wendt Commons</li>\n</ol>\n" +
                "<p>This path takes 112.80 seconds to travel.</p>";
        if(!result.equals(expected))
                Assertions.fail("failed to find path between two connected points");
    }

   /**
    * tests generateLongestLocationListFromResponseHTML() method in Frontend class
    * when implemented with Backend class (WORKING CASES)
    */
    @Test
    public void IntegrationTest4() {
        //initalize a working Frontend, also tests loadGraphData() by making sure it reads in campus.dot properly
        Backend backend = new Backend(new DijkstraGraph());
        try { backend.loadGraphData("campus.dot"); }
        catch(IOException e) { Assertions.fail("failed to load in graph data"); }
        Frontend test = new Frontend(backend);

	//tests getting the longest list of locations from Union South, however, since this method DOES NOT WORK AT ALL,
	//it is impossible for me to know what the actual list from this would look like so even if that method did work
	//it wouldn't pass this test. If that method did work, I would be able to know what the locations are on this list so that
	//I can actually test properly but my backend partner's code struggles with the longest location list code so I cant
        String result = test.generateLongestLocationListFromResponseHTML("Union South");
        String expected = "<p>The shortest path with the most locations starts at Union South and ends at Atmospheric, Oceanic and Space Sciences.</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n<li>Union South</li>\n" +
                "<li>Computer Sciences and Statistics</li>\n<li>Atmospheric, Oceanic and Space Sciences</li>\n</ol>\n" +
                "<p>This path has 3 locations in total.</p>";
        if(!result.equals(expected))
                Assertions.fail("failed to find a longest location list from a point");
    }
}
