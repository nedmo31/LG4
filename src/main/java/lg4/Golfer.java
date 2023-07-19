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
        power = accuracy = putting = 4;
        money = golfbag = 0;
        name = "Anon";
        initClubs();
    }

    public Golfer(String n) {
        power = accuracy = putting = 4;
        money = golfbag = 0;
        name = n; 
        initClubs();
    }

    public void initClubs() {
        clubs = lg4.clubs;
        lg4.player = this;
        for (int i = 0; i < clubs.length; i++) {
            checkClubUpgrade(i);
        }
        lg4.club = clubs[0];
    }

    public void checkClubUpgrade(int index) {
        int powerup = (golfbag & (0b11 << (index<<1))) >> (index<<1);
        clubs[index].power += powerup * 4;
        if (powerup > 1) { 
            clubs[index].name += "++";
        } else if (powerup > 0) {
            clubs[index].name += "+";
        } 
        clubs[index].updateRadius();
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

    public void fixShopCosts() {
        ((TextButtonWithMessage)GraphicsStage.playerCard.buttons[5]).onHover = "$" + power*10;
        ((TextButtonWithMessage)GraphicsStage.playerCard.buttons[6]).onHover = "$" + accuracy*10;
        ((TextButtonWithMessage)GraphicsStage.playerCard.buttons[7]).onHover = "$" + putting*10;
    }

}
