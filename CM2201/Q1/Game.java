import java.io.*;
import java.util.*;

public class Game {
    // The BufferedReader used throughout
    public static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    // The random number generator used throughout
    public static RandomInterface r = new LinearCongruentialGenerator();

    // Variable(s) used in the card game methods
    public static ArrayList<String> cardList;
    public static HashSet<String> cardsChosen = new HashSet<String>();

    public static void playCardGame() throws Exception {
        // Play card game:

        // Initialise the game
        initialiseCardGame();

        // Play the main game phase
        mainCardGame();

        // Now see if (s)he has won!
        declareCardGameWinner();
    }

    public static void initialiseCardGame() throws Exception {
        // The initialisation phase:

        // Create a list of cards ... and shuffle them
        String cards[] = {"AHrts", "2Hrts", "3Hrts", "4Hrts", "5Hrts", "6Hrts",
                          "7Hrts", "8Hrts", "9Hrts", "10Hrts", "JHrts",
                          "QHrts", "KHrts",
                          "ADmnds", "2Dmnds", "3Dmnds", "4Dmnds", "5Dmnds",
                          "6Dmnds", "7Dmnds", "8Dmnds", "9Dmnds", "10Dmnds",
                          "JDmnds", "QDmnds", "KDmnds",
                          "ASpds", "2Spds", "3Spds", "4Spds", "5Spds", "6Spds",
                          "7Spds", "8Spds", "9Spds", "10Spds", "JSpds",
                          "QSpds", "KSpds",
                          "AClbs", "2Clbs", "3Clbs", "4Clbs", "5Clbs", "6Clbs",
                          "7Clbs", "8Clbs", "9Clbs", "10Clbs", "JClbs",
                          "QClbs", "KClbs"};
        cardList = new ArrayList<String> (Arrays.asList(cards));
        // Taking advantage of "generics" to tell the compiler all the elements will be Strings

        // Shuffle them
        for (int i = 0; i < 100; i++) {
            // choose two random cards at random and swap them, 100 times
            int firstIndex = ((int) (r.next() * 52));
            int secondIndex = ((int) (r.next() * 52));
            String temp = (String) cardList.get(firstIndex);
            cardList.set(firstIndex, cardList.get(secondIndex));
            cardList.set(secondIndex, temp);
        }

        // Print out the result
        System.out.println(cardList);
    }

    public static void mainCardGame() throws Exception {
        // The main game:

        // Let user select two cards from the pack
        for (int i = 0; i < 2; i++) {
            System.out.println("Hit <RETURN> to choose a card");
            br.readLine();

            int cardChoice = (int) (r.next() * cardList.size());
            System.out.println("You chose " + cardList.get(cardChoice));
            cardsChosen.add(cardList.remove(cardChoice));
        }

        // Display the cards chosen and remaining cards
        System.out.println("Cards chosen: " + cardsChosen);
        System.out.println("Remaining cards: " + cardList);

    }

    public static void declareCardGameWinner() throws Exception {
        // Declare the winner:

        // User wins if one of them is an Ace
        System.out.println("Cards chosen: " + cardsChosen);
        if (cardsChosen.contains("AHrts") || cardsChosen.contains("ADmnds") ||
            cardsChosen.contains("ASpds") || cardsChosen.contains("AClbs")) {
            System.out.println("You won!");
        }
        else System.out.println("You lost!");
    }

    //Variable(s) used in the die game methods
    public static HashSet<Integer> numbersRolled = new HashSet<Integer>();

    public static void playDieGame() throws Exception {
        // Play die game:

        // Initialise the game
        initialiseDieGame();

        // Play the main game phase
        mainDieGame();

        // Now see if (s)he has won!
        declareDieGameWinner();
    }

    public static void initialiseDieGame() throws Exception {
        // The initialisation phase:

        // Actually there isn't anything to do here
    }

    public static void mainDieGame() throws Exception {
        // The main game:

        // Let the user roll the die twice
        for (int i = 0; i < 2; i++) {
            System.out.println("Hit <RETURN> to roll the die");
            br.readLine();
            int dieRoll = (int)(r.next() * 6) + 1;

            System.out.println("You rolled " + dieRoll);
            numbersRolled.add(new Integer(dieRoll));
        }

        // Display the numbers rolled
        System.out.println("Numbers rolled: " + numbersRolled);
    }

    public static void declareDieGameWinner() throws Exception {
        // Declare the winner:

        // User wins if at least one of the die rolls is a 1
        if (numbersRolled.contains(new Integer(1))) {
            System.out.println("You won!");
        }
        else System.out.println("You lost!");
    }

    public static void main(String[] args) throws Exception {
        // Ask whether to play a card game or a die game

        System.out.print("Card (c) or Die (d) game? ");
        String ans = br.readLine();

        if (ans.equals("c")) {
            playCardGame();
        }

        else if (ans.equals("d")) {
            playDieGame();
        }

        else System.out.println("Input not understood");
    }
}
