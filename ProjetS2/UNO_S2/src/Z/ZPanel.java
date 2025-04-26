package Z;
import javax.swing.*;
import java.awt.*;

public class ZPanel extends JPanel {
    public ZPanel() {
        super();  // Appelle le constructeur par défaut de JPanel
        setBackground(new Color(92, 89, 89));
        setOpaque(false);
        setBorder(null);
        setOpaque(false);
        setBackground(Color.darkGray);
    }

    public ZPanel(LayoutManager layout) {
        super(layout);  // Appelle le constructeur de JPanel avec un LayoutManager
        setBackground(new Color(92, 89, 89));
    }

}
