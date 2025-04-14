import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class YComboBox<T> extends JComboBox<T> {

    private Color normalColor = new Color(250, 0, 250);
    private Color hoverColor = new Color(0, 230, 230);
    private Color borderColor = new Color(200, 200, 0);
    private int cornerRadius = 10;

    public YComboBox(T[] items) {
        super(items);
        setUI(new RoundedComboBoxUI());
        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setForeground(Color.DARK_GRAY);
        setBackground(normalColor);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(false);

        // Hover effect
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

    public void setThemeColors(Color normal, Color hover, Color border) {
        this.normalColor = normal;
        this.hoverColor = hover;
        this.borderColor = border;
        setBackground(normalColor);
        repaint();
    }

    private class RoundedComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton button = new JButton("▾");
            button.setBorder(null);
            button.setFont(new Font("Segoe UI", Font.BOLD, 12));
            button.setBackground(normalColor);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return button;
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), cornerRadius, cornerRadius);

            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, cornerRadius, cornerRadius);
        }
    }
}
