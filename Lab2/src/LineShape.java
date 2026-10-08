import java.awt.*;

public class LineShape extends Shape {
    public LineShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
    }

    public void draw(Graphics g) {
        setOutline(g);
        g.drawLine(x1, y1, x2, y2);
    }
}