import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.*;

public class GraphGenerator {

    public static void generate(String outputPath) throws Exception {

        Map<Integer, Graph> graphs = new LinkedHashMap<>();

        int[] smallVerts = {5, 8, 11, 14, 17};
        double[] smallDensities = {0.6, 0.5, 0.45, 0.4, 0.35};

        int[] mediumVerts = {20, 24, 28, 32, 36, 40, 44, 48, 52, 56};
        double[] mediumDensities = {0.25,0.22,0.20,0.18,0.16,0.15,0.14,0.13,0.12,0.11};

        int[] largeVerts = {60, 66, 72, 78, 84, 90, 96, 102, 108, 114};
        double[] largeDensities = {0.08,0.075,0.07,0.065,0.06,0.055,0.05,0.045,0.04,0.035};

        int[] xlVerts = {150, 220, 300};
        double[] xlDensities = {0.02, 0.015, 0.01};

        Random rnd = new Random(12345);
        int id = 1;

        for (int i=0;i<smallVerts.length;i++, id++) {
            graphs.put(id, createRandomGraph(id, smallVerts[i], smallDensities[i], rnd));
        }
        for (int i=0;i<mediumVerts.length;i++, id++) {
            graphs.put(id, createRandomGraph(id, mediumVerts[i], mediumDensities[i], rnd));
        }
        for (int i=0;i<largeVerts.length;i++, id++) {
            graphs.put(id, createRandomGraph(id, largeVerts[i], largeDensities[i], rnd));
        }
        for (int i=0;i<xlVerts.length;i++, id++) {
            graphs.put(id, createRandomGraph(id, xlVerts[i], xlDensities[i], rnd));
        }

        Map<String, Object> wrapper = new HashMap<>();
        List<Graph> list = new ArrayList<>(graphs.values());
        wrapper.put("graphs", list);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(outputPath)) {
            gson.toJson(wrapper, fw);
        }
        System.out.println("Input JSON generated: " + outputPath);
    }

    private static Graph createRandomGraph(int id, int n, double density, Random rnd) {
        List<String> nodes = new ArrayList<>();
        for (int i=0;i<n;i++) nodes.add("V" + i);

        List<Edge> edges = new ArrayList<>();
        for (int i=1;i<n;i++) {
            int parent = rnd.nextInt(i); // connect to some previous node
            int w = 1 + rnd.nextInt(100);
            edges.add(new Edge("V" + parent, "V" + i, w));
        }

        long maxEdges = (long)n * (n - 1) / 2;
        long desiredEdges = Math.min(maxEdges, Math.max(edges.size(), (long)(maxEdges * density)));
        Set<String> used = new HashSet<>();
        for (Edge e : edges) {
            String key = edgeKey(e.from, e.to);
            used.add(key);
        }
        while (edges.size() < desiredEdges) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (a == b) continue;
            String k = edgeKey("V"+a, "V"+b);
            if (used.contains(k)) continue;
            used.add(k);
            int w = 1 + rnd.nextInt(100);
            edges.add(new Edge("V"+a, "V"+b, w));
        }

        return new Graph(id, nodes, edges);
    }

    private static String edgeKey(String a, String b) {
        if (a.compareTo(b) <= 0) return a + "-" + b;
        return b + "-" + a;
    }
}
