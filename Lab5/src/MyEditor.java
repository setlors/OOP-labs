import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class MyEditor implements MyTable.TableListener {
    private static class Holder {
        static final MyEditor INSTANCE = new MyEditor();
    }

    public static MyEditor getInstance() {
        return Holder.INSTANCE;
    }

    private MyEditor() {
    }

    private final Shape[] shapes = new Shape[119];
    private int count = 0;
    private int type = 0;
    private int startX, startY, curX, curY;
    private boolean drawing = false;
    private int selected = -1;
    private MyTable table;
    private Runnable repaintHook;

    public void attachTable(MyTable t, Runnable repaint) {
        table = t;
        repaintHook = repaint;
        table.setListener(this);
    }

    public void setType(int t) {
        type = t;
    }

    public String getTypeName() {
        return ShapeRegistry.name(type);
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
        addShape(createShape());
    }

    private boolean addShape(Shape s) {
        if (count >= shapes.length) return false;
        shapes[count++] = s;
        table.addRow(s.getName(), s.getX1(), s.getY1(), s.getX2(), s.getY2());
        return true;
    }

    public void paint(Graphics g) {
        for (int i = 0; i < count; i++) {
            shapes[i].selected = (i == selected);
            shapes[i].show(g);
        }
        if (drawing) {
            Rubber.draw(g, createShape());
        }
    }

    private Shape createShape() {
        return ShapeRegistry.create(type, startX, startY, curX, curY);
    }

    public void rowSelected(int row) {
        selected = row;
        repaintHook.run();
    }

    public void rowDeleted(int row) {
        for (int i = row; i < count - 1; i++) {
            shapes[i] = shapes[i + 1];
        }
        count--;
        selected = -1;
        repaintHook.run();
    }

    public void save(File f) {
        try (BufferedWriter w = Files.newBufferedWriter(f.toPath())) {
            for (int i = 0; i < count; i++) {
                Shape s = shapes[i];
                w.write(s.getName() + ";" + s.getX1() + ";" + s.getY1() + ";"
                        + s.getX2() + ";" + s.getY2());
                w.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(File f) {
        List<String> lines;
        try {
            lines = Files.readAllLines(f.toPath());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        count = 0;
        selected = -1;
        table.clear();
        for (String line : lines) {
            Shape s = parseLine(line);
            if (s != null && !addShape(s)) break;
        }
        repaintHook.run();
    }

    private Shape parseLine(String line) {
        String[] p = line.trim().split(";");
        if (p.length != 5) return null;
        int t = ShapeRegistry.indexOf(p[0]);
        if (t < 0) return null;
        try {
            return ShapeRegistry.create(t, Integer.parseInt(p[1]), Integer.parseInt(p[2]),
                    Integer.parseInt(p[3]), Integer.parseInt(p[4]));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}