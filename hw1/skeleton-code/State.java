import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// FILL THIS METHOD

		// TODO check all four neighbors in left, down, right, up order
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		ArrayList<State> successors = new ArrayList<State>();
		if(explored[getX()][getY()-1]!=true && maze.getSquareValue(getX(),getY()-1)!='%'){//L
			successors.add(new State(new Square(this.getX(),this.getY()-1), this, gValue+1, depth+1));
		}
		if(explored[getX()+1][getY()]!=true && maze.getSquareValue(getX()+1,getY())!='%'){//D
			successors.add(new State(new Square(getX()+1,getY()), this, gValue+1, depth+1));
		}
		if(explored[getX()][getY()+1]!=true && maze.getSquareValue(getX(),getY()+1)!='%'){//R
			successors.add(new State(new Square(getX(),getY()+1), this, gValue+1, depth+1));
		}
		if(explored[getX()-1][getY()]!=true && maze.getSquareValue(getX()-1,getY())!='%'){//U
			successors.add(new State(new Square(getX()-1,getY()), this, gValue+1, depth+1));
		}
		return successors;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}
	
	/**
	 * @return boolean of whether equal
	 */
	public boolean equals(State that) {
		Square sq1 = this.getSquare();
		Square sq2 = that.getSquare();
		if(sq1.X==sq2.X&&sq1.Y==sq2.Y&&this.getDepth()==that.getDepth()&&this.getGValue()==that.getGValue()) {
			return true;
		}
		return false;
	}
}
