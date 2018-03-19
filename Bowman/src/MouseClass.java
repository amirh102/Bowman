import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.*;
// Class to handle all the mouse action listeners
public class MouseClass implements MouseListener,MouseMotionListener {
	// Set lineFlag as false
	public static boolean lineFlag = false;
	@Override
	public void mouseDragged(MouseEvent event) {
		// If it is during the single player game
		if (MainClass.option==4){
			double tempDistance, tempAngle;
			// If user has not shot yet
			if (GamePanel.moving==0){
				// Get the distance and angle and display it to the window
				tempDistance = GamePanel.distance(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
				tempAngle = GamePanel.getAngle(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
				tempDistance =  Math.round(tempDistance * 100.0) / 100.0;
				tempAngle = Math.round(tempAngle * 100.0) / 100.0;
				GameScreen.distanceLabel.setText("Power: " + tempDistance);
				GameScreen.angleLabel.setText("Angle: " + tempAngle);
				// Get the tempx and tempy as long as the angle is making 90 degrees
				int tempx, tempy;
				if (event.getX()<GamePanel.x1 && event.getY() > GamePanel.y1){
					tempx = event.getX();
					tempy = event.getY();
					// Set the maximum distance for the line
					if (GamePanel.distance(GamePanel.x1, GamePanel.y1,tempx, tempy)<=180){
						GamePanel.x2=event.getX();
						GamePanel.y2=event.getY();
						lineFlag= true;
					}
				}
			}
		}
		// If it is during the online game
		else if (MainClass.option==5){
			// If user is client
			if (OnlineGame.option == 1){
				double tempDistance, tempAngle;
				// If user has not shot yet
				if (GamePanel.moving==0){
					// Get the distance and angle and display it to the window
					tempDistance = GamePanel.distance(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
					tempAngle = GamePanel.getAngle(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
					tempDistance =  Math.round(tempDistance * 100.0) / 100.0;
					tempAngle = Math.round(tempAngle * 100.0) / 100.0;
					OnlineGame.distanceLabelPlayer.setText("Power: " + tempDistance);
					OnlineGame.angleLabelPlayer.setText("Angle: " + tempAngle);
					// Get the tempx and tempy as long as the angle is making 90 degrees
					int tempx, tempy;
					if (event.getX()<GamePanel.x1 && event.getY() > GamePanel.y1){
						tempx = event.getX();
						tempy = event.getY();
						// Set the maximum distance for the line
						if (GamePanel.distance(GamePanel.x1, GamePanel.y1,tempx, tempy)<=180){
							GamePanel.x2=event.getX();
							GamePanel.y2=event.getY();
							lineFlag= true;
						}
					}
				}
			}
			// If user is server
			else if (OnlineGame.option == 2){
				double tempDistance, tempAngle;
				// If user has not shot yet
				if (GamePanel.moving==5){
					// Get the distance and angle and display it to the window
					tempDistance = GamePanel.distance(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
					tempAngle = GamePanel.getAngle(GamePanel.x1, GamePanel.y1, GamePanel.x2, GamePanel.y2);
					tempDistance =  Math.round(tempDistance * 100.0) / 100.0;
					tempAngle = Math.round(tempAngle * 100.0) / 100.0;
					OnlineGame.distanceLabelEnemy.setText("Power: " + tempDistance);
					OnlineGame.angleLabelEnemy.setText("Angle: " + tempAngle);
					// Get the tempx and tempy as long as the angle is making 90 degrees
					int tempx, tempy;
					if (event.getX()>GamePanel.x1 && event.getY() > GamePanel.y1){
						tempx = event.getX();
						tempy = event.getY();
						// Set the maximum distance for the line
						if (GamePanel.distance(GamePanel.x1, GamePanel.y1,tempx, tempy)<=180){
							GamePanel.x2=event.getX();
							GamePanel.y2=event.getY();
							lineFlag= true;
						}
					}
				}
			}

		}
		

	}

	@Override
	public void mouseMoved(MouseEvent event) {

	}

	@Override
	public void mouseClicked(MouseEvent event) {
		// If it is in the main menu
		if (MainClass.option==1){
			GameScreen.screen.remove(GameScreen.topPanel);	
			Rectangle instructionsButton = new Rectangle (142, 375, 366, 114);
			Rectangle playButton = new Rectangle (408, 539, 366, 114);
			Rectangle onlineButton = new Rectangle (690, 375, 366, 114);
			// Start the game
			if (playButton.contains(event.getPoint())){
				MainClass.win=true;
				MainClass.option=4;
				GameScreen.topPanel.add(GameScreen.roundPanel, BorderLayout.CENTER);

				GameScreen.topPanel.add(GameScreen.userPanel,BorderLayout.WEST);
				GameScreen.topPanel.add(GameScreen.enemyPanel, BorderLayout.EAST);
				GameScreen.screen.add(GameScreen.topPanel,BorderLayout.NORTH);

				GameScreen.screen.repaint();

			}
			// Go to instructions screen
			else if (instructionsButton.contains(event.getPoint())){
				MainClass.option = 3;
			}
			// Play online
			else if (onlineButton.contains(event.getPoint())){
				MainClass.option = 5;
				MainClass.win=false;
			}
		}
		// If it is in the game over screen
		else if (MainClass.option==2){
				GameScreen.screen.remove(GameScreen.topPanel);	
			Rectangle returnButton = new Rectangle (61, 470, 442, 190);
			Rectangle exitButton = new Rectangle (690, 470, 442, 190);
			// Play again
			if (returnButton.contains(event.getPoint())){
				MainClass.win=true;
				MainClass.option=4;
				GameScreen.topPanel.add(GameScreen.roundPanel, BorderLayout.CENTER);

				GameScreen.topPanel.add(GameScreen.userPanel,BorderLayout.WEST);
				GameScreen.topPanel.add(GameScreen.enemyPanel, BorderLayout.EAST);
				GameScreen.screen.add(GameScreen.topPanel,BorderLayout.NORTH);
				GameScreen.screen.repaint();

			}
			// Exit the game
			else if (exitButton.contains(event.getPoint())){
				System.exit(0);
			}
		}
		// If it is in the instructions screen
		else if (MainClass.option==3){
			GameScreen.screen.remove(GameScreen.topPanel);	
			Rectangle returnButton = new Rectangle (54, 570, 318, 89);
			// Return to the menu
			if (returnButton.contains(event.getPoint())){
				MainClass.option=1;
			}
		}
		// If it is in the game over screen after an online game
		else if (MainClass.option==7){
			Rectangle exitButton = new Rectangle (375, 461, 442, 190);
			// Exit the game
			if (exitButton.contains(event.getPoint())){
				System.exit(0);
			}
		}


	}

	@Override
	public void mouseEntered(MouseEvent event) {

	}

	@Override
	public void mouseExited(MouseEvent event) {

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// If it is the single player game
		if (MainClass.option==4){
			// Get the x1 and y1
			if (GamePanel.moving==0){
				GamePanel.x1=event.getX();
				GamePanel.y1=event.getY();
			}
		}
		// If it is the online mode
		else if (MainClass.option==5){
			// Get the x1 and y1 for the client
			if (OnlineGame.option == 1){
				if (GamePanel.moving==0){
					GamePanel.x1=event.getX();
					GamePanel.y1=event.getY();
				}
			}
			// Get the x1 and y1 for the server
			else if (OnlineGame.option == 2){
				if (GamePanel.moving==5){
					GamePanel.x1=event.getX();
					GamePanel.y1=event.getY();
				}
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		// If it is in the single player mode
		if (MainClass.option==4){
			
			if (GamePanel.moving==0){
				// Set isReady to true to run the makeMove()
				if (lineFlag)
					GamePanel.isReady = true;
				lineFlag=false;	
			}
		}
		// If it is in the online mode
		else if (MainClass.option==5){
			if (OnlineGame.option == 1){
				if (GamePanel.moving==0){
					// Set isReady to true to run the makeMove()
					if (lineFlag)
						GamePanel.isReady = true;
					lineFlag=false;	
				}
			}
			else if (OnlineGame.option == 2){
				if (GamePanel.moving==5){
					// Set isReady to true to run the makeMove()
					if (lineFlag)
						GamePanel.isReady = true;
					lineFlag=false;	
				}
			}
		}
	}
}
