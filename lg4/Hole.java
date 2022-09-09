package lg4;

/**
 * This class represents a playable hole in golf. The image of this
 * will be the main graphics on the screen. 
 */
public class Hole {
    
    
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
    int x, y, par;

}
