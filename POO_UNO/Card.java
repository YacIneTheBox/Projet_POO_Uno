package POO_UNO;
public class Card {
    public String color;
    public String effect;
    

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
    public boolean canPlay(Card c) {
    	return false;
    }
    public void afficher() {
    	System.out.println("Carte");
    }
}
