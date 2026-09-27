package com.connexal.mcdlmi.api.command.arguments;

public sealed class CommandOption permits CommandSubOption {
    private final Type type;
    private final String name;

    protected CommandOption(Type type, String name) {
        this.type = type;
        this.name = name;
    }

    public Type type() {
        return this.type;
    }

    public String name() {
        return this.name;
    }

    public enum Type {
        LITERAL,
        WORD,
        GREEDY_STRING
    }

    public static CommandOption literal(String name, CommandOption... options) {
        return new CommandSubOption(Type.LITERAL, name, options);
    }

    public static CommandOption literal(String name) {
        return new CommandOption(Type.LITERAL, name);
    }

    public static CommandOption word(String name, CommandOption... options) {
        return new CommandSubOption(Type.WORD, name, options);
    }

    public static CommandOption word(String name) {
        return new CommandOption(Type.WORD, name);
    }

    public static CommandOption greedyString(String name) {
        return new CommandOption(Type.GREEDY_STRING, name);
    }
}
