package POO_UNO;

public class Bot extends Player {
	
	// constructeur du bot
	
	public Bot(int numero_jouer,String name) {
		super(numero_jouer,true,name);
	}
	
	// algo de choix de la carte a poser 
	

	public Card PlayBot(Card Last_carte) {
		for (Card carte : main) {
			// le cas ou une carte a la meme couleur
			if (carte.getColor() == Last_carte.getColor()) {
				return carte;
			}
			// le cas ou meme chiffre
			if (carte instanceof RegularCard && Last_carte instanceof RegularCard ) {
				RegularCard Card = (RegularCard) carte;
				RegularCard Last_card = (RegularCard) Last_carte;
				
				if (Card.getNumber() == Last_card.getNumber()) {
					return Card;
				}
			}
			// le cas ou on tombe sur une carte joker
			if (carte instanceof Plus4Card || carte instanceof WildCard) {
					return carte;
			}
		}
		return null;
	}
}
