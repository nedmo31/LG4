package lg4;

import java.awt.Color;
import java.awt.Shape;

public class Fairway extends HoleSegment {

    static final double FAIRWAY_BOUNCE = .7;
    static final Color FAIRWAY_COLOR = new Color(80, 200, 80);

    public Fairway(Shape p) {
        super(p, FAIRWAY_COLOR, FAIRWAY_BOUNCE, "fairway");
    }
    
}
