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

import javax.swing.Timer;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

class PlayingField extends JPanel {

    private static final int DEFAULT_GRID_SIZE = 50;
    private static final int PATCH_SIZE = 10;
    private static final int PADDING_SIZE = 3;
    static final int PLAYING_FIELD_HEIGHT = DEFAULT_GRID_SIZE * PATCH_SIZE + (DEFAULT_GRID_SIZE + 1) * PADDING_SIZE;
    static final int PLAYING_FIELD_WIDTH = DEFAULT_GRID_SIZE * PATCH_SIZE + (DEFAULT_GRID_SIZE + 1) * PADDING_SIZE;
    // random number genrator
    private static final long SEED = 37L; // seed for random number generator; any number goes
    private static final Random random = new Random(SEED);
    private static final int NEIGHBOURS_COUNT = 8;
    private static final long serialVersionUID = -1360313648885953666L;
    private final Timer timer;
    private Patch[][] grid;
    private double alpha; // defection award factor
    private boolean selfPreference = false;
    private boolean running;
    private int delay;

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
    private void step() {
        Patch[][] grid = new Patch[this.grid.length][this.grid[0].length];
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                Patch patch = this.grid[x][y];
                patch.setNeighbours(getNeighbours(x, y));
                patch.calculateScore(this.alpha);

                this.grid[x][y] = patch;
            }
        }

        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                Patch patch = this.grid[x][y];
                Patch[] neighbours = patch.getNeighbours();
                List<Patch> matches = new ArrayList<>(Arrays.asList(neighbours));
                matches.add(patch);
                Patch bestPatch = getBestPatch(matches);

                if (this.selfPreference && bestPatch.getScore() <= patch.getScore()) {
                    bestPatch = patch;
                }

                grid[x][y] = new Patch(bestPatch.isCooperating(), patch.isCooperating() != bestPatch.isCooperating());
            }
        }

        this.grid = grid;
        updateDraw();
    }

    void draw() {
        this.setLayout(null);
        this.setSize(PLAYING_FIELD_WIDTH, PLAYING_FIELD_HEIGHT);
        updateDraw();
    }

    private void updateDraw() {
        removeAll();
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                Patch patch = this.grid[x][y];
                patch.setSize(PATCH_SIZE, PATCH_SIZE);
                patch.setLocation(PATCH_SIZE * x + PADDING_SIZE * (x + 1), PATCH_SIZE * y + PADDING_SIZE * (y + 1));
                patch.draw();
                patch.addMouseListener(patch);
                add(patch);
            }
        }
        repaint();
    }

    public double getAlpha() {
        return this.alpha;
    }

    void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    private Patch getBestPatch(List<Patch> matches) {
        double max = matches.stream().mapToDouble(Patch::getScore).reduce(Double::max).orElse(0);
        List<Patch> bestPatches = matches.stream()
                .filter(neighbour -> max == neighbour.getScore())
                .collect(Collectors.toList());
        Collections.shuffle(bestPatches, random);
        return bestPatches.get(0);
    }

    private Patch[] getNeighbours(int x, int y) {
        Patch[] neighbours = new Patch[NEIGHBOURS_COUNT];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    continue;
                }
                neighbours[index++] = this.grid[(x + i - 1 + this.grid.length) % this.grid.length][(y + j - 1 + this.grid[0].length) % this.grid[0].length];
            }
        }
        return neighbours;
    }

    // return grid as 2D array of booleans
    // true for cooperators, false for defectors
    // precondition: grid is rectangular, has non-zero size and elements are non-null
    public boolean[][] getGrid() {
        boolean[][] resultGrid = new boolean[this.grid.length][this.grid[0].length];
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                resultGrid[x][y] = this.grid[x][y].isCooperating();
            }
        }

        return resultGrid;
    }

    // sets grid according to parameter inGrid
    // a patch should become cooperating if the corresponding
    // item in inGrid is true
    public void setGrid(boolean[][] inGrid) {
        this.grid = new Patch[inGrid.length][inGrid[0].length];
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                this.grid[x][y] = new Patch(inGrid[x][y]);
            }
        }
    }

    void resetGrid() {
        this.grid = new Patch[DEFAULT_GRID_SIZE][DEFAULT_GRID_SIZE];
        for (int x = 0; x < this.grid.length; x++) {
            for (int y = 0; y < this.grid[0].length; y++) {
                this.grid[x][y] = new Patch(random.nextBoolean());
            }
        }
        updateDraw();
    }

    boolean toggleRunning() {
        this.running = !this.running;
        if (this.running) {
            this.timer.start();
        } else {
            this.timer.stop();
        }
        return this.running;
    }

    void setDelay(int delay) {
        this.delay = delay;
        this.timer.setDelay(delay);
    }

    public boolean isSelfPreference() {
        return this.selfPreference;
    }

    void setSelfPreference(boolean selfPreference) {
        this.selfPreference = selfPreference;
    }
}

