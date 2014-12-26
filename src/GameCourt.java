/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;

/**
 * GameCourt
 * 
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 * 
 */
@SuppressWarnings("serial")
public class GameCourt extends JPanel {

    private JLabel status; // Current status text
    private JLabel gCount; // Displays current count of greens
    private JLabel rCount; // Displays current count of reds

    // Game constants
    public static final int COURT_WIDTH = 540;
    public static final int COURT_HEIGHT = 220;

    public boolean side = true; // determines which side has the turn

    private final int fieldXDim = COURT_WIDTH/20;
    private final int fieldYDim = COURT_HEIGHT/20;

    private TroopObj[][] battleField =
            new TroopObj[fieldXDim][fieldYDim];    
    private ArrayList<TroopObj> troopUnits = new ArrayList<TroopObj>();

    private int greens;
    private int reds;

    private int chosenX = -1;
    private int chosenY = -1;

    public GameCourt(final JLabel status, final JLabel gCount,
            final JLabel rCount) {
        
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);

        // Adds a mouselistener that determines the click's coords in the 2D
        // array.
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                chosenX = e.getX()/20;
                chosenY = e.getY()/20;

                if(battleField[chosenX][chosenY] instanceof StandardUnit) {
                    int troops = battleField[chosenX][chosenY].getTroops();

                    if (side) {
                        status.setText("Green to move. Selected unit's troop "
                                + "count: " + troops);
                    }
                    else {status.setText("Red to move. Selected unit's troop "
                            + "count: " + troops);
                    }
                }

                else {
                    if (side) {
                        status.setText("Running...Green to move.");
                    }
                    else {status.setText("Running...Red to move.");
                    }
                }

                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (chosenX > -1 && chosenY > -1 &&
                        battleField[chosenX][chosenY] != null) {
                    System.out.println(
                            "passed " + KeyEvent.getKeyText(e.getKeyCode())
                            + " to addMove");
                    addMove(battleField[chosenX][chosenY], side, e);
                }
            }
        });

        this.status = status;
        this.gCount = gCount;
        this.rCount = rCount;
    }

    public ArrayList<TroopObj> getTroopUnits() {
        return troopUnits;
    }

    public int getGreens() {
        return greens;
    }

    public int getReds() {
        return reds;
    }

    //adds move to troop unit's move queue
    private void addMove(TroopObj t, boolean side, KeyEvent e) {
        //prevents user from adding moves to a GhostUnit made from prior moves
        if (t instanceof GhostUnit) return;

        int steps = t.getStepsLeft();
        boolean onSide = t.getSide();
        //creates a GhostUnit copy of the unit, with the same coordinates
        GhostUnit g = new GhostUnit(steps, side, t.getX(),
                t.getY());
        g.setMoves(t.getMoves());

        if (onSide == side && steps != 0 && validMove(g, e) ) {
            moveParser(g, e);
            battleField[g.getX()][g.getY()] = g;
            repaint();

            t.addMoves(e);
            t.decrStepsLeft();
            System.out.println("added a move");
        }
    }

    /* checks if move dictated by arrow key is illegal
     * (if it is out of bounds or if move is onto an occupied square)
     * updates battleField if move is valid, for reference in later
     * calls of validMove*/
    private boolean validMove(TroopObj g, KeyEvent e) {
        
        //executes valid moves prior to the current move being evaluated;
        for (KeyEvent m : g.getMoves()) {
            moveParser(g, m);
        }

        int x = g.getX();
        int y = g.getY();

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (x - 1 < 0) return false;
            for (TroopObj to : troopUnits) {
                if (!(to instanceof GhostUnit) && 
                        (to.getX() == x - 1 && to.getY() == y)) return false;
            }
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (x + 1 >= fieldXDim) return false;
            for (TroopObj to : troopUnits) {
                if (!(to instanceof GhostUnit) && 
                        (to.getX() == x + 1 && to.getY() == y)) return false;
            }
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (y + 1 >= fieldYDim) return false;
            for (TroopObj to : troopUnits) {
                if (!(to instanceof GhostUnit) && 
                        (to.getX() == x && to.getY() == y + 1)) return false;
            }
            return true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (y - 1 < 0) return false;
            for (TroopObj to : troopUnits) {
                if (!(to instanceof GhostUnit) && 
                        (to.getX() == x && to.getY() == y - 1)) return false;
            }
            return true;
        }
        return false;
    }

    /*checks if troop unit t is adjacent to an enemy unit; attacks that unit if
     * it exists*/
    private void checkEnemies(TroopObj t) {
        int xpos = t.getX();
        int ypos = t.getY();
        boolean side = t.getSide();

        for (int x = xpos - 1; x < xpos + 2; x++) {
            for (TroopObj to : troopUnits) {
                if (to.getX() == x && to.getY() == ypos &&
                        to.getSide() != side) {
                    t.attackUnit(to);
                }
            }
        }

        for (int  y = ypos - 1; y < ypos + 2; y++) {
            for (TroopObj to : troopUnits) {
                if (to.getX() == xpos && to.getY() == y &&
                        to.getSide() != side) {
                    t.attackUnit(to);
                }
            }
        }
    }

    /* iterates through and executes each move in the LinkedList of moves;
     * checks enemies*/
    public void moveParser(TroopObj t, KeyEvent m) {

        int prevX = t.getX();
        int prevY = t.getY();

        if (m.getKeyCode() == KeyEvent.VK_LEFT) {
            t.setX(prevX -= 1);
        }
        else if (m.getKeyCode() == KeyEvent.VK_RIGHT) {
            t.setX(prevX += 1);
        }
        else if (m.getKeyCode() == KeyEvent.VK_DOWN) {
            t.setY(prevY += 1);
        }
        else if (m.getKeyCode() == KeyEvent.VK_UP) {
            t.setY(prevY -= 1);
        }
        else {
            System.out.println(m.getKeyCode() + " was in queue.");
            throw new IllegalArgumentException();
        }
        
    }


    public void move(TroopObj t) {

        if (t.getMoves().isEmpty()) {
            checkEnemies(t);
            repaint(); return;
        }
        

        for(KeyEvent m : t.getMoves()) {

            int prevX = t.getX();
            int prevY = t.getY();

            System.out.println("move method called moveParser");
            moveParser(t, m);

            int newX = t.getX();
            int newY = t.getY();
            
            if (prevX != newX || prevY != newY) {
                if (!(t instanceof GhostUnit)) {
                    battleField[prevX][prevY] = null;
                }
                battleField[newX][newY] = t;
            }
            
            if (!(t instanceof GhostUnit)) {
                checkEnemies(t);
                System.out.println("repainted after attacking");
            }

            repaint();
        }

        t.clearMoves();
        t.resetSteps();

    }   

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        
        troopUnits.clear();

        // makes new battlefield
        for (int x = 0; x < fieldXDim; x++) {
            for (int y = 0; y < fieldYDim; y++) {
                if (x > 0 && x < 11 && y > 3 && y < fieldYDim - 4) {
                    TroopObj newUnit =
                            new StandardUnit(true, x, y, Color.GREEN);
                    battleField[x][y] = newUnit;
                    troopUnits.add(newUnit);
                }
                else if (x > fieldXDim - 12 && x < fieldXDim - 1 && y > 3 && y
                        < fieldYDim - 4) {
                    TroopObj newUnit =
                            new StandardUnit(false, x, y, Color.RED);
                    battleField[x][y] = newUnit;
                    troopUnits.add(newUnit);
                }
                else {
                    battleField[x][y] = null;
                }
            }
        }

        side = true;
        status.setText("Running...Green to move.");
        repaint();

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);

        // draws area last clicked by mouse
        if (chosenX != -1 && chosenY != -1) {
            g.setColor(Color.ORANGE);
            g.drawRect(chosenX*20, chosenY*20, 20, 20);
        }

        // draws/redraws battlefield
        for (int x = 0; x < fieldXDim; x++) {
            for (int y = 0; y < fieldYDim; y++) {
                if (battleField[x][y] == null) {
                    g.setColor(Color.GRAY);
                    g.fillOval(x*20, y*20, 20, 20);
                }
                //cleans up dead units
                else if (battleField[x][y].getTroops() <= 0) {
                    troopUnits.remove(battleField[x][y]);
                    battleField[x][y] = null;
                    g.setColor(Color.GRAY);
                    g.fillOval(x*20, y*20, 20, 20);
                }
                else {
                    battleField[x][y].draw(g);
                }
            }
        }



        // displays scores and checks if a side has won
        int greenCounter = 0;
        int redCounter = 0;

        for (TroopObj t : troopUnits) {
            if (t.getSide()) {
                greenCounter += t.getTroops();
            } else {
                redCounter += t.getTroops();
            }
        }
        greens = greenCounter;
        reds = redCounter;
        gCount.setText("Green troops: " + greens);
        rCount.setText("Red troops: " + reds);

        if (greens == 0) {
            status.setText("Red wins. The field is theirs!");
        }
        else if (reds == 0) {
            status.setText("Green wins. The field is theirs!");
        }

        requestFocusInWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}
