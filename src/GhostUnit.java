import java.awt.Color;

public class GhostUnit extends TroopObj {
    
    GhostUnit(int steps, boolean side, int xpos, int ypos) {
        super(steps, side, xpos, ypos, 1000, 0, 0, Color.WHITE);
    }
}
