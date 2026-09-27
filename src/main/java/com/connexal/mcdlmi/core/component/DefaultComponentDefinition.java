package com.connexal.mcdlmi.core.component;

import com.connexal.mcdlmi.api.component.ComponentDefinition;
import com.connexal.mcdlmi.api.component.ComponentState;
import com.connexal.mcdlmi.api.component.DLMIComponent;

public class DefaultComponentDefinition implements ComponentDefinition {
    private final DLMIComponent component;
    private ComponentState state;

    public DefaultComponentDefinition(DLMIComponent component) {
        this.component = component;
        this.state = ComponentState.DISCOVERED;
    }

    @Override
    public DLMIComponent component() {
        return this.component;
    }

    @Override
    public ComponentState state() {
        return this.state;
    }

    void state(ComponentState state) {
        this.state = state;
    }
}
