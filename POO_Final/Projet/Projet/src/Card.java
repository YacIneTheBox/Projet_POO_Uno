public class Card {
    public String color;
    public String effect;

    public Card() {
        this.color = "";
        this.effect = "";
    }

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
        System.out.println("Card: " + this.getColor());
    }

    public String getEffect() {
        return effect;
    }
    public void setEffect(String effect) {
        this.effect = effect;
    }
}
