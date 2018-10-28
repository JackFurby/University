import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;


public class DFA {
	private ArrayList<String> alphabet;
	private HashMap<String, HashMap<String, String>> transitions = new HashMap<String, HashMap<String, String>>();
	private String startState;
	private ArrayList<String> acceptStates;

	//initialises a new DFA from txt file
	public DFA(String filename) {

		try {
			String line;
			int lineNum = 0;
			int stateNum = 0;
			int alphabetSize;
			ArrayList<String> allStates = new ArrayList<String>();

			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while((line = bufferedReader.readLine()) != null) {
				if (lineNum == 0) { // number of states in DFA
					stateNum = Integer.parseInt(line);
				} else if (lineNum == 1) { // states in DFA
					allStates = new ArrayList<String>(Arrays.asList(line.trim().split("\\s+"))); // holds every state for now
				} else if (lineNum == 2) { // alphabet size for DFA
					alphabetSize = Integer.parseInt(line);
				} else if (lineNum == 3) { // alphabet for DFA
					alphabet = new ArrayList<String>(Arrays.asList(line.trim().split("\\s+")));
				} else if (lineNum > 3 && lineNum < 4 + stateNum) { //transitions in DFA
					HashMap<String, String> transition = new HashMap<String, String>();
					List<String> toState = new ArrayList<>(Arrays.asList(line.trim().split("\\s+")));
					for (int i = 0; i < toState.size(); i++) {
						transition.put(alphabet.get(i), toState.get(i));
					}
					transitions.put(allStates.get(lineNum - 4), transition);
				} else if (lineNum == stateNum + 4) { // start state for DFA
					startState = line.trim();
				} else if (lineNum == stateNum + 6) { //accept states for DFA
					acceptStates = new ArrayList<String>(Arrays.asList(line.trim().split("\\s+")));
				}
				lineNum++;
			}

			bufferedReader.close();

		} catch(FileNotFoundException e) {
			System.out.println("Unable to open file '" + filename + "'");
		} catch(IOException e) {
			System.out.println("Error reading file '" + filename + "'");
		}
	}

	//initialises a new DFA from individual components
	public DFA(ArrayList<String> newAlphabet,
		HashMap<String, HashMap<String, String>> newTransitions,
		String newStartState,
		ArrayList<String> newAcceptStates) {

		alphabet = newAlphabet;
		transitions = newTransitions;
		startState = newStartState;
		acceptStates = newAcceptStates;
	}

	//initialises a new DFA from a DFA
	public DFA(DFA dfa) {

		alphabet = new ArrayList<String>(dfa.getAlphabet());
		transitions = new HashMap<String, HashMap<String, String>>(dfa.getTransitions());
		startState = new String(dfa.getStartState());
		acceptStates = new ArrayList<String>(dfa.getAcceptStates());
	}

	// returns the number of states in DFA
	int getStatesNum() {
		return this.getStates().size();
	}

	// returns the number of accept states in DFA
	int getAcceptStatesNum() {
		return this.acceptStates.size();
	}

	// returns the number of none accept states in DFA
	int getNoneAcceptStatesNum() {
		return this.getNoneAcceptStates().size();
	}

	// returns states in DFA
	ArrayList<String> getStates() {
		ArrayList<String> allStates = new ArrayList<String>();
		for (Map.Entry<String, HashMap<String, String>> i : this.getTransitions().entrySet()) { // for each state
			allStates.add(i.getKey());
		}
		return allStates;
	}

	// returns the accept states in the DFA
	ArrayList<String> getAcceptStates() {
		return this.acceptStates;
	}

	// returns the none accept states in the DFA
	ArrayList<String> getNoneAcceptStates() {
		ArrayList<String> noneAcceptStates = new ArrayList<String>();
		for (int i = 0; i < this.getStatesNum(); i++) {
			if (this.getAcceptStates().contains(this.getState(i)) != true) {
				noneAcceptStates.add(this.getState(i));
			}
		}
		return noneAcceptStates;
	}

	// returns true if given state is an accept state
	boolean isAccept(String state) {
		if (this.getAcceptStates().contains(state)) {
			return true;
		} else {
			return false;
		}
	}

	// returns alphabet in DFA
	int getAlphabetSize() {
		return alphabet.size();
	}

	// returns alphabet in DFA
	ArrayList<String> getAlphabet() {
		return this.alphabet;
	}

	// returns the start state in the DFA
	String getStartState() {
		return this.startState;
	}

	void setAcceptStates(ArrayList<String> newStates) {
		this.acceptStates = newStates;
	}

	// returns transitions in the DFA
	HashMap<String, HashMap<String, String>> getTransitions() {
		return this.transitions;
	}

	// returns a state based on index from a number
	String getState(int number) {
		ArrayList<String> states = this.getStates();
		return states.get(number);
	}

	// changes the current DFA to the complement of the DFA
	void setComplement() {
		ArrayList<String> newAcceptStates = new ArrayList<String>();
		for(int i = 0; i < this.getStatesNum(); i++) {
			if (this.getAcceptStates().contains(this.getStates().get(i)) != true) { // if no match found add to new array
				newAcceptStates.add(this.getStates().get(i));
			}
		}
		this.setAcceptStates(newAcceptStates);
	}

	//returns a DFA which is the intersection of dfa1 and dfa2
	DFA intersection(DFA dfa1) {
		ArrayList<String> newAlphabet;
		HashMap<String, HashMap<String, String>> newTransitions = new HashMap<String, HashMap<String, String>>();
		String newStartState;
		ArrayList<String> newAcceptStates;

		// set the start state of the intersection of 2 given DFA's
		newStartState = this.getStartState() + dfa1.getStartState();

		// Get the intersection of the alphabet for this and dfa1
		newAlphabet = new ArrayList<String>(this.getAlphabet());
		newAlphabet.retainAll(dfa1.getAlphabet());

		// Get all none accept states for the intersection of dfa1 and dfa2
		newAcceptStates = new ArrayList<String>();
		for (int i = 0; i < this.getAcceptStates().size(); i++) {
			for (int j = 0; j < dfa1.getAcceptStates().size(); j++) {
				newAcceptStates.add(this.getAcceptStates().get(i) + dfa1.getAcceptStates().get(j));
			}
		}

		// Get new transactions for each new state
		for (Map.Entry<String, HashMap<String, String>> i : this.getTransitions().entrySet()) {
			HashMap<String, String> transition = new HashMap<String, String>();
			HashMap<String, String> thisValues = new HashMap<String, String>(i.getValue());
			String thisKey = new String(i.getKey());
			for (Map.Entry<String, HashMap<String, String>> j : dfa1.getTransitions().entrySet()) {
				HashMap<String, String> dfa1Values = new HashMap<String, String>(j.getValue());
				String dfa1Key = new String(j.getKey());

				for (int k = 0; k < newAlphabet.size(); k++) {
					transition.put(newAlphabet.get(k), thisValues.get(newAlphabet.get(k)) + dfa1Values.get(newAlphabet.get(k)));
				}
				newTransitions.put(thisKey + dfa1Key, new HashMap<String, String>(transition));
			}
		}
		return new DFA(newAlphabet, newTransitions, newStartState, newAcceptStates);
	}

	//returns a DFA which is the symmetric difference of 2 given DFA's
	DFA SymmetricDifference(DFA dfa1) {
		DFA notDFA1 = new DFA(dfa1);
		notDFA1.setComplement();
		DFA notDFAthis = new DFA(this);
		notDFAthis.setComplement();
		DFA newDFA1 = this.intersection(notDFA1);
		DFA newDFA2 = notDFAthis.intersection(dfa1);

		// Gets the accept states in this and dfa1 such that only the accept states in one DFA or the other are accepted
		ArrayList<String> newAcceptStates = new ArrayList<String>();
		newAcceptStates.addAll(newDFA1.getAcceptStates());
		newAcceptStates.addAll(newDFA2.getAcceptStates());
		newDFA1.setAcceptStates(newAcceptStates);
		return newDFA1;
	}



	String acceptString() {
		Stack<ArrayList<String>> toVisit = new Stack<ArrayList<String>>(); // States still unvisited
		ArrayList<String> visitedStates = new ArrayList<String>();
		ArrayList<String> accepted = new ArrayList<String>(); // elements in a an accepted string (if exists)

		// Depth first search
		ArrayList<String> newNode = new ArrayList<String>();
		newNode.add(this.getStartState());
		newNode.add("");
		toVisit.push(newNode); // add start state to stack
		while (toVisit.empty() != true) {
			ArrayList<String> currentNodeComplete = toVisit.pop();
			String currentNode = new String(currentNodeComplete.get(0)); // node to visit
			String currentTransition = new String(currentNodeComplete.get(1)); //symbol used to get to node
			if (visitedStates.contains(currentNode) != true) { // if state not visited before
				visitedStates.add(currentNode);
				accepted.add(currentTransition);
				if (this.getAcceptStates().contains(currentNode)) { // At an accept state
					if (accepted.size() == 1) { // if accept string is empty
						accepted.add("e");
					}
					return "language non-empty - " + String.join("", accepted);
				}
				for (int i = 0; i < this.getAlphabetSize(); i++) { // Set next states to visit
					ArrayList<String> nextNode = new ArrayList<String>();
					nextNode.add(this.getTransitions().get(currentNode).get(this.getAlphabet().get(i)));
					nextNode.add(this.getAlphabet().get(i));
					toVisit.push(nextNode);
				}
			}
		}
		return "language empty";
	}

	boolean isEquivalent(DFA dfa1) {
		Stack<ArrayList<String>> toVisit = new Stack<ArrayList<String>>(); // States still unvisited
		ArrayList<ArrayList<String>> visitedStates = new ArrayList<ArrayList<String>>();

		// Depth first search
		ArrayList<String> currentNodes = new ArrayList<String>();
		currentNodes.add(this.getStartState());
		currentNodes.add(dfa1.getStartState());
		toVisit.push(currentNodes); // add start states to stack
		while (toVisit.empty() != true) {
			currentNodes = toVisit.pop();
			if (visitedStates.contains(currentNodes) != true) { // if states not visited before
				visitedStates.add(currentNodes);
				if ((this.isAccept(currentNodes.get(0))) == (dfa1.isAccept(currentNodes.get(1))) != true) { // if states not the same return false
					return false;
				}
				for (int i = 0; i < this.getAlphabetSize(); i++) { // Set next states to visit
					ArrayList<String> nextNodes = new ArrayList<String>();
					nextNodes.add(this.getTransitions().get(currentNodes.get(0)).get(this.getAlphabet().get(i)));
					nextNodes.add(dfa1.getTransitions().get(currentNodes.get(1)).get(this.getAlphabet().get(i)));
					toVisit.push(nextNodes);
				}
			}
		}
		return true;
	}

	//Returns a string of the encoding of a given DFA
	String getEncoding() {
		return (String.join(" ", this.getStates()) + ", " + String.join(" ", this.getAlphabet()) +
			", " + this.getTransitions() + ", " + this.getStartState() + ", " +
			String.join(" ", this.getAcceptStates()));
	}

	// returns DFA as a string
	public String toString() {

		// converst transitions to a string
		String transitionString = "";
		boolean isFirst = true;
		for (Map.Entry<String, HashMap<String, String>> i : this.getTransitions().entrySet()) { // for each state
			List<String> nodeTransition = new ArrayList<String>();
			for (Map.Entry j : i.getValue().entrySet()) { // for each transition
				nodeTransition.add(j.getValue().toString());
			}
			if(!isFirst) {
				transitionString += System.lineSeparator();
			} else {
				isFirst = false;
			}
			transitionString += String.join(" ", nodeTransition);
		}

		return (this.getStatesNum() + System.lineSeparator() +
			String.join(" ", this.getStates()) + System.lineSeparator() +
			this.getAlphabetSize() + System.lineSeparator() +
			String.join(" ", this.getAlphabet()) + System.lineSeparator() +
			transitionString + System.lineSeparator() +
			this.getStartState() + System.lineSeparator() +
			this.getAcceptStatesNum() + System.lineSeparator() +
			String.join(" ", this.getAcceptStates()));
	}
}
