import java.awt.Point;
import java.awt.Rectangle;

// Enemy class for the single player mode
public class Enemy{
	 // Shots get more accurate based on round number
	static int round;
	// Point to determine the position
	static Point position;
	// Health
	static double health;

	Enemy(int round){
		// Get the round number
		this.round = round-1;
		// Set the health as 100
		health = 100;
		// Find a random position for the enemy between the set boundaries
		int rand = (int)Math.ceil((Math.random()*518)+2370);
		// Set the enemy's position
		position = new Point (rand,530);
	}
	
	

	// Method to calculate the enemy's shot
	static void makeMove(double power, double angle){
		// Delay to show users shot
		  try {
		   Thread.sleep(600);
		  } catch (InterruptedException e) {
		   e.printStackTrace();
		  }
		  // Variable for gravity and maximum range of the arrow
		  double g=9.81;
		  double maxRange = 1360-Player.position.getX();
		  // Initialize the angle, range and power
		  double angle2=10;
		  double range2=0;
		  double power2 = 100;
		  // Calculate the range
		  range2 = ((Math.pow(power2,2))*( 2 * ((Math.sin(Math.toRadians(angle2)))*(Math.cos(Math.toRadians(angle2)))))) / g ; 
		  // While loop to run through the range and angle and go up by 0.01 until angle needed to hit target is found
		  while ( (maxRange - range2)>=0.01){
		    angle2 +=0.01;
		    range2= ((Math.pow(power2,2))*( 2 * ((Math.sin(Math.toRadians(angle2)))*(Math.cos(Math.toRadians(angle2)))))) / g ;    
		    // If angle surpassed 90, restarts
		    if (angle2>=90) {
		      power2+=10;
		      angle2=10;
		      range2=0;
		    }      
		  }
		  // Randomize the angle and power needed to hit the user
		  power2 = Math.random()*10 +(power2-5);
		  angle2 = Math.random()*10+(angle2-5);
		  // Shoot the arrow
		shoot(power2,angle2);
		// Set it back to users turn
		GamePanel.moving=0;
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
		double temp;
		double frameCount = 0;
		int frame = 0;
		double range =0;
		double g=9.81;
		double y=0;
		double x=0;
		double time=0;
		double t=0;
		double maxRange;
		// Calculate the max range depending if the game is online or not
		if (MainClass.option==5){
			 maxRange = EnemyOnline.position.getX()-position.getX();
			}
		else{
			// Randomize the perfect range needed to hit the user
			// Decrease the margins as the rounds increase
			temp =  (Math.random()*(1500-(90*round))+(610+(90*round)));
			maxRange = temp-Player.position.getX();
		}
		// Range is the maximum distance that the arrow can go
		range= ((Math.pow(power,2))*( 2 * ((Math.sin(Math.toRadians(angle)))*(Math.cos(Math.toRadians(angle)))))) / g ;   
		// Calculate the time
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
			if (GameScreen.collisionEnemy()==true){
				t= time+1;
				GamePanel.moving = 4;
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
	 * These methods are used to get the x and y values of the Enemy
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
			return new Rectangle((int)position.getX()+36-(GamePanel.changeX),(int)position.getY()+47,15,53);
		return new Rectangle((int)position.getX()+36,(int)position.getY()+47,15,53);

	}
	public static Rectangle getBoundsLegs(){
		if (GamePanel.moving == 1)
			return new Rectangle((int)position.getX()+30-(GamePanel.changeX),(int)position.getY()+96,29,36);
		return new Rectangle((int)position.getX()+30,(int)position.getY()+96,29,36);
	}
}
