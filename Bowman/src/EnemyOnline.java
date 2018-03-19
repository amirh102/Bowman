import java.awt.Point;
import java.awt.Rectangle;

public class EnemyOnline {
	// Point to determine the position
		static Point position;
		// Health
		static double health;


	EnemyOnline(int num){
		// Set the health as 100
		health = 100;
		// Set the enemy's position
		position = new Point (num,530);
	}
	
	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to get the health of the enemy
	 */
	public static double getHealth(){
		return health;
	}
	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to deduct the health of the enemy
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
	 * This method is used to shoot the arrow if the opponent is enemy
	 */
	static void makeMove(double power, double angle) throws InterruptedException{
			Thread.sleep(600);

		shoot(power,angle);
		
		GamePanel.moving=0;
	}
	/*
	 * @param: Void
	 * @return: Void
	 * This method is used to shoot the arrow if the user is enemy
	 */
	static void makeMove() throws InterruptedException{
			// Get the power and the angle of the line
			if (GamePanel.isReady && MouseClass.lineFlag == false)	{
				double power = GamePanel.distance(GamePanel.x1,GamePanel.y1,GamePanel.x2, GamePanel.y2);
				double angle = GamePanel.getAngle(GamePanel.x1, GamePanel.y1,GamePanel.x2, GamePanel.y2);
				// Send it to the opponnent
				if (MainClass.option == 5){
					OnlineGame.sendMessage(power, angle);
				}
				// Shoot the arrow and set it back to the opponnents turn
				shoot(power,angle);
				GamePanel.isReady = false;
				GamePanel.moving = 0;
				GamePanel.firstMove = false;
			}
	}
	/*
	 * @param: The power and angle of the arrow being shot
	 * @return: Void
	 * This method is used to calculate the x and y co-ordinates of the arrow in projectile motion
	 */
	public static void shoot(double power, double angle){
		// Set the angle as moving
		GamePanel.moving=3;
		// Reset the change variables
		GamePanel.changeX = 0;
		GamePanel.changeArrow = 0;
		// Create variables to use in formulas
		double frameCount = 0;
		int frame = 0;
		double range =0;
		double g=9.81;
		double y=0;
		double x=0;
		double time=0;
		double t=0;
		double maxRange = position.getX()-Player.position.getX();

		// Range is the maximum distance that the arrow can go
		range= ((Math.pow(power,2))*( 2 * ((Math.sin(Math.toRadians(angle)))*(Math.cos(Math.toRadians(angle)))))) / g ;   
		time=(2*power*Math.sin(Math.toRadians(angle)))/g;
		double xr=1200;

		t=0;
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
			x=xr-x;
			// Set the x and y to the game panel
			GamePanel.arrowY = (int)(660+(y*-1));
			GamePanel.arrowX = (int) x;
			// Return if arrow hits the ground
			if (GamePanel.arrowY>660)
				return;
			// Stop moving arrow if there is a collision
			if (OnlineGame.collisionEnemy()==true){
				t= time+1;
				GamePanel.moving = 4;
			}
			// Add to the frames
			t+=time/frame;
			// Delay to create an animation effect
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Set the animation as only the camera moves
		GamePanel.moving = 4;
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
	}
	/*
	 * @param: Void
	 * @return: Void
	 * These methods are used to get the boundaries of the different parts of the enemy to deduct health points based on where was hit 
	 */
	public static Rectangle getBoundsHead(){
		if (GamePanel.moving == 1)
			return new Rectangle((int)position.getX()+26-(GamePanel.changeX),(int)position.getY()+4,43,44);
		return new Rectangle((int)position.getX()+26,(int)position.getY()+4,43,44);
	}
	public static Rectangle getBoundsArms(){
		if (GamePanel.moving == 1)
			return new Rectangle((int)position.getX()+3-(GamePanel.changeX),(int)position.getY()+48,75,12);
		return new Rectangle((int)position.getX()+3,(int)position.getY()+48,75,12);
	}
	public static Rectangle getBoundsBody(){
		if (GamePanel.moving == 1)
			return new Rectangle((int)position.getX()+36-(GamePanel.changeX),(int)position.getY()+47,8,53);
		return new Rectangle((int)position.getX()+36,(int)position.getY()+47,8,53);

	}
	public static Rectangle getBoundsLegs(){
		if (GamePanel.moving == 1)
			return new Rectangle((int)position.getX()+30-(GamePanel.changeX),(int)position.getY()+96,29,36);
		return new Rectangle((int)position.getX()+30,(int)position.getY()+96,29,36);
	}
	
	
}
