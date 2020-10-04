package tech.land.ownership;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

public class CompanyGraph {
    private final Map<String, Set<String>> graph;
    private Map<String, String> graphReverse;

    public CompanyGraph(Map<String, String> graphReverse) {
        this.graphReverse = graphReverse;
        this.graph = this.graphReverse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, Collectors.toSet())));
    }

    public Map<String, Set<String>> getGraph() {
        return this.graph;
    }
}
