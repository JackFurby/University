import java.util.*;
import java.io.*;

public class DieGame implements GameInterface {

    private HashSet<Integer> numbersRolled = new HashSet<Integer>();

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private RandomInterface r = new LinearCongruentialGenerator();

    public void mainGame() throws Exception {

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

    public void initialiseGame() throws Exception {
        return;
    }

	public void declareGameWinner() {
        if (numbersRolled.contains(new Integer(1))) {
            System.out.println("You won!");
        }
        else System.out.println("You lost!");
    }
}
