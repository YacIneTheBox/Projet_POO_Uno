import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerHandPanel {
    // Components
    private ZPanel mainPanel;
    private ZPanel cardsPanel;
    
    // Card stuff
    private List<GameCard> cards = new ArrayList<>();
    private Consumer<GameCard> cardClickHandler;
    private boolean isActive = false;
    
    // Some size constants I figured would work
    private static final int CARD_WIDTH = 80;
    private static final int CARD_HEIGHT = 120;
    private static final int MAX_CARDS_PER_ROW = 7;
    private static final int CARD_SPACING = 5;

    public PlayerHandPanel() {
        setupMainPanel();
        setupCardsPanel();
    }
    
    private void setupMainPanel() {
        mainPanel = new ZPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
    
    private void setupCardsPanel() {
        cardsPanel = new ZPanel();
        cardsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, CARD_SPACING, CARD_SPACING));
        
        ZScrollPane scrollPane = new ZScrollPane(cardsPanel);

        
        mainPanel.add(scrollPane);
    }

    public void addCard(Card card) {
        GameCard gameCard = new GameCard(card);
        cards.add(gameCard);
        
        ZCardButton cardButton = createCardButton(gameCard);
        cardsPanel.add(cardButton);
        
        updateCardLayout();
    }
    
    private ZCardButton createCardButton(GameCard gameCard) {
        ZCardButton button = new ZCardButton(gameCard.getCardImage());
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isActive) {
                    button.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
            }
        });
        
        // Click action
        button.addActionListener(e -> {
            if (isActive && cardClickHandler != null) {
                cardClickHandler.accept(gameCard);
            }
        });
        
        return button;
    }
    
    private void updateCardLayout() {
        int cardCount = cards.size();
        int rows = (int) Math.ceil((double)cardCount / MAX_CARDS_PER_ROW);
        
        int width = MAX_CARDS_PER_ROW * (CARD_WIDTH + CARD_SPACING);
        int height = rows * (CARD_HEIGHT + CARD_SPACING);
        
        cardsPanel.setPreferredSize(new Dimension(width, height));
        
        // Refresh the layout
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
            mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        }
    }
    
    public void setCardClickHandler(Consumer<GameCard> handler) {
        this.cardClickHandler = handler;
    }
    
    public ZPanel getPanel() {
        return mainPanel;
    }
    
    public List<GameCard> getCards() {
        return new ArrayList<>(cards); // Return copy for safety
    }
}