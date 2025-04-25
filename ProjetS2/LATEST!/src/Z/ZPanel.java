package Z;
import javax.swing.*;
import java.awt.*;

public class ZPanel extends JPanel {
    public ZPanel() {
        super();  // Appelle le constructeur par défaut de JPanel

    }
    public ZPanel(LayoutManager layout) {
        super(layout);  // Appelle le constructeur de JPanel avec un LayoutManager


    }

    public void personalizePanel(Color backgroundColor, int borderThickness, Color borderColor) {
        setBackground(backgroundColor);
        setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
    }

}
