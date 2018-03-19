
//Amir�s Imports          
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.*;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

// Class used to run the online game
public class OnlineGame implements Runnable {


	// Client = user || Option = 1
	// Server = enemy || Option = 2

	// Client/user goes first (sends x coordinate first)
	// Server/enemy goes second (receives then sends x coordinate)

	// Import networking variables
	ServerSocket serverSock;
	Socket mySocket; 
	BufferedReader input;
	InputStreamReader stream;
	Socket client;
	static PrintWriter output;
	static int option ;
	// Boolean to tell if the round has just started
	Boolean start = true, round = true;
	// Boolean to determine when to end the input thread
	static Boolean online;
	// Enemy an player objects
	Player user;
	EnemyOnline enemy;
	// JLabels to be added to the screen, custom color to match background and JPanels to add to the window
	static JLabel playerScore, enemyScore , distanceLabelPlayer,distanceLabelEnemy, angleLabelPlayer, angleLabelEnemy,roundLabel;
	Color lightBlue;
	static JPanel screen;
	static JPanel topPanel;
	static JPanel userPanel;
	static JPanel enemyPanel;
	static JPanel roundPanel;

	public OnlineGame (JFrame window,int num, String ipAddress, int port, GamePanel panel) throws InterruptedException, IOException{
		option = num;
		online = true;
		// If client is running
		if (option == 1) {
			// Try to establish a connection
			try {
				mySocket = new Socket(ipAddress, port); // attempt socket
				// connection
				stream = new InputStreamReader(mySocket.getInputStream()); // make
				// Stream
				input = new BufferedReader(stream);
				output = new PrintWriter(mySocket.getOutputStream());
				// Catch any exceptions
			} catch (IOException e) {
				System.out.println("Connection to Server Failed");
				e.printStackTrace();
			}
			// Give a connected message
			System.out.println("Connected");
		}
		// If the player is the server
		else if (option == 2) {

			// Try to establish a connection
			try {
				serverSock = new ServerSocket(port);
				client = serverSock.accept(); // wait for connection
				output = new PrintWriter(client.getOutputStream());
				stream = new InputStreamReader(client.getInputStream());
				input = new BufferedReader(stream);
				// System.out.println ("Connected");
			} 
			// Catch any exceptions
			catch (Exception e) {
				System.out.println("Error accepting connection");
				e.printStackTrace();
			}
			System.out.println("Connected");
		}
		// Start thread to input the opponents messages
		Thread inputThread = new Thread(this);
		inputThread.start();
		// Run the initial method to determine where the user and enemy are (random X co-ordinates)
		initialStart();

		// Code from GameScreen
		GamePanel.firstMove = true;
		round = true;
		lightBlue = new Color (39,91,158);
		screen = panel;
		topPanel = new JPanel();
		userPanel = new JPanel();
		enemyPanel = new JPanel();
		roundPanel = new JPanel();

		topPanel.setLayout(new BorderLayout());
		topPanel.add(roundPanel, BorderLayout.CENTER);

		topPanel.add(userPanel,BorderLayout.WEST);
		topPanel.add(enemyPanel, BorderLayout.EAST);
		topPanel.setBackground(lightBlue);
		window.setLayout(new BorderLayout());
		screen.setLayout(new BorderLayout());
		userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.PAGE_AXIS));
		enemyPanel.setLayout(new BoxLayout(enemyPanel,BoxLayout.PAGE_AXIS));

		screen.add(topPanel,BorderLayout.NORTH);
		window.add(screen,BorderLayout.CENTER);
		window.setVisible(true);
		window.	setResizable(false);
		window.setSize(1200, 700);

		window.	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		screen.addMouseListener((MouseListener) GameScreen.handler);
		screen.addMouseMotionListener((MouseMotionListener) GameScreen.handler);


		playerScore = new JLabel("Player One: " + String.valueOf(user.getHealth()));
		enemyScore = new JLabel("Player Two: " + String.valueOf(enemy.getHealth()));
		distanceLabelPlayer = new JLabel ("Power: ");
		angleLabelPlayer = new JLabel ("Angle: ");
		distanceLabelEnemy = new JLabel ("Power: ");
		angleLabelEnemy = new JLabel ("Angle: ");

		roundLabel = new JLabel();
		roundLabel.setText("ONLINE");

		roundPanel.setBackground(lightBlue);

		playerScore.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		enemyScore.setFont(new Font("Times New Roman", Font.ITALIC, 40));
		roundLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));

		distanceLabelPlayer.setFont(new Font("Times New Roman", Font.BOLD, 28));
		angleLabelPlayer.setFont(new Font("Times New Roman", Font.BOLD, 28));

		distanceLabelEnemy.setFont(new Font("Times New Roman", Font.BOLD, 28));
		angleLabelEnemy.setFont(new Font("Times New Roman", Font.BOLD, 28));


		userPanel.add(playerScore);
		if (option == 1){
			userPanel.add(distanceLabelPlayer);
			userPanel.add(angleLabelPlayer);
		}
		userPanel.setBackground(lightBlue);

		enemyPanel.add(enemyScore);
		if (option == 2){
			enemyPanel.add(distanceLabelEnemy);
			enemyPanel.add(angleLabelEnemy);
		}
		enemyPanel.setBackground(lightBlue);


		roundPanel.add(roundLabel);

		// While loop to run until someone has won
		while(round){
			if (option == 1){
				// Client
				// If the user is the client
				if (GamePanel.moving == 0){
					// User makes move
					Player.makeMove();
					// If the enemy has died
					if (enemy.getHealth()<=0){
						online = false;
						round = false;
						topPanel.removeAll();
						// Go to end screen for online games
						MainClass.option=7;
					}
				}
				// If it is the servers turn
				else if (GamePanel.moving == 5){
					// If user had died
					if (user.getHealth()<=0){
						online = false;
						round = false;
						topPanel.removeAll();
						// Go to end screen for online games
						MainClass.option=7;

					}
				}
			}
			// Server
			else if (option == 2){
				// If server is waiting for client to make the move
				if (GamePanel.moving == 0){
					// If enemy had died
					if (enemy.getHealth()<=0){
						online = false;
						round = false;
						topPanel.removeAll();
						// Go to end screen for online games
						MainClass.option=7;

					}
				}
				else if (GamePanel.moving == 5){
					// Enemy makes move
					GamePanel.firstMove = false;
					EnemyOnline.makeMove();
					// If user had died
					if (user.getHealth()<=0){
						online = false;
						round = false;
						topPanel.removeAll();
						// Go to end screen for online games
						MainClass.option=7;
					}
				}
			}
		}

		// Close networking variables once round is over
		if (option == 1){
			mySocket.close();
			stream.close();
			input.close();
			output.close();
		}
		else if (option == 2){
			serverSock.close();
			client.close();
			stream.close();
			input.close();
			output.close();
		}
		// Run the end screen
		while(MainClass.option == 7){
			window.remove(topPanel);
			window.repaint();
		}
		// Reset all static variables and delete the JPanels
		GamePanel.moving = 0;
		GamePanel.changeArrow = 0;
		GamePanel.changeX = 0;
		window.remove(screen);
		window.remove(topPanel);
		window.remove(userPanel);
		window.remove(enemyPanel);
		window.remove(roundPanel);

		user = null;
		enemy = null;


		screen = null;
		userPanel = null;
		enemyPanel = null;
		roundPanel = null;
		topPanel = null;

		GamePanel.arrowX = 0;
		GamePanel.arrowY = 660;
	}

	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to initialize the position of the user and enemy randomly and send it to each other
	 */
	public void initialStart (){
		int position;
		String msg = null;
		if (option == 1){
			position = (int)Math.ceil((Math.random()*518)+20);
			user = new Player(position);

			output.println(String.valueOf(position));
			output.flush();
			while (msg == null){
				try {				
					msg = input.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Client receiving position of: " + msg);

			enemy = new EnemyOnline(Integer.valueOf(msg));	
		}
		else if (option == 2){
			while (msg == null){
				try {

					msg = input.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			user = new Player(Integer.valueOf(msg));
			position = (int)Math.ceil((Math.random()*518)+2370);
			System.out.println("Server sending position of: " + position);
			for (int i = 0; i < 200; i ++){
				output.println(String.valueOf(position));
				output.flush();
			}
			enemy = new EnemyOnline(position);	
		}
		start = false;
	}
	/*
	 * @param: The power and angle being used
	 * @return: Void
	 * This method is used to tell the opponnent what move was just made
	 */
	public static void sendMessage(double power, double angle) throws InterruptedException {
		String message = String.valueOf(power);
		message += "#";
		message += String.valueOf(angle);
		// Check to see if it's our move again or not
		// Send the message
		output.println(message);
		output.flush();
	}


	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to run the thread to constantly check for messages being inputed
	 */
	@Override
	public void run(){
		String msg = null;
		double power;
		double angle;
		while (online){
			try {
				msg = input.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			if (msg == null){
				if (online == false){
					return;
				}
			}
			else if (msg.indexOf('#')== -1)
				msg = null;

			if (start == false && msg != null){
				// Get the power and angle from the string message
				power = Double.valueOf(msg.substring(0, msg.indexOf('#')));
				angle = Double.valueOf(msg.substring(msg.indexOf('#')+1));

				// Send the angle and power to its corresponding method
				if (option == 1){
					if (GamePanel.moving == 5){
						try {
							EnemyOnline.makeMove(power,angle);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						if (user.getHealth()<=0){
							round = false;
						}
					}
				}
				else if (option == 2){
					try {
						Player.makeMove(power,angle);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (enemy.getHealth()<=0){
						round = false;
					}
				}

			}
		}
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
			playerScore.setText("Player One: " + String.valueOf(Player.getHealth()));
			return true;
		}
		else if (temp.intersects(Player.getBoundsArms())){
			Player.deductHealth(5);
			playerScore.setText("Player One: " + String.valueOf(Player.getHealth()));
			return true;
		}
		//	temp.setBounds(GamePanel.arrowX,GamePanel.arrowY,38,7);
		else if(temp.intersects(Player.getBoundsBody())){
			Player.deductHealth(15);
			playerScore.setText("Player One: " + String.valueOf(Player.getHealth()));
			return true;
		}
		else if(temp.intersects(Player.getBoundsLegs())){
			Player.deductHealth(8);
			playerScore.setText("Player One: " + String.valueOf(Player.getHealth()));
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
		if (temp.intersects(EnemyOnline.getBoundsHead())){
			EnemyOnline.deductHealth(20);
			enemyScore.setText("Player Two: " + String.valueOf(EnemyOnline.getHealth()));
			return true;
		}
		else if (temp.intersects(EnemyOnline.getBoundsArms())){
			EnemyOnline.deductHealth(5);
			enemyScore.setText("Player Two: " + String.valueOf(EnemyOnline.getHealth()));
			return true;
		}
		else if(temp.intersects(EnemyOnline.getBoundsBody())){
			EnemyOnline.deductHealth(15);
			enemyScore.setText("Player Two: " + String.valueOf(EnemyOnline.getHealth()));
			return true;
		}
		else if(temp.intersects(EnemyOnline.getBoundsLegs())){
			EnemyOnline.deductHealth(8);
			enemyScore.setText("Player Two: " + String.valueOf(EnemyOnline.getHealth()));
			return true;
		}
		return false;	
	}
}
