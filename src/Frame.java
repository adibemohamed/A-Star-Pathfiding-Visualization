import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Frame extends JPanel implements MouseListener, KeyListener, MouseMotionListener, MouseWheelListener {

	JFrame frame;
	Dimension frameSize;
	final int HEIGHT = 750, WIDTH = 750;
	int height, width;
	int gDimension = 30;
	private ArrayList<Node> openNodes, closeNodes, path;
	int grid[][];
	Node startNode, endNode;
	char currentKey = ' ';
	Dimension screenSize;
	int bordersValue = 1, startValue = 76987, endValue = 07450, backValue = -1;

	// APathFinding pathFinding;
	Node gridNodes[][];
	AStar astar;
	boolean isPath = false, isRunning = false;

	public Frame() {
		initComponenet();
		setLayout();

	}

	// find path
	void findPath() {
		astar = new AStar(this.frame);
		setGridNodes();
		astar.setGrid(gridNodes);
		if (startNode.isExist)
			astar.setStart(startNode.getxPos(), startNode.getyPos());
		if (startNode.isExist)
			astar.setEnd(endNode.getxPos(), endNode.getyPos());
		if (startNode.isExist && endNode.isExist) {
			astar.findPath();
			path = astar.getPath();
			isRunning = true;
		} else {
			System.out.println("start or end node is missing!");
		}

	}

	public void update() {
		openNodes = astar.getOpenSet();
		closeNodes = astar.getCloseSet();
	}

	// convert graphic grid to list of nodes
	public void setGridNodes() {
		gridNodes = new Node[height / gDimension + 1][width / gDimension + 1];
		Node node;
		// System.out.println(height/gDimension+"/"+width/gDimension);
		for (int i = 0; i < height; i += gDimension) {
			for (int j = 0; j < width; j += gDimension) {
				int yPos = i / gDimension;
				int xPos = j / gDimension;
				node = new Node();
				node.setxPos(xPos);
				node.setyPos(yPos);

				if (grid[yPos][xPos] == bordersValue) {
					node.isWalkable = false;

				} else {
					node.isWalkable = true;
				}
				gridNodes[yPos][xPos] = node;
			}
		}
	}

	// initialize component
	public void initComponenet() {

		frame = new JFrame();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		width = (int) screenSize.getWidth();
		height = (int) screenSize.getHeight();
		frameSize = new Dimension(WIDTH, HEIGHT);
		// grid = new int[(int) screenSize.getHeight()][(int) screenSize.getWidth()];
		grid = new int[height][width];
		startNode = new Node();
		endNode = new Node();

	}

	// setLayout
	public void setLayout() {

		frame.setSize(frameSize);
		frame.setTitle("A*Pathfinding Visualisation");
		frame.setLocation(width / 2 - WIDTH / 2, height / 2 - HEIGHT / 2);
		frame.setFocusable(true);
		frame.setEnabled(true);
		frame.addMouseListener(this);
		frame.addMouseMotionListener(this);
		frame.addKeyListener(this);
		frame.addMouseWheelListener(this);
		frame.setResizable(true);
		frame.add(this);
		frame.setVisible(true);
	}

	// get approximate value of position
	public void setMousePos(int mouseX, int mouseY, int mouseBtn) {
		mouseX -= gDimension;
		mouseY -= gDimension;

		// draw borders
		if (currentKey == ' ' && mouseBtn == 1) {
			grid[mouseY / gDimension][mouseX / gDimension] = bordersValue;
		}
		// remove borders
		else if (currentKey == ' ' && mouseBtn == 3) {
			// remove startNodenode
			if (mouseX / gDimension == startNode.getxPos() && mouseY / gDimension == startNode.getyPos())
				startNode.isExist = false;
			// remove endNodenode
			if (mouseX / gDimension == endNode.getxPos() && mouseY / gDimension == endNode.getyPos())
				endNode.isExist = false;
			// remove borders
			grid[mouseY / gDimension][mouseX / gDimension] = backValue;
		}
		// add startNodenode
		else if (currentKey == 's' && mouseBtn == 1) {
			startNode.setxPos(mouseX / gDimension);
			startNode.setyPos(mouseY / gDimension);
			startNode.isExist = true;
			startNode.isWalkable = false;
		}
		// add endNodenode
		else if (currentKey == 'e' && mouseBtn == 1) {
			endNode.setxPos(mouseX / gDimension);
			endNode.setyPos(mouseY / gDimension);
			endNode.isExist = true;
			endNode.isWalkable = true;
		}

		repaint();

	}

	// get pressed key
	public void keyHandler(KeyEvent e) {
		// set startNodenode
		if (e.getKeyCode() == KeyEvent.VK_S) {
			currentKey = 's';

		}
		// set the endNodenode
		else if (e.getKeyCode() == KeyEvent.VK_E) {
			currentKey = 'e';
		}
		// reset settings
		else if (e.getKeyCode() == KeyEvent.VK_R) {
			gDimension = 20;
		}
		// clear canvas
		else if (e.getKeyCode() == KeyEvent.VK_C) {
			initComponenet();
		}
		// find the path
		else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			findPath();
		}
		// find the path
		else if (e.getKeyCode() == KeyEvent.VK_P) {
			isPath = true;
		}

		repaint();
	}

	// mouse event
	void gridHandler(MouseEvent e) {
		// add borders nodes
		if (e.getX() >= 0 && e.getY() >= 0) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				setMousePos(e.getX(), e.getY(), 1);
				// System.out.println("Left | x: " + e.getX() + " y:" + e.getY());
			}
			// remove nodes from grid
			if (SwingUtilities.isRightMouseButton(e)) {
				setMousePos(e.getX(), e.getY(), 3);
				// System.out.println("Right | x: " + e.getX() + " y:" + e.getY());

			}
		}

	}

	// canvas draw method
	public void paint(Graphics g) {
		if (isRunning)
			update();
		// background
		g.setColor(new Color(103, 58, 183));
		g.fillRect(0, 0, width, height);

		// draw borders
		for (int i = 0; i < height / gDimension; i++)
			for (int j = 0; j < width / gDimension; j++) {
				// draw borders
				if (grid[i][j] == bordersValue) {
					g.setColor(new Color(26, 26, 26));
					g.fillRect(j * gDimension, i * gDimension, gDimension, gDimension);
				}

			}

		// draw open node
		if (isRunning)
			for (Node node : openNodes) {
				g.setColor(Color.pink);
				g.fillRect(node.getxPos() * gDimension, node.getyPos() * gDimension, gDimension, gDimension);

			}
		// draw close
		if (isRunning)
			for (Node node : closeNodes) {
				g.setColor(Color.lightGray);
				g.fillRect(node.getxPos() * gDimension, node.getyPos() * gDimension, gDimension, gDimension);

			}
		// draw close node
		if (isPath)
			for (Node node : path) {
				g.setColor(Color.red);
				g.fillRect(node.getxPos() * gDimension, node.getyPos() * gDimension, gDimension, gDimension);

			}

		// draw grid lines
		for (int i = 0; i < width; i += gDimension) {
			// lines
			g.setColor(new Color(158, 158, 158));
			g.drawLine(0, i, width, i);
			g.drawLine(i, 0, i, height);
		}

		// draw start node
		if (startNode.isExist) {
			g.setColor(Color.blue);
			g.fillRect(startNode.getxPos() * gDimension, startNode.getyPos() * gDimension, gDimension, gDimension);
		}
		// draw end node
		if (endNode.isExist) {
			g.setColor(Color.orange);
			g.fillRect(endNode.getxPos() * gDimension, endNode.getyPos() * gDimension, gDimension, gDimension);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		gridHandler(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		keyHandler(e);
		// System.out.println("current key: " + currentKey);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		currentKey = ' ';
		// System.out.println("current key: " + currentKey);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		gridHandler(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int rotation = e.getWheelRotation();
		// zoom
		if (gDimension + rotation * 2 > 10 && gDimension + rotation * 2 < 120)
			gDimension += rotation * 3;
		repaint();
	}

	public static void main(String args[]) {
		new Frame();
	}
}