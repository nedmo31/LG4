package lg4;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Graphics;

/**
 * This class will handle the graphics for the game. 
 * Probably shouldn't have named it pixelart ¯\_(ツ)_/¯
 */
public class PixelArt extends JPanel {

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
