package io.papermc.testplugin;

import org.bukkit.GameEvent;
import org.bukkit.Registry;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        final GameEvent gameEvent = Registry.GAME_EVENT.getOrThrow(TestPluginBootstrap.NEW_GAME_EVENT);
        System.out.println("custom range: " + gameEvent.getRange());
    }

}
