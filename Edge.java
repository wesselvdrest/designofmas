// Getting the coordinates of a box' edges. A box has 4 edges, 2 horizontal and 2 vertical.  
public class Edge {

    private int x, y;
    private boolean horizontal;

    Edge() {
        x = y = -1;
        horizontal = false;
    }
    //Creates an edge, connecting the vertices x and y.
    Edge(int x, int y, boolean horizontal) {
        this.x = x;
        this.y = y;
        
        this.horizontal = horizontal;
    }
    //Checks whether the edge is horizontal or not (i.e. vertical)
    public boolean isHorizontal() {
        return horizontal;
    }
    //Get the vertice x
    public int getX() {
        return x;
    }
    //Get the vertice y
    public int getY() {
        return y;
    }
    //If the edge is not a horizontal edge, it must be one of the two vertical edges
    @Override
    public String toString() {
        return ((horizontal ? "H " : "V ") + x + " " + y);
    }

}

