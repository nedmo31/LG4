package lg4;

import java.awt.Color;

public class Ball {

    // Gravity!
    public static double gravity = 9.8;

    // x and y correspond to the screen
    // and z is the height
    int x, y, z;

    // These are stats for the ball. Weight: the weight of the ball
    // spin: how much the ball can spin. Bounce: how bouncy the ball is.
    // These numbers will be multipliers, probably between 0 and 2.
    double weight, spin, bounce;

    // The color of the ball
    Color color;

    public Ball(double w, double s, double b) {
        weight = w; 
        spin = s;
        bounce = b;
        color = Color.white;
        x = y = z = 0;
    }

    public void hit(Club club, double pow, double xyAng) {

    }
}
