package lg4;

import java.awt.*;
import java.awt.Color;

class HoleSegment {
    Polygon area;
    Color color;
    boolean solid = false, oob;

    public HoleSegment(Polygon p, Color c) {
        area = p;
        color = c;
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public void paintArea(Graphics g) {
        g.setColor(color);
        g.fillPolygon(area);
    }

}