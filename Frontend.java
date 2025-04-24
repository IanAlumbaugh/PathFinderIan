import java.util.List;
import java.lang.StringBuilder;

/**
 * This is the interface that a frontend developer will implement.  It will 
 * enable users to access the functionality exposed by the BackendInterface.
 *
 * Notice the organization of the methods below into pairs that generate HTML
 * strings that 1) prompt the user for input to perform a specific computation,
 * and then 2) make use of that input with the help of the backend, to compute
 * and then display the requested results.
 *
 * A webapp will be developed later in this project to integrate these html
 * snippets into a webpage that is returned custom build in response to each
 * user request.
 */
public class Frontend implements FrontendInterface {

    private BackendInterface backend; //the backend that will be used for gathering location data

    /**
     * Implementing classes should support the constructor below.
     * @param backend is used for shortest path computations
     */
    public Frontend(BackendInterface backend) {
	this.backend = backend;
    }
    
    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="start", for the start location
     * - a text input field with the id="end", for the destination
     * - a button labelled "Find Shortest Path" to request this computation
     * Ensure that these text fields are clearly labelled, so that the user
     * can understand how to use them.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a shortest path computation
     */
    public String generateShortestPathPromptHTML() {
	//returns one long html fragment in string format with two text prompts to indicate
	//start and end of the path, and then a button to find the path between those points
	return "<input type=\"text\" id=\"start\" placeholder=\"enter start here...\" />\n" +
                "<input type=\"text\" id=\"end\" placeholder=\"enter end here...\" />\n" +
                "<input type=\"button\" value=\"Find Shortest Path\" />";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total travel time along this path
     * Or if there is no such path, the HTML returned should instead indicate 
     * the kind of problem encountered.
     * @param start is the starting location to find a shortest path from
     * @param end is the destination that this shortest path should end at
     * @return an HTML string that describes the shortest path between these
     *         two locations
     */
    public String generateShortestPathResponseHTML(String start, String end) {
	//finds all locations on the shortest path from start to end
	List<String> locations = backend.findLocationsOnShortestPath(start, end);

	//checks if the above list of locations is empty, responds accordingly
	if(locations == null || locations.isEmpty())
		return "<p>No path could be found between the two points.</p>";

	//makes a html list of all locations on shortest path
	StringBuilder list = new StringBuilder();

	for(int i = 0; i < locations.size(); i++)
		list.append("<li>" + locations.get(i) + "</li>\n");

	//finds the total time to run the path
	List<Double> times = backend.findTimesOnShortestPath(start, end);
	double totalTime = 0;

	//adds up times from times list and adds it to totalTime
	for(int i = 0; i < times.size(); i++)
		totalTime += times.get(i);

	//shortens total time taken to just 2 decimal points
	String time = String.format("%.2f", totalTime);

	//returns a html fragment in string format that indicates the start and end positions of
	//the shortest path, the contents of the path, and then the time it takes to travel that path
	return "<p>The shortest path starts at " + start + " and ends at " + end + ".</p>\n" +
		"<p>The locations on this path are as follows:</p>\n<ol>\n" + list.toString() + "</ol>\n" + 
		"<p>This path takes " + time + " seconds to travel.</p>";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a text input field with the id="from", for the start location
     * - a button labelled "Longest Location List From" to submit this request
     * Ensure that this text field is clearly labelled, so that the user
     * can understand how to use it.
     * @return an HTML string that contains input controls that the user can
     *         make use of to request a longest location list calculation
     */
    public String generateLongestLocationListFromPromptHTML() {
	//returns one long html fragment in string format with a text prompt to indicate
        //start of the path, and then a button to find the path between those points
        return "<input type=\"text\" id=\"from\" placeholder=\"enter start here...\" />\n" +
		"<input type=\"button\" value=\"Longest Location List From\" />";
    }

    /**
     * Returns an HTML fragment that can be embedded within the body of a
     * larger html page.  This HTML output should include:
     * - a paragraph (p) that describes the path's start and end locations
     * - an ordered list (ol) of locations along that shortest path
     * - a paragraph (p) that includes the total number of locations on path
     * Or if no such path can be found, the HTML returned should instead
     * indicate the kind of problem encountered.
     * @param start is the starting location to find the longest list from
     * @return an HTML string that describes the longest list of locations 
     *        along a shortest path starting from the specified location
     */
    public String generateLongestLocationListFromResponseHTML(String start) {
	//this gets the shortest path with the most locations
	List<String> locations = backend.getLongestLocationListFrom(start);

	//checks if list above is empty, responds accordingly
	if(locations == null || locations.isEmpty())
                return "<p>No path could be found from the given starting point.</p>";

	//finds the end location of the path
	String end = locations.get(locations.size() - 1);

	//makes a html list of all locations on the longest location list
        StringBuilder list = new StringBuilder();

        for(int i = 0; i < locations.size(); i++)
                list.append("<li>" + locations.get(i) + "</li>\n");

	//returns a html fragment in string format that indicates the start and end positions of
        //the shortest path, the contents of the path, and then how many locations are in that path
        return "<p>The shortest path with the most locations starts at " + start + " and ends at " + end + ".</p>\n" +
                "<p>The locations on this path are as follows:</p>\n<ol>\n" + list.toString() + "</ol>\n" +
                "<p>This path has " + locations.size() + " locations in total.</p>";
    }
}
