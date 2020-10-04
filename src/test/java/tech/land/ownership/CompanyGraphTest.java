package tech.land.ownership;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static org.assertj.core.api.Assertions.assertThat;

public class CompanyGraphTest {

    @Test
    public void testBuildsGraph() {
        Map<String, String> reverseGraph = new HashMap<>();
        reverseGraph.put("A", null);
        reverseGraph.put("B", "A");
        reverseGraph.put("C", "A");
        reverseGraph.put("D", "A");
        reverseGraph.put("E", "C");
        reverseGraph.put("F", "E");
        reverseGraph.put("G", "E");
        reverseGraph.put("H", "D");

        CompanyGraph companyGraph = new CompanyGraph(reverseGraph);

        Map<String, Set<String>> forwardGraph = companyGraph.getGraph();

        assertThat(forwardGraph).hasSize(4);
        assertThat(forwardGraph).containsEntry("A", setOf("B", "C", "D"));
        assertThat(forwardGraph).containsEntry("C", setOf("E"));
        assertThat(forwardGraph).containsEntry("E", setOf("F", "G"));
        assertThat(forwardGraph).containsEntry("D", setOf("H"));
    }

    public static HashSet<String> setOf(String... companyIds) {
        return newHashSet(companyIds);
    }

}
