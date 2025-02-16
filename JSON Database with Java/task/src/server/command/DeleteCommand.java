package server.command;

import java.util.Map;

public class DeleteCommand implements Command {
    private final DatabaseReceiver receiver;
    private final String key;

    public DeleteCommand(DatabaseReceiver receiver, String key) {
        this.receiver = receiver;
        this.key = key;
    }

    @Override
    public Map<String, String> execute() {
        return receiver.delete(key);
    }
}
