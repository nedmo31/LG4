package lg4;

import java.awt.*;

/**
 * This class represents a playable hole in golf. The image of this
 * will be the main graphics on the screen. 
 * 
 */
public class Hole {
    
    HoleSegment rough = new HoleSegment(
        new Rectangle(0, 0, lg4.screenWidth, lg4.screenHeight), Color.green, .40);
    
    /**
     * This contains all the parts for the hole including the green,
     * fairway, water, sand, teebox, and anything else. At a minimum,
     * The hole should have a teebox and a green (which has the hole in it)
     * 
     * This array should be ordered by layer of output
     * Whatever comes last will be painted on top of others things
     */
    HoleSegment[] segments;

    /**
     * x and y are the location of the hole
     */
    int x, y, par, windDir, windSpeed;
    // TO DO: improve upon wind. 0 north, 1 east, 2 south, 3 west for windDir

    /**
     * Note: looks backwards through the array to find first segment
     * that the ball is in. So, if there is a river running through the
     * fairway, it would find the river first
     * 
     * @return the segment the ball is currently over
     */
    HoleSegment whatSegment(Ball ball) {
        for (int i = segments.length-1; i >= 0; i--) {
            if (segments[i].contains(ball.x(), ball.y()))
                return segments[i];
        }
        return rough;
    }

}
