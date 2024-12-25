package io.papermc.testplugin;

import io.papermc.testplugin.lidded.LiddedCommands;
import io.papermc.testplugin.lidded.LiddedTestListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        LiddedCommands.registerAll(this);
        getServer().getPluginManager()
            .registerEvents(new LiddedTestListener(this.getLogger()), this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

}
