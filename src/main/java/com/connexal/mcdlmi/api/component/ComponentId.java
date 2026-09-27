package com.connexal.mcdlmi.api.component;

import java.util.Objects;

public record ComponentId(String value) implements Comparable<ComponentId> {
    public ComponentId {
        Objects.requireNonNull(value, "value");

        if (value.isBlank()) {
            throw new IllegalArgumentException("Component ID cannot be blank");
        }

        if (!value.matches("[a-z][a-z0-9-]*")) {
            throw new IllegalArgumentException("Invalid component ID: " + value);
        }
    }

    public static ComponentId of(String value) {
        return new ComponentId(value);
    }

    @Override
    public int compareTo(ComponentId other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return value;
    }
}
