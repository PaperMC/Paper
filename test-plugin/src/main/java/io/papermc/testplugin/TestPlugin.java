package io.papermc.testplugin;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.MenuType;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, (c) -> {
            c.registrar().register(Commands.literal("topen")
                    .then(Commands.argument("menu", ArgumentTypes.resource(RegistryKey.MENU)).executes((context) -> {
                        ((Player) context.getSource().getSender()).openInventory(
                                context.getArgument("menu", MenuType.class).typed().builder().title(Component.text("12")).build((Player) context.getSource().getSender())
                        );
                        return 1;
                    }))
                    .build());
        });
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
