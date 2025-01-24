package POO_UNO;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Player {
	Scanner scanner = new Scanner(System.in);
	
	int numero_player;
	boolean bot;
	int nbrtotal = 108;
	List<Card> main = new ArrayList<>(nbrtotal);
	boolean turn = false;
	String name;
	// nahi num
	public Player(int numero_player,String name) {
		this.numero_player = numero_player;
		this.name = name;
	}
	
	// fonction pour definir si c le tour du jouer ou pas
	
	public void setTurn() {
		turn = true;
	}												
	public void endTurn() {
		turn = false;
	}
	
	// varif si c un bot
	
	public boolean IsBot() {
		if (this.bot = true) {
			return true;
		}
		return false;
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
	
	// voir ses cartes 
	public void VoirCarte() {
		int i=1;
		System.out.println("you have : ");
		for (Card carte : main) {
			System.out.println(i); // numero de la carte dans la main
			carte.afficher();
			i++;
		}
	}
	
	// nombre de carte restant
	public int nbrCarteRestante() {
		return main.size();
	}

	// choix d'une carte
	public int chooseCard(Card Last_carte) { // return index de la carte qu'on veut jouer
		int nbrcarte = main.size();
		int numCarte = -1;
		do {
			System.out.println("Enter a Card : ");
			numCarte = scanner.nextInt();}
		while (numCarte < 0 && numCarte > nbrcarte);
		
		return numCarte;
	}
	
	public Card poserCarte(int numCarte) {
		Card carte = main.remove(numCarte);
		return carte;
	}
	
	public boolean isUno() {
		if (main.size() == 1) {
			return true;
		}
		return false;
	}
	
	public void uno() {
		System.out.println("The player : "+ this.name + "Is UNO");
	}
	
	public void won() {
		System.out.println("The player : "+ this.name + "won the game");
	}
	
	// verif si la main est vide (victoire)
	public boolean IsEmpty() {
		if (main.size()==0) {
			return true;
		}
		return false;
	}	
}
