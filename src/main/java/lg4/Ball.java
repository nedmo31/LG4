package lg4;

import java.awt.Color;

public class Ball {

    // Gravity!
    public static double gravity = 9.8;

    // higher speeds mean the ball moves faster. Makes testing faster
    final int SPEED = 6;

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

    public Ball(double w, double s, double b) {
        weight = w; 
        spin = s;
        bounce = b;
        color = Color.white;
        x = y = z = 0;
    }

    void updateBall(double xv, double yv, double zv, double time) {
        System.out.println("Updating ball with xv,yv,zv,time:"+xv+","+yv+","+zv+","+time);
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

        pow = Math.sqrt(pow); // this is to make the power recommendation more accurate. IDK about it though

        // The intitial velocity of the ball
        // uses the club power, player power, power of the shot, and weight of the ball
        double velocity = lg4.club.power * ((double)lg4.player.power / 10) * pow * (1 / weight), xVelocity, yVelocity, zVelocity;
        // The spin on the ball in [-1, 1], 0 being none, 1 being strong
        // This is just for left/right spin
        double spinLR = targetSpinLR;

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
        double spinUD = targetSpinUD;

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

        boolean repaint = true;
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

            if (z <= 0) {
                // Ball is bouncing!
                HoleSegment seg = lg4.hole.whatSegment(this);
                if (seg instanceof Water) {
                    // To DO!
                    break;
                } else if (seg instanceof Sand) {
                    // To DO!
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
                zVelocity += spinUD * loopTime;

                // apply other accelerations for x and y directions
                switch (lg4.hole.windDir) {
                    case 0:
                        yVelocity += lg4.hole.windSpeed * loopTime;
                        break;
                    case 1: 
                        xVelocity += lg4.hole.windSpeed * loopTime;
                        break;
                    case 2:
                        yVelocity -= lg4.hole.windSpeed * loopTime;
                        break;
                    case 3: 
                        xVelocity -= lg4.hole.windSpeed * loopTime;
                        break; 
                }
                xVelocity += spinLR * 2 * Math.cos(spinLRdir) * loopTime;
                yVelocity += spinLR * 2 * Math.sin(spinLRdir) * loopTime;
            }
            if (repaint) 
                lg4.win.repaint();
            repaint = !repaint;
        }
        lg4.hitStatus = lg4.NOT_HITTING;
    }
}
