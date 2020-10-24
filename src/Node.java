import java.sql.Array;
import java.util.ArrayList;

public class Node {
	private int xPos, yPos, gCost, hCost, fCost;
	public boolean isExist = false, isWalkable = false;
	private Node parent = null;
	private ArrayList<Node> neighbours;

	public Node() {

	}

	public int getxPos() {
		return xPos;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getgCost() {
		return gCost;
	}

	public void setgCost(int gCost) {
		this.gCost = gCost;
	}

	public int gethCost() {
		return hCost;
	}

	public void sethCost(int hCost) {
		this.hCost = hCost;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public void setfCost(int fCost) {
		this.fCost = fCost;
	}

	public int getfCost() {
		return hCost + gCost;
	}

	public void setNeighbours(ArrayList<Node> neighbours) {
		this.neighbours = neighbours;
	}

	public ArrayList<Node> getNeighbours() {
		return neighbours;
	}
}
