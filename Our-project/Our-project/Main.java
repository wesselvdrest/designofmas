import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    private int n = 5;
    private GameSolver redSolver, blueSolver, greenSolver;
    private String redName, blueName, greenName;

    private JFrame frame;
    private JLabel modeError, sizeError;

    String[] players = {"Human", "Random Player", "Greedy Player"};
    private JRadioButton[] sizeButton;

    JComboBox<String> redList, blueList, greenList;
    ButtonGroup sizeGroup;

    public Main() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        redList = new JComboBox<String>(players);
        blueList = new JComboBox<String>(players);
        greenList = new JComboBox<String>(players);

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
        if(level == 1) return new RandomSolver();
        else if(level == 2) return new GreedySolver();
        else return null;
    }

    private ActionListener submitListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int rIndex = redList.getSelectedIndex();
            int bIndex = blueList.getSelectedIndex();
            int gIndex = greenList.getSelectedIndex();
//            if(rIndex==0 || bIndex==0) {
//                modeError.setText("You MUST select the players before continuing.");
//                return;
//            }
//            else {
                modeError.setText("");
                redName = players[rIndex];
                blueName = players[bIndex];
                greenName = players[gIndex];
                if(rIndex > 1) redSolver = getSolver(rIndex - 1);
                if(bIndex > 1) blueSolver = getSolver(bIndex - 1);
                if(gIndex > 1) greenSolver = getSolver(gIndex - 1);
//            }
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
        greenSolver = null;

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

        JPanel modePanel = new JPanel(new GridLayout(3, 1));
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
        modePanel.add(new JLabel("<html><font color='green'>Player-3:", SwingConstants.CENTER));
        modePanel.add(greenList);
        greenList.setSelectedIndex(2);
        greenList.setBackground(Color.DARK_GRAY);
        greenList.setForeground(Color.WHITE);
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
        new GamePlay(this, frame, n, redSolver, blueSolver, greenSolver, redName, blueName, greenName);
    }

    public static void main(String[] args) {
        new Main().initGUI();
    }

}
