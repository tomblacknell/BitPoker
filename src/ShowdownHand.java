import java.util.ArrayList;
import java.util.Collections;

public class ShowdownHand {

    private ArrayList<Card> cards;

    public ShowdownHand() {
        this.cards = new ArrayList<Card>();
    }

    public void printCards() {
        for (Card card : cards) System.out.print(card.toString());
        System.out.println();
    }

    public void add(Card card) {
        this.cards.add(card);
        Collections.sort(this.cards);
    }

    public void clear() {
        this.cards.clear();
    }

    public int getType() {

        int score = 0;

        if (    (   (cards.get(1).getRank().toValue() == (cards.get(0).getRank().toValue() - 1)) &&
                    (cards.get(2).getRank().toValue() == (cards.get(1).getRank().toValue() - 1)) &&
                    (cards.get(3).getRank().toValue() == (cards.get(2).getRank().toValue() - 1)) &&
                    (cards.get(4).getRank().toValue() == (cards.get(3).getRank().toValue() - 1))) &&

                (   (cards.get(0).getSuit().equals(cards.get(1).getSuit())) &&
                    (cards.get(0).getSuit().equals(cards.get(2).getSuit())) &&
                    (cards.get(0).getSuit().equals(cards.get(3).getSuit())) &&
                    (cards.get(0).getSuit().equals(cards.get(4).getSuit())))) {

            score = 10000000 + Collections.max(cards).getRank().toValue();

            //System.out.println("Straight Flush");

        } else if (     (       (cards.get(0).getRank().equals(cards.get(1).getRank())) &&
                                (cards.get(0).getRank().equals(cards.get(2).getRank())) &&
                                (cards.get(0).getRank().equals(cards.get(3).getRank()))) ||

                        (       (cards.get(1).getRank().equals(cards.get(2).getRank())) &&
                                (cards.get(1).getRank().equals(cards.get(3).getRank())) &&
                                (cards.get(1).getRank().equals(cards.get(4).getRank())))) {

            score = 9000000 + cards.get(2).getRank().toValue();

            //System.out.println("Four of a Kind");

        } else if (     (       (cards.get(0).getRank().equals(cards.get(1).getRank())) &&
                                (cards.get(0).getRank().equals(cards.get(2).getRank())) &&
                                (cards.get(3).getRank().equals(cards.get(4).getRank()))) ||

                        (       (cards.get(0).getRank().equals(cards.get(1).getRank())) &&
                                (cards.get(2).getRank().equals(cards.get(3).getRank())) &&
                                (cards.get(2).getRank().equals(cards.get(4).getRank())))) {

            score = 8000000 + cards.get(2).getRank().toValue();

            //System.out.println("Full House");

        } else if (     (cards.get(0).getSuit().equals(cards.get(1).getSuit())) &&
                        (cards.get(0).getSuit().equals(cards.get(2).getSuit())) &&
                        (cards.get(0).getSuit().equals(cards.get(3).getSuit())) &&
                        (cards.get(0).getSuit().equals(cards.get(4).getSuit()))) {

            score =  7000000 + (10000 * cards.get(0).getRank().toValue()) +
                    (1000 * cards.get(1).getRank().toValue()) +
                    (100 * cards.get(2).getRank().toValue()) +
                    (10 * cards.get(3).getRank().toValue()) +
                    cards.get(4).getRank().toValue();

            //System.out.println("Flush");

        } else if (     (cards.get(1).getRank().toValue() == cards.get(0).getRank().toValue() - 1) &&
                        (cards.get(2).getRank().toValue() == cards.get(1).getRank().toValue() - 1) &&
                        (cards.get(3).getRank().toValue() == cards.get(2).getRank().toValue() - 1) &&
                        (cards.get(4).getRank().toValue() == cards.get(3).getRank().toValue() - 1)) {

            score = 6000000 + Collections.max(cards).getRank().toValue();

            //System.out.println("Straight");

        } else if (     (   (cards.get(1).getRank() == cards.get(0).getRank()) &&
                            (cards.get(2).getRank() == cards.get(1).getRank()) ||

                        (   (cards.get(2).getRank() == cards.get(1).getRank()) &&
                            (cards.get(3).getRank() == cards.get(2).getRank())) ||

                        (   (cards.get(3).getRank() == cards.get(2).getRank()) &&
                            (cards.get(4).getRank() == cards.get(3).getRank())))) {

            score = 5000000 + cards.get(2).getRank().toValue();

            //System.out.println("Three of a Kind");

        } else if (     (       (cards.get(0).getRank().equals(cards.get(1).getRank())) &&
                                (cards.get(2).getRank().equals(cards.get(3).getRank()))) ||

                        (       (cards.get(1).getRank().equals(cards.get(2).getRank())) &&
                                (cards.get(3).getRank().equals(cards.get(4).getRank()))) ||

                        (       (cards.get(0).getRank().equals(cards.get(1).getRank())) &&
                                (cards.get(3).getRank().equals(cards.get(4).getRank())))) {

            int highCardPos = 0;
            if (cards.get(0).getRank().equals(cards.get(1).getRank()) && (cards.get(2).getRank().equals(cards.get(3).getRank()))) {
                highCardPos = 4;
            } else if ((cards.get(1).getRank().equals(cards.get(2).getRank())) && (cards.get(3).getRank().equals(cards.get(4).getRank()))) {
                highCardPos = 0;
            } else {
                highCardPos = 2;
            }

            score = 4000000 + (10000 * cards.get(1).getRank().toValue()) + (1000 * cards.get(3).getRank().toValue()) + (100 * cards.get(highCardPos).getRank().toValue());

            //System.out.println("Two Pair");

        } else if (false) {

            //System.out.println("One Pair");

        } else {

            score =  1000000 + (10000 * cards.get(0).getRank().toValue()) +
                    (1000 * cards.get(1).getRank().toValue()) +
                    (100 * cards.get(2).getRank().toValue()) +
                    (10 * cards.get(3).getRank().toValue()) +
                    cards.get(4).getRank().toValue();

            //System.out.println("High Card");

        }

        return score;

    }


}
