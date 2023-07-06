package lg4;

class Course {

    Hole[] holes;
    String name;
    int id;

    public static final int DEFAULT_HOLES = 6;

    public Course() {
        holes = new Hole[DEFAULT_HOLES];
        for (int i = 0; i < DEFAULT_HOLES; i++) {
            holes[i] = new Hole();
        }
        name = "auto-generated";
        id = -1;
    }

    public Course(Hole[] h, String n, int i) {
        holes = h; name = n; id = i;
    }

    public int playCourse(int holesToPlay) {
        holesToPlay = holesToPlay > DEFAULT_HOLES ? DEFAULT_HOLES : holesToPlay;
        lg4.course = this;
        int strokes = 0;
        for (int i = 0; i < holesToPlay; i++) {
            lg4.hole = holes[i];
            strokes += holes[i].playHole();
        }
        if (id == -1) { return strokes; }
        Score results = new Score(lg4.player.name, id, strokes);
        try {
            lg4.server.uploadScore(results);
        } catch(Exception e) {
            System.out.println("Couldn't upload results :(");
        }
        return strokes;
    }

}