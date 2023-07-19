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

    public double getPenalty() {
        HoleSegment hs = lg4.hole.whatSegment(lg4.ball);
        if (hs instanceof Sand) { return sandPenalty; }
        if (hs instanceof Rough) { return roughPenalty; }
        return 1.0;
    }

    public void updateRadius() {
        double velocity = power * ((28+(double)lg4.player.power) / 60) * (1 / lg4.ball.weight) * ((14 - lg4.swingSpeed)/10.0);
        radius = (int) (2 * (velocity*velocity * Math.sin(2*angle)) / Ball.gravity);
    }
    
    public int getRadius() {
        updateRadius();
        return radius;
    }
}
