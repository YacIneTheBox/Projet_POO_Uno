import Z.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerHandPanel {
    private ZPanel mainPanel;
    private ZPanel cardsPanel;

    private List<GameCard> cards = new ArrayList<>();
    private Consumer<GameCard> cardClickHandler;
    private boolean isActive = false;

    private static final int CARD_WIDTH = 90;
    private static final int CARD_HEIGHT = 140;
    private static final int CARD_OVERLAP = 54;

    public PlayerHandPanel() {
        setupMainPanel();
        setupCardsPanel();
    }

    private void setupMainPanel() {
        mainPanel = new ZPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.darkGray));
        mainPanel.setBackground(new Color(92, 89, 89));
        mainPanel.setBorder(null);
        //mainPanel.setPreferredSize(new Dimension(250,250));
    }

    private void setupCardsPanel() {
        cardsPanel = new ZPanel(null); // We'll manually place cards
        cardsPanel.setBackground(mainPanel.getBackground());
        cardsPanel.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(600, 151));

        mainPanel.add(scrollPane);
    }

    public void addCard(Card card) {
        GameCard gameCard = new GameCard(card);
        cards.add(gameCard);
        updateCardLayout();
    }

    private ZCardButton createCardButton(GameCard gameCard) {
        ZCardButton button = new ZCardButton(gameCard.getCardImage());

        button.setSize(CARD_WIDTH, CARD_HEIGHT);

        button.addMouseListener(new MouseAdapter() {
            Timer zoomTimer;
            @Override
            public void mouseEntered(MouseEvent e) {
                if (isActive) {
                    zoomTimer = new Timer(7, new ActionListener() {
                        int step = 0;
                        final int maxStep = 5;
                        @Override
                        public void actionPerformed(ActionEvent evt) {
                            if (step < maxStep) {
                                button.setSize(CARD_WIDTH + step * 2, CARD_HEIGHT + step * 2);
                                button.repaint();
                                step++;
                            } else {
                                zoomTimer.stop();
                            }
                        }
                    });
                    zoomTimer.start();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBorder(null);
                button.setSize(CARD_WIDTH, CARD_HEIGHT);
                button.repaint();
            }
        });

        button.addActionListener(e -> {
            if (isActive && cardClickHandler != null) {
                cardClickHandler.accept(gameCard);
            }
        });

        return button;
    }

    private void updateCardLayout() {
        cardsPanel.removeAll();
        cardsPanel.setBorder(null);

        int totalWidth = CARD_WIDTH + (cards.size() - 1) * CARD_OVERLAP;
        int startX = Math.max((600 - totalWidth) / 2, 0);

        for (int i = 0; i < cards.size(); i++) {
            GameCard gameCard = cards.get(i);
            ZCardButton button = createCardButton(gameCard);
            button.setLocation(startX + i * CARD_OVERLAP, 10);
            cardsPanel.add(button);
        }

        cardsPanel.setPreferredSize(new Dimension(600, CARD_HEIGHT + 18));
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    public void clear() {
        cards.clear();
        cardsPanel.removeAll();
        updateCardLayout();
    }

    public void setActive(boolean active) {
        isActive = active;

        // Pour chaque carte, on lui fait faire un zoomIn ou un zoomOut selon le tour
        for (Component comp : cardsPanel.getComponents()) {
            if (comp instanceof ZCardButton) {
                ZCardButton button = (ZCardButton) comp;
                if (active) {
                    button.zoomIn();
                } else {
                    button.zoomOut();
                }
            }
        }
    }


    public void setCardClickHandler(Consumer<GameCard> handler) {
        this.cardClickHandler = handler;
    }

    public ZPanel getPanel() {
        return mainPanel;
    }

    public List<GameCard> getCards() {
        return new ArrayList<>(cards);
    }
}
