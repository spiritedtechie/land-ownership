package tech.land.ownership;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static tech.land.ownership.TestUtilities.setOf;

public class CompanyGraphTest {

    private CompanyGraph buildTestGraph() {
        Map<String, String> reverseGraph = new HashMap<>();
        reverseGraph.put("A", null);
        reverseGraph.put("B", "A");
        reverseGraph.put("C", "A");
        reverseGraph.put("D", "A");
        reverseGraph.put("E", "C");
        reverseGraph.put("F", "E");
        reverseGraph.put("G", "E");
        reverseGraph.put("H", "D");

        return new CompanyGraph(reverseGraph);
    }

    @Test
    public void testBuildsGraph() {
        CompanyGraph companyGraph = buildTestGraph();

        Map<String, Set<String>> forwardGraph = companyGraph.getGraph();

        assertThat(forwardGraph).hasSize(4);
        assertThat(forwardGraph).containsEntry("A", setOf("B", "C", "D"));
        assertThat(forwardGraph).containsEntry("C", setOf("E"));
        assertThat(forwardGraph).containsEntry("E", setOf("F", "G"));
        assertThat(forwardGraph).containsEntry("D", setOf("H"));
    }

    @Test
    public void testGetParent_forANode() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<String> parent = companyGraph.getParentFor("B");

        assertThat(parent.get()).isSameAs("A");
    }

    @Test
    public void testGetParent_forAnotherNode() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<String> parent = companyGraph.getParentFor("H");

        assertThat(parent.get()).isSameAs("D");
    }

    @Test
    public void testGetParent_forNodeThatDoesntExist() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<String> parent = companyGraph.getParentFor("U");

        assertThat(parent).isEmpty();
    }

    @Test
    public void testGetChildren_whereNodeNotFound() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<Set<String>> children = companyGraph.getChildrenOf("U");

        assertThat(children).isEmpty();
    }

    @Test
    public void testGetChildren_whereNodeHasNoChildren() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<Set<String>> children = companyGraph.getChildrenOf("F");

        assertThat(children).isEmpty();
    }

    @Test
    public void testGetChildren_forNodeThatExists() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<Set<String>> children = companyGraph.getChildrenOf("E");

        assertThat(children.get()).containsExactly("F", "G");
    }

    @Test
    public void testGetChildren_forAnotherNodeThatExists() {
        CompanyGraph companyGraph = buildTestGraph();

        Optional<Set<String>> children = companyGraph.getChildrenOf("C");

        assertThat(children.get()).containsExactly("E");
    }

    @Test
    public void testContains_forNodeThatIsInGraph() {
        CompanyGraph companyGraph = buildTestGraph();

        boolean result = companyGraph.contains("E");

        assertThat(result).isTrue();
    }

    @Test
    public void testContains_forNodeThatIsNotInGraph() {
        CompanyGraph companyGraph = buildTestGraph();

        boolean result = companyGraph.contains("U");

        assertThat(result).isFalse();
    }

    @Test
    public void testGetRoot_success() {
        CompanyGraph companyGraph = buildTestGraph();

        String root = companyGraph.getRootFor("G");

        assertThat(root).isSameAs("A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRoot_forANodeThatDoesNotExists() {
        CompanyGraph companyGraph = buildTestGraph();

        companyGraph.getRootFor("U");
    }

    @Test
    public void testGetRoot_forANodeThatIsAlreadyARoot() {
        CompanyGraph companyGraph = buildTestGraph();

        String root = companyGraph.getRootFor("A");

        assertThat(root).isSameAs("A");
    }
}
