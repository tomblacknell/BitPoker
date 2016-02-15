
public class Main {

    public static void main(String[] args) {

        Game game = new Game(5, 500, 1000);

        game.addPlayer(new Player("Will", 1000));
        game.addPlayer(new Player("Bob", 1000));
        game.addPlayer(new Player("Marge", 1000));
        game.addPlayer(new Player("Chris", 1000));

        game.start();

    }

}