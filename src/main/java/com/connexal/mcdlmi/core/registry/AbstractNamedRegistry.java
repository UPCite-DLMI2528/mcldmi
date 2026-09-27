package com.connexal.mcdlmi.core.registry;

import com.connexal.mcdlmi.api.registry.Registration;
import com.connexal.mcdlmi.api.registry.Registry;

import java.util.*;

public abstract class AbstractNamedRegistry<K, V> implements Registry<K, V> {
    private final Map<K, V> entries = new LinkedHashMap<>();

    @Override
    public final Registration register(V value) {
        K key = this.createKey(value);

        if (entries.containsKey(key)) {
            throw new IllegalStateException("An entry is already registered with key '" + key + "'");
        }

        this.beforeRegister(key, value);

        entries.put(key, value);

        try {
            this.afterRegister(key, value);
        } catch (Exception exception) {
            entries.remove(key);
            throw exception;
        }

        return new RegistryRegistration<>(this, key);
    }

    @Override
    public final Optional<V> find(K key) {
        return Optional.ofNullable(entries.get(key));
    }

    @Override
    public final Collection<V> values() {
        return Collections.unmodifiableCollection(entries.values());
    }

    @Override
    public final boolean contains(K key) {
        return entries.containsKey(key);
    }

    @Override
    public final void unregister(K key) {
        V value = entries.remove(key);
        if (value == null) {
            return;
        }

        this.afterUnregister(key, value);
    }

    @Override
    public final int size() {
        return entries.size();
    }

    @Override
    public final void clear() {
        // A copy is required to avoid ConcurrentModificationException when unregistering entries
        for (K key : Set.copyOf(entries.keySet())) {
            this.unregister(key);
        }
    }

    protected void beforeRegister(K key, V value) {
    }

    protected void afterRegister(K key, V value) {
    }

    protected void afterUnregister(K key, V value) {
    }

    protected abstract K createKey(V value);

    private record RegistryRegistration<K, V>(AbstractNamedRegistry<K, V> registry, K key) implements Registration {
        @Override
        public boolean isRegistered() {
            return registry.contains(key);
        }

        @Override
        public void unregister() {
            registry.unregister(key);
        }
    }
}
