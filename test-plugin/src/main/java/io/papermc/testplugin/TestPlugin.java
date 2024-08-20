package io.papermc.testplugin;

import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Registry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        System.out.println(Registry.ITEM.getTag(TestPluginBootstrap.AXE_PICKAXE));
        System.out.println(RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(TestPluginBootstrap.CUSTOM_ENCHANT));
    }

}
