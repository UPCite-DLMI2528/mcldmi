package com.connexal.mcdlmi.core.registry;

import com.connexal.mcdlmi.api.registry.Registration;
import com.connexal.mcdlmi.api.registry.Registry;

import java.util.*;

public abstract class AbstractUnnamedRegistry<V> implements Registry<V, V> {
    private final Set<V> entries = new LinkedHashSet<>();

    @Override
    public final Registration register(V value) {
        if (entries.contains(value)) {
            throw new IllegalStateException("An entry is already registered with this object");
        }

        this.beforeRegister(value);

        entries.add(value);

        try {
            this.afterRegister(value);
        } catch (Exception exception) {
            entries.remove(value);
            throw exception;
        }

        return new RegistryRegistration<>(this, value);
    }

    @Override
    public final Optional<V> find(V value) {
        if (entries.contains(value)) {
            return Optional.of(value);
        }

        return Optional.empty();
    }

    @Override
    public final Collection<V> values() {
        return Collections.unmodifiableCollection(entries);
    }

    @Override
    public final boolean contains(V value) {
        return entries.contains(value);
    }

    @Override
    public final void unregister(V value) {
        if (entries.remove(value)) {
            this.afterUnregister(value);
        }
    }

    @Override
    public final int size() {
        return entries.size();
    }

    @Override
    public final void clear() {
        // A copy is required to avoid ConcurrentModificationException when unregistering entries
        for (V entry : Set.copyOf(entries)) {
            this.unregister(entry);
        }
    }

    protected void beforeRegister(V value) {
    }

    protected void afterRegister(V value) {
    }

    protected void afterUnregister(V value) {
    }

    private record RegistryRegistration<V>(AbstractUnnamedRegistry<V> registry, V value) implements Registration {
        @Override
        public boolean isRegistered() {
            return registry.contains(value);
        }

        @Override
        public void unregister() {
            registry.unregister(value);
        }
    }
}
