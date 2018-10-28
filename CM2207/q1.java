public class q1 {

	public static void main(String[] args) {

		DFA dfa;

		if (args.length == 0) {
			System.out.println("Arguments expected, none found");
			System.exit(0);
		} else {
			dfa = new DFA(args[0]);
			dfa.setComplement();
			System.out.println(dfa.toString());
		}
	}
}
