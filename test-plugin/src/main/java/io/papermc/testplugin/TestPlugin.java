package io.papermc.testplugin;

import io.papermc.paper.event.dialog.CustomClickActionEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @EventHandler
    public void onCustomClick(CustomClickActionEvent event) {
        event.getPlayer().sendMessage(Component.text(event.id().asString()));
    }
}
