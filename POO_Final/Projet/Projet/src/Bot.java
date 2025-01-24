public class Bot extends Player {
    public Bot(String name) {
        super(name);
    }

    public int chooseCard(Card Last_carte) {
        for (int i = 0; i < main.size(); i++) {
            if (main.get(i).canPlay(Last_carte)) {
                return i;
            }
        }
        System.out.println("No card playable");
        return -1;
    }
    
    public Card chooseColor(Card currentCard) {
    	currentCard.setColor("red");
    	return currentCard;
    }
}