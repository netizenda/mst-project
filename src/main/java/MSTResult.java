import java.util.ArrayList;
import java.util.List;

public class MSTResult {
    public List<Edge> mst_edges = new ArrayList<>();
    public int total_cost = 0;
    public long operations_count = 0;
    public double execution_time_ms = 0.0;

    public void computeTotals() {
        total_cost = mst_edges.stream().mapToInt(e -> e.weight).sum();
    }
}
