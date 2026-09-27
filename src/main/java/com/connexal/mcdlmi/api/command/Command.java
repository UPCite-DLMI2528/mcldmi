package com.connexal.mcdlmi.api.command;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.api.command.arguments.CommandOption;
import com.connexal.mcdlmi.api.command.arguments.CommandSubOption;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

public abstract class Command {
    public abstract boolean requiresOp();

    public abstract String name();

    public abstract String[] aliases();

    public abstract CommandOption[] options();

    protected abstract boolean run(CommandSender sender, String[] args);

    public boolean execute(CommandSender sender, String[] args) {
        if (this.requiresOp() && !sender.isOp()) {
            sender.sendMessage(Component.text("You do not have permission to use this command.", NamedTextColor.RED));
            return true;
        }

        if (!this.run(sender, args)) {
            this.sendHelp(sender);
        }

        return true;
    }

    private void sendHelp(CommandSender sender) {
        StringBuilder builder = new StringBuilder("The correct usage is:\n");

        if (this.options().length == 0) {
            builder.append(" /").append(this.name());
        } else {
            for (CommandOption option : this.options()) {
                this.buildUsageNode(builder, "\n -", option);
            }
        }

        sender.sendMessage(builder.toString());
    }

    private String buildName(CommandOption option) {
        return switch (option.type()) {
            case LITERAL -> option.name();
            case WORD -> "<" + option.name() + ">";
            case GREEDY_STRING -> "<" + option.name() + "...>";
        };
    }

    private void buildUsageNode(StringBuilder builder, String prefix, CommandOption option) {
        if (option instanceof CommandSubOption subOption) {
            for (CommandOption sub : subOption.options()) {
                this.buildUsageNode(builder, prefix + " " + this.buildName(subOption), sub);
            }
        } else {
            builder.append(prefix).append(" ").append(this.buildName(option));
        }
    }

    public void completeAsync(Runnable runnable) {
        MCDLMI.scheduleTask(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                MCDLMI.getLog().log(Level.SEVERE, "An error occurred while executing a command completion task: " + e.getMessage(), e);
            }
        });
    }
}
