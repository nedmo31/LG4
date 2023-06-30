package lg4;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SegmentAdapter extends TypeAdapter<HoleSegment>{

    @Override
    public void write(JsonWriter out, HoleSegment value) throws IOException {
        super.toJson(value);
        // if (value instanceof Forest) {
        //     out.beginObject();
        //     out.name("type");out.value("forest");
        //     out.endObject();
        // } else if (value instanceof Green) {
        //     out.beginObject();

        //     out.endObject();
        // } else if (value instanceof Sand) {
        //     out.beginObject();

        //     out.endObject();
        // }else if (value instanceof TeeBox) {
        //     out.beginObject();

        //     out.endObject();
        // }else if (value instanceof Water) {
        //     out.beginObject();

        //     out.endObject();
        // }else {
        //     throw new UnsupportedOperationException("Unknown shape subclass");
        // }
    }

    @Override
    public HoleSegment read(JsonReader in) throws IOException {
        // the first token is the start object
		JsonToken token = in.peek();
        HoleSegment hs = null;
		if (token.equals(JsonToken.BEGIN_OBJECT)) {
			in.beginObject();
            if (in.peek().equals(JsonToken.NAME)) {
                String name = in.nextName();
					if (name.equals("fairway")) {
                        
                    } 
                    else if (name.equals("forest")) {

                    }
                    else if (name.equals("green")) {
                        
                    }
                    else if (name.equals("rough")) {
                        
                    }
                    else if (name.equals("sand")) {
                        
                    }
                    else if (name.equals("teebox")) {
                        
                    }
                    else if (name.equals("water")) {
                        
                    } 
                    else {
                        throw new IOException("unknown holesegment");
                    }
				}
			
			in.endObject();

		}
		return hs;

    }
}
