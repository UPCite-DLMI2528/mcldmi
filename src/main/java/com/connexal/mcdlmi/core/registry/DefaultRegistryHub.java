package com.connexal.mcdlmi.core.registry;

import com.connexal.mcdlmi.api.block.BlockRegistry;
import com.connexal.mcdlmi.api.command.CommandRegistry;
import com.connexal.mcdlmi.api.event.EventRegistry;
import com.connexal.mcdlmi.api.item.ItemRegistry;
import com.connexal.mcdlmi.api.recipe.RecipeRegistry;
import com.connexal.mcdlmi.api.registry.RegistryHub;
import com.connexal.mcdlmi.core.command.DefaultCommandRegistry;
import com.connexal.mcdlmi.core.event.DefaultEventRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class DefaultRegistryHub implements RegistryHub {
    private final CommandRegistry commandRegistry;
    private final EventRegistry eventRegistry;

    public DefaultRegistryHub(JavaPlugin plugin) {
        this.commandRegistry = new DefaultCommandRegistry(plugin);
        this.eventRegistry = new DefaultEventRegistry(plugin);
    }

    @Override
    public CommandRegistry commands() {
        return this.commandRegistry;
    }

    @Override
    public EventRegistry events() {
        return this.eventRegistry;
    }

    @Override
    public ItemRegistry items() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public BlockRegistry blocks() {
        throw new RuntimeException("Not implemented yet");
    }

    @Override
    public RecipeRegistry recipes() {
        throw new RuntimeException("Not implemented yet");
    }
}
