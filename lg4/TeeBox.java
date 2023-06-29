package lg4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class TeeBox extends HoleSegment {
    
    public static Color teeBoxColor = Color.lightGray;

    public TeeBox() {
        super(new Rectangle(250, 300 + (int)(Math.random()*100),
        40, 80), teeBoxColor, .75);
    }

    public void paintArea(Graphics g) {
        g.setColor(Color.black);
        if (area instanceof Rectangle) {
            int x = ((Rectangle)area).x;
            int y = ((Rectangle)area).y;
            int width = ((Rectangle)area).width;
            int height = ((Rectangle)area).height;
            g.drawRect(x, y, width, height);
            g.setColor(teeBoxColor);
            g.fillRect(x, y, width, height);
        }
    }

}
