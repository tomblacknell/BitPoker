import java.util.ArrayList;
import java.util.Random;

public class Deck {

    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<Card>();

        buildDeck();
    }

    public void shuffleDeck() {
        ArrayList<Card> temp = new ArrayList<Card>();
        Random random = new Random();
        int randomInt;

        while(this.cards.size() != 0) {
            randomInt = random.nextInt(this.cards.size());
            temp.add(this.cards.get(randomInt));
            this.cards.remove(randomInt);
        }

        this.cards.addAll(temp);
    }

    public void printDeck() {
        for (Card card : this.cards) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();
    }

    public Card removeCardFromTop() {
        Card topCard = this.cards.get(0);
        this.cards.remove(0);
        return topCard;
    }

    public void buildDeck() {
        for (CardSuit suit : CardSuit.values()) {
            for (CardRank rank : CardRank.values()) {
                this.cards.add(new Card(rank, suit));
            }
        }

        this.shuffleDeck();
    }

}