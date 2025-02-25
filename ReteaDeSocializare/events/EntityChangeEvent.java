package src.lab7.events;

import src.lab7.domain.Utilizator;

public class EntityChangeEvent extends jdk.jfr.Event implements Event {
    private ChangeEventType type;
    private Utilizator data, oldData;

    public EntityChangeEvent(ChangeEventType type, Utilizator data) {
        this.type = type;
        this.data = data;
    }
    public EntityChangeEvent(ChangeEventType type, Utilizator data, Utilizator oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Utilizator getData() {
        return data;
    }

    public Utilizator getOldData() {
        return oldData;
    }
}