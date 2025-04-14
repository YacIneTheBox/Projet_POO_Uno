import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Ybutton extends JButton {


    public Ybutton(String texte, Color c2,Color c1) {
        super(texte);
        setContentAreaFilled(false); // Empêche le remplissage par défaut
        setFocusPainted(false);     // Enlève le focus bleu
        setBorderPainted(false);   // Supprime les bordures
        setForeground(c1); // Texte en blanc
        setFont(new Font("Montserrat", Font.BOLD, 14));
        setBackground(Color.RED);

    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // On utilise la couleur du fond définie par setBackground()
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        super.paintComponent(g); // Dessine le texte
        g2.dispose();
    }
}
