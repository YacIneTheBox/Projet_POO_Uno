package Z;

import javax.swing.*;
import java.awt.*;

public class ZOptionPane extends JOptionPane {

    // Bloc statique : exécuté une seule fois quand la classe est chargée
    static {
        UIManager.put("OptionPane.background", Color.DARK_GRAY);
        UIManager.put("Panel.background", Color.DARK_GRAY);

        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.PLAIN, 14));

        UIManager.put("Button.background", new Color(70, 130, 180)); // Bleu doux
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 13));
        UIManager.put("Button.border", BorderFactory.createEmptyBorder(5, 15, 5, 15));
        UIManager.put("Button.focus", new Color(0, 0, 0, 0)); // Supprime le contour focus
    }

    public ZOptionPane() {
        super();
    }

    public ZOptionPane(Object message) {
        super(message);
    }

    public static void showGameMessage(String message, String title) {
        ZOptionPane.showMessageDialog(null, message, title, ZOptionPane.ERROR_MESSAGE);
    }

    public ZOptionPane(Object message, int messageType) {
        super(message, messageType);
    }
}
