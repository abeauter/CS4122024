import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DiceRollPanel extends GamePanel {
    private JButton diceRollButton;

    public DiceRollPanel() {
        super();
        setImage(new DiceImage());
    }

    public void addDiceRollActionListener(ActionListener a) {
        diceRollButton.addActionListener(a);
    }

    @Override
    public void disableInput() {
        diceRollButton.setEnabled(false);
    }

    @Override
    public void enableInput() {
        diceRollButton.setEnabled(true);
    }

    @Override
    protected JPanel constructGamePanel() {
        resultText = new JLabel("Roll to play!");
        diceRollButton = new JButton("ROLL");
        imagePanel = constructImagePanel();

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.NORTH, resultText);
        jPanel.add(BorderLayout.CENTER, imagePanel);
        jPanel.add(BorderLayout.SOUTH, diceRollButton);
        jPanel.setBorder(BorderFactory.createEmptyBorder(50,50,50,50));

        // jPanel.setSize(300,300);
        return jPanel;
    }

    @Override
    protected JPanel constructButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));

        buttonGroup = new ButtonGroup();

        for (int i = 1; i <= 6; i++) {
            JRadioButton jRadioButton = new JRadioButton(String.valueOf(i));
            jRadioButton.setActionCommand(String.valueOf(i));
            if(i == 1) jRadioButton.setSelected(true);
            buttonGroup.add(jRadioButton);
            buttonsPanel.add(jRadioButton);
        }

        return buttonsPanel;
    }
}
