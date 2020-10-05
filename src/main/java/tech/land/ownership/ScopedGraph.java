package tech.land.ownership;

import java.util.*;

import static java.util.Collections.emptySet;

public class ScopedGraph {
    private final CompanyGraph sourceGraph;

    private final String startNode;
    private final String endNode;

    private final Map<String, Set<String>> scopedGraph;

    public ScopedGraph(CompanyGraph sourceGraph, String nodeToScopeTo) {
        this.sourceGraph = sourceGraph;
        this.endNode = initialiseEndNode(nodeToScopeTo);
        this.startNode = initialiseStartNode(nodeToScopeTo);
        this.scopedGraph = scope();
    }

    private String initialiseStartNode(String endNode) {
        Optional<String> rootForNode = sourceGraph.getRootFor(endNode);
        if (rootForNode.isEmpty()) {
            throw new IllegalStateException("Cannot scope graph as root node not found for scoped node");
        }
        return rootForNode.get();
    }

    private String initialiseEndNode(String endNode) {
        if (!sourceGraph.contains(endNode)) {
            throw new IllegalArgumentException("Cannot scope graph as end node does not exist in source graph");
        }
        return endNode;
    }

    public Map<String, Set<String>> get() {
        return this.scopedGraph;
    }

    public String getStartNode() {
        return startNode;
    }

    private Map<String, Set<String>> scope() {
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
