package com.connexal.mcdlmi.api.component;

import java.util.Collection;
import java.util.Optional;

public interface ComponentManager {
    void load();

    void unload();

    Optional<ComponentDefinition> find(ComponentId id);

    Collection<ComponentDefinition> components();
}
