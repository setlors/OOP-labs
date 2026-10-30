import java.awt.*;

/** Графічний редактор: зберігає фігури, поточний тип і обробляє події вводу. */
public class Editor {
    public static final String[] NAMES =
            {"Крапка", "Лінія", "Прямокутник", "Еліпс", "Лінія з кружечками", "Куб"};

    private final Shape[] shapes = new Shape[119];
    private int count = 0;
    private int type = 0;
    private int startX, startY, curX, curY;
    private boolean drawing = false;

    public void setType(int t) {
        type = t;
    }

    public int getType() {
        return type;
    }

    public String getTypeName() {
        return NAMES[type];
    }

    public void onPress(int x, int y) {
        startX = curX = x;
        startY = curY = y;
        drawing = true;
    }

    public void onDrag(int x, int y) {
        curX = x;
        curY = y;
    }

    public void onRelease(int x, int y) {
        curX = x;
        curY = y;
        drawing = false;
        if (count < shapes.length) {
            shapes[count++] = createShape();
        }
    }

    public void paint(Graphics g) {
        for (int i = 0; i < count; i++) {
            shapes[i].show(g);
        }
        if (drawing) {
            Rubber.draw(g, createShape());
        }
    }

    /** Єдине місце, яке треба змінити для додавання нового типу фігури. */
    private Shape createShape() {
        switch (type) {
            case 0: return new PointShape(startX, startY, curX, curY);
            case 1: return new LineShape(startX, startY, curX, curY);
            case 2: return new RectShape(startX, startY, curX, curY);
            case 3: return new EllipseShape(startX, startY, curX, curY);
            case 4: return new LineOShape(startX, startY, curX, curY);
            default: return new CubeShape(startX, startY, curX, curY);
        }
    }
}
