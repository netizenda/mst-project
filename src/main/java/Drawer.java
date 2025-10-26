import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Drawer {

    public static void draw(Graph g, String outPath) throws Exception {
        int W = 1200;
        int H = 1200;
        BufferedImage img = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();

        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,W,H);

        int n = g.nodes.size();
        double cx = W / 2.0;
        double cy = H / 2.0;
        double radius = Math.min(W,H) * 0.35;
        Map<String, Point> pos = new HashMap<>();
        for (int i=0;i<n;i++) {
            double theta = 2.0 * Math.PI * i / n;
            int x = (int)(cx + radius * Math.cos(theta));
            int y = (int)(cy + radius * Math.sin(theta));
            pos.put(g.nodes.get(i), new Point(x,y));
        }

        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.LIGHT_GRAY);
        for (Edge e : g.edges) {
            Point a = pos.get(e.from);
            Point b = pos.get(e.to);
            if (a==null || b==null) continue;
            g2.drawLine(a.x, a.y, b.x, b.y);
            // midpoint and weight
            int mx = (a.x + b.x)/2;
            int my = (a.y + b.y)/2;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            g2.drawString(String.valueOf(e.weight), mx, my);
            g2.setColor(Color.LIGHT_GRAY);
        }

        int r = 14;
        for (String node : g.nodes) {
            Point p = pos.get(node);
            g2.setColor(Color.ORANGE);
            g2.fillOval(p.x - r, p.y - r, 2*r, 2*r);
            g2.setColor(Color.BLACK);
            g2.drawOval(p.x - r, p.y - r, 2*r, 2*r);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(node, p.x + r, p.y + r);
        }

        g2.dispose();
        ImageIO.write(img, "PNG", new File(outPath));
    }
}
