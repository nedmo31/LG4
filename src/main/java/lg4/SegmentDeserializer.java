package lg4;

import java.lang.reflect.Type;

import com.google.gson.*;

public class SegmentDeserializer implements JsonDeserializer<HoleSegment>{

    @Override
    public HoleSegment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String name = json.getAsJsonObject().get("type").toString();
        if (name.equals("\"fairway\"")) {
            return context.deserialize(json, Fairway.class); 
        } 
        else if (name.equals("\"forest\"")) {
            return context.deserialize(json, Forest.class); 
        }
        else if (name.equals("\"green\"")) {
            return context.deserialize(json, Green.class); 
        }
        else if (name.equals("\"rough\"")) {
            return context.deserialize(json, Rough.class); 
        }
        else if (name.equals("\"sand\"")) {
            return context.deserialize(json, Sand.class); 
        }
        else if (name.equals("\"teebox\"")) {
            return context.deserialize(json, TeeBox.class); 
        }
        else if (name.equals("\"water\"")) {
            return context.deserialize(json, Water.class); 
        } 
        else {
            throw new JsonParseException("unknown holesegment");
        }
    }
    
}
