import java.util.List;

public class AlphaBetaPruning {
    int move;
    double value;
    int nov;
    int noe;
    int maxpossibledepth;
    int maxdepth;
    double avgbranchfactor;
    static int stepstarted;

    public AlphaBetaPruning() {
        move = 0;
        value = 0;
        nov = 0;
        noe = 0;
        maxpossibledepth = 0;
        maxdepth = 0;
        avgbranchfactor = 0;
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        // TODO Add your code here
        System.out.println("Move: "+move);
        System.out.printf("Value: %.1f %n", value);
        System.out.println("Number of Nodes Visited: "+nov);
        System.out.println("Number of Nodes Evaluated: "+noe);
        System.out.println("Max Depth Reached: "+maxdepth);
        System.out.printf("Avg Effective Branching Factor: %.1f %n", avgbranchfactor);
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
    	nov++;  // increase nov
    	maxpossibledepth = depth;
    	int cnt_mv = 0;
    	if(state.getLastMove()!=-1) {  // check when is max
    		for(int i = 1; i <= state.getSize(); i++) {
                if(state.getStone(i) == false) {
                    cnt_mv++;
                }
            }
    	}
    	//stepstarted = cnt_mv;
    	int opti = 0;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        boolean maxplayer;
        if(cnt_mv%2 == 0) {
        	maxplayer = true;
        } else {
        	maxplayer = false;
        }
    	
        List<GameState> successors = state.getSuccessors();
        if(depth == 0 || successors.isEmpty()) {
        	noe++;  // increase noe
            value = state.evaluate();
        }
        if(maxplayer) {
            for(GameState succ: successors) {
                double v = alphabeta(succ, depth-1, alpha, beta, !maxplayer);
            	if(v > alpha) {
            		alpha = v;
            		opti = succ.getLastMove();
            	}else if(v == alpha) { // for tie case
            		if(succ.getLastMove() < opti) {
            			opti = succ.getLastMove();
            		}
            	}
            }
            value = alpha;
        } else {
            for(GameState succ: successors) {
                double v = alphabeta(succ, depth-1, alpha, beta, !maxplayer);
                if(v < beta) {
                	beta = v;
                	opti = succ.getLastMove();
                }else if(v == beta) { // for tie case
            		if(succ.getLastMove() < opti) {
            			opti = succ.getLastMove();
            		}
            	}
            }
            value = beta;
        }
        move = opti;
        avgbranchfactor = (double)(nov-1)/(nov-noe);
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the cursrent game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
        // TODO Add your code here
    	int smallv;
    	if(maxpossibledepth - depth > maxdepth) {
    		maxdepth = maxpossibledepth - depth;
    	}
    	nov++;  // increase nov
        List<GameState> successors = state.getSuccessors();
        if(depth == 0 || successors.isEmpty()) {
        	noe++;  // increase noe
            return state.evaluate();
        }
        if(maxPlayer) {
        	double v =  Double.NEGATIVE_INFINITY;
            for(GameState succ: successors) {
            	v = Math.max(v, alphabeta(succ, depth-1, alpha, beta, !maxPlayer));
                //double v = alphabeta(succ, depth-1, alpha, beta, !maxPlayer);
            	//v = Math.max(v, alpha);//Math.min(alpha, beta));
            	if(v >= beta) {
            		return v;
            	}
            	//return v;
            	alpha = Math.max(alpha, v);
            }
            return alpha;
        } else {
        	double v =  Double.POSITIVE_INFINITY;
            for(GameState succ: successors) {
            	v = Math.min(v, alphabeta(succ, depth-1, alpha, beta, !maxPlayer));
                //double v = alphabeta(succ, depth-1, alpha, beta, !maxPlayer);
                //v = Math.min(v, beta);//Math.max(alpha, beta));
                if(v <= alpha) {
                	return v;
                }
                //return v;
                beta = Math.min(beta, v);
            }
            return beta;// return v
        }
    }
}
