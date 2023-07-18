package lg4;

import java.awt.*;

/**
 * This class represents a forest on the course. This is
 * how we will block off a large portion of the course
 */
public class Forest extends HoleSegment {

    public static int xDensity = 60, yDensity = 60;

    public static int height = 10;
    
    public static Color treetopColor = new Color(30, 130, 30);

    public Forest(Polygon a) {
        super(a, treetopColor, .3, "forest");
    }

    public Forest(Rectangle a) {
        super(a, treetopColor, .3, "forest");
    }

    public void paintArea(Graphics g) {
        g.setColor(treetopColor);
        if (area instanceof Polygon) {
            Polygon p = (Polygon)area;
            g.fillPolygon(p);
            g.setColor(Color.white);
            for (int i = 0; i < p.npoints; i++) {
                g.fillRect(p.xpoints[i]-2, p.ypoints[i]-2, 4, 4);
            }
        } else {
            Rectangle r = (Rectangle)area;
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }

}
