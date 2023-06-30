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
    int radius = 100;

    /**
     * Number in (0, 1]. Power of shot gets multiplied when hitting
     * out of the rough. 1 means there is no penalty
     */
    double roughPenalty;

    /**
     * Number in (0, 1]. Power of shot gets multiplied when hitting
     * out of the sand. 1 means there is no penalty
     */
    double sandPenalty;

    public Club(String n, double a, int p, double rP, double sP) {
        name = n; angle = a; power = p;
        roughPenalty = rP; sandPenalty = sP;
    }

    public void updateRadius() {
        double velocity = power * ((double)lg4.player.power / 10) * (1 / lg4.ball.weight);
        radius = (int) (2 * (velocity*velocity * Math.sin(2*angle)) / Ball.gravity);
    }
}
