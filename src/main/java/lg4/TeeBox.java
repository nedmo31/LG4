package lg4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class TeeBox extends HoleSegment {
    
    public static Color teeBoxColor = new Color(80, 220, 120);

    public TeeBox() {
        super(new Rectangle(250, 300 + (int)(Math.random()*100),
        40, 80), teeBoxColor, .75, "teebox");
    }

    public void paintArea(Graphics g) {
        if (area instanceof Rectangle) {
            int x = ((Rectangle)area).x;
            int y = ((Rectangle)area).y;
            int width = ((Rectangle)area).width;
            int height = ((Rectangle)area).height;
            g.setColor(teeBoxColor);
            g.fillRect(x, y, width, height);
            g.setColor(Color.white);
            g.fillRect(x + width/2 - 2, y + 4, 4, 4);
            g.fillRect(x + width/2 - 2, y +height - 8, 4, 4);
        }
    }

}
