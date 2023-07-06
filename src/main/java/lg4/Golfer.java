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

    public int money;

    public int golfbag;

    /**
     * Index into array. weird relationship with lg4.club but whatever
     */
    public int clubIndex = 0;

    /**
     * An array of clubs that the player owns
     */
    public Club[] clubs;

    public Golfer(boolean ugh) {
        power = accuracy = putting = 5;
        money = golfbag = 0;
        name = "Anon";
        initClubs();
    }

    public Golfer(String n) {
        power = accuracy = putting = 5;
        money = golfbag = 0;
        name = n; 
        initClubs();
    }

    public void initClubs() {
        clubs = lg4.clubs;
        lg4.player = this;
        clubs[0].power += (golfbag & 0b11) * 10;
        clubs[1].power += (golfbag & 0b1100 >> 2) * 10;
        clubs[2].power += (golfbag & 0b110000 >> 4) * 10;
        clubs[3].power += (golfbag & 0b11000000 >> 6) * 10;
        clubs[4].power += (golfbag & 0b1100000000 >> 8) * 10;
        clubs[5].power += (golfbag & 0b110000000000 >> 10) * 10;
        clubs[6].power += (golfbag & 0b11000000000000 >> 12) * 10;
        clubs[7].power += (golfbag & 0b1100000000000000 >> 14) * 10;
        clubs[8].power += (golfbag & 0b110000000000000000 >> 16) * 10;
        for (int i = 0; i < clubs.length; i++) {
            clubs[i].updateRadius();
        }
        lg4.club = clubs[0];
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
