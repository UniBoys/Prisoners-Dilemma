import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * INCOMPLETE
 * Assignment 6 -- Prisoner's Dilemma -- 2ip90
 * part Patch
 *
 * @author FILL IN
 * @author FILL IN
 * assignment group FILL IN
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
    // If this patch has changed in the last cycle.
    private boolean hasChanged;
    // The score of this patch.
    private double score;
    // The neighbours on the playing field for this patch.
    private List<Patch> neighbours;

    /**
     * This constructor
     */
    Patch(int x, int y, boolean cooperating) {
        this(x, y, cooperating, false);
    }

    Patch(int x, int y, boolean cooperating, boolean hasChanged) {
        this.x = x;
        this.y = y;
        this.cooperating = cooperating;
        this.hasChanged = hasChanged;
        this.score = 0;
    }

    List<Patch> getNeighbours() {
        return this.neighbours;
    }

    void setNeighbours(List<Patch> neighbours) {
        this.neighbours = neighbours;
    }

    // returns true if and only if patch is cooperating
    boolean isCooperating() {
        return this.cooperating; // CHANGE THIS
    }

    // set strategy to C if isC is true and to D if false
    private void setCooperating(boolean cooperating) {
        if (this.cooperating != cooperating) {
            this.hasChanged = true;
        }
        this.cooperating = cooperating;
    }

    // change strategy from C to D and vice versa
    private void toggleStrategy() {
        this.cooperating = !this.cooperating;
        this.hasChanged = true;
    }

    void draw() {
        this.setBackground(this.getColor());
        repaint();
    }

    private Color getColor() {
        if (this.cooperating) {
            return this.hasChanged ? Color.CYAN : Color.BLUE;
        } else {
            return this.hasChanged ? Color.ORANGE : Color.RED;
        }
    }

    public int getFieldX() {
        return x;
    }

    public int getFieldY() {
        return y;
    }

    // return score of this patch in current round
    double getScore() {
        return this.score;
    }

    void calculateScore(double alpha) {
        this.score = this.neighbours.stream().filter(Patch::isCooperating).count();
        if (!isCooperating()) {
            this.score *= alpha;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        toggleStrategy();
        draw();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

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

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
