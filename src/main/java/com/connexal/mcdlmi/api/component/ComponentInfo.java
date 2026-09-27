package com.connexal.mcdlmi.api.component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public record ComponentInfo(ComponentId id, Set<ComponentId> dependencies) {
    public static ComponentInfo of(String id, String... dependencies) {
        return new ComponentInfo(new ComponentId(id), Arrays.stream(dependencies).map(ComponentId::new).collect(Collectors.toSet()));
    }

    public static ComponentInfo of(String id) {
        return new ComponentInfo(new ComponentId(id), Set.of());
    }
}
