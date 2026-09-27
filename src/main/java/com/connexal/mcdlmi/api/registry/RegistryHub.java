package com.connexal.mcdlmi.api.registry;

import com.connexal.mcdlmi.api.block.BlockRegistry;
import com.connexal.mcdlmi.api.command.CommandRegistry;
import com.connexal.mcdlmi.api.event.EventRegistry;
import com.connexal.mcdlmi.api.item.ItemRegistry;
import com.connexal.mcdlmi.api.recipe.RecipeRegistry;

public interface RegistryHub {
    CommandRegistry commands();

    EventRegistry events();

    ItemRegistry items();

    BlockRegistry blocks();

    RecipeRegistry recipes();
}
