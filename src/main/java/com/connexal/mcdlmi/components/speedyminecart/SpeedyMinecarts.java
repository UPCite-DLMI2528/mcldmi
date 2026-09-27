package com.connexal.mcdlmi.components.speedyminecart;

import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.connexal.mcdlmi.api.config.Config;
import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Supplier;

@AutoService(DLMIComponent.class)
public class SpeedyMinecarts implements DLMIComponent {
    static final double DEFAULT_MAX_SPEED = 0.4;
    static final double DEFAULT_SPEED_INCREMENT = 1;
    static final List<Material> RAIL_BLOCKS = ImmutableList.of(
            Material.RAIL, Material.POWERED_RAIL,
            Material.DETECTOR_RAIL, Material.ACTIVATOR_RAIL
    );

    private Supplier<List<World>> worldsSupplier;
    private Config config;
    private double maxSpeed;
    private double speedIncrement;

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("speedy-minecarts");
    }

    @Override
    public void enable(ComponentContext context) {
        this.config = context.configSupplier().get();
        this.worldsSupplier = () -> context.server().getWorlds();

        this.loadSaved();

        context.registries().commands().register(new MinecartSpeedCommand(this));
        context.registries().events().register(new MinecartSpeedListener(this));
    }

    private void loadSaved() {
        double maxSpeed;
        if (!this.config.contains("max-speed")) {
            maxSpeed = DEFAULT_MAX_SPEED; // Default minecart speed
        } else {
            maxSpeed = this.config.getDouble("max-speed");
        }
        this.minecartMaxSpeed(maxSpeed);


        double speedIncrement;
        if (!this.config.contains("speed-increment")) {
            speedIncrement = DEFAULT_SPEED_INCREMENT; // Default speed increment
        } else {
            speedIncrement = this.config.getDouble("speed-increment");
        }
        this.minecartSpeedIncrement(speedIncrement);
    }

    public double minecartMaxSpeed() {
        return this.maxSpeed;
    }

    public void minecartMaxSpeed(double speed) {
        this.maxSpeed = speed;
        this.config.set("max-speed", speed);
        this.config.save();

        this.worldsSupplier.get().stream()
                .flatMap(world -> world.getEntitiesByClass(Minecart.class).stream())
                .filter(minecart -> minecart.getPassengers().stream().anyMatch(passenger -> passenger instanceof Player))
                .forEach(minecart -> minecart.setMaxSpeed(speed));
    }

    public double minecartSpeedIncrement() {
        return this.speedIncrement;
    }

    public void minecartSpeedIncrement(double increment) {
        this.speedIncrement = increment;
        this.config.set("speed-increment", increment);
        this.config.save();
    }
}
