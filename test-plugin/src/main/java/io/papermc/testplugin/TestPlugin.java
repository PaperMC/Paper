package io.papermc.testplugin;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final Map<MenuType, Inventory> icache = new HashMap<>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, (event) -> {
            event.registrar().register(Commands.literal("mopen").executes((stack) -> {
                stack.getSource().getSender().sendMessage("Not Enough Arguments");
                return 1;
            }).then(Commands.argument("menu-type", ArgumentTypes.resource(RegistryKey.MENU)).executes((stack) -> {
                final Player executor = (Player) stack.getSource().getSender();
                final MenuType menu = stack.getArgument("menu-type", MenuType.class);
                if (menu.typed().builder() instanceof InventorySupport support) {
                    support.inventory(icache.computeIfAbsent(menu, (m) -> {
                        if (m == MenuType.GENERIC_9X1) {
                            return Bukkit.createInventory(null, 9);
                        }
                        if (m == MenuType.GENERIC_9X2) {
                            return Bukkit.createInventory(null, 9 * 2);
                        }
                        if (m == MenuType.GENERIC_9X3) {
                            return Bukkit.createInventory(null, 9 * 3);
                        }
                        if (m == MenuType.GENERIC_9X4) {
                            return Bukkit.createInventory(null, 9 * 4);
                        }
                        if (m == MenuType.GENERIC_9X5) {
                            return Bukkit.createInventory(null, 9 * 5);
                        }
                        if (m == MenuType.GENERIC_9X6) {
                            return Bukkit.createInventory(null, 9 * 6);
                        }
                        if (m == MenuType.GENERIC_3X3) {
                            return Bukkit.createInventory(null, 9);
                        }
                        if (m == MenuType.HOPPER) {
                            return Bukkit.createInventory(null, 5);
                        }
                        throw new IllegalStateException("Can't open type %s with inventory".formatted(stack.getInput()));
                    })).build(executor).open();
                }
                return 1;
            })).build());
        });
    }
}
