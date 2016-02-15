import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {

    private int smallBlind, bigBlind, minBuyIn, maxBuyIn, maxPlayers, minPlayers, dealerIndex, pot, currentTurnPlayerIndex, currentBet;
    private Player currentTurnPlayer;
    private ArrayList<Player> players;
    private ArrayList<Card> communityCards;
    private Deck deck;
    private Scanner userInput;

    public Game(int initialSmallBlind, int minBuyIn, int maxBuyIn) {
        System.out.println("New game created");

        this.deck = new Deck();

        this.pot = 0;

        this.minBuyIn = minBuyIn;
        this.maxBuyIn = maxBuyIn;

        this.maxPlayers = 8;
        this.minPlayers = 4;

        this.players = new ArrayList<Player>();
        this.communityCards = new ArrayList<Card>();

        this.smallBlind = initialSmallBlind;
        this.bigBlind = 2 * initialSmallBlind;

        this.userInput = new Scanner(System.in);
    }

    public void start() {
        if (this.players.size() >= this.minPlayers && this.players.size() <= this.maxPlayers) {

            System.out.println("New game started");

            this.pickDealer();
            System.out.println(getDealer().getName() + " will start as dealer");

            deck.buildDeck();

            playBlinds();

            dealHoles();

            // pre flop betting
            bettingRound(this.bigBlind, this.bigBlind);

            // deal the flop
            dealCommunityCards(3);
            printTable();

            // post flop betting
            bettingRound(this.bigBlind, 0);

            // deal the turn
            dealCommunityCards(1);
            printTable();

            // turn betting
            bettingRound(this.bigBlind, 0);

            // deal the river
            dealCommunityCards(1);
            printTable();

            // river betting
            bettingRound(this.bigBlind, 0);

            // showdown, decide winner
            showdown();

        } else {
            System.out.println("Game cannot be started, must be between " + this.minPlayers + " and " + this.maxPlayers + " players");
        }
    }

    private void showdown() {

        ArrayList<Card> allCards = new ArrayList<Card>();
        ShowdownHand showdownHand = new ShowdownHand();
        ArrayList<Player> winners = new ArrayList<Player>();
        int winningScore = 0;

        for (Player player : this.players) {
            allCards.clear();
            allCards.add(player.getHole1());
            allCards.add(player.getHole2());
            allCards.addAll(this.communityCards);

            for ( int i = 0; i < 7; i++ ) {
                for ( int j = i + 1; j < 7; j++ ) {
                    for ( int k = j + 1; k < 7; k++ ) {
                        for (int l = k + 1; l < 7; l++) {
                            for (int m = l + 1; m < 7; m++) {
                                showdownHand.clear();

                                showdownHand.add(allCards.get(i));
                                showdownHand.add(allCards.get(j));
                                showdownHand.add(allCards.get(k));
                                showdownHand.add(allCards.get(l));
                                showdownHand.add(allCards.get(m));

                                int value = showdownHand.getType();
                                if (value > winningScore) {
                                    winners.clear();
                                    winners.add(player);
                                } else if (value == winningScore) {
                                    winners.add(player);
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Player player : winners) {
            System.out.println(player.getName() + " won!");
        }

    }

    private void bettingRound(int minBet, int initialCurrentBet) {
        for (Player player : players) player.setActed(false);

        this.currentBet = initialCurrentBet;

        if (initialCurrentBet != 0) {
            // first betting round
            // player next to BB bets
            currentTurnPlayerIndex = players.indexOf(getBigBlindPlayer());
            movePlayer();
        } else {
            // normal betting round
            // player next to dealer bets
            currentTurnPlayerIndex = players.indexOf(getDealer());
            movePlayer();
        }

        while ((!allNonFoldedPlayersHaveActed()) || (!allNonFoldedPlayersBetsEqual())) {
            System.out.println("Current Bet: " + currentBet);

            currentTurnPlayer = players.get(currentTurnPlayerIndex);

            printTable();

            ArrayList<Integer> validOptions = new ArrayList<Integer>();

            int playerChoice = -1;
            while(!validOptions.contains(playerChoice)) {
                System.out.print(currentTurnPlayer.getName() + "'s turn: ");
                if (currentTurnPlayer.getBet() == currentBet) { System.out.print("Check(1) "); validOptions.add(1); }
                if (currentTurnPlayer.getChips() >= (currentBet - currentTurnPlayer.getBet()) && currentTurnPlayer.getBet() != currentBet) { System.out.print("Call $"+ (currentBet - currentTurnPlayer.getBet()) + " (2) "); validOptions.add(2); }
                if (currentTurnPlayer.getChips() >= minBet) { System.out.print("Raise(3) "); validOptions.add(3); }
                System.out.print("Fold(4) | Choice: "); validOptions.add(4);

                playerChoice = userInput.nextInt();
            }

            System.out.println();

            switch(playerChoice) {
                case 2: currentTurnPlayer.playChips(currentBet - currentTurnPlayer.getBet()); break;
                case 3: { // RAISE
                    int raiseTo = -1;
                    while (raiseTo > currentTurnPlayer.getChips() || raiseTo < bigBlind + currentBet) {
                        System.out.print("Raise to (min. " + (bigBlind + currentBet) + "): ");
                        raiseTo = userInput.nextInt();
                    }

                    currentBet = raiseTo;

                    currentTurnPlayer.playChips(currentBet - currentTurnPlayer.getBet());
                    break;
                }
                case 4: currentTurnPlayer.fold(); break;
            }

            currentTurnPlayer.setActed(true);
            movePlayer();
        }

        // Chips to the middle
        for (Player player : players) {
            this.pot += player.getBet();
            player.resetBet();
        }
    }

    private boolean allNonFoldedPlayersHaveActed() {
        for (Player player : players) {
            if (player.hasNotActed() && !player.isFolded()) {
                System.out.println(Colour.RED + player.getName() + " hasn't acted yet" + Colour.RESET);
                return false;
            }
        }
        //System.out.println("all players have acted");
        return true;
    }

    private boolean allNonFoldedPlayersBetsEqual() {
        for (Player player : players) {
            if ((!player.isFolded()) && (player.getBet() != currentBet)) {
                System.out.println(player.getName() + " : " + player.getBet() + " != " + currentBet);
                System.out.println(Colour.RED + "some non-folded players have non equal bets" + Colour.RESET);
                return false;
            }
        }
        //System.out.println("all non-folded players bets are equal");
        return true;
    }

    private void playBlinds() {
        getSmallBlindPlayer().playChips(smallBlind);
        System.out.println(getSmallBlindPlayer().getName() + " plays the small blind of " + smallBlind);
        getBigBlindPlayer().playChips(bigBlind);
        System.out.println(getBigBlindPlayer().getName() + " plays the big blind of " + bigBlind);
    }

    private void dealHoles() {
        for (Player player : this.players) {
            player.setHole1(this.deck.removeCardFromTop());
            player.setHole2(this.deck.removeCardFromTop());
        }
    }

    private void dealCommunityCards(int number) {
        for (int i = 0; i < number; i++) communityCards.add(deck.removeCardFromTop());
    }

    public void addPlayer(Player player) {
        if (player.getChips() < this.minBuyIn) {
            System.out.println(player.getName() + " denied entry - buy in must be more than " + this.minBuyIn);
        } else if (player.getChips() > this.maxBuyIn) {
            System.out.println(player.getName() + " denied entry - buy in must be less than " + this.maxBuyIn);
        } else {
            this.players.add(player);
            System.out.println(player.getName() + " joined the game");
        }
    }

    public Deck getDeck() {
        return this.deck;
    }

    private void movePlayer() {
        if (currentTurnPlayerIndex == players.size() - 1) currentTurnPlayerIndex = 0; else currentTurnPlayerIndex++;
        currentTurnPlayer = players.get(currentTurnPlayerIndex);
        while (currentTurnPlayer.isFolded()) {
            if (currentTurnPlayerIndex == players.size() - 1) currentTurnPlayerIndex = 0; else currentTurnPlayerIndex++;
            currentTurnPlayer = players.get(currentTurnPlayerIndex);
        }
    }

    private void pickDealer() {
        Random random = new Random();
        this.dealerIndex = random.nextInt(this.players.size());
    }

    public Player getDealer() {
        return this.players.get(this.dealerIndex);
    }

    public Player getSmallBlindPlayer() {
        if (this.dealerIndex == this.players.size() - 1) {
            return this.players.get(0);
        } else {
            return this.players.get(this.dealerIndex + 1);
        }
    }

    public Player getBigBlindPlayer() {
        if (this.dealerIndex == this.players.size() - 1) {
            return this.players.get(1);
        } else if (this.dealerIndex == this.players.size() - 2) {
            return this.players.get(0);
        } else {
            return this.players.get(this.dealerIndex + 2);
        }
    }

    public void moveDealer() {
        if (this.dealerIndex == this.players.size() - 1) {
            this.dealerIndex = 0;
        } else {
            this.dealerIndex ++;
        }
        System.out.println(getDealer().getName() + " is now the dealer");
    }

    public void printTable() {
        for (Player player : this.players) System.out.print("-------------");
        System.out.println();
        System.out.println("Pot: $" + this.pot);
        for (Card card : this.communityCards) System.out.print(card.toString() + " ");
        System.out.println();
        for (Player player : this.players) {
            String string = player.getBet() != 0 ? "$" + player.getBet() : "";
            StringBuilder builder = new StringBuilder("             ");
            builder.replace(6 - (string.length() / 2), 6 - (string.length() / 2) + string.length(), string);
            System.out.print(builder.toString());
        }
        System.out.println();
        for (Player player : this.players) {
            if (this.players.indexOf(player) == this.currentTurnPlayerIndex) {
                System.out.print(Colour.GREEN + "|-----------|" + Colour.RESET);
            } else if (player.isFolded()) {
                System.out.print(Colour.BLACK + "|-----------|" + Colour.RESET);
            } else {
                System.out.print(Colour.RESET + "|-----------|");
            }
        }
        System.out.println();
        for (Player player : this.players) {
            String string;
            if (player.equals(getDealer())) string = player.getName().toUpperCase();
            else string = player.getName().toLowerCase();
            StringBuilder builder = new StringBuilder("             ");
            builder.replace(6 - (string.length() / 2), 6 - (string.length() / 2) + string.length(), string);
            System.out.print(builder.toString());
        }
        System.out.println();
        for (Player player : this.players) {
            System.out.print("   ");
            System.out.print((player.getHole1() == null ? "**" : player.getHole1().toString()) + "   ");
            System.out.print(player.getHole2() == null ? "**" : player.getHole2().toString());
            System.out.print("   ");
        }
        System.out.println();
        for (Player player : this.players) {
            String string = "$" + player.getChips();
            StringBuilder builder = new StringBuilder("             ");
            builder.replace(6-(string.length()/2), 6-(string.length()/2) + string.length(), string);
            System.out.print(builder.toString());
        }
        System.out.println();

        for (Player player : this.players) {
            if (this.players.indexOf(player) == this.currentTurnPlayerIndex) {
                System.out.print(Colour.GREEN + "|-----------|" + Colour.RESET);
            } else if (player.isFolded()) {
                System.out.print(Colour.BLACK + "|-----------|" + Colour.RESET);
            } else {
                System.out.print(Colour.RESET + "|-----------|");
            }
        }

        System.out.println();
        for (Player player : this.players) System.out.print("-------------");
        System.out.println();
    }

}
