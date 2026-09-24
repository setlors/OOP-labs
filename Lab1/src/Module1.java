// В1: 0, В2: 1
import javax.swing.*;
import java.awt.*;

public class Module1 extends JDialog {
    private String output;
    public Module1(JDialog parent) {
        super(parent, "Робота 1", true);
        setSize(300, 200);
        setLocationRelativeTo(null);
        JLabel label = new JLabel("Enter your text: ");
        JTextField text = new JTextField();
        JButton tak = new JButton("Так");
        JButton cancel = new JButton("Відміна");
        JPanel panel = new JPanel();
        panel.add(tak);
        panel.add(cancel);

        tak.addActionListener(e -> {
            output = text.getText();
            dispose();
        });
        cancel.addActionListener(e -> dispose());

        add(label, BorderLayout.NORTH);
        add(text, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

    }
    public String getOutput() {
        return output;
    }
}
