package io.papermc.testplugin;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.JukeboxPlayable;
import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.JukeboxSong;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        final ItemStack stick = new ItemStack(Material.STICK);
        final Registry<JukeboxSong> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.JUKEBOX_SONG);
        final JukeboxSong orThrow = registry.getOrThrow(TestPluginBootstrap.NEW);
        stick.setData(DataComponentTypes.JUKEBOX_PLAYABLE, JukeboxPlayable.jukeboxPlayable(orThrow));
        event.getPlayer().getInventory().addItem(stick);
    }

}
