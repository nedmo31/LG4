package lg4;

import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.*;
import java.awt.Color;
import java.awt.Font;

/**
 * This class will handle the graphics for the game. 
 * Probably shouldn't have named it Window ¯\_(ツ)_/¯
 * 
 * Also, it has the MouseListeners and KeyboardListeners
 */
public class Window extends JPanel {

    public static Font f1 = new Font("Serif", Font.BOLD, 50);
    public static Font f2 = new Font("Serif", Font.PLAIN, 40);
    public static Font f3 = new Font("Serif", Font.PLAIN, 30);
    public static Font f3b = new Font("Serif", Font.BOLD, 30);
    public static Font f4 = new Font("Serif", Font.PLAIN, 25);
    public static Font f4b = new Font("Serif", Font.BOLD, 25);
    public static Font f5 = new Font("Serif", Font.PLAIN, 20);

    /**
     * For click and drag feature, the place where the initial click was
     */
    public static Point firstClick = new Point(0,0);
    /**
     * For click and drag feature, the place where the release was
     */
    public static Point secondClick = new Point(0,0);

    /**
     * The time in ns that a swing starts. See hitting.txt
     */
    public static long swingFirst = 0;

    /**
     * The time in ns for the second click in a swing
     */
    public static long swingSecond = 0;

    /**
     * The time in ns for the third click in a swing
     */
    public static long swingThird = 0;

    /**
     * The time to hit the full power
     */
    public static long targetFirst = 0;

    /**
     * The time to hit the ball perfectly straight
     */
    public static long targetSecond = 0;

    public static int mx, my, aimedX, aimedY;

    public Window() {
        addMouseWheelListener(
            new MouseWheelListener() {
                public void mouseWheelMoved(MouseWheelEvent e) {
                    if (e.getWheelRotation() < 0) {
                        lg4.player.clubDown();
                    } else {   
                        lg4.player.clubUp();
                    }
                }
            }
        );
        addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3 && lg4.editor) {
                    lg4.points.add(new Point(e.getX(), e.getY()));
                    // System.out.println("saving course...");
                    // try {
                    //     lg4.server.updateCourse(lg4.course);
                    // } catch (Exception e1) {
                    //     e1.printStackTrace();
                    // }
                    // return;
                }
                mx = e.getX();
                my = e.getY();
                if (lg4.gStage == GraphicsStage.play) {
                    if (lg4.hitStatus == lg4.AIMING) {
                        // get distance between ball and click
                        int xDist = mx - lg4.ball.x();
                        int yDist = my - lg4.ball.y();
                        double totalDist = Math.sqrt(xDist*xDist + yDist*yDist);
                        lg4.targetPower = Math.min(1, 2*totalDist/lg4.club.radius);
                        // get angle
                        lg4.xyAngle = -1*Math.atan2(yDist, xDist);
                        if (yDist > 0) {
                            lg4.xyAngle -= Math.PI;
                        }
                        aimedX = mx;
                        aimedY = my;

                        lg4.hitStatus = lg4.AIMED;

                    } else if (lg4.hitStatus == lg4.AIMED) {
                        lg4.hitStatus = lg4.SWINGING1;
                        swingFirst = System.nanoTime();
                        targetFirst = swingFirst + 200000000 * lg4.swingSpeed;
                        targetSecond = swingFirst + 400000000 * lg4.swingSpeed;
                    } else if (lg4.hitStatus == lg4.SWINGING1) {
                        swingSecond = System.nanoTime();
                        long diffAbs = Math.abs(swingSecond - targetFirst);
                        lg4.hitPower = Math.max(1 - (diffAbs / (double)(200000000 * lg4.swingSpeed)), .05); // hit's will always be at least .05 power
                        lg4.hitStatus = lg4.SWINGING2;
                    } else if (lg4.hitStatus == lg4.SWINGING2) {
                        swingThird = System.nanoTime();
                        long diff = swingThird - targetSecond;
                        lg4.hitSpinLeftRight = (diff / (double)(50000000 * lg4.swingSpeed));
                        if (lg4.hitSpinLeftRight > 1)
                            lg4.hitSpinLeftRight = 1; 
                        if (lg4.hitSpinLeftRight < -1) 
                            lg4.hitSpinLeftRight = -1; 
                        lg4.hitStatus = lg4.BALL_MOVING;
                    } else if (lg4.hitStatus == lg4.PUTTING) {
                        firstClick.x = mx;
                        firstClick.y = my;
                    }
                }
			}
            public void mouseReleased(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  
                lg4.gStage.checkButtonClick(e);
                if (lg4.hitStatus == lg4.PUTTING) {
                    secondClick.x = mx;
                    secondClick.y = my;
                    lg4.hitPower = .5 * Math.sqrt(Math.pow(firstClick.getX() - secondClick.getX(), 2)
							+ Math.pow(secondClick.getY() - firstClick.getY(), 2));
                    lg4.xyAngle = -Math.atan2(firstClick.getY() - secondClick.getY(),
                        firstClick.getX() - secondClick.getX());
                    lg4.hitStatus = lg4.PUTT_ROLL;
                }
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
                mx = e.getX();
                my = e.getY();
                // System.out.println("(x,y) : ("+mx+","+my+")");
                if (lg4.hitStatus == lg4.AIMING) {
                    // get distance between ball and click
                    int xDist = mx - lg4.ball.x();
                    int yDist = my - lg4.ball.y();
                    double totalDist = Math.sqrt(xDist*xDist + yDist*yDist);
                    lg4.targetPower = Math.min(1, 2*totalDist/lg4.club.radius);
                }
			}
            public void mouseDragged(MouseEvent e) {
                mx = e.getX();
                my = e.getY();  
                if (lg4.hitStatus == lg4.PUTTING) {
                    secondClick.x = mx;
                    secondClick.y = my;
                    lg4.hitPower = .5 * Math.sqrt(Math.pow(firstClick.getX() - secondClick.getX(), 2)
							+ Math.pow(secondClick.getY() - firstClick.getY(), 2));
                    lg4.xyAngle = -Math.atan2(firstClick.getY() - secondClick.getY(),
                        firstClick.getX() - secondClick.getX());
                }
			}
		});
		lg4.win.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
                if (lg4.editor) {
                if (keyCode == KeyEvent.VK_ENTER) {
                        int nps = lg4.points.size();
                        int[] xps = new int[nps];
                        int[] yps = new int[nps];
                        for (int i = 0; i < nps; i++) {
                            Point p = lg4.points.get(i);
                            xps[i] = p.x; yps[i] = p.y;
                        }
                        if (lg4.makerStatus == lg4.SAND) {
                            lg4.hole.addSegment(new Sand(new Polygon(xps, yps, nps)));
                        } else if (lg4.makerStatus == lg4.WATER) {
                            lg4.hole.addSegment(new Water(new Polygon(xps, yps, nps)));
                        } else if (lg4.makerStatus == lg4.FOREST) {
                            lg4.hole.addSegment(new Forest(new Polygon(xps, yps, nps)));
                        } else if (lg4.makerStatus == lg4.FAIRWAY) {
                            lg4.hole.addSegment(new Fairway(new Polygon(xps, yps, nps)));
                        }
                        lg4.points.clear();
                    }
                    else if (keyCode == KeyEvent.VK_PERIOD) {
                        try {
                            lg4.server.saveCourse(lg4.course);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                    else if (keyCode == KeyEvent.VK_SPACE) {
                        try {
                            lg4.server.updateCourse(lg4.course);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    } else if (keyCode == KeyEvent.VK_0) { lg4.makerStatus = 0; } 
                     else if (keyCode == KeyEvent.VK_1) { lg4.makerStatus = 1; } 
                      else if (keyCode == KeyEvent.VK_2) { lg4.makerStatus = 2; } 
                      else if (keyCode == KeyEvent.VK_3) { lg4.makerStatus = 3; } 
                      else if (keyCode == KeyEvent.VK_RIGHT) { // right arrow
						lg4.holeNum++;
                        lg4.hole = lg4.course.holes[lg4.holeNum];
					} else if (keyCode == KeyEvent.VK_LEFT) { // left arrow
						lg4.holeNum--;
                        lg4.hole = lg4.course.holes[lg4.holeNum];
					}
                    return;
                }
				//System.out.println("KEY PRESSED - "+keyCode);
				if (lg4.gStage == GraphicsStage.play) {
					if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) { // up arrow
                        lg4.hitSpinUpDown = 1;
					} else if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) { // down arrow
						lg4.hitSpinUpDown = -1;
					} else if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) { // right arrow
						lg4.swingSpeed = 3;
					} else if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) { // left arrow
						lg4.swingSpeed = 5;
					} else if (keyCode == KeyEvent.VK_SPACE) {
                        lg4.swingSpeed = 4;
                        lg4.hitSpinUpDown = 0;
                    } 
                    else if (keyCode == KeyEvent.VK_ESCAPE && lg4.hitStatus == lg4.AIMED) {
                        lg4.hitStatus = lg4.AIMING;
                    }
                    else if (keyCode == KeyEvent.VK_E) { // show inventory
                        lg4.gStage = GraphicsStage.playerCard;
                    }
                    
				} else if (lg4.gStage == GraphicsStage.playerCard) {
                    if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_E) {
                        lg4.gStage = GraphicsStage.play;
                    }
				}
				else if (lg4.gStage == GraphicsStage.loginPage) {
					if (keyCode == 8) { // BACKSPACE
						if (lg4.toName.length() > 0)
							lg4.toName = lg4.toName.substring(0, lg4.toName.length() - 1);
					} else if (keyCode == 65) {
						lg4.toName += "a";
					} else if (keyCode == 66) {
						lg4.toName += "b";
					} else if (keyCode == 67) {
						lg4.toName += "c";
					} else if (keyCode == 68) {
						lg4.toName += "d";
					} else if (keyCode == 69) {
						lg4.toName += "e";
					} else if (keyCode == 70) {
						lg4.toName += "f";
					} else if (keyCode == 71) {
						lg4.toName += "g";
					} else if (keyCode == 72) {
						lg4.toName += "h";
					} else if (keyCode == 73) {
						lg4.toName += "i";
					} else if (keyCode == 74) {
						lg4.toName += "j";
					} else if (keyCode == 75) {
						lg4.toName += "k";
					} else if (keyCode == 76) {
						lg4.toName += "l";
					} else if (keyCode == 77) {
						lg4.toName += "m";
					} else if (keyCode == 78) {
						lg4.toName += "n";
					} else if (keyCode == 79) {
						lg4.toName += "o";
					} else if (keyCode == 80) {
						lg4.toName += "p";
					} else if (keyCode == 81) {
						lg4.toName += "q";
					} else if (keyCode == 82) {
						lg4.toName += "r";
					} else if (keyCode == 83) {
						lg4.toName += "s";
					} else if (keyCode == 84) {
						lg4.toName += "t";
					} else if (keyCode == 85) {
						lg4.toName += "u";
					} else if (keyCode == 86) {
						lg4.toName += "v";
					} else if (keyCode == 87) {
						lg4.toName += "w";
					} else if (keyCode == 88) {
						lg4.toName += "x";
					} else if (keyCode == 89) {
						lg4.toName += "y";
					} else if (keyCode == 90) {
						lg4.toName += "z";
					}
				}
            }
		});
    }

    /**
     * The method that paints the screen
     */
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);

        lg4.gStage.paintGraphicsStage(g);

        if (lg4.editor) {
            g.setColor(Color.black);
            for (Point p : lg4.points) {
                g.fillOval(p.x, p.y, 3, 3);
            }
            g.drawString("Hole #"+lg4.holeNum, 100, 100);
        }
    }

    // private void handleHitReleased(MouseEvent e) {
        
    // }

    // private void handleHitPressed(MouseEvent e) {

    // }

    // private void handlHitDragged(MouseEvent e) {
        
    // }

}
