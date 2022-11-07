package lg4;

public class Golfer {
    
    public String name;

    // Consistency is spin control!
    public int power, accuracy, consistency;

    public Club[] clubs = new Club[]{ new Club("first", .8, 65), new Club("second", .7, 55) };

    public Golfer() {
        power = accuracy = consistency = 10;
    }

}
