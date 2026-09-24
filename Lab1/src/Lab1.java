// В1: 0, В2: 1
import javax.swing.*;
import java.awt.*;

public class Lab1 {
    public static void main(String[] args) {
        JDialog mainDialog = new JDialog();
        mainDialog.setTitle("Menu");
        mainDialog.setSize(400, 400);
        mainDialog.setLocationRelativeTo(null);
        mainDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        JLabel l1 = new JLabel();
        JLabel l2 = new JLabel();
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(l1);
        mainPanel.add(l2);
        mainPanel.add(Box.createVerticalGlue());
        l1.setAlignmentX(Component.CENTER_ALIGNMENT);
        l2.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainDialog.add(mainPanel);

        JMenuBar menu = new JMenuBar();
        JMenuItem moduleI1 = new JMenuItem("Робота 1");
        JMenuItem moduleI2 = new JMenuItem("Робота 2");
        menu.add(moduleI1);
        menu.add(moduleI2);

        Font font = new Font("Arial", Font.BOLD, 20);
        l1.setFont(font);
        l2.setFont(font);

        moduleI1.addActionListener(e -> {
            Module1 dialog1 = new Module1(mainDialog);
            dialog1.setVisible(true);
            String output = dialog1.getOutput();
            l1.setText(output);
        });
        moduleI2.addActionListener(e -> {
            Module2 dialog2 = new Module2(mainDialog);
            dialog2.setVisible(true);
            int number = dialog2.getNumber();
            if(number != 0) {
                l2.setText(String.valueOf(number));
            }
        });
        mainDialog.setJMenuBar(menu);
        mainDialog.setVisible(true);
    }
}
