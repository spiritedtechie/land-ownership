package tech.land.ownership;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ConsoleGraphDisplay implements GraphDisplay {

    @Override
    public void show(ScopedGraph scopedGraph) {
        this.printGraph(scopedGraph, scopedGraph.getStartNode(), 1);
    }

    private void printGraph(ScopedGraph graph, String node, int level) {
        printNode(level, node);

        boolean nodeHasNoChildren = graph.get().get(node) == null;
        if (nodeHasNoChildren) return;

        for (String childNode : graph.get().get(node)) {
            printGraph(graph, childNode, level + 1);
        }
    }

    private void printNode(int level, String node) {
        String levelText = IntStream.range(1, level + 1)
                .mapToObj(n -> "| ")
                .collect(Collectors.joining());

        printLineToConsole(levelText + " - " + node + ";");
    }

    /**
     * This has been extracted to allow unit testing of core display functionality
     * via unit tests
     */
    void printLineToConsole(String line) {
        System.out.println(line);
    }
}
