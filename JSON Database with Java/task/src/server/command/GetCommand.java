package server.command;

import java.util.Map;

public class GetCommand implements Command {
    private final DatabaseReceiver receiver;
    private final String key;

    public GetCommand(DatabaseReceiver receiver, String key) {
        this.receiver = receiver;
        this.key = key;
    }

    @Override
    public Map<String, String> execute() {
        return receiver.get(key);
    }
}
