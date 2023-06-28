package lg4;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.JFrame;

/**
 * Class that contains the main method.
 * 
 * Linear Golf 4 will be a 2d golf game from birds eye view. 
 */
public class lg4 {

    // Constants to keep track of hitting process
    public static final int NOT_HITTING = 0;
    public static final int AIMING = 1;
    public static final int AIMED = 2;
    public static final int SWINGING1 = 3;
    public static final int SWINGING2 = 4;
    public static final int BALL_MOVING = 5;
    public static final int PUTTING = 6;
    public static final int PUTT_ROLL = 7;
    
    /**
     * The window that the game runs in. JFrame object with an lg4.Window() object as a child
     */
    static JFrame win;
    /**
     * The current hole being played
     */
    static Hole hole;
    /**
     * The current course being player
     */
    static Course course;
    /**
     * The current player playing
     */
    static Golfer player;
    /**
     * The current ball being used
     */
    static Ball ball;
    /**
     * The dimensions of the screen. 
     */
    static int screenWidth = 1400, screenHeight = 800;
    /**
     * The current lg4.GraphicsStage to be displayed, starts at mainMenu
     */
    static GraphicsStage gStage = GraphicsStage.play;

    /**
     * An int to keep track of the stage in the hitting process
     */
    static int hitStatus = NOT_HITTING;
    
    /**
     * The current club being used by the Golfer
     */
    static Club club;

    // variables for a hit
    static double hitPower = 0, targetPower = 1;
    static double xyAngle = 0;
    static double hitSpinUpDown = 0;
    static double hitSpinLeftRight = 0;
    
    static long swingSpeed = 4;

    public static void main(String[] args) {

        initTest();
        //hole = createTestHole();
        course = new Course();
        
        initGUI();
        win.repaint();

        ball.x = 230; ball.y = 530;

        System.out.println(course.playCourse(3));

        //TODO FOR TESTING
        while (true) {
            win.repaint();
        }
    }

    /**
     * Function to start up the graphics and open the window
     */
    static void initGUI() {
        win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(screenWidth, screenHeight);
        win.setVisible(true);
        win.add(new Window());
    }

    static void initTest() {
        player = new Golfer();
        ball = new Ball(1, 1, .5);
        //club = player.clubs[0];
        club = new Club("first", .8, 100);
    }

    static Hole createTestHole() {
        Polygon fairway = new Polygon();
        fairway.addPoint(215, 505); fairway.addPoint(272, 505); fairway.addPoint(295, 485);
        fairway.addPoint(300,315); fairway.addPoint(338, 244); fairway.addPoint(380, 222);
        fairway.addPoint(381, 157); fairway.addPoint(349, 129); fairway.addPoint(311, 127);
        fairway.addPoint(266, 144); fairway.addPoint(207, 210); fairway.addPoint(200, 259);
        fairway.addPoint(200, 485);
        Polygon tees = new Polygon(); tees.addPoint(190,520);
        tees.addPoint(260, 520);tees.addPoint(260, 550);tees.addPoint(190, 550);
        Polygon river = new Polygon(); river.addPoint(0, 394); river.addPoint(218, 357);
        river.addPoint(283, 358); river.addPoint(615, 401); river.addPoint(615, 428);
        river.addPoint(280, 390); river.addPoint(222, 389); river.addPoint(0, 422);

        Hole testHole = new Hole();
        testHole.segments = new HoleSegment[] {
            new Green(new Ellipse2D.Double(400, 64, 85, 75)), new HoleSegment(fairway, Color.green, .65),
            new HoleSegment(tees, Color.lightGray, .8), new Water(river), 
            new Sand(new Ellipse2D.Double(336, 154, 36, 50))
        };
        return testHole;
    }

}