/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/

package rushhour;

import search.Action;
import search.State;

public class Move implements Action {

	private int carIndex;
	private int spaces;
	private String direction;

	public Move (int carIndex, int spaces, String direction) {
		this.carIndex=carIndex;
		this.spaces=spaces;
		this.direction=direction;
	}

	public int getCost() {
		return 1;
	}

	public String toString(){
		return "move ";
	}

	public int getCar() {
		return this.carIndex;
	}

	public int getSpaces() {
		return this.spaces;
	}

	public boolean isRight() {
		return this.direction == "right";
	}

	public boolean isLeft() {
		return this.direction == "left";
	}

	public boolean isUp() {
		return this.direction == "up";
	}

	public boolean isDown() {
		return this.direction == "down";
	}

}
