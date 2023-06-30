package lg4;

import java.awt.*;

/**
 * This class represents a forest on the course. This is
 * how we will block off a large portion of the course
 */
public class Forest extends HoleSegment {

    public int height = 10;
    
    public static Color treetopColor = Color.pink;

    public Forest(Polygon a) {
        super(a, treetopColor, .9, "forest");
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        g.fillPolygon((Polygon)area);
    }

}
