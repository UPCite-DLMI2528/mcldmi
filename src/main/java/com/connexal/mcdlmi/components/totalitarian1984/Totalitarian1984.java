package com.connexal.mcdlmi.components.totalitarian1984;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.connexal.mcdlmi.components.totalitarian1984.listeners.*;
import com.google.auto.service.AutoService;

import java.nio.file.Path;
import java.util.logging.Level;

@AutoService(DLMIComponent.class)
public class Totalitarian1984 implements DLMIComponent {
    private static final String LOG_FOLDER_NAME = "1984";

    private Logging1984 logger = null;

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("totalitarian-1984");
    }

    @Override
    public void enable(ComponentContext context) {
        Path logFolder = this.createLogFolder(context.plugin().getDataPath());
        this.logger = new Logging1984(logFolder);

        context.registries().events().register(new BlockListener(this.logger));
        context.registries().events().register(new BucketListener(this.logger));
        context.registries().events().register(new ChatListener(this.logger));
        context.registries().events().register(new ConnectionListener(this.logger));
        context.registries().events().register(new ContainerListener(this.logger));
        context.registries().events().register(new DeathListener(this.logger));
        context.registries().events().register(new ItemListener(this.logger));
        context.registries().events().register(new TeleportListener(this.logger));
    }

    @Override
    public void disable(ComponentContext context) {
        this.logger.flush();
    }

    private Path createLogFolder(Path parent) {
        // The folder might or might not exist. If it doesn't, create it. If it does, just return it.
        Path logFolder = parent.resolve(LOG_FOLDER_NAME);
        if (!logFolder.toFile().exists() && !logFolder.toFile().mkdirs()) {
            throw new RuntimeException("Unable to create log folder");
        }
        return logFolder;
    }
}
