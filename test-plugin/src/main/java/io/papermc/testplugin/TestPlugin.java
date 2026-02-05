package io.papermc.testplugin;

import io.papermc.paper.advancement.CriteriaTrigger;
import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    final CriteriaTrigger<CustomTriggerInstance> trigger = (CriteriaTrigger<CustomTriggerInstance>) RegistryAccess.registryAccess().getRegistry(RegistryKey.TRIGGER_TYPE).get(TestPluginBootstrap.CUSTOM_TRIGGER_TYPE);

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        this.trigger.trigger(event.getPlayer(), custom -> custom.isFlying() == event.getPlayer().isFlying());
    }
}
