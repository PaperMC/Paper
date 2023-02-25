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

        final GameEvent newEvent = Registry.GAME_EVENT.get(TestPluginBootstrap.NEW_EVENT);
        if (newEvent == null) {
            throw new RuntimeException("could not find new event");
        } else {
            System.out.println("New event: " + newEvent.getKey() + " " + newEvent.getRange());
        }
    }

}
