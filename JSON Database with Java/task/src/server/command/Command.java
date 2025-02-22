package server.command;

import com.google.gson.JsonElement;

import java.util.Map;

public interface Command {
    Map<String, JsonElement> execute();
}
