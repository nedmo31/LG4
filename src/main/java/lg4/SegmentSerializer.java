package lg4;

import java.lang.reflect.Type;

import com.google.gson.*;

public class SegmentSerializer implements JsonSerializer<HoleSegment>{

    @Override
    public JsonElement serialize(HoleSegment src, Type typeOfSrc, JsonSerializationContext context) {
        String name = src.type;
        if (name.equals("fairway")) {
            return context.serialize(src, Fairway.class); 
        } 
        else if (name.equals("forest")) {
            return context.serialize(src, Forest.class); 
        }
        else if (name.equals("green")) {
            return context.serialize(src, Green.class); 
        }
        else if (name.equals("rough")) {
            return context.serialize(src, Rough.class); 
        }
        else if (name.equals("sand")) {
            return context.serialize(src, Sand.class); 
        }
        else if (name.equals("teebox")) {
            return context.serialize(src, TeeBox.class); 
        }
        else if (name.equals("water")) {
            return context.serialize(src, Water.class); 
        } 
        else {
            throw new JsonParseException("unknown holesegment");
        }
    }

    
}
