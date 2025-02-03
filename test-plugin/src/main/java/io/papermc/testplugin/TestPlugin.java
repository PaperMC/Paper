package io.papermc.testplugin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.event.player.PlayerItemCooldownEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.MusicInstrument;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        /**
        this.getServer().getCommandMap().register("fallback", new BukkitCommand("test", "cool test command", "<>", new ArrayList<>()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (sender instanceof Player player) {
                    ItemStack item = player.getInventory().getItemInMainHand();
                    if (item.hasData(DataComponentTypes.INSTRUMENT)) {
                        MusicInstrument musicInstrument = item.getData(DataComponentTypes.INSTRUMENT);
                        Component componentInstrumentDescription = (musicInstrument.description() != null) ? musicInstrument.description() : Component.text("Nothing");
                        player.sendMessage(Component.text().append(Component.text("Instrument data for")).appendSpace().append(componentInstrumentDescription));
                        player.sendMessage(Component.text().append(Component.text("Range:")).appendSpace().append(Component.text(musicInstrument.getRange())));
                        player.sendMessage(Component.text().append(Component.text("Duration:")).appendSpace().append(Component.text(musicInstrument.getDuration())));
                        if (musicInstrument.getSoundEvent() != null) {
                            player.sendMessage(Component.text().append(Component.text("SoundEvent:")).appendSpace().append(Component.text(musicInstrument.getSoundEvent().toString())));
                        } else {
                            player.sendMessage(Component.text().append(Component.text("Nothing for SoundEvent")));
                        }
                    } else {
                        player.sendMessage("Item not has instrument component");
                    }
                }
                return true;
            }
        });
         */

        this.getServer().getCommandMap().register("fallback", new BukkitCommand("test2", "cool test command", "<>", new ArrayList<>()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (sender instanceof Player player) {
                    player.setCooldown(player.getInventory().getItemInMainHand(), 20 * 5);
                    player.sendMessage("Cooldown set");
                }
                return true;
            }
        });
    }

    @EventHandler
    public void onCooldown(PlayerItemCooldownEvent event) {
        final Player player = event.getPlayer();
        player.sendMessage("Cooldown called for " + event.getType() + " for " + event.getCooldown() + " ticks");
        event.setCancelled(true);
    }
}
