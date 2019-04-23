package org.bukkit.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;
import org.jetbrains.annotations.NotNull;

/**
 * A list of event handlers, stored per-event. Based on lahwran's fevents.
 */
public class HandlerList {

    /**
     * Handler array. This field being an array is the key to this system's
     * speed.
     */
    private volatile RegisteredListener[] handlers = null;

    /**
     * Dynamic handler lists. These are changed using register() and
     * unregister() and are automatically baked to the handlers array any time
     * they have changed.
     */
    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerslots;

    /**
     * List of all HandlerLists which have been created, for use in bakeAll()
     */
    private static ArrayList<HandlerList> allLists = new ArrayList<HandlerList>();

    /**
     * Bake all handler lists. Best used just after all normal event
     * registration is complete, ie just after all plugins are loaded if
     * you're using fevents in a plugin system.
     */
    public static void bakeAll() {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.bake();
            }
        }
    }

    /**
     * Unregister all listeners from all handler lists.
     */
    public static void unregisterAll() {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                synchronized (h) {
                    for (List<RegisteredListener> list : h.handlerslots.values()) {
                        list.clear();
                    }
                    h.handlers = null;
                }
            }
        }
    }

    /**
     * Unregister a specific plugin's listeners from all handler lists.
     *
     * @param plugin plugin to unregister
     */
    public static void unregisterAll(@NotNull Plugin plugin) {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.unregister(plugin);
            }
        }
    }

    /**
     * Unregister a specific listener from all handler lists.
     *
     * @param listener listener to unregister
     */
    public static void unregisterAll(@NotNull Listener listener) {
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                h.unregister(listener);
            }
        }
    }

    /**
     * Create a new handler list and initialize using EventPriority.
     * <p>
     * The HandlerList is then added to meta-list for use in bakeAll()
     */
    public HandlerList() {
        handlerslots = new EnumMap<EventPriority, ArrayList<RegisteredListener>>(EventPriority.class);
        for (EventPriority o : EventPriority.values()) {
            handlerslots.put(o, new ArrayList<RegisteredListener>());
        }
        synchronized (allLists) {
            allLists.add(this);
        }
    }

    /**
     * Register a new listener in this handler list
     *
     * @param listener listener to register
     */
    public synchronized void register(@NotNull RegisteredListener listener) {
        if (handlerslots.get(listener.getPriority()).contains(listener))
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        handlers = null;
        handlerslots.get(listener.getPriority()).add(listener);
    }

    /**
     * Register a collection of new listeners in this handler list
     *
     * @param listeners listeners to register
     */
    public void registerAll(@NotNull Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            register(listener);
        }
    }

    /**
     * Remove a listener from a specific order slot
     *
     * @param listener listener to remove
     */
    public synchronized void unregister(@NotNull RegisteredListener listener) {
        if (handlerslots.get(listener.getPriority()).remove(listener)) {
            handlers = null;
        }
    }

    /**
     * Remove a specific plugin's listeners from this handler
     *
     * @param plugin plugin to remove
     */
    public synchronized void unregister(@NotNull Plugin plugin) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerslots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
                if (i.next().getPlugin().equals(plugin)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) handlers = null;
    }

    /**
     * Remove a specific listener from this handler
     *
     * @param listener listener to remove
     */
    public synchronized void unregister(@NotNull Listener listener) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerslots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
                if (i.next().getListener().equals(listener)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) handlers = null;
    }

    /**
     * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
     */
    public synchronized void bake() {
        if (handlers != null) return; // don't re-bake when still valid
        List<RegisteredListener> entries = new ArrayList<RegisteredListener>();
        for (Entry<EventPriority, ArrayList<RegisteredListener>> entry : handlerslots.entrySet()) {
            entries.addAll(entry.getValue());
        }
        handlers = entries.toArray(new RegisteredListener[entries.size()]);
    }

    /**
     * Get the baked registered listeners associated with this handler list
     *
     * @return the array of registered listeners
     */
    @NotNull
    public RegisteredListener[] getRegisteredListeners() {
        RegisteredListener[] handlers;
        while ((handlers = this.handlers) == null) bake(); // This prevents fringe cases of returning null
        return handlers;
    }

    /**
     * Get a specific plugin's registered listeners associated with this
     * handler list
     *
     * @param plugin the plugin to get the listeners of
     * @return the list of registered listeners
     */
    @NotNull
    public static ArrayList<RegisteredListener> getRegisteredListeners(@NotNull Plugin plugin) {
        ArrayList<RegisteredListener> listeners = new ArrayList<RegisteredListener>();
        synchronized (allLists) {
            for (HandlerList h : allLists) {
                synchronized (h) {
                    for (List<RegisteredListener> list : h.handlerslots.values()) {
                        for (RegisteredListener listener : list) {
                            if (listener.getPlugin().equals(plugin)) {
                                listeners.add(listener);
                            }
                        }
                    }
                }
            }
        }
        return listeners;
    }

    /**
     * Get a list of all handler lists for every event type
     *
     * @return the list of all handler lists
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static ArrayList<HandlerList> getHandlerLists() {
        synchronized (allLists) {
            return (ArrayList<HandlerList>) allLists.clone();
        }
    }
}
