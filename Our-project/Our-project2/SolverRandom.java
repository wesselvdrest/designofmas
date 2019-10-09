import java.util.ArrayList;
import java.util.Random;

public class SolverRandom extends GameSolver {

    @Override
    public Edge getNextMove(final Board board, int color) {
        ArrayList<Edge> moves = board.getAvailableMoves();
        return moves.get(new Random().nextInt(moves.size())); //choose random move from all possible moves
    }

}
