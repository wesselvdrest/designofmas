import java.awt.*;
import java.util.ArrayList;

public class TestProgram {

    private static GameSolver getSolver(int level) {
        if(level == 1) return new RandomSolver();
        else if(level == 2) return new GreedySolver();
        else return null;
    }

    public static void main(String[] args) {
        int lev1 = Integer.parseInt(args[0]);
        int lev2 = Integer.parseInt(args[1]);
        int size = Integer.parseInt(args[2]);
        int nGames = Integer.parseInt(args[3]);
        
        int redCount = 0, blueCount = 0, greenCount = 0, tieCount = 0;

        for (int z = 0; z < nGames; z++) {
            GameSolver redSolver = getSolver(lev1);
            GameSolver blueSolver = getSolver(lev2);
            GameSolver greenSolver = getSolver(lev3);
            int turn = Board.RED;
            Board board = new Board(size);
            while (!board.isComplete()) {
                Edge move;
                if (turn == Board.RED) move = redSolver.getNextMove(board, Board.RED);
                else if (turn == Board.BLUE) move = blueSolver.getNextMove(board, Board.BLUE);
                else move = greenSolver.getNextMove(board, Board.GREEN);
                ArrayList<Point> ret;
                if (move.isHorizontal())
                    ret = board.setHEdge(move.getX(), move.getY(), turn);
                else
                    ret = board.setVEdge(move.getX(), move.getY(), turn);
                if (ret.isEmpty())
                    turn = Board.toggleColor(turn);
            }
            int winner = board.getWinner();
            if (winner == Board.RED) redCount++;
            else if (winner == Board.BLUE) blueCount++;
            else if (winner == Board.GREEN) greenCount++;
            else tieCount++;
        }

        System.out.println("Red (Level " + lev1 + ") Won : " + redCount);
        System.out.println("Blue (Level " + lev2 + ") Won : " + blueCount);
        System.out.println("Green (Level " + lev3 + ") Won : " + greenCount);
        System.out.println("Tied : " + tieCount);

    }

}
