import java.awt.*;

public class LineOShape extends LineShape {
    private static final int R = 6;
    private final EllipseShape circle1;
    private final EllipseShape circle2;

    public LineOShape(int x1, int y1, int x2, int y2) {
        super(x1, y1, x2, y2);
        circle1 = new EllipseShape(x1 - R, y1 - R, x1 + R, y1 + R);
        circle2 = new EllipseShape(x2 - R, y2 - R, x2 + R, y2 + R);
    }

    public String getName() {
        return "Лінія з кружечками";
    }

    public void show(Graphics g) {
        super.show(g);
        circle1.rubber = circle2.rubber = rubber;
        circle1.selected = circle2.selected = selected;
        circle1.show(g);
        circle2.show(g);
    }
}