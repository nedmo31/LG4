package lg4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Ellipse2D;

/**
 * This class represents the Greens on a hole
 * This is where the actuall hole and flag sits
 */
public class Green extends HoleSegment {
    
    public static Color greensColor = Color.lightGray;

    public Green(Ellipse2D ellipse) {
        super(ellipse, greensColor, .75);
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        Ellipse2D temp = (Ellipse2D)area;
        g.fillOval((int)temp.getX(), (int)temp.getY(), (int)temp.getWidth(), (int)temp.getHeight());
    }

}
