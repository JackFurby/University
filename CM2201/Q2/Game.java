import java.io.*;

public class Game {

	public static void main(String[] args) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Card (c) or Die (d) game? ");
        String ans = br.readLine();

        GameInterface newGame = GameFactory.getGame(ans);

        if (newGame == null) {
            System.out.println("Input not understood"); // Game does not exist
        } else {
            try { // Game exists, starts game
                newGame.initialiseGame();
                newGame.mainGame();
                newGame.declareGameWinner();
            } catch (Exception e) {
                System.out.println("Unexpected error. See " + e + " for more");
            }
        }
    }
}
