import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class YCheckBox extends JCheckBox {

    private Color hoverColor = new Color(0, 0, 230);
    private Color normalColor = new Color(0, 250, 0);
    private Color borderColor = new Color(200, 0, 0);
    private int cornerRadius = 10;

    public YCheckBox(String text) {
        super(text);
        setFocusPainted(false);
        setBackground(normalColor);
        setForeground(Color.DARK_GRAY);
        setFont(new Font("Segoe UI", Font.PLAIN, 15)); // Police moderne
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false); // Important pour que la transparence fonctionne

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(normalColor);
                repaint();
            }
        });
    }

    // Pour arrondir l'arrière-plan
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessine le fond arrondi
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Dessine les bords
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        super.paintComponent(g2);
        g2.dispose();
    }

    // Facilite l'ajout d'une action
    public void onChange(Runnable action) {
        addActionListener(e -> action.run());
    }
}
