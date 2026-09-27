package com.connexal.mcdlmi.components.speedyminecart;

import net.kyori.adventure.util.TriState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleCreateEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.util.Vector;

public class MinecartSpeedListener implements Listener {
    private final SpeedyMinecarts component;

    public MinecartSpeedListener(SpeedyMinecarts component) {
        this.component = component;
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        if (event.getVehicle() instanceof Minecart minecart) {
            minecart.setMaxSpeed(component.minecartMaxSpeed());
            minecart.setSlowWhenEmpty(true);
            //minecart.setDerailedVelocityMod(new Vector(0, 0, 0));
            //minecart.setFlyingVelocityMod(new Vector(0, 0, 0));
            minecart.setFrictionState(TriState.FALSE);
        }
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Minecart minecart) {
            if (minecart.isEmpty()) return;
            if (minecart.getPassengers().stream().noneMatch(p -> p instanceof Player)) return;

            Block railBlock = minecart.getLocation().getBlock();
            if (!SpeedyMinecarts.RAIL_BLOCKS.contains(railBlock.getType())) return;

            double maxSpeed = component.minecartMaxSpeed();
            double speedIncrement = component.minecartSpeedIncrement();

            if (vehicle.getVelocity().length() < maxSpeed && railBlock.getType() == Material.POWERED_RAIL) {
                // Check if the powered rail is active
                Powerable powerableData = (Powerable) railBlock.getBlockData();
                if (powerableData.isPowered()) {
                    Vector velocity = vehicle.getVelocity().multiply(speedIncrement);
                    vehicle.setVelocity(velocity);
                }
            }

            minecart.setMaxSpeed(maxSpeed);
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        if (event.getVehicle() instanceof Minecart minecart) {
            if (!(event.getExited() instanceof Player)) return;

            if (minecart.getMaxSpeed() > SpeedyMinecarts.DEFAULT_MAX_SPEED) {
                minecart.setMaxSpeed(SpeedyMinecarts.DEFAULT_MAX_SPEED);
            }
        }
    }
}
