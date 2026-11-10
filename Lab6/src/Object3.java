import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.geom.Path2D;
import java.io.*;
import java.net.*;
import java.util.Locale;

public class Object3 {
    static final int PORT = 50003;

    static JFrame frame;
    static JLabel info;
    static PlotPanel plot;

    static class PlotPanel extends JPanel {
        double[] y = new double[0];

        PlotPanel() {
            setBackground(Color.WHITE);
        }

        void setData(double[] d) {
            y = d;
            repaint();
        }

        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (y.length == 0) return;

            int L = 70, R = 20, T = 20, B = 45;
            int w = getWidth() - L - R, h = getHeight() - T - B;
            if (w < 10 || h < 10) return;

            int n = y.length;
            double xmax = Math.max(1, n - 1);
            double ymin = y[0], ymax = y[0];
            for (double v : y) {
                ymin = Math.min(ymin, v);
                ymax = Math.max(ymax, v);
            }
            if (ymax - ymin < 1e-12) {
                ymin -= 1;
                ymax += 1;
            }
            double pad = (ymax - ymin) * 0.05;
            ymin -= pad;
            ymax += pad;

            FontMetrics fm = g.getFontMetrics();
            int xt = Math.max(1, Math.min(10, n - 1)), yt = 10;
            for (int i = 0; i <= xt; i++) {
                int px = L + (int) Math.round(w * (double) i / xt);
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(px, T, px, T + h);
                g.setColor(Color.BLACK);
                String s = String.format(Locale.ROOT, "%.0f", xmax * i / xt);
                g.drawString(s, px - fm.stringWidth(s) / 2, T + h + 18);
            }
            for (int i = 0; i <= yt; i++) {
                int py = T + h - (int) Math.round(h * (double) i / yt);
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(L, py, L + w, py);
                g.setColor(Color.BLACK);
                String s = String.format(Locale.ROOT, "%.2f", ymin + (ymax - ymin) * i / yt);
                g.drawString(s, L - 8 - fm.stringWidth(s), py + fm.getAscent() / 2 - 1);
            }
            g.drawRect(L, T, w, h);
            g.drawString("x", L + w / 2, T + h + 38);
            g.drawString("y", 10, T + h / 2);

            Path2D path = new Path2D.Double();
            for (int i = 0; i < n; i++) {
                double px = L + w * i / xmax;
                double py = T + h - h * (y[i] - ymin) / (ymax - ymin);
                if (i == 0) path.moveTo(px, py);
                else path.lineTo(px, py);
            }
            g.setColor(Color.BLUE);
            g.setStroke(new BasicStroke(2));
            g.draw(path);
        }
    }

    static void createUi() {
        frame = new JFrame("Object3 - графік");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        plot = new PlotPanel();
        info = new JLabel(" Очікування Lab6...");
        frame.add(info, BorderLayout.NORTH);
        frame.add(plot, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    static double[] readVector() throws Exception {
        var clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        if (!clip.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
            throw new Exception("у буфері обміну немає тексту");
        }
        String[] lines = ((String) clip.getData(DataFlavor.stringFlavor)).trim().split("\\r?\\n");
        String[] head = lines[0].trim().split("\\s+");
        if (head.length != 2 || !head[0].equals("VECTOR")) {
            throw new Exception("у буфері обміну немає даних Object2");
        }
        int n = Integer.parseInt(head[1]);
        if (lines.length - 1 != n) throw new Exception("кількість значень не збігається з n");
        double[] v = new double[n];
        for (int i = 0; i < n; i++) v[i] = Double.parseDouble(lines[i + 1].trim());
        return v;
    }

    static String process(String[] p) throws Exception {
        switch (p[0]) {
            case "PING":
                return "PONG";
            case "BOUNDS": {
                int x = Integer.parseInt(p[1]), y = Integer.parseInt(p[2]);
                int w = Integer.parseInt(p[3]), h = Integer.parseInt(p[4]);
                SwingUtilities.invokeAndWait(() -> {
                    frame.setBounds(x, y, w, h);
                    frame.setVisible(true);
                    frame.toFront();
                });
                return "OK";
            }
            case "READ": {
                double[] v = readVector();
                SwingUtilities.invokeAndWait(() -> {
                    plot.setData(v);
                    info.setText("n = " + v.length);
                });
                return "OK " + v.length;
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
        SwingUtilities.invokeAndWait(Object3::createUi);
        while (true) {
            Socket s = server.accept();
            new Thread(() -> handle(s)).start();
        }
    }
}