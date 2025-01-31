package io.papermc.testplugin;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Armadillo;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class TestPlugin extends JavaPlugin implements Listener {

    BlockData testBlockData;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
        testBlockData = Material.PISTON.createBlockData();

        this.getServer().getCommandMap().register("fallback", new BukkitCommand("test", "cool test command", "<>", List.of()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                sender.sendMessage(testBlockData.toString());
                return true;
            }
        });
        this.getServer().getCommandMap().register("fallback", new BukkitCommand("test2", "cool test command", "<>", List.of()) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                sender.sendMessage("hi");
                sender.sendMessage(Material.PISTON.createBlockData().toString());
                return true;
            }
        });
    }


}
