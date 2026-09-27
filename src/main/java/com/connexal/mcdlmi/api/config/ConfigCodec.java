package com.connexal.mcdlmi.api.config;

public enum ConfigCodec {
    YAML("yml");

    private final String name;

    ConfigCodec(String name) {
        this.name = name;
    }

    public String extension() {
        return this.name;
    }
}
