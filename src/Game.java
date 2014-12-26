/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {	    

		// Top-level frame in which game components live
		final JFrame frame = new JFrame("CIS 120 Wars");
		frame.setLocation(10, 10);

		// Status panel
		final JPanel status_panel = new JPanel();
		frame.add(status_panel, BorderLayout.CENTER);
		
		final JLabel status = new JLabel("Running...Green to move");
		status_panel.add(status);

        // Shows green count
        final JPanel green_count = new JPanel();
        frame.add(green_count, BorderLayout.WEST);
        
        final JLabel gCount = new JLabel("Green troops: " );
        green_count.add(gCount);

        // Shows red count
        final JPanel red_count = new JPanel();
        frame.add(red_count, BorderLayout.EAST);
        
        final JLabel rCount = new JLabel("Red troops: " );
        red_count.add(rCount);
        
		// Main playing area
		final GameCourt court = new GameCourt(status, gCount, rCount);
		frame.add(court, BorderLayout.SOUTH);
        
		// Reset button
		final JPanel control_panel = new JPanel();
		frame.add(control_panel, BorderLayout.NORTH);

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		final JButton reset = new JButton("New Battle");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				court.reset();
			}
		});
		control_panel.add(reset);
		
		final JButton instrucs = new JButton("Instructions");
		instrucs.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e){
		        JOptionPane.showMessageDialog(frame,
		                "This is a turn-based game."
		                + "\nClick units if you want to move them."
		                + "\nHit arrows to determine their path."
		                + "\nClick Execute to execute your commands.");
		    }
	    });
		control_panel.add(instrucs);
		
		// execute button to execute moves and switch turns between players
		final JButton exec = new JButton("Execute");
		exec.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		        for (TroopObj t : court.getTroopUnits()) {
		            court.move(t);
		        }
		        court.side = !court.side;
		        if (court.side) {
		            status.setText("Running...Green to move.");
		        } else {
		            status.setText("Running...Red to move.");
		        }
		    }
		});
		control_panel.add(exec);

		// Put the frame on the screen
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
		frame.setVisible(true);

		// Start game
		court.reset();
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Game());
	}
}
