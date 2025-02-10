package server.command;

import java.util.Map;

public interface Command {
    Map<String, String> execute();
}
