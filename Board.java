import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Board implements Cloneable {
	//Default
    final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 3;
    final static int BLANK = 4;

    //Defining the coordinates of the edges and the boxes (a 1x1 square of edges)
    private int[][] hEdge;
    private int[][] vEdge;
    private int[][] box;

    private int n, redScore, blueScore;

    // Default board setting
    public Board(int n) {
        hEdge = new int[n-1][n];
        vEdge = new int[n][n-1];
        box = new int[n-1][n-1];
        fill(hEdge,BLANK);
        fill(vEdge,BLANK);
        fill(box,BLANK);
        this.n = n;
        redScore = blueScore = 0;
    }
    //Measure how many boxes are scored by player 1 and player 2 (Red vs. Blue)
    public Board clone() {
        Board cloned = new Board(n);

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<n; j++)
                cloned.hEdge[i][j] = hEdge[i][j];

        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                cloned.vEdge[i][j] = vEdge[i][j];

        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++)
                cloned.box[i][j] = box[i][j];

        cloned.redScore = redScore;
        cloned.blueScore = blueScore;

        return cloned;
    }

    private void fill(int[][] array, int val) {
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    }

    public int getSize() { return n; }

    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getScore(int color) {
        if(color == RED) return redScore;
        else return blueScore;
    }

    public static int toggleColor(int color) {
        if(color == RED)
            return BLUE;
        else
            return RED;
    }
    //Inform agent which edges are left to choose from
    public ArrayList<Edge> getAvailableMoves() {
        ArrayList<Edge> ret = new ArrayList<Edge>();
        for(int i=0; i<(n-1);i++)
            for(int j=0; j<n; j++)
                if(hEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,true));
        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                if(vEdge[i][j] == BLANK)
                    ret.add(new Edge(i,j,false));
        return ret;
    }
    
    public ArrayList<Edge> getAvailableMoves(ArrayList<Point> Boxes) { //Get available moves of given boxes
        ArrayList<Edge> ret = new ArrayList<Edge>();
		for(Point box : Boxes){ // check for all boxes which edges are still empty and add to ret if true
			int x = (int)box.getX();
			int y = (int)box.getY();
			if(hEdge[x][y] == BLANK){
				ret.add(new Edge(x,y,true));
			}
			if(hEdge[x][y+1] == BLANK){
				ret.add(new Edge(x,y+1,true));
			}
			if(vEdge[x][y] == BLANK){
				ret.add(new Edge(x,y,false));
			}
			if(vEdge[x+1][y] == BLANK){
				ret.add(new Edge(x+1,y,false));
			}
		}
        return ret;
    }
    //Make up the board; fill all the horizontal edges with black-coloured edges
    public ArrayList<Point> setHEdge(int x, int y, int color) {
        hEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(y<(n-1) && vEdge[x][y]==BLACK && vEdge[x+1][y]==BLACK && hEdge[x][y+1]==BLACK) {
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(y>0 && vEdge[x][y-1]==BLACK && vEdge[x+1][y-1]==BLACK && hEdge[x][y-1]==BLACK) {
            box[x][y-1]=color;
            ret.add(new Point(x,y-1));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }
    //Make up the board; fill all the vertical edges with black-coloured edges
    public ArrayList<Point> setVEdge(int x, int y, int color) {
        vEdge[x][y]=BLACK;
        ArrayList<Point> ret = new ArrayList<Point>();
        if(x<(n-1) && hEdge[x][y]==BLACK && hEdge[x][y+1]==BLACK && vEdge[x+1][y]==BLACK) {
            box[x][y]=color;
            ret.add(new Point(x,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(x>0 && hEdge[x-1][y]==BLACK && hEdge[x-1][y+1]==BLACK && vEdge[x-1][y]==BLACK) {
            box[x-1][y]=color;
            ret.add(new Point(x-1,y));
            if(color == RED) redScore++;
            else blueScore++;
        }
        return ret;
    }

    public boolean isComplete() {
        return (redScore + blueScore) == (n - 1) * (n - 1);
    }
    //Winner is player with highest score
    public int getWinner() {
        if(redScore > blueScore) return RED;
        else if(blueScore > redScore) return BLUE;
        else return BLANK;
    }

    public Board getNewBoard(Edge edge, int color) {
        Board ret = clone();
        if(edge.isHorizontal())
            ret.setHEdge(edge.getX(), edge.getY(), color);
        else
            ret.setVEdge(edge.getX(), edge.getY(), color);
        return ret;
    }
    //System knows of how many edges the board exists. Hence, by deriving the number of edge filled, it knows how many turns are still left
    private int getEdgeCount(int i, int j) {
        int count = 0;
        if(hEdge[i][j] == BLACK) count++;
        if(hEdge[i][j+1] == BLACK) count++;
        if(vEdge[i][j] == BLACK) count++;
        if(vEdge[i+1][j] == BLACK) count++;
        return count;
    }

    public int getBoxCount(int nSides) {
        int count = 0;
        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++) {
                if(getEdgeCount(i, j) == nSides)
                    count++;
            }
        return count;
    }

	public int amountBoxesLeft() { //loops over all boxes and counts how many are left
		int count = 0;
        for(int i=0; i<(n-1); i++)
            for(int j=0; j<(n-1); j++) {
                if(box[i][j] == BLANK)
                    count++;
            }
        return count;
	}
	
	//Goes over the board and extracts information about the chains, how many, how long and 
	// what the coordinates of certain chains are (of length 1, length 2 and the shortest chain on the board)
	public ReturnValues getChainInformation() { 
		int[] Histogram = new int[64]; //Initializes at zero. Size of largest board (8*8) (cannot have a chain longer than that)
		ArrayList<Point> singleBoxes = new ArrayList<Point>(); //coordinates for chains of length 1
		ArrayList<Point> diadChains = new ArrayList<Point>(); //coordinates for chains of length 2
		ArrayList<Point> shortestChains = new ArrayList<Point>(); //coordinates for shortest chain on the board
		ArrayList<Point> blackList = new ArrayList<Point>(); //these boxes have already been considered
		ArrayList<Point> Chain = new ArrayList<Point>(); // array for current chain coordinates
		Point coordinate = new Point();
		int sizeShortest = 64;
		for(int i=0; i<(n-1); i++) { //loop over all boxes of the board
            for(int j=0; j<(n-1); j++) {
				boolean blacklisted = false;
				coordinate = new Point(i,j);
				if (!(box[i][j] == BLANK)) { // colored boxes are excluded from count
					blackList.add(coordinate); 
					continue;
				}
				for(Point pt : blackList) { // check whether this box has already been considered
					if(coordinate.equals(pt)){
						blacklisted = true;
						break;
					}
				}
				if(blacklisted) continue;
				blackList.add(coordinate); //blacklist all boxes that have been considered
				Chain.add(coordinate);
				int size = Chain.size()-1;
				Point neighbour= new Point();
				while(Chain.size() != size){ // look at the neighbours without walls in between, add to Chain untill no new neighbours are added
					size = Chain.size();
					ArrayList<Point> Temp = new ArrayList<Point>(); // Array for temporary storing coordinates to add to the chain
					for(Point crd : Chain){
						int x = (int)crd.getX();
						int y = (int)crd.getY();
						
						//for all edges check wheter the edge is not a border and edge is not filled in (blank)

						//Horizontal edge above, neighbour at coordinates (x, y-1):
						if(y>0 && hEdge[x][y] == BLANK){ 
							neighbour = new Point(x, (y-1));
							boolean bl = false;
							for(Point pt : blackList) { // check whether this box has already been considered
								if(neighbour.equals(pt)){
									bl = true;
									break;
								}
							}
							if(!bl){ // if this box has not been considered before, add it to the chain and blacklist it
								Temp.add(neighbour);
								blackList.add(neighbour);
							}
						}
						 //Same for Horizontal edge below, (((might break on N))), neighbour at coordinates (x, y+1):
						if(y<n-2 && hEdge[x][y+1] == BLANK){
							neighbour = new Point(x, (y+1));
							boolean bl = false;
							for(Point pt : blackList) {
								if(neighbour.equals(pt)){
									bl = true;
									break;
								}
							}
							if(!bl){
								Temp.add(neighbour);
								blackList.add(neighbour);
							}
						}
						//Same for Vertical edge left, neighbour at coordinates (x-1, y):
						if(x>0 && vEdge[x][y] == BLANK){ 
							neighbour = new Point((x-1), y);
							boolean bl = false;
							for(Point pt : blackList) {
								if(neighbour.equals(pt)){
									bl = true;
									break;
								}
							}
							if(!bl){
								Temp.add(neighbour);
								blackList.add(neighbour);
							}
						}
						//Same for Vertical edge right, neighbour at coordinates (x+1, y):
						if(x<(n-2) && vEdge[x+1][y] == BLANK){ 
							neighbour = new Point((x+1), y);
							boolean bl = false;
							for(Point pt : blackList) {
								if(neighbour.equals(pt)){
									bl = true;
									break;
								}
							}
							if(!bl){
								Temp.add(neighbour);
								blackList.add(neighbour);
							}
						}
					}
					for(Point tp : Temp){
						Chain.add(tp);
					}
					Temp.clear();
				}
				Histogram[Chain.size()]++; //add chain to histogram
				if(Chain.size() == 1){ //if the chain is length 1, add it to the other length 1 coordinates
					for(Point crd : Chain){
						singleBoxes.add(crd);
					}
				}
				if(Chain.size() == 2){ //if the chain is length 2, add it to the other length 2 coordinates
					for(Point crd : Chain){
						diadChains.add(crd);
					}
				}
				if(Chain.size() == sizeShortest){ //if this is also one of the shortest chains, add to the shortestChains
					for(Point crd : Chain){
						shortestChains.add(crd);
					}
				}
				if(Chain.size() < sizeShortest){ //if we found a shorter chain than the current shortchain, replace it
					sizeShortest = Chain.size();
					shortestChains.clear();
					for(Point crd : Chain){
						shortestChains.add(crd);
					}
				} 
				Chain.clear();
            }
		} 
		ReturnValues values = new ReturnValues(singleBoxes, diadChains, shortestChains, Histogram);
		return values;
	}
	
}