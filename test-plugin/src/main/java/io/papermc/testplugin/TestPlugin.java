package io.papermc.testplugin;

import io.papermc.paper.event.dialog.CompoundClickActionEvent;
import io.papermc.paper.event.dialog.SimpleClickActionEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
    public void onCustomClick(SimpleClickActionEvent event) {
        event.getPlayer().sendMessage(Component.text(event.id().asString()));
    }

    @EventHandler
    public void onCompoundClick(CompoundClickActionEvent event) {
        if(event.id().equals(Key.key("paper", "say_message"))) {
            for(var p : Bukkit.getOnlinePlayers()) {
                p.sendMessage(Component.text(event.payload().getString("name") + " said: " + event.payload().getString("message")));
            }
        }
    }
}
