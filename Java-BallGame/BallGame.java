/******************************************************************************
 *  Yurii Boiko Assignment 6 
 *  Compilation:  javac BallGame.java
 *  Execution:    java BallGame n
 *  Dependencies: BasicBall.java StdDraw.java
 *  
 *  Creates a BasicBall and animates it
 *
 *  Part of the animation code is adapted from Computer Science:   An Interdisciplinary Approach Book
 *  
 *  Run the skeleton code with arguments : 1  basic  0.08
 *******************************************************************************/
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
public class BallGame { 

    public static void main(String[] args) {
  
    	// number of bouncing balls
    	int numBalls = Integer.parseInt(args[0]);
    	//ball types
    	String ballTypes[] = new String[numBalls];
    	//sizes of balls
    	double ballSizes[] = new double[numBalls];
    	
    	//retrieve ball types
    	int index =1;
    	for (int i=0; i<numBalls; i++) {
    		ballTypes[i] = args[index];
    		index = index+2;
    	}
    	//retrieve ball sizes
    	index = 2;
    	for (int i=0; i<numBalls; i++) {
    		ballSizes[i] = Double.parseDouble(args[index]);
    		index = index+2;
    	}
     
    	Player player = new Player();
    	
    	//number of active balls
    	int numBallsinGame = 0;
        StdDraw.enableDoubleBuffering();

        StdDraw.setCanvasSize(800, 800);
        // set boundary to box with coordinates between -1 and +1
        StdDraw.setXscale(-1.0, +1.0);
        StdDraw.setYscale(-1.0, +1.0);

        //Create "numBalls" balls (of types given in "ballTypes" with sizes given in "ballSizes") and store them in an Arraylist
        ArrayList<BasicBall> balls= new ArrayList<BasicBall>();


        for (String type : ballTypes){
            switch(type){
                case "basic": balls.add( new BasicBall(ballSizes[numBallsinGame],Color.RED));
                    break;
                case "bounce": balls.add( new BounceBall(ballSizes[numBallsinGame], Color.GREEN));
                    break;
                case "shrink": balls.add( new ShrinkBall(ballSizes[numBallsinGame],Color.BLUE));
                    break;
                case "split": balls.add( new SplitBall(ballSizes[numBallsinGame],Color.YELLOW));
                    break;
                default: balls.add(new BasicBall(ballSizes[numBallsinGame],Color.PINK));
                    break;
            }
            numBallsinGame++;
        }
 
        
        // do the animation loop
        StdDraw.enableDoubleBuffering();
        while (numBallsinGame > 0) {

            for(int i=0; i<balls.size();i++){
                balls.get(i).move();
            }

                //Check if the mouse is clicked
                if (StdDraw.isMousePressed()) {
                    double x = StdDraw.mouseX();
                    double y = StdDraw.mouseY();
                    //check whether a ball is hit. Check each ball. 
                    //update player
                    for(int i=0; i<balls.size();i++){
                        if (balls.get(i).isHit(x,y)) {
                            balls.get(i).reset();
                            if (balls.get(i) instanceof SplitBall){
                                SplitBall newBall = (SplitBall) balls.get(i);
                                balls.add(newBall.getChild());
                            }
                            player.update(balls.get(i).getScore());
                            i=balls.size();
                        }
                    } 
                }
              
            numBallsinGame = 0;
            // draw the n balls
            StdDraw.clear(StdDraw.GRAY);
            StdDraw.setPenColor(StdDraw.BLACK);
            
            for(int i=0; i<balls.size();i++){
                if (balls.get(i).isOut == false) { 
                    balls.get(i).draw();
                    numBallsinGame++;
                }
            }

            //Print the game progress
            StdDraw.setPenColor(StdDraw.YELLOW);
            Font font = new Font("Arial", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.text(-0.65, 0.90, "Number of balls in game: "+ String.valueOf(numBallsinGame));
            StdDraw.text(-0.65, 0.85, "Number of total hits: " + String.valueOf(player.getNumHits()));
            StdDraw.text(-0.65, 0.80, "Total Score: " + String.valueOf(player.getScore()));
            StdDraw.text(-0.65, 0.75, "Ball with most hits: " + player.getBallType());

            StdDraw.show();
            StdDraw.pause(20);
            
        }
        while (true) {
            StdDraw.setPenColor(StdDraw.BLUE);
            Font font = new Font("Arial", Font.BOLD, 60);
            StdDraw.setFont(font);
            StdDraw.text(0, .25, "GAME OVER");
            StdDraw.text(0, -.25, "SCORE: " + String.valueOf(player.getScore()));
            StdDraw.show();
            StdDraw.pause(10);           
        }
        	
        
    }
}
