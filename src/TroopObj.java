import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.LinkedList;


public class TroopObj {
    private int steps;
    private int stepsLeft;
    private LinkedList<KeyEvent> moves;
    private boolean leftSide;
    private int xpos;
    private int ypos;
    private int troops;
    private int attack;
    private int defense;
    private Color color;

    TroopObj(int steps, boolean side, int xpos,
            int ypos, int troops, int att, int def, Color c) {
        this.steps = steps;
        this.stepsLeft = steps;
        moves = new LinkedList<KeyEvent>();
        this.leftSide = side;
        this.xpos = xpos;
        this.ypos = ypos;
        this.troops = troops;
        this.attack = att;
        this.defense = def;
        this.color = c;
    }
    
    public int getStepsLeft() {
        return stepsLeft;
    }
    
    public void decrStepsLeft() {
        stepsLeft--;
    }
    
    public void resetSteps() {
        stepsLeft = steps;
    }
    
    public void addMoves(KeyEvent e) {
        moves.add(e);
    }
    
    public LinkedList<KeyEvent> getMoves() {
        return moves;
    }
    
    public void setMoves(LinkedList<KeyEvent> newM) {
        moves = newM;
    }
    
    public boolean getSide() {
        return leftSide;
    }
    
    public int getX() {
        return xpos;
    }
    
    public void setX(int newX) {
        xpos = newX;
    }
    
    public int getY() {
        return ypos;
    }
    
    public void setY(int newY) {
        ypos = newY;
    }
    
    public int getTroops() {
        return troops;
    }
    
    private void setTroops(int newT) {
        System.out.println(
                troops - newT +
                " subtracted from " + troops + " " + leftSide + " troops");
        troops = newT;
    }

    public int getDef() {
        return defense;
    }

    public Color getColor() { return color; }

    public void clearMoves() {
        moves.clear();
    }
        
    /* subtracts troops from a unit based on attacking unit's attack
     * and own defense. If the unit is dead, do nothing*/
    public void attackUnit(TroopObj t) {
        //noinspection StatementWithEmptyBody
        if (troops > 0) {
            int loss = attack*25 - t.getDef()*15;
            t.setTroops(t.getTroops() - loss);
        }
        else {}
    }
    
    //draws the unit
    public void draw(Graphics g) {
        if (troops > 100 ) {
        g.setColor(color);
        }
        //if troop count is low, sets unit to a different color, based on side
        else if (leftSide) {
            g.setColor(new Color(154,205,50));
        }
        else {
            g.setColor(Color.PINK);
        }

        g.fillOval(xpos*20, ypos*20, 20, 20);
    }
}
