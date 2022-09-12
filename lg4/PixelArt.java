package lg4;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;

/**
 * This class will handle the graphics for the game. 
 * Probably shouldn't have named it pixelart ¯\_(ツ)_/¯
 * 
 * Also, it has the MouseListeners and KeyboardListeners
 */
public class PixelArt extends JPanel {

    public PixelArt() {
        addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
			}

		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {

			}

		});
		addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
		
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

        // Hole
        // This will fill the whole background with dark green to represent the rough
        g.setColor(new Color(29, 153, 66));
        g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);

        for (HoleSegment hs : lg4.hole.segments) {
            hs.paintArea(g);
        }

        // Ball + Extras
        int xInSky = lg4.ball.xInSky();
        int yInSky = lg4.ball.yInSky();
        g.setColor(Color.DARK_GRAY);
        g.fillOval(lg4.ball.xCorrected(), lg4.ball.yCorrected(), lg4.ball.size, lg4.ball.size);
        g.setColor(lg4.ball.color);
        g.fillOval(xInSky, yInSky, lg4.ball.size, lg4.ball.size);
        g.setColor(Color.black);
        g.drawOval(xInSky, yInSky, lg4.ball.size, lg4.ball.size);

    }
}
