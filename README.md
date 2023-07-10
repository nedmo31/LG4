## Golf 4!
The latest in my series of making golf games in Java. 

## How to play
You'll need to run the jar file lg4.jar. Unfortunately, there are two flags you need to specify when you run it, but you run the run-lg4.bat or run-lg4.sh
file instead of opening a command prompt and passing the flags yourself.

At the moment, the game is not very self-explanatory. The menu presents 3 options: 
 - continue: If you've created a new golfer last time you played, type your username in so it appears at the top of the screen and press continue
 - new golfer: If you've never played before and want to save progress, type a username in so it appears at the top and then press continue
 - anonymous: If you just want to play, click this

There may only be one course available if you're getting them from the server. In this case the next course button will do nothing. If it's 
auto generating holes because it couldn't find any on the server, you'll have 3 options. Click Play when you find a course you like

When playing, your ball should appear on the teebox on the left side of the screen. Before you hit, there are multiple options to select from
 - Club: by using the scroll wheel, you can select a club from the left. How far each club goes is represented by a black circle 
 - Spin: Using the up down arrows, or space to reset, select a spin. Up arrow = top spin, down arrow = back spin
 - Swing speed: Using the left right arrows, or space to reset, select a swing speed. The faster it is, the further you can hit, but it does get harder.

Now to hit, aim your mouse where you would like to hit the ball. You should see a red target around the mouse. Click once to set your aim
The game waits for you to click again (or press escape) before starting the hit.
When you click again, you will see a black circle start moving to the left at the bottom of the screen. Ideally, you will click when the black circle
crosses over the target. Now the black circle travels back to the right, and you want to click when it goes over the vertical line.
Your power is determined by how far to the left the black circle is when you click. The spin on the ball (slice or hook) is determined by how close
you get to the vertical line.

The ball should fly and the process repeats. Putting is a simple click and drag system, and any slope on the green is shown by a set of three arrows.

## What's new?
 - 3 Dimensions: We're finally golfing in all three dimensions!
 - Leaderboards: There's no mulitplayer, but you can see how everyone else is doing on the courses you're playing, and try to get the best score
 - New hitting: Getting a good hit is a bit harder now. Your timing has to be good to get the right power and not to slice or hook your shot

## Updates
# 6/28/23
The work begins! 
# 7/1/23
Hitting has been implemented, but it's not very straightforward. Only one type of hole gets generated, and there is no hazards yet.
# 7/5/23
The backend server and database were deployed, so game client can now communicate with the server.
# 7/10/23
Added a course that look alright. There are now hazards (sand, water, out of bounds areas).
