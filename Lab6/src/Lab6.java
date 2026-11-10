import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;

public class Lab6 {
    static final int PORT2 = 50002, PORT3 = 50003;
    static final int W = 520, H = 260, GAP = 10;
    static final Font FONT = new Font("SansSerif", Font.PLAIN, 18);

    static JFrame frame;
    static JTextField fN, fMin, fMax;
    static JButton run;
    static JLabel status;

    static <T extends JComponent> T big(T c) {
        c.setFont(FONT);
        return c;
    }

    static Rectangle screen() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
    }

    static String send(int port, String cmd) throws IOException {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(InetAddress.getLoopbackAddress(), port), 500);
            s.setSoTimeout(8000);
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out.println(cmd);
            return in.readLine();
        }
    }

    static boolean ping(int port) {
        try {
            return "PONG".equals(send(port, "PING"));
        } catch (IOException e) {
            return false;
        }
    }

    static void ensure(int port, String cls) throws Exception {
        if (ping(port)) return;
        String java = Paths.get(System.getProperty("java.home"), "bin", "java").toString();
        new ProcessBuilder(java, "-cp", System.getProperty("java.class.path"), cls).inheritIO().start();
        for (int i = 0; i < 50; i++) {
            Thread.sleep(200);
            if (ping(port)) return;
        }
        throw new IOException("не вдалося запустити " + cls);
    }

    static void ok(String reply) throws IOException {
        if (reply == null || !reply.startsWith("OK")) {
            throw new IOException(reply == null ? "немає відповіді" : reply);
        }
    }

    static void execute() {
        int n;
        double min, max;
        try {
            n = Integer.parseInt(fN.getText().trim());
            min = Double.parseDouble(fMin.getText().trim().replace(',', '.'));
            max = Double.parseDouble(fMax.getText().trim().replace(',', '.'));
            if (n < 1 || n > 100000) throw new NumberFormatException("n має бути від 1 до 100000");
            if (min >= max) throw new NumberFormatException("Min має бути менше Max");
        } catch (NumberFormatException e) {
            status.setText(" Помилка введення: " + e.getMessage());
            return;
        }
        Rectangle ar = screen();
        int cx = frame.getX() + frame.getWidth() / 2;
        int top = frame.getY();
        int bottom = frame.getY() + frame.getHeight();
        int w3 = Math.min(700, ar.width);
        int x3 = Math.max(ar.x, cx - w3 / 2);
        int y3 = bottom + GAP;
        int h3 = Math.max(250, Math.min(420, ar.y + ar.height - y3));
        String place2 = "PLACE " + cx + " " + (top - GAP);
        String bounds3 = "BOUNDS " + x3 + " " + y3 + " " + w3 + " " + h3;
        String gen = "GENERATE " + n + " " + min + " " + max;

        run.setEnabled(false);
        status.setText(" Виконується...");
        new Thread(() -> {
            String result;
            try {
                ensure(PORT2, "Object2");
                ensure(PORT3, "Object3");
                ok(send(PORT3, bounds3));
                ok(send(PORT2, gen));
                ok(send(PORT2, place2));
                ok(send(PORT3, "READ"));
                result = " Готово";
            } catch (Exception e) {
                result = " Помилка: " + e.getMessage();
            }
            String text = result;
            SwingUtilities.invokeLater(() -> {
                status.setText(text);
                run.setEnabled(true);
                frame.toFront();
                frame.requestFocus();
            });
        }).start();
    }

    static void exit() {
        for (int port : new int[]{PORT2, PORT3}) {
            try {
                send(port, "EXIT");
            } catch (IOException e) {
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        frame = new JFrame("Lab6");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });

        fN = big(new JTextField("20"));
        fMin = big(new JTextField("-10"));
        fMax = big(new JTextField("10"));
        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        form.add(big(new JLabel("n:")));
        form.add(fN);
        form.add(big(new JLabel("Min:")));
        form.add(fMin);
        form.add(big(new JLabel("Max:")));
        form.add(fMax);

        run = big(new JButton("Виконати"));
        run.addActionListener(e -> execute());
        status = big(new JLabel(" Введіть параметри"));
        JPanel bottom = new JPanel(new BorderLayout(8, 8));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        bottom.add(run, BorderLayout.NORTH);
        bottom.add(status, BorderLayout.SOUTH);

        frame.add(form, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);
        frame.setSize(W, H);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}