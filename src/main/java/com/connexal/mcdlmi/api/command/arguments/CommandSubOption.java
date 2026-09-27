package com.connexal.mcdlmi.api.command.arguments;

public final class CommandSubOption extends CommandOption {
    private final CommandOption[] options;

    CommandSubOption(Type type, String name, CommandOption... options) {
        super(type, name);
        this.options = options;
    }

    public CommandOption[] options() {
        return this.options;
    }
}
