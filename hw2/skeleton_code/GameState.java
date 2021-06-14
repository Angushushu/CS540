import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;            // The number of stones
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;

        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[this.size + 1];
        this.stones[0] = false;

        // Set default state of stones to available
        for (int i = 1; i <= this.size; ++i) {
            this.stones[i] = true;
        }

        // Set the last move be -1
        this.lastMove = -1;
    }

    /**
     * Copy constructor
     */
    public GameState(GameState other) {
        this.size = other.size;
        this.stones = Arrays.copyOf(other.stones, other.stones.length);
        this.lastMove = other.lastMove;
    }


    /**
     * This method is used to compute a list of legal moves
     *
     * @return This is the list of state's moves
     */
    public List<Integer> getMoves() {
        // TODO Add your code here
        List<Integer> legalmoves = new ArrayList<Integer>();
        // if first move
        if(lastMove == -1) {
            for(int i = 1; i < (double)this.size/2; i+=2) {
                legalmoves.add(i);
            }
        } else {
            // if not first move
            for(int i = 1; i <= this.size; i++) {
                if((i%lastMove == 0 || lastMove%i == 0) && this.stones[i]) {
                    legalmoves.add(i);
                }
            }
        }
        return legalmoves;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
     *
     * @return This is the list of state's successors
     */
    public List<GameState> getSuccessors() {
        return this.getMoves().stream().map(move -> {
            var state = new GameState(this);
            state.removeStone(move);
            return state;
        }).collect(Collectors.toList());
    }


    /**
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() {
        // TODO Add your code here
        double retval;
        List<GameState> successors = this.getSuccessors();

        int cnt_mv = 0;
        for(int i = 1; i <= size; i++) {
            if(stones[i] == false) {
                cnt_mv++;
            }
        }
        //for player 1
        if(successors.isEmpty() && cnt_mv%2 == 1) {  // if maxplayer win(player 1 w/o options)
            return 1;
        } else if(successors.isEmpty() && cnt_mv%2 == 0) {  // if min win
            return -1;
        } else {
            int possiblesucc = this.getMoves().size();
            if(this.stones[1] == true) {
                retval = 0;
            } else if(lastMove == 1) {
                if(possiblesucc%2 == 1) {
                    retval = 0.5;
                } else {
                    retval = -0.5;
                }
            } else if(Helper.isPrime(lastMove)) {
                
            	var moves = getMoves();
            	int count_of_mult = 0;
            	for(int i:moves) {
            		if(i%lastMove == 0) {
            			count_of_mult++;
            		}
            	}
            	if(count_of_mult%2 == 1) {
                    retval = 0.7;
                } else {
                    retval = -0.7;
                }
            	//
            	/*int count_of_mult = 0;
                
                for(int i = 1; i <= size; i++){
                    if(this.stones[i] == true && (int)this.getStone(i)%lastMove == 0) {
                        count_of_mult++;
                    }
                }
                if(count_of_mult%2 == 1) {
                    retval = 0.7;
                } else {
                    retval = -0.7;
                }*/
            } else {
            	var moves = getMoves();
            	int count = 0;
            	int lpf = Helper.getLargestPrimeFactor(lastMove);
            	for(int i:moves) {
            		if(i%lpf == 0 && /*Helper.isPrime(i) &&*/ stones[i]) {
            			count++;
            		}
            	}
                if(count%2 == 1) {
                    retval = 0.6;
                } else {
                    retval = -0.6;
                }
            	
            	
            	//
                /*int count = 0;
                int lpf = Helper.getLargestPrimeFactor(lastMove);
                for(int i = lpf; i < size; i++){
                    if(i%lpf == 0 && Helper.isPrime(i) && stones[i]) {
                        count++;
                    }
                }
                if(count%2 == 1) {
                    retval = 0.6;
                } else {
                    retval = -0.6;
                }*/
            }
            if(cnt_mv%2 == 1) {  // if player 2
                retval = -1*retval;
            }
            return retval;
        }
        
    }

    /**
     * This method is used to take a stone out
     *
     * @param idx Index of the taken stone
     */
    public void removeStone(int idx) {
        this.stones[idx] = false;
        this.lastMove = idx;
    }

    /**
     * These are get/set methods for a stone
     *
     * @param idx Index of the taken stone
     */
    public void setStone(int idx) {
        this.stones[idx] = true;
    }

    public boolean getStone(int idx) {
        return this.stones[idx];
    }

    /**
     * These are get/set methods for lastMove variable
     *
     * @param move Index of the taken stone
     */
    public void setLastMove(int move) {
        this.lastMove = move;
    }

    public int getLastMove() {
        return this.lastMove;
    }

    /**
     * This is get method for game size
     *
     * @return int the number of stones
     */
    public int getSize() {
        return this.size;
    }

}	
