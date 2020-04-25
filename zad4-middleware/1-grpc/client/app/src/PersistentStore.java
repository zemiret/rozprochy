package src;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PersistentStore<T> {
    private final List<T> entities = new LinkedList<>();

    public synchronized void add(T entity) {
        this.entities.add(entity);
    }

    public synchronized List<T> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public synchronized boolean isEmpty() {
        return entities.isEmpty();
    }
}
