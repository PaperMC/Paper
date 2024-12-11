package io.papermc.paper.plugin.lifecycle.event;

import io.papermc.paper.plugin.lifecycle.event.registrar.PaperRegistrar;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.registrar.RegistrarEventImpl;
import io.papermc.paper.plugin.lifecycle.event.registrar.ReloadableRegistrarEvent;
import io.papermc.paper.plugin.lifecycle.event.types.AbstractLifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEventType;
import io.papermc.paper.plugin.lifecycle.event.types.OwnerAwareLifecycleEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class LifecycleEventRunner {

    public static final LifecycleEventRunner INSTANCE = new LifecycleEventRunner();

    private final List<LifecycleEventType<?, ?, ?>> lifecycleEventTypes = new ArrayList<>();
    private boolean blockPluginReloading = false;

    public <O extends LifecycleEventOwner> void checkRegisteredHandler(final O owner, final AbstractLifecycleEventType<O, ?, ?> eventType) {
        /*
        Lifecycle event handlers for reloadable events that are registered from the BootstrapContext prevent
        the server from reloading plugins. This is because reloading plugins requires disabling all the plugins,
        running the reload logic (which would include places where these events should fire) and then re-enabling plugins.
         */
        if (eventType.blocksReloading(owner)) {
            this.blockPluginReloading = true;
        }
    }

    public boolean blocksPluginReloading() {
        return this.blockPluginReloading;
    }

    public <O extends LifecycleEventOwner, E extends LifecycleEvent, ET extends LifecycleEventType<O, E, ?>> void addEventType(final ET eventType) {
        this.lifecycleEventTypes.add(eventType);
    }

    public <O extends LifecycleEventOwner, E extends PaperLifecycleEvent> void callEvent(final LifecycleEventType<O, ? super E, ?> eventType, final E event) {
        this.callEvent(eventType, event, $ -> true);
    }

    public <O extends LifecycleEventOwner, E extends PaperLifecycleEvent> void callEvent(final LifecycleEventType<O, ? super E, ?> eventType, final E event, final Predicate<? super O> ownerPredicate) {
        final AbstractLifecycleEventType<O, ? super E, ?> lifecycleEventType = (AbstractLifecycleEventType<O, ? super E, ?>) eventType;
        lifecycleEventType.forEachHandler(event, registeredHandler -> {
            try {
                if (event instanceof final OwnerAwareLifecycleEvent<?> ownerAwareEvent) {
                    ownerAwareGenericHelper(ownerAwareEvent, registeredHandler.owner());
                }
                registeredHandler.lifecycleEventHandler().run(event);
            } catch (final Throwable ex) {
                throw new RuntimeException("Could not run '%s' lifecycle event handler from %s".formatted(lifecycleEventType.name(), registeredHandler.owner().getPluginMeta().getDisplayName()), ex);
            } finally {
                if (event instanceof final OwnerAwareLifecycleEvent<?> ownerAwareEvent) {
                    ownerAwareEvent.setOwner(null);
                }
            }
        }, handler -> ownerPredicate.test(handler.owner()));
        event.invalidate();
    }

    private static <O extends LifecycleEventOwner> void ownerAwareGenericHelper(final OwnerAwareLifecycleEvent<O> event, final LifecycleEventOwner possibleOwner) {
        final @Nullable O owner = event.castOwner(possibleOwner);
        if (owner != null) {
            event.setOwner(owner);
        } else {
            throw new IllegalStateException("Found invalid owner " + possibleOwner + " for event " + event);
        }
    }

    public void unregisterAllEventHandlersFor(final Plugin plugin) {
        for (final LifecycleEventType<?, ?, ?> lifecycleEventType : this.lifecycleEventTypes) {
            this.removeEventHandlersOwnedBy(lifecycleEventType, plugin);
        }
    }

    private <O extends LifecycleEventOwner> void removeEventHandlersOwnedBy(final LifecycleEventType<O, ?, ?> eventType, final Plugin possibleOwner) {
        final AbstractLifecycleEventType<O, ?, ?> lifecycleEventType = (AbstractLifecycleEventType<O, ?, ?>) eventType;
        lifecycleEventType.removeMatching(registeredHandler -> registeredHandler.owner().getPluginMeta().getName().equals(possibleOwner.getPluginMeta().getName()));
    }

    @SuppressWarnings("unchecked")
    public <O extends LifecycleEventOwner, R extends PaperRegistrar<? super O>> void callStaticRegistrarEvent(final LifecycleEventType<O, ? extends RegistrarEvent<? super R>, ?> lifecycleEventType, final R registrar, final Class<? extends O> ownerClass) {
        this.callEvent((LifecycleEventType<O, RegistrarEvent<? super R>, ?>) lifecycleEventType, new RegistrarEventImpl<>(registrar, ownerClass), ownerClass::isInstance);
    }

    @SuppressWarnings("unchecked")
    public <O extends LifecycleEventOwner, R extends PaperRegistrar<? super O>> void callReloadableRegistrarEvent(final LifecycleEventType<O, ? extends ReloadableRegistrarEvent<? super R>, ?> lifecycleEventType, final R registrar, final Class<? extends O> ownerClass, final ReloadableRegistrarEvent.Cause cause) {
        this.callEvent((LifecycleEventType<O, ReloadableRegistrarEvent<? super R>, ?>) lifecycleEventType, new RegistrarEventImpl.ReloadableImpl<>(registrar, ownerClass, cause), ownerClass::isInstance);
    }

    private LifecycleEventRunner() {
    }
}
