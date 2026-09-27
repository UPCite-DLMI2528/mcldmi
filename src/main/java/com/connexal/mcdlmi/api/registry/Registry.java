package com.connexal.mcdlmi.api.registry;

import java.util.Collection;
import java.util.Optional;

public interface Registry<K, V> {
    Registration register(V value);

    Optional<V> find(K key);

    Collection<V> values();

    boolean contains(K key);

    void unregister(K key);

    int size();

    void clear();
}
