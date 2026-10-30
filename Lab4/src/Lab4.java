import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Lab4 {
    static Editor editor = new Editor();

    static JFrame frame;
    static JPanel panel;
    static JCheckBoxMenuItem[] items = new JCheckBoxMenuItem[6];
    static JToggleButton[] buttons = new JToggleButton[6];

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
        } else if (t == 3) {
            g.setColor(Color.YELLOW);
            g.fillOval(3, 6, 18, 12);
            g.setColor(Color.BLACK);
            g.drawOval(3, 6, 18, 12);
        } else if (t == 4) {
            g.drawLine(6, 18, 18, 6);
            g.setColor(Color.YELLOW);
            g.fillOval(2, 14, 8, 8);
            g.fillOval(14, 2, 8, 8);
            g.setColor(Color.BLACK);
            g.drawOval(2, 14, 8, 8);
            g.drawOval(14, 2, 8, 8);
        } else {
            g.setStroke(new BasicStroke(1.5f));
            g.drawRect(3, 9, 12, 12);
            g.drawRect(9, 3, 12, 12);
            g.drawLine(3, 9, 9, 3);
            g.drawLine(15, 9, 21, 3);
            g.drawLine(3, 21, 9, 15);
            g.drawLine(15, 21, 21, 15);
        }
        g.dispose();
        return new ImageIcon(img);
    }

    static void setType(int t) {
        editor.setType(t);
        items[t].setSelected(true);
        buttons[t].setSelected(true);
        frame.setTitle("OOP_lab4 - " + editor.getTypeName());
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame("OOP_lab4");
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                editor.paint(g);
            }
        };
        panel.setBackground(Color.WHITE);

        MouseAdapter mouse = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                editor.onPress(e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                editor.onDrag(e.getX(), e.getY());
                panel.repaint();
            }

            public void mouseReleased(MouseEvent e) {
                editor.onRelease(e.getX(), e.getY());
                panel.repaint();
            }
        };
        panel.addMouseListener(mouse);
        panel.addMouseMotionListener(mouse);
        frame.add(panel);

        JToolBar toolBar = new JToolBar();
        ButtonGroup toolGroup = new ButtonGroup();
        JMenu objects = new JMenu("Об'єкти");
        ButtonGroup menuGroup = new ButtonGroup();

        for (int i = 0; i < 6; i++) {
            final int t = i;
            buttons[i] = new JToggleButton(createIcon(i));
            buttons[i].setToolTipText(Editor.NAMES[i]);
            buttons[i].addActionListener(e -> setType(t));
            toolGroup.add(buttons[i]);
            toolBar.add(buttons[i]);

            items[i] = new JCheckBoxMenuItem(Editor.NAMES[i]);
            items[i].addActionListener(e -> setType(t));
            menuGroup.add(items[i]);
            objects.add(items[i]);
        }
        frame.add(toolBar, BorderLayout.NORTH);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu help = new JMenu("Довідка");
        JMenuItem exit = new JMenuItem("Вихід");
        JMenuItem about = new JMenuItem("Про програму");
        file.add(exit);
        help.add(about);
        menu.add(file);
        menu.add(objects);
        menu.add(help);

        exit.addActionListener(e -> System.exit(0));
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Лабораторна робота №4"));

        frame.setJMenuBar(menu);
        setType(0);
        frame.setVisible(true);
    }
}
