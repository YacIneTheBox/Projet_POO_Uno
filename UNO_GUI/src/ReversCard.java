public class ReversCard extends Card {
    public String effect = "reverse";

    public boolean canPlay(Card c) {
        boolean canPlay = false;
        if (c.getColor().equals(this.getColor()) || c instanceof ReversCard) {
            canPlay = true;
        }
        return canPlay;
    }

    public void afficher() {
        System.out.println("Reverse Card: " + this.getColor());
    }
}
