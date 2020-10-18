/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part PlayingField
 *
 * @author FILL IN
 * @author FILL IN
 * assignment group FILL IN
 * <p>
 * assignment copyright Kees Huizing
 */

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class PlayingField extends JPanel {

    // The amount of rows and columns the grid starts with.
    private static final int DEFAULT_GRID_SIZE = 200;
    // The width and height, in pixels, of a patch when it is rendered.
    private static final int PATCH_SIZE = 5;
    // The padding around each patch, in pixels, when it is rendered.
    private static final int PADDING_SIZE = 1;
    // seed for random number generator; any number goes
    private static final long SEED = 37L;
    // random number genrator
    private static final Random RANDOM = new Random(SEED);
    // The amount of neighbours a patch can have.
    private static final int NEIGHBOURS_COUNT = 8;
    private static final long serialVersionUID = 8170149681706008947L;
    // The timer that is used to time when to step.
    private final Timer timer;
    // The height for the grid, defaults to 50.
    private int gridHeight = DEFAULT_GRID_SIZE;
    // The width for the grid, defaults to 50.
    private int gridWidth = DEFAULT_GRID_SIZE;
    // The grid of patches as a list.
    private List<Patch> grid;
    // defection award factor
    private double alpha;
    // If the patch should prefer its own strategy if its points are equal to the points of its best neighbour.
    private boolean selfPreference = false;
    // If the playing field is currently running.
    private boolean running;
    // The amount of delay between each step.
    private int delay;

    /**
     * The constructor for the playing field. It requires an alpha value and a delay value.
     */
    PlayingField(double alpha, int delay) {
        this.alpha = alpha;
        this.delay = delay;
        this.timer = new Timer(this.delay, e -> {
            step();
        });
        this.timer.setRepeats(true);
        this.timer.setInitialDelay(1);
    }

    /**
     * calculate and execute one step in the simulation
     */
    void step() {
        this.grid.parallelStream().forEach(patch -> {
            if (patch.getNeighbours() == null) {
                int x = patch.getFieldX();
                int y = patch.getFieldY();
                patch.setNeighbours(getNeighbours(x, y));
            }
            patch.setHasChanged(false);
            patch.calculateScore(this.alpha);
        });

        this.grid.parallelStream().forEach(patch -> {
            List<Patch> neighbours = patch.getNeighbours();
            List<Patch> patches = new ArrayList<>(neighbours);
            patches.add(patch);
            Patch bestPatch = getBestPatch(patches);

            if (this.selfPreference && bestPatch.getScore() <= patch.getScore()) {
                bestPatch = patch;
            }
            patch.setCooperating(bestPatch.isOldCooperating());
        });

        this.grid.parallelStream().forEach(Patch::draw);
        repaint();
    }

    /**
     * Sets size and layout of the playing field and draws each patch.
     */
    void draw() {
        this.setLayout(null);
        // The dimension of this playing field panel.
        Dimension dimension = getDimension();
        this.setSize((int) dimension.getWidth(), (int) dimension.getHeight());
        updateDraw();
    }

    /**
     * Draws each patch on the playing field after removing the previous patches.
     */
    private void updateDraw() {
        removeAll();
        this.grid.forEach(patch -> {
            int x = patch.getFieldX();
            int y = patch.getFieldY();
            patch.setSize(PATCH_SIZE, PATCH_SIZE);
            patch.setLocation(PATCH_SIZE * x + PADDING_SIZE * (x + 1), PATCH_SIZE * y + PADDING_SIZE * (y + 1));
            patch.draw();
            patch.addMouseListener(patch);
            add(patch);
        });
        repaint();
    }

    /**
     * Returns the alpha value for the playing field.
     */
    public double getAlpha() {
        return this.alpha;
    }

    /**
     * Sets the alpha value to the playing field.
     */
    void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    /**
     * Returns the patch with the highest score from the given patches. It chooses a random patch if there are multiple
     * patches with the highest score.
     */
    private Patch getBestPatch(List<Patch> patches) {
        // The highest score for the specified patches.
        double max = patches.stream().mapToDouble(Patch::getScore).reduce(Double::max).orElse(0);
        // The list of patches that have the highest score.
        List<Patch> bestPatches = patches.stream()
                .filter(neighbour -> max == neighbour.getScore())
                .collect(Collectors.toList());
        Collections.shuffle(bestPatches, RANDOM);
        return bestPatches.get(0);
    }

    /**
     * Returns the neighbours for the patch on the given x and y value.
     */
    private List<Patch> getNeighbours(int x, int y) {
        return this.grid.parallelStream()
                .filter(patch -> !(x == patch.getFieldX() && y == patch.getFieldY())
                        && isNeighbour(x, patch.getFieldX(), this.gridWidth)
                        && isNeighbour(y, patch.getFieldY(), this.gridHeight))
                .collect(Collectors.toList());
    }

    /**
     * Returns true if the given value is within the neighbourhood of the specified index coordinate.
     */
    private boolean isNeighbour(int index, int value, int max) {
        return Math.abs((index + max) % max - value) < 2;
    }

    // return grid as 2D array of booleans
    // true for cooperators, false for defectors
    // precondition: grid is rectangular, has non-zero size and elements are non-null
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[this.gridWidth][this.gridHeight];
        this.grid.forEach(patch -> resultGrid[patch.getFieldX()][patch.getFieldY()] = patch.isCooperating());
        return resultGrid;
    }

    // sets grid according to parameter inGrid
    // a patch should become cooperating if the corresponding
    // item in inGrid is true
    public void setGrid(boolean[][] inGrid) {
        this.gridWidth = inGrid.length;
        this.gridHeight = inGrid[0].length;

        this.grid = IntStream.range(0, this.gridWidth * this.gridHeight).parallel().mapToObj(i -> {
            int x = i / this.gridWidth;
            int y = i % this.gridHeight;
            return new Patch(x, y, inGrid[x][y]);
        }).collect(Collectors.toList());

        draw();
    }

    /**
     * Resets tbe grid to random patches.
     */
    void resetGrid() {
        this.grid = IntStream.range(0, this.gridWidth * this.gridHeight).parallel().mapToObj(i -> new Patch(i / this.gridWidth, i % this.gridHeight, RANDOM.nextBoolean())).collect(Collectors.toList());
        updateDraw();
    }

    /**
     * Toggles the running state, starting or stopping the timer, and returns the new state.
     */
    boolean toggleRunning() {
        this.running = !this.running;
        if (this.running) {
            this.timer.start();
        } else {
            this.timer.stop();
        }
        return this.running;
    }

    /**
     * Returns the running state of the playing field.
     */
    boolean isRunning() {
        return this.running;
    }

    /**
     * Sets the delay of the playing field.
     */
    void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    /**
     * Returns the width and the height of the playing field, determined by a calculation, as a Dimension object.
     */
    Dimension getDimension() {
        return new Dimension(this.gridWidth * PATCH_SIZE + (this.gridWidth + 1) * PADDING_SIZE, this.gridHeight * PATCH_SIZE + (this.gridHeight + 1) * PADDING_SIZE);
    }

    /**
     * Returns if the patches will have self preference.
     */
    public boolean hasSelfPreference() {
        return this.selfPreference;
    }

    /**
     * Sets the self preference for the patches.
     */
    void setSelfPreference(boolean selfPreference) {
        this.selfPreference = selfPreference;
    }
}

