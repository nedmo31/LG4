package lg4;

import java.awt.Color;
import java.awt.Polygon;

import javax.swing.JFrame;

/**
 * Class that contains the main method.
 * 
 * Linear Golf 4 will be a 2d golf game from birds eye view. 
 * The game will run on a single thread and will be driven by 
 * user inputs. 
 */
public class lg4 {
    
    static JFrame win;
    static Hole hole;
    static Course course;
    static Golfer player;
    static int screenWidth = 1400, screenHeight = 800;

    public static void main(String[] args) {

        hole = createTestHole();
        
        initGUI();

    }

    /**
     * Function to start up the graphics and open the window
     */
    static void initGUI() {
        win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(screenWidth, screenHeight);
        win.setVisible(true);
        win.add(new PixelArt(10));
    }

    static Hole createTestHole() {
        Polygon fairway = new Polygon();
        fairway.addPoint(300, 500); fairway.addPoint(350, 570); fairway.addPoint(400, 600); 
        fairway.addPoint(800, 600); fairway.addPoint(800, 300); fairway.addPoint(400, 300);
        fairway.addPoint(350, 230); fairway.addPoint(300, 400);
        Polygon green = new Polygon();
        green.addPoint(950, 180); green.addPoint(950, 100);
        green.addPoint(1050, 100);
        green.addPoint(950, 180);
        Polygon tees = new Polygon(); tees.addPoint(200, 200);
        tees.addPoint(260, 200);tees.addPoint(260, 300);tees.addPoint(200, 300);
        Hole testHole = new Hole();
        testHole.segments = new HoleSegment[] {
            new HoleSegment(fairway, Color.green), new HoleSegment(green, Color.CYAN),
            new HoleSegment(tees, Color.lightGray)
        };
        return testHole;
    }

}