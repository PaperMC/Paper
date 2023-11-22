package io.papermc.testplugin;

import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        final LifecycleEventManager<Plugin> lifecycles = this.getLifecycleManager();
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.handler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook SECOND");
        }).priority(1));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.handler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FIRST");
        }).priority(-1));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.handler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FOURTH (monitor)");
        }));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.handler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook THIRD");
        }).priority(100));

        lifecycles.registerEventHandler(LifecycleEvents.REGISTER_ANYWHERE_EVENT.handler(event -> {

        }));
    }
}
