package io.papermc.testplugin;

import com.destroystokyo.paper.exception.ServerInternalException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        this.getSLF4JLogger().error("hi", new ServerInternalException("poop"));
        this.getSLF4JLogger().warn("hi", new RuntimeException("poop"));
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }
}
