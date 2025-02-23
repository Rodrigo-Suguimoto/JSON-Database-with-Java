package server.command;

import com.google.gson.JsonElement;

import java.util.Map;
import java.util.List;

public class SetCommand implements Command {
    private final DatabaseReceiver receiver;
    private final List<String> keys;
    private final JsonElement value;

    public SetCommand(DatabaseReceiver receiver, List<String> keys, JsonElement value) {
        this.receiver = receiver;
        this.keys = keys;
        this.value = value;
    }

    @Override
    public Map<String, JsonElement> execute() {
        return receiver.set(keys, value);
    }
}
