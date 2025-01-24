package POO_UNO;

public class Bot extends Player {
	
	// constructeur du bot
	
	public Bot(String name) {
		super(name);
	}
	
	// algo de choix de la carte a poser 
	

	public int chooseCard(Card Last_carte) { // return the num of the played card
		int i=1;
		for (Card carte : main) {
			// le cas ou une carte a la meme couleur
			if (carte.getColor() == Last_carte.getColor()) {
				return i;
			}
			// le cas ou meme chiffre
			if (carte instanceof RegularCard && Last_carte instanceof RegularCard ) {
				RegularCard Card = (RegularCard) carte;
				RegularCard Last_card = (RegularCard) Last_carte;
				
				if (Card.getNumber() == Last_card.getNumber()) {
					return i;
				}
			}
			// le cas ou on tombe sur une carte joker
			if (carte instanceof Plus4Card || carte instanceof WildCard) {
					return i;
			}
			i++;
		}
		System.out.println("No card playable");
		return -1;
	}
}
