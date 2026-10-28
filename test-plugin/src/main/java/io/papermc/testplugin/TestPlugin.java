package io.papermc.testplugin;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().showDialog(RegistryAccess.registryAccess().getRegistry(RegistryKey.DIALOG).get(TestPluginBootstrap.CUSTOM_DIALOG));
    }
}
