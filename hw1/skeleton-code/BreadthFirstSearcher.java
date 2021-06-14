import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		
		Square startsqr = maze.getPlayerSquare();
		State start = new State(startsqr, null, 0, 0);
		queue.add(start);
		
		//System.out.println("Welcome to BFS, stat state added");
		
		while (!queue.isEmpty()) {
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
			State curr = queue.pop();
			noOfNodesExpanded++;//?
			maxDepthSearched = curr.getDepth() > maxDepthSearched ? curr.getDepth():maxDepthSearched;//?
			explored[curr.getX()][curr.getY()] = true;
			if(curr.isGoal(maze)) {
				while(curr.getParent().getParent()!=null) {
					maze.setOneSquare(curr.getParent().getSquare(), '.');
					curr = curr.getParent();
					cost++;
				}
				cost++;
				return true;
			}
			ArrayList<State> successors = curr.getSuccessors(explored, maze);
			for(int i = 0; i < successors.size(); i++){
				boolean add = true;
				State succ = successors.get(i);
				for(State s: queue) {
					if(succ.equals(s)) {
						add = false;
						break;
					}
				}
				if(add) {
					queue.add(succ);
				}
			}
			maxSizeOfFrontier = queue.size() > maxSizeOfFrontier ? queue.size():maxSizeOfFrontier;//?
		}

		// TODO return false if no solution
		return false;
	}
}
