package com.connexal.mcdlmi.core.component;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.component.*;
import com.connexal.mcdlmi.api.config.Config;
import com.connexal.mcdlmi.api.config.ConfigCodec;
import com.connexal.mcdlmi.api.config.ConfigurationManager;
import com.connexal.mcdlmi.api.registry.RegistryHub;
import com.connexal.mcdlmi.core.registry.DefaultRegistryHub;
import com.google.common.collect.ImmutableList;
import org.bukkit.Server;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.function.Supplier;

public class DefaultComponentManager implements ComponentManager {
    private final JavaPlugin plugin;
    private final ConfigurationManager configManager;
    private final RegistryHub registryHub;

    private final Map<ComponentId, DefaultComponentDefinition> components;
    private final ImmutableList<DefaultComponentDefinition> loadOrder;

    public DefaultComponentManager(JavaPlugin plugin, ConfigurationManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.registryHub = new DefaultRegistryHub(plugin);

        this.components = this.buildComponentMap();

        ComponentGraph graph = ComponentGraph.of(this.components);
        graph.validate();
        this.loadOrder = ImmutableList.copyOf(graph.sorted());
    }

    @Override
    public void load() {
        for (DefaultComponentDefinition definition : this.loadOrder) {
            // No need to check if components can be enabled, as the graph sorting ignores what shouldn't be loaded.

            MCDLMI.getLog().info("Loading component " + definition.info().id().value());

            ComponentContext context = this.createContext(definition);
            definition.component().enable(context);
            definition.state(ComponentState.ENABLED);
        }

        // List all ignored components
        for (DefaultComponentDefinition definition : this.components.values()) {
            switch (definition.state()) {
                case DISABLED -> MCDLMI.getLog().info("Component " + definition.info().id().value() + " is disabled in the config");
                case FAILED -> MCDLMI.getLog().info("Component " + definition.info().id().value() + " failed");
                case DISCOVERED -> MCDLMI.getLog().severe("Component " + definition.info().id().value() + " didn't get loaded, but should have");
            }
        }
    }

    @Override
    public void unload() {
        for (DefaultComponentDefinition definition : this.loadOrder) {
            // Only disable components that are currently enabled
            if (definition.state() != ComponentState.ENABLED) continue;

            MCDLMI.getLog().info("Unloading component " + definition.info().id().value());

            ComponentContext context = this.createContext(definition);
            definition.component().disable(context);
            definition.state(ComponentState.DISABLED);
        }
    }

    @Override
    public Optional<ComponentDefinition> find(ComponentId id) {
        return Optional.ofNullable(this.components.get(id));
    }

    @Override
    public Collection<ComponentDefinition> components() {
        return Collections.unmodifiableCollection(this.components.values());
    }

    private Map<ComponentId, DefaultComponentDefinition> buildComponentMap() {
        Map<ComponentId, DefaultComponentDefinition> components = new HashMap<>();
        Config config = this.configManager.getDefaultConfig();

        List<DefaultComponentDefinition> definitions = ComponentDiscoverer.discover();
        for (DefaultComponentDefinition definition : definitions) {
            ComponentId id = definition.info().id();

            // Check in the config file to see if this component should be enabled
            String configPath = "components." + id.value();
            if (config.contains(configPath)) {
                definition.state(config.getBoolean(configPath) ? ComponentState.DISCOVERED : ComponentState.DISABLED);
            } else {
                definition.state(ComponentState.DISCOVERED);
                config.set(configPath, true);
            }

            components.put(id, definition);
        }

        config.save();
        return components;
    }

    private ComponentContext createContext(DefaultComponentDefinition definition) {
        return new ComponentContextImpl(definition.info(), this, this.registryHub, this.plugin, this.configManager);
    }

    private record ComponentContextImpl(
            ComponentInfo component, ComponentManager componentManager, RegistryHub registries, JavaPlugin plugin,
            ConfigurationManager configManager
    ) implements ComponentContext {
        @Override
        public Server server() {
            return this.plugin.getServer();
        }

        @Override
        public Supplier<Config> configSupplier(ConfigCodec codec) {
            return () -> this.configManager.getConfig(this.component.id().value(), codec);
        }
    }
}