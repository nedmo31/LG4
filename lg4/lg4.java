package lg4;

import java.awt.*;
import java.awt.geom.Ellipse2D;
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
    static Ball ball;
    static int screenWidth = 1400, screenHeight = 800;

    public static void main(String[] args) {

        initTest();
        hole = createTestHole();
        //hole.windDir = 1;
        //hole.windSpeed = 1;
        
        initGUI();
        win.repaint();

        ball.x = 230; ball.y = 530;
        ball.hit(new Club("test", 1.2, 50), .8, 1.5, 1, 0);
    }

    /**
     * Function to start up the graphics and open the window
     */
    static void initGUI() {
        win = new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(screenWidth, screenHeight);
        win.setVisible(true);
        win.add(new PixelArt());
    }

    static void initTest() {
        player = new Golfer();
        ball = new Ball(1, 1, .5);
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