import java.awt.*;

public class CubeShape extends RectShape {
    private final RectShape back;
    private final LineShape[] edges = new LineShape[4];

    public CubeShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        int left = Math.min(x2, 2 * x1 - x2);
        int right = Math.max(x2, 2 * x1 - x2);
        int top = Math.min(y2, 2 * y1 - y2);
        int bottom = Math.max(y2, 2 * y1 - y2);
        int d = Math.min(right - left, bottom - top) / 3;

        back = new RectShape(x1 + d, y1 - d, x2 + d, y2 - d);
        edges[0] = new LineShape(left, top, left + d, top - d);
        edges[1] = new LineShape(right, top, right + d, top - d);
        edges[2] = new LineShape(left, bottom, left + d, bottom - d);
        edges[3] = new LineShape(right, bottom, right + d, bottom - d);
    }

    public void show(Graphics g) {
        super.show(g);
        back.rubber = rubber;
        back.show(g);
        for (LineShape e : edges) {
            e.rubber = rubber;
            e.show(g);
        }
    }
}
