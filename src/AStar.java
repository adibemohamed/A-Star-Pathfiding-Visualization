import java.util.*;

import javax.swing.JFrame;

public class AStar {
	private ArrayList<Node> openSet, closeSet, path;
	private Node startNode, endNode, currentNode;
	private Node grid[][];
	private JFrame frame;

	public AStar(JFrame frame) {
		this.openSet = new ArrayList<Node>();
		this.closeSet = new ArrayList<Node>();
		this.path = new ArrayList<Node>();
		this.frame = frame;
	}

	public void setStart(int Sx, int Sy) {
		this.startNode = grid[Sy][Sx];
	}

	public void setEnd(int Ex, int Ey) {
		this.endNode = grid[Ey][Ex];
		displayInfo();
	}

	public void setGrid(Node grid[][]) {
		this.grid = grid;
	}

	public ArrayList<Node> getPath() {
		return path;
	}

	public ArrayList<Node> getOpenSet() {
		return openSet;
	}

	public ArrayList<Node> getCloseSet() {
		return closeSet;
	}

	private void displayInfo() {
		System.out.println("Grid: h: " + grid.length);
		System.out.println("      w: " + grid[0].length);
		System.out.println("Start: x: " + startNode.getxPos());
		System.out.println("       y: " + startNode.getyPos());
		System.out.println("End: x: " + endNode.getxPos());
		System.out.println("     y: " + endNode.getyPos());
	}

	public void findPath() {

		// displayInfo();

		openSet.add(startNode);

		while (!openSet.isEmpty()) {

			currentNode = getLowest();
			openSet.remove(currentNode);
			closeSet.add(currentNode);

			// path found to the end node
			if (currentNode.getxPos() == endNode.getxPos() && currentNode.getyPos() == endNode.getyPos()) {
				System.out.println("Path found !");
				retracePath();
				return;
			}

//			if (currentNode.getParent() != null)
//				System.out.println("Current[" + currentNode.getxPos() + "," + currentNode.getyPos() + "]" + "->Parent ["
//						+ currentNode.getParent().getxPos() + "," + currentNode.getParent().getyPos() + "]");

			currentNode.setNeighbours(this.getNeighours(currentNode));

			for (Node neighbour : currentNode.getNeighbours()) {
				

				if (closeSet.contains(neighbour) || !neighbour.isWalkable)
					continue;

				int newMvtCostToNeighbour = currentNode.getfCost() + getDistance(currentNode, neighbour);

				if (newMvtCostToNeighbour < neighbour.getfCost() || !openSet.contains(neighbour)) {
					neighbour.setgCost(newMvtCostToNeighbour);
					neighbour.sethCost(getDistance(neighbour, endNode));
					neighbour.setParent(currentNode);
					if (!openSet.contains(neighbour))
						openSet.add(neighbour);
				}
			}

			 
		}
	}

	// retrace the path from end to start
	private void retracePath() {
		path = new ArrayList<Node>();
		Node tempNode = endNode;
		while (tempNode.getParent() != null) {
			path.add(tempNode);
			tempNode = tempNode.getParent();
		}

		path.add(startNode);
		// reverse the path;

		for (int i = 0; i < path.size(); i++) {

		}

	}

	// calc distance between two nodes
	private int getDistance(Node A, Node B) {
		int distX = Math.abs(A.getxPos() - B.getxPos());
		int distY = Math.abs(A.getyPos() - B.getyPos());
		if (distX > distY)
			return 14 * distY + 10 * (distX - distY);
		return 14 * distX + 10 * (distY - distX);
	}

	// find the lowest fcost node
	private Node getLowest() {
		Node lowest = this.openSet.get(0);
		for (Node node : this.openSet) {
			if (node.getfCost() < lowest.getfCost()
					|| node.getfCost() == lowest.getfCost() && node.gethCost() < lowest.gethCost())
				lowest = node;
		}
		return lowest;
	}

	// find neighbours of a current node
	private ArrayList<Node> getNeighours(Node current) {
		ArrayList<Node> neighbours = new ArrayList<Node>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int tempX = current.getxPos() + i;
				int tempY = current.getyPos() + j;
				if (i == 0 && j == 0)
					continue;
				if (tempX >= 0 && tempX < grid[0].length && tempY > 0 && tempY < grid.length)
					neighbours.add(grid[tempY][tempX]);
			}
		}
		return neighbours;
	}
}