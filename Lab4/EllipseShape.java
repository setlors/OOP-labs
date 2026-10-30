import java.awt.*;

public class EllipseShape extends Shape {
    public EllipseShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    public void show(Graphics g) {
        int left = Math.min(x1, x2);
        int top = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);
        if (!rubber) {
            g.setColor(Color.YELLOW);
            g.fillOval(left, top, w, h);
        }
        setOutline(g);
        g.drawOval(left, top, w, h);
    }
}
