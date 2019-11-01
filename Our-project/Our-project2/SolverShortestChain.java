import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

public class SolverShortestChain extends GameSolver {

    @Override
    public Edge getNextMove(final Board board, int color) {
		boolean Endgame = false;
		
		if(board.getBoxCount(0)==0 && board.getBoxCount(1)==0) { //Endgame means all boxes have at least 2 sides
			Endgame = true; 
		}
		
		if(!Endgame) {//If not in Endgame yet, play like Heuristic
			referenceColor = color;
			ArrayList<Edge> moves = board.getAvailableMoves();
			Collections.shuffle(moves);
			int moveCount = moves.size();
			int value[] = new int[moveCount];
        
			for(int i=0;i<moveCount;i++) {
				Board nextBoard = board.getNewBoard(moves.get(i), color);
				value[i] = heuristic(nextBoard, (nextBoard.getScore(color) > board.getScore(color) ? color : Board.toggleColor(color)));
			}

			int maxValueIndex=0;
			for(int i=1;i<moveCount;i++){
				if(value[i]>value[maxValueIndex])
					maxValueIndex=i;
			}
			return moves.get(maxValueIndex);
		}
		else {
			referenceColor = color;
			ArrayList<Edge> moves = board.getAvailableMoves();
			int moveCount = moves.size();
			int score = board.getScore(color);
        
			for(int i=0;i<moveCount;i++) { // if boxes can be taken, take boxes.
				Board nextBoard = board.getNewBoard(moves.get(i), color);
				if(nextBoard.getScore(color) > score) {
					return moves.get(i);
				}
			}
			ReturnValues chainValues = board.getChainInformation();
			ArrayList<Point> Shorties = chainValues.shortest; //Return the array with the boxes that are part of the shortest chain (ideally single blocks)
//        	System.out.println("Shortlist: "+ Shorties);
			ArrayList<Edge> bMoves = board.getAvailableMoves(Shorties); //Get the open edges of these boxes
			Collections.shuffle(bMoves);
			return bMoves.get(0);//Pick one of these edges as the next move (thus opening the shortest chain).
		}
    }
}