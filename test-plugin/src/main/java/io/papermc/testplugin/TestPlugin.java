package io.papermc.testplugin;

import io.papermc.paper.plugin.event.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.event.hook.RegisterHooks;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        RegisterHooks.DUMMY_STATIC.add(this, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook");
        });
    }
}
