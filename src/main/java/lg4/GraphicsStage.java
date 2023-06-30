package lg4;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;

public class GraphicsStage {
    
    Button [] buttons;
    String name;
    int id;

    public GraphicsStage(String n, Button[] b, int id) {
        name = n; 
        buttons = b;
        this.id = id;
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
        } }, 0) {
        void paintGraphicsStage(java.awt.Graphics g) {
            play.paintGraphicsStage(g);
            g.setColor(Color.black);
            super.paintGraphicsStage(g);
        }
    };

    public static GraphicsStage play = new GraphicsStage("Play", new Button[0], 1) {
        void paintGraphicsStage(java.awt.Graphics g) {
            // This will fill the whole background with dark green to represent the rough
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);

            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }

            if (lg4.hitStatus == lg4.AIMING) {
                
                g.setColor(new Color(180, 100, 100, 160));
                int offset = (15 - lg4.player.accuracy)* 4;
                g.fillOval(Window.mx-offset/2, Window.my-offset/2, offset, offset);
                
                g.setColor(Color.black);
                g.drawOval(lg4.ball.x()-lg4.club.radius/2, lg4.ball.y()-lg4.club.radius/2, lg4.club.radius, lg4.club.radius);
            } else if (lg4.hitStatus == lg4.AIMED) {
                
                g.setColor(new Color(180, 100, 100, 160));
                int offset = (15 - lg4.player.accuracy)* 4;
                g.fillOval(Window.mx-offset/2, Window.my-offset/2, offset, offset);
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

            // Show swinging bar. Just gonna fix it to a point, TODO make it based on screen size
            g.setColor(Color.black);
            g.drawRect(400, 600, 600, 50);
            g.fillRect(898, 600, 4, 50);
            g.fillOval((int)(900-(lg4.targetPower)*500), 575, 10, 10);

            if (lg4.hitStatus == lg4.SWINGING1) {
                double percentage = (System.nanoTime() - Window.swingFirst)/(double)(Window.targetFirst-Window.swingFirst);
                if (percentage > 1) {
                    g.fillOval((int)(900-(2-percentage)*500), 610, 15, 15);
                } else {
                    g.fillOval((int)(900-percentage*500), 610, 15, 15);
                }
            }
            if (lg4.hitStatus == lg4.SWINGING2) {
                double percentage = (System.nanoTime() - Window.swingFirst)/(double)(Window.targetFirst-Window.swingFirst);
                if (percentage > 1) {
                    g.fillOval((int)(900-(2-percentage)*500), 610, 15, 15);
                } else {
                    g.fillOval((int)(900-percentage*500), 610, 15, 15);
                }
            }

            else if (lg4.hitStatus == lg4.BALL_MOVING) {
                g.fillOval((int)(900-lg4.hitPower*500), 610, 15, 30);
                g.fillOval((int)(900+lg4.hitSpinLeftRight*100), 610, 15, 30);

                g.setColor(new Color(100, 100, 240, 220));
                g.fillOval((int)(400), 610, 15, 15);
                g.fillOval((int)(1000), 610, 15, 15);
                g.setColor(new Color(240, 100, 100, 220));
                g.fillOval((int)(800), 610, 15, 15);
            }
            

            
			if (lg4.hitStatus == lg4.PUTTING || lg4.hitStatus == lg4.PUTT_ROLL) {
                Green green = (Green)lg4.hole.segments[lg4.hole.segments.length-1];
				g.setColor(Color.black);
                int midX = lg4.screenWidth/2;
                int midY = lg4.screenHeight/2;
				int topCornerX = midX-300;
				int topCornerY = midY-225;
				g.fillRect(topCornerX-4, topCornerY-4, 608, 458);
				g.setColor(new Color(50, 250, 50));
				g.fillRect(topCornerX, topCornerY, 600, 450);
				green.paintAreaPutting(g);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20));

				// Draw arrows for green slope :(
				g.setColor(new Color(5, 5, 5, 100));
				g.fillPolygon(arrow(green.topLeftSlope, midX-130, midY-25));
				g.fillPolygon(arrow(green.topLeftSlope, midX-30, midY-125));
				g.fillPolygon(arrow(green.topLeftSlope, midX-30, midY-25));

				g.fillPolygon(arrow(green.topRightSlope, midX+30, midY-25));
				g.fillPolygon(arrow(green.topRightSlope, midX+30, midY-125));
				g.fillPolygon(arrow(green.topRightSlope, midX+130, midY-25));

				g.fillPolygon(arrow(green.botLeftSlope, midX-130, midY+25));
				g.fillPolygon(arrow(green.botLeftSlope, midX-30, midY+125));
				g.fillPolygon(arrow(green.botLeftSlope, midX-30, midY+25));

				g.fillPolygon(arrow(green.botRightSlope, midX+30, midY+125));
				g.fillPolygon(arrow(green.botRightSlope, midX+30, midY+25));
				g.fillPolygon(arrow(green.botRightSlope, midX+130, midY+25));
			}

            // Show clubs on the top left of the frame
			g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			for (int i = 0; i < lg4.player.clubs.length; i++) {
				Club c = lg4.player.clubs[i];
				if (c.name.equals(lg4.club.name)) {
					g.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g.drawRect(20, 110 + i * 30, 5, 5);
				}
				g.drawString(c.name, 35, 120 + i * 30);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			}
            g.fillRect(160, 0, 2, lg4.screenHeight);
            g.fillRect(0, 400, 160, 2);
        }
    };

    public Polygon arrow(int dir, int x, int y) {
		return new Polygon(triangleX(dir, x, y), triangleY(dir, x, y), 3);
	}

	public int[] triangleX(int dir, int x, int y) {
		if (dir == 0)
			return new int[] { 0, 0, 0 };
		if (dir == 1) { // right
			return new int[] { x, x, x + 8 };
		} else if (dir == 2) { // left
			return new int[] { x - 8, x, x };
		} else if (dir == 3) { // up
			return new int[] { x, x + 8, x + 16 };
		} else { // down
			return new int[] { x, x + 8, x + 16 };
		}
	}

	public int[] triangleY(int dir, int x, int y) {
		if (dir == 0)
			return new int[] { 0, 0, 0 };
		if (dir == 1) { // right
			return new int[] { y, y + 16, y + 8 };
		} else if (dir == 2) { // left
			return new int[] { y + 8, y, y + 16 };
		} else if (dir == 3) { // up
			return new int[] { y + 8, y, y + 8 };
		} else { // down
			return new int[] { y, y + 8, y };
		}
	}

}
