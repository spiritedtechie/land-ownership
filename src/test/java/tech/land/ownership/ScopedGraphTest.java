package tech.land.ownership;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;
import static tech.land.ownership.TestUtilities.setOf;

public class ScopedGraphTest {
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testScopeGraph_nodeToScopeToDoesNotExists() {
        CompanyGraph companyGraph = new CompanyGraph(new HashMap<>());

        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("Cannot scope graph as end node does not exist in source graph");
        new ScopedGraph(companyGraph, "U");
    }

    @Test
    public void testScopeGraph_rootNodeNotFound() {
        Map<String, String> graphReverse = new HashMap<>();
        graphReverse.put("B", "A");
        graphReverse.put("A", null);
        CompanyGraph companyGraph = spy(new CompanyGraph(graphReverse));

        // Override getRoot of company graph to return empty for this test case
        doReturn(Optional.empty()).when(companyGraph).getRootFor("B");

        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("Cannot scope graph as root node not found for scoped node");
        new ScopedGraph(companyGraph, "B");
    }

    @Test
    public void testScopeGraph_nodeToScopeToIsARoot() {
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
