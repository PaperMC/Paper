package io.papermc.testplugin;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.util.Strings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemCraftResult;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Arrays;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getLogger().info("TestPlugin enabled");
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        this.getLifecycleManager().registerEventHandler(
            LifecycleEvents.COMMANDS.newHandler((handler) -> {
                handler.registrar().register(Commands.literal("testcraft").executes(context -> {
                    if (context.getSource().getSender() instanceof Player player) {
                        ItemStack bottle = ItemStack.of(Material.HONEY_BOTTLE);
                        ItemStack air = ItemStack.empty();

                        ItemStack[] craftingGrid = {
                            air.clone(), bottle.clone(), bottle.clone(),
                            air.clone(), bottle.clone(), bottle.clone(),
                            air.clone(),    air.clone(),    air.clone()
                        };

                        ItemCraftResult result = Bukkit.craftItemResult(
                            craftingGrid,
                            player.getWorld(),
                            player
                        );
                        ItemStack[] resultingMatrix = result.getResultingMatrix();
                        player.sendMessage(Component.text(Arrays.toString(resultingMatrix)));
                    }
                    return 1;
                }).build());
            })
        );



    }
}
