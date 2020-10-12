/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * main class
 *
 * @author FILL IN
 * @author FILL IN
 * assignment group FILL IN
 * <p>
 * assignment copyright Kees Huizing
 */

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;

class PrisonersDilemma {
    private static final int PLAYING_FIELD_PADDING = 30;
    private static final int PADDING_ERROR_CORRECTION = 7;
    private static final int OPTIONS_HEIGHT = 150;
    private static final int FRAME_WIDTH = PlayingField.PLAYING_FIELD_WIDTH + PLAYING_FIELD_PADDING * 2;
    private static final int FRAME_HEIGHT = PlayingField.PLAYING_FIELD_HEIGHT + PLAYING_FIELD_PADDING / 2 * 3 + OPTIONS_HEIGHT;
    private static final double MIN_ALPHA = 0;
    private static final double MAX_ALPHA = 3;
    private static final double DEFAULT_ALPHA = 1;
    private static final int MIN_DELAY = 10;
    private static final int MAX_DELAY = 2000;
    private static final int DEFAULT_DELAY = 1000;
    private final PlayingField playingField;

    private PrisonersDilemma() {
        this.playingField = new PlayingField(DEFAULT_ALPHA, DEFAULT_DELAY);
        this.playingField.resetGrid();
    }

    public static void main(String[] a) {
        (new PrisonersDilemma()).buildGUI();
    }

    //...

    private void buildGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prisoners Dilemma");
            frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
            frame.setVisible(true);
            frame.setResizable(false);
            frame.setLayout(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            this.playingField.setLocation(PLAYING_FIELD_PADDING - PADDING_ERROR_CORRECTION, PLAYING_FIELD_PADDING / 2);
            this.playingField.draw();
            frame.add(this.playingField);

            JPanel options = new JPanel();
            options.setSize(PlayingField.PLAYING_FIELD_WIDTH, OPTIONS_HEIGHT);
            options.setLocation(PLAYING_FIELD_PADDING, PLAYING_FIELD_PADDING + PlayingField.PLAYING_FIELD_HEIGHT);
            options.setLayout(new FlowLayout());
            // Options
            JLabel alphaLabel = new JLabel("Alpha = " + DEFAULT_ALPHA / 10.0);
            options.add(alphaLabel);
            JSlider alphaSlider = new JSlider((int) MIN_ALPHA * 10, (int) MAX_ALPHA * 10, (int) DEFAULT_ALPHA * 10);
            Hashtable<Integer, JLabel> alphaLabelTable = new Hashtable<>();
            alphaLabelTable.put((int) MIN_ALPHA * 10, new JLabel(String.format("%.2f", MIN_ALPHA)));
            alphaLabelTable.put((int) DEFAULT_ALPHA * 10, new JLabel(String.format("%.2f", DEFAULT_ALPHA)));
            alphaLabelTable.put((int) MAX_ALPHA * 10, new JLabel(String.format("%.2f", MAX_ALPHA)));
            alphaSlider.setLabelTable(alphaLabelTable);
            alphaSlider.setPaintLabels(true);
            alphaSlider.addChangeListener(e -> {
                double alpha = (double) alphaSlider.getValue() / 10.0d;
                this.playingField.setAlpha(alpha);
                alphaLabel.setText("Alpha = " + alpha);
            });
            options.add(alphaSlider);

            JLabel delayLabel = new JLabel("Delay/step (ms) = " + DEFAULT_DELAY);
            options.add(delayLabel);
            JSlider delaySlider = new JSlider(MIN_DELAY, MAX_DELAY, DEFAULT_DELAY);
            Hashtable<Integer, JLabel> delayLabelTable = new Hashtable<>();
            delayLabelTable.put(MIN_DELAY, new JLabel(Integer.toString(MIN_DELAY)));
            delayLabelTable.put(DEFAULT_DELAY, new JLabel(Integer.toString(DEFAULT_DELAY)));
            delayLabelTable.put(MAX_DELAY, new JLabel(Integer.toString(MAX_DELAY)));
            delaySlider.setLabelTable(delayLabelTable);
            delaySlider.setPaintLabels(true);
            delaySlider.addChangeListener(e -> {
                int delay = delaySlider.getValue();
                this.playingField.setDelay(delay);
                delayLabel.setText("Delay/step (ms) = " + delay);
            });
            options.add(delaySlider);

            JLabel selfPreferenceLabel = new JLabel("Self preference");
            options.add(selfPreferenceLabel);
            JCheckBox selfPreferenceBox = new JCheckBox();
            selfPreferenceBox.addChangeListener(e -> this.playingField.setSelfPreference(selfPreferenceBox.isSelected()));
            options.add(selfPreferenceBox);

            // Buttons
            JButton go = new JButton("Go");
            go.addActionListener(e -> go.setText(this.playingField.toggleRunning() ? "Pause" : "Go"));
            options.add(go);
            JButton reset = new JButton("Reset");
            reset.addActionListener(e -> this.playingField.resetGrid());
            options.add(reset);

            frame.add(options);
        });
    }
}
