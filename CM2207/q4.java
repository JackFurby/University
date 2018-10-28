public class q4 {

	public static void main(String[] args) {

		DFA dfa;

		if (args.length == 0) {
			System.out.println("Arguments expected, none found");
			System.exit(0);
		} else {
			DFA dfa1 = new DFA(args[0]);
			String accString = dfa1.acceptString();
			System.out.println(accString);
		}
	}
}
