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
import java.util.function.Consumer;
import java.util.function.Function;

class PrisonersDilemma {
    // The padding, in pixels, around the playing field panel.
    private static final int PLAYING_FIELD_PADDING = 30;
    // A correction of 7 pixels to center the playing field correctly.
    private static final int PADDING_ERROR_CORRECTION = 7;
    // The required height, in pixels, for the options panel.
    private static final int OPTIONS_HEIGHT = 400; // 150
    // The minimal value for alpha.
    private static final double MIN_ALPHA = 0;
    // The maximum value for alpha.
    private static final double MAX_ALPHA = 3;
    // The default value for alpha.
    private static final double DEFAULT_ALPHA = 1;
    // The minimal value for the delay.
    private static final int MIN_DELAY = 10;
    // The maximum value for the delay.
    private static final int MAX_DELAY = 2000;
    // The default value for the delay.
    private static final int DEFAULT_DELAY = 1000;
    // The titel of the frame.
    private static final String FRAME_TITLE = "Prisoners Dilemma";
    // The playing field panel.
    private final PlayingField playingField;

    private PrisonersDilemma() {
        this.playingField = new PlayingField(DEFAULT_ALPHA, DEFAULT_DELAY);
        this.playingField.resetGrid();
    }

    public static void main(String[] a) {
        (new PrisonersDilemma()).buildGUI();
    }

    private JFrame buildFrame() {
        Dimension dimension = this.playingField.getDimension();
        // The total width of the frame, in pixels.
        int frameWidth = (int) dimension.getWidth() + PLAYING_FIELD_PADDING * 2;
        // The total height of the frame, in pixels.
        int frameHeight = (int) dimension.getHeight() + PLAYING_FIELD_PADDING / 2 * 3 + OPTIONS_HEIGHT;

        // The prisoners dilemma frame.
        JFrame frame = new JFrame(FRAME_TITLE);
        frame.setSize(frameWidth, frameHeight);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        return frame;
    }

    private JPanel buildSlider(String name, int defaultValue, int min, int max, Consumer<Integer> onChange) {
        return buildSlider(name, defaultValue, min, max, i -> Integer.toString(i), onChange);
    }

    private JPanel buildSlider(String name, int defaultValue, int min, int max, Function<Integer, String> formatter, Consumer<Integer> onChange) {
        JPanel options = new JPanel();
        JLabel label = new JLabel(name + " = " + formatter.apply(defaultValue));
        options.add(label);
        // The slider.
        JSlider slider = new JSlider(min, max, defaultValue);
        // The hash table with with labels for under the slider.
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(min, new JLabel(formatter.apply(min)));
        labelTable.put(defaultValue, new JLabel(formatter.apply(defaultValue)));
        labelTable.put(max, new JLabel(formatter.apply(max)));
        slider.setLabelTable(labelTable);
        slider.setPaintLabels(true);
        slider.addChangeListener(e -> {
            // Update the value.
            int value = slider.getValue();
            onChange.accept(value);
            label.setText(name + " = " + formatter.apply(value));
        });
        options.add(slider);
        return options;
    }

    private JPanel buildOptionsPanel() {
        // The options panel.
        JPanel options = new JPanel();
        options.setSize(this.playingField.getDimension().width, OPTIONS_HEIGHT);
        options.setLocation(PLAYING_FIELD_PADDING, PLAYING_FIELD_PADDING + (int) this.playingField.getDimension().getHeight());
        options.setLayout(new FlowLayout());

        // The alpha slider
        JPanel alphaSlider = buildSlider("Alpha", (int) DEFAULT_ALPHA * 10, (int) MIN_ALPHA * 10, (int) MAX_ALPHA * 10, i -> String.format("%.2f", i / 10.0), i -> this.playingField.setAlpha(i / 10.0));
        options.add(alphaSlider);

        // The delay slider
        JPanel delaySlider = buildSlider("Delay/step (ms)", DEFAULT_DELAY, MIN_DELAY, MAX_DELAY, this.playingField::setDelay);
        options.add(delaySlider);

        // The label for the self preference checkbox.
        JLabel selfPreferenceLabel = new JLabel("Self preference");
        options.add(selfPreferenceLabel);
        // The self preference checkbox.
        JCheckBox selfPreferenceBox = new JCheckBox();
        selfPreferenceBox.addChangeListener(e -> this.playingField.setSelfPreference(selfPreferenceBox.isSelected()));
        options.add(selfPreferenceBox);

        // The button that starts and stops running the playing field.
        JButton go = new JButton("Go");
        go.addActionListener(e -> go.setText(this.playingField.toggleRunning() ? "Pause" : "Go"));
        options.add(go);
        // The his button lets the playing field step once.
        JButton step = new JButton("Step");
        step.addActionListener(e -> {
            if (!this.playingField.isRunning()) {
                this.playingField.step();
            }
        });
        options.add(step);
        // The reset button, that resets the playing field.
        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> this.playingField.resetGrid());
        options.add(reset);

        return options;
    }

    private JPanel buildPlayingField() {
        this.playingField.setLocation(PLAYING_FIELD_PADDING - PADDING_ERROR_CORRECTION, PLAYING_FIELD_PADDING / 2);
        this.playingField.draw();

        return this.playingField;
    }

    private void buildGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = buildFrame();
            frame.add(buildPlayingField());
            frame.add(buildOptionsPanel());
        });
    }
}
