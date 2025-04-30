import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PilePanel {
    private ZPanel panel;
    private ZLabel topCardLabel;


    public PilePanel() {
        panel = new ZPanel(new GridLayout(1, 2, 10, 0));
        panel.personalizePanel(new Color(92, 89, 89),0,Color.black);

        // Deck pile (left)
        ZPanel deckPanel = new ZPanel(new BorderLayout());
        deckPanel.personalizePanel(new Color(92, 89, 89),0,Color.black);
        
        // Create card back image - FIXED PATH
        ImageIcon cardBackIcon = null;
        try {
            // Use direct file path instead of getResource()
            String path = "./cards/card_back.png";
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            cardBackIcon = new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("error loading card back image: " + e.getMessage());
            // Create a simple placeholder if image fails to load
            cardBackIcon = createPlaceholderBackImage();
        }

        ZLabel deckImageLabel = new ZLabel(cardBackIcon);
        deckImageLabel.personalizeLabel(new Color(92, 89, 89),new Font("Arial", Font.BOLD, 24));
        deckImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deckPanel.add(deckImageLabel, BorderLayout.CENTER);
        
        
        // Discard pile (right)
        ZPanel discardPanel = new ZPanel(new BorderLayout());
        discardPanel.personalizePanel(new Color(92, 89, 89),0,Color.black);
        
        topCardLabel = new ZLabel();
        topCardLabel.personalizeLabel(new Color(92, 89, 89),new Font("Arial", Font.BOLD, 24));
        topCardLabel.setHorizontalAlignment(SwingConstants.CENTER);
        discardPanel.add(topCardLabel, BorderLayout.CENTER);
        
        // Add both piles to main panel
        panel.add(deckPanel);
        panel.add(discardPanel);
    }
    
    private ImageIcon createPlaceholderBackImage() {
        BufferedImage img = new BufferedImage(100, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        
        // Draw blue background
        g2d.setColor(new Color(70, 70, 200));
        g2d.fillRect(0, 0, 100, 150);
        
        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 99, 149);
        
        // Draw UNO text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("UNO", 25, 75);
        
        g2d.dispose();
        return new ImageIcon(img);
    }
    
    public void updateTopCard(UNOCard card) {
        // Get the card image and resize it to match the deck card size (100x150)
        ImageIcon cardIcon = card.getCardImage();
        if (cardIcon != null) {
            Image img = cardIcon.getImage().getScaledInstance(100, 150, Image.SCALE_SMOOTH);
            topCardLabel.setIcon(new ImageIcon(img));
        } else {
            topCardLabel.setIcon(null);
        }
        panel.revalidate();
        panel.repaint();
    }
    
    public ZPanel getPanel() {
        return panel;
    }
}