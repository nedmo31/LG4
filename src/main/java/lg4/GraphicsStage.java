package lg4;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.Color;
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
        //System.out.println("Checking button click ("+e.getX()+","+e.getY()+") on "+this.name);
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

    public static GraphicsStage loginPage = new GraphicsStage("LoginPage", new Button[]{ 
        new TextButtonWithMessage(new Rectangle(120,150,150,100), "Continue", Color.black, "Type your username first!"){
            public void clickAction() {
                if (lg4.toName.contains("delete") || lg4.toName.length() > 15) {
                    return;
                }
                try {
                    lg4.player = lg4.server.getGolfer(lg4.toName);
                    lg4.gStage = GraphicsStage.mainMenu;
                    System.out.println("Started golfing as "+lg4.toName);
                } catch (Exception e) {
                    System.out.println("That's not a registered golfer");
                    e.printStackTrace();
                }
            }
        }, new TextButtonWithMessage(new Rectangle(120,270,150,100), "New Golfer", Color.black, "Type your username first!"){
            public void clickAction() {
                if (lg4.toName.contains("delete") || lg4.toName.length() > 15 || lg4.toName.length() == 0) {
                    return;
                }
                try {
                    lg4.player = lg4.server.getGolfer(lg4.toName);
                } catch (Exception e) {
                    lg4.player = new Golfer(lg4.toName);
                    System.out.println("Registered new golfer: "+lg4.toName);
                }
                lg4.gStage = GraphicsStage.mainMenu;
            } }, new TextButton(new Rectangle(120, 390,150,100), "Anonymous", Color.black){
            public void clickAction() {
                lg4.player = new Golfer(true);
                lg4.gStage = GraphicsStage.mainMenu;
            } } }, 0) {
            
        void paintGraphicsStage(java.awt.Graphics g) {
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);
            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }
            g.setColor(Color.black);
            super.paintGraphicsStage(g);
            g.setColor(Color.black);
            g.setFont(Window.f1);
            g.drawString("Welcome to Golf!", 100, 100);
            g.setFont(Window.f2);
            g.drawString(name, id, id);
            g.setFont(Window.f3);
            g.drawString("Type username: "+lg4.toName, 300, 600);

            g.drawString("Move mouse to aim", 800, 150);
            g.drawString("Click to start hitting", 800, 175);

            g.drawString("Use arrow keys to change", 800, 270);
            g.drawString("spin and swing speed", 800, 295);

            g.drawString("Press e to open inventory", 800, 390);
            g.drawString("and upgrade your stats", 800, 415);

            g.setColor(Color.RED);
            g.drawOval(745, 125, 40, 40);
            g.fillRect(764, 125, 2, 40);
            g.fillRect(745, 144, 40, 2);

            g.setColor(Color.lightGray);
            g.fillRect(735, 250, 50, 50);
            g.setColor(Color.black);
            g.drawRect(735, 250, 50, 50);
            g.fillPolygon(new int[]{740, 760, 780, 776, 760, 744}, new int[]{282, 262, 282, 282, 266, 282},6);

            g.setColor(Color.lightGray);
            g.fillRect(735, 370, 50, 50);
            g.setColor(Color.black);
            g.drawRect(735, 370, 50, 50);
            g.setFont(Window.f2);
            g.drawString("E", 747, 408);
        }
    };

    public static GraphicsStage mainMenu = new GraphicsStage("Menu", new Button[]{ 
        new TextButton(new Rectangle(50,100,100,100), "Play", Color.black){
            public void clickAction() {
                lg4.gStage = GraphicsStage.play;
                new Thread(() -> {
                    lg4.course.playCourse(Course.DEFAULT_HOLES);
                }).start();
                
            }
        }, new TextButton(new Rectangle(50,250,100,100), "Next Course", Color.black){
            public void clickAction() {
                lg4.courseList.nextCourse();
            }
        } }, 1) {
        void paintGraphicsStage(java.awt.Graphics g) {
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);
            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }
            g.setColor(Color.black);
            g.setFont(Window.f2);
            g.drawString("Scoreboard", lg4.screenWidth-400, 90);
            g.fillRect(lg4.screenWidth-410, 110, 350, 3);
            int i = 1; g.setFont(Window.f4);
            for (Score s : lg4.course.scores) {
                g.drawString(i+": "+s.score+" ~ "+s.name, lg4.screenWidth-380, 120 + 30 * i++);
            }

            super.paintGraphicsStage(g);
            g.setColor(Color.black);
            g.setFont(Window.f1);
            g.drawString(lg4.course.name, 350, 100);
        }
    };

    public static GraphicsStage play = new GraphicsStage("Play", new Button[]{
        new TextButtonWithMessage(new Rectangle(lg4.screenWidth-100,50,30,30), "X", Color.black, "exit course?"){
            public void clickAction() {
                lg4.gStage = GraphicsStage.mainMenu;
                try {
                    lg4.server.saveGolfer(lg4.player);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }, 2) {

        Polygon leftArrowSwing = arrow(2, 80, 620, 12),
                rightArrowSwing = arrow(1, 120, 620, 12),
                upArrowSpin =  arrow(3, 100, 472, 10),
                downArrowSpin = arrow(4, 100, 487, 10),
                windArrowUp = arrow(3, 92, 200, 18),
                windArrowLeft = arrow(2, 92, 200, 18),
                windArrowRight = arrow(1, 92, 200, 18), 
                windArrowDown = arrow(4, 92, 200, 18),
                clubPointerArrow = arrow(1, 40, 350, 12);

        void paintGraphicsStage(java.awt.Graphics g) {
            
            // This will fill the whole background with dark green to represent the rough
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);

            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }

            if (lg4.hitStatus == lg4.AIMING) {
                
                g.setColor(Color.RED);
                g.drawOval(Window.mx-20, Window.my-20, 40, 40);
                g.fillRect(Window.mx-1, Window.my-20, 2, 40);
                g.fillRect(Window.mx-20, Window.my-1, 40, 2);
                
                g.setColor(Color.black);
                int radius = lg4.club.getRadius();
                g.drawOval(lg4.ball.x()-radius/2, lg4.ball.y()-radius/2, radius, radius);
            } else if (lg4.hitStatus == lg4.AIMED) {
                
                g.setColor(Color.RED);
                g.drawOval(Window.aimedX-20, Window.aimedY-20, 40, 40);
                g.fillRect(Window.aimedX-1, Window.aimedY-20, 2, 40);
                g.fillRect(Window.aimedX-20, Window.aimedY-1, 40, 2);
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
            
            g.setColor(Color.RED);
            g.drawOval((int)(900-(lg4.targetPower)*500), 605, 40, 40);
            g.fillRect((int)(920-(lg4.targetPower)*500), 605, 2, 40);
            g.fillRect((int)(900-(lg4.targetPower)*500), 624, 40, 2);
            
            g.setColor(Color.BLACK);
            if (lg4.hitStatus == lg4.SWINGING1) {
                double percentage = (System.nanoTime() - Window.swingFirst)/(double)(Window.targetFirst-Window.swingFirst);
                if (percentage > 1) {
                    g.fillOval((int)(900-(2-percentage)*500), 610, 30, 30);
                } else {
                    g.fillOval((int)(900-percentage*500), 610, 30, 30);
                }
            }
            if (lg4.hitStatus == lg4.SWINGING2) {
                g.fillOval((int)(900-lg4.hitPower*500), 610, 30, 30);
                double percentage = (System.nanoTime() - Window.swingFirst)/(double)(Window.targetFirst-Window.swingFirst);
                if (percentage > 1) {
                    g.fillOval((int)(900-(2-percentage)*500), 610, 30, 30);
                } else {
                    g.fillOval((int)(900-percentage*500), 610, 30, 30);
                }
            }

            else if (lg4.hitStatus == lg4.BALL_MOVING) {
                g.fillOval((int)(900-lg4.hitPower*500), 610, 30, 30);
                g.fillOval((int)(900+lg4.hitSpinLeftRight*100), 610, 30, 30);

                // g.setColor(new Color(100, 100, 240, 220));
                // g.fillOval((int)(400), 610, 15, 15);
                // g.fillOval((int)(1000), 610, 15, 15);
                // g.setColor(new Color(240, 100, 100, 220));
                // g.fillOval((int)(800), 610, 15, 15);
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

				// Draw arrows for green slope :(
				g.setColor(new Color(5, 5, 5, 100));
				g.fillPolygon(arrow(green.topLeftSlope, midX-130, midY-25, 8));
				g.fillPolygon(arrow(green.topLeftSlope, midX-30, midY-125, 8));
				g.fillPolygon(arrow(green.topLeftSlope, midX-30, midY-25, 8));

				g.fillPolygon(arrow(green.topRightSlope, midX+30, midY-25, 8));
				g.fillPolygon(arrow(green.topRightSlope, midX+30, midY-125, 8));
				g.fillPolygon(arrow(green.topRightSlope, midX+130, midY-25, 8));

				g.fillPolygon(arrow(green.botLeftSlope, midX-130, midY+25, 8));
				g.fillPolygon(arrow(green.botLeftSlope, midX-30, midY+125, 8));
				g.fillPolygon(arrow(green.botLeftSlope, midX-30, midY+25, 8));

				g.fillPolygon(arrow(green.botRightSlope, midX+30, midY+125, 8));
				g.fillPolygon(arrow(green.botRightSlope, midX+30, midY+25, 8));
				g.fillPolygon(arrow(green.botRightSlope, midX+130, midY+25, 8));
			}

            // Left panel background
			g.setColor(Color.black);
            g.fillRect(199, 0, 3, lg4.screenHeight);
            g.setColor(new Color(69, 122, 77));
            g.fillRect(0, 0, 200, lg4.screenHeight);

            g.setColor(Color.black);
            g.fillRect(20, 50, 160, 624);

            // Sections on the left panel
            g.setColor(new Color(101, 181, 113));
            g.fillRect(24, 54, 152, 80);
            g.fillRect(24, 138, 152, 120);
            g.fillRect(24, 262, 152, 160);
            g.fillRect(24, 426, 152, 120);
            g.fillRect(24, 550, 152, 120);

            g.setColor(Color.black);
            g.setFont(Window.f3);
            g.drawString("Club:", 40, 295);
            g.drawPolygon(clubPointerArrow);
			for (int i = 0; i < lg4.player.clubs.length; i++) {
				Club c = lg4.player.clubs[i];
				if (c.name.equals(lg4.club.name)) {
					g.setFont(Window.f3b);
                    g.drawString(c.name, 60, 370);

                    g.setFont(Window.f5);
                    if (i > 0) {
                        g.drawString(lg4.player.clubs[i-1].name, 40, 330);
                    }
                    if (i < lg4.player.clubs.length - 1) {
                        g.drawString(lg4.player.clubs[i+1].name, 40, 405);
                    }
                    break;
				}
			}

            // Show swing speed and ball spin
            g.setColor(Color.white);
            g.fillOval(40, 460, 50, 50);
            g.setColor(Color.red);
            g.fillOval(62, 481 - (int)(16*lg4.hitSpinUpDown), 7, 7);

            g.setColor(Color.black);
            g.drawOval(40, 460, 50, 50);
            g.fillPolygon(upArrowSpin);
            g.fillPolygon(downArrowSpin);

            g.setFont(Window.f4);
            g.drawString("Spin:", 30, 450);

            g.drawString("Swing Speed:", 30, 575);
            g.fillPolygon(leftArrowSwing);
            g.fillPolygon(rightArrowSwing);
            if (lg4.swingSpeed <= 3) {
                g.drawString("Fast", 120, 610);
            } else if (lg4.swingSpeed == 4) {
                g.drawString("Medium", 60, 610);
            } else {
                g.drawString("Slow", 40, 610);
            }
            

            // Draw wind arrow 
            // TO DO: improve upon wind. 0 north, 1 east, 2 south, 3 west for windDir
            g.setColor(Color.black);
            switch (lg4.hole.windDir) {
                case 0:
                    g.fillPolygon(windArrowUp);
                    break;
                case 1: 
                    g.fillPolygon(windArrowRight);
                    break;
                case 2:
                    g.fillPolygon(windArrowDown);
                    break;
                case 3: 
                    g.fillPolygon(windArrowLeft);
                    break; 
            }
            g.setFont(Window.f3);
            g.drawString("Wind " +lg4.hole.windSpeed+"m/s", 30, 180);

            g.setFont(Window.f2);
            g.drawString("Hole #"+(lg4.holeNum+1), 32, 102);

            super.paintGraphicsStage(g);
        }
    };

    public static GraphicsStage playerCard = new GraphicsStage("PlayerCard", new Button[]{ 
        new TextButton(new Rectangle(920,320,30,30), "", Color.black){ // 0
            public void clickAction() {
                double x = lg4.ball.x, y = lg4.ball.y;
                lg4.ball = lg4.balls[0];
                lg4.ball.x = x; lg4.ball.y = y;
            }
        }, new TextButton(new Rectangle(920,380,30,30), "", Color.black){ // 1
            public void clickAction() {
                double x = lg4.ball.x, y = lg4.ball.y;
                lg4.ball = lg4.balls[1];
                lg4.ball.x = x; lg4.ball.y = y;
            }
        }, new TextButton(new Rectangle(920,440,30,30), "", Color.black){ // 2
            public void clickAction() {
                double x = lg4.ball.x, y = lg4.ball.y;
                lg4.ball = lg4.balls[2];
                lg4.ball.x = x; lg4.ball.y = y;
            }
        }, new TextButton(new Rectangle(920,500,30,30), "", Color.black){ // 3
            public void clickAction() {
                double x = lg4.ball.x, y = lg4.ball.y;
                lg4.ball = lg4.balls[3];
                lg4.ball.x = x; lg4.ball.y = y;
            }
        }, new TextButton(new Rectangle(920,560,30,30), "", Color.black){ // 4
            public void clickAction() {
                double x = lg4.ball.x, y = lg4.ball.y;
                lg4.ball = lg4.balls[4];
                lg4.ball.x = x; lg4.ball.y = y;
            }
        }, new TextButtonWithMessage(new Rectangle(640,323,50,40), "++", Color.black, "$10"){ // 5
            public void clickAction() {
                if (lg4.player.money >= lg4.player.power*10 && lg4.player.power < 10) {
                    lg4.player.money -= lg4.player.power++*10;
                    lg4.player.fixShopCosts();
                }
            }
        }, new TextButtonWithMessage(new Rectangle(665,448,50,40), "++", Color.black, "$10"){ // 6
            public void clickAction() {
                if (lg4.player.money >= lg4.player.accuracy*10 && lg4.player.accuracy < 10) {
                    lg4.player.money -= lg4.player.accuracy++*10;
                    lg4.player.fixShopCosts();
                }
            }
        }, new TextButtonWithMessage(new Rectangle(645,573,50,40), "++", Color.black, "$10"){ // 7
            public void clickAction() {
                if (lg4.player.money >= lg4.player.putting*10 && lg4.player.putting < 10) {
                    lg4.player.money -= lg4.player.putting++*10;
                    lg4.player.fixShopCosts();
                }
            }
        }, }, 1) {
        void paintGraphicsStage(java.awt.Graphics g) {
            g.setColor(new Color(29, 153, 66));
            g.fillRect(0, 0, lg4.screenWidth, lg4.screenHeight);
            for (HoleSegment hs : lg4.hole.segments) {
                hs.paintArea(g);
            }
            g.setColor(new Color(160, 160, 80));
			g.fillRect(295, 145, 810, 510);
			g.setColor(new Color(204, 204, 102));
			g.fillRect(300, 150, 800, 500);

			g.setColor(Color.black);
			g.setFont(Window.f2);
			g.drawString(lg4.player.name, 350, 210);
			g.drawString("$ " + lg4.player.money, 700, 210);

			g.setColor(Color.black);
			g.setFont(Window.f3);
			g.drawString("Clubs", 350, 285);
            //g.drawString("Stats", 575, 285);
			g.drawString("Golf Balls:", 800, 285);
            g.setFont(Window.f5);
			for (int i = 0; i < lg4.balls.length; i++) {
                g.setColor(Color.black);
                if (lg4.ball.name.equals(lg4.balls[i].name)) {
                    g.fillRect(795, 322 + i *60, 10, 10);
                }
                g.setColor(Color.black);
				g.drawString(lg4.balls[i].name, 810, 335 + i * 60);
                g.drawOval(920, 320+i*60, 30, 30);
                g.setColor(lg4.balls[i].color);
                g.fillOval(920, 320+i*60, 30, 30);
                //g.fillOval(i, i, i, i);
			} g.setColor(Color.black);

			g.setFont(Window.f4);
			g.drawString("Power", 580, 340);
			g.drawString("Accuracy", 580, 465);
			g.drawString("Putting", 580, 590);
            g.fillRect(503, 350, 256, 14);
            g.fillRect(503, 475, 256, 14);
            g.fillRect(503, 600, 256, 14);
            g.setColor(Color.red);
            for (int i = 0; i < lg4.player.power; i++) {
                g.fillRect(505+i*25, 352, 23, 10);
            }
            for (int i = 0; i < lg4.player.accuracy; i++) {
                g.fillRect(505+i*25, 477, 23, 10);
            }
            for (int i = 0; i < lg4.player.putting; i++) {
                g.fillRect(505+i*25, 602, 23, 10);
            }

			// Clubs on player card
			for (int i = 0; i < lg4.player.clubs.length; i++) {
				g.setColor(Color.black);
				g.fillRect(340, 300 + i * 38, 4, 8);
				g.setColor(Color.gray);
				g.fillRect(340, 308 + i * 38, 4, 12);
				g.fillRect(344, 316 + i * 38, 6, 4);
			}
            g.setColor(Color.black);
			g.setFont(Window.f5);
			for (int i = 0; i < lg4.player.clubs.length; i++) {
				g.drawString(lg4.player.clubs[i].name, 360, 315 + i * 38);
			}
            super.paintGraphicsStage(g);
        }
    };

    public Polygon arrow(int dir, int x, int y, int size) {
		return new Polygon(triangleX(dir, x, y, size), triangleY(dir, x, y, size), 3);
	}

	public int[] triangleX(int dir, int x, int y, int size) {
		if (dir == 0)
			return new int[] { 0, 0, 0 };
		if (dir == 1) { // right
			return new int[] { x, x, x + size };
		} else if (dir == 2) { // left
			return new int[] { x - size, x, x };
		} else if (dir == 3) { // up
			return new int[] { x, x + size, x + size*2 };
		} else { // down
			return new int[] { x, x + size, x + size*2 };
		}
	}

	public int[] triangleY(int dir, int x, int y, int size) {
		if (dir == 0)
			return new int[] { 0, 0, 0 };
		if (dir == 1) { // right
			return new int[] { y, y + size*2, y + size};
		} else if (dir == 2) { // left
			return new int[] { y + size, y, y + size*2 };
		} else if (dir == 3) { // up
			return new int[] { y + size, y, y + size };
		} else { // down
			return new int[] { y, y + size, y };
		}
	}

}
