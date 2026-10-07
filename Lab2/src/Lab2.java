// Ж = 18
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Lab2 {
    static Shape[] shapes = new Shape[118];
    static int count = 0;
    static int type = 2;
    static int startX, startY, curX, curY;
    static boolean drawing = false;

    public static void main(String[] args) {
        JFrame frame = new JFrame("OOP_lab2");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < count; i++) {
                    shapes[i].draw(g);
                }
                if (drawing) {
                    g.setColor(Color.BLUE);
                    if (type == 0) {
                        g.fillOval(startX - 3, startY - 3, 6, 6);
                    } else if (type == 1) {
                        g.drawLine(startX, startY, curX, curY);
                    } else if (type == 2) {
                        g.drawRect(Math.min(startX, curX), Math.min(startY, curY), Math.abs(curX - startX), Math.abs(curY - startY));
                    } else {
                        g.drawOval(Math.min(curX, 2 * startX - curX), Math.min(curY, 2 * startY - curY), 2 * Math.abs(curX - startX), 2 * Math.abs(curY - startY));
                    }
                }
            }
        };
        panel.setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startX = curX = e.getX();
                startY = curY = e.getY();
                drawing = true;
            }

            public void mouseDragged(MouseEvent e) {
                curX = e.getX();
                curY = e.getY();
                panel.repaint();
            }

            public void mouseReleased(MouseEvent e) {
                curX = e.getX();
                curY = e.getY();
                drawing = false;
                if (count < shapes.length) {
                    if (type == 0) {
                        shapes[count++] = new PointShape(startX, startY, curX, curY);
                    } else if (type == 1) {
                        shapes[count++] = new LineShape(startX, startY, curX, curY);
                    } else if (type == 2) {
                        shapes[count++] = new RectShape(startX, startY, curX, curY);
                    } else {
                        shapes[count++] = new EllipseShape(startX, startY, curX, curY);
                    }
                }
                panel.repaint();
            }
        };
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        frame.add(panel);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu objects = new JMenu("Об'єкти");
        JMenu help = new JMenu("Довідка");

        JMenuItem exit = new JMenuItem("Вихід");
        JMenuItem about = new JMenuItem("Про програму");
        JCheckBoxMenuItem point = new JCheckBoxMenuItem("Крапка");
        JCheckBoxMenuItem line = new JCheckBoxMenuItem("Лінія");
        JCheckBoxMenuItem rect = new JCheckBoxMenuItem("Прямокутник", true);
        JCheckBoxMenuItem ellipse = new JCheckBoxMenuItem("Еліпс");

        ButtonGroup group = new ButtonGroup();
        group.add(point);
        group.add(line);
        group.add(rect);
        group.add(ellipse);

        file.add(exit);
        help.add(about);
        objects.add(point);
        objects.add(line);
        objects.add(rect);
        objects.add(ellipse);

        menu.add(file);
        menu.add(objects);
        menu.add(help);

        exit.addActionListener(e -> System.exit(0));
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Лабораторна робота №2"));
        point.addActionListener(e -> type = 0);
        line.addActionListener(e -> type = 1);
        rect.addActionListener(e -> type = 2);
        ellipse.addActionListener(e -> type = 3);

        frame.setJMenuBar(menu);
        frame.setVisible(true);
    }
}