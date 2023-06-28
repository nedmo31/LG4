package lg4;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.*;
import java.awt.Font;

/**
 * This class will handle the graphics for the game. 
 * Probably shouldn't have named it Window ¯\_(ツ)_/¯
 * 
 * Also, it has the MouseListeners and KeyboardListeners
 */
public class Window extends JPanel {

    public static Font f1 = new Font("Serif", Font.BOLD, 50);
    public static Font f2 = new Font("Serif", Font.PLAIN, 40);
    public static Font f3 = new Font("Serif", Font.PLAIN, 30);
    public static Font f3b = new Font("Serif", Font.BOLD, 30);
    public static Font f4 = new Font("Serif", Font.PLAIN, 25);
    public static Font f5 = new Font("Serif", Font.PLAIN, 20);

    /**
     * For click and drag feature, the place where the initial click was
     */
    public static Point firstClick = new Point(0,0);
    /**
     * For click and drag feature, the place where the release was
     */
    public static Point secondClick = new Point(0,0);

    /**
     * The time in ns that a swing starts. See hitting.txt
     */
    public static long swingFirst = 0;

    /**
     * The time in ns for the second click in a swing
     */
    public static long swingSecond = 0;

    /**
     * The time in ns for the third click in a swing
     */
    public static long swingThird = 0;

    /**
     * The time to hit the full power
     */
    public static long targetFirst = 0;

    /**
     * The time to hit the ball perfectly straight
     */
    public static long targetSecond = 0;

    public static int mx, my;

    public Window() {
        addMouseWheelListener(
            new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.getWheelRotation() < 0) {
                        lg4.player.clubDown();
                    } else {   
                        lg4.player.clubUp();
                    }
                }
            }
        );
        addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
                if (lg4.hitStatus == lg4.AIMING) {
                    // get distance between ball and click
                    int xDist = mx - lg4.ball.x();
                    int yDist = my - lg4.ball.y();
                    double totalDist = Math.sqrt(xDist*xDist + yDist*yDist);
                    lg4.targetPower = Math.min(1, 2*totalDist/lg4.club.radius);
                    // get angle
                    lg4.xyAngle = -1*Math.atan2(yDist, xDist);
                    if (yDist > 0) {
                        lg4.xyAngle -= Math.PI;
                    }
                    
                    lg4.hitStatus = lg4.AIMED;

                } else if (lg4.hitStatus == lg4.AIMED) {
                    lg4.hitStatus = lg4.SWINGING1;
                    swingFirst = System.nanoTime();
                    targetFirst = swingFirst + 200000000 * lg4.swingSpeed;
                    targetSecond = swingFirst + 400000000 * lg4.swingSpeed;
                } else if (lg4.hitStatus == lg4.SWINGING1) {
                    swingSecond = System.nanoTime();
                    long diff = swingSecond - targetFirst;
                    long diffAbs = Math.abs(swingSecond - targetFirst);
                    lg4.hitPower = Math.max(1 - (diffAbs / (double)(200000000 * lg4.swingSpeed)), .05); // hit's will always be at least .05 power
                    if (diff < 0) 
                        targetSecond -= diff;
                    lg4.hitStatus = lg4.SWINGING2;
                } else if (lg4.hitStatus == lg4.SWINGING2) {
                    swingThird = System.nanoTime();
                    long diff = swingThird - targetSecond;
                    lg4.hitSpinLeftRight = (diff / (double)(200000000 * lg4.swingSpeed));
                    if (lg4.hitSpinLeftRight > 1)
                        lg4.hitSpinLeftRight = 1; 
                    if (lg4.hitSpinLeftRight < -1) 
                        lg4.hitSpinLeftRight = -1; 
                    lg4.hitStatus = lg4.SWINGING2;
                    lg4.hitStatus = lg4.BALL_MOVING;
                } else if (lg4.hitStatus == lg4.PUTTING) {
                    firstClick.x = mx;
                    firstClick.y = my;
                }
			}
            public void mouseReleased(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  
                lg4.gStage.checkButtonClick(e);
                if (lg4.hitStatus == lg4.PUTTING) {
                    secondClick.x = mx;
                    secondClick.y = my;
                    lg4.hitPower = .5 * Math.sqrt(Math.pow(firstClick.getX() - secondClick.getX(), 2)
							+ Math.pow(secondClick.getY() - firstClick.getY(), 2));
                    lg4.xyAngle = -Math.atan2(firstClick.getY() - secondClick.getY(),
                        firstClick.getX() - secondClick.getX());
                    lg4.hitStatus = lg4.PUTT_ROLL;
                }
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
                if (lg4.hitStatus == lg4.AIMING) {
                    // get distance between ball and click
                    int xDist = mx - lg4.ball.x();
                    int yDist = my - lg4.ball.y();
                    double totalDist = Math.sqrt(xDist*xDist + yDist*yDist);
                    lg4.targetPower = Math.min(1, 2*totalDist/lg4.club.radius);
                }
			}
            public void mouseDragged(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  
                if (lg4.hitStatus == lg4.PUTTING) {
                    secondClick.x = mx;
                    secondClick.y = my;
                    lg4.hitPower = .5 * Math.sqrt(Math.pow(firstClick.getX() - secondClick.getX(), 2)
							+ Math.pow(secondClick.getY() - firstClick.getY(), 2));
                    lg4.xyAngle = -Math.atan2(firstClick.getY() - secondClick.getY(),
                        firstClick.getX() - secondClick.getX());
                }
			}
		});
		addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				
            }
		});
    }

    /**
     * The method that paints the screen
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        lg4.gStage.paintGraphicsStage(g);

    }

    // private void handleHitReleased(MouseEvent e) {
        
    // }

    // private void handleHitPressed(MouseEvent e) {

    // }

    // private void handlHitDragged(MouseEvent e) {
        
    // }

}
