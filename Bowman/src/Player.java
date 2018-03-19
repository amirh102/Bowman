import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 */

/**
 * @author Amir
 *
 */
//Player class for single player and online mode
public class Player {
	static Point position ;
	static double health;
	// Constructor for single player mode
	Player(){
		int rand = (int)Math.ceil((Math.random()*518)+20);
		position = new Point (rand,530);
		health = 100	;
	}
	// Constructor for online mode
	Player(int num){
		position = new Point (num,530);
		health = 100;
	}
	// Method to get the angle and power of the shot
	static void makeMove() throws InterruptedException{

		if (GamePanel.isReady && MouseClass.lineFlag == false)	{
			double power = GamePanel.distance(GamePanel.x1,GamePanel.y1,GamePanel.x2, GamePanel.y2);
			double angle = GamePanel.getAngle(GamePanel.x1, GamePanel.y1,GamePanel.x2, GamePanel.y2);
			if (MainClass.option == 4){
				GameScreen.power = power;
				GameScreen.angle = angle;
			}
			// Send the power and angle to opponent if online
			if (MainClass.option == 5){
				OnlineGame.sendMessage(power, angle);
			}
			// Shoot the arrow
			shoot(power,angle);
			// Set back to opponnents turn
			GamePanel.isReady = false;
			GamePanel.moving = 5;
			GamePanel.firstMove = false;

		}


	}
	/*
	 * @param: The power and the angle of the shot
	 * @return: Void
	 * This method is used to shoot the arrow if the opponent is the player
	 */
	static void makeMove(double power, double angle) throws InterruptedException{
		Thread.sleep(600);


		shoot(power,angle);

		GamePanel.moving=5;
	}
	/*
	 * @param: Void
	 * @return: Void
	 * These methods are used to get the X and Y of the position which the player is at
	 */
	public static int getX(){
		return (int) position.getX();
	}

	public static int getY(){
		return (int) position.getY();
	}
	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to get the players health
	 */
	public static double getHealth(){
		return health;
	}
	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to deduct the players health
	 */
	public static void deductHealth(double num){
		if (health - num < 0)
			health = 0;
		else
			health -= num;
	}
	/*
	 * @param: The power and the angle of the shot
	 * @return: Void
	 * This method is used to calculate the x and y co-ordinates of the arrow in projectile motion
	 */
	public static void shoot(double power, double angle) {
		// Set the arrow as moving
		GamePanel.moving=1;
		// Reset all the variables
		GamePanel.changeX = 0;
		// Create variables to use in formulas
		double range =0;
		double frameCount = 0;
		int frame = 0;
		double g=9.81;
		double y=0;
		double x=0;
		double time=0;
		double t=0;
		double maxRange;
		// Calculate the max range base on if player is online or not
		if (MainClass.option==5){
			maxRange = EnemyOnline.position.getX()-position.getX();
		}
		else
			maxRange = Enemy.position.getX()-position.getX();

		// Range is the maximum distance that the arrow can go
		range= ((Math.pow(power,2))*( 2 * ((Math.sin(Math.toRadians(angle)))*(Math.cos(Math.toRadians(angle)))))) / g ;   
		// Calculate the time
		time=(2*power*Math.sin(Math.toRadians(angle)))/g;
		GamePanel.changeX = 0 ;	
		// Calculate how many frames will be used
		frameCount = (range/maxRange) * 150;
		frame = (int) frameCount;
		// Create a while loop to increase the x and y of the arrow
		while (t<time){
			// To move the screen to follow the arrow
			if (GamePanel.changeX+20 > 1800)
				GamePanel.changeX = 1800;
			else
				GamePanel.changeX+= 20;

			// Calculate the x and y of the arrow
			x=power*t*Math.cos(Math.toRadians(angle));
			y=(power*t*Math.sin(Math.toRadians(angle)))-((g*Math.pow(t,2)))/2;

			// Set the x and y to the game panel
			GamePanel.arrowY= (int)(660+(y*-1));
			GamePanel.arrowX= (int) x;

			// Return if arrow hits the ground
			if (GamePanel.arrowY>660)
				return;

			// Stop moving arrow if there is a collision
			if (MainClass.option == 4){
				if (GameScreen.collision()==true){
					t= time+1;
					GamePanel.changeArrow = 0;
					GamePanel.moving = 2;
				}
			}
			else if (MainClass.option == 5){
				if (OnlineGame.collision()==true){
					t= time+1;
					GamePanel.changeArrow = 0;
					GamePanel.moving = 2;
				}
			}
			// Add to the frames
			t=t+time/frame;
			// Delay to create an animation effect
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Set the animation as only the camera moves
		GamePanel.changeArrow = 0;
		GamePanel.moving = 2;
		// Calculate the movement of the camera left
		while (GamePanel.changeX < 1800){
			if (GamePanel.changeX+20 > 1800){
				GamePanel.changeArrow += 1800 - GamePanel.changeX; 
				GamePanel.changeX = 1800;
			}
			else{
				GamePanel.changeX+= 20;
				GamePanel.changeArrow+=20;
				try {
					Thread.sleep(15);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (GamePanel.moving == 1)
			GamePanel.changeArrow = 0;
	}
	/*
	 * @param: Void
	 * @return: Void
	 * These methods are used to get the boundaries of the different parts of the player to deduct health points based on where was hit 
	 */
	public static Rectangle getBoundsHead(){
		if (GamePanel.moving == 3)
			return new Rectangle((int)position.getX()+12-1800+GamePanel.changeX,(int)position.getY()+3,43,44);
		return new Rectangle((int)position.getX()+12,(int)position.getY()+3,43,42);
	}
	public static Rectangle getBoundsArms(){
		if (GamePanel.moving == 3)
			return new Rectangle((int)position.getX()+3-1800+GamePanel.changeX,(int)position.getY()+47,79,8);
		return new Rectangle((int)position.getX()+3,(int)position.getY()+47,79,8);
	}
	public static Rectangle getBoundsBody(){
		if (GamePanel.moving == 3)
			return new Rectangle((int)position.getX()+28-1800+GamePanel.changeX,(int)position.getY()+48,15,46);
		return new Rectangle((int)position.getX()+28,(int)position.getY()+48,15,46);

	}
	public static Rectangle getBoundsLegs(){
		if (GamePanel.moving == 3)
			return new Rectangle((int)position.getX()+20-1800+GamePanel.changeX,(int)position.getY()+91,29,44);
		return new Rectangle((int)position.getX()+20,(int)position.getY()+91,29,44);
	}
}
