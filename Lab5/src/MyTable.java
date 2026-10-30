import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class MyTable extends JDialog {

    public interface TableListener {
        void rowSelected(int row);

        void rowDeleted(int row);
    }

    private final DefaultTableModel model;
    private final JTable table;
    private TableListener listener;
    private Consumer<Boolean> visibilityHandler;
    private boolean silent = false;

    public MyTable(Frame owner) {
        super(owner, "Таблиця", false);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        model = new DefaultTableModel(new String[]{"Назва", "x1", "y1", "x2", "y2"}, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && !silent && listener != null) {
                listener.rowSelected(table.getSelectedRow());
            }
        });
        table.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "del");
        table.getActionMap().put("del", new AbstractAction() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                deleteSelected();
            }
        });

        JButton delete = new JButton("Видалити");
        delete.addActionListener(e -> deleteSelected());
        JPanel bottom = new JPanel();
        bottom.add(delete);

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        setSize(420, 320);

        addComponentListener(new ComponentAdapter() {
            public void componentShown(ComponentEvent e) {
                visibilityHandler.accept(true);
            }

            public void componentHidden(ComponentEvent e) {
                visibilityHandler.accept(false);
            }
        });
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        silent = true;
        model.removeRow(row);
        silent = false;
        listener.rowDeleted(row);
    }

    public void setListener(TableListener l) {
        listener = l;
    }

    public void setVisibilityHandler(Consumer<Boolean> h) {
        visibilityHandler = h;
    }

    public void addRow(String name, int x1, int y1, int x2, int y2) {
        silent = true;
        model.addRow(new Object[]{name, x1, y1, x2, y2});
        silent = false;
    }

    public void clear() {
        silent = true;
        model.setRowCount(0);
        silent = false;
    }

    public void showTable() {
        setVisible(true);
    }

    public void hideTable() {
        setVisible(false);
    }
}