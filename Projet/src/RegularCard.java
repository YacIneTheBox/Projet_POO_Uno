public class RegularCard extends Card {
    public int number;
    public String effect = "regular";

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean canPlay(Card c) {
        boolean canPlay = false;
        if (c instanceof RegularCard) {
            RegularCard temp = (RegularCard) c;
            if (temp.getColor().equals(this.getColor()) || temp.getNumber() == this.getNumber()) {
                canPlay = true;
            }
        } else if (c.getColor().equals(this.getColor())) {
            canPlay = true;
        }
        return canPlay;
    }

    public void afficher() {
        System.out.println("Regular Card - Color: " + this.getColor() + ", Number: " + this.getNumber());
    }
}
