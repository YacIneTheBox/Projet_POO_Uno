package POO_UNO;
public class WildCard extends Card{

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public boolean canPlay(Card c) {return true;}

}
