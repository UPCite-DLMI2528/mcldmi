package com.connexal.mcdlmi.api.component;

public interface DLMIComponent {
    ComponentInfo info();

    void enable(ComponentContext context);

    default void disable(ComponentContext context) {
    }
}
