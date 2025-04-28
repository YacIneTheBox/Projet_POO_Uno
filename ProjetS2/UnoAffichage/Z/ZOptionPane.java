package Z;
import javax.swing.*;
import java.awt.*;

public class ZOptionPane extends JOptionPane {
    // Constructeur par défaut
    public ZOptionPane() {
        super();
        customizeLookAndFeel();
    }

    // Constructeur avec message
    public ZOptionPane(Object message) {
        super(message);
        customizeLookAndFeel();
    }

    // Constructeur avec message et type de message
    public ZOptionPane(Object message, int messageType) {
        super(message, messageType);
        customizeLookAndFeel();
    }


    // Méthode pour personnaliser l'apparence
    private void customizeLookAndFeel() {
        // Appliquer des couleurs personnalisées

    }

    // Méthode statique pour personnaliser les couleurs avant d'afficher une boîte de dialogue
    public static void setCustomColors(Color dialogBackground, Color panelBackground, Color messageForeground, Color buttonBackground, Color buttonForeground, Color border) {
        UIManager.put("OptionPane.background", dialogBackground);
        UIManager.put("Panel.background", panelBackground);
        UIManager.put("OptionPane.messageForeground", messageForeground);
        UIManager.put("OptionPane.buttonBackground", buttonBackground);
        UIManager.put("OptionPane.buttonForeground", buttonForeground);
        UIManager.put("OptionPane.border", BorderFactory.createLineBorder(border, 0));
    }

}