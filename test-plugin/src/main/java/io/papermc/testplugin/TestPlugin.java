package io.papermc.testplugin;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Art;
import org.bukkit.Registry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        final Registry<Art> registry = RegistryAccess.registryAccess().getRegistry(RegistryKey.PAINTING_VARIANT);
        final Art art = registry.get(TestPluginBootstrap.NEW);
        System.out.println(art.getKey());
    }

}
