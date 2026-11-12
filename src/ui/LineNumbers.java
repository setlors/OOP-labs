package ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class LineNumbers extends JComponent {
    private final JTextArea area;

    public LineNumbers(JTextArea area) {
        this.area = area;
        setFont(area.getFont());
        area.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                update();
            }
            public void removeUpdate(DocumentEvent e) {
                update();
            }
            public void changedUpdate(DocumentEvent e) {
                update();
            }
        });
        area.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
        update();
    }

    private void update() {
        int digits = Math.max(2, String.valueOf(area.getLineCount()).length());
        int width = getFontMetrics(getFont()).charWidth('0') * digits + 24;
        setPreferredSize(new Dimension(width, 1));
        revalidate();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Theme.PANEL);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Theme.BORDER);
        g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
        g2.setColor(Theme.GRAY);
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        Rectangle clip = g.getClipBounds();
        try {
            for (int i = 0; i < area.getLineCount(); i++) {
                int y = (int) area.modelToView2D(area.getLineStartOffset(i)).getY();
                if (y > clip.y + clip.height) break;
                String s = String.valueOf(i + 1);
                g2.drawString(s, getWidth() - 12 - fm.stringWidth(s), y + fm.getAscent());
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}