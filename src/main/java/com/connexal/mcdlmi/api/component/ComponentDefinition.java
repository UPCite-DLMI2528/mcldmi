package com.connexal.mcdlmi.api.component;

public interface ComponentDefinition {
    DLMIComponent component();

    default ComponentInfo info() {
        return component().info();
    }

    ComponentState state();

    default boolean disabled() {
        return state() == ComponentState.DISABLED || state() == ComponentState.FAILED;
    }
}
