package lg4;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * This class represents a playable hole in golf. The image of this
 * will be the main graphics on the screen. 
 * 
 */
public class Hole {
    
    HoleSegment rough = new Rough(
        new Rectangle(0, 0, lg4.screenWidth, lg4.screenHeight), Color.green, .40);
    
    /**
     * This contains all the parts for the hole including the green,
     * fairway, water, sand, teebox, and anything else. At a minimum,
     * The hole should have a teebox and a green (which has the hole in it)
     * 
     * This array should be ordered by layer of output
     * Whatever comes last will be painted on top of others things
     */
    HoleSegment[] segments;

    /**
     * x and y are the location of the hole
     */
    int x, y, par, windDir, windSpeed;
    // TO DO: improve upon wind. 0 north, 1 east, 2 south, 3 west for windDir

    /**
     * Note: looks backwards through the array to find first segment
     * that the ball is in. So, if there is a river running through the
     * fairway, it would find the river first
     * 
     * @return the segment the ball is currently over
     */
    HoleSegment whatSegment(Ball ball) {
        for (int i = segments.length-1; i >= 0; i--) {
            if (segments[i].contains(ball.x(), ball.y()))
                return segments[i];
        }
        return rough;
    }

    public Hole(HoleSegment[] segments, int x, int y, int par, int windDir, int windSpeed) {
        this.segments = segments; this.x = x; this.y = y; this.par = par; this.windDir = windDir; this.windSpeed = windSpeed;
    }

    /** 
     * A blank constructor to make a randomized Hole
     * God help me make this 
     */
    public Hole() {
        /*
         * Randomize some values for par, distance, waters, sands, maybe type?
         * Types: 
         *  - 0: short, par 3 with fairway, teebox, green
         *  - 1: narrow forest, par 4/5 straight with trees
         *  - 2: Snake, par 4/5 that goes back and forth
         *  - 3: Dog-leg, par 4/5 that makes a sharp turn
         * 
         * Make a teebox on some set range near the left side of screen
         * 
         * Fairway >> 
         *  make a tree of circles and for each connect it with its children
         * 
         */

        // Randomize the type of hole 
        int type = (int)(4*Math.random());
        if (lg4.editor) { type = -1; }
        MapCreationTree tree = new MapCreationTree();
        TeeBox teebox = new TeeBox();

        windSpeed = (int)(11*Math.random());
        windDir = (int)(4*Math.random());

        if (type == 0) {
            par = 3;
            segments = new HoleSegment[4];
            segments[0] = teebox;
            // randomize the hole location from where the teebox is
            double holex = teebox.area.getBounds2D().getCenterX() + 400;
            double holey = teebox.area.getBounds2D().getCenterY() + (int)(50-Math.random()*100);

            // set the instance variables to our randomized location
            this.x = (int)holex;
            this.y = (int)holey;

            for (int i = 0; i < 3; i++) {                
                tree.add(new Oval(this.x - (200 - (i*75)), this.y + (int)(50-Math.random()*100), (int)(50+Math.random()*50), (int)(50+Math.random()*50)));
            }
            Polygon[] fairwayChunks = tree.getPolygonRepresentation();
            for (int i = 1; i < par; i++) {
                segments[i] = new Fairway(fairwayChunks[i-1]);
            }

            // Add the green to the array
            segments[3] = new Green(new Ellipse2D.Double(
                holex, holey,
                70 + (int)(25-Math.random()*50), 70 + (int)(25-Math.random()*50)
            ));
        } else if (type == 1) { // Narrow forest, par 4/5
            par = 4 + (int)(Math.random() * 2);
            int lastx = (int)teebox.area.getBounds2D().getCenterX() + 50;
            int lasty = (int)teebox.area.getBounds2D().getCenterY();
            
            for (int i = 0; i < par; i++) {
                tree.add(new Oval(lastx = (lastx + (int)(Math.random()*100 + 75)), lasty = (lasty + (int)(20-Math.random()*10)), (int)(50+Math.random()*50), (int)(30+Math.random()*20)));
            }

            segments = new HoleSegment[1+par];
            segments[0] = teebox;
            Polygon[] fairwayChunks = tree.getPolygonRepresentation();
            for (int i = 1; i < par; i++) {
                segments[i] = new Fairway(fairwayChunks[i-1]);
            }
            // // Add forest above and below
            // segments[par] = getForest(fairwayChunks, roughStartX, roughStartX+par*140, true);
            // segments[par+1] = getForest(fairwayChunks, roughStartX, roughStartX+par*140, false);

            // Add the green to the array
            segments[segments.length-1] = new Green(new Ellipse2D.Double(
                lastx+75, lasty,
                70 + (int)(25-Math.random()*50), 70 + (int)(25-Math.random()*50)
            ));
            // make segments array at the end
        } else if (type == 2) { // Snake, par 4/5 that goes back and forth
            par = 4 + (int)(Math.random() * 2); 
            int lastx = (int)teebox.area.getBounds2D().getCenterX();
            int lasty = (int)teebox.area.getBounds2D().getCenterY()-75;
            boolean up = true;
            
            for (int i = 0; i < par; i++) {
                if (up) {
                    tree.add(new Oval(lastx = (lastx + (int)(Math.random()*100 + 100)), lasty = (lasty + (int)(Math.random()*100 + 75)), (int)(50+Math.random()*50), (int)(50+Math.random()*50)));
                } else {
                    tree.add(new Oval(lastx = (lastx + (int)(Math.random()*100 + 100)), lasty = (lasty - (int)(Math.random()*100 + 75)), (int)(50+Math.random()*50), (int)(50+Math.random()*50)));
                } 
                up = !up;
            }

            segments = new HoleSegment[2+par-1];
            segments[0] = teebox;
            Polygon[] fairwayChunks = tree.getPolygonRepresentation();
            for (int i = 1; i < par; i++) {
                segments[i] = new Fairway(fairwayChunks[i-1]);
            }
            

            // Add the green to the array
            segments[segments.length-1] = new Green(new Ellipse2D.Double(
                lastx+75, up?lasty+75:lasty-75,
                70 + (int)(25-Math.random()*50), 70 + (int)(25-Math.random()*50)
            ));
            // make segments array at the end
        } else if (type==3) {
            par = 4;
            boolean up = ((int)(Math.random()*2)) == 1;
            if (up) {
                teebox = new TeeBox(200);
            } else {
                teebox = new TeeBox(-150);
            }
            int lastx = (int)teebox.area.getBounds2D().getCenterX() + 50;
            int lasty = (int)teebox.area.getBounds2D().getCenterY();
            
            for (int i = 0; i < 3; i++) {
                tree.add(new Oval(lastx = (lastx + (int)(Math.random()*100 + 125)),
                     lasty = (lasty + (int)(20-Math.random()*10)), (int)(50+Math.random()*50), (int)(30+Math.random()*20)));
            } for (int i = 0; i < 2; i++) {
                tree.add(new Oval(lastx = (lastx + (int)(Math.random()*10 + 10)),
                    lasty = up ? (lasty - (int)(Math.random()*50+100)) : (lasty + (int)(Math.random()*50+100)), 
                    (int)(50+Math.random()*50), (int)(30+Math.random()*20)));
            }

            segments = new HoleSegment[5];
            segments[0] = teebox;
            Polygon[] fairwayChunks = tree.getPolygonRepresentation();
            for (int i = 1; i < 4; i++) {
                segments[i] = new Fairway(fairwayChunks[i-1]);
            }

            // Add the green to the array
            segments[segments.length-1] = new Green(new Ellipse2D.Double(
                lastx, up ? lasty - 70 : lasty + 70,
                70 + (int)(25-Math.random()*50), 70 + (int)(25-Math.random()*50)
            ));
        } else if (type == -1) {
            par = 3;
            segments = new HoleSegment[2];
            teebox = new TeeBox((int)(150-Math.random()*75));
            segments[0] = teebox;
            // randomize the hole location from where the teebox is
            double holex = teebox.area.getBounds2D().getCenterX() + 800 + (int)(100-Math.random()*200);
            double holey = teebox.area.getBounds2D().getCenterY() + (int)(150-Math.random()*300);

            // set the instance variables to our randomized location
            this.x = (int)holex;
            this.y = (int)holey;

            // Add the green to the array
            segments[1] = new Green(new Ellipse2D.Double(
                holex, holey,
                70 + (int)(25-Math.random()*50), 70 + (int)(25-Math.random()*50)
            ));
        }
    }

    int playHole() {
        lg4.ball.x = segments[0].area.getBounds().getCenterX();
        lg4.ball.y = segments[0].area.getBounds().getCenterY();

        int strokes = 0;
        // while the ball isn't on the green
        while (! (whatSegment(lg4.ball) instanceof Green)) {

            lg4.hitStatus = lg4.AIMING;
            while (!(lg4.hitStatus == lg4.BALL_MOVING)) {
                //lg4.win.repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lg4.ball.hit(lg4.hitPower, lg4.xyAngle, lg4.hitSpinLeftRight, lg4.hitSpinUpDown);
            strokes++;
        }
        Green g = ((Green)segments[segments.length-1]);
        int xStartPutt = lg4.screenWidth/2 + Green.HOLE_SIZEUP*(lg4.ball.x() - (int)g.area.getBounds2D().getCenterX());
        int yStartPutt = lg4.screenHeight/2 + Green.HOLE_SIZEUP*(lg4.ball.y() - (int)g.area.getBounds2D().getCenterY());
        strokes += g.playGreen(xStartPutt, yStartPutt);

        lg4.player.money += 12-strokes;

        System.out.println("Finished hole with "+strokes+" strokes");
        return strokes;
    }

    public void holeInfo() {
        System.out.println("Hole with "+segments.length+" segments");
        for (int i = 0; i < segments.length; i++) {
            System.out.println("Segment "+i+": "+segments[i].getClass().getSimpleName());
            System.out.println(segments[i].area.getClass().getSimpleName());
        }
    }

    public void addSegment(HoleSegment hs) {
        HoleSegment[] newSegs = new HoleSegment[segments.length+1];
        for (int i = 0; i < segments.length-1; i++) {
            newSegs[i] = segments[i];
        }
        newSegs[segments.length] = segments[segments.length-1];
        newSegs[segments.length-1] = hs;
        segments = newSegs;
    }

    /**
     * This class helps in random hole generation. It is actually a LinkedList 
     * right now, but it could expand to a tree. Adding ovals to this tree will help
     * create the fairway in a hopefully interesting and good-looking way. 
     */
    class MapCreationTree {

        /**
         * The first Node in the tree/list
         */
        MCTreeNode root = null;
        int size = 0;

        void add(Oval m) {
            if (root == null) {
                root = new MCTreeNode(m);
                size++;
            } else {
                add(m, root);
            }
        }
        void add(Oval m, MCTreeNode current) {
            if (current.child == null) {
                size++;
                current.addChild(new MCTreeNode(m));
            } else {
                add(m, current.child);
            }
        }

        /**
         * This function will return a polygon approximation of the entire tree. This is what
         * will become the HoleSegment 
         * @return a Polygon representation of the tree
         */
        Polygon[] getPolygonRepresentation() {
            // TO DO: Polygon.addpoint is slow, copies the array every single time. Much better to 
            // make an array and create polygon object at the end.
            Polygon[] p = new Polygon[size-1];
            if (root != null) {
                for (int i = 0; i < p.length; i++) {
                    Polygon pol = new Polygon();
                    for (Point point : root.node.getPointsFromSector(root.angleToChild+90, root.angleToChild + 270)) {
                        pol.addPoint(point.x, point.y);
                    }
                    if (root.child != null) {
                        addPolygonPoints(pol, root.child, root.angleToChild);
                        root = root.child;
                    }
                    p[i] = pol;
                }
            } 
            else {
                System.out.println("Root is null");
            }
            return p;
        }

        /**
         * This is a recursive function to help add all the points to the polygon
         * that will be returned in getPolygonRepresentation()
         */
        void addPolygonPoints(Polygon p, MCTreeNode node, int angleIn) {
            // if (node.child == null) {
                for (Point point : node.node.getPointsFromSector(270 + angleIn, 450 + angleIn)) {
                    p.addPoint(point.x, point.y);
                }
            // } else {
            //     Point temp = node.node.getPointFromAngle(270);
            //     p.addPoint(temp.x, temp.y);
            //     addPolygonPoints(p, node.child, node.angleToChild);
            //     temp = node.node.getPointFromAngle(90);
            //     p.addPoint(temp.x, temp.y);
            // }
        }
        /**
         * This is an inner class representing the nodes of the tree.
         * As of now, they have only one child, so it's basically a list
         */
        class MCTreeNode {
            Oval node;
            MCTreeNode child = null;

            /**
             * The angle to the node's child in degrees
             */
            int angleToChild = 0;

            public MCTreeNode(Oval n) {
                node = n;
            }

            void addChild(MCTreeNode mctn) {
                child = mctn;
                // Use the Math class to get the right angle to child
                int y = mctn.node.y - node.y;
                int x = mctn.node.x - node.x;
                angleToChild = (int)Math.toDegrees(Math.atan2(y, x));
            }

        }
    }

    

    class Oval {

        /**
         * The x-coordinate center of the Oval
         */
        int x;
        /**
         * The y-coordinate of the center of the Oval
         */
        int y;
        int width, height;

        final static int degreesPerPoint = 10;

        public Oval(int x, int y, int w, int h) {
            this.x = x; this.y = y;
            width = w; height = h;
        }


        /**
         * A function to get a point approximation of the outside of the oval.
         * Going counterclockwise from startDegree to endDegree, provides a point
         * for every 15 degrees
         * @param startDegree 
         * @param endDegree
         * @return
         */
        Point[] getPointsFromSector(int startDegree, int endDegree) {
            Point[] p = new Point[(endDegree-startDegree)/degreesPerPoint + 1];
            int i = 0;
            while (startDegree <= endDegree) {
                p[i] = new Point(x + (int)(width * Math.cos(Math.toRadians(startDegree))), 
                                 y + (int)(height * Math.sin(Math.toRadians(startDegree))));
                i++;
                startDegree += degreesPerPoint;
            }
            return p;
        }

        Point getPointFromAngle(int angle) {
            return new Point(x + (int)(width * Math.cos(Math.toRadians(angle))), 
                             y + (int)(height * Math.sin(Math.toRadians(angle))));
        }

        /*
         * To Do, change this to return an array of points to smooth out the fairway
         */
        Point getPointFromAngle2(int angle) {
            return new Point(x + (int)(width * 1.4 * Math.cos(Math.toRadians(angle))), 
                             y + (int)(height * 1.4 * Math.sin(Math.toRadians(angle))));
        }

    }

}
