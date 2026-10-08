import java.awt.*;

public class EllipseShape extends Shape {
    public EllipseShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    public void draw(Graphics g) {
        int left = Math.min(x2, 2 * x1 - x2);
        int top = Math.min(y2, 2 * y1 - y2);
        int w = 2 * Math.abs(x2 - x1);
        int h = 2 * Math.abs(y2 - y1);
        if (!rubber) {
            g.setColor(Color.GRAY);
            g.fillOval(left, top, w, h);
        }
        setOutline(g);
        g.drawOval(left, top, w, h);
    }
}