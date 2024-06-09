package io.papermc.testplugin;

import io.papermc.paper.registry.keys.GameEventKeys;
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
        final GameEvent changed = Registry.GAME_EVENT.get(GameEventKeys.BLOCK_OPEN);
        System.out.println("changed: " + changed.getRange());
        final GameEvent same = Registry.GAME_EVENT.get(GameEventKeys.CONTAINER_OPEN);
        System.out.println("same: " + same.getRange());
    }

}
