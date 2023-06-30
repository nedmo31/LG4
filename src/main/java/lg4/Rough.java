package lg4;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;

public class Rough extends HoleSegment {

    public Rough(Shape p, Color c, double b) {
        super(new Rectangle(0, 0, lg4.screenWidth, lg4.screenHeight), Color.green, .40, "rough");
    }
    
}
