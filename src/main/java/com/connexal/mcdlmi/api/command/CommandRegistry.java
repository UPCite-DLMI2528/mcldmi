package com.connexal.mcdlmi.api.command;

import com.connexal.mcdlmi.api.registry.Registration;

public interface CommandRegistry {
    Registration register(Command command);
}
