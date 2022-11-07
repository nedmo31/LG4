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
                
			}

		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
			}

		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
                lg4.gStage.checkButtonClick(e);
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {

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
}
