package server.command;

import java.util.Map;

public class SetCommand implements Command {
    private final DatabaseReceiver receiver;
    private final String key;
    private final String value;

    public SetCommand(DatabaseReceiver receiver, String key, String value) {
        this.receiver = receiver;
        this.key = key;
        this.value = value;
    }

    @Override
    public Map<String, String> execute() {
        return receiver.set(key, value);
    }
}
