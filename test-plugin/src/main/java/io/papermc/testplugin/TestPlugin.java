package io.papermc.testplugin;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, (handler) -> {
            handler.registrar().register(
                    literal("menu-open")
                            .then(
                                    argument("menu-type", ArgumentTypes.resource(RegistryKey.MENU))
                                            .executes(c -> {
                                                final CommandSender sender = c.getSource().getSender();
                                                if (!(sender instanceof Player player)) {
                                                    sender.sendMessage("Not Player");
                                                    return 1;
                                                }

                                                player.openInventory(c.getArgument("menu-type", MenuType.class)
                                                        .typed()
                                                        .builder()
                                                        .build(player)
                                                );
                                                return 1;
                                            }).then(
                                                    argument("look-at", ArgumentTypes.blockPosition())
                                                            .executes(
                                                                    c -> {
                                                                        final CommandSender sender = c.getSource().getSender();
                                                                        if (!(sender instanceof Player player)) {
                                                                            sender.sendMessage("Not Player");
                                                                            return 1;
                                                                        }

                                                                        final BlockPositionResolver resolver = c.getArgument("look-at", BlockPositionResolver.class);
                                                                        final BlockPosition pos = resolver.resolve(c.getSource());
                                                                        final Location location = pos.toLocation(player.getWorld());
                                                                        player.openInventory(((LocationInventoryViewBuilder<InventoryView>) c.getArgument("menu-type", MenuType.class)
                                                                                .typed()
                                                                                .builder())
                                                                                .checkReachable(true)
                                                                                .location(location)
                                                                                .build(player)
                                                                        );

                                                                        return 1;
                                                                    }
                                                            )
                                            )
                            )
                            .build()
            );
        });
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
