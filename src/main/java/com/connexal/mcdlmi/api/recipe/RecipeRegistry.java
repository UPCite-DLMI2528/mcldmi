package com.connexal.mcdlmi.api.recipe;

import com.connexal.mcdlmi.api.registry.Registration;

public interface RecipeRegistry {
    Registration register(Object value);
}
