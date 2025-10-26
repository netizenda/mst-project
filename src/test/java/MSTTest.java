import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

public class MSTTest {

    static List<Graph> loadGraphs() {
        try (FileReader reader = new FileReader("ass_3_input.json")) {
            Gson gson = new Gson();
            Type wrapperType = new TypeToken<Map<String, List<Graph>>>() {}.getType();
            Map<String, List<Graph>> wrapper = gson.fromJson(reader, wrapperType);
            return wrapper.get("graphs");
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать ass_3_input.json: " + e.getMessage(), e);
        }
    }

    @Test
    void testTotalCostEquality() {
        List<Graph> graphs = loadGraphs();
        for (Graph g : graphs) {
            MSTResult prim = MST.prim(g);
            MSTResult kruskal = MST.kruskal(g);
            assertEquals(prim.total_cost, kruskal.total_cost,
                    "Graph " + g.id + ": MST cost mismatch between Prim and Kruskal");
        }
    }

    @Test
    void testEdgeCountInMST() {
        List<Graph> graphs = loadGraphs();
        for (Graph g : graphs) {
            MSTResult prim = MST.prim(g);
            assertEquals(g.vertexCount() - 1, prim.mst_edges.size(),
                    "Graph " + g.id + ": Prim MST should have V-1 edges");

            MSTResult kruskal = MST.kruskal(g);
            assertEquals(g.vertexCount() - 1, kruskal.mst_edges.size(),
                    "Graph " + g.id + ": Kruskal MST should have V-1 edges");
        }
    }

    @Test
    void testNoCyclesInMST() {
        List<Graph> graphs = loadGraphs();
        for (Graph g : graphs) {
            MSTResult prim = MST.prim(g);
            assertTrue(isAcyclic(prim, g.nodes), "Graph " + g.id + ": Prim MST contains cycles");

            MSTResult kruskal = MST.kruskal(g);
            assertTrue(isAcyclic(kruskal, g.nodes), "Graph " + g.id + ": Kruskal MST contains cycles");
        }
    }

    @Test
    void testPerformanceValuesNonNegative() {
        List<Graph> graphs = loadGraphs();
        for (Graph g : graphs) {
            MSTResult prim = MST.prim(g);
            MSTResult kruskal = MST.kruskal(g);

            assertTrue(prim.execution_time_ms >= 0, "Graph " + g.id + ": Prim negative time");
            assertTrue(kruskal.execution_time_ms >= 0, "Graph " + g.id + ": Kruskal negative time");
            assertTrue(prim.operations_count >= 0, "Graph " + g.id + ": Prim negative operations");
            assertTrue(kruskal.operations_count >= 0, "Graph " + g.id + ": Kruskal negative operations");
        }
    }

    @Test
    void testConnectivityOfMST() {
        List<Graph> graphs = loadGraphs();
        for (Graph g : graphs) {
            MSTResult prim = MST.prim(g);
            MSTResult kruskal = MST.kruskal(g);
            assertTrue(isConnected(prim, g.nodes), "Graph " + g.id + ": Prim MST disconnected");
            assertTrue(isConnected(kruskal, g.nodes), "Graph " + g.id + ": Kruskal MST disconnected");
        }
    }

    private boolean isAcyclic(MSTResult mst, List<String> nodes) {
        UnionFind uf = new UnionFind(nodes);
        for (Edge e : mst.mst_edges) {
            String ra = uf.find(e.from);
            String rb = uf.find(e.to);
            if (ra != null && rb != null && ra.equals(rb)) return false;
            uf.union(e.from, e.to);
        }
        return true;
    }

    private boolean isConnected(MSTResult mst, List<String> nodes) {
        if (mst.mst_edges.isEmpty() && nodes.size() > 1) return false;
        UnionFind uf = new UnionFind(nodes);
        for (Edge e : mst.mst_edges) {
            uf.union(e.from, e.to);
        }
        String root = uf.find(nodes.get(0));
        for (String n : nodes) {
            if (!uf.find(n).equals(root)) return false;
        }
        return true;
    }
}
