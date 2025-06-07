package io.papermc.testplugin;

import java.net.URI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        final TextComponent display = Component.text("Test Link", NamedTextColor.GOLD);
        final URI link = URI.create("https://www.google.com");
        this.getServer().getServerLinks().addLink(display, link);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
