public class q3 {

	public static void main(String[] args) {

		DFA dfa;

		if (args.length == 0) {
			System.out.println("Arguments expected, none found");
			System.exit(0);
		} else {
			if (args.length != 2) {
				System.out.println("Two arguments expected");
				System.exit(0);
			} else {
				DFA dfa1 = new DFA(args[0]);
				DFA dfa2 = new DFA(args[1]);
				DFA dfa3 = dfa1.SymmetricDifference(dfa2);
				System.out.println(dfa3.toString());
			}
		}
	}
}
