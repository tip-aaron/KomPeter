package com.github.ragudos.kompeter.utilities.observer;

import java.util.function.Consumer;

public interface Observer<V> {
    void notifySubscribers(V value);

    void subscribe(Consumer<V> subscriber);

    void unsubscribe(Consumer<V> subscriber);
}
