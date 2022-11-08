package lg4;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class GraphicsStage {
    
    Button [] buttons;
    String name;

    public GraphicsStage(String n, Button[] b) {
        name = n; 
        buttons = b;
    }

    void checkButtonClick(MouseEvent e) {
        System.out.println("Checking button click ("+e.getX()+","+e.getY()+") on "+this.name);
        for (Button b : buttons) {
            if (b.area.contains(e.getX(), e.getY()))
                b.clickAction();
        }
    }

    void paintGraphicsStage(java.awt.Graphics g) {
        for (Button b : buttons) {
            b.paintButton(g);
        }
    }

    public static GraphicsStage mainMenu = new GraphicsStage("Menu", new Button[]{ 
        new TextButton(new Rectangle(100,100,100,100), "Play", Color.black){
            public void clickAction() {
                lg4.gStage = GraphicsStage.play;
            }
        } }) {
        void paintGraphicsStage(java.awt.Graphics g) {
            play.paintGraphicsStage(g);
            g.setColor(Color.black);
            super.paintGraphicsStage(g);
        }
    };

    public static GraphicsStage play = new GraphicsStage("Play", new Button[0]) {
        void paintGraphicsStage(java.awt.Graphics g) {
            // This will fill the whole background with dark green to represent the rough
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);

            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }

            if (!lg4.hit) {
                g.setColor(Color.black);
                g.drawOval(lg4.ball.x()-lg4.club.radius/2, lg4.ball.y()-lg4.club.radius/2, lg4.club.radius, lg4.club.radius);
            }

            // Ball + Extras
            int xInSky = lg4.ball.xInSky();
            int yInSky = lg4.ball.yInSky();
            g.setColor(Color.DARK_GRAY);
            g.fillOval(lg4.ball.xCorrected(), lg4.ball.yCorrected(), lg4.ball.size, lg4.ball.size);
            g.setColor(lg4.ball.color);
            g.fillOval(xInSky, yInSky, lg4.ball.size, lg4.ball.size);
            g.setColor(Color.black);
            g.drawOval(xInSky, yInSky, lg4.ball.size, lg4.ball.size);
        }
    };

}
