package lg4;

import java.awt.*;

/**
 * This class represents a playable hole in golf. The image of this
 * will be the main graphics on the screen. 
 * 
 */
public class Hole {
    
    HoleSegment rough = new HoleSegment(
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

    /** 
     * A blank constructor to make a randomized Hole
     * God help me make this 
     */
    public Hole() {
        /*
         * Randomize some values for par, distance, waters, sands, maybe type?
         * Types: 
         *  - 0: short, par 3 with fairway, teebox, green
         *  - 1: pitch&putt, par 3 just teebox and green
         *  - 2: Snake, par 4/5 that goes back and forth
         *  - 3: Dog-leg, par 4/5 that makes a sharp turn
         *  - 4: Choice, par 4/5 with left and right option
         * 
         * Make a teebox on some set range near the left side of screen
         * 
         * Fairway >> 
         *  make a tree of circles and for each connect it with its children
         * 
         */
        //int type = (int)(5*Math.random());
        

    }

    /**
     * This class helps in random hole generation. It is actually a LinkedList r
     * right now, but it could expand to a tree. Adding ovals to this tree will help
     * create the fairway in a hopefully interesting and good-looking way. 
     */
    class MapCreationTree {

        /**
         * The first Node in the tree/list
         */
        MCTreeNode root = null;

        void add(Oval m) {
            if (root == null) {
                root = new MCTreeNode(m);
            } else {
                add(m, root);
            }
        }
        void add(Oval m, MCTreeNode current) {
            if (current.child == null) {
                current.child = new MCTreeNode(m);
            } else {
                add(m, current.child);
            }
        }

        /**
         * This function will return a polygon approximation of the entire tree. This is what
         * will become the HoleSegment 
         * @return a Polygon representation of the tree
         */
        Polygon getPolygonRepresentation() {
            // TO DO: Polygon.addpoint is slow, copies the array every single time. Much better to 
            // make an array and create polygon object at the end.
            Polygon p = new Polygon();
            if (root != null) {
                for (Point point : root.node.getPointsFromSector(root.angleToChild+90, root.angleToChild + 270)) {
                    p.addPoint(point.x, point.y);
                }
                if (root.child != null)
                    addPolygonPoints(p, root.child, root.angleToChild);
            }  else {
                System.out.println("Root is null");
            }
            return p;
        }

        /**
         * This is a recursive function to help add all the points to the polygon
         * that will be returned in getPolygonRepresentation()
         */
        void addPolygonPoints(Polygon p, MCTreeNode node, int angleIn) {
            if (node.child == null) {
                System.out.println("last treenode");
                for (Point point : node.node.getPointsFromSector(270 + angleIn, 450 + angleIn)) {
                    p.addPoint(point.x, point.y);
                }
            } else {
                Point temp = node.node.getPointFromAngle(270);
                p.addPoint(temp.x, temp.y);
                addPolygonPoints(p, node.child, node.angleToChild);
                temp = node.node.getPointFromAngle(90);
                p.addPoint(temp.x, temp.y);
            }
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
                int y = mctn.node.y - this.node.y;
                int x = mctn.node.x - this.node.x;
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

        static int degreesPerPoint = 10;

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
