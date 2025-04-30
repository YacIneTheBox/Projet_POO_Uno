import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerHandPanel {
    // Components
    private ZPanel mainPanel;
    private ZPanel cardsPanel;

    // Card stuff
    private List<UNOCard> cards = new ArrayList<>();
    private Consumer<UNOCard> cardClickHandler;
    private boolean isActive = false;
    private boolean isHumanPlayer = false; // Indicates if the player is human
    private List<ZCardButton> cardButtons = new ArrayList<>();
    // Some size constants
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final int MAX_CARDS_PER_ROW = 7;
    private static final int CARD_SPACING = 6;



    public PlayerHandPanel() {
        setupMainPanel();
        setupCardsPanel();
    }




    private void setupMainPanel() {
        mainPanel = new ZPanel(new BorderLayout());
        mainPanel.personalizePanel(new Color(92, 89, 89),0,Color.black);
    }

    private void setupCardsPanel() {
        cardsPanel = new ZPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, CARD_SPACING, CARD_SPACING));
        cardsPanel.personalizePanel(new Color(92, 89, 89),0,Color.black);
        ZScrollPane scrollPane = new ZScrollPane(cardsPanel);
        mainPanel.add(scrollPane);
    }

    public void addCard(Card card) {
        UNOCard gameCard = new UNOCard(card);
        cards.add(gameCard);

        ZCardButton cardButton = createCardButton(gameCard);
        cardsPanel.add(cardButton);

        updateCardLayout();
    }

    private ZCardButton createCardButton(UNOCard gameCard) {
        ImageIcon displayIcon = isActive ? gameCard.getCardImage() : getCardBackImage();
        ZCardButton button = new ZCardButton(displayIcon);
        button.setZoomEnabled(isActive && isHumanPlayer);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isActive && isHumanPlayer ) {
                    button.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                    button.setIcon(gameCard.getCardImage());
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
                if (!isHumanPlayer) {
                    button.setIcon(getCardBackImage());
                }
            }
        });

        button.addActionListener(e -> {
            if (isActive && cardClickHandler != null && isHumanPlayer) {
                cardClickHandler.accept(gameCard);
            }
        });
        cardButtons.add(button); // Ajout à la liste
        return button;
    }

    private void updateCardLayout() {
        int cardCount = cards.size();
        int rows = (int) Math.ceil((double)cardCount / MAX_CARDS_PER_ROW);

        int width = MAX_CARDS_PER_ROW * (CARD_WIDTH + CARD_SPACING);
        int height = rows * (CARD_HEIGHT + CARD_SPACING);

        cardsPanel.setPreferredSize(new Dimension(width, height));
        cardsPanel.revalidate();
        mainPanel.revalidate();
    }

    public void clear() {
        cards.clear();
        cardsPanel.removeAll();
        updateCardLayout();
    }

    public void setActive(boolean active) {
        isActive = active;
        if (active) {
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        } else {
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));
        }

        // Met à jour l'affichage des cartes (face visible ou dos)
        for (Component comp : cardsPanel.getComponents()) {
            if (comp instanceof ZCardButton) {
                ZCardButton button = (ZCardButton) comp;
                button.setIcon(isActive ? button.getIcon() : getCardBackImage());
                // Désactive/active l'animation selon le tour et si c'est un joueur humain
                button.setZoomEnabled(isActive && isHumanPlayer);
            }
        }
    }


    public void setHumanPlayer(boolean isHuman) {
        this.isHumanPlayer = isHuman;
    }

    public void setCardClickHandler(Consumer<UNOCard> handler) {
        this.cardClickHandler = handler;
    }

    public ZPanel getPanel() {
        return mainPanel;
    }

    public List<UNOCard> getCards() {
        return new ArrayList<>(cards);
    }

    private ImageIcon getCardBackImage() {
        try {
            String path = "./cards/card_back.png";
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(CARD_WIDTH, CARD_HEIGHT, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (Exception e) {
            System.out.println("Error loading card back image: " + e.getMessage());
            // Create a simple placeholder if image fails to load
            return createPlaceholderBackImage();
        }
    }

    private ImageIcon createPlaceholderBackImage() {
        BufferedImage img = new BufferedImage(CARD_WIDTH, CARD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();

        // Draw blue background
        g2d.setColor(new Color(70, 70, 200));
        g2d.fillRect(0, 0, CARD_WIDTH, CARD_HEIGHT);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, CARD_WIDTH-1, CARD_HEIGHT-1);

        // Draw UNO text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("UNO", CARD_WIDTH/2-20, CARD_HEIGHT/2);

        g2d.dispose();
        return new ImageIcon(img);
    }
}