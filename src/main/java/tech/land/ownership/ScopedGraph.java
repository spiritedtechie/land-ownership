package tech.land.ownership;

import java.util.*;

import static java.util.Collections.emptySet;

public class ScopedGraph {
    private final CompanyGraph sourceGraph;

    private final String nodeToScopeTo;

    private final Map<String, Set<String>> scoped;

    public ScopedGraph(CompanyGraph sourceGraph, String nodeToScopeTo) {
        this.sourceGraph = sourceGraph;
        this.nodeToScopeTo = nodeToScopeTo;
        this.scoped = scope();
    }

    public Map<String, Set<String>> get() {
        return this.scoped;
    }

    private Map<String, Set<String>> scope() {
        boolean nodeExists = sourceGraph.contains(nodeToScopeTo);
        if (!nodeExists) {
            return Collections.emptyMap();
        }

        boolean scopedNodeIsARoot = sourceGraph.getParentFor(nodeToScopeTo).isEmpty();
        if (scopedNodeIsARoot) {
            return Map.of(nodeToScopeTo, emptySet());
        }

        return scopeRecursively(nodeToScopeTo, new HashMap<>());
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
