package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.support.environment.VanillaFeature;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

@VanillaFeature
class LegacyVelocityTrackerTest {

    @Test
    void trackerPublishesFinalVelocityAndHonorsLateEventChanges() throws Exception {
        checkTracker(false, true, true);
    }

    @Test
    void cancelledLegacyTrackerVelocityIsConsumedWithoutSendingOrRetrying() throws Exception {
        checkTracker(true, true, false);
    }

    @Test
    void unmarkedPlayerRetainsNativeCancellationRetryBehavior() throws Exception {
        checkTracker(true, false, false);
    }

    private void checkTracker(boolean cancelled, boolean legacy, boolean modify) throws Exception {
        ServerPlayer victim = mock(ServerPlayer.class);
        victim.legacyCombat = legacy;
        victim.hurtMarked = true;
        AtomicReference<Vec3> movement = new AtomicReference<>(Vec3.ZERO);
        when(victim.getDeltaMovement()).thenAnswer(call -> movement.get());
        when(victim.trackingPosition()).thenReturn(Vec3.ZERO);
        when(victim.getPassengers()).thenReturn(List.of());
        when(victim.getEntityData()).thenReturn(mock(SynchedEntityData.class));
        AttributeMap attributes = mock(AttributeMap.class);
        when(attributes.getAttributesToSync()).thenReturn(new java.util.HashSet<>());
        when(victim.getAttributes()).thenReturn(attributes);
        CraftPlayer bukkit = mock(CraftPlayer.class);
        when(victim.getBukkitEntity()).thenReturn(bukkit);
        when(bukkit.getVelocity()).thenAnswer(call -> new Vector(movement.get().x, movement.get().y, movement.get().z));
        doAnswer(call -> {
            Vector velocity = call.getArgument(0);
            movement.set(new Vec3(velocity.getX(), velocity.getY(), velocity.getZ()));
            return null;
        }).when(bukkit).setVelocity(any(Vector.class));
        ServerEntity.Synchronizer network = mock(ServerEntity.Synchronizer.class);
        ServerEntity tracker = new ServerEntity(mock(ServerLevel.class), victim, 20, false, network, Set.of());
        // Make the normal delta path eligible too: it must not publish a
        // tracking-only motion packet before the final cancellable event.
        movement.set(new Vec3(1, 0.2, 0));
        victim.needsSync = legacy;
        Field ticks = ServerEntity.class.getDeclaredField("tickCount");
        ticks.setAccessible(true);
        ticks.setInt(tracker, 1); // Isolate the native hurtMarked delivery boundary.
        PluginManager plugins = mock(PluginManager.class);
        doAnswer(call -> {
            PlayerVelocityEvent event = call.getArgument(0);
            assertEquals(new Vector(1, 0.2, 0), event.getVelocity());
            if (modify) event.setVelocity(new Vector(2, 0.3, 0));
            event.setCancelled(cancelled);
            return null;
        }).when(plugins).callEvent(any(PlayerVelocityEvent.class));
        try (MockedStatic<Bukkit> api = mockStatic(Bukkit.class)) {
            api.when(Bukkit::getPluginManager).thenReturn(plugins);
            tracker.sendChanges();
        }
        verify(plugins).callEvent(any(PlayerVelocityEvent.class));
        verify(network, never()).sendToTrackingPlayers(any(ClientboundSetEntityMotionPacket.class));
        if (cancelled) {
            verify(network, never()).sendToTrackingPlayersAndSelf(any(ClientboundSetEntityMotionPacket.class));
            if (legacy) assertFalse(victim.hurtMarked); else assertTrue(victim.hurtMarked);
        } else {
            ArgumentCaptor<ClientboundSetEntityMotionPacket> packet = ArgumentCaptor.forClass(ClientboundSetEntityMotionPacket.class);
            verify(network).sendToTrackingPlayersAndSelf(packet.capture());
            assertEquals(new Vec3(2, 0.3, 0), packet.getValue().movement());
            assertFalse(victim.hurtMarked);
        }
    }
}
