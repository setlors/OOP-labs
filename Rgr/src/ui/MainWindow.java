package ui;

import logic.Editor;
import logic.TextOperations;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private final Editor editor = Editor.getInstance();
    private final JTextArea area = new JTextArea();
    private final JLabel position = new JLabel();
    private final JLabel counts = new JLabel();
    private LineNumbers lineNumbers;

    public MainWindow() {
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BG);

        area.setFont(Theme.EDIT_FONT);
        area.setMargin(new Insets(8, 10, 8, 10));
        area.setBackground(Color.WHITE);
        area.setForeground(Theme.TEXT);
        area.setCaretColor(Theme.ACCENT);
        area.setSelectionColor(Theme.SELECTION);
        area.setTabSize(4);
        editor.attach(area);

        area.addCaretListener(e -> updateStatus());
        area.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                updateStatus();
            }
            public void removeUpdate(DocumentEvent e) {
                updateStatus();
            }
            public void changedUpdate(DocumentEvent e) {
                updateStatus();
            }
        });

        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        lineNumbers = new LineNumbers(area);
        scroll.setRowHeaderView(lineNumbers);

        setJMenuBar(createMenu());
        add(createToolBar(), BorderLayout.NORTH);
        add(createSidePanel(), BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);

        updateTitle();
        updateStatus();
    }

    private JMenuItem item(String text, int key, ActionListener a) {
        JMenuItem it = new JMenuItem(text);
        if (key != 0) {
            int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
            it.setAccelerator(KeyStroke.getKeyStroke(key, mask));
        }
        it.addActionListener(a);
        return it;
    }

    private JMenuBar createMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu file = new JMenu("Файл");
        JMenu edit = new JMenu("Правка");
        JMenu process = new JMenu("Обробка");
        JMenu view = new JMenu("Вигляд");
        JMenu help = new JMenu("Довідка");

        file.add(item("Новий", KeyEvent.VK_N, e -> newFile()));
        file.add(item("Відкрити", KeyEvent.VK_O, e -> openFile()));
        file.add(item("Зберегти", KeyEvent.VK_S, e -> saveFile()));
        file.addSeparator();
        file.add(item("Вихід", 0, e -> System.exit(0)));
        edit.add(item("Скасувати", KeyEvent.VK_Z, e -> editor.undo()));
        edit.add(item("Повторити", KeyEvent.VK_Y, e -> editor.redo()));
        edit.addSeparator();
        edit.add(item("Знайти", KeyEvent.VK_F, e -> find()));
        edit.add(item("Замінити все", KeyEvent.VK_H, e -> replace()));

        for (int i = 0; i < TextOperations.size(); i++) {
            final int op = i;
            process.add(item(TextOperations.name(i), 0, e -> editor.process(op)));
        }
        JCheckBoxMenuItem wrap = new JCheckBoxMenuItem("Перенос рядків");
        wrap.addActionListener(e -> setWrap(wrap.isSelected()));
        view.add(wrap);
        view.add(item("Статистика", 0, e -> JOptionPane.showMessageDialog(this,
                editor.stats(), "Статистика", JOptionPane.INFORMATION_MESSAGE)));

        help.add(item("Про програму", 0, e -> JOptionPane.showMessageDialog(this,
                "Розрахункова робота, варіант 19\nПрограма для вводу та обробки текстів")));

        menu.add(file);
        menu.add(edit);
        menu.add(process);
        menu.add(view);
        menu.add(help);
        return menu;
    }
    private Button toolButton(String tip, int icon, ActionListener a) {
        Button b = new Button(null, Theme.icon(icon));
        b.setToolTipText(tip);
        b.addActionListener(a);
        return b;
    }

    private JToolBar createToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.setBackground(Theme.PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        bar.add(toolButton("Новий", 0, e -> newFile()));
        bar.add(toolButton("Відкрити", 1, e -> openFile()));
        bar.add(toolButton("Зберегти", 2, e -> saveFile()));
        bar.addSeparator();
        bar.add(toolButton("Скасувати", 3, e -> editor.undo()));
        bar.add(toolButton("Повторити", 4, e -> editor.redo()));
        bar.addSeparator();
        bar.add(toolButton("Знайти", 5, e -> find()));
        return bar;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Theme.PANEL);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Theme.BORDER),
                BorderFactory.createEmptyBorder(14, 10, 14, 10)));

        JLabel title = new JLabel("ОБРОБКА ТЕКСТУ");
        title.setFont(Theme.BOLD_FONT);
        title.setForeground(Theme.GRAY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createVerticalStrut(8));

        for (int i = 0; i < TextOperations.size(); i++) {
            final int op = i;
            Button b = new Button(TextOperations.name(i), null);
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setAlignmentX(Component.LEFT_ALIGNMENT);
            b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
            b.addActionListener(e -> editor.process(op));
            panel.add(b);
            panel.add(Box.createVerticalStrut(2));
        }

        panel.add(Box.createVerticalGlue());
        JLabel hint = new JLabel("<html>Діє на виділений фрагмент<br>або на весь текст</html>");
        hint.setFont(Theme.SMALL_FONT);
        hint.setForeground(Theme.GRAY);
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(hint);
        return panel;
    }

    private JPanel createStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Theme.PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        position.setFont(Theme.SMALL_FONT);
        position.setForeground(Theme.GRAY);
        counts.setFont(Theme.SMALL_FONT);
        counts.setForeground(Theme.GRAY);
        bar.add(position, BorderLayout.WEST);
        bar.add(counts, BorderLayout.EAST);
        return bar;
    }

    private void updateTitle() {
        String name = editor.getFile() == null ? "Без назви" : editor.getFile().getName();
        setTitle("OOP_RGR19 - " + name);
    }

    private void updateStatus() {
        try {
            int pos = area.getCaretPosition();
            int line = area.getLineOfOffset(pos);
            int col = pos - area.getLineStartOffset(line);
            position.setText("Рядок " + (line + 1) + ", стовпець " + (col + 1));
        } catch (BadLocationException e) {
            position.setText("");
        }
        String text = area.getText();
        String trimmed = text.trim();
        int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
        counts.setText("Слів: " + words + "     Символів: " + text.length());
    }

    private void newFile() {
        editor.newDocument();
        updateTitle();
    }

    private void openFile() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            editor.load(fc.getSelectedFile());
            updateTitle();
        }
    }

    private void saveFile() {
        if (editor.getFile() != null) {
            editor.save(editor.getFile());
            return;
        }
        JFileChooser fc = new JFileChooser();
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            editor.save(fc.getSelectedFile());
            updateTitle();
        }
    }

    private void find() {
        String what = JOptionPane.showInputDialog(this, "Знайти:");
        if (what != null && !what.isEmpty() && !editor.find(what)) {
            JOptionPane.showMessageDialog(this, "Не знайдено");
        }
    }

    private void replace() {
        String what = JOptionPane.showInputDialog(this, "Знайти:");
        if (what == null || what.isEmpty()) return;
        String with = JOptionPane.showInputDialog(this, "Замінити на:");
        if (with != null) editor.replaceAll(what, with);
    }

    private void setWrap(boolean wrap) {
        area.setLineWrap(wrap);
        area.setWrapStyleWord(wrap);
        lineNumbers.repaint();
    }
}