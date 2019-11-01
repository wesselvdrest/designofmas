import java.util.ArrayList;
import java.util.Random;

public class SolverGreedy extends GameSolver {

    @Override
    public Edge getNextMove(final Board board, int color) {
		int score = board.getScore(color);
        ArrayList<Edge> moves = board.getAvailableMoves();
        ArrayList<Edge> bMoves = new ArrayList<Edge>(); //Empty array to save best moves into
        for(Edge move : moves) { //Find move that increases the score the most in the next round
        	Board newBoard = board.getNewBoard(move, color);
        	if(newBoard.getScore(color) > score)
        		bMoves.add(move);
				score = newBoard.getScore(color);
        }
        if(!bMoves.isEmpty()) 
			return bMoves.get(bMoves.size()-1); //If there are moves that improve the score, select the highest score (by definition the latest added move)
        return moves.get(new Random().nextInt(moves.size())); //Otherwise, do a random move
    }

}