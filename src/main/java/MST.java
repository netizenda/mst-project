import java.util.*;

public class MST {

    public static MSTResult prim(Graph g) {
        MSTResult res = new MSTResult();
        long start = System.nanoTime();

        Map<String, List<Edge>> adj = g.adjacencyList();
        if (g.nodes.isEmpty()) {
            res.execution_time_ms = (System.nanoTime() - start) / 1e6;
            return res;
        }

        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        String startNode = g.nodes.get(0);
        visited.add(startNode);
        for (Edge e : adj.get(startNode)) pq.offer(e);

        long comparisons = 0;
        long pqOps = 0;

        while (!pq.isEmpty() && res.mst_edges.size() < g.vertexCount() - 1) {
            Edge e = pq.poll(); pqOps++;
            comparisons++;
            if (visited.contains(e.to)) continue;
            visited.add(e.to);
            res.mst_edges.add(e);
            for (Edge ne : adj.get(e.to)) {
                comparisons++;
                if (!visited.contains(ne.to)) {
                    pq.offer(ne);
                    pqOps++;
                }
            }
        }

        long end = System.nanoTime();
        res.execution_time_ms = (end - start) / 1e6;
        res.computeTotals();
        res.operations_count = comparisons + pqOps;
        return res;
    }

    public static MSTResult kruskal(Graph g) {
        MSTResult res = new MSTResult();
        long start = System.nanoTime();

        List<Edge> edges = new ArrayList<>(g.edges);
        edges.sort(Comparator.comparingInt(e -> e.weight));

        UnionFind uf = new UnionFind(g.nodes);
        long finds = 0;
        long unions = 0;
        long comparisons = 0;

        for (Edge e : edges) {
            finds += 2;
            String ra = uf.find(e.from);
            String rb = uf.find(e.to);
            if (ra == null || rb == null) continue;
            comparisons++;
            if (!ra.equals(rb)) {
                boolean joined = uf.union(ra, rb);
                if (joined) unions++;
                res.mst_edges.add(e);
                if (res.mst_edges.size() == g.vertexCount() - 1) break;
            }
        }

        long end = System.nanoTime();
        res.execution_time_ms = (end - start) / 1e6;
        res.computeTotals();
        res.operations_count = finds + unions + comparisons;
        return res;
    }
}
