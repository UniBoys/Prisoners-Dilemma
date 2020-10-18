import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 *
 * @author Thijs Aarnoudse 1551159
 * @author Jort van Driel 1579584
 * assignment group 52
 * <p>
 * assignment copyright Kees Huizing
 */

class Patch extends JPanel implements MouseListener {
    // The x coordinate of this patch in the playing field.
    private final int x;
    // The y coordinate of this patch in the playing field.
    private final int y;
    // If this patch is cooperating or not.
    private boolean cooperating;
    // The previous version if the patch was cooperating. This old version is used to save the old state of the patch.
    private boolean oldCooperating;
    // If this patch has changed in the last cycle.
    private boolean hasChanged;
    // The score of this patch.
    private double score;
    // The neighbours on the playing field for this patch.
    private List<Patch> neighbours;

    /**
     * This constructor sets up a standard patch. This is done by using the full patch constructor with hasChanged as
     * false. It requires the x and y coordinates and strategy for the patch.
     */
    Patch(int x, int y, boolean cooperating) {
        this(x, y, cooperating, false);
    }

    /**
     * This is the full constructor for a patch. It requires the x and y coordinates, the strategy for the patch and if
     * the patch has changed last step.
     */
    private Patch(int x, int y, boolean cooperating, boolean hasChanged) {
        this.x = x;
        this.y = y;
        this.cooperating = cooperating;
        this.hasChanged = hasChanged;
        this.score = 0;
    }

    /**
     * Returns the list of neighbouring patches.
     */
    List<Patch> getNeighbours() {
        return this.neighbours;
    }

    /**
     * Sets the list of neighbouting patches.
     */
    void setNeighbours(List<Patch> neighbours) {
        this.neighbours = neighbours;
    }

    // returns true if and only if patch is cooperating
    boolean isCooperating() {
        return this.cooperating; // CHANGE THIS
    }

    // set strategy to C if isC is true and to D if false
    void setCooperating(boolean cooperating) {
        if (this.cooperating != cooperating) {
            this.hasChanged = true;
        }
        this.cooperating = cooperating;
    }

    /**
     * Returns the old cooperating strategy.
     */
    boolean isOldCooperating() {
        return this.oldCooperating;
    }

    /**
     * Sets if the patch has changed.
     */
    void setHasChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }

    // change strategy from C to D and vice versa
    private void toggleStrategy() {
        this.cooperating = !this.cooperating;
        this.hasChanged = true;
    }

    /**
     * Draws the patch by setting the background color of the JPanel.
     */
    void draw() {
        this.setBackground(this.getColor());
    }

    /**
     * Returns the color for this patch, depending on if it is cooperating and if it has changed in the last step.
     */
    private Color getColor() {
        if (this.cooperating) {
            return this.hasChanged ? Color.CYAN : Color.BLUE;
        } else {
            return this.hasChanged ? Color.ORANGE : Color.RED;
        }
    }

    /**
     * Returns the x coordinate of the patch.
     */
    int getFieldX() {
        return this.x;
    }

    /**
     * Returns the y coordinate of the patch.
     */
    int getFieldY() {
        return this.y;
    }

    // return score of this patch in current round
    double getScore() {
        return this.score;
    }

    /**
     * Calculates the new score for this patch depending on the given alpha.
     */
    void calculateScore(double alpha) {
        this.score = this.neighbours.stream().filter(Patch::isCooperating).count();
        if (!isCooperating()) {
            this.score *= alpha;
        }
        this.oldCooperating = this.cooperating;
    }

    /**
     * Is called when the mouse is pressed on a patch. Is not used as mousePressed is more accurate.
     */
    @Override
    public void mouseClicked(MouseEvent e) { }

    /**
     * Is called when a mouse button is pressed while the mouse is on a patch.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        toggleStrategy();
        draw();
        repaint();
    }

    /**
     * Is called when a mouse button is released while the mouse is on a patch. Is not used as mousePressed is more
     * accurate.
     */
    @Override
    public void mouseReleased(MouseEvent e) { }

    /**
     * Is called when the mouse enters a patch.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        if (e.isShiftDown()) {
            setCooperating(false);
            draw();
        } else if (e.isControlDown()) {
            setCooperating(true);
            draw();
        }
    }

    /**
     * Is called when the mouse exists a patch.
     */
    @Override
    public void mouseExited(MouseEvent e) { }
}
