package lg4;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

/**
 * This class represents the Greens on a hole
 * This is where the actuall hole and flag sits
 */
public class Green extends HoleSegment {

    // how much bigger to make the greens when putting.
    // should be an even number
    final int HOLE_SIZEUP = 8;
    
    public static Color greensColor = Color.lightGray;

	public int mapHoleX, mapHoleY;

	// ball location and physics vars
    public double holeX, holeY, ballX, ballY, xAcc, yAcc;
	public int
		botLeftSlope = (int)(5*Math.random()), botRightSlope = (int)(5*Math.random()),
		topLeftSlope = (int)(5*Math.random()), topRightSlope = (int)(5*Math.random());

    public Green(Ellipse2D ellipse) {
        super(ellipse, greensColor, .75);
		double xOffsetPercentage = (.7-1.4*Math.random());
		double yOffsetPercentage = (.7-1.4*Math.random());
		mapHoleX = (int)ellipse.getCenterX() + (int)((ellipse.getMaxX()-ellipse.getCenterX())*xOffsetPercentage);
		mapHoleY = (int)ellipse.getCenterY() + (int)((ellipse.getMaxY()-ellipse.getCenterY())*yOffsetPercentage);
		holeX = lg4.screenWidth/2 + xOffsetPercentage * 25 * HOLE_SIZEUP;
		holeY = lg4.screenHeight/2 + yOffsetPercentage * 25 * HOLE_SIZEUP;
    }


    public void paintArea(Graphics g) {
        g.setColor(color);
        Ellipse2D temp = (Ellipse2D)area;
        g.fillOval((int)temp.getX(), (int)temp.getY(), (int)temp.getWidth(), (int)temp.getHeight());
		g.setColor(Color.darkGray);
		g.fillRect(mapHoleX-5, mapHoleY-15, 5, 15);
		g.setColor(Color.red);
		g.fillRect(mapHoleX-15, mapHoleY-15, 10, 7);
    }

    public void paintAreaPutting(Graphics g) {
        g.setColor(color);
        Ellipse2D temp = (Ellipse2D)area;
        int height = (int)temp.getHeight() * HOLE_SIZEUP;
        int width = (int)temp.getWidth() * HOLE_SIZEUP;
        g.fillOval(lg4.screenWidth/2 - width/2, lg4.screenHeight/2 - height/2, width, height);

        g.setColor(Color.BLACK);
        g.fillOval((int)holeX - 10, (int)holeY - 10, 20, 20);
        g.setColor(lg4.ball.color);
        g.fillOval((int)ballX - 6, (int)ballY - 6, 12, 12);
        g.setColor(Color.BLACK);
        g.drawOval((int)ballX - 6, (int)ballY - 6, 12, 12);
        
        for (Point p : puttingTrajectory()) {
			g.fillOval(p.x-2, p.y-1, 5, 5);
		}
    }

    public int playGreen(double x, double y) {
        lg4.hitStatus = lg4.PUTTING;
        ballX = x; ballY = y;
        int putts = 0;
		while (!inHole(ballX, ballY)) {
			lg4.win.repaint();
            if (lg4.hitStatus == lg4.PUTT_ROLL) {
            	putt(lg4.hitPower, lg4.xyAngle);
            	putts++;
            	lg4.hitStatus = lg4.PUTTING;
            }
		}
		System.out.println("Done putting!");
		lg4.hitStatus = lg4.NOT_HITTING;
		return putts;
    }

    public void putt(double pow, double ang) {

        int centerx = lg4.screenWidth/2;
        int centery = lg4.screenHeight/2;

		double v = pow*.75;
		System.out.println(ang);
        double yVelocity = -v * Math.sin( ang );
        double xVelocity = v * Math.cos( ang ); 
        //ballX += xVelocity; 
        //ballY += yVelocity;
        double time = System.nanoTime(), looptime;
        while (Math.abs(v) > .1 ) {
        	
        	
        	looptime = (System.nanoTime() - time)  / Math.pow(10, 9) * 6;
        	time = System.nanoTime();
        	lg4.win.repaint();
        	
        	if (ballX <= centerx && ballY <= centery) { // top left
        		slopeAcceleration(topLeftSlope);
        	} else if (ballX <= centerx && ballY > centery) { // bottom left
        		slopeAcceleration(botLeftSlope);
        	} else if (ballY <= centery) { // top right
        		slopeAcceleration(topRightSlope);
        	} else { // bottom right
        		slopeAcceleration(botRightSlope);
        	}
        	
        	ballX += (xVelocity * looptime);
        	ballY += (yVelocity * looptime);
        	//v = Math.sqrt(xVelocity*xVelocity + yVelocity*yVelocity);
        	ang = -Math.atan2(yVelocity, xVelocity);
        	//System.out.println(ang);
        	if (v > 0)
        		v -= 6*looptime;
        	else
        		v += 6*looptime;
        	yVelocity = -v * Math.sin( ang );
            xVelocity = v * Math.cos( ang );
            xVelocity += xAcc * looptime;
            yVelocity += yAcc * looptime;
            if (inHole(ballX, ballY) && v < (5 + .4*lg4.player.putting)
            		|| reallyInHole(ballX, ballY) && v < (22 + .2*lg4.player.putting)) {
            	break;
            }
            
        }
	}

    //TODO make switch
    public void slopeAcceleration(int n) {
		if (n == 0) {
			yAcc = xAcc = 0;
		} else if (n == 1) {
			yAcc = 0; xAcc = 5;
		} else if (n == 2) {
			yAcc = 0; xAcc = -5;
		} else if (n == 3) {
			yAcc = -5; xAcc = 0;
		} else {
			yAcc = 5; xAcc = 0;
		}
	}

    public void flattenSlope() {
		botLeftSlope = botRightSlope = topLeftSlope = topRightSlope = 0;
	}
	
	public boolean inHole(double x, double y) {
		return Math.abs(holeX - x) < (8 + .4*lg4.player.putting) && 
				Math.abs(holeY - y) < (8 + .4*lg4.player.putting);
	}
	public boolean reallyInHole(double x, double y) {
		return Math.abs(holeX - x) < (5 + .4*lg4.player.putting) && 
				Math.abs(holeY - y) < (5 + .4*lg4.player.putting);
	}

    public Point[] puttingTrajectory() {
		int length = (int) (lg4.hitPower / 10);
		Point[] p = new Point[length];

		double v = lg4.hitPower * 1.5;
		double initialx = ballX;
		double initialy = ballY;
		double yVelocity = v * Math.sin(lg4.xyAngle);
		double xVelocity = v * Math.cos(lg4.xyAngle);

		for (int i = 0; i < length; i++) {
			p[i] = new Point((int) (initialx + xVelocity * (.3 * i)), // constant velocity in x-dir
					(int) (initialy - yVelocity * (.3 * i)));
		}

		return p;
	}

}
