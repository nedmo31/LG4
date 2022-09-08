package lg4;

import javax.swing.JPanel;
import java.awt.Graphics;

/**
 * This class will handle the graphics for the game. 
 * Everything will be pixels?
 */
public class PixelArt extends JPanel {
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background

        // Hole
        for (HoleSegment hs : lg4.hole.segments) {
            
        }

        // Ball + Extras

    }
}
