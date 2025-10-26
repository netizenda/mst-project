import java.util.HashMap;
import java.util.Map;

public class UnionFind {
    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();

    public UnionFind(Iterable<String> elements) {
        for (String e : elements) {
            parent.put(e, e);
            rank.put(e, 0);
        }
    }

    public String find(String x) {
        String p = parent.get(x);
        if (p == null) return null;
        if (!p.equals(x)) {
            String root = find(p);
            parent.put(x, root);
            return root;
        }
        return p;
    }

    public boolean union(String a, String b) {
        String ra = find(a);
        String rb = find(b);
        if (ra == null || rb == null) return false;
        if (ra.equals(rb)) return false;
        int rra = rank.get(ra);
        int rrb = rank.get(rb);
        if (rra < rrb) {
            parent.put(ra, rb);
        } else if (rra > rrb) {
            parent.put(rb, ra);
        } else {
            parent.put(rb, ra);
            rank.put(ra, rra + 1);
        }
        return true;
    }
}
