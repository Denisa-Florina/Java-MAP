package src.lab7.events;

import java.nio.channels.Channel;

public class NotifyEvent implements Event {
    private ChangeEventType type;
    public NotifyEvent(ChangeEventType type) {
        this.type = type;
    }
    public ChangeEventType getType() {
        return type;
    }
}
