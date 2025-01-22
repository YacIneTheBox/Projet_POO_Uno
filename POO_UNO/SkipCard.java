package POO_UNO;
public class SkipCard extends Card{

    public String effect ="skip";

    public String getEffect() {
        return effect;
    }

    public boolean canPlay(Card c) {
        boolean canPlay = false;
        if (c.getColor().equals(this.getColor()) || c instanceof SkipCard) {
            canPlay = true;
        }
        return canPlay;
    }

    public void play() {

    }
    
    public void afficher() {
    	System.out.println("Carte : "+ this.getEffect());
    }
}
