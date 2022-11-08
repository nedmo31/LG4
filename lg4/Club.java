package lg4;

public class Club {
    
    /**
     * The name of the club
     */
    String name;

    /**
     * The angle in radians that this club hits above the horizontal
     * e.g. a wedge would have a large angle such as 1.2
     */
    double angle;

    /**
     * How powerful the club is. Probably some int between 30 and 100
     */
    int power;

    /**
     * The radius of the circle that this club can hit within
     */
    int radius;

    public Club(String n, double a, int p) {
        name = n; angle = a; power = p;
        radius = (int) (5 * power * (1- Math.abs(.785 - angle)));
    }
}
