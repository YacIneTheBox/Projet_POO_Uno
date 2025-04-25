package Z;
import javax.swing.JButton;
import java.awt.*;

public class ZButton extends JButton {
	public ZButton (String text) {
	super(text) ;
	personalizeButton();
}

	public void personalizeButton() {
		setBackground(new Color(114, 88, 64));
		setForeground(new Color(225, 238, 188));
		setFont(new Font("Times New Roman", Font.ITALIC, 24));
		setContentAreaFilled(true); // Permet de personnaliser la couleur de fond
	}
}
