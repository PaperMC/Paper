package org.bukkit.plugin;

import com.google.common.base.Preconditions;
import io.papermc.paper.event.executor.EventExecutorFactory;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Interface which defines the class for event call backs to plugins
 */
public interface EventExecutor {
    public void execute(@NotNull Listener listener, @NotNull Event event) throws EventException;

    // Paper start
    @NotNull
    static EventExecutor create(@NotNull Method m, @NotNull Class<? extends Event> eventClass) {
        Preconditions.checkNotNull(m, "Null method");
        Preconditions.checkArgument(m.getParameterCount() != 0, "Incorrect number of arguments %s", m.getParameterCount());
        Preconditions.checkArgument(m.getParameterTypes()[0] == eventClass, "First parameter %s doesn't match event class %s", m.getParameterTypes()[0], eventClass);
        if (m.getReturnType() != Void.TYPE) {
            final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(m.getDeclaringClass());
            org.bukkit.Bukkit.getLogger().warning("@EventHandler method " + m.getDeclaringClass().getName() + (Modifier.isStatic(m.getModifiers()) ? '.' : '#') + m.getName()
                + " returns non-void type " + m.getReturnType().getName() + ". This is unsupported behavior and will no longer work in a future version of Paper."
                + " This should be reported to the developers of " + plugin.getPluginMeta().getDisplayName() + " (" + String.join(",", plugin.getPluginMeta().getAuthors()) + ')');
        }
        if (!m.trySetAccessible()) {
            final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(m.getDeclaringClass());
            throw new AssertionError(
                "@EventHandler method " + m.getDeclaringClass().getName() + (Modifier.isStatic(m.getModifiers()) ? '.' : '#') + m.getName() + " is not accessible."
                    + " This should be reported to the developers of " + plugin.getDescription().getName() + " (" + String.join(",", plugin.getDescription().getAuthors()) + ')'
            );
        }
        return EventExecutorFactory.create(m, eventClass);
    }
    // Paper end
}
