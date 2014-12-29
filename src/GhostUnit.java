import java.awt.Color;
import java.awt.Graphics;

public class GhostUnit extends TroopObj {

    private Color c;
    private int xpos;
    private int ypos;

    GhostUnit(int steps, boolean side, int xpos, int ypos, Color c) {
        super(steps, side, xpos, ypos, 1, 0, 0, c);
        this.c = c;
        this.xpos = xpos;
        this.ypos = ypos;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        g.fillOval(xpos*20, ypos*20, 20, 20);
    }
}
