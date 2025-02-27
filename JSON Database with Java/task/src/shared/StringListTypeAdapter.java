package shared;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StringListTypeAdapter implements JsonSerializer<List<String>>, JsonDeserializer<List<String>> {
    @Override
    public JsonElement serialize(List<String> src, Type typeOfSrc, JsonSerializationContext context) {
        if (src.size() == 1) {
            return new JsonPrimitive(src.get(0));
        } else {
            JsonArray array = new JsonArray();
            for (String s : src) {
                array.add(new JsonPrimitive(s));
            }
            return array;
        }
    }

    @Override
    public List<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<String> list = new ArrayList<>();
        if (json.isJsonArray()) {
            for (JsonElement element : json.getAsJsonArray()) {
                list.add(element.getAsString());
            }
        } else if (json.isJsonPrimitive()) {
            list.add(json.getAsString());
        }
        return list;
    }
}
