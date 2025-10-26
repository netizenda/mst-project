import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        String inputPath = args.length > 0 ? args[0] : "ass_3_input.json";
        String outputPath = args.length > 1 ? args[1] : "ass_3_output.json";
        String imagesDir = args.length > 2 ? args[2] : "graphs_images";

        File fin = new File(inputPath);
        if (!fin.exists()) {
            System.out.println("Input not found, generating: " + inputPath);
            GraphGenerator.generate(inputPath);
        }

        Gson gson = new Gson();
        Type wrapperType = new TypeToken<Map<String, List<Graph>>>(){}.getType();
        Map<String, List<Graph>> wrapper;
        try (FileReader fr = new FileReader(inputPath)) {
            wrapper = gson.fromJson(fr, wrapperType);
        }
        List<Graph> graphs = wrapper.get("graphs");

        Map<String, Object> outWrapper = new LinkedHashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        File dir = new File(imagesDir);
        if (!dir.exists()) dir.mkdirs();

        for (Graph g : graphs) {
            System.out.println("Processing graph id=" + g.id + " (V=" + g.vertexCount() + " E=" + g.edgeCount() + ")");
            Map<String, Object> resMap = new LinkedHashMap<>();
            resMap.put("graph_id", g.id);
            Map<String,Integer> stats = new LinkedHashMap<>();
            stats.put("vertices", g.vertexCount());
            stats.put("edges", g.edgeCount());
            resMap.put("input_stats", stats);

            MSTResult p = MST.prim(g);
            Map<String,Object> pm = new LinkedHashMap<>();
            pm.put("mst_edges", edgesToSimple(p.mst_edges));
            pm.put("total_cost", p.total_cost);
            pm.put("operations_count", p.operations_count);
            pm.put("execution_time_ms", Math.round(p.execution_time_ms*100.0)/100.0);
            resMap.put("prim", pm);

            MSTResult k = MST.kruskal(g);
            Map<String,Object> km = new LinkedHashMap<>();
            km.put("mst_edges", edgesToSimple(k.mst_edges));
            km.put("total_cost", k.total_cost);
            km.put("operations_count", k.operations_count);
            km.put("execution_time_ms", Math.round(k.execution_time_ms*100.0)/100.0);
            resMap.put("kruskal", km);

            results.add(resMap);

            String imgPath = imagesDir + File.separator + "graph_" + g.id + ".png";
            try {
                Drawer.draw(g, imgPath);
            } catch (Exception ex) {
                System.err.println("Failed to draw graph " + g.id + ": " + ex.getMessage());
            }
        }

        outWrapper.put("results", results);
        Gson pretty = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter fw = new FileWriter(outputPath)) {
            pretty.toJson(outWrapper, fw);
        }

        System.out.println("Processing finished. Output written to: " + outputPath);
        System.out.println("Graph images saved to folder: " + imagesDir);
    }

    private static List<Map<String,Object>> edgesToSimple(List<Edge> edges) {
        List<Map<String,Object>> list = new ArrayList<>();
        for (Edge e : edges) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("from", e.from);
            m.put("to", e.to);
            m.put("weight", e.weight);
            list.add(m);
        }
        return list;
    }
}
