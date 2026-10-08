import java.awt.*;

public class Rubber {
    public static void draw(Graphics g, Shape s) {
        g.setColor(Color.BLUE);
        s.rubber = true;
        s.draw(g);
        s.rubber = false;
    }
}