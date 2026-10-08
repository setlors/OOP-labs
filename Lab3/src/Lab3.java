import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Lab3 {
    static Figure[] figures = new Figure[119];
    static int count = 0;
    static int type = 0;
    static int startX, startY, curX, curY;
    static boolean drawing = false;
    static JFrame frame;
    static JPanel panel;
    static JCheckBoxMenuItem[] items;
    static String[] names = {"Крапка", "Лінія", "Прямокутник", "Еліпс"};

    static Icon createIcon(int t) {
        BufferedImage img = new BufferedImage(24, 24, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.BLACK);
        if (t == 0) {
            g.fillOval(9, 9, 6, 6);
        } else if (t == 1) {
            g.drawLine(4, 20, 20, 4);
        } else if (t == 2) {
            g.drawRect(4, 6, 16, 12);
        } else {
            g.setColor(Color.YELLOW);
            g.fillOval(3, 6, 18, 12);
            g.setColor(Color.BLACK);
            g.drawOval(3, 6, 18, 12);
        }
        g.dispose();
        return new ImageIcon(img);
    }

    static void setType(int t) {
        type = t;
        items[t].setSelected(true);
        frame.setTitle("OOP_lab3 - " + names[t]);
    }

    static void onPress(MouseEvent e) {
        startX = curX = e.getX();
        startY = curY = e.getY();
        drawing = true;
    }

    static void onDrag(MouseEvent e) {
        curX = e.getX();
        curY = e.getY();
        panel.repaint();
    }

    static void onRelease(MouseEvent e) {
        curX = e.getX();
        curY = e.getY();
        drawing = false;
        if (count < figures.length) {
            if (type == 0) {
                figures[count++] = new Point(startX, startY, curX, curY);
            } else if (type == 1) {
                figures[count++] = new Line(startX, startY, curX, curY);
            } else if (type == 2) {
                figures[count++] = new Rect(startX, startY, curX, curY);
            } else {
                figures[count++] = new Ellipse(startX, startY, curX, curY);
            }
        }
        panel.repaint();
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame("OOP_lab3");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < count; i++) {
                    figures[i].draw(g);
                }
                if (drawing) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(Color.BLACK);
                    g2.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, new float[]{5, 5}, 0));
                    if (type == 0) {
                        g2.fillOval(startX - 3, startY - 3, 6, 6);
                    } else if (type == 1) {
                        g2.drawLine(startX, startY, curX, curY);
                    } else if (type == 2) {
                        g2.drawRect(Math.min(curX, 2 * startX - curX), Math.min(curY, 2 * startY - curY), 2 * Math.abs(curX - startX), 2 * Math.abs(curY - startY));
                    } else {
                        g2.drawOval(Math.min(startX, curX), Math.min(startY, curY), Math.abs(curX - startX), Math.abs(curY - startY));
                    }
                }
            }
        };
        panel.setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                onPress(e);
            }

            public void mouseDragged(MouseEvent e) {
                onDrag(e);
            }

            public void mouseReleased(MouseEvent e) {
                onRelease(e);
            }
        };
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        frame.add(panel);

        JToolBar toolBar = new JToolBar();
        JButton pointBtn = new JButton(createIcon(0));
        JButton lineBtn = new JButton(createIcon(1));
        JButton rectBtn = new JButton(createIcon(2));
        JButton ellipseBtn = new JButton(createIcon(3));
        pointBtn.setToolTipText("Крапка");
        lineBtn.setToolTipText("Лінія");
        rectBtn.setToolTipText("Прямокутник");
        ellipseBtn.setToolTipText("Еліпс");
        toolBar.add(pointBtn);
        toolBar.add(lineBtn);
        toolBar.add(rectBtn);
        toolBar.add(ellipseBtn);
        frame.add(toolBar, BorderLayout.NORTH);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu objects = new JMenu("Об'єкти");
        JMenu help = new JMenu("Довідка");

        JMenuItem exit = new JMenuItem("Вихід");
        JMenuItem about = new JMenuItem("Про програму");
        JCheckBoxMenuItem point = new JCheckBoxMenuItem("Крапка", true);
        JCheckBoxMenuItem line = new JCheckBoxMenuItem("Лінія");
        JCheckBoxMenuItem rect = new JCheckBoxMenuItem("Прямокутник");
        JCheckBoxMenuItem ellipse = new JCheckBoxMenuItem("Еліпс");
        items = new JCheckBoxMenuItem[]{point, line, rect, ellipse};

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
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Лабораторна робота №3"));
        point.addActionListener(e -> setType(0));
        line.addActionListener(e -> setType(1));
        rect.addActionListener(e -> setType(2));
        ellipse.addActionListener(e -> setType(3));
        pointBtn.addActionListener(e -> setType(0));
        lineBtn.addActionListener(e -> setType(1));
        rectBtn.addActionListener(e -> setType(2));
        ellipseBtn.addActionListener(e -> setType(3));

        frame.setJMenuBar(menu);
        frame.setVisible(true);
    }
}