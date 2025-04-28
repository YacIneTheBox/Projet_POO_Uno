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

    @Override
    public String toString() {
        if (this instanceof RegularCard) {
            RegularCard rc = (RegularCard) this;
            return rc.getNumber() + " " + getColor();
        } else if (this instanceof SkipCard) {
            return "Skip " + getColor();
        } else if (this instanceof ReversCard) {
            return "Reverse " + getColor();
        } else if (this instanceof Plus2Card) {
            return "+2 " + getColor();
        } else if (this instanceof Plus4Card) {
            return "+4 Wild";
        } else if (this instanceof FourColorCard) {
            return "Wild";
        } else {
            return "Unknown Card";
        }
    }
}
