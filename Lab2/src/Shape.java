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
        if (!rubber) {
            g.setColor(Color.BLACK);
        }
    }
    
    public abstract void draw(Graphics g);
}