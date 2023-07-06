package lg4;

import java.util.ArrayList;

public class CourseList {
    ArrayList<Course> courses;
    public int coursenum = 0;
    public CourseList(ArrayList<Course> c) {
        courses = c;
    }
    public CourseList() {
        courses = new ArrayList<>(3);
        courses.add(new Course());
        courses.add(new Course());
        courses.add(new Course());
    }
    public void nextCourse() {
        if (coursenum++ >= courses.size()-1) {
            coursenum = 0;
        } 
        lg4.course = courses.get(coursenum);
        lg4.hole = lg4.course.holes[0];
    }
}
