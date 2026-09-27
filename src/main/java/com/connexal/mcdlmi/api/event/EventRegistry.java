package com.connexal.mcdlmi.api.event;

import com.connexal.mcdlmi.api.registry.Registration;
import org.bukkit.event.Listener;

public interface EventRegistry {
    Registration register(Listener listener);
}
