import java.util.*;

public class Bot extends Player {
    public Bot(String name) {
        super(name);
    }

    public int chooseCard(Card Last_carte) { // ajout les carte jouable dans un Arraylist
        Random rand = new Random();
        List<Integer> playableCardIndices = new ArrayList<>();
        for (int i = 0; i < main.size(); i++) {
            if (main.get(i).canPlay(Last_carte)) {
                playableCardIndices.add(i); 
            }
        }
        if (!playableCardIndices.isEmpty()) { // choisis aleatoirement une carte parmis les carte jouable
            int randomIndex = rand.nextInt(playableCardIndices.size());
            return playableCardIndices.get(randomIndex); 
        } else {
            return -1; 
        }
    }
    public String determineMostCommonColor() {
        Map<String, Integer> colorCounts = new HashMap<>();
        for (int i = 0; i < this.nbrCarteRestante(); i++) {
            String color = this.getCardNum(i).getColor();
            colorCounts.put(color, colorCounts.getOrDefault(color, 0) + 1);
        }
        return colorCounts.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("red");
    }   
    public Card chooseColor(Card currentCard) {
        int maximum = 0;
        String colorChoice = "red"; // choix par défaut

        String[] colors = {"red", "yellow", "green", "blue"};

        for (String color : colors) {
            int cmpt = 0;
            for (Card carte : main) {
                if (color.equals(carte.getColor())) {
                    cmpt++;
                }
            }
            if (maximum < cmpt) {
                maximum = cmpt;
                colorChoice = color; // la couleur la plus présante est prise par le bot
            }
        }

        currentCard.setColor(colorChoice);
        return currentCard;
    }
    
}