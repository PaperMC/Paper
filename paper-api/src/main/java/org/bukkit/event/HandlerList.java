package org.bukkit.event;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.*;
import java.util.Map.Entry;

/**
 * A list of event handlers, stored per-event. Based on lahwran's fevents.
 */
@SuppressWarnings("unchecked")
public class HandlerList {
    /**
     * Handler array. This field being an array is the key to this system's speed.
     */
    private RegisteredListener[][] handlers = new RegisteredListener[EventPriority.values().length][];

    /**
     * Dynamic handler lists. These are changed using register() and
     * unregister() and are automatically baked to the handlers array any
     * time they have changed.
     */
    private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerslots;

    /**
     * Whether the current HandlerList has been fully baked. When this is set
     * to false, the Map<EventPriority, List<RegisteredListener>> will be baked to RegisteredListener[][]
     * next time the event is called.
     *
     * @see org.bukkit.plugin.SimplePluginManager#callEvent
     */
    private boolean baked = false;

    /**
     * List of all HandlerLists which have been created, for use in bakeAll()
     */
    private static ArrayList<HandlerList> alllists = new ArrayList<HandlerList>();

    /**
     * Bake all handler lists. Best used just after all normal event
     * registration is complete, ie just after all plugins are loaded if
     * you're using fevents in a plugin system.
     */
    public static void bakeAll() {
        for (HandlerList h : alllists) {
            h.bake();
        }
    }

    public static void unregisterAll() {
        for (HandlerList h : alllists) {
            for (List<RegisteredListener> list : h.handlerslots.values()) {
                list.clear();
            }
            h.baked = false;
        }
    }

    public static void unregisterAll(Plugin plugin) {
        for (HandlerList h : alllists) {
            h.unregister(plugin);
        }
    }

    /**
     * Create a new handler list and initialize using EventPriority
     * The HandlerList is then added to meta-list for use in bakeAll()
     */
    public HandlerList() {
        handlerslots = new EnumMap<EventPriority, ArrayList<RegisteredListener>>(EventPriority.class);
        for (EventPriority o : EventPriority.values()) {
            handlerslots.put(o, new ArrayList<RegisteredListener>());
        }
        alllists.add(this);
    }

    /**
     * Register a new listener in this handler list
     *
     * @param listener listener to register
     */
    public void register(RegisteredListener listener) {
        if (handlerslots.get(listener.getPriority()).contains(listener))
            throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
        baked = false;
        handlerslots.get(listener.getPriority()).add(listener);
    }

    public void registerAll(Collection<RegisteredListener> listeners) {
        for (RegisteredListener listener : listeners) {
            register(listener);
        }
    }

    /**
     * Remove a listener from a specific order slot
     *
     * @param listener listener to remove
     */
    public void unregister(RegisteredListener listener) {
        if (handlerslots.get(listener.getPriority()).contains(listener)) {
            baked = false;
            handlerslots.get(listener.getPriority()).remove(listener);
        }
    }

    void unregister(Plugin plugin) {
        boolean changed = false;
        for (List<RegisteredListener> list : handlerslots.values()) {
            for (ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
                if (i.next().getPlugin().equals(plugin)) {
                    i.remove();
                    changed = true;
                }
            }
        }
        if (changed) baked = false;
    }

    /**
     * Bake HashMap and ArrayLists to 2d array - does nothing if not necessary
     */
    public void bake() {
        if (baked) return; // don't re-bake when still valid
        for (Entry<EventPriority, ArrayList<RegisteredListener>> entry : handlerslots.entrySet()) {
            handlers[entry.getKey().getSlot()] = (entry.getValue().toArray(new RegisteredListener[entry.getValue().size()]));
        }
        baked = true;
    }

    public RegisteredListener[][] getRegisteredListeners() {
        return handlers;
    }
}
