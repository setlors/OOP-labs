import java.awt.*;

public abstract class Shape {
    protected int x1, y1, x2, y2;
    public boolean rubber = false;

    public Shape(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    protected void setOutline(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        if (rubber) {
            g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10, new float[]{5, 5}, 0));
        } else {
            g2.setStroke(new BasicStroke(1));
        }
    }

    public abstract void show(Graphics g);
}
