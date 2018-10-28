public class q5 {

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
				boolean result = dfa1.isEquivalent(dfa2);
				if (result) {
					System.out.println("equivalent");
				} else {
					System.out.println("not equivalent");
				}
			}
		}
	}
}
