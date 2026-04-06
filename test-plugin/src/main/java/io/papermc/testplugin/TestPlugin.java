package io.papermc.testplugin;

import java.util.Comparator;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        this.registerSleepTestCommands();

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    private void registerSleepTestCommands() {
        this.getServer().getCommandMap().register("test-plugin", new BukkitCommand("testsleep", "Sleep the nearest living entity at its current location", "/testsleep", List.of()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }

                LivingEntity target = findNearestLivingEntity(player);
                if (target == null) {
                    player.sendMessage("No living entity found nearby.");
                    return true;
                }

                boolean slept = target.sleep(target.getLocation());
                player.sendMessage("testsleep: " + target.getType() + " -> " + slept + " (sleeping=" + target.isSleeping() + ")");
                return true;
            }
        });

        this.getServer().getCommandMap().register("test-plugin", new BukkitCommand("testwakeup", "Wake the nearest sleeping living entity", "/testwakeup", List.of()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage("Only players can use this command.");
                    return true;
                }

                LivingEntity target = findNearestSleepingLivingEntity(player);
                if (target == null) {
                    player.sendMessage("No sleeping living entity found nearby.");
                    return true;
                }

                target.wakeup();
                player.sendMessage("testwakeup: " + target.getType() + " -> sleeping=" + target.isSleeping());
                return true;
            }
        });
    }

    private static LivingEntity findNearestLivingEntity(Player player) {
        return player.getLocation().getNearbyEntitiesByType(LivingEntity.class, 8.0, entity -> !(entity instanceof Player))
            .stream()
            .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(player.getLocation())))
            .orElse(null);
    }

    private static LivingEntity findNearestSleepingLivingEntity(Player player) {
        return player.getLocation().getNearbyEntitiesByType(LivingEntity.class, 8.0, entity -> !(entity instanceof Player) && entity.isSleeping())
            .stream()
            .min(Comparator.comparingDouble(entity -> entity.getLocation().distanceSquared(player.getLocation())))
            .orElse(null);
    }
}
