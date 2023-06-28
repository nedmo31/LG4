package lg4;

class Course {

    Hole[] holes;

    public Course() {
        holes = new Hole[18];
        for (int i = 0; i < 18; i++) {
            holes[i] = new Hole();
        }
    }

    public int playCourse(int holesToPlay) {
        lg4.course = this;
        int strokes = 0;
        for (int i = 0; i < 18; i++) {
            lg4.hole = holes[i];
            strokes += holes[i].playHole();
        }
        return strokes;
    }

}