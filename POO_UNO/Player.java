package POO_UNO;

import java.util.ArrayList;
import java.util.List;

public class Player {
	
	int numero_player;
	boolean bot;
	int nbrtotal = 108;
	List<Card> main = new ArrayList<>(nbrtotal);
	boolean turn = false;
	String name;
	// nahi num
	public Player(int numero_player ,boolean bot,String name) {
		this.numero_player = numero_player;
		this.bot = bot;
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

	// poser une carte
	public Card poserCarte(int numCarte) {
		Card carte = main.remove(numCarte-1);
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
