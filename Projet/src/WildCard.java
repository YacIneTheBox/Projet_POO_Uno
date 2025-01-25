public class WildCard extends Card {
    public WildCard() {
        super();
    }

    public boolean canPlay(Card c) {
        return true;
    }

    public void afficher() {
        System.out.println("Wild Card: " + this.getEffect() +" "+ this.getColor());
    }
}
