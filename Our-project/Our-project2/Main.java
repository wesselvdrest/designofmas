import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
// Original code from https://github.com/gaurav708/dots-and-boxes, this code provides the basic Dots and Boxes game, 
// with structures like the board and turn taking conventions.
// Strategies of agents and the tournament setup are implemented by our own team. 
// Only the SolverHeuristics is copied from the original code (file GreedySolver.java), 
// since it was an interesting strategy to add to our own strategies
import java.util.ArrayList;

public class Main {
	
	private CsvParser agents;

    private int n = 5;
    private GameSolver redSolver, blueSolver;
    private String redName, blueName;

    private JFrame frame;
    private JLabel modeError, sizeError;

    String[] players = {"Human", "Random Player", "Greedy Player", "Heuristic Player"};
    private JRadioButton[] sizeButton;

    JComboBox<String> redList, blueList;
    ButtonGroup sizeGroup;

    public Main() {
    	agents.main();
    	
        
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        redList = new JComboBox<String>(players);
        blueList = new JComboBox<String>(players);

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

    private GameSolver getSolver(int level) {
        switch(level) {
        case 1: // level == 1
        	return new SolverRandom();
        case 2:
        	return new SolverGreedy();
        case 3:
        	return new SolverHeuristic();
        default:
          return null;
      }
    }

    private ActionListener submitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();
            int bIndex = blueList.getSelectedIndex();
            modeError.setText("");
            redName = players[rIndex];
            blueName = players[bIndex];
            if(rIndex > 0) redSolver = getSolver(rIndex);
            if(bIndex > 0) blueSolver = getSolver(bIndex);
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
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);

        modeError = new JLabel("", SwingConstants.CENTER);
        modeError.setForeground(Color.RED);
        modeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(modeError, constraints);

        JPanel modePanel = new JPanel(new GridLayout(2, 1));
        modePanel.setPreferredSize(new Dimension(400, 100));
        modePanel.setBackground(Color.darkGray);
        modePanel.add(new JLabel("<html><font color='red'>Player-1:", SwingConstants.CENTER));
        modePanel.add(redList);
        redList.setSelectedIndex(2);
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
        grid.add(getEmptyLabel(new Dimension(500,25)), constraints);

        sizeError = new JLabel("", SwingConstants.CENTER);
        sizeError.setForeground(Color.RED);
        sizeError.setPreferredSize(new Dimension(500, 25));
        ++constraints.gridy;
        grid.add(sizeError, constraints);

        ++constraints.gridy;
        JLabel messageLabel = new JLabel("<html><font color='white'>Select the size of the board:</font></html>");
        messageLabel.setPreferredSize(new Dimension(400, 7));
        grid.add(messageLabel, constraints);

        JPanel sizePanel = new JPanel(new GridLayout(4, 2));
        sizePanel.setPreferredSize(new Dimension(400, 100));
        for(int i=0; i<7; i++)
            sizePanel.add(sizeButton[i]);
        sizePanel.setBackground(Color.DARK_GRAY);
        sizeGroup.clearSelection();
        ++constraints.gridy;
        grid.add(sizePanel, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        JButton submitButton = new JButton("Start Game");
        submitButton.addActionListener(submitListener);
        ++constraints.gridy;
        grid.add(submitButton, constraints);

        ++constraints.gridy;
        grid.add(getEmptyLabel(new Dimension(500, 25)), constraints);

        frame.setContentPane(grid);
        frame.getContentPane().setBackground( Color.DARK_GRAY );
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startGame = false;
        while(!startGame) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        new GamePlay(this, frame, n, redSolver, blueSolver, redName, blueName);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
