import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random; 
import java.util.List;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GamePlay {

	private final static int size = 15;
    private final static int dist = 70;

    private int n;
    private Board board;
    private int turn;
    private boolean mouseEnabled;
    
	ArrayList<Agent> list;
	private int players = 0;
	private int epochs = 0;
    private int agent1;
    private int agent2;
    private boolean tournament;
    private int agentNumber = 0;
    private int totalGames = 0;
    private String redSolverName;
    private String blueSolverName;
    private int randomAmount = 0;
    private int greedyAmount = 0;
    private int heuristicsAmount = 0;
    private int test = 1;
    private boolean endTournament = false;
    
    
    
    private CsvParser agents;

    GameSolver redSolver, blueSolver, solver;
    String redName, blueName;
    Main parent;

    private JLabel[][] hEdge, vEdge, box;
    private boolean[][] isSetHEdge, isSetVEdge;

    private JFrame frame;
    private JLabel redScoreLabel, blueScoreLabel, statusLabel;

    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            processMove(getSource(mouseEvent.getSource()));
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {

        }

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Edge location = getSource(mouseEvent.getSource());
            int x=location.getX(), y=location.getY();
            if(location.isHorizontal()) {
                if(isSetHEdge[x][y]) return;
                if (turn == Board.RED)
                	hEdge[x][y].setBackground(Color.RED);
                else
                	hEdge[x][y].setBackground(Color.BLUE);
            }
            else {
                if(isSetVEdge[x][y]) return;
                if (turn == Board.RED)
                	vEdge[x][y].setBackground(Color.RED);
                else
                	vEdge[x][y].setBackground(Color.BLUE);
            }
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if(!mouseEnabled) return;
            Edge location = getSource(mouseEvent.getSource());
            int x=location.getX(), y=location.getY();
            if(location.isHorizontal()) {
                if(isSetHEdge[x][y]) return;
                hEdge[x][y].setBackground(Color.GRAY);
            }
            else {
                if(isSetVEdge[x][y]) return;
                vEdge[x][y].setBackground(Color.GRAY);
            }
        }
    };
    
    private void getAmountPerStrategy(String strategy) {
//    	System.out.println("STRATEGY: "+ strategy);
        switch(strategy) {
        case "random": // level == 1
        	randomAmount++;
        	break;
        case "greedy":
        	greedyAmount++;
        	break;
        case "heuristic":
        	heuristicsAmount++;
        	break;
        default:
        	break;
        }
    }

    private void processMove(Edge location) {
        int x=location.getX(), y=location.getY();
        ArrayList<Point> ret;
        if(location.isHorizontal()) {
            if(isSetHEdge[x][y]) return;
            ret = board.setHEdge(x,y,turn);
            hEdge[x][y].setBackground(Color.BLACK);
            isSetHEdge[x][y] = true;
        }
        else {
            if(isSetVEdge[x][y]) return;
            ret = board.setVEdge(x,y,turn);
            vEdge[x][y].setBackground(Color.BLACK);
            isSetVEdge[x][y] = true;
        }

        for(Point p : ret)
        	if (turn == Board.RED)
                box[p.x][p.y].setBackground(Color.RED);
            else
                box[p.x][p.y].setBackground(Color.BLUE);


        redScoreLabel.setText(String.valueOf(board.getRedScore()));
        redScoreLabel.setBackground(Color.DARK_GRAY);
        blueScoreLabel.setText(String.valueOf(board.getBlueScore()));
        blueScoreLabel.setBackground(Color.DARK_GRAY);

        if(board.isComplete()) {
            int winner = board.getWinner();
            if (tournament) {
	    		randomAmount = 0;
	    		greedyAmount = 0;
	    		heuristicsAmount = 0;
	            for (int i = 0; i < players; i++) {
		    		getAmountPerStrategy(list.get(i).getStrategy());
		    	}
            }
            if(winner == Board.RED) {
                statusLabel.setText( redName + " is the winner!");
                statusLabel.setForeground(Color.RED);
//            	appendUsingPrintWriter("./results/result.txt", "Epoch, Winner, WinnerStrategy, Player, Opponent, playerStrategy, opponentStrategy, randomAmount, greedyAmount, heuristicsAmount");
            	appendUsingPrintWriter("./results/result.txt", totalGames + ", "+ redName + ", "+ redSolverName + ", "+ redName + ", "+ blueName + ", "+ redSolverName + ", "+ blueSolverName + ", "+ randomAmount +", "+ greedyAmount+", "+ heuristicsAmount);
            	if (tournament) {
            		list.get(agent2).setStrategy(redSolverName);
            	}
            	System.out.println("Winner: "+ redSolverName  + " "+redName +"\t Opponent: "+ blueSolverName  + " "+blueName +"\t \t EPOCH: "+ totalGames + "\t RANDOM: " + randomAmount +"\t GREEDY: "+ greedyAmount+"\t HEURISTICS: "+ heuristicsAmount);
            }
            else if(winner == Board.BLUE) {
                statusLabel.setText( blueName + " is the winner!");
                statusLabel.setForeground(Color.BLUE);
            	appendUsingPrintWriter("./results/result.txt", totalGames + ", "+ blueName + ", "+ blueSolverName + ", "+ redName + ", "+ blueName + ", "+ redSolverName + ", "+ blueSolverName + ", "+ randomAmount +", "+ greedyAmount+", "+ heuristicsAmount);
            	if (tournament) {
            		list.get(agent1).setStrategy(blueSolverName);
            	}
            	System.out.println("Winner: "+ blueSolverName  + " "+blueName +"\t Opponent: "+ redSolverName  + " "+redName +"\t \t EPOCH: "+ totalGames + "\t RANDOM: " + randomAmount +"\t GREEDY: "+ greedyAmount+"\t HEURISTICS: "+ heuristicsAmount);
            }
            else {
                statusLabel.setText("Game Tied!");
                statusLabel.setForeground(Color.BLACK);
            	appendUsingPrintWriter("./results/result.txt", totalGames + ", None, None, "+ redName + ", "+ blueName + ", "+ redSolverName + ", "+ blueSolverName + ", "+ randomAmount +", "+ greedyAmount+", "+ heuristicsAmount);
            	System.out.println("Winner: NONE \t Opponent: NONE \t \t EPOCH: "+ totalGames + "\t RANDOM: " + randomAmount +"\t GREEDY: "+ greedyAmount+"\t HEURISTICS: "+ heuristicsAmount);
            }
            if (tournament) {
            	initGame();
            }
        }

        if(ret.isEmpty()) {
            if(turn == Board.RED) {
                turn = Board.BLUE;
                solver = blueSolver;
                statusLabel.setText(blueName + "'s Turn...");
                statusLabel.setForeground(Color.BLUE);
            }
            else {
                turn = Board.RED;
                solver = redSolver;
                statusLabel.setText( redName + "'s Turn...");
                statusLabel.setForeground(Color.RED);
            }
        }

    }
    
    private static void appendUsingPrintWriter(String filePath, String text) {
		File file = new File(filePath);
		FileWriter fr = null;
		BufferedWriter br = null;
		PrintWriter pr = null;
		try {
			// to append to file, you need to initialize FileWriter using below constructor
			fr = new FileWriter(file, true);
			br = new BufferedWriter(fr);
			pr = new PrintWriter(br);
			pr.println(text);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				pr.close();
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

    private void manageGame() {
        while(!board.isComplete()) {
            if(goBack) return;
            if(solver == null) {
                mouseEnabled = true;
            }
            else {
                mouseEnabled = false;
                processMove(solver.getNextMove(board, turn));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Edge getSource(Object object) {
        for(int i=0; i<(n-1); i++)
            for(int j=0; j<n; j++)
                if(hEdge[i][j] == object)
                    return new Edge(i,j,true);
        for(int i=0; i<n; i++)
            for(int j=0; j<(n-1); j++)
                if(vEdge[i][j] == object)
                    return new Edge(i,j,false);
        return new Edge();
    }

    private JLabel getHorizontalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, size));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getVerticalEdge() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, dist));
        label.setOpaque(true);
        label.addMouseListener(mouseListener);
        return label;
    }

    private JLabel getDot() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(size, size));
        label.setBackground(Color.BLACK);
        label.setOpaque(true);
        return label;
    }

    private JLabel getBox() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(dist, dist));
        label.setOpaque(true);
        return label;
    }

    private JLabel getEmptyLabel(Dimension d) {
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        return label;
    }
    
    private GameSolver getSolver(String strategy) {
        switch(strategy) {
        case "random": // level == 1
        	return new SolverRandom();
        case "greedy":
        	return new SolverGreedy();
        case "heuristic":
        	return new SolverHeuristic();
        default:
          return null;
      }
    }

    public GamePlay(Main parent, JFrame frame, int n, GameSolver redSolver, GameSolver blueSolver,  String redName, String blueName, boolean tournament, int players, int epochs) {
    	appendUsingPrintWriter("./results/result.txt", "Epoch, Winner, WinnerStrategy, Player, Opponent, playerStrategy, opponentStrategy, randomAmount, greedyAmount, heuristicsAmount");
    	this.tournament = tournament;

    	if (tournament) {
    		agents = new CsvParser();
    		list = agents.getAgents();
    		this.parent = parent;
    		this.players = players;
	        this.frame = frame;
	        this.epochs = epochs;
	        this.n = n;
    		initGame();
    	}
    	else {
	    	this.parent = parent;
	        this.frame = frame;
	        this.n = n;
	        // if none do something
	        this.redSolver = redSolver;
	        this.blueSolver = blueSolver;
	        this.redName = redName;
	        this.blueName = blueName;
	        initGame();
    	}
    }

    private boolean goBack;

    private ActionListener backListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            goBack = true;
        }
    };

    private void initGame() {
    	if (tournament) {

	        ArrayList<Agent> subAgents = new ArrayList<Agent>();
	        List<Integer> subAgentsInt = new ArrayList<Integer>();

	    	for (int i = 0; i < players; i++) {
	    	    if (list.get(i).getAmountPlayed() == totalGames) {
	    	    	subAgentsInt.add(i);
	    	    	subAgents.add(list.get(i));
	    	    }
	    	}
	    	if (subAgents.isEmpty()) {
	    		System.out.println("==================");
	    		System.out.println("==================");
	    		totalGames++;
	    		randomAmount = 0;
	    		greedyAmount = 0;
	    		heuristicsAmount = 0;
	    		for (int i = 0; i < players; i++) {
		    		getAmountPerStrategy(list.get(i).getStrategy());
		    	}
	    		if (randomAmount == players) {
	    			endTournament = true;
	    		}
	    		else if (greedyAmount == players) {
	    			endTournament = true;
	    		}
	    		else if (heuristicsAmount == players) {
	    			endTournament = true;
	    		}
	    	
	    		if (endTournament) {
	    			File directory=new File("./results");
	    		    int fileCount=directory.list().length;
		    		System.out.println("=====FILECOUNT=============" + fileCount);
	    			File oldfile =new File("./results/result.txt");
	    			File newfile =new File("./results/Tournament_" + fileCount + ".txt");
	    			System.out.println("=====NEWFILE=============" + newfile);
	    			if(oldfile.renameTo(newfile)){
	    				System.out.println("Rename succesful");
	    			}else{
	    				System.out.println("Rename failed");
	    			}
	    			parent.initGUI();
	    		}
	    		initGame();
	    	}
	    	Random rand = new Random(); 
	    	int rnd = rand.nextInt(subAgents.size());
	    	int rnd2 = rand.nextInt(subAgents.size());
	    	while (rnd2 == rnd) {
	    		rnd2 = rand.nextInt(subAgents.size());
	    	}
	    	agent1 = subAgentsInt.get(rnd);
	    	agent2 = subAgentsInt.get(rnd2);
	        this.redSolverName = list.get(agent1).getStrategy();
	        this.redSolver = getSolver(redSolverName);
	        this.blueSolverName = list.get(agent2).getStrategy();
	        this.blueSolver = getSolver(blueSolverName);
	        this.redName = list.get(agent1).getName();
	        this.blueName = list.get(agent2).getName();
	        list.get(agent1).incrementAmountPlayed();
	        list.get(agent2).incrementAmountPlayed();
    	}

        board = new Board(n);
        int boardWidth = n * size + (n-1) * dist;
        turn = Board.RED;
        solver = redSolver;

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
        grid.setBackground(Color.DARK_GRAY);
        
        JPanel playerPanel = new JPanel(new GridLayout(4, 3));
        if(n>3) playerPanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        else playerPanel.setPreferredSize(new Dimension(2 * boardWidth, 2 * dist));
        playerPanel.add(new JLabel("<html><font color='white'>Player-1:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>Game:", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>Player-2:", SwingConstants.CENTER));
        
        playerPanel.add(new JLabel("<html><font color='white'>" + redName, SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>"+ totalGames, SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>" + blueName, SwingConstants.CENTER));
        
        playerPanel.add(new JLabel("<html><font color='white'>" + redSolverName, SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>" + blueSolverName, SwingConstants.CENTER));
        
        playerPanel.add(new JLabel("<html><font color='red'>||||||||||||||||", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='white'>", SwingConstants.CENTER));
        playerPanel.add(new JLabel("<html><font color='blue'>||||||||||||||||", SwingConstants.CENTER));
        playerPanel.setBackground(Color.DARK_GRAY);
        ++constraints.gridy;
        grid.add(playerPanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        JPanel scorePanel = new JPanel(new GridLayout(2, 2));
        scorePanel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        scorePanel.add(new JLabel("<html><font color='white'>Score:", SwingConstants.CENTER));
        scorePanel.add(new JLabel("<html><font color='white'>Score:", SwingConstants.CENTER));
        redScoreLabel = new JLabel("0", SwingConstants.CENTER);
        redScoreLabel.setForeground(Color.WHITE);
        scorePanel.add(redScoreLabel);
        blueScoreLabel = new JLabel("0", SwingConstants.CENTER);
        blueScoreLabel.setForeground(Color.WHITE);
        scorePanel.add(blueScoreLabel);
        scorePanel.setBackground(Color.DARK_GRAY);
        ++constraints.gridy;
        grid.add(scorePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);
        grid.setBackground(Color.DARK_GRAY);

        hEdge = new JLabel[n-1][n];
        isSetHEdge = new boolean[n-1][n];

        vEdge = new JLabel[n][n-1];
        isSetVEdge = new boolean[n][n-1];

        box = new JLabel[n-1][n-1];

        for(int i=0; i<(2*n-1); i++) {
            JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER,0,0));
            if(i%2==0) {
                pane.add(getDot());
                for(int j=0; j<(n-1); j++) {
                    hEdge[j][i/2] = getHorizontalEdge();
                    hEdge[j][i/2].setBackground(Color.GRAY);
                    pane.add(hEdge[j][i/2]);
                    pane.add(getDot());
                }
            }
            else {
                for(int j=0; j<(n-1); j++) {
                    vEdge[j][i/2] = getVerticalEdge();
                    vEdge[j][i/2].setBackground(Color.GRAY);
                    pane.add(vEdge[j][i/2]);
                    box[j][i/2] = getBox();
                    box[j][i/2].setBackground(Color.DARK_GRAY);
                    pane.add(box[j][i/2]);
                }
                vEdge[n-1][i/2] = getVerticalEdge();
                vEdge[n-1][i/2].setBackground(Color.GRAY);
                pane.add(vEdge[n-1][i/2]);
            }
            ++constraints.gridy;
            pane.setBackground(Color.DARK_GRAY);
            grid.add(pane, constraints);
        }

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        statusLabel = new JLabel("Player-1's Turn...", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);
        statusLabel.setPreferredSize(new Dimension(2 * boardWidth, dist));
        ++constraints.gridy;
        grid.add(statusLabel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(2 * boardWidth, 10)), constraints);

        JButton goBackButton = new JButton("Go Back to Main Menu");
        goBackButton.setPreferredSize(new Dimension(boardWidth, dist));
        goBackButton.addActionListener(backListener);
        ++constraints.gridy;
        grid.add(goBackButton, constraints);

        frame.getContentPane().removeAll();
        frame.revalidate();
        
        frame.repaint();

        frame.setContentPane(grid);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.DARK_GRAY );

        goBack = false;
        manageGame();

        while(!goBack) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        parent.initGUI();
    }

}
