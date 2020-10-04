package tech.land.ownership;

import java.util.*;

import static java.util.Collections.emptySet;

public class ScopedGraph {
    private final CompanyGraph sourceGraph;

    private final String startNode;
    private final String endNode;

    private final Map<String, Set<String>> scoped;

    public ScopedGraph(CompanyGraph sourceGraph, String nodeToScopeTo) {
        this.sourceGraph = sourceGraph;
        this.startNode = sourceGraph.getRootFor(nodeToScopeTo);
        this.endNode = nodeToScopeTo;
        this.scoped = scope();
    }

    public Map<String, Set<String>> get() {
        return this.scoped;
    }

    public String getStartNode() {
        return startNode;
    }

    private Map<String, Set<String>> scope() {
        boolean nodeExists = sourceGraph.contains(endNode);
        if (!nodeExists) {
            return Collections.emptyMap();
        }

        boolean endNodeIsARoot = sourceGraph.getParentFor(endNode).isEmpty();
        if (endNodeIsARoot) {
            return Map.of(endNode, emptySet());
        }

        return scopeRecursively(endNode, new HashMap<>());
    }

    private Map<String, Set<String>> scopeRecursively(String node, Map<String, Set<String>> accumulator) {
        Optional<String> parent = sourceGraph.getParentFor(node);
        if (!parent.isPresent()) {
            return accumulator;
        }

        Set<String> children = sourceGraph.getChildrenOf(parent.get()).orElse(emptySet());
        accumulator.put(parent.get(), children);
        return scopeRecursively(parent.get(), accumulator);
    }
}
