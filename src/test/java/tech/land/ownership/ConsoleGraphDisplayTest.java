package tech.land.ownership;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConsoleGraphDisplayTest {

    private ConsoleGraphDisplay graphDisplay;

    @Before
    public void setup() {
        graphDisplay = Mockito.spy(new ConsoleGraphDisplay());
    }

    @Test
    public void testShow_complexGraph() {
        Map<String, String> reverseGraph = new HashMap<>();
        reverseGraph.put("A", null);
        reverseGraph.put("B", "A");
        reverseGraph.put("C", "A");
        reverseGraph.put("D", "A");
        reverseGraph.put("E", "C");
        CompanyGraph companyGraph = new CompanyGraph(reverseGraph);
        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "E");

        graphDisplay.show(scopedGraph);

        InOrder inOrder = Mockito.inOrder(graphDisplay);
        inOrder.verify(graphDisplay).printLineToConsole("|  - A;");
        inOrder.verify(graphDisplay).printLineToConsole("| |  - B;");
        inOrder.verify(graphDisplay).printLineToConsole("| |  - C;");
        inOrder.verify(graphDisplay).printLineToConsole("| | |  - E;");
        inOrder.verify(graphDisplay).printLineToConsole("| |  - D;");
    }

    @Test
    public void testShow_whereStartNodeIsRoot() {
        Map<String, String> reverseGraph = new HashMap<>();
        reverseGraph.put("A", null);
        reverseGraph.put("B", "A");
        reverseGraph.put("C", "A");
        CompanyGraph companyGraph = new CompanyGraph(reverseGraph);
        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, "A");

        graphDisplay.show(scopedGraph);

        InOrder inOrder = Mockito.inOrder(graphDisplay);
        inOrder.verify(graphDisplay).printLineToConsole("|  - A;");
    }
}
