import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Lab5 {
    static MyEditor editor = MyEditor.getInstance();

    static JFrame frame;
    static JPanel panel;
    static MyTable table;
    static JCheckBoxMenuItem[] items = new JCheckBoxMenuItem[ShapeRegistry.size()];
    static JToggleButton[] buttons = new JToggleButton[ShapeRegistry.size()];

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
        frame.setTitle("OOP_lab5 - " + editor.getTypeName());
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame("OOP_lab5");
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

        table = new MyTable(frame);
        editor.attachTable(table, panel::repaint);

        JToolBar toolBar = new JToolBar();
        ButtonGroup toolGroup = new ButtonGroup();
        JMenu objects = new JMenu("Об'єкти");
        ButtonGroup menuGroup = new ButtonGroup();

        for (int i = 0; i < ShapeRegistry.size(); i++) {
            final int t = i;
            buttons[i] = new JToggleButton(createIcon(i));
            buttons[i].setToolTipText(ShapeRegistry.name(i));
            buttons[i].addActionListener(e -> setType(t));
            toolGroup.add(buttons[i]);
            toolBar.add(buttons[i]);

            items[i] = new JCheckBoxMenuItem(ShapeRegistry.name(i));
            items[i].addActionListener(e -> setType(t));
            menuGroup.add(items[i]);
            objects.add(items[i]);
        }
        frame.add(toolBar, BorderLayout.NORTH);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu view = new JMenu("Вигляд");
        JMenu help = new JMenu("Довідка");
        JMenuItem open = new JMenuItem("Відкрити");
        JMenuItem save = new JMenuItem("Зберегти");
        JMenuItem exit = new JMenuItem("Вихід");
        JMenuItem about = new JMenuItem("Про програму");
        JCheckBoxMenuItem tableItem = new JCheckBoxMenuItem("Таблиця");
        file.add(open);
        file.add(save);
        file.add(exit);
        view.add(tableItem);
        help.add(about);
        menu.add(file);
        menu.add(objects);
        menu.add(view);
        menu.add(help);

        open.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                editor.load(fc.getSelectedFile());
            }
        });
        save.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                editor.save(fc.getSelectedFile());
            }
        });
        exit.addActionListener(e -> System.exit(0));
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Лабораторна робота №5"));

        tableItem.addActionListener(e -> {
            if (tableItem.isSelected()) table.showTable();
            else table.hideTable();
        });
        table.setVisibilityHandler(tableItem::setSelected);

        frame.setJMenuBar(menu);
        setType(0);
        frame.setVisible(true);
        table.setLocation(frame.getX() + frame.getWidth(), frame.getY());
    }
}