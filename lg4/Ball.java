package lg4;

import java.awt.Color;

public class Ball {

    // Gravity!
    public static double gravity = 9.8;

    // x and y correspond to the screen
    // and z is the height
    double x, y, z;

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

    void updateBall(double xv, double yv, double zv, double time) {
        this.x += xv * time; this.y += yv * time; this.z += zv * time;
    }

    // It will be useful later to get integer values for xyz
    int x() { return (int)x; }
    int y() { return (int)y; }
    int z() { return (int)z; }

    /**
     * the fuction that handles the ball being hit.
     *  
     * @param club The club used to hit the ball
     * @param pow value between 0 and 1 for what percentage of power was used
     * @param xyAng the angle in radians in the xy plan that the ball was hit
     */
    public void hit(Club club, double pow, double xyAng, double targetSpinLR, double targetSpinUD) {
        // The intitial velocity of the ball
        // uses the club power, player power, power of the shot, and weight of the ball
        double velocity = club.power * (lg4.player.power / 10) * pow * (1 / weight), xVelocity, yVelocity, zVelocity;
        // The spin on the ball in [-1, 1], 0 being none, 1 being strong
        // This is just for left/right spin
        double spinLR = targetSpinLR + 
            (.5-Math.random())*((11 - lg4.player.consistency) / 10); // random error based on player's consistency

        // This keeps track of the direction of the spin in radians
        double spinLRdir;
        // If it's to the left, we take the initial angle 90 degrees to the left. Otherwise to the right
        if (spinLR >= 0) {
            spinLRdir = xyAng - Math.PI/2;
        } else {
            spinLRdir = xyAng + Math.PI/2;
        }
        //This is the top/back spin on the ball, it will be a bit simpler to keep track of.
        // [-1, 1], positive for top spin
        double spinUD = targetSpinUD + 
            (.5-Math.random())*((11 - lg4.player.consistency) / 10); // random error based on player's consistency

        // Split velocity into components
        if (xyAng < 0) {
            xVelocity = (-1)*(Math.cos(xyAng))*velocity;
            yVelocity = (Math.sin(xyAng))*velocity;
        } else {
            xVelocity = (Math.cos(xyAng))*velocity;
            yVelocity = (-1)*(Math.sin(xyAng))*velocity;
        }
        zVelocity = Math.sin(club.angle) * velocity;
        
        System.out.println("Hitting ball with velocities x: " + xVelocity + 
        ", y: " + yVelocity + ", z: " +zVelocity);
        System.out.println("Hitting ball with spin direction: " + spinLRdir);
        System.out.println("Xcomp: " + Math.cos(spinLRdir) + "\tYcomp: " + Math.sin(spinLRdir));

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
            loopTime = (System.nanoTime() - startTime) / 1000000000 * 3;
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
                } else {
                    double slowDown = (this.bounce + seg.bounce) / 2;
                    slowDown *= 1 + (spinUD / 5); // multiplies it by value in [.8, 1.2]
                    zVelocity *= -1 * slowDown;
                    xVelocity *= slowDown;
                    yVelocity *= slowDown;
                    if (xVelocity < .01)
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
            lg4.win.repaint();
        }

    }

    class Force {

        /*
         * the maginitude and direction of the force.
         * dir should be a number [-Math.PI, Math.PI]
         */
        double mag, dir;

        public Force(Force[] forces) {
            double totalX = 0, totalY = 0;
            for (Force f : forces) {
                totalX += f.getXComp();
                totalY += f.getYComp();
            }
            mag = Math.sqrt(totalX*totalX + totalY * totalY);
            dir = Math.atan(totalY / totalX);
        }

        public Force(double xComp, double yComp, boolean extra) {
            this(Math.sqrt(xComp*xComp + yComp * yComp), Math.atan(yComp / xComp));
        }

        public Force(double m, double d) {
            mag = m; dir = d;
        }

        double getXComp() {
            if (dir >= 0 && dir <= Math.PI)
                return mag * Math.cos(dir);
            return -1 * mag * Math.cos(-dir);
        }

        double getYComp() {
            if (dir >= 0 && dir <= Math.PI)
                return mag * Math.sin(dir);
            return -1 * mag * Math.sin(-dir);
        }

    }
}
