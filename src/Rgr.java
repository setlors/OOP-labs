import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Rgr {
    static Editor editor = Editor.getInstance();

    static JFrame frame;
    static JTextArea area;

    static void save() {
        if (editor.getFile() != null) {
            editor.save(editor.getFile());
            return;
        }
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
            editor.save(fc.getSelectedFile());
            setTitle();
        }
    }

    static void setTitle() {
        String name = editor.getFile() == null ? "Без назви" : editor.getFile().getName();
        frame.setTitle("OOP_RGR19 - " + name);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame();
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        area = new JTextArea();
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        frame.add(new JScrollPane(area));
        editor.attach(area);

        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu edit = new JMenu("Правка");
        JMenu process = new JMenu("Обробка");
        JMenu view = new JMenu("Вигляд");
        JMenu help = new JMenu("Довідка");

        JMenuItem newItem = new JMenuItem("Новий");
        JMenuItem open = new JMenuItem("Відкрити");
        JMenuItem save = new JMenuItem("Зберегти");
        JMenuItem exit = new JMenuItem("Вихід");
        JMenuItem undo = new JMenuItem("Скасувати");
        JMenuItem redo = new JMenuItem("Повторити");
        JMenuItem find = new JMenuItem("Знайти");
        JMenuItem replace = new JMenuItem("Замінити все");
        JMenuItem stats = new JMenuItem("Статистика");
        JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Перенос рядків");
        JMenuItem about = new JMenuItem("Про програму");

        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, mask));
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, mask));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, mask));
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, mask));

        file.add(newItem);
        file.add(open);
        file.add(save);
        file.add(exit);
        edit.add(undo);
        edit.add(redo);
        edit.add(find);
        edit.add(replace);
        view.add(wrap);
        view.add(stats);
        help.add(about);

        for (int i = 0; i < TextOperations.size(); i++) {
            final int op = i;
            JMenuItem it = new JMenuItem(TextOperations.name(i));
            it.addActionListener(e -> editor.process(op));
            process.add(it);
        }

        menu.add(file);
        menu.add(edit);
        menu.add(process);
        menu.add(view);
        menu.add(help);
        frame.setJMenuBar(menu);

        newItem.addActionListener(e -> {
            editor.newDocument();
            setTitle();
        });
        open.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                editor.load(fc.getSelectedFile());
                setTitle();
            }
        });
        save.addActionListener(e -> save());
        exit.addActionListener(e -> System.exit(0));
        undo.addActionListener(e -> editor.undo());
        redo.addActionListener(e -> editor.redo());
        find.addActionListener(e -> {
            String what = JOptionPane.showInputDialog(frame, "Знайти:");
            if (what != null && !what.isEmpty() && !editor.find(what)) {
                JOptionPane.showMessageDialog(frame, "Не знайдено");
            }
        });
        replace.addActionListener(e -> {
            String what = JOptionPane.showInputDialog(frame, "Знайти:");
            if (what == null || what.isEmpty()) return;
            String with = JOptionPane.showInputDialog(frame, "Замінити на:");
            if (with != null) editor.replaceAll(what, with);
        });
        wrap.addActionListener(e -> {
            area.setLineWrap(wrap.isSelected());
            area.setWrapStyleWord(wrap.isSelected());
        });
        stats.addActionListener(e -> JOptionPane.showMessageDialog(frame, editor.stats()));
        about.addActionListener(e -> JOptionPane.showMessageDialog(frame,
                "Розрахункова робота, варіант 19\nПрограма для вводу та обробки текстів"));

        setTitle();
        frame.setVisible(true);
    }
}