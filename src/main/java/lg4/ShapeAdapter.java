package lg4;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.io.IOException;

public class ShapeAdapter extends TypeAdapter<Shape>{

    @Override
    public void write(JsonWriter out, Shape value) throws IOException {
        if (value instanceof Polygon) {
            Polygon poly = ((Polygon)value);
            out.beginObject();
            out.name("npoints");out.value(poly.npoints);
            out.name("xpoints");
            out.beginArray();
            for (int i = 0; i < poly.npoints; i++) {
                out.value(poly.xpoints[i]);
            }
            out.endArray();
            out.name("ypoints");
            out.beginArray();
            for (int i = 0; i < poly.npoints; i++) {
                out.value(poly.ypoints[i]);
            }
            out.endArray();
            out.endObject();
        } else if (value instanceof Ellipse2D) {
            Ellipse2D r = ((Ellipse2D)value);
            out.beginObject();
            out.name("x"); out.value(r.getX());
            out.name("y"); out.value(r.getY());
            out.name("width"); out.value(r.getWidth());
            out.name("height"); out.value(r.getHeight());
            out.endObject();
        } else if (value instanceof Rectangle) {
            Rectangle r = ((Rectangle)value);
            out.beginObject();
            out.name("y"); out.value(r.y);
            out.name("x"); out.value(r.x);
            out.name("width"); out.value(r.width);
            out.name("height"); out.value(r.height);
            out.endObject();
        }else {
            throw new UnsupportedOperationException("Unknown shape subclass");
        }
    }

    @Override
    public Shape read(JsonReader in) throws IOException {
        // the first token is the start object
		JsonToken token = in.peek();
        Shape shape = null;
		if (token.equals(JsonToken.BEGIN_OBJECT)) {
			in.beginObject();
            if (in.peek().equals(JsonToken.NAME)) {
                String name = in.nextName();
					if (name.equals("npoints")) {
                        int npoints = in.nextInt();
                        int[] xs = new int[npoints];
                        int[] ys = new int[npoints];
                        in.nextName(); // xpoints
                        in.beginArray();
                        int i = 0;
                        while (!in.peek().equals(JsonToken.END_ARRAY)) {
                            xs[i++] = in.nextInt();
                        } in.endArray();
                        in.nextName(); // ypoints
                        in.beginArray();
                        i = 0;
                        while (!in.peek().equals(JsonToken.END_ARRAY)) {
                            ys[i++] = in.nextInt();
                        } in.endArray();
                        shape = new Polygon(xs, ys, npoints);
                    } 
                    else if (name.equals("x")) {
                        double x = in.nextDouble();
                        in.nextName();
                        double y = in.nextDouble();
                        in.nextName();
                        double width = in.nextDouble();
                        in.nextName();
                        double height = in.nextDouble();
                        shape = new Ellipse2D.Double(x, y, width, height);
                    }
                    else if (name.equals("y")) {
                        int y = in.nextInt();
                        in.nextName();
                        int x = in.nextInt();
                        in.nextName();
                        int width = in.nextInt();
                        in.nextName();
                        int height = in.nextInt();
                        shape = new Rectangle(x, y, width, height);
                    } else {
                        throw new IOException("unknown shape");
                    }
				}
			
			in.endObject();

		}
		return shape;

    }
    
}
