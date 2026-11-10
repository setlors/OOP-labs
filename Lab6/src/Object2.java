import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.*;
import java.util.Locale;
import java.util.Random;

public class Object2 {
    static final int PORT = 50002;
    static final int COLS = 8;
    static final int MAX_ROWS = 12;

    static JFrame frame;
    static JLabel info;
    static JTable table;
    static DefaultTableModel model;

    static void createUi() {
        frame = new JFrame("Object2 - вектор");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String[] names = new String[COLS + 1];
        names[0] = "Індекс";
        for (int i = 1; i <= COLS; i++) names[i] = "+" + (i - 1);
        model = new DefaultTableModel(names, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        table = new JTable(model);
        info = new JLabel(" Очікування Lab6...");
        frame.add(info, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.pack();
    }

    static void fill(double[] v, double min, double max) {
        model.setRowCount(0);
        for (int s = 0; s < v.length; s += COLS) {
            Object[] row = new Object[COLS + 1];
            row[0] = s;
            for (int j = 0; j < COLS; j++) {
                row[j + 1] = s + j < v.length ? String.format(Locale.ROOT, "%.4f", v[s + j]) : "";
            }
            model.addRow(row);
        }
        info.setText(String.format(Locale.ROOT, " n = %d, [%.3f; %.3f]", v.length, min, max));
        int rows = Math.min(model.getRowCount(), MAX_ROWS);
        table.setPreferredScrollableViewportSize(new Dimension(600, table.getRowHeight() * rows));
        frame.pack();
    }

    static void toClipboard(double[] v) {
        StringBuilder sb = new StringBuilder("VECTOR " + v.length + "\n");
        for (double x : v) sb.append(String.format(Locale.ROOT, "%.6f", x)).append("\n");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
    }

    static String process(String[] p) throws Exception {
        switch (p[0]) {
            case "PING":
                return "PONG";
            case "PLACE": {
                int cx = Integer.parseInt(p[1]), bottom = Integer.parseInt(p[2]);
                SwingUtilities.invokeAndWait(() -> {
                    frame.setLocation(cx - frame.getWidth() / 2, Math.max(0, bottom - frame.getHeight()));
                    frame.setVisible(true);
                    frame.toFront();
                });
                return "OK";
            }
            case "GENERATE": {
                int n = Integer.parseInt(p[1]);
                double min = Double.parseDouble(p[2]), max = Double.parseDouble(p[3]);
                double[] v = new double[n];
                Random rnd = new Random();
                for (int i = 0; i < n; i++) v[i] = min + (max - min) * rnd.nextDouble();
                SwingUtilities.invokeAndWait(() -> fill(v, min, max));
                toClipboard(v);
                return "OK " + n;
            }
            case "EXIT":
                return "OK";
            default:
                return "ERROR";
        }
    }

    static void handle(Socket s) {
        try (s) {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            String line = in.readLine();
            if (line == null) return;
            String[] p = line.trim().split("\\s+");
            String reply;
            try {
                reply = process(p);
            } catch (Exception e) {
                reply = "ERROR " + e.getMessage();
            }
            out.println(reply);
            if (p[0].equals("EXIT")) System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket server;
        try {
            server = new ServerSocket(PORT, 10, InetAddress.getLoopbackAddress());
        } catch (IOException e) {
            return;
        }
        SwingUtilities.invokeAndWait(Object2::createUi);
        while (true) {
            Socket s = server.accept();
            new Thread(() -> handle(s)).start();
        }
    }
}