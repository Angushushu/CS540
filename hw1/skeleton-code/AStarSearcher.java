import java.util.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.lang.Math;


/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		Square initialSqr = maze.getPlayerSquare();
		Square goalSqr = maze.getGoalSquare();
		State firstSqr = new State(initialSqr, null, 0, 0);
		double fvalue_distance = Math.sqrt(Math.pow(initialSqr.X - goalSqr.X , 2) + Math.pow(initialSqr.Y - goalSqr.Y , 2));
		StateFValuePair fisrt = new StateFValuePair(firstSqr, fvalue_distance);
		frontier.add(fisrt);
		
		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found

			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
			System.out.println("initialize staffs");
			StateFValuePair expandedNode_SF  = frontier.poll();
			noOfNodesExpanded++;
			State expandedNode_state = expandedNode_SF.getState();
			explored[expandedNode_state.getX()][expandedNode_state.getY()] = true;
			if(maxDepthSearched <= expandedNode_state.getDepth())
				maxDepthSearched = expandedNode_state.getDepth();
			if(expandedNode_state.isGoal(maze))
			{
				System.out.println("goal found");
				// here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				//char ch = maze.getSquareValue(expandedNode_state.getParent().getParent().getX(),expandedNode_state.getParent().getParent().getY());
				while(maze.getSquareValue(expandedNode_state.getParent().getParent().getX(),expandedNode_state.getParent().getParent().getY())!='S')//maze.getSquareValue(expandedNode_state.getParent().getX(), expandedNode_state.getParent().getY()) != 'S'
				{

					cost++;
					maze.setOneSquare(expandedNode_state.getParent().getSquare(), '.');
					expandedNode_state = expandedNode_state.getParent();

				}
				/*while(curr_state.getParent().getParent()!=null) {
					//System.out.println("- - Adding .");
					maze.setOneSquare(curr_state.getParent().getSquare(), '.');
					curr_state = curr_state.getParent();
					cost++;
				}*/
				cost++;
				return true;
			}
			//here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			explored[expandedNode_state.getX()][expandedNode_state.getY()] = true;
			if(maxDepthSearched <= expandedNode_state.getDepth() + 1)
				maxDepthSearched = expandedNode_state.getDepth() + 1;

			
			
			ArrayList<State> succs = expandedNode_state.getSuccessors(explored, maze);
			for(int i = 0; i < succs.size(); i++)
			{
				System.out.println("getting successors");
				boolean check = true;
				State st = succs.get(i);
				double sFvalue = st.getGValue() + Math.sqrt(Math.pow(st.getX() - goalSqr.X , 2) + Math.pow(st.getY() - goalSqr.Y , 2));
				
					//here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					for(StateFValuePair sp:frontier)
					{
						//Object[] arr = frontier.toArray();
						//State s = arr[j].getState();
						State s = sp.getState();
						if(st.getX() == s.getX() &&  st.getY() == s.getY())
						{
							if(st.getGValue() < s.getGValue()){
								StateFValuePair stp = new StateFValuePair(st, sFvalue);
								frontier.add(stp);
								frontier.remove(s);
								System.out.println("added a new one");
								check = false;
							}
							//check = false;
							//break;
						}
					}
					if(check){
						StateFValuePair stp = new StateFValuePair(st, sFvalue);
						frontier.add(stp);
					}
				
					/*for(StateFValuePair sf:frontier)
					{
						State sf_state = sf.getState();
						if(st.getX()==sf_state.getX()&&st.getY()==sf_state.getY()&&sFvalue>=sf.getFValue()) 
						{
							check = false;
							break;
						}
					}*/
				/*if(check)
				{
					StateFValuePair addSF = new StateFValuePair(st, sFvalue);
					frontier.add(addSF);
				}*/
			}
			if(maxSizeOfFrontier <= frontier.size())
				maxSizeOfFrontier = frontier.size();
			
		}

		// TODO return false if no solution
		return false;
	}

}
