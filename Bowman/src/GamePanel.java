import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
// The class where all the animations take place
public class GamePanel extends JPanel{
	// Initialize all the x and y co-ordinates of the different icons
	public static int x1 =0, y1=0, x2 =0, y2 =0;
	public static int arrowX = 0, arrowY = 660;
	int personX = 0;
	int personY = 0;

	int enemyX = 0;
	int enemyY = 0;

	int arrowXPos = 0;
	int arrowYPos = 0;
	// Initialize the isReady boolean as false
	public static Boolean isReady = false;
	// Variables to move the screen
	public static int moving = 0;
	public static int changeX = 0;
	public static int changeArrow = 0;
	// Images needed
	ImageIcon endScreen , menu, instructions,endScreenOnline,background, nextRound,arrowEnemy,enemy,person,arrow;
	// To rotate the first arrow
	static boolean firstMove = true;
	//NOTE: ALL FILE LOCATIONS FOR IMAGES MUST BE CHANGED TO GIVEN LOCATION FOR GAME TO WORK
	public void paintComponent(Graphics g) {
		super.repaint();
		// If it is in the main menu
		if (MainClass.option == 1){
			menu = new ImageIcon (this.getClass().getResource("/Icons/menu.png"));
			menu.paintIcon(this,g,0,0);
			g.setColor(Color.BLACK);

			// Instructions button
			g.drawRect(142, 375, 366, 114);
			// Online button
			g.drawRect(690, 375, 366, 114);

			// Play button
			g.drawRect(408, 539, 366, 114);

		}
		// If it is in the game over screen
		else if (MainClass.option == 2){
			endScreen = new ImageIcon (this.getClass().getResource("/Icons/endScreen.png"));
			endScreen.paintIcon(this,g,0,0);
			g.setColor(Color.BLACK);
			g.drawRect(61, 470, 442, 190);
			g.drawRect(690, 470, 442, 190);

		}
		// If it is in the instructions screen
		else if (MainClass.option == 3){
			instructions = new ImageIcon (this.getClass().getResource("/Icons/instructions.png"));
			instructions.paintIcon(this, g, 0, 0);
			g.setColor(Color.BLACK);
			g.drawRect(54, 570, 318, 89);
		}
		// If it is in the "in between" rounds screen
		else if (MainClass.option == 6){
			nextRound = new ImageIcon (this.getClass().getResource("/Icons/nextRound.png"));
			nextRound.paintIcon(this, g, 0, 0);
		}
		// If it is in the game over screen after an online game
		else if (MainClass.option == 7){
			endScreenOnline = new ImageIcon (this.getClass().getResource("/Icons/endScreenOnline.png"));
			endScreenOnline.paintIcon(this, g, 0, 0);
			g.setColor(Color.BLACK);
			g.drawRect(375, 461, 442, 190);
		}
		// If it is in the game
		else if (MainClass.option == 4 || MainClass.option == 5){
			arrow = new ImageIcon(this.getClass().getResource("/Icons/arrow.png"));
			person = new ImageIcon(this.getClass().getResource("/Icons/stick.png"));
			enemy = new ImageIcon (this.getClass().getResource("/Icons/enemy.png"));
			arrowEnemy = new ImageIcon (this.getClass().getResource("/Icons/arrowEnemy.png"));
			background = new ImageIcon (this.getClass().getResource("/Icons/background.png"));
			// If the user is supposed to shoot but has not shot their arrow yet
			if (moving == 0){
				personX = (int) Player.position.getX();
				personY = (int) Player.position.getY();
				// Different class for online game
				if (MainClass.option == 5){
					enemyX = (int)EnemyOnline.position.getX();
					enemyY = (int)EnemyOnline.position.getY();
				}
				else{
					enemyX = (int)Enemy.position.getX();
					enemyY = (int)Enemy.position.getY();
				}

				arrowXPos = arrowX+changeArrow;
				arrowYPos = arrowY;
			}
			// If the user shot their arrow and it is in motion
			else if (moving == 1 || moving == 2){
				personX = (int) Player.position.getX()-(changeX);
				personY = (int) Player.position.getY();
				if (MainClass.option == 5){
					enemyX = (int)EnemyOnline.position.getX()-(changeX);
					enemyY = (int)EnemyOnline.position.getY();
				}
				else{
					enemyX = (int)Enemy.position.getX()-(changeX);
					enemyY = (int)Enemy.position.getY();
				}
				if (moving == 2){
					arrowXPos = arrowX-changeArrow;
					arrowYPos = arrowY;
				}
				else{
					arrowXPos = arrowX;
					arrowYPos = arrowY;
				}
			}
			// If the enemy is supposed to shoot but has not shot their arrow yet
			else if (moving == 5){
				personX = (int) Player.position.getX()-1800;
				personY = (int) Player.position.getY();
				if (MainClass.option == 5){
					enemyX = (int)EnemyOnline.position.getX()-1800;
					enemyY = (int)EnemyOnline.position.getY();
				}
				else{
					enemyX = (int)Enemy.position.getX()-1800;
					enemyY = (int)Enemy.position.getY();
				}


				arrowXPos = arrowX-changeArrow;
				arrowYPos = arrowY;
			}
			// If the enemy shot their arrow and it is in motion
			else if (moving == 3 || moving == 4){
				personX = (int) Player.position.getX()-1800+changeX;
				personY = (int) Player.position.getY();
				
				if (MainClass.option == 5){
					enemyX = (int)EnemyOnline.position.getX()-1800+changeX;
					enemyY = (int)EnemyOnline.position.getY();
				}
				else{
					enemyX = (int)Enemy.position.getX()-1800+changeX;
					enemyY = (int)Enemy.position.getY();
				}
				

				if (moving == 4){

					arrowXPos = arrowX + changeArrow;
					arrowYPos = arrowY;
				}
				else{
					arrowXPos = arrowX;
					arrowYPos = arrowY;
				}
			}

			// Paint the background image
			background.paintIcon(this,g, 0,0);
			
			// Draw the line only when it is permitted by the lineFlag and isReady booleans
			if (MouseClass.lineFlag && isReady==false){
				g.setColor(Color.BLACK);
				g.drawLine(x1,y1,x2,y2);
			}

			// Paint the user and the enemy
			person.paintIcon(this, g, personX, personY);
			enemy.paintIcon(this, g, enemyX, enemyY);

			// Paint an arrow if the user has shot it
			if (moving == 5 || moving == 1 || moving == 2 || firstMove == true){
				arrow.paintIcon(this,g,arrowXPos,arrowYPos);
			}
			// Paint an arrow if the enemy has shot it
			else if (moving == 0 || moving == 3 || moving == 4)
				arrowEnemy.paintIcon(this,g,arrowXPos,arrowYPos);
		}
	}
	/*
	 * @param: The x and y co-ordinates of the line drawn
	 * @return: The distance of the line drawn
	 * This method is used to determine the power of the line
	 */
	public static double distance (int x1, int y1, int x2, int y2){
		double x = x2-x1;
		double y = y2-y1;
		x =  Math.pow(x,2);
		y = Math.pow(y,2);
		return (Math.sqrt(x+y));
	}
	/*
	 * @param: The x and y co-ordinates of the line drawn
	 * @return: The angle of the line drawn in relation to the x-axis
	 * This method is used to determine the angle of which the arrow will be shot in projectile motion
	 */
	public static double getAngle (int x1, int y1, int x2, int y2){
		double a = Math.abs(x1 - x2);
		double o = Math.abs(y2 - y1);
		double h = Math.sqrt(Math.pow(a, 2) + Math.pow(o, 2));
		double s = (o/h);
		double angle = Math.asin(s);
		angle = Math.toDegrees(angle);
		if (angle<0)
			angle=-1*angle;
		return (angle);
	}


}
