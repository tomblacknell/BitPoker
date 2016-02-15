public enum CardRank {

    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"), JACK(11, "J"), QUEEN(12, "Q"), KING(13, "K"), ACE(14, "A");

    private int value;
    private String string;

    CardRank(int value, String string) {
        this.value = value;
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

    public int toValue() {
        return this.value;
    }

}
