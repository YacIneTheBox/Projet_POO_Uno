import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PilePanel {
    private ZPanel panel;
    private ZLabel topCardLabel;
    public static Image getHighQualityScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    public PilePanel() {
        panel = new ZPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(new Color(200, 200, 200));
        
        // Deck pile (left)
        ZPanel deckPanel = new ZPanel(new BorderLayout());
        deckPanel.setBackground(new Color(200, 200, 200));
        
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
        deckImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        deckPanel.add(deckImageLabel, BorderLayout.CENTER);
        
        
        // Discard pile (right)
        ZPanel discardPanel = new ZPanel(new BorderLayout());
        discardPanel.setBackground(new Color(200, 200, 200));
        
        topCardLabel = new ZLabel();
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
    
    public void updateTopCard(GameCard card) {
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