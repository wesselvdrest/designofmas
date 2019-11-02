import javax.swing.*;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
// Original code from https://github.com/gaurav708/dots-and-boxes, read more below 
// this code provides the basic Dots and Boxes game, with structures like the board and turn taking conventions.
// Strategies of agents and the tournament setup are implemented by our own team. 
// Only the SolverHeuristics is copied from the original code (file GreedySolver.java), 
// since it was an interesting strategy to add to our own strategies
import java.util.ArrayList;


public class Main {
	
	protected static final GamePlay GamePlay = null;

	private CsvParser agents;

    private int n = 5;
    private int amountOfPlayers = 100;
    private int epochs = 100;
    private GameSolver redSolver, blueSolver;
    private String redName, blueName;

    private JFrame frame;
    private JLabel modeError, sizeError;

    //All options for strategies
    String[] players = {"Human", "Random Player", "Greedy Player", "Heuristic Player", "ShortestChain Player", "DoubleDealing Player"};
    private JRadioButton[] sizeButton;

    JComboBox<String> redList, blueList;
    ButtonGroup sizeGroup;

    public Main() {
    	//Make it able to access the agents
    	agents.main();
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        redList = new JComboBox<String>(players);
        blueList = new JComboBox<String>(players);

        //Create all board-size buttons
        sizeButton = new JRadioButton[7];
        sizeGroup = new ButtonGroup();
        for(int i=0; i<7; i++) {
            String size = String.valueOf(i+3);
            sizeButton[i] = new JRadioButton(size + " x " + size);
            sizeButton[i].setBackground(Color.DARK_GRAY);
            sizeButton[i].setForeground(Color.white); //To see it on the black background. 
            sizeGroup.add(sizeButton[i]);
        }
    }

    private JLabel getEmptyLabel(Dimension d) {
        JLabel label = new JLabel();
        label.setPreferredSize(d);
        return label;
    }

    private boolean startGame;
    private boolean startTournament;

    //switch function that returns the correct Solver
    private GameSolver getSolver(int level) {
        switch(level) {
        case 1:
        	return new SolverRandom();
        case 2:
        	return new SolverGreedy();
        case 3:
        	return new SolverHeuristic();
        case 4:
        	return new SolverShortestChain();
        case 5:
        	return new SolverDoubleDealing();
        default:
          return null;
      }
    }

//    If the start game button is pressed this function gets fired.
//    it checks which strategies are selected, and checks which board size
//    is selected.
//    If no board size is selected it returns the error message that a size
//    should be selected
    private ActionListener submitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();
            int bIndex = blueList.getSelectedIndex();
            modeError.setText("");
            redName = players[rIndex];
            blueName = players[bIndex];
            if(rIndex == 0) {
            	redSolver = null;
            }
            else {
	            if(rIndex > 0) redSolver = getSolver(rIndex);
            }
            if(bIndex == 0) {
            	blueSolver = null;
            }
            else {
            	if(bIndex > 0) blueSolver = getSolver(bIndex);
            }
            System.out.println("N: "+ n);
            for(int i=0; i<7; i++) {
                if(sizeButton[i].isSelected()) {
                    n = i+3;
                    startGame = true;
                    return;
                }
            }
            sizeError.setText("You MUST select the size of board before continuing.");
        }
    };
    
//  If the start tournament button is pressed this function gets fired.
//  it checks which board size is selected.
//  If no board size is selected it returns the error message that a size
//  should be selected
    private ActionListener tournamentButtonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();
            int bIndex = blueList.getSelectedIndex();
            modeError.setText("");
            redName = players[rIndex];
            blueName = players[bIndex];
            if(rIndex == 0) {
            	redSolver = null;
            }
            else {
	            if(rIndex > 0) redSolver = getSolver(rIndex);
            }
            if(bIndex == 0) {
            	blueSolver = null;
            }
            else {
            	if(bIndex > 0) blueSolver = getSolver(bIndex);
            }
            System.out.println("N: "+ n);
            for(int i=0; i<7; i++) {
                if(sizeButton[i].isSelected()) {
                    n = i+3;
                    startTournament = true;
                    return;
                }
            }
            sizeError.setText("You MUST select the size of board before continuing.");
        }
    };

//  Initialise the GUI
    public void initGUI() {
        redSolver = null;
        blueSolver = null;

        JPanel grid = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel titleLabel = new JLabel(new ImageIcon(getClass().getResource("dot.png")));
        grid.add(titleLabel, constraints);

        ++constraints.gridy;

        modeError = new JLabel("", SwingConstants.CENTER);
        modeError.setForeground(Color.RED);
        modeError.setPreferredSize(new Dimension(500, 25));
        grid.add(modeError, constraints);

//      Field to select the player strategies
        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.setPreferredSize(new Dimension(400, 100));
        modePanel.setBackground(Color.darkGray);
        modePanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
        modePanel.add(redList);
        redList.setSelectedIndex(0);
        redList.setBackground(Color.DARK_GRAY);
        redList.setForeground(Color.WHITE);
        modePanel.add(new JLabel("<html><font color='blue'>Player-2:", SwingConstants.CENTER));
        modePanel.add(blueList);
        blueList.setSelectedIndex(2);
        blueList.setBackground(Color.DARK_GRAY);
        blueList.setForeground(Color.WHITE);
        ++constraints.gridy;
        grid.add(modePanel, constraints);
        grid.setBackground(Color.DARK_GRAY);

        ++constraints.gridy;

        sizeError = new JLabel("", SwingConstants.CENTER);
        sizeError.setForeground(Color.RED);
        sizeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(sizeError, constraints);

//      Add all board size buttons
        JPanel sizePanel = new JPanel(new GridLayout(4, 2));
        sizePanel.setPreferredSize(new Dimension(400, 100));
        for(int i=0; i<7; i++)
            sizePanel.add(sizeButton[i]);
        sizePanel.setBackground(Color.DARK_GRAY);
        sizeGroup.clearSelection();
        ++constraints.gridy;
        grid.add(sizePanel, constraints);
        
//      Add button to start the game
        JButton submitButton = new JButton("Start Game");
        submitButton.addActionListener(submitListener);
        ++constraints.gridy;
        sizePanel.add(submitButton, constraints);
        sizePanel.setBackground(Color.DARK_GRAY);
        sizeGroup.clearSelection();
        ++constraints.gridy;
        grid.add(sizePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);
        
//      Add button to start the tournament
        JPanel tournamentPanel = new JPanel(new GridLayout(1, 5));
        tournamentPanel.setPreferredSize(new Dimension(700, 20));
        JButton tournamentButton = new JButton("Start Tournament");
        tournamentButton.addActionListener(tournamentButtonListener);
        tournamentPanel.add(tournamentButton);
        
        
        grid.add(tournamentPanel, constraints);
        grid.setBackground(Color.DARK_GRAY);   
       
        ++constraints.gridy;
        frame.setContentPane(grid);
        frame.getContentPane().setBackground( Color.DARK_GRAY );
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startGame = false;
        startTournament = false;        
    	
//      If both startGame and startTournament are false it isn't doing anything,
//      when one of them is true it will continue and it will file a new GamePlay
        while(!startGame && !startTournament) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new GamePlay(this, frame, n, redSolver, blueSolver, redName, blueName, startTournament, amountOfPlayers, epochs);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
