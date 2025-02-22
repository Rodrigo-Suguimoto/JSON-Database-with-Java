package server.command;

import com.google.gson.JsonElement;

import java.util.Map;

public class DeleteCommand implements Command {
    private final DatabaseReceiver receiver;
    private final String key;

    public DeleteCommand(DatabaseReceiver receiver, String key) {
        this.receiver = receiver;
        this.key = key;
    }

    @Override
    public Map<String, JsonElement> execute() {
        return receiver.delete(key);
    }
}
