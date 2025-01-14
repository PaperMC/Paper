package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import io.papermc.paper.registry.keys.PaintingVariantKeys;
import org.bukkit.Art;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Painting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        final Entity targetEntity = event.getPlayer().getTargetEntity(10);
        if (targetEntity instanceof Painting painting) {
            painting.setArt(Art.create(f -> f.copyFrom(PaintingVariantKeys.BAROQUE).height(10)));
        }
    }
}
