package lg4;

public class Golfer {
    
    /**
     * The name of the Golfer
     */
    public String name;

    /**
     * How strong the golfer is. This affects the distance of their shots
     */
    public int power;
    /**
     * How accurate the golfer is with their shots. Low accuracy means they 
     * might not hit it where they wanted to
     */
    public int accuracy;
    /**
     * How consistent the golfer is with their spin. Low consitency means 
     * they may slice it or hook it when they don't want to.
     */
    public int consistency;

    /**
     * How skilled the golfer is with putting. It just makes putting
     * easier the higher it is.
     */
    public int putting;

    /**
     * An array of clubs that the player owns
     */
    public Club[] clubs = new Club[]{ new Club("first", .8, 65), new Club("second", .7, 55) };

    public Golfer() {
        power = accuracy = consistency = putting = 5;
    }

}
