import Z.*;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class UNOMain {
    // Game state variables
    private Deck deck;
    private Player currentPlayer;
    private CircularDoublyLinkedList players;
    private Card currentCard;
    private boolean isReversed;
    private boolean gameOver;
    private int numHumanPlayers;
    
    // GUI components
    private static GameWindow window;
    private static List<PlayerHandPanel> playerPanels = new ArrayList<>();
    private static PilePanel piles;
    private static ZLabel turnLabel;
    private static ZButton drawButton;
    private static ZButton unoButton;
    private static Timer gameTimer;
    
    private static boolean isChoosingColor = false;
    private static Map<Player, Boolean> unoCalledMap = new HashMap<>();
    private static final int MAX_PLAYERS = 4;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UNOMain unoGame = new UNOMain();
            unoGame.initializeGame();
        });
    }

    public UNOMain() {
        numHumanPlayers = getNumberOfHumanPlayers();
        int numBots = MAX_PLAYERS - numHumanPlayers;

        deck = new Deck();
        players = new CircularDoublyLinkedList();
        deck.initdeckpile();
        deck.initgamepile();
        deck.shuffle();

        // Add real players
        for (int i = 1; i <= numHumanPlayers; i++) {            
            String name = ZOptionPane.showInputDialog("Enter player " + i + " name:");
            if (name == null || name.trim().isEmpty()) {
                name = "Player " + i;
            }
            Player player = new Player(name);
            for (int j = 0; j < 7; j++) {
                Card card = deck.Drawcard();
                if(deck.deckisempty()){
                	deck.resetdeck();
                	}
                player.receiveCard(card);
            }
            players.add(player);
        }

        // Add bots
        for (int i = 1; i <= numBots; i++) {
            Bot bot = new Bot("Bot" + i);            
            for (int j = 0; j < 7; j++) {
                Card card = deck.Drawcard();
                if(deck.deckisempty()){
                	deck.resetdeck();
                	}
                bot.receiveCard(card);
            }
            players.add(bot);
        }

        // Initialize game state
        currentCard = deck.getfirstcard();
        isReversed = false;
        gameOver = false;
        players.setCurrentPlayer(players.getFirstPlayer()); 
        currentPlayer = players.getFirstPlayer();
        
        // Initialize UNO status
        unoCalledMap.clear();
        Player current = players.getFirstPlayer();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            unoCalledMap.put(current, false);
            current = players.next();
        }
    }

    private int getNumberOfHumanPlayers() {
        Integer[] options = {1, 2, 3, 4};
        Integer selection = (Integer) ZOptionPane.showInputDialog(
                null,
                "How many human players?",
                "UNO Setup",
                ZOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        return (selection != null) ? selection : 1;
    }

    private Player nextplayer() {
        if (!isReversed) {
            return players.getNextPlayer(currentPlayer);
        } else {
            return players.getPreviousPlayer(currentPlayer);
        }
    }

    private void initializeGame() {
        window = new GameWindow("UNO Game");
        piles = new PilePanel();
        setupPlayerPanels();
        setupCenterPanel();
        window.setSize(1000, 700);
        window.setVisible(true);
        updateGameState();
        
        // Add this line to set up click handlers
        setupCardClickHandlers();
        
        if (!isCurrentPlayerHuman()) {
            scheduleAITurn(800);
        }
    }

    private void setupPlayerPanels() {
        String[] positions = {"bottom", "right", "top", "left"};
        for (int i = 0; i < MAX_PLAYERS; i++) {
            PlayerHandPanel panel = new PlayerHandPanel();
            panel.setHumanPlayer(i < numHumanPlayers); // Add this line
            playerPanels.add(panel);
            window.addPlayerPanel(panel.getPanel(), positions[i]);
        }
    }

    private void setupCenterPanel() {
        ZPanel center = new ZPanel(new BorderLayout()); // affichage de Player
        center.personalizePanel(new Color(103, 174, 110),2,Color.BLACK);

        turnLabel = new ZLabel("", SwingConstants.CENTER);
        turnLabel.personalizeLabel(new Color(68, 54, 39),new Font("Times New Roman", Font.ITALIC, 24));


        // Ensure the label itself is centered
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        center.add(turnLabel, BorderLayout.NORTH);

        center.add(piles.getPanel(), BorderLayout.CENTER);
        center.add(createButtonPanel(), BorderLayout.SOUTH);

        window.setContent(center);
    }

    private ZPanel createButtonPanel() {
        ZPanel buttonPanel = new ZPanel(new FlowLayout());
        buttonPanel.personalizePanel(new Color(103, 174, 110),1,Color.BLACK);
        drawButton = new ZButton("Draw Card");
        drawButton.addActionListener(e -> handleDrawCard());
        
        unoButton = new ZButton("UNO!");
        unoButton.addActionListener(e -> handleUnoCall());
        
        buttonPanel.add(drawButton);
        buttonPanel.add(unoButton);
        
        return buttonPanel;
    }

    private void setupCardClickHandlers() {
        for (int i = 0; i < numHumanPlayers; i++) {
            final int playerIndex = i;
            playerPanels.get(i).setCardClickHandler(card -> {
                if (getCurrentPlayerIndex() == playerIndex && !gameOver && !isChoosingColor) {
                    // Add debug output to verify clicks are being received
                    System.out.println("Card clicked: " + card.getOriginalCard());
                    handleHumanPlay(card);
                } else {
                    System.out.println("Click ignored - Not your turn or game over");
                }
            });
        }
    }
    /* ========== CARD PLAY HANDLING ========== */
    private void handleHumanPlay(GameCard card) {
        Player currentPlayer = getCurrentPlayer();
        System.out.println("Attempting to play: " + card.getOriginalCard() + " on " + currentCard);
        if (card.getOriginalCard().canPlay(currentCard)) {
            int cardIndex = findCardIndex(currentPlayer, card.getOriginalCard());
            if (cardIndex != -1) {
                System.out.println("Playing card at index: " + cardIndex);
                Card playedCard = currentPlayer.poserCarte(cardIndex);
                currentCard = playedCard;
                deck.addtogamepile(currentCard);
                
                if (currentPlayer.nbrCarteRestante() == 1 && !unoCalledMap.get(currentPlayer)) {
                    drawPenaltyCards(currentPlayer, 2);
                    showGameMessage("You didn't call UNO! +2 cards", "PENALTY");
                }
                
                // Use SwingWorker to handle updates in the background
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Update the discard pile
                        updateDiscardPile();
                        
                        // Update only the current player's hand
                        updateCurrentPlayerHand();
                        
                        // Handle special card effects
                        handleSpecialCard(playedCard);
                        
                        return null;
                    }

                    @Override
                    protected void done() {
                        // Check win condition and advance turn
                        if (!checkWinCondition() && !isChoosingColor) {
                            advanceTurn();
                        }
                    }
                };
                worker.execute();
            } else {
                System.out.println("Card not found in player's hand");
                showGameMessage("Card not found in your hand!", "Error");
            }
        } else {
            System.out.println("Invalid play - card cannot be played on current card");
            showGameMessage("Invalid card selection!", "Error");
        }
    }
 
    private void updateCurrentPlayerHand() {
        Player currentPlayer = getCurrentPlayer();
        PlayerHandPanel currentPanel = playerPanels.get(getCurrentPlayerIndex());
        
        // Clear and re-add cards for the current player
        currentPanel.clear();
        for (int j = 0; j < currentPlayer.nbrCarteRestante(); j++) {
            currentPanel.addCard(currentPlayer.getCardNum(j));
        }
        
        // Update the active status for the current player
        currentPanel.setActive(true);
    }
    private void handleDrawCard() {
        if (gameOver || isChoosingColor || !isCurrentPlayerHuman()) return;
        
        Player currentPlayer = getCurrentPlayer();
        Card drawnCard = deck.Drawcard();
        if(deck.deckisempty()){
        	deck.resetdeck();
        	}

        if (drawnCard != null) {
            currentPlayer.receiveCard(drawnCard);
            updatePlayerHands();
            
            if (drawnCard.canPlay(currentCard)) {
                int choice = ZOptionPane.showConfirmDialog(
                    window.getContentPane(),
                    "Play the drawn card?",
                    "Play Card",
                        ZOptionPane.YES_NO_OPTION);
                    
                if (choice == ZOptionPane.YES_OPTION) {
                    int cardIndex = findCardIndex(currentPlayer, drawnCard);
                    if (cardIndex != -1) {
                        currentCard = currentPlayer.poserCarte(cardIndex);
                        deck.addtogamepile(currentCard);
                        updateDiscardPile();
                        handleSpecialCard(drawnCard);
                        
                        // Update the player's hand again after playing the card
                        updatePlayerHands();
                        
                        if (!isChoosingColor) {
                            advanceTurn();
                        }
                        return; // Exit after playing the card
                    }
                }
            }
            
            if (!isChoosingColor) {
                advanceTurn();
            }
        } else {
            showGameMessage("The deck is empty!", "Empty Deck");
        }
    }

    /* ========== UNO MECHANICS ========== */
    private void handleUnoCall() {
        Player currentPlayer = getCurrentPlayer();
        if(!isCurrentPlayerHuman()) return ;
        if (currentPlayer.nbrCarteRestante() == 2) {
            unoCalledMap.put(currentPlayer, true);
            showGameMessage(currentPlayer.getname() + " called UNO!", "UNO Call");
        } else {
        	drawPenaltyCards(currentPlayer, 1);
            updatePlayerHands();
        	updateGameState(); 
            //checkForUnoPenalties();
        }
    }

    private void checkForUnoPenalties() {
        boolean foundPlayer = false;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            Player player = getPlayerAt(i);
            // Changement ici : vérifier 2 cartes au lieu de 1
            if (player.nbrCarteRestante() == 2 && !unoCalledMap.get(player)) {
                drawPenaltyCards(player, 2);
                showGameMessage(player.getname() + " caught without calling UNO! +2 cards", "UNO Penalty");
                foundPlayer = true;
                break;
            }
        }
        
        if (!foundPlayer) {
            drawPenaltyCards(getCurrentPlayer(), 1);
        }
        updatePlayerHands();
    }
    /* ========== AI TURN HANDLING ========== */
    private void scheduleAITurn(int delay) {
        if (gameTimer != null) gameTimer.stop();
        
        gameTimer = new Timer(delay, e -> {
            if (!gameOver && !isChoosingColor && !isCurrentPlayerHuman()) {
                aiPlay();
            }
        });
        gameTimer.setRepeats(false);
        gameTimer.start();
    }

    private void aiPlay() {
        Bot currentBot = (Bot) getCurrentPlayer();

        // Check 
        if (currentBot.nbrCarteRestante() == 2 && currentBot.canplay(currentCard)) {
            unoCalledMap.put(currentBot, true);
            showGameMessage(currentBot.getname() + " calls UNO!", "UNO Call");
        }

        int cardIndex = currentBot.chooseCard(currentCard);
        if (cardIndex != -1) {
            Card playedCard = currentBot.getCardNum(cardIndex);
            currentCard = currentBot.poserCarte(cardIndex);
            deck.addtogamepile(currentCard);

            // no need to ckeck since its a bot 
            /*if (currentBot.nbrCarteRestante() == 1 && !unoCalledMap.get(currentBot)) {
                drawPenaltyCards(currentBot, 2);
                showGameMessage(currentBot.getname() + " didn't call UNO! +2 cards", "PENALTY");
            }*/

            showGameMessage(currentBot.getname() + " plays " + playedCard, "AI Turn");
            updateDiscardPile();
            updatePlayerHands();
            handleSpecialCard(playedCard);
        } else {
            handleAIDraw();
        }

        if (!checkWinCondition() && !isChoosingColor) {
            advanceTurn();
        }
    }

    private void handleAIDraw() {
        Bot bot = (Bot) getCurrentPlayer();
        Card drawnCard = deck.Drawcard();
        if(deck.deckisempty()){
        	deck.resetdeck();
        	}
        if (drawnCard != null) {
            bot.receiveCard(drawnCard);
            showGameMessage(bot.getname() + " draws a card", "AI Turn");
            
            if (drawnCard.canPlay(currentCard) ) {
                int newCardIndex = findCardIndex(bot, drawnCard);
                if (newCardIndex != -1) {
                    currentCard = bot.poserCarte(newCardIndex);
                    deck.addtogamepile(currentCard);
                    showGameMessage(bot.getname() + " plays drawn card", "AI Turn");
                    updateTopCard();
                    handleSpecialCard(drawnCard);
                }
            }
        }
        updatePlayerHands();
    }

    /* ========== SPECIAL CARD EFFECTS ========== */
    private void handleSpecialCard(Card card) {
        if (card instanceof WildCard) {
            chooseColor();
            if (card instanceof Plus4Card) {
                handlePlus4Effect();
            }
        } else if (card instanceof SkipCard) {
            handleSkipEffect();
        } else if (card instanceof ReversCard) {
            handleReverseEffect();
        } else if (card instanceof Plus2Card) {
            handlePlus2Effect();
        }
    }


    private void handleSkipEffect() {
        Player playerToSkip = nextplayer();
        showGameMessage(playerToSkip.getname() + "'s turn is skipped!", "Skip Card");
        
        // Move to the player after the skipped one
        if (!isReversed) {
            currentPlayer = players.getNextPlayer(currentPlayer);
        } else {
            currentPlayer = players.getPreviousPlayer(currentPlayer);
        }
        
        players.setCurrentPlayer(currentPlayer);
        updateTurnDisplay();
        
        if (!gameOver && !isCurrentPlayerHuman()) {
            scheduleAITurn(800);
        }
    }
    private void handleReverseEffect() {
        isReversed = !isReversed;
        showGameMessage("Direction reversed!", "Reverse Card");
        updateTurnDisplay();
    }

    private void handlePlus4Effect() {
        Player nextPlayer = nextplayer();
        drawPenaltyCards(nextPlayer, 4);
        showGameMessage(nextPlayer.getname() + " draws 4 cards!", "Plus 4");
        // Don't advance turn here - let advanceTurn() handle it
    }

    private void handlePlus2Effect() {
        Player nextPlayer = nextplayer();
        drawPenaltyCards(nextPlayer, 2);
        showGameMessage(nextPlayer.getname() + " draws 2 cards!", "Plus 2");
        // Don't advance turn here - let advanceTurn() handle it
    }
    /* ========== GAME STATE MANAGEMENT ========== */
    private void advanceTurn() {
        Player current = getCurrentPlayer();
        unoCalledMap.put(current, false);
        
        Player nextPlayer;
        if (!isReversed) {
            nextPlayer = players.getNextPlayer(current);
        } else {
            nextPlayer = players.getPreviousPlayer(current);
        }
        
        currentPlayer = nextPlayer;
        players.setCurrentPlayer(currentPlayer);
        updateTurnDisplay();
        
        if (!gameOver && !isCurrentPlayerHuman()) {
            scheduleAITurn(800);
        }
    }
    private boolean checkWinCondition() {
        if (getCurrentPlayer().nbrCarteRestante() == 0) {
            gameOver = true;
            showGameMessage(getCurrentPlayer().getname() + " wins!", "Game Over");
            
            int restart = ZOptionPane.showConfirmDialog(
                window.getContentPane(),
                "Play again?",
                "Game Over",
                    ZOptionPane.YES_NO_OPTION);
                
            if (restart == ZOptionPane.YES_OPTION) {
                restartGame();
            }else {
            	window.close();
            }
            
            
            return true;
        }
        return false;
    }

    private void restartGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }

        // Clear static fields
        playerPanels.clear();
        window.close();  // already done
        window = null;
        piles = null;
        turnLabel = null;
        drawButton = null;
        unoButton = null;

        SwingUtilities.invokeLater(() -> {
            UNOMain newGame = new UNOMain();
            newGame.initializeGame();
        });
    }


    /* ========== UTILITY METHODS ========== */
    private int findCardIndex(Player player, Card targetCard) {
        for (int i = 0; i < player.nbrCarteRestante(); i++) {
            if (player.getCardNum(i).equals(targetCard)) {
                return i;
            }
        }
        return -1;
    }

    private void drawPenaltyCards(Player player, int count) {
        for (int i = 0; i < count; i++) {
            Card drawn = deck.Drawcard();
            if(deck.deckisempty()){
            	deck.resetdeck();
            	}
            if (drawn != null) player.receiveCard(drawn);
        }
        updatePlayerHands();
    }

    private void chooseColor() {
        if (isChoosingColor) return;
        isChoosingColor = true;
        
        try {
            if (isCurrentPlayerHuman()) {
                String[] options = {"Red", "Blue", "Green", "Yellow"};
                String choice = (String) ZOptionPane.showInputDialog(
                    window.getContentPane(),
                    "Choose color:",
                    "Wild Card",
                        ZOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
                    
                currentCard.setColor(choice != null ? choice.toLowerCase() : "red");
            } else {
                Bot bot = (Bot) getCurrentPlayer();
                String bestColor = determineMostCommonColor(bot);
                currentCard.setColor(bestColor);
                showGameMessage(bot.getname() + " chooses " + bestColor, "Wild Card");
            }
            updateTopCard();
        } finally {
            isChoosingColor = false;
        }
    }

    private String determineMostCommonColor(Bot bot) {
        Map<String, Integer> colorCounts = new HashMap<>();
        for (int i = 0; i < bot.nbrCarteRestante(); i++) {
            String color = bot.getCardNum(i).getColor();
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
        }
        return colorCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("red");
    }

    /* ========== UI UPDATE METHODS ========== */
    private void updateTopCard() {
        piles.updateTopCard(new GameCard(currentCard));
    }

    private void updateDiscardPile() {
        GameCard visualCard = new GameCard(deck.getfirstcard());
        piles.updateTopCard(visualCard);
    }

    private void updatePlayerHands() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            Player player = getPlayerAt(i);
            PlayerHandPanel panel = playerPanels.get(i);
            
            panel.clear();
            for (int j = 0; j < player.nbrCarteRestante(); j++) {
                panel.addCard(player.getCardNum(j));
            }
            
            panel.setActive(player.nbrCarteRestante() == 1 && unoCalledMap.get(player));
        }
    }

    private void updateTurnDisplay() {
        turnLabel.setText(getCurrentPlayer().getname() + "'s turn");
        drawButton.setEnabled(isCurrentPlayerHuman() && !gameOver && !isChoosingColor);
        unoButton.setEnabled(isCurrentPlayerHuman() && !gameOver && !isChoosingColor);
        for (int i = 0; i < MAX_PLAYERS; i++) {
            playerPanels.get(i).setActive(i == getCurrentPlayerIndex());
        }
    }

    /* ========== PLAYER MANAGEMENT ========== */
    private int getCurrentPlayerIndex() {
        Player current = players.getFirstPlayer();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (current == currentPlayer) return i;
            current = players.next();
        }
        return 0;
    }

    private Player getCurrentPlayer() {
        return currentPlayer;
    }

    private Player getPlayerAt(int index) {
        Player current = players.getFirstPlayer();
        for (int i = 0; i < index; i++) {
            current = players.next();
        }
        return current;
    }

    private void updateGameState() {
        updateTopCard();
        updatePlayerHands();
        updateTurnDisplay();
    }

    private boolean isCurrentPlayerHuman() {
        return getCurrentPlayerIndex() < numHumanPlayers;
    }

    private void showGameMessage(String message, String title) {
        ZOptionPane.showMessageDialog(window.getContentPane(), message, title, ZOptionPane.INFORMATION_MESSAGE);
    }
}
