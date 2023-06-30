package lg4;

import java.awt.geom.Ellipse2D;
import java.awt.*;

/**
 * Class to represent sand traps/bunkers
 */
public class Sand extends HoleSegment {
    
    public static Color shallowSandColor = Color.yellow, deepSandColor;

    public Sand(Polygon p) {
        super(p, shallowSandColor, 0, "sand");
        oob = false;
    }

    public Sand(Ellipse2D ellipse) {
        super(ellipse, shallowSandColor, 0, "sand");
        oob = false;
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        
        // Do this!
        // First block is for polygons
        if (area instanceof Polygon) {
            g.fillPolygon((Polygon)area);

        // Second block is for ellipses
        } else {
            Ellipse2D temp = (Ellipse2D)area;
            g.fillOval((int)temp.getX(), (int)temp.getY(), (int)temp.getWidth(), (int)temp.getHeight());
        }
    }

}
