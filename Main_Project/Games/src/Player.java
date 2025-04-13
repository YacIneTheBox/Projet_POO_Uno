import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Player {
	Scanner scanner = new Scanner(System.in);
	
	int nbrtotal = 108;
	List<Card> main = new ArrayList<>(nbrtotal);

	String name;
	// nahi num
	public Player(String name) {
		this.name = name;
	}
	
	// ajoute une carte a la main
	
	public void receiveCard(Card receivedCard) {
		main.add(receivedCard);
	}
	
	
	// verifer automatiquement si il peut poser une carte
	
	public Boolean canplay(Card Last_carte) {
		for (Card carte : main) {
			if (carte.canPlay(Last_carte) == true ) {
				return true;
			}
		}
		return false;
	}
	
	public Card getCardNum(int numCard) {
		return main.get(numCard);
	}
	
	// voir ses cartes 
    public void VoirCarte() {
        int i=1;
        System.out.println("you have : ");
        for (Card carte : main) {
            System.out.print(i + " : "); // numero de la carte dans la main
            carte.afficher();
            System.out.println("");
            i++;
        }
    }
	
	// nombre de carte restant
	public int nbrCarteRestante() {
		return main.size();
	}

	// choix d'une carte
	public int chooseCard(Card Last_carte) {
	    int nbrcarte = main.size();
	    int numCarte = -1;
	    do {
	        System.out.println("Enter a Card (1-" + nbrcarte + "):");
	        numCarte = scanner.nextInt() -1;
	    } while (numCarte < 0 || numCarte >= nbrcarte);
	    return numCarte;
	}
	
	public Card poserCarte(int numCarte) {
		Card carte = main.remove(numCarte);
		System.out.print("Je Pose la carte : ");
		carte.afficher();
		return carte;
	}
	
    // Allow the player to choose a color for wild cards
    public Card chooseColor(Card currentCard) {
        Scanner scanner = new Scanner(System.in); 
        int choice;
        
        do {
            System.out.println("Choose the color:");
            System.out.println("1: Red, 2: Blue, 3: Green, 4: Yellow");
            choice = scanner.nextInt();
        }while(choice < 1 || choice > 4);
        
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
            return currentCard;
        }
    
	
	public boolean isUno() {
		if (main.size() == 1) {
			return true;
		}
		return false;
	}
	
	public void uno() {
		System.out.println("The player : " + this.name + " Is UNO");
	}
	
	public void won() {
		System.out.println("The player : " + this.name + " won the game");
	}
	public String getname() {
        return this.name;
    }
		
}