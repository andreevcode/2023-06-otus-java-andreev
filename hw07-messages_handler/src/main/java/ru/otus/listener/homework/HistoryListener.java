package ru.otus.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.MessageSnapshot;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, MessageSnapshot> historyMessageSnapshots = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        historyMessageSnapshots.put(msg.getId(), new MessageSnapshot(msg));
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        var snapshot = Optional.ofNullable(historyMessageSnapshots.get(id));
        return snapshot.map(MessageSnapshot::getState);
    }
}
