public class Plus2Card extends Card {
    public String effect = "plus2";

    public boolean canPlay(Card c) {
        boolean canPlay = false;
        if (c.getColor().equals(this.getColor()) || c instanceof Plus2Card) {
            canPlay = true;
        }
        return canPlay;
    }

    public void afficher() {
        System.out.println("Plus 2 Card: " + this.getColor());
    }
}