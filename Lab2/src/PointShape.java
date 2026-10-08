import java.awt.*;

public class PointShape extends Shape {
    public PointShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    public void draw(Graphics g) {
        setOutline(g);
        g.fillOval(x1 - 3, y1 - 3, 6, 6);
    }
}
 