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
     * How skilled the golfer is with putting. It just makes putting
     * easier the higher it is.
     */
    public int putting;

    /**
     * Index into array. weird relationship with lg4.club but whatever
     */
    public int clubIndex = 0;

    /**
     * An array of clubs that the player owns
     */
    public Club[] clubs;

    public Golfer() {
        power = accuracy = putting = 7;
    }

    public void clubUp() {
        if (clubIndex++ >= clubs.length-1)
            clubIndex = 0;
        lg4.club = clubs[clubIndex];
    }

    public void clubDown() {
        if (clubIndex-- <= 0)
            clubIndex = clubs.length-1;
        lg4.club = clubs[clubIndex];
    }

}
