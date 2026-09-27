package com.connexal.mcdlmi.core.command;

import com.connexal.mcdlmi.api.command.Command;
import com.connexal.mcdlmi.api.command.CommandRegistry;
import com.connexal.mcdlmi.api.command.arguments.CommandOption;
import com.connexal.mcdlmi.api.command.arguments.CommandSubOption;
import com.connexal.mcdlmi.core.registry.AbstractNamedRegistry;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public final class DefaultCommandRegistry extends AbstractNamedRegistry<String, Command> implements CommandRegistry {
    private boolean registrationDone;

    public DefaultCommandRegistry(JavaPlugin plugin) {
        this.registrationDone = false;

        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, this::registerCommands);
    }

    @Override
    protected void beforeRegister(String key, Command command) {
        if (this.registrationDone) {
            throw new IllegalStateException("Cannot register commands after the registration phase has completed");
        }
    }

    @Override
    protected String createKey(Command command) {
        return command.name();
    }

    private void registerCommands(ReloadableRegistrarEvent<Commands> event) {
        this.registrationDone = true;

        for (Command command : this.values()) {
            LiteralArgumentBuilder<CommandSourceStack> builder = LiteralArgumentBuilder.<CommandSourceStack>literal(command.name())
                    .requires(source -> {
                        if (command.requiresOp()) {
                            return source.getSender().isOp();
                        } else {
                            return true;
                        }
                    });

            for (CommandOption option : command.options()) {
                this.processOption(command, option, builder);
            }

            builder.executes(context -> {
                command.execute(context.getSource().getSender(), new String[0]);
                return com.mojang.brigadier.Command.SINGLE_SUCCESS;
            });

            event.registrar().register(builder.build(), List.of(command.aliases()));
        }
    }

    private void processOption(Command command, CommandOption option, ArgumentBuilder<CommandSourceStack, ?> builder) {
        ArgumentBuilder<CommandSourceStack, ?> newOption;

        switch (option.type()) {
            case LITERAL -> newOption = LiteralArgumentBuilder.literal(option.name());
            case WORD -> newOption = RequiredArgumentBuilder.argument(option.name(), word());
            case GREEDY_STRING -> newOption = RequiredArgumentBuilder.argument(option.name(), greedyString());
            default -> throw new IllegalStateException("Command option not understood");
        }

        if (option instanceof CommandSubOption subOption) {
            for (CommandOption tmpOption : subOption.options()) {
                this.processOption(command, tmpOption, newOption);
            }
        }

        newOption.executes(context -> {
            String argsUnparsed = context.getInput().substring(context.getInput().indexOf(' ') + 1); // Remove the command name from the input
            String[] args = argsUnparsed.split(" ");

            command.execute(context.getSource().getSender(), args);
            return com.mojang.brigadier.Command.SINGLE_SUCCESS;
        });

        builder.then(newOption);
    }
}
