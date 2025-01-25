public class SkipCard extends Card {
    public String effect = "skip";

    public boolean canPlay(Card c) {
        boolean canPlay = false;
        if (c.getColor().equals(this.getColor()) || c instanceof SkipCard) {
            canPlay = true;
        }
        return canPlay;
    }

    public void afficher() {
        System.out.println("Skip Card: " + this.getColor());
    }
}