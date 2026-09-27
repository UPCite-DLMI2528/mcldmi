package com.connexal.mcdlmi.components.misc;

import com.connexal.mcdlmi.api.command.Command;
import com.connexal.mcdlmi.api.command.arguments.CommandOption;
import com.connexal.mcdlmi.api.component.ComponentContext;
import com.connexal.mcdlmi.api.component.ComponentInfo;
import com.connexal.mcdlmi.api.component.DLMIComponent;
import com.google.auto.service.AutoService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@AutoService(DLMIComponent.class)
public class HeadItemCommand extends Command implements DLMIComponent {
    @Override
    public ComponentInfo info() {
        return ComponentInfo.of("head-item-command");
    }

    @Override
    public void enable(ComponentContext context) {
        context.registries().commands().register(this);
    }

    @Override
    public boolean requiresOp() {
        return false;
    }

    @Override
    public String name() {
        return "headitem";
    }

    @Override
    public String[] aliases() {
        return new String[] { "hat" };
    }

    @Override
    public CommandOption[] options() {
        return new CommandOption[0];
    }

    @Override
    protected boolean run(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            PlayerInventory inv = player.getInventory();

            ItemStack handItem = inv.getItemInMainHand();
            ItemStack headItem = inv.getHelmet();

            inv.setHelmet(handItem);
            inv.setItemInMainHand(headItem);
        } else {
            sender.sendMessage(Component.text("You must be a player to use this command!", NamedTextColor.RED));
        }
        return true;
    }
}
