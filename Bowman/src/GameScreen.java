import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
/**
 * 
 */
// Main class which the game takes place in
public class GameScreen {
	// Create JPanels to add to the frame
	static JPanel screen;
	static JPanel topPanel;
	static JPanel userPanel;
	static JPanel enemyPanel;
	static JPanel roundPanel;
	// Create a Mouse Handler
	public static MouseClass handler;
	// Create the user and the opponent 
	Player user;
	Enemy enemy;
	// Create JLabels to display game information
	static JLabel playerScore, enemyScore , distanceLabel, angleLabel,roundLabel;
	// Create a custom color to match the background of the game
	Color lightBlue;
	// Store the users input
	static double power;
	static double angle;
	// Boolean to determine when the round is over
	Boolean round;

	GameScreen(JFrame window, int roundCounter, GamePanel panel) throws InterruptedException {
		// Initialize the booleans for a new round
		GamePanel.firstMove = true;
		round = true;
		// Create the custom color
		lightBlue = new Color (39,91,158);
		// Create all the required panels
		screen = panel;
		topPanel = new JPanel();
		userPanel = new JPanel();
		enemyPanel = new JPanel();
		roundPanel = new JPanel();
		// Change the layouts
		topPanel.setLayout(new BorderLayout());
		window.setLayout(new BorderLayout());
		screen.setLayout(new BorderLayout());
		userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.PAGE_AXIS));

		
		// Add the panels
		topPanel.add(roundPanel, BorderLayout.CENTER);
		topPanel.add(userPanel,BorderLayout.WEST);
		topPanel.add(enemyPanel, BorderLayout.EAST);
		topPanel.setBackground(lightBlue);
		screen.add(topPanel,BorderLayout.NORTH);
		window.add(screen,BorderLayout.CENTER);
		window.setVisible(true);
		window.	setResizable(false);
		window.setSize(1200, 700);
		window.	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Initialize and add the mouse handler
		handler = new MouseClass();
		screen.addMouseListener((MouseListener) handler);
		screen.addMouseMotionListener((MouseMotionListener) handler);
		// Create the user and the enemy
		user = new Player();
		enemy = new Enemy(roundCounter);
		// Initialize and add all the labels to the screen
		playerScore = new JLabel("Player: " + String.valueOf(user.getHealth()));
		enemyScore = new JLabel("Enemy: " + String.valueOf(enemy.getHealth()));
		distanceLabel = new JLabel ("Power: ");
		angleLabel = new JLabel ("Angle: ");

		roundLabel = new JLabel();
		roundLabel.setText("Round: " + roundCounter);
		roundPanel.setBackground(lightBlue);

		playerScore.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		enemyScore.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		roundLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
		distanceLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));
		angleLabel.setFont(new Font("Times New Roman", Font.BOLD, 28));

		userPanel.add(playerScore);
		userPanel.add(distanceLabel);
		userPanel.add(angleLabel);
		userPanel.setBackground(lightBlue);

		enemyPanel.add(enemyScore);
		enemyPanel.setBackground(lightBlue);

		roundPanel.add(roundLabel);
		
		// Remove the top panel if it is not in Game Play yet
		if(MainClass.option==1 || MainClass.option == 2 || MainClass.option == 3 || MainClass.option == 6)
			screen.remove(topPanel);
		// Loop to run the round
		while(round){
			// If the online mode was pressed
			if(MainClass.option==5){
				round = false;
				MainClass.win = false;
			}
			// If it is the users turn
			if (GamePanel.moving == 0){
				// Send the users move
				user.makeMove();
				//for some reason the code doesnt work without this
				System.out.println();
				// Check to see if enemy has died yet
				if (enemy.getHealth()<=0){
					// Exit loop
					round = false;
					// Remove the panels
					topPanel.removeAll();
					screen.removeAll();
					// Go to the in between screen
					MainClass.option=6;
					window.repaint();
					MainClass.win = true;
				}
			}
			// If it is the enemy's turn
			else if (GamePanel.moving == 5){
				// Send the enemy's move
				Enemy.makeMove(power,angle);
				// Check to see if user has died yet
				if (user.getHealth()<=0){
					// Exit loop
					round = false;
					// Remove the panels
					topPanel.removeAll();
					screen.removeAll();
					window.remove(screen);
					screen = null;
					// Set the main class boolean win to false
					MainClass.win = false;
					// Restart the round counter
					roundCounter = 1;
					MainClass.roundCounter = 1;
					// Go to the game over screen
					MainClass.option=2;
				}
			}
		}
		// Reset all the static variables
		GamePanel.moving = 0;
		GamePanel.changeArrow = 0;
		GamePanel.changeX = 0;
		GamePanel.arrowX = 0;
		GamePanel.arrowY = 660;
		// Remove all the panels
		window.remove(topPanel);
		window.remove(userPanel);
		window.remove(enemyPanel);
		window.remove(roundPanel);

		// Delete the user, enemy and unwanted panels
		user = null;
		enemy = null;
		userPanel = null;
		enemyPanel = null;
		roundPanel = null;
		topPanel = null;
	}

	/*
	 * @param: void
	 * @return: void
	 * Checks to see if enemy has hit the player and deduct health based on where was hit
	 * Updates the health of the player on the screen
	 */
	public static boolean collisionEnemy(){
		Rectangle  temp = new Rectangle  (GamePanel.arrowX,GamePanel.arrowY,18,7);
		if (temp.intersects(Player.getBoundsHead())){
			Player.deductHealth(20);
			playerScore.setText("Player: " + String.valueOf(Player.getHealth()));
			return true;
		}
		else if (temp.intersects(Player.getBoundsArms())){
			Player.deductHealth(5);
			playerScore.setText("Player: " + String.valueOf(Player.getHealth()));
			return true;
		}
		else if(temp.intersects(Player.getBoundsBody())){
			Player.deductHealth(15);
			playerScore.setText("Player: " + String.valueOf(Player.getHealth()));
			return true;
		}
		else if(temp.intersects(Player.getBoundsLegs())){
			Player.deductHealth(8);
			playerScore.setText("Player: " + String.valueOf(Player.getHealth()));
			return true;
		}
		return false;	
	}

	/*
	 * @param: void
	 * @return: void
	 * Checks to see if player has hit the enemy and deduct health based on where was hit
	 * Updates the health of the enemy on the screen
	 */
	public static boolean collision ( ){
		Rectangle  temp = new Rectangle  (GamePanel.arrowX+32,GamePanel.arrowY,18,7);
		if (temp.intersects(Enemy.getBoundsHead())){
			Enemy.deductHealth(20);
			enemyScore.setText("Enemy: " + String.valueOf(Enemy.getHealth()));
			return true;
		}
		else if (temp.intersects(Enemy.getBoundsArms())){
			Enemy.deductHealth(5);
			enemyScore.setText("Enemy: " + String.valueOf(Enemy.getHealth()));
			return true;
		}
		else if(temp.intersects(Enemy.getBoundsBody())){
			Enemy.deductHealth(15);
			enemyScore.setText("Enemy: " + String.valueOf(Enemy.getHealth()));
			return true;
		}
		else if(temp.intersects(Enemy.getBoundsLegs())){
			Enemy.deductHealth(8);
			enemyScore.setText("Enemy: " + String.valueOf(Enemy.getHealth()));
			return true;
		}
		return false;	
	}


}
