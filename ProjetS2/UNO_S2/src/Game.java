import java.util.Scanner;

public class Game {
      Deck deck; // Deck of cards
      Player currentPlayer; // Current player
      CircularDoublyLinkedList players; // List of players
      Card currentCard; // Current card on the discard pile
      boolean isReversed; // Indicates if the turn order is reversed
      boolean gameOver; // Indicates if the game is over
      int numHumanPlayers;
      
      
      
      

      
      
      
    // Constructor to initialize the game
    public Game(int numberOfRealPlayers, int numberOfBots) {
    	numHumanPlayers=numberOfRealPlayers;
        Scanner scanner = new Scanner(System.in);
        deck = new Deck();
        //init deck
        // int gamepile
        players = new CircularDoublyLinkedList();
        deck.initdeckpile();
        deck.initgamepile();
        deck.shuffle();

        // Add real players
        for (int i = 1; i <= numberOfRealPlayers; i++) {            
            System.out.println("Enter player " + i + " name:");
            String name = scanner.nextLine();
            Player player = new Player(name);
            for (int j = 0; j < 7; j++) {
                Card card = deck.Drawcard();
                player.receiveCard(card);
            }
            players.add(player);
        }

        // Add bots
        for (int i = 1; i <= numberOfBots; i++) {
            Bot bot = new Bot("Bot" + i);            
            for (int j = 0; j < 7; j++) {
                Card card = deck.Drawcard();
                bot.receiveCard(card);
            }
            players.add(bot);
        }

        // Initialize game state
        //getfirstcard
        currentCard = deck.getfirstcard() ;
        isReversed = false;
        gameOver = false;
     // Set first human player as current
        players.setCurrentPlayer(players.getFirstPlayer()); 
        currentPlayer = players.getFirstPlayer();
    }
    public Player nextplayer() {
    	if (!isReversed) {
            return players.getnext();
        } else {
            return players.getprev();
        }

    }

    // Move to the next player based on the turn order
    // Move to the next player based on the turn order
    // Move to the next player based on the turn order

    public Player nextturn() {
        if (!isReversed) {
            return players.next();
        } else {
            return players.prev();
        }

    }
    // Start the game
    public void start() {
        Scanner scanner = new Scanner(System.in);
        Player winner = null;
        boolean validChoice;

        while (!gameOver) {
        	System.out.println("//////////////////////////////////////////////////////////////");
            System.out.print("Top card: " );
            currentCard.afficher() ;
            System.out.println(" ");
            System.out.println("Current player: " + currentPlayer.getname() + " / Remaining cards : " + currentPlayer.nbrCarteRestante());
            System.out.println(" ");
            
            if (currentPlayer.canplay(currentCard)) {
                currentPlayer.VoirCarte();
                if (currentPlayer.isUno()) {
                    currentPlayer.uno();
                     currentPlayer.won();
                    gameOver = true;
                    break;
                }

                validChoice = false;
                int carte = -1 ;
                Card chosenCard=null ;
                while (!validChoice) {
                    carte = currentPlayer.chooseCard(currentCard) ;
                    chosenCard=currentPlayer.getCardNum(carte) ;
                    
                    if (chosenCard.canPlay(currentCard)) {
                        validChoice = true;
                    } else {
                        System.out.println("Invalid card! Choose again.");
                    }
                }

                //currentPlayer.playCard(carte);
                currentCard = currentPlayer.poserCarte(carte) ;
                //ajoutergamepile
                deck.addtogamepile(currentCard);
                handleSpecialCard(chosenCard);

            } else {
                System.out.println("No valid cards to play. Drawing a card...");
                Card drawnCard = deck.Drawcard();
                currentPlayer.receiveCard(drawnCard);
            }

            currentPlayer = nextturn();
        }

        
    }

    // Handle special card effects
      void handleSpecialCard(Card card) {
        if (card instanceof SkipCard) {
            System.out.println("Skipping next player!");
            currentPlayer = nextturn(); // Passe au joueur suivant et le saute
        }else if (card instanceof ReversCard) {
                System.out.println("Reversing turn order!");
                isReversed = !isReversed;
        }else if (card instanceof Plus2Card) {
            System.out.println("Next player draws 2 cards!");
            drawCards(2); // Fait piocher 2 cartes au joueur suivant
        }else if (card instanceof FourColorCard) {
            currentPlayer.chooseColor(card); // Le joueur choisit une couleur
        }else if (card instanceof Plus4Card) {
            currentPlayer.chooseColor(card); // Le joueur choisit une couleur
            System.out.println("Next player draws 4 cards!");
            drawCards(4); // Fait piocher 4 cartes au joueur suivant
        }

    }
    // Draw a specified number of cards for the next player
      void drawCards(int count) {
        Player nextPlayer = nextplayer(); // Stocke le joueur suivant
        for (int i = 0; i < count; i++) {
            if (deck.deckisempty()) {
                deck.resetdeck();
                deck.shuffle();
            }
            Card card = deck.Drawcard();
            nextPlayer.receiveCard(card); // Distribue la carte au joueur suivant
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
