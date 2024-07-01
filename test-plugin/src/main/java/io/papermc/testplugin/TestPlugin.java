package io.papermc.testplugin;

import io.papermc.paper.entity.TeleportFlag;
import io.papermc.paper.event.player.ChatEvent;
import java.util.Collection;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        final Player player = event.getPlayer();
        final Collection<Pig> nearby = player.getWorld().getNearbyEntitiesByType(Pig.class, player.getLocation(), 10);
        for (final Pig pig : nearby) {
            if (!pig.getPassengers().isEmpty()) {
                System.out.println("Teleport: " + pig.teleport(player.getLocation()));
            }
        }
    }

    @EventHandler
    public void onEvent(EntityTeleportEvent event) {
        System.out.println("EntityTeleportEvent");
        System.out.println(event.getEntity());
        System.out.println(event.getClass().getSimpleName());
    }

    @EventHandler
    public void onEvent(PlayerTeleportEvent event) {
        System.out.println("PlayerTeleportEvent");
        System.out.println(event.getPlayer());
        System.out.println(event.getClass().getSimpleName());
    }

}
