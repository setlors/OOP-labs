package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Theme {
    public static final Color ACCENT = new Color(66, 133, 244);
    public static final Color BG = new Color(250, 250, 252);
    public static final Color PANEL = new Color(243, 244, 248);
    public static final Color BORDER = new Color(220, 223, 230);
    public static final Color TEXT = new Color(40, 44, 52);
    public static final Color GRAY = new Color(120, 126, 140);
    public static final Color HOVER = new Color(225, 232, 248);
    public static final Color SELECTION = new Color(200, 220, 255);

    public static final Font UI_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    public static final Font BOLD_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 12);
    public static final Font EDIT_FONT = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    public static Icon icon(int t) {
        BufferedImage img = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(1.8f));
        g.setColor(ACCENT);
        if (t == 0) {          //новий
            g.drawRect(5, 3, 10, 14);
            g.drawLine(8, 8, 12, 8);
            g.drawLine(8, 12, 12, 12);
        } else if (t == 1) {   //відкрити
            g.drawRect(3, 6, 14, 10);
            g.fillRect(3, 4, 6, 2);
        } else if (t == 2) {   //зберегти
            g.drawRect(3, 3, 14, 14);
            g.drawRect(6, 3, 8, 5);
            g.fillRect(6, 12, 8, 5);
        } else if (t == 3) {   //скасувати
            g.drawLine(4, 10, 16, 10);
            g.drawLine(4, 10, 8, 6);
            g.drawLine(4, 10, 8, 14);
        } else if (t == 4) {   //повторити
            g.drawLine(4, 10, 16, 10);
            g.drawLine(16, 10, 12, 6);
            g.drawLine(16, 10, 12, 14);
        } else {               //знайти
            g.drawOval(3, 3, 9, 9);
            g.drawLine(11, 11, 17, 17);
        }
        g.dispose();
        return new ImageIcon(img);
    }
}