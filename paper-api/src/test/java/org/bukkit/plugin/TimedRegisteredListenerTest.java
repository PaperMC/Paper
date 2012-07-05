package org.bukkit.plugin;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.junit.Test;

public class TimedRegisteredListenerTest {

    @Test
    public void testEventClass() throws EventException {
        Listener listener = new Listener() {};
        EventExecutor executor = new EventExecutor() {
            public void execute(Listener listener, Event event) {}
        };
        TestPlugin plugin = new TestPlugin("Test");

        PlayerInteractEvent interactEvent = new PlayerInteractEvent(null, null, null, null, null);
        PlayerMoveEvent moveEvent = new PlayerMoveEvent(null, null, null);
        BlockBreakEvent breakEvent = new BlockBreakEvent(null, null);

        TimedRegisteredListener trl = new TimedRegisteredListener(listener, executor, EventPriority.NORMAL, plugin, false);

        // Ensure that the correct event type is reported for a single event
        trl.callEvent(interactEvent);
        assertThat(trl.getEventClass(), is((Object) PlayerInteractEvent.class));
        // Ensure that no superclass is used in lieu of the actual event, after two identical event types
        trl.callEvent(interactEvent);
        assertThat(trl.getEventClass(), is((Object) PlayerInteractEvent.class));
        // Ensure that the closest superclass of the two events is chosen
        trl.callEvent(moveEvent);
        assertThat(trl.getEventClass(), is((Object) PlayerEvent.class));
        // As above, so below
        trl.callEvent(breakEvent);
        assertThat(trl.getEventClass(), is((Object) Event.class));
        // In the name of being thorough, check that it never travels down the hierarchy again.
        trl.callEvent(breakEvent);
        assertThat(trl.getEventClass(), is((Object) Event.class));

        trl = new TimedRegisteredListener(listener, executor, EventPriority.NORMAL, plugin, false);

        trl.callEvent(breakEvent);
        assertThat(trl.getEventClass(), is((Object) BlockBreakEvent.class));
        // Test moving up the class hierarchy by more than one class at a time
        trl.callEvent(moveEvent);
        assertThat(trl.getEventClass(), is((Object) Event.class));
    }
}
