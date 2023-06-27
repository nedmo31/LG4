package lg4;

import javax.swing.JPanel;

import java.awt.Graphics;
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

    public static int mx, my;

    public Window() {
        addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
                if (lg4.hitStatus == lg4.AIMING) {
                    // get distance between ball and click
                    int xDist = mx - lg4.ball.x();
                    int yDist = my - lg4.ball.y();
                    double totalDist = Math.sqrt(xDist*xDist + yDist*yDist);
                    lg4.hitPower = Math.min(1, 2*totalDist/lg4.club.radius);
                    // get angle
                    lg4.xyAngle = -1*Math.atan2(yDist, xDist);
                    if (yDist > 0) {
                        lg4.xyAngle -= Math.PI;
                    }
                    
                    lg4.hitStatus = lg4.SWINGING1;
                } else if (lg4.hitStatus == lg4.SWINGING1) {
                    // get time and set status to swining 2
                    lg4.hitStatus = lg4.SWINGING2;
                } else if (lg4.hitStatus == lg4.SWINGING2) {
                    // get time and set status to ball moving?
                    lg4.hitStatus = lg4.BALL_MOVING;
                }
			}
            public void mouseReleased(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  
                lg4.gStage.checkButtonClick(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
			}
            public void mouseDragged(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  

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
