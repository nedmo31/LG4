package lg4;

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

    public static void main(String[] args) {

        initGUI();

    }

    /**
     * Function to start up the graphics and open the window
     */
    static void initGUI() {
        win = new JFrame();
    }

}