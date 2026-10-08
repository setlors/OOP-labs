import java.awt.*;

public class Ellipse extends Figure {
    public Ellipse(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    public void draw(Graphics g) {
        int left = Math.min(x1, x2);
        int top = Math.min(y1, y2);
        int w = Math.abs(x2 - x1);
        int h = Math.abs(y2 - y1);
        g.setColor(Color.YELLOW);
        g.fillOval(left, top, w, h);
        g.setColor(Color.BLACK);
        g.drawOval(left, top, w, h);
    }
}