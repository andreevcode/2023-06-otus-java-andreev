package ru.otus.model;

import java.util.ArrayList;

public class MessageSnapshot {
    private final Message state;

    public MessageSnapshot(Message original) {
        this.state = createSnapshot(original);
    }

    public Message getState() {
        return state;
    }

    private Message createSnapshot(Message org) {
        return org.toBuilder().field13(deepCopy(org.getField13())).build();
    }

    private ObjectForMessage deepCopy(ObjectForMessage msg) {
        if (msg == null) {
            return null;
        }
        var copy = new ObjectForMessage();
        copy.setData(new ArrayList<>(msg.getData()));
        return copy;
    }
}
