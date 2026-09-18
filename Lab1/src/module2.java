// В1: 0, В2: 1
import javax.swing.*;
import java.awt.*;

public class module2 extends JDialog {
    private int number;
    public module2(JDialog parent) {
        super(parent, "Робота 2", true);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JLabel numbers = new JLabel("1", SwingConstants.CENTER);
        JScrollBar scroll = new JScrollBar(JScrollBar.HORIZONTAL);
        scroll.setVisibleAmount(1);
        scroll.setMinimum(1);
        scroll.setMaximum(101);
        scroll.addAdjustmentListener(e -> {
            numbers.setText("" + scroll.getValue());
        });
        JButton tak = new JButton("Так");
        JButton cancel = new JButton("Відміна");
        JPanel panel = new JPanel();
        panel.add(tak);
        panel.add(cancel);
        tak.addActionListener(e -> {
            number = scroll.getValue();
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        add(panel, BorderLayout.NORTH);
        add(numbers, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);
    }
    public int getNumber() {
        return number;
    }
}
