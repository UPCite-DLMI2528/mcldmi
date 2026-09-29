package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.command.Command;
import com.connexal.mcdlmi.api.command.arguments.CommandOption;
import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

@AutoService(DLMIComponent.class)
public class ShowItemCommand extends Command implements DLMIComponent {
    private Consumer<Component> broadcast;

    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("show-item-command");
    }

    @Override
    public void enable(ComponentContext context) {
        this.broadcast = context.plugin().getServer()::broadcast;

        context.registries().commands().register(this);
    }

    @Override
    public boolean requiresOp() {
        return false;
    }

    @Override
    public String name() {
        return "showitem";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

    @Override
    public CommandOption[] options() {
        return new CommandOption[0];
    }

    @Override
    protected boolean run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();

            Component hoverable = item.displayName().hoverEvent(item.asHoverEvent());
            Component message = player.displayName().append(Component.text(" is holding: ")).append(hoverable);

            this.broadcast.accept(message);
        } else {
            sender.sendMessage(Component.text("You must be a player to use this command!", NamedTextColor.RED));
        }
        return true;
    }
}
