import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

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

    private static final long serialVersionUID = 3868284208279483200L;
    private boolean cooperating;
    private boolean hasChanged;
    private double score;
    private Patch[] neighbours;

    Patch(boolean cooperating) {
        this(cooperating, false);
    }

    Patch(boolean cooperating, boolean hasChanged) {
        this.cooperating = cooperating;
        this.hasChanged = hasChanged;
        this.score = 0;
    }

    Patch[] getNeighbours() {
        return this.neighbours;
    }

    void setNeighbours(Patch[] neighbours) {
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

    // return score of this patch in current round
    double getScore() {
        return this.score;
    }

    void calculateScore(double alpha) {
        this.score = Arrays.stream(this.neighbours).filter(Patch::isCooperating).count();
        if (!isCooperating()) {
            this.score *= alpha;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {
        toggleStrategy();
        draw();
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

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
    public void mouseExited(MouseEvent e) {}
}
