package server.command;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.List;

public class DeleteCommand implements Command {
    private final DatabaseReceiver receiver;
    private final List<String> keys;

    public DeleteCommand(DatabaseReceiver receiver, List<String> keys) {
        this.receiver = receiver;
        this.keys = keys;
    }

    @Override
    public Map<String, JsonElement> execute() {
        return receiver.delete(keys);
    }
}
