package projetUNO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	List<Card> deckpile = new ArrayList<>();
	List<Card> gamepile = new Arraylist<>();
	
	//cree et maitretre la premiere carte dans la liste de la game
	public void initgamepile() {
		Card card = deckpile.remove(0);
		gamepile.add(card);
	}
	
	//creation du deck et remplissage avec 108 acrte
	public void initdeckpile() {
	    deckpile.clear(); // Assurez-vous que le deck est vide avant de commencer

	    String[] colors = {"Red", "Blue", "Green", "Yellow"};

	    // Ajouter les cartes régulières (0 à 9, deux fois pour chaque nombre sauf 0)
	    for (String color : colors) {
	        RegularCard zeroCard = new RegularCard();
	        zeroCard.setColor(color);
	        zeroCard.setNumber(0);
	        deckpile.add(zeroCard); // Une seule carte 0 par couleur

	        for (int i = 1; i <= 9; i++) {
	            // Première copie
	            RegularCard card1 = new RegularCard();
	            card1.setColor(color);
	            card1.setNumber(i);
	            deckpile.add(card1);

	            // Deuxième copie
	            RegularCard card2 = new RegularCard();
	            card2.setColor(color);
	            card2.setNumber(i);
	            deckpile.add(card2);
	        }
	    }

	    // Ajouter les cartes d'action (Skip, Reverse, +2)
	    for (String color : colors) {
	        for (int i = 0; i < 2; i++) { // Deux copies pour chaque action par couleur
	            SkipCard skipCard = new SkipCard();
	            skipCard.setColor(color);
	            deckpile.add(skipCard);

	            ReversCard reverseCard = new ReversCard();
	            reverseCard.setColor(color);
	            deckpile.add(reverseCard);

	            Plus2Card plus2Card = new Plus2Card();
	            plus2Card.setColor(color);
	            deckpile.add(plus2Card);
	        }
	    }

	    // Ajouter les cartes spéciales (Wild et +4 Wild)
	    for (int i = 0; i < 4; i++) {
	        WildCard wildCard = new WildCard();
	        deckpile.add(wildCard);

	        Plus4Card plus4Card = new Plus4Card();
	        deckpile.add(plus4Card);
	    }
	}

	//melanger les cartes afin de pouvoire les distribuer
	public void shuffle() {
		Collections.shuffle(deckpile);
	}

	//la methode distribuer qui retourne une cartes du deck pour la distribuer sur un joueure dans la classe game
	public Card Drawcard() {
	            Card card = deckpile.remove(0); // Prendre la première carte du deck
	            return card;
	}

	//la methode deck is empty pour verifier si on le joueure peut piocher si il n'est pas vide le joueure peut pioche sinon on vas reconstuire le deck a partire de gamepile
	public boolean deckisempty() {
		return deckpile.isEmpty();
	}

    //on utilise cette methode pour avoire la derniere carte ajouter la liste game pour savoire si la prochaine carte que le joueure vas poser est jouable ou non
	public Card getfirstcard() {
		Card card = gamepile.get(gamepile.size() -1);
		return card;
		}
	
	
	//la methode add to game pile pour qu'on puisse ajouter des carte a la pile du jeux en cours du jeux
	public void addtogamepile(Card card) {
		gamepile.add(card);
	}
	
	//la methode reset deck pour reconstruire le deck a partire de la gamepile vue qu'elle contiendra presque toute les carte, en cas ou il est devenu vide et la game ne s'est pas finit
	public void resetdeck() {
		Card card = gamepile.remove(0);
		deckpile.addAll(gamepile);
		gamepile.add(card);
	}






}