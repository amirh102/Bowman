import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 */

/**
 * @author Amir
 *
 */

//NOTE: ALL FILE LOCATIONS FOR IMAGES MUST BE CHANGED TO GIVEN LOCATION FOR GAME TO WORK
//CHANGES SHOULD BE MADE IN GamePanel.java UNDER THE METHOD paintComponent
public class MainClass {

	// Boolean to determine if game will be continued to next round
	static boolean win;
	// Integer to determine which screen to show
	static int option;
	// Game window
	static JFrame window;
// Integer to count what round it is
	static int roundCounter;
	
	// Main method
	public static void main(String[] args) throws Exception {
		// Linked list of type GameScreen, GameScreen represents one round, it is added to the linked list when user wins
		LinkedList <GameScreen>  list= new LinkedList<GameScreen>();
		// Start the boolean as true to run it
		win = true;
		// Set round counter to 1
		roundCounter = 1;
		// Start at the main menu
		option = 1;
		// Create the window and set it up
		window = new JFrame("Bowman");
		window.setSize(1200, 700);
		window.	setResizable(false);
		window.	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		// Run the game in a loop other than the online mode (Option #5)
		while(option!=5){
			// Add the round to the linked list
			list.add(new GameScreen(window,roundCounter, new GamePanel()));
			// Remove the previous rounds to save memory
			for (int i = 0; i > roundCounter; i ++)
				list.remove(i);
			// If the round was won
			if (win){
				// Add one to the round counter
				roundCounter++;
				// Create a delay for the screen in between rounds
				Thread.sleep(3000);
				// Set option back to 4
				option=4;
				// Remove the previous GamePanel
				window.remove(GameScreen.screen);
				GameScreen.screen = null;

			}
		}
		// Remove the previous GamePanel for the online mode
		window.remove(GameScreen.screen);
		GameScreen.screen = null;
		
		int temp, port;
		String ipAddress = "";
		// Ask for and get if the user is the client/server, the IP address, and the port
		temp = Integer.valueOf(JOptionPane.showInputDialog("Are you the client(1) or server(2)?"));
		while (temp != 1 && temp != 2){
			temp = Integer.valueOf(JOptionPane.showInputDialog("Incorrect selection! Are you the client(1) or server(2)?"));
		}
		if (temp == 1 ){
			ipAddress = JOptionPane.showInputDialog("Please enter the server's IP address: ");
		}
		port = Integer.valueOf(JOptionPane.showInputDialog("Please enter your port: "));
		// Set up an online game
		new OnlineGame(window,temp,ipAddress,port,new GamePanel());
	}
}
