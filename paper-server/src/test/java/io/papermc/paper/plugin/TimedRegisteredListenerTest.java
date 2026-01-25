package io.papermc.paper.plugin;

import org.bukkit.craftbukkit.event.player.CraftPlayerInteractEvent;
import org.bukkit.craftbukkit.event.player.CraftPlayerMoveEvent;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.TimedRegisteredListener;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.api.Test;

import static org.bukkit.support.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Normal
public class TimedRegisteredListenerTest {

    @Test
    public void testEventClass() throws EventException {
        Listener listener = new Listener() {};
        EventExecutor executor = new EventExecutor() {
            @Override
            public void execute(Listener listener, Event event) {}
        };
        PaperTestPlugin plugin = new PaperTestPlugin("TimedRegisteredListenerTestPlugin");

        PlayerInteractEvent interactEvent = new CraftPlayerInteractEvent(null, null, null, null, null, null, null);
        PlayerMoveEvent moveEvent = new CraftPlayerMoveEvent(null, null, null);
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
