public class Player {

    private String name;
    private int chips, bet;
    private boolean folded, hasNotActed;
    private Card hole1, hole2;

    public Player(String name, int buyIn) {
        this.name = name;
        this.chips = buyIn;
        this.bet = 0;
        this.folded = false;
        this.hasNotActed = true;
    }

    public String getName() {
        return this.name;
    }

    public int getChips() {
        return this.chips;
    }

    public boolean isFolded() {
        return this.folded;
    }

    public void fold() {
        this.folded = true;
    }

    public int getBet() {
        return this.bet;
    }

    public void playChips(int chips) {
        if (chips > this.chips) {
            System.out.println("You don't have this many chips");
        } else {
            this.chips -= chips;
            this.bet += chips;
        }
    }

    public void resetBet() {
        this.bet = 0;
    }

    public void setActed(boolean acted) { this.hasNotActed = !acted; }

    public Card getHole1() {
        return this.hole1;
    }

    public Card getHole2() {
        return this.hole2;
    }

    public void setHole1(Card hole1) {
        this.hole1 = hole1;
    }

    public void setHole2(Card hole2) {
        this.hole2 = hole2;
    }

    public boolean hasNotActed() { return this.hasNotActed; }

}