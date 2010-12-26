
package com.dinnerbone.bukkit.sample;

import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Handle events for all Player related events
 * @author Dinnerbone
 */
public class SamplePlayerListener extends PlayerListener {
    private final SamplePlugin plugin;

    public SamplePlayerListener(SamplePlugin instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerJoin(PlayerEvent event) {
        System.out.println(event.getPlayer().getName() + " joined the server! :D");
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
        System.out.println(event.getPlayer().getName() + " left the server! :'(");
    }
}
