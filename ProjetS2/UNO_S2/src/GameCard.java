import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GameCard {
    private final Card originalCard;
    private final ImageIcon image;
    
    // Card colors for fallback
    private static final Color[] CARD_COLORS = {
        new Color(220, 50, 50),    // Red
        new Color(50, 50, 220),    // Blue
        new Color(50, 220, 50),    // Green
        new Color(220, 220, 50)    // Yellow
    };
    
    // Back of card image
    private static ImageIcon CARD_BACK = null;
    private static final String CARD_FOLDER = "./cards/";

    public GameCard(Card originalCard) {
        this.originalCard = originalCard;
        this.image = loadCardImage();
        
        // Load back image if not loaded yet
        if (CARD_BACK == null) {
            CARD_BACK = loadCardBack();
        }
    }

    private ImageIcon loadCardImage() {
        String imageName = getImageName();
        ImageIcon icon = loadImageFromResources(imageName);
        
        return icon != null ? icon : createFallbackIcon();
    }

    private static ImageIcon loadCardBack() {
        String path = CARD_FOLDER + "card_back.png";
        Image img = new ImageIcon(path).getImage();
        return new ImageIcon(img.getScaledInstance(80, 120, Image.SCALE_SMOOTH));
    }

    public static ImageIcon getCardBackImage() {
        return CARD_BACK;
    }

    private ImageIcon loadImageFromResources(String imageName) {
        try {
            String path = CARD_FOLDER + imageName + ".png";
            ImageIcon icon = new ImageIcon(path);
            // Check if the image is invalid (dimensions <= 0)
            if (icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
                throw new Exception("Invalid image dimensions");
            }
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(80, 120, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.out.println("Failed to load: " + imageName);
            return null;
        }
    }

    private String getImageName() {
        if (originalCard instanceof WildCard) {
            return (originalCard instanceof Plus4Card) ? "wild_drawfour" : "wild";
        }

        String color = originalCard.getColor().toLowerCase();
        
        if (originalCard instanceof Plus2Card) return color + "_drawtwo";
        if (originalCard instanceof SkipCard) return color + "_skip"; 
        if (originalCard instanceof ReversCard) return color + "_reverse";
        if (originalCard instanceof RegularCard) {
            return color + "_" + ((RegularCard)originalCard).getNumber();
        }
        
        return color + "_" + originalCard.getEffect().toLowerCase();
    }

    private ImageIcon createFallbackIcon() {
        BufferedImage img = new BufferedImage(80, 120, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Card background
        g.setColor(getFallbackColor());
        g.fillRoundRect(2, 2, 76, 116, 15, 15);
        
        // Border
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(2, 2, 76, 116, 15, 15);
        
        // Card text
        String text = getDisplayText();
        g.setColor(Color.WHITE);
        
        if (originalCard instanceof SkipCard || originalCard instanceof ReversCard) {
            g.setFont(new Font("Arial", Font.BOLD, 24));
            drawCenteredString(g, text, new Rectangle(80, 120));
            
            // Small description
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            String desc = originalCard instanceof SkipCard ? "SKIP" : "REVERSE";
            g.drawString(desc, 40 - g.getFontMetrics().stringWidth(desc)/2, 100);
        } 
        else if (originalCard instanceof Plus2Card || originalCard instanceof Plus4Card) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            drawCenteredString(g, text, new Rectangle(80, 120));
        }
        else {
            g.setFont(new Font("Arial", Font.BOLD, 20));
            drawCenteredString(g, text, new Rectangle(80, 120));
        }
        
        g.dispose();
        return new ImageIcon(img);
    }

    private Color getFallbackColor() {
        String color = originalCard.getColor().toLowerCase();
        switch(color) {
            case "red": return CARD_COLORS[0];
            case "blue": return CARD_COLORS[1];
            case "green": return CARD_COLORS[2];
            case "yellow": return CARD_COLORS[3];
            default: return Color.LIGHT_GRAY;
        }
    }

    private String getDisplayText() {
        if (originalCard instanceof RegularCard) {
            return String.valueOf(((RegularCard)originalCard).getNumber());
        }
        if (originalCard instanceof Plus2Card) return "+2";
        if (originalCard instanceof SkipCard) return "Ø";
        if (originalCard instanceof ReversCard) return "⇄";
        if (originalCard instanceof Plus4Card) return "+4";
        if (originalCard instanceof WildCard) return "W";
        return originalCard.getEffect();
    }

    private void drawCenteredString(Graphics g, String text, Rectangle rect) {
        FontMetrics fm = g.getFontMetrics();
        int x = rect.x + (rect.width - fm.stringWidth(text)) / 2;
        int y = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
        g.drawString(text, x, y);
    }

    public ImageIcon getCardImage() {
        return image;
    }

    public ZCardButton getCardButton() {
        ZCardButton btn = new ZCardButton(getCardImage());

        return btn;
    }


    public ZLabel getCardLabel() {
        ZLabel label = new ZLabel(getCardImage());
        label.setPreferredSize(new Dimension(80, 120));
        return label;
    }

    public String getColor() {
        return originalCard.getColor();
    }

    public String getValue() {
        if (originalCard instanceof RegularCard) {
            return String.valueOf(((RegularCard)originalCard).getNumber());
        }
        return originalCard.getEffect();
    }

    public Card getOriginalCard() {
        return originalCard;
    }

    public boolean canPlay(GameCard topCard) {
        return originalCard.canPlay(topCard.getOriginalCard());
    }

    @Override
    public String toString() {
        return originalCard.toString();
    }
}