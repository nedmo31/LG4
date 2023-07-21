package lg4;

import java.awt.Color;

public class Ball {

    public static Ball defaultBall = new Ball(1, 1, .4, "default");

    public final double WIND_MULTIPLIER = .1;

    // Gravity!
    public static double gravity = 9.8;

    // higher speeds mean the ball moves faster. Makes testing faster
    final int SPEED = 5;

    // x and y correspond to the screen
    // and z is the height
    double x, y, z;

    // These are stats for the ball. Weight: the weight of the ball
    // spin: how much the ball can spin. Bounce: how bouncy the ball is.
    // These numbers will be multipliers, probably between 0 and 2.
    double weight, spin, bounce;

    int size = 8;

    // The color of the ball
    Color color;

    String name;

    public Ball(double w, double s, double b, String n) {
        weight = w; 
        spin = s;
        bounce = b;
        color = Color.white;
        name = n;
        x = y = z = 0;
    }

    public Ball(double w, double s, double b, String n, Color c) {
        weight = w; 
        spin = s;
        bounce = b;
        color = c;
        name = n;
        x = y = z = 0;
    }

    void updateBall(double xv, double yv, double zv, double time) {
        this.x += xv * time; this.y += yv * time; this.z += zv * time;
    }

    // It will be useful later to get integer values for xyz
    int x() { return (int)x; }
    int y() { return (int)y; }
    int z() { return (int)z; }

    int xCorrected() { return (int)(x - size/2); }
    int yCorrected() { return (int)(y - size/2); }

    int xInSky() { return xCorrected() - (int)(z/3);}
    int yInSky() { return yCorrected() - (int)(z/3);}

    /**
     * the fuction that handles the ball being hit.
     *  
     * @param club The club used to hit the ball
     * @param pow value between 0 and 1 for what percentage of power was used
     * @param xyAng the angle in radians in the xy plan that the ball was hit
     */
    public void hit(double pow, double xyAng, double targetSpinLR, double targetSpinUD) {
        // set the hitStatus
        lg4.hitStatus = lg4.BALL_MOVING;
        this.z = 0;

        double initialX = this.x;
        double initialY = this.y;

        pow = Math.sqrt(pow); // this is to make the power recommendation more accurate. IDK about it though

        // The intitial velocity of the ball
        // uses the club power, player power, power of the shot, and weight of the ball
        double velocity = lg4.club.power * ((28+(double)lg4.player.power) / 60) * pow * (1 / weight) * lg4.club.getPenalty()  * ((14 - lg4.swingSpeed)/10.0)
        , xVelocity, yVelocity, zVelocity;
        // The spin on the ball in [-1, 1], 0 being none, 1 being strong
        // This is just for left/right spin
        double spinLR = targetSpinLR * pow;

        if (xyAng == -1 * Math.PI) {
            xyAng = Math.PI;
        }

        xyAng += (.5 - Math.random())*((10-lg4.player.accuracy)/20.0);
        if (xyAng < 0 && xyAng > -1) {
            xyAng = -1*Math.PI + xyAng;
        }

        // This keeps track of the direction of the spin in radians
        double spinLRdir = xyAng + Math.PI/2;
        // If it's to the left, we take the initial angle 90 degrees to the left. Otherwise to the right
        // if (spinLR >= 0) {
        //     spinLRdir = xyAng - Math.PI/2;
        // } else {
        //     spinLRdir = xyAng + Math.PI/2;
        // }
        //This is the top/back spin on the ball, it will be a bit simpler to keep track of.
        // [-1, 1], positive for top spin
        double spinUD = targetSpinUD / 2;

        // Split velocity into components
        double xyVelocity = Math.abs(Math.cos(lg4.club.angle) * velocity);
        if (xyAng < 0) {
            xVelocity = (-1)*(Math.cos(xyAng))*xyVelocity;
            yVelocity = (Math.sin(xyAng))*xyVelocity;
        } else {
            xVelocity = (Math.cos(xyAng))*xyVelocity;
            yVelocity = (-1)*(Math.sin(xyAng))*xyVelocity;
        }
        if (yVelocity > 0) {
            spinLRdir *= -1;
        }
        zVelocity = Math.sin(lg4.club.angle) * velocity;
        
        // System.out.println("Hitting ball with power: " + pow);
        // System.out.println("Hitting ball with angle: " + xyAng);
        // System.out.println("Hitting ball with velocities x: " + xVelocity + 
        //  ", y: " + yVelocity + ", z: " +zVelocity);
        // System.out.println("Hitting ball with spin direction: " + spinLRdir);
        // System.out.println("Xcomp: " + Math.cos(spinLRdir) + "\tYcomp: " + Math.sin(spinLRdir));

        // The loop that's gonna start moving the ball. We use the system time to help
        double startTime = System.nanoTime(), loopTime = 0;

        while (true) {
            /*
             * update x y and z with velocity
             * update x y and z velocity with acceleration = F / m
             * 
             * if ball is in sand stop it
             * if ball is in water move it right before
             * if ball is under z = 0 plane, bounce
             * 
             * repaint
             * 
             */
            // Keep track of how long each loop takes 
            loopTime = (System.nanoTime() - startTime) / 1000000000 * SPEED;
            startTime = System.nanoTime();
            // move the ball
            updateBall(xVelocity, yVelocity, zVelocity, loopTime);
            // FOR NOW, I think i'll treat the forest as just Out of bounds, so check at end
            // if (z <= Forest.height) {
            //     HoleSegment seg = lg4.hole.whatSegment(this);
            //     if (seg instanceof Forest) {
            //         this.x = initialX;
            //         this.y = initialY;
            //         break;
            //     }
            // }
            if (z <= 0) {
                // Ball is bouncing!
                HoleSegment seg = lg4.hole.whatSegment(this);
                if (seg instanceof Water) {
                    this.x = initialX;
                    this.y = initialY;
                    break;
                } else if ((seg instanceof Sand) && zVelocity < 0) {
                    break;
                } else if (zVelocity < 0) {
                    // Averages the bounciness of the ball and the terrain, value < 1
                    double slowDown = (this.bounce + seg.bounce) / 2;
                    slowDown *= 1 + (spinUD / 5); // multiplies it by value in [.8, 1.2]
                    zVelocity *= -1 * slowDown;
                    xVelocity *= slowDown;
                    yVelocity *= slowDown;
                    if (Math.abs(xVelocity) < .01)
                        break;
                }
                
            } else {

                // apply acceleration onto the ball in the z-direction
                // TO DO: make weight matter for spin?
                zVelocity -= gravity * loopTime;
                zVelocity -= spinUD * loopTime;

                // apply other accelerations for x and y directions
                switch (lg4.hole.windDir) {
                    case 0:
                        yVelocity -= lg4.hole.windSpeed * loopTime * WIND_MULTIPLIER;
                        break;
                    case 1: 
                        xVelocity += lg4.hole.windSpeed * loopTime * WIND_MULTIPLIER;
                        break;
                    case 2:
                        yVelocity += lg4.hole.windSpeed * loopTime * WIND_MULTIPLIER;
                        break;
                    case 3: 
                        xVelocity -= lg4.hole.windSpeed * loopTime * WIND_MULTIPLIER;
                        break; 
                }
                xVelocity += spinLR * 2 * Math.cos(spinLRdir) * loopTime;
                yVelocity += spinLR * 2 * Math.sin(spinLRdir) * loopTime;
            }
            //lg4.win.repaint();
        }
        HoleSegment seg = lg4.hole.whatSegment(this);
        if (seg instanceof Forest) {
            this.x = initialX;
            this.y = initialY;
            System.out.println("Ball went out of bounds.");
        }
        lg4.hitStatus = lg4.NOT_HITTING;
    }
}
