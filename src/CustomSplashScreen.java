import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class CustomSplashScreen {

    private JFrame frame;
    private JLabel image;
    private JLabel text;
    private JLabel message;
    private JProgressBar progressBar;
    private final float hue = 0.0f;

    public CustomSplashScreen(){
        createGUI();
        runProgressBar();
    }

    private void createGUI(){
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setSize(500,500);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);

        //Add gradient background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = Color.getHSBColor(hue, 0.7f, 1.0f);
                Color color2 = Color.getHSBColor((hue + 0.5f) % 1.0f, 0.7f, 1.0f);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setBounds(0, 0, 500, 500);
        gradientPanel.setLayout(null);
        frame.add(gradientPanel);
        addImage(gradientPanel);
        addText(gradientPanel);
        addProgressBar(gradientPanel);
        animateLogo();
        frame.setVisible(true);
    }

    private void addImage(JPanel gradientPanel) {
        URL imageURL = getClass().getResource("/key-lock.png");
        if (imageURL != null) {
            image = new JLabel(new ImageIcon(imageURL));
            image.setBounds(50, 50, 400, 200);
            gradientPanel.add(image);
        } else {
            System.out.println("Image not found!");
        }
    }

    private void animateLogo() {
        new Thread(() -> {
            try {
                int y = 50, direction = 1;
                while (progressBar.getValue() < 100) {
                    y += direction;
                    if (y <= 40 || y >= 60) direction *= -1;
                    final int finalY = y;
                    SwingUtilities.invokeLater(() -> image.setBounds(50, finalY, 400, 200));
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void addText(JPanel gradientPanel) {
        text = new JLabel("Welcome to Secure Manager", JLabel.CENTER);
        text.setFont(new Font("Roboto", Font.BOLD, 22));
        text.setBounds(50, 270, 400, 30);
        text.setForeground(Color.WHITE);
        gradientPanel.add(text);
    }

    private void addProgressBar(JPanel gradientPanel) {
        progressBar = new JProgressBar();
        progressBar.setBounds(100, 350, 300, 20);
        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setFont(new Font("Arial", Font.BOLD, 12));
        progressBar.setForeground(new Color(0x00FF00)); // Bright green color
        progressBar.setBackground(Color.DARK_GRAY);
        gradientPanel.add(progressBar);

        message = new JLabel("Loading...", JLabel.CENTER);
        message.setFont(new Font("Roboto", Font.ITALIC, 14));
        message.setBounds(100, 380, 300, 20);
        message.setForeground(Color.WHITE);
        gradientPanel.add(message);
    }

    private void runProgressBar() {
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i++) {
                    Thread.sleep(40); // Adjust the sleep time for faster/slower progress
                    final int progress = i;
                    SwingUtilities.invokeLater(() -> progressBar.setValue(progress));
                }
                SwingUtilities.invokeLater(frame::dispose); // Close splash screen
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void waitForCompletion() {
        try {
            Thread.sleep(4000); // Ensure splash runs long enough for progress
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
