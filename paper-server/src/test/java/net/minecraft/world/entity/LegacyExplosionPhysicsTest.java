package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyExplosionPhysicsTest {
    private ServerPlayer player(double distanceSqr) {
        ServerPlayer player = mock(ServerPlayer.class);
        player.legacyCombat = true;
        when(player.distanceToSqr(Vec3.ZERO)).thenReturn(distanceSqr);
        when(player.getEyePosition()).thenReturn(new Vec3(0, 1.27, 0));
        return player;
    }

    private ServerExplosion explosion(Entity source) {
        ServerExplosion explosion = mock(ServerExplosion.class);
        when(explosion.radius()).thenReturn(4.0F);
        when(explosion.center()).thenReturn(Vec3.ZERO);
        when(explosion.getDirectSourceEntity()).thenReturn(source);
        when(explosion.getDamageSource()).thenReturn(mock(DamageSource.class));
        return explosion;
    }

    @Test
    void classicDamageUsesIntegerFactorEightWithFloatDistance() {
        ExplosionDamageCalculator calculator = new EntityBasedExplosionDamageCalculator(mock(PrimedTnt.class));
        ServerExplosion explosion = explosion(mock(PrimedTnt.class));
        ServerPlayer player = player(0);
        assertEquals(65.0F, calculator.getEntityDamageAmount(explosion, player, 1));
        when(player.distanceToSqr(Vec3.ZERO)).thenReturn(16.0);
        assertEquals(25.0F, calculator.getEntityDamageAmount(explosion, player, 1));
        assertEquals(11.0F, calculator.getEntityDamageAmount(explosion, player, 0.5F));
        when(player.distanceToSqr(Vec3.ZERO)).thenReturn(2.0);
        double distance = (double) (float) Math.sqrt(2.0);
        assertEquals(distance, calculator.getEntityDistance(explosion, player));
        double power = (1 - distance / 8) * 0.37F;
        assertEquals((float) (int) ((power * power + power) / 2 * 8 * 8 + 1), calculator.getEntityDamageAmount(explosion, player, 0.37F));
    }

    @Test
    void witherSpawnBlastIsClassicAndOptOutRetainsNativeCurve() {
        Entity wither = mock(WitherBoss.class);
        ExplosionDamageCalculator calculator = new EntityBasedExplosionDamageCalculator(wither);
        ServerExplosion explosion = explosion(wither);
        when(explosion.radius()).thenReturn(7.0F);
        ServerPlayer player = player(0);
        assertTrue(calculator.usesLegacyPhysics(explosion, player));
        assertEquals(113.0F, calculator.getEntityDamageAmount(explosion, player, 1));
        player.legacyCombat = false;
        assertFalse(calculator.usesLegacyPhysics(explosion, player));
        assertEquals(99.0F, calculator.getEntityDamageAmount(explosion, player, 1));
    }

    @Test
    void optOutAndCustomCalculatorsKeepModernDamageAndDirection() {
        ServerExplosion explosion = explosion(mock(PrimedTnt.class));
        ServerPlayer player = player(0);
        player.legacyCombat = false;
        ExplosionDamageCalculator calculator = new ExplosionDamageCalculator();
        assertEquals(57.0F, calculator.getEntityDamageAmount(explosion, player, 1));
        assertEquals(new Vec3(0, 1, 0), calculator.getEntityKnockbackDirection(explosion, player));
        player.legacyCombat = true;
        ExplosionDamageCalculator custom = new ExplosionDamageCalculator() {};
        assertFalse(custom.usesLegacyPhysics(explosion, player));
        assertEquals(57.0F, custom.getEntityDamageAmount(explosion, player, 1));
        ExplosionDamageCalculator wind = new SimpleExplosionDamageCalculator(false, false, Optional.empty(), Optional.empty());
        assertFalse(wind.usesLegacyPhysics(explosion, player));
        assertEquals(57.0F, wind.getEntityDamageAmount(explosion, player, 1));
        when(explosion.getDirectSourceEntity()).thenReturn(mock(Entity.class));
        assertFalse(calculator.usesLegacyPhysics(explosion, player));
    }

    @Test
    void nativeBedIsClassicButAnonymousAnchorCalculatorRemainsModern() {
        ServerExplosion explosion = explosion(null);
        when(explosion.getDamageSource().is(DamageTypes.BAD_RESPAWN_POINT)).thenReturn(true);
        ServerPlayer player = player(0);
        ExplosionDamageCalculator bed = new ExplosionDamageCalculator();
        assertTrue(bed.usesLegacyPhysics(explosion, player));
        assertEquals(65.0F, bed.getEntityDamageAmount(explosion, player, 1));
        ExplosionDamageCalculator anchor = new ExplosionDamageCalculator() {};
        assertFalse(anchor.usesLegacyPhysics(explosion, player));
        assertEquals(57.0F, anchor.getEntityDamageAmount(explosion, player, 1));
        when(explosion.getDamageSource().is(DamageTypes.BAD_RESPAWN_POINT)).thenReturn(false);
        assertFalse(bed.usesLegacyPhysics(explosion, player));
    }

    @Test
    void classicDirectionUsesStandingEyeAndFloatSquareRootEvenWhenCrouching() {
        ServerExplosion explosion = explosion(mock(PrimedTnt.class));
        ServerPlayer player = player(1);
        when(player.getX()).thenReturn(1.0);
        ExplosionDamageCalculator calculator = new ExplosionDamageCalculator();
        Vec3 expected = new Vec3(1, (double) 1.62F, 0);
        double length = (double) (float) Math.sqrt(expected.lengthSqr());
        expected = new Vec3(expected.x / length, expected.y / length, expected.z / length);
        assertEquals(expected, calculator.getEntityKnockbackDirection(explosion, player));
        when(player.getX()).thenReturn(0.0);
        when(player.getY()).thenReturn(-(double) 1.62F);
        assertEquals(Vec3.ZERO, calculator.getEntityKnockbackDirection(explosion, player));
    }
}
