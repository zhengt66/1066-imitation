import java.awt.Color;
import java.awt.Graphics;

public class GhostUnit extends TroopObj {
    
    private Color c;
    private int xpos;
    private int ypos;

    GhostUnit(int steps, boolean side, int xpos, int ypos) {
        super(steps, side, xpos, ypos, 1000, 0, 0, Color.WHITE);
        this.c = Color.WHITE;
        this.xpos = xpos;
        this.ypos = ypos;
    }
    
    @Override
    public void draw(Graphics g) {
        g.setColor(c);
        g.fillOval(xpos*20, ypos*20, 20, 20);
    }
}
