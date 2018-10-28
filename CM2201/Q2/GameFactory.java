public class GameFactory {

	public static GameInterface getGame(String type){

		if (type.equalsIgnoreCase("c")) {
			GameInterface game = new CardGame();
			return game;
		} else if (type.equalsIgnoreCase("d")) {
			GameInterface game = new DieGame();
			return game;
		}

		return null;
	}
}
