package com.connexal.mcdlmi.core.component;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.component.DLMIComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class ComponentDiscoverer {
    public static List<DefaultComponentDefinition> discover() {
        List<DefaultComponentDefinition> definitions = new ArrayList<>();

        ServiceLoader<DLMIComponent> loader = ServiceLoader.load(DLMIComponent.class, MCDLMI.class.getClassLoader());
        for (DLMIComponent component : loader) {
            definitions.add(new DefaultComponentDefinition(component));
        }

        MCDLMI.getLog().info("Discovered " + definitions.size() + " components");

        return definitions;
    }
}
