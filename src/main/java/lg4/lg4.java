package lg4;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JFrame;

import com.google.gson.*;

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
        hole = course.holes[0];
        
        initGUI();
        win.repaint();

        ball.x = 230; ball.y = 530;
        win.repaint();

        jsonStuff();

        System.out.println(course.playCourse(3));
    }

    public static void jsonStuff() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Shape.class, new ShapeAdapter());
        gb.registerTypeAdapter(HoleSegment.class, new SegmentDeserializer());
        gb.registerTypeAdapter(HoleSegment.class, new SegmentSerializer());
        Gson gson = gb.create();
        try {
            File f = new File("course.json");
            PrintWriter pw = new PrintWriter(f);
            pw.write(gson.toJson(course, Course.class));
            pw.close();
            Scanner fr = new Scanner(f);
            String s = fr.next();
            course = gson.fromJson(s, Course.class);
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        player.clubs = clubs;
        ball = new Ball(1, 1, .5);
        club = player.clubs[0];
        for (Club c : player.clubs) {
            c.updateRadius();
        }
    }

    public static Club[] clubs = new Club[]{ 
        new Club("Driver", .5, 110, .4, .2), 
        new Club("3 Wood", .55, 95, .5, .25),
        new Club("5 Iron", .75, 80, .9, .7),
        new Club("6 Iron", .8, 75, .9, .7),
        new Club("7 Iron", .85, 70, .9, .7),
        new Club("8 Iron", .9, 65, .9, .7),
        new Club("9 Iron", .95, 60, .9, .7),
        new Club("P Wedge", 1.1, 60, .9, .7),
        new Club("S Wedge", 1.2, 60, .9, .7) };

}