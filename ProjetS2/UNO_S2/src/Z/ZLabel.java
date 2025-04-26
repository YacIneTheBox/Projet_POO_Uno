package Z;
import javax.swing.*;
import java.awt.*;

public class ZLabel extends JLabel {
    public ZLabel(String s, int center) {
        super();  // Appelle le constructeur par défaut de JLabel
    }
    public ZLabel(String text) {
        super(text);  // Appelle le constructeur de JLabel avec du texte
    }
    public ZLabel(Icon icon) {
        super(icon);  // Appelle le constructeur de JLabel avec une icône
    }
    public ZLabel() {
        super();
        setBorder(null);
        setOpaque(false);
        setBackground(Color.darkGray);
    }
    public void Zlabel(Color c) {
        setBackground(Color.RED);
    }


}
