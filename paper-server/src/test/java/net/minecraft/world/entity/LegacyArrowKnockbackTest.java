package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.papermc.paper.event.entity.EntityKnockbackEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.support.environment.VanillaFeature;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

@VanillaFeature
class LegacyArrowKnockbackTest {

    @Test
    void selfArrowUsesLegacyRandomBaseAndAirborneVerticalHalving() {
        LivingEntity victim = mock(LivingEntity.class, CALLS_REAL_METHODS);
        doReturn(0.0).when(victim).getAttributeValue(any());
        doReturn(0.0).when(victim).getX();
        doReturn(0.0).when(victim).getZ();
        Arrow arrow = mock(Arrow.class);
        arrow.legacyFlight = true;
        when(arrow.getOwner()).thenReturn(victim);
        when(arrow.calculateHorizontalHurtKnockbackDirection(any(), any())).thenReturn(
            it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair.of(2, 0));
        DamageSource source = mock(DamageSource.class);
        when(source.getDirectEntity()).thenReturn(arrow);
        doReturn(new Vec3(1, -0.6, 0)).when(victim).getDeltaMovement();
        doReturn(mock(CraftLivingEntity.class)).when(victim).getBukkitEntity();
        doNothing().when(victim).setDeltaMovement(any(Vec3.class));
        doNothing().when(victim).indicateDamage(anyDouble(), anyDouble());
        try (MockedStatic<CraftEventFactory> factory = mockStatic(CraftEventFactory.class)) {
            factory.when(() -> CraftEventFactory.callEntityKnockbackEvent(any(), any(), any(), any(), anyDouble(), any()))
                .thenAnswer(call -> {
                    Vec3 outgoing = call.getArgument(5);
                    // Base strength stays 0.4, but direction is randomized for a self-hit.
                    assertEquals((double) 0.4F, Math.hypot(outgoing.x + 0.5, outgoing.z), 1.0E-7);
                    EntityKnockbackEvent event = mock(EntityKnockbackEvent.class);
                    when(event.getKnockback()).thenReturn(new Vector(outgoing.x, outgoing.y, outgoing.z));
                    return event;
                });
            victim.dealDefaultKnockback(source, 1, false);
        }
        ArgumentCaptor<Vec3> output = ArgumentCaptor.forClass(Vec3.class);
        verify(victim).setDeltaMovement(output.capture());
        assertEquals(-0.6 / 2 + (double) 0.4F, output.getValue().y, 1.0E-12);
        assertTrue(victim.needsSync);
    }

    @Test
    void cancelledNativeKnockbackProducesNoVelocityMutation() {
        LivingEntity victim = mock(LivingEntity.class, CALLS_REAL_METHODS);
        doReturn(0.0).when(victim).getAttributeValue(any());
        doReturn(0.0).when(victim).getX();
        doReturn(0.0).when(victim).getZ();
        Arrow arrow = mock(Arrow.class);
        arrow.legacyFlight = true;
        DamageSource source = mock(DamageSource.class);
        when(source.getDirectEntity()).thenReturn(arrow);
        doReturn(new Vec3(1, 0.6, 0)).when(victim).getDeltaMovement();
        doReturn(mock(CraftLivingEntity.class)).when(victim).getBukkitEntity();
        try (MockedStatic<CraftEventFactory> factory = mockStatic(CraftEventFactory.class)) {
            EntityKnockbackEvent cancelled = mock(EntityKnockbackEvent.class);
            when(cancelled.isCancelled()).thenReturn(true);
            factory.when(() -> CraftEventFactory.callEntityKnockbackEvent(any(), any(), any(), any(), anyDouble(), any()))
                .thenReturn(cancelled);
            victim.knockback(0.4F, 1, 0, source, 1, false, arrow, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
        }
        verify(victim, never()).setDeltaMovement(any(Vec3.class));
    }
    @Test
    void markedPunchUsesFloatNormAndFloatFactorForNonAxialFlight() throws Exception {
        Vec3 flight = new Vec3(0.7, 0.4, 0.23);
        float length = (float) Math.sqrt(flight.x * flight.x + flight.z * flight.z);
        assertPunch(true, flight, 0.25,
            new Vec3(flight.x * 2.0 * (double) 0.6F / length * 0.75, 0.1,
                flight.z * 2.0 * (double) 0.6F / length * 0.75));
    }

    @Test
    void markedNearVerticalPunchKeepsPositiveHorizontalDirection() throws Exception {
        assertPunch(true, new Vec3(1.0E-6, 1, 0), 0,
            new Vec3(1.0E-6 * 2.0 * (double) 0.6F / (double) (float) 1.0E-6, 0.1, 0));
    }

    @Test
    void unmarkedPunchRetainsModernNormAndEpsilon() throws Exception {
        Vec3 flight = new Vec3(0.7, 0.4, 0.23);
        Vec3 nativeHorizontal = flight.multiply(1, 0, 1).normalize().scale(2.0 * 0.6 * 0.75);
        assertPunch(false, flight, 0.25, new Vec3(nativeHorizontal.x, 0.1, nativeHorizontal.z));
        assertPunch(false, new Vec3(1.0E-6, 1, 0), 0, null);
    }

    @Test
    void zeroDirectionAndFullArmourResistanceDoNotPush() throws Exception {
        assertPunch(true, new Vec3(0, 1, 0), 0, null);
        assertPunch(true, new Vec3(1, 1, 0), 1, null);
        assertPunch(false, new Vec3(0, 1, 0), 0, null);
    }

    private static void assertPunch(boolean legacy, Vec3 flight, double resistance, Vec3 expected) throws Exception {
        Arrow arrow = mock(Arrow.class, CALLS_REAL_METHODS);
        arrow.legacyFlight = legacy;
        arrow.firedFromWeapon = mock(ItemStack.class);
        ServerLevel level = mock(ServerLevel.class);
        doReturn(level).when(arrow).level();
        doReturn(flight).when(arrow).getDeltaMovement();
        LivingEntity victim = mock(LivingEntity.class);
        when(victim.getAttributeValue(any())).thenReturn(resistance);
        DamageSource source = mock(DamageSource.class);
        try (MockedStatic<EnchantmentHelper> enchantments = mockStatic(EnchantmentHelper.class)) {
            enchantments.when(() -> EnchantmentHelper.modifyKnockback(level, arrow.firedFromWeapon, victim, source, 0.0F))
                .thenReturn(2.0F);
            var method = AbstractArrow.class.getDeclaredMethod("doKnockback", LivingEntity.class, DamageSource.class);
            method.setAccessible(true);
            method.invoke(arrow, victim, source);
        }
        if (expected == null) {
            verify(victim, never()).push(anyDouble(), anyDouble(), anyDouble(), any(Entity.class));
        } else {
            ArgumentCaptor<Double> x = ArgumentCaptor.forClass(Double.class);
            ArgumentCaptor<Double> y = ArgumentCaptor.forClass(Double.class);
            ArgumentCaptor<Double> z = ArgumentCaptor.forClass(Double.class);
            verify(victim).push(x.capture(), y.capture(), z.capture(), org.mockito.ArgumentMatchers.same(arrow));
            assertEquals(expected.x, x.getValue(), 0.0);
            assertEquals(expected.y, y.getValue(), 0.0);
            assertEquals(expected.z, z.getValue(), 0.0);
        }
    }

}
