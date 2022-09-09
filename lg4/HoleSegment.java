package lg4;

import java.awt.*;

class HoleSegment {

    // Circle or Polygon, nothing else
    Shape area;

    // Color of area
    Color color;

    // For gameplay:
    // If the segements can be flown through, and if the
    // segment is out of bounds
    boolean solid = false, oob;

    // How much the ball bounces off the segment
    double bounce;

    public HoleSegment(Shape p, Color c, double b) {
        area = p;
        color = c;
        bounce = b;
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        // e.g.
        if (area instanceof Polygon) {
            g.fillPolygon((Polygon)area);
        } else {
            //g.fillOval(area.x, area.x, area.radius, area.radius);
        }
    }

}