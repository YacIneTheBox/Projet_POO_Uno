package projetUNO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
	List<Card> deckpile = new ArrayList<>();
	List<Card> lapiochepile = new ArrayList<>();
	List<Card> gamepile = new Arraylist<>();
	
	public void initgamepile() {
		Card card = deckpile.remove(0);
		gamepile.add(card);
	}
		
	
	
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

	
	public void shuffle() {
		Collections.shuffle(deckpile);
	}
	
	public void emptydeckpile() {
		deckpile.clear(); //supression de tout les elements du deck
	}
	
	public void addtolapioche() {
		// Ajouter toutes les cartes restantes du deckpile à lapioche
	    lapiochepile.addAll(deckpile);

	    // Vider deckpile, car toutes ses cartes sont maintenant dans lapioche
	    deckpile.clear();
	}
	
	public void addtodeckpile(Card nvCard) {
		deckpile.add(nvCard);
	}

	//la methode distribuer qui prend en parametre le nombre de joueur dans la partie;
	public void destribuer(int nbplayer, List<Player> p1) {
		 // Vérification : il doit y avoir suffisamment de joueurs et de cartes
	    if (p1.size() != nbplayer) {
	        throw new IllegalArgumentException("Le nombre de joueurs ne correspond pas à la liste fournie.");
	    }
	    if (deckpile.size() < nbplayer * 7) {
	        throw new IllegalStateException("Pas assez de cartes dans le deck pour distribuer.");
	    }

	    // Distribution des 

	    for (int i = 0; i < 7; i++) { // Chaque joueur reçoit 7 cartes
	        for (Player player : p1) {
	            Card card = deckpile.remove(0); // Prendre la première carte du deck
	            player.receivcard(card); // Ajouter la carte au joueur
	        }
	    }
	}

	public void removefromlapioche() {
		//enlever le premier element de la pile lapioche
		lapiochepile.remove(0);}

	public boolean lapiocheisempty() {
		return lapioche.isEmpty();
	}
