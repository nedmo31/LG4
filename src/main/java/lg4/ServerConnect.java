package lg4;

import java.awt.Shape;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServerConnect {

    Gson gson;

    final String SERVER_URL = "https://lg4-c9a630827645.herokuapp.com";
    
    public ServerConnect() {
        GsonBuilder gb = new GsonBuilder();
        gb.registerTypeAdapter(Shape.class, new ShapeAdapter());
        gb.registerTypeAdapter(HoleSegment.class, new SegmentDeserializer());
        gb.registerTypeAdapter(HoleSegment.class, new SegmentSerializer());
        gson = gb.create();
    }

    public void uploadScore(Score s) throws Exception {
        System.out.println("uploading score");
        URL url = new URL(SERVER_URL+"/addScore");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = gson.toJson(s);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        System.out.println("Uploaded score");
    }

    // https://www.baeldung.com/java-http-request
    public Golfer getGolfer(String name) throws Exception {
        URL url = new URL(SERVER_URL+"/getGolfer/"+name);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        Scanner in = new Scanner(new InputStreamReader(con.getInputStream()));
        String next = in.next();
        Golfer g = gson.fromJson(next, Golfer.class);
        lg4.player = g;
        g.initClubs();
        g.fixShopCosts();
        con.disconnect();
        in.close();
        return g;
    }

    // https://www.baeldung.com/java-http-request
    public CourseList getCourses() throws Exception {
        URL url = new URL(SERVER_URL+"/getCourses");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        String back = getFullResponse(con);
        CourseFormatList cl = gson.fromJson(back, CourseFormatList.class);
        con.disconnect();
        return cl.getCourseList(gson);
    }

    // https://www.baeldung.com/httpurlconnection-post
    public void saveCourse(Course c) throws Exception {
        c.id = 39;
        c.name = "editme";
        System.out.println("saving course: "+c.name);
        URL url = new URL(SERVER_URL+"/addCourse");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = gson.toJson(new CourseFormat(c.name, c.id, gson.toJson(c)));

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        System.out.println("Added course");

    }

    // https://www.baeldung.com/httpurlconnection-post
    public void updateCourse(Course c) throws Exception {
        System.out.println("updating course: "+c.name);
        URL url = new URL(SERVER_URL+"/updateCourse");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = gson.toJson(new CourseFormat(c.name, c.id, gson.toJson(c)));

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        System.out.println("Updated course");

    }

    // https://www.baeldung.com/httpurlconnection-post
    public void saveGolfer(Golfer g) throws Exception {
        System.out.println("saving golfer: "+g.name);
        URL url = new URL(SERVER_URL+"/addGolfer");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = gson.toJson(g);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);			
        }

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        try(BufferedReader br = new BufferedReader(
        new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }

    }

    /**
     * Call after calling getCourses() to get the leaderboard for current
     * courses.
     * @return
     * @throws Exception
     */
    public ScoreList getScores() throws Exception {
        URL url = new URL(SERVER_URL+"/getScores");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");

        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);

        Scanner in = new Scanner(new InputStreamReader(con.getInputStream()));
        ScoreList cl = gson.fromJson(in.next(), ScoreList.class);
        con.disconnect();
        in.close();
        for (Course c : lg4.courseList.courses) {
            c.scores.clear();
        }
        for (Score s : cl.scores) {
            for (Course c : lg4.courseList.courses) {
                if (s.cid == c.id) {
                    c.scores.add(s);
                }
            }
        }
        for (Course c : lg4.courseList.courses) {
            c.scores.sort(new Comparator<Score>() {
                public int compare(Score o1, Score o2) {
                    return o1.score - o2.score;
                }
            });
            
        }
        return cl;
    }
    
    public class CourseFormatList {
        ArrayList<CourseFormat> courses;
        public CourseFormatList(ArrayList<CourseFormat> c) {
            courses = c;
        }

        public CourseList getCourseList(Gson gson) {
            ArrayList<Course> cs = new ArrayList<>();
            for (CourseFormat cf : courses) {
                cs.add(gson.fromJson(cf.json, Course.class));
            }
            return new CourseList(cs);
        }
    }

    // TODO make other requests use this method
    public String getFullResponse(HttpURLConnection con) throws Exception {
        Scanner in = new Scanner(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        while (in.hasNext()) {
            sb.append(in.next());
        }
        in.close();
        return sb.toString();
    }
}
