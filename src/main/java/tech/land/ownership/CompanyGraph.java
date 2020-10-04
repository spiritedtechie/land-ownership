package tech.land.ownership;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

public class CompanyGraph {

    // Key: node id, Value: set of child node ids
    private final Map<String, Set<String>> graph;
    // Key: node id, Value: parent node id
    private final Map<String, String> graphReverse;

    public CompanyGraph(Map<String, String> graphReverse) {
        this.graphReverse = graphReverse;
        this.graph = buildGraph();
    }

    private Map<String, Set<String>> buildGraph() {
        return this.graphReverse.entrySet()
                .stream()
                .filter(entry -> entry.getValue() != null)
                .collect(groupingBy(Map.Entry::getValue, mapping(Map.Entry::getKey, Collectors.toSet())));
    }

    public Map<String, Set<String>> getGraph() {
        return this.graph;
    }

    public boolean contains(String node) {
        return this.graphReverse.containsKey(node);
    }

    public Optional<String> getParentFor(String node) {
        return Optional.ofNullable(graphReverse.get(node));
    }

    public String getRootFor(String node) {
        if (!graphReverse.containsKey(node)) {
            return null;
        }

        String parent = graphReverse.get(node);
        boolean nodeIsRoot = parent == null;

        return (nodeIsRoot) ? node : getRootFor(parent);
    }

    public Optional<Set<String>> getChildrenOf(String node) {
        if (!this.graphReverse.containsKey(node)) {
            return Optional.empty();
        }

        return Optional.ofNullable(this.graph.get(node));
    }
}
