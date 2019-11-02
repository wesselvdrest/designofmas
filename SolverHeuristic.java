import java.util.ArrayList;
import java.util.Collections;

public class SolverHeuristic extends GameSolver {

    @Override
    public Edge getNextMove(final Board board, int color) {
        referenceColor = color;
        ArrayList<Edge> moves = board.getAvailableMoves();
        Collections.shuffle(moves);
        int moveCount = moves.size();
        int value[] = new int[moveCount];
        
        for(int i=0;i<moveCount;i++) { //get the heuristic value for each move, depending on whether you can take a box with that move
            Board nextBoard = board.getNewBoard(moves.get(i), color);
        	value[i] = heuristic(nextBoard, (nextBoard.getScore(color) > board.getScore(color) ? color : Board.toggleColor(color)));
        }

        int maxValueIndex=0;
        for(int i=1;i<moveCount;i++){ // get move with highest value
        	if(value[i]>value[maxValueIndex])
        		maxValueIndex=i;
        }
        return moves.get(maxValueIndex);
    }
}