import java.awt.*;

public class Rubber {
    public static void draw(Graphics g, Shape s) {
        s.rubber = true;
        s.show(g);
        s.rubber = false;
    }
}