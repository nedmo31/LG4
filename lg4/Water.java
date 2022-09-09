package lg4;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Water extends HoleSegment {
    
    public static Color shallowWaterColor = Color.blue, deepWaterColor;

    public Water(Polygon p) {
        super(p, shallowWaterColor, 0);
        oob = true;
    }

    public Water(Ellipse2D ellipse) {
        super(ellipse, shallowWaterColor, 0);
        oob = true;
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        
        // Do this!
        // First block is for rivers (polygons)
        if (area instanceof Polygon) {
            g.fillPolygon((Polygon)area);

        // Second block is for ponds (ellipses)
        } else {
            Ellipse2D temp = (Ellipse2D)area;
            g.fillOval((int)temp.getX(), (int)temp.getY(), (int)temp.getWidth(), (int)temp.getHeight());
        }
    }


}
