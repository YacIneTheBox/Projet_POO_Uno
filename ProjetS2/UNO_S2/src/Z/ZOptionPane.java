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
        // Exemple : Définir une couleur de fond personnalisée
    }

}