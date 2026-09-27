package com.connexal.mcdlmi.components.speedyminecart;

import com.connexal.mcdlmi.api.command.Command;
import com.connexal.mcdlmi.api.command.arguments.CommandOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

public class MinecartSpeedCommand extends Command {
    private final SpeedyMinecarts component;

    public  MinecartSpeedCommand(SpeedyMinecarts component) {
        this.component = component;
    }

    @Override
    public boolean requiresOp() {
        return true;
    }

    @Override
    public String name() {
        return "minecartspeed";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public CommandOption[] options() {
        return new CommandOption[] {
                CommandOption.literal("max", CommandOption.word("number")),
                CommandOption.literal("increment", CommandOption.word("number"))
        };
    }

    @Override
    protected boolean run(CommandSender sender, String[] args) {
        if (args.length != 2) {
            return false;
        }

        // Check if the first argument is either "increment" or "max"
        boolean increment = args[0].equalsIgnoreCase("increment");
        if (!increment && !args[0].equalsIgnoreCase("max")) {
            return false;
        }

        // Attempt to parse the second argument as a double
        double number;
        try {
            number = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text("Invalid speed value. Please enter a number.", NamedTextColor.RED));
            return true;
        }

        // Validate that the number is positive
        if (number < 0) {
            sender.sendMessage(Component.text("Speed must be positive.", NamedTextColor.RED));
            return true;
        }

        // Set the appropriate minecart speed based on the first argument
        if (increment) {
            component.minecartSpeedIncrement(number);
            sender.sendMessage(Component.text("Minecart speed increment set to " + number, NamedTextColor.GREEN));
        } else {
            component.minecartMaxSpeed(number);
            sender.sendMessage(Component.text("Minecart max speed set to " + number, NamedTextColor.GREEN));
        }

        return true;
    }
}
