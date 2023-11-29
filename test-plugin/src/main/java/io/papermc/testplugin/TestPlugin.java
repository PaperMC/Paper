package io.papermc.testplugin;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    private final BootstrapContext context;
    public TestPlugin(BootstrapContext context) {
        this.context = context;
    }

    @Override
    public void onEnable() {
        System.out.println(context);
        // context.getLifecycleManager().registerEventHandler(LifecycleEvents.DUMMY.handler(event -> {
        //
        // }));
        this.getServer().getPluginManager().registerEvents(this, this);

        final LifecycleEventManager<Plugin> lifecycles = this.getLifecycleManager();
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.newHandler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FIRST");
        }).priority(-1));
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.newHandler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FOURTH (monitor)");
        }).monitor());
        lifecycles.registerEventHandler(LifecycleEvents.DUMMY_STATIC.newHandler(event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook THIRD");
        }).priority(100));

        // lifecycles.registerEventHandler(LifecycleEvents.DUMMY.handler(event -> { // shouldn't compile
        // }));

        lifecycles.registerEventHandler(LifecycleEvents.REGISTER_ANYWHERE_EVENT.newHandler(event -> {

        }));
    }
}
