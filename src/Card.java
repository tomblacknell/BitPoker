

public class Card implements Comparable<Card> {

    private CardRank rank;
    private CardSuit suit;

    public Card(CardRank rank, CardSuit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public CardRank getRank() {
        return this.rank;
    }

    public CardSuit getSuit() {
        return this.suit;
    }

    @Override
    public int compareTo(Card otherCard) {
        if (this.getRank().toValue() < otherCard.getRank().toValue()) {
            return -1;
        } else if (this.getRank().toValue() == otherCard.getRank().toValue()) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public String toString() {
        if (this.suit.equals(CardSuit.HEART) || this.suit.equals(CardSuit.DIAMOND)) {
            return Colour.RED + this.rank.toString() + this.suit.toString() + Colour.RESET;
        } else {
            return this.rank.toString() + this.suit.toString();
        }
    }

}