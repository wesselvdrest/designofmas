public abstract class GameSolver {

    protected int referenceColor;
    private final static int cScore = 20;
    private final static int cThree = 15;
    private final static int cTwo = 1;

    protected int heuristic(final Board board, int color) {
        int value;
        if(referenceColor == Board.RED)
            value = cScore * board.getRedScore() - cScore * board.getBlueScore() - cScore * board.getGreenScore();
        else if(referenceColor == Board.BLUE)
            value = cScore * board.getBlueScore() - cScore * board.getRedScore()- cScore * board.getGreenScore();
        else
        	value = cScore * board.getGreenScore() - cScore * board.getRedScore() - cScore * board.getBlueScore();
        if(referenceColor == color)
            value += cThree * board.getBoxCount(3) - cTwo * board.getBoxCount(2);
        else
            value -= cThree * board.getBoxCount(3) - cTwo * board.getBoxCount(2);
        return value;
    }

    public abstract Edge getNextMove(final Board board, int color );
}
