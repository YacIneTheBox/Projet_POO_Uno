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
    private SoundManager soundManager;

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
        soundManager = new SoundManager();
        soundManager.setMasterVolume(0.6f); // Volume global à 70%

        // Configuration des effets sonores avec volumes individuels
        soundManager.loadSoundEffect("bgMusic", 0.15f);
        soundManager.loadSoundEffect("playSound",  0.8f);
        soundManager.loadSoundEffect("wrongCard",  0.5f);
        soundManager.loadSoundEffect("marioLose",  0.5f);
        soundManager.loadSoundEffect("marioWin",  0.5f);
        soundManager.loadSoundEffect("Draw",  0.5f);


        soundManager.playSoundEffect("bgMusic");

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

        ZOptionPane.setCustomColors(new Color(96, 107, 101),
                new Color(94, 118, 104), Color.WHITE,
                new Color(201, 237, 221), Color.WHITE, Color.DARK_GRAY);

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

    //ici c le panel du centre qui contient le turn label en haut les deux dicard(droite) et deck(gauche) et les boutton
    private void setupCenterPanel() {
        ZPanel center = new ZPanel(new BorderLayout()); // affichage de Player
        center.personalizePanel(new Color(92, 89, 89),0,Color.BLACK);

        turnLabel = new ZLabel("", SwingConstants.CENTER);
        turnLabel.personalizeLabel(new Color(92, 89, 89),new Font("Arial", Font.BOLD, 24));
        turnLabel.setForeground(Color.white);


        // Ensure the label itself is centered
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);

        center.add(turnLabel, BorderLayout.NORTH);

        center.add(piles.getPanel(), BorderLayout.CENTER);
        center.add(createButtonPanel(), BorderLayout.SOUTH);
        center.setMinimumSize(new Dimension(170,170));

        window.setContent(center);
    }

    //f had la methode je vais pouvoire modifier le panel des deux boutton.
    private ZPanel createButtonPanel() {
        ZPanel buttonPanel = new ZPanel(new FlowLayout());
        buttonPanel.personalizePanel(new Color(92, 89, 89),0,Color.BLACK);
        drawButton = new ZButton("Draw Card",Color.red);
        drawButton.addActionListener(e -> handleDrawCard());

        unoButton = new ZButton("UNO!",Color.red);
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
            soundManager.playSoundEffect("playSound");
            int cardIndex = findCardIndex(currentPlayer, card.getOriginalCard());
            if (cardIndex != -1) {
                System.out.println("Playing card at index: " + cardIndex);
                Card playedCard = currentPlayer.poserCarte(cardIndex);
                currentCard = playedCard;
                deck.addtogamepile(currentCard);

                if (currentPlayer.nbrCarteRestante() == 1 && !unoCalledMap.get(currentPlayer)) {
                    drawPenaltyCards(currentPlayer, 2,false);
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
            soundManager.playSoundEffect("wrongCard");
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
        soundManager.playSoundEffect("Draw");
        if (gameOver || isChoosingColor || !isCurrentPlayerHuman()) return;

        Player currentPlayer = getCurrentPlayer();
        Card drawnCard = deck.Drawcard();
        if (deck.deckisempty()) {
            deck.resetdeck();
        }

        if (drawnCard != null) {
            currentPlayer.receiveCard(drawnCard);

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
                        handleSpecialCard(drawnCard);

                        // Use SwingUtilities.invokeLater to update UI components
                        SwingUtilities.invokeLater(() -> {
                            updateCurrentPlayerHand();
                            updateDiscardPile();
                            if (!isChoosingColor) {
                                advanceTurn();
                            }
                        });
                        return; // Exit after playing the card
                    }
                }
            }

            // If the card isn't played, just update the current player's hand
            SwingUtilities.invokeLater(() -> {
                updateCurrentPlayerHand();
                if (!isChoosingColor) {
                    advanceTurn();
                }
            });
        } else {
            showGameMessage("The deck is empty!", "Empty Deck");
            advanceTurn();
        }
    }

    /* ========== UNO MECHANICS ========== */
    private void handleUnoCall() {
        if (!isCurrentPlayerHuman()) return;

        Player currentPlayer = getCurrentPlayer();
        boolean isValidCall = currentPlayer.nbrCarteRestante() == 2;

        // Determine the message to show
        String message;
        String title;
        if (isValidCall) {
            soundManager.playSoundEffect("marioWin");
            unoCalledMap.put(currentPlayer, true);
            message = currentPlayer.getname() + " called UNO!";
            title = "UNO Call";
        } else {
            soundManager.playSoundEffect("marioLose");
            drawPenaltyCards(currentPlayer, 1,false);
            message = currentPlayer.getname() + " called UNO with more than 2 cards! +1 card";
            title = "UNO Penalty";
        }

        // Show the message and update the UI on the EDT
        SwingUtilities.invokeLater(() -> {
            showGameMessage(message, title);
            updateCurrentPlayerHand();
            // Update the turn display if needed
            updateTurnDisplay();
        });
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
        soundManager.playSoundEffect("playSound");
        // Check
        if (currentBot.nbrCarteRestante() == 2 && currentBot.canplay(currentCard)) {
            soundManager.playSoundEffect("marioWin");
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
        soundManager.playSoundEffect("Draw");
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
        drawPenaltyCards(nextPlayer, 4,true);
        showGameMessage(nextPlayer.getname() + " draws 4 cards!", "Plus 4");
        // Don't advance turn here - let advanceTurn() handle it
    }

    private void handlePlus2Effect() {
        Player nextPlayer = nextplayer();
        drawPenaltyCards(nextPlayer, 2,true);
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
            soundManager.playSoundEffect("Win");
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

    private void drawPenaltyCards(Player player, int count,boolean forspeacialcard) {
        for (int i = 0; i < count; i++) {
            Card drawn = deck.Drawcard();
            if(deck.deckisempty()){
                deck.resetdeck();
            }
            if (drawn != null) player.receiveCard(drawn);
        }
        if(forspeacialcard) updatePlayerHands();
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
