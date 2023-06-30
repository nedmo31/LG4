package lg4;

import java.awt.*;

abstract class HoleSegment {

    // The type of subclass the segment is. Stupid way to help
    // me do serialization
    String type;

    // Ellipse or Polygon or Rectangle, nothing else
    Shape area;

    // Color of area
    Color color;

    // For gameplay:
    // If the segements can be flown through, and if the
    // segment is out of bounds
    boolean solid = false, oob;

    // How much the ball bounces off the segment
    double bounce;

    public HoleSegment(Shape p, Color c, double b, String t) {
        area = p;
        color = c;
        bounce = b;
        type = t;
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        if (area instanceof Polygon) {
            g.fillPolygon((Polygon)area);
        } else {
            //g.fillOval(area.x, area.x, area.radius, area.radius);
        }
    }

}