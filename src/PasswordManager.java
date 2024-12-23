import javax.swing.*;
import java.awt.*;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public class PasswordManager {

    private JFrame frame;
    private Container container;

    // Data storage
    private HashtablePassword passwordStorage = new HashtablePassword(15, 0.5F, new LinearProbing());
    //private HashMap<String, String> passwordStorage = new HashMap<>();
    private ArrayList<String> notes = new ArrayList<>();

    public PasswordManager() {
        initializeUI();
    }

    private void initializeUI() {
        // Frame Setup
        frame = new JFrame("Password Manager");
        frame.setSize(400, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Gradient Background
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = Color.getHSBColor(0.6f, 0.7f, 1.0f);
                Color color2 = Color.getHSBColor(0.2f, 0.7f, 1.0f);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(null);
        frame.add(gradientPanel);

        // Add Buttons
        addButton(gradientPanel, "GENERATE PASSWORD", 20, e -> generatePassword());
        addButton(gradientPanel, "ENCRYPT TEXT", 90, e -> encryptText());
        addButton(gradientPanel, "DECRYPT TEXT", 160, e -> decryptText());
        addButton(gradientPanel, "STORE PASSWORD", 230, e -> storePassword());
        addButton(gradientPanel, "SEARCH PASSWORD", 300, e -> searchPassword());
        addButton(gradientPanel, "DELETE PASSWORD", 370, e -> deletePassword());
        addButton(gradientPanel, "ADD NOTE", 440, e -> addNote());
        addButton(gradientPanel, "GET NOTE", 510, e -> getNote());

        frame.setVisible(true);
    }

    private void addButton(JPanel panel, String text, int yPosition, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setBounds(90, yPosition, 220, 40);
        button.setFont(new Font("Roboto", Font.BOLD, 16)); // Increase font size to 16 for better readability
        button.setForeground(Color.BLACK); // Use black text for high contrast
        button.setBackground(new Color(0xFFD700)); // Gold background for strong visibility
        button.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2)); // Add a border for better definition
        button.setFocusable(false); // Disable focus border
        button.addActionListener(actionListener);
        panel.add(button);
    }

    private void generatePassword() {
        try {
            int length = Integer.parseInt(JOptionPane.showInputDialog("Enter password length (min 5):"));
            if (length > 4) {
                PasswordGenerator generator = new PasswordGenerator();
                String password = generator.generatePassword(length, true, true, true, true);
                showTextDialog("Generated Password", password);
            } else {
                JOptionPane.showMessageDialog(frame, "Length must be greater than 4!", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void encryptText() {
        String text = JOptionPane.showInputDialog("Enter text to encrypt:");
        String secretKey = JOptionPane.showInputDialog("Enter secret key:");
        if (text != null && secretKey != null) {
            try {
                String encryptedText = CryptoUtil.encrypt(secretKey, text);
                showTextDialog("Encrypted Text", encryptedText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Encryption failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void decryptText() {
        String text = JOptionPane.showInputDialog("Enter text to decrypt:");
        String secretKey = JOptionPane.showInputDialog("Enter secret key:");
        if (text != null && secretKey != null) {
            try {
                String decryptedText = CryptoUtil.decrypt(secretKey, text);
                showTextDialog("Decrypted Text", decryptedText);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Decryption failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void storePassword() {
        String accountName = JOptionPane.showInputDialog("Enter account name:");
        String password = JOptionPane.showInputDialog("Enter password:");
        if (accountName != null && password != null) {
            passwordStorage.add(accountName.toLowerCase(), password);
            JOptionPane.showMessageDialog(frame, "Password stored successfully!");
        }
    }

    private void searchPassword() {
        String accountName = JOptionPane.showInputDialog("Enter account name:");
        if (accountName != null) {
            Object password = passwordStorage.get(accountName.toLowerCase());
            if (password != null) {
                showTextDialog("Retrieved Password", "Account: " + accountName + "\nPassword: " + password);
            } else {
                JOptionPane.showMessageDialog(frame, "No password found for this account.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void deletePassword() {
        String accountName = JOptionPane.showInputDialog("Enter account name:");
        if (accountName != null) {
            if (passwordStorage.remove(accountName.toLowerCase()) != null) {
                JOptionPane.showMessageDialog(frame, "Password deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "No account found with this name.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void addNote() {
        JTextArea textArea = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(textArea);
        int result = JOptionPane.showConfirmDialog(frame, scrollPane, "Add Note", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String note = textArea.getText().trim();
            if (!note.isEmpty()) {
                notes.add(note);
                JOptionPane.showMessageDialog(frame, "Note added successfully!");
            } else {
                JOptionPane.showMessageDialog(frame, "Note cannot be empty!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void getNote() {
        if (notes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No notes found!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder allNotes = new StringBuilder();
            for (int i = 0; i < notes.size(); i++) {
                allNotes.append("Note ").append(i + 1).append(":\n").append(notes.get(i)).append("\n\n");
            }
            showTextDialog("All Notes", allNotes.toString());
        }
    }

    private void showTextDialog(String title, String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(frame, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        //Show splash screen
        CustomSplashScreen customSplashScreen = new CustomSplashScreen();
        customSplashScreen.waitForCompletion();

        // Launch main application
        SwingUtilities.invokeLater(PasswordManager::new);
    }
}
