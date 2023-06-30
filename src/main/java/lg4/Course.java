package lg4;

class Course {

    Hole[] holes;

    public static final int DEFAULT_HOLES = 9;

    public Course() {
        holes = new Hole[DEFAULT_HOLES];
        for (int i = 0; i < DEFAULT_HOLES; i++) {
            holes[i] = new Hole();
        }
    }

    public Course(Hole[] h) {
        holes = h;
    }

    public int playCourse(int holesToPlay) {
        holesToPlay = holesToPlay > 9 ? 9 : holesToPlay;
        lg4.course = this;
        int strokes = 0;
        for (int i = 0; i < holesToPlay; i++) {
            lg4.hole = holes[i];
            strokes += holes[i].playHole();
        }
        return strokes;
    }

}