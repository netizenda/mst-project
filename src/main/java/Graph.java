import java.util.*;

public class Graph {
    public int id;
    public List<String> nodes;
    public List<Edge> edges;

    public Graph() {}

    public Graph(int id, List<String> nodes, List<Edge> edges) {
        this.id = id;
        this.nodes = nodes;
        this.edges = edges;
    }

    public Map<String, List<Edge>> adjacencyList() {
        Map<String, List<Edge>> adj = new HashMap<>();
        for (String n : nodes) adj.put(n, new ArrayList<>());
        for (Edge e : edges) {
            adj.get(e.from).add(new Edge(e.from, e.to, e.weight));
            adj.get(e.to).add(new Edge(e.to, e.from, e.weight));
        }
        return adj;
    }

    public int vertexCount() {
        return nodes.size();
    }

    public int edgeCount() {
        return edges.size();
    }
}
