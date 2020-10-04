package tech.land.ownership;

import java.util.HashMap;
import java.util.Map;

public class Application {

    private static final Map<String, String> companyParents = new HashMap<>();

    static {
        companyParents.put("C4012", null);
        companyParents.put("C12332", "C4012");
        companyParents.put("C71299", "C4012");
        companyParents.put("C45353", "C71299");
        companyParents.put("C91123", "C71299");
        companyParents.put("C555123", "C4012");
        companyParents.put("C655123", "C555123");
    }

    public static void main(String[] args) {
        if (args == null || args.length == 0 || args[0] == null) {
            throw new IllegalArgumentException("Company ID not supplied");
        }

        String companyId = args[0];

        CompanyGraph companyGraph = new CompanyGraph(companyParents);
        ScopedGraph scopedGraph = new ScopedGraph(companyGraph, companyId);

        GraphDisplay display = new ConsoleGraphDisplay();
        display.show(scopedGraph);
    }

}
