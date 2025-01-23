import java.util.Scanner;

public class Game {
    private Deck deck; // Deck of cards
    private Player currentPlayer; // Current player
    private CircularDoublyLinkedList players; // List of players
    private Card currentCard; // Current card on the discard pile
    private boolean isReversed; // Indicates if the turn order is reversed
    private boolean gameOver; // Indicates if the game is over

    // Constructor to initialize the game
    public Game(int numberOfRealPlayers, int numberOfBots) {
        Scanner scanner = new Scanner(System.in);
        deck = new Deck();
        players = new CircularDoublyLinkedList2();
        deck.initDeck();

        // Add real players
        for (int i = 1; i <= numberOfRealPlayers; i++) {
            Player player = new Player();
            System.out.println("Enter player " + i + " name:");
            String name = scanner.nextLine();
            player.setName(name);
            for (int j = 0; j < 7; j++) {
                Card card = deck.drawCard();
                player.addCard(card);
            }
            players.add(player);
        }

        // Add bots
        for (int i = 1; i <= numberOfBots; i++) {
            Bot bot = new Bot();
            bot.setName("Bot" + i);
            for (int j = 0; j < 7; j++) {
                Card card = deck.drawCard();
                bot.addCard(card);
            }
            players.add(bot);
        }

        // Initialize game state
        currentCard = deck.drawCard();
        isReversed = false;
        gameOver = false;
        currentPlayer = players.getFirstPlayer();
    }

    // Move to the next player based on the turn order
    public Player nextPlayer() {
        if (!isReversed) {
            currentPlayer = players.next();
        } else {
            currentPlayer = players.prev();
        }
        return currentPlayer;
    }

    // Start the game
    public void start() {
        Scanner scanner = new Scanner(System.in);
        Player winner = null;
        boolean validChoice;
        int choice;

        while (!gameOver) {
            System.out.println("Top card: " + currentCard);
            System.out.println("Current player: " + currentPlayer.getName());

            if (currentPlayer.canPlay(currentCard)) {
                currentPlayer.displayHand();
                if (currentPlayer.isUno()) {
                    winner = currentPlayer;
                    gameOver = true;
                    break;
                }

                validChoice = false;
                Card chosenCard = null;
                while (!validChoice) {
                    chosenCard = currentPlayer.chooseCard();
                    if (chosenCard.canPlay(currentCard)) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid card! Choose again.");
                    }
                }

                currentPlayer.playCard(chosenCard);
                currentCard = chosenCard;
                handleSpecialCard(chosenCard);

            } else {
                System.out.println("No valid cards to play. Drawing a card...");
                Card drawnCard = deck.drawCard();
                currentPlayer.addCard(drawnCard);
            }

            currentPlayer = nextPlayer();
        }

        System.out.println("Game over! Winner: " + winner.getName());
    }

    // Handle special card effects
    private void handleSpecialCard(Card card) {
        switch (card.getEffect()) {
            case "skip":
                System.out.println("Skipping next player!");
                currentPlayer = nextPlayer();
                break;
            case "reverse":
                System.out.println("Reversing turn order!");
                isReversed = !isReversed;
                break;
            case "plus2":
                System.out.println("Next player draws 2 cards!");
                drawCards(2);
                break;
            case "4color":
            case "plus4":
                chooseColor();
                if (card.getEffect().equals("plus4")) {
                    System.out.println("Next player draws 4 cards!");
                    drawCards(4);
                }
                break;
            default:
                break;
        }
    }

    // Draw a specified number of cards for the next player
    private void drawCards(int count) {
        for (int i = 0; i < count; i++) {
            if (deck.isEmpty()) {
                deck.reset();
                deck.shuffle();
            }
            Card card = deck.drawCard();
            currentPlayer.addCard(card);
        }
    }

    // Allow the player to choose a color for wild cards
    private void chooseColor() {
        System.out.println("Choose the color:");
        System.out.println("1: Red, 2: Blue, 3: Green, 4: Yellow");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                currentCard.setColor("red");
                break;
            case 2:
                currentCard.setColor("blue");
                break;
            case 3:
                currentCard.setColor("green");
                break;
            case 4:
                currentCard.setColor("yellow");
                break;
            default:
                System.out.println("Invalid choice! Defaulting to red.");
                currentCard.setColor("red");
                break;
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int numberOfPlayers = 0, numberOfBots = 0;
        boolean validChoice = false;

        while (!validChoice) {
            System.out.println("Enter the number of players (1-4):");
            numberOfPlayers = scanner.nextInt();
            if (numberOfPlayers > 0 && numberOfPlayers < 5) {
                validChoice = true;
            } else {
                System.out.println("Invalid choice! Try again.");
            }
        }

        numberOfBots = 4 - numberOfPlayers;
        Game game = new Game(numberOfPlayers, numberOfBots);
        game.start();
    }
}