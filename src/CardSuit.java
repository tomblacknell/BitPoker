public enum CardSuit {

    HEART("♥"), DIAMOND("♦"), CLUB("♣"), SPADE("♠");

    private String string;

    CardSuit(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return this.string;
    }

}
