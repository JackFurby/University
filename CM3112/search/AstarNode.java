/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package search;

import java.util.*;

/**
*
* @author steven
*/
public class AstarNode extends Node implements Comparable<AstarNode>{

	public AstarNode(State st, Node previousNode, Action lastAction) {
		super(st,previousNode,lastAction);
	}

	public int compareTo(AstarNode n){
		int score = cost + st.getEstimatedDistanceToGoal();
		int scoreN = n.cost + n.st.getEstimatedDistanceToGoal();
		//st.printBoard();
		//System.out.println(cost);
		//System.out.println(st.getEstimatedDistanceToGoal());
		//System.out.println(score - scoreN);
		//System.out.println("=============");
		return score - scoreN;
	}

}
