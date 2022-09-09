package lg4;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class will handle the graphics for the game. 
 * Everything will be pixels, which simplifies/rounds
 * the underlying exactness by 10. Basically, items will 
 * by exact location, and the pixels will generalize what's
 * going on. 
 */
public class PixelArt extends JPanel {
    
    int pixelSize = 10;

    public PixelArt(int ps) {
        pixelSize = ps;
    }

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

    }
}
