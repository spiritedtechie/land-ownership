package tech.land.ownership;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static tech.land.ownership.TestUtilities.setOf;

public class ScopedGraphTest {

    @Test
    public void testScopeGraph_nodeToScopeToDoesNotExists() {
        Map<String, String> graphReverse = new HashMap<>();
        graphReverse.put("B", "A");
        graphReverse.put("A", null);
        CompanyGraph companyGraph = new CompanyGraph(graphReverse);

        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "U");

        assertThat(scopedGraph.get()).isEmpty();
    }

    @Test
    public void testScopeGraph_nodeToScopeToIsARoot () {
        Map<String, String> graphReverse = new HashMap<>();
        graphReverse.put("B", "A");
        graphReverse.put("A", null);
        CompanyGraph companyGraph = new CompanyGraph(graphReverse);

        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "A");

        assertThat(scopedGraph.get()).containsEntry("A", setOf());
    }

    @Test
    public void testScopeGraph_nodeIsPartOfSingleLevelTree() {
        Map<String, String> graphReverse = new HashMap<>();
        graphReverse.put("B", "A");
        graphReverse.put("A", null);
        CompanyGraph companyGraph = new CompanyGraph(graphReverse);

        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "B");

        assertThat(scopedGraph.get()).hasSize(1);
        assertThat(scopedGraph.get()).containsEntry("A", setOf("B"));
    }

    @Test
    public void testScopeGraph_nodeIsPartOfMultipleLeveledTree() {
        Map<String, String> graphReverse = new HashMap<>();
        graphReverse.put("C", "A");
        graphReverse.put("E", "C");
        graphReverse.put("A", null);
        CompanyGraph companyGraph = new CompanyGraph(graphReverse);

        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "E");

        assertThat(scopedGraph.get()).hasSize(2);
        assertThat(scopedGraph.get()).containsEntry("A", setOf("C"));
        assertThat(scopedGraph.get()).containsEntry("C", setOf("E"));
    }

    @Test
    public void testScopeGraph_includesSiblingsNodesOfNodePathToScopedNode() {
        Map<String, String> reverseGraph = new HashMap<>();
        reverseGraph.put("B", "A");
        reverseGraph.put("C", "A");
        reverseGraph.put("D", "C");
        reverseGraph.put("E", "C");
        reverseGraph.put("A", null);
        CompanyGraph companyGraph = new CompanyGraph(reverseGraph);

        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "E");

        assertThat(scopedGraph.get()).hasSize(2);
        assertThat(scopedGraph.get()).containsEntry("A", setOf("B" , "C"));
        assertThat(scopedGraph.get()).containsEntry("C", setOf("D", "E"));
    }
}
