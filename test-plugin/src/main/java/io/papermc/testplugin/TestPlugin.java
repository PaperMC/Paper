package io.papermc.testplugin;

import io.papermc.paper.plugin.lifecycle.dummy.DummyResourceRegistrar;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        this.handleLifecycleEventWithPriority(LifecycleEvents.DUMMY_STATIC, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook SECOND");
        }, 1);
        this.handleLifecycleEventWithPriority(LifecycleEvents.DUMMY_STATIC, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FIRST");
        }, -1);
        this.handleLifecycleEventAsMonitor(LifecycleEvents.DUMMY_STATIC, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook FOURTH (monitor)");
        });
        this.handleLifecycleEventWithPriority(LifecycleEvents.DUMMY_STATIC, event -> {
            final DummyResourceRegistrar registrar = event.registrar();
            System.out.println("dummy_static hook THIRD");
        }, 100);
    }
}
