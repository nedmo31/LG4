package lg4;

import java.awt.Polygon;
import java.awt.Color;

class HoleSegment {
    Polygon area;
    Color color;

    public HoleSegment(Polygon p, Color c) {
        area = p;
        color = c;
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

}