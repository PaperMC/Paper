package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Answers.RETURNS_DEFAULTS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.papermc.paper.configuration.WorldConfiguration;
import io.papermc.paper.event.entity.EntityKnockbackEvent;
import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.entity.vehicle.minecart.MinecartTNT;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.support.environment.VanillaFeature;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

@VanillaFeature
class LegacyPlayerKnockbackTest {

    private ServerPlayer victim(Vec3 velocity, boolean grounded) {
        ServerPlayer victim = mock(ServerPlayer.class, CALLS_REAL_METHODS);
        victim.legacyCombat = true;
        victim.hurtMarked = true;
        doReturn(velocity).when(victim).getDeltaMovement();
        doReturn(grounded).when(victim).onGround();
        doReturn(0.0).when(victim).getAttributeValue(any());
        doReturn(mock(CraftPlayer.class)).when(victim).getBukkitEntity();
        doNothing().when(victim).setDeltaMovement(any(Vec3.class));
        return victim;
    }

    private MockedStatic<CraftEventFactory> nativeEvents(boolean cancel) {
        MockedStatic<CraftEventFactory> factory = mockStatic(CraftEventFactory.class);
        factory.when(() -> CraftEventFactory.callEntityKnockbackEvent(any(), any(), any(), any(), anyDouble(), any()))
            .thenAnswer(call -> {
                Vec3 delta = call.getArgument(5);
                EntityKnockbackEvent event = mock(EntityKnockbackEvent.class);
                when(event.isCancelled()).thenReturn(cancel);
                when(event.getKnockback()).thenReturn(new Vector(delta.x, delta.y, delta.z));
                return event;
            });
        return factory;
    }

    @Test
    void unattributedEnvironmentalDamageDoesNotInventLegacyBaseKnockback() {
        ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
        try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
            victim.dealDefaultKnockback(mock(DamageSource.class), 1, false);
            events.verifyNoInteractions();
        }
        verify(victim, never()).setDeltaMovement(any(Vec3.class));
    }

    @Test
    void onlyOwnedClassicExplosionsOverrideNativeNoKnockbackTag() throws Exception {
        ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
        DamageSource source = mock(DamageSource.class);
        when(source.is(DamageTypeTags.NO_KNOCKBACK)).thenReturn(true);
        Method applies = LivingEntity.class.getDeclaredMethod("shouldApplyDefaultKnockback", DamageSource.class);
        applies.setAccessible(true);
        assertEquals(false, applies.invoke(victim, source));
        when(source.getEntity()).thenReturn(mock(Entity.class));
        when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(true);
        for (Entity direct : new Entity[]{mock(PrimedTnt.class),
                mock(Creeper.class),
                mock(WitherBoss.class)}) {
            when(source.getDirectEntity()).thenReturn(direct);
            assertEquals(true, applies.invoke(victim, source));
            when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(false);
            when(source.is(DamageTypes.EXPLOSION)).thenReturn(true);
            assertEquals(true, applies.invoke(victim, source));
            when(source.is(DamageTypes.EXPLOSION)).thenReturn(false);
            when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(true);
        }
        victim.legacyCombat = false;
        assertEquals(false, applies.invoke(victim, source));
        victim.legacyCombat = true;
        when(source.getEntity()).thenReturn(null);
        assertEquals(false, applies.invoke(victim, source));
        when(source.getEntity()).thenReturn(mock(Entity.class));
        when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(false);
        // An unrelated modern NO_KNOCKBACK source still retains its guard.
        assertEquals(false, applies.invoke(victim, source));
    }

    @Test
    void modernProjectileAndCrystalOwnersDoNotInventClassicExplosionBase() throws Exception {
        ServerPlayer victim = victim(Vec3.ZERO, false);
        DamageSource source = mock(DamageSource.class);
        Entity owner = mock(Player.class);
        when(source.getEntity()).thenReturn(owner);
        when(source.is(DamageTypeTags.NO_KNOCKBACK)).thenReturn(true);
        when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(true);
        Method applies = LivingEntity.class.getDeclaredMethod("shouldApplyDefaultKnockback", DamageSource.class);
        applies.setAccessible(true);
        for (Entity direct : new Entity[]{mock(EndCrystal.class),
                mock(LargeFireball.class),
                mock(WitherSkull.class),
                mock(MinecartTNT.class)}) {
            when(source.getDirectEntity()).thenReturn(direct);
            assertEquals(false, applies.invoke(victim, source));
            // The native damage source and owner remain intact for protection/event attribution.
            assertEquals(owner, source.getEntity());
            assertEquals(direct, source.getDirectEntity());
        }
        when(source.is(DamageTypeTags.NO_KNOCKBACK)).thenReturn(false);
        assertEquals(true, applies.invoke(victim, source)); // Native non-tagged behavior is unchanged.
    }

    @Test
    void ownedExplosionBaseDirectionUsesResponsibleOwnerInsteadOfTntPosition() {
        ServerPlayer victim = victim(Vec3.ZERO, false);
        doReturn(0.0).when(victim).getX();
        doReturn(0.0).when(victim).getZ();
        Entity owner = mock(Entity.class);
        when(owner.getX()).thenReturn(-5.0);
        DamageSource source = mock(DamageSource.class);
        when(source.getEntity()).thenReturn(owner);
        when(source.getSourcePosition()).thenReturn(new Vec3(10, 0, 0));
        when(source.getDirectEntity()).thenReturn(mock(PrimedTnt.class));
        try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
            victim.dealDefaultKnockback(source, 1, true);
        }
        ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
        verify(victim).setDeltaMovement(result.capture());
        assertEquals((double) 0.4F, result.getValue().x, 1.0E-12);
    }

    @Test
    void cancelledOwnedExplosionDamageNeverReachesBaseOrVelocityMutation() throws Exception {
        // Use the real ServerPlayer -> Player -> LivingEntity damage chain and
        // its actual cancelled EntityDamageEvent boundary, without world mutation.
        ServerPlayer victim = mock(ServerPlayer.class);
        victim.legacyCombat = true;
        ServerLevel level = mock(ServerLevel.class);
        DamageSource source = mock(DamageSource.class);
        Player owner = mock(Player.class);
        when(source.getEntity()).thenReturn(owner);
        when(source.is(DamageTypes.PLAYER_EXPLOSION)).thenReturn(true);
        when(source.is(DamageTypeTags.NO_KNOCKBACK)).thenReturn(true);
        when(source.getDirectEntity()).thenReturn(mock(PrimedTnt.class));
        when(victim.canHarmPlayer(owner)).thenReturn(true);
        when(victim.getHealth()).thenReturn(20.0F);
        when(victim.getUseItem()).thenReturn(ItemStack.EMPTY);
        Field abilities = Player.class.getDeclaredField("abilities");
        abilities.setAccessible(true);
        abilities.set(victim, new Abilities());
        doCallRealMethod().when(victim).hurtServer(level, source, 1.0F);
        doCallRealMethod().when((LivingEntity) victim).actuallyHurt(any(), any(), anyFloat(), any(EntityDamageEvent.class));
        EntityDamageEvent cancelled = mock(EntityDamageEvent.class);
        when(cancelled.isCancelled()).thenReturn(true);
        try (MockedStatic<CraftEventFactory> events = mockStatic(CraftEventFactory.class, call -> {
            if (call.getMethod().getName().equals("handleLivingEntityDamageEvent")) return cancelled;
            return RETURNS_DEFAULTS.answer(call);
        })) {
            assertEquals(false, victim.hurtServer(level, source, 1.0F));
        }
        verify(victim, never()).dealDefaultKnockback(any(), anyFloat(), anyBoolean());
        verify(victim, never()).setDeltaMovement(any(Vec3.class));
    }

    @Test
    void baseKnockbackHalvesEveryAxisInGroundAndAir() {
        DamageSource source = mock(DamageSource.class);
        for (boolean ground : new boolean[]{false, true}) {
            ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), ground);
            try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                victim.knockback(0.4F, 1, 0, source, 1, false, null, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
            }
            ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
            verify(victim).setDeltaMovement(result.capture());
            assertEquals(0.4 - (double) 0.4F, result.getValue().x, 1.0E-12);
            assertEquals((double) 0.4F, result.getValue().y, 1.0E-12);
            assertEquals(0.1, result.getValue().z, 1.0E-12);
        }
    }

    @Test
    void nonAxialBaseUsesLegacyFloatLengthAndComponentDivision() {
        for (boolean legacy : new boolean[]{false, true}) {
            ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
            victim.legacyCombat = legacy;
            try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                victim.knockback(0.4F, 1, 1, mock(DamageSource.class), 1, false, null, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
            }
            ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
            verify(victim).setDeltaMovement(result.capture());
            double length = legacy ? (double) (float) Math.sqrt(2) : Math.sqrt(2);
            assertEquals(0.4 - 1 / length * (double) 0.4F, result.getValue().x, 1.0E-15);
            assertEquals(0.1 - 1 / length * (double) 0.4F, result.getValue().z, 1.0E-15);
        }
    }

    @Test
    void unmarkedPlayerKeepsNativeAirborneVerticalMotion() {
        ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
        victim.legacyCombat = false;
        try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
            victim.knockback(0.4F, 1, 0, mock(DamageSource.class), 1, false, null, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
        }
        ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
        verify(victim).setDeltaMovement(result.capture());
        assertEquals(0.6, result.getValue().y);
    }

    @Test
    void shieldBlockKeepsNativeAirborneKnockbackWithOrWithoutProfile() {
        for (boolean legacy : new boolean[]{false, true}) {
            ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
            victim.legacyCombat = legacy;
            try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                victim.knockback(0.5, 1, 0, mock(DamageSource.class), 1, false, mock(Player.class), EntityKnockbackEvent.Cause.SHIELD_BLOCK);
            }
            ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
            verify(victim).setDeltaMovement(result.capture());
            assertEquals(0.4 - 0.5, result.getValue().x, 1.0E-12);
            assertEquals(0.6, result.getValue().y, 1.0E-12);
            assertEquals(0.1, result.getValue().z, 1.0E-12);
        }
    }

    @Test
    void modernDamageSourcesRetainNativeBaseBonusAndImmediateDelivery() {
        for (var type : List.of(DamageTypes.SPEAR, DamageTypes.MACE_SMASH, DamageTypes.TRIDENT,
            DamageTypes.WIND_CHARGE, DamageTypes.FIREWORKS, DamageTypes.SONIC_BOOM)) {
            DamageSource source = mock(DamageSource.class);
            when(source.is(type)).thenReturn(true);
            Player attacker = mock(Player.class);
            attacker.legacyCombat = true;
            for (boolean legacy : new boolean[]{false, true}) {
                for (boolean effect : new boolean[]{false, true}) {
                    ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
                    victim.legacyCombat = legacy;
                    try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                        victim.knockback(0.5, 1, 0, source, 1, effect, attacker, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
                    }
                    ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
                    verify(victim).setDeltaMovement(result.capture());
                    assertEquals(0.4 - 0.5, result.getValue().x, 1.0E-12);
                    assertEquals(0.6, result.getValue().y, 1.0E-12);
                    assertEquals(0.1, result.getValue().z, 1.0E-12);
                }
                ServerPlayer delivered = victim(new Vec3(0.8, 0.6, 0.2), false);
                delivered.legacyCombat = legacy;
                doReturn(42).when(delivered).getId();
                delivered.connection = mock(ServerGamePacketListenerImpl.class);
                ServerLevel level = mock(ServerLevel.class);
                WorldConfiguration config = mock(WorldConfiguration.class);
                config.misc = config.new Misc();
                when(level.paperConfig()).thenReturn(config);
                CraftServer server = mock(CraftServer.class);
                when(server.getPluginManager()).thenReturn(mock(PluginManager.class));
                when(level.getCraftServer()).thenReturn(server);
                when(attacker.level()).thenReturn(level);
                when(attacker.getDeltaMovement()).thenReturn(Vec3.ZERO);
                Vec3 old = new Vec3(0.1, 0.2, 0.3);
                doCallRealMethod().when(attacker).causeExtraKnockback(delivered, 0.5F, old, source, 1, true);
                try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                    attacker.causeExtraKnockback(delivered, 0.5F, old, source, 1, true);
                }
                verify(delivered.connection).send(any(ClientboundSetEntityMotionPacket.class));
                verify(delivered).setDeltaMovement(old);
                assertEquals(false, delivered.hurtMarked);
            }
        }
    }

    @Test
    void modernDamageSourcesKeepNativeProjectileDirectionWithProfileEnabled() {
        for (var type : List.of(DamageTypes.SPEAR, DamageTypes.MACE_SMASH, DamageTypes.TRIDENT,
            DamageTypes.WIND_CHARGE, DamageTypes.FIREWORKS, DamageTypes.SONIC_BOOM)) {
            for (boolean legacy : new boolean[]{false, true}) {
                ServerPlayer victim = victim(Vec3.ZERO, false);
                victim.legacyCombat = legacy;
                doReturn(0.0).when(victim).getX();
                doReturn(0.0).when(victim).getZ();
                doNothing().when(victim).indicateDamage(anyDouble(), anyDouble());
                Entity owner = mock(Entity.class);
                when(owner.getX()).thenReturn(5.0);
                Arrow direct = mock(Arrow.class);
                when(direct.calculateHorizontalHurtKnockbackDirection(any(), any())).thenReturn(
                    DoubleDoubleImmutablePair.of(2, 0));
                DamageSource source = mock(DamageSource.class);
                when(source.is(type)).thenReturn(true);
                when(source.getDirectEntity()).thenReturn(direct);
                when(source.getEntity()).thenReturn(owner);
                try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                    victim.dealDefaultKnockback(source, 1, true);
                }
                ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
                verify(victim).setDeltaMovement(result.capture());
                assertEquals((double) 0.4F, result.getValue().x, 1.0E-12);
                assertEquals(0.0, result.getValue().y, 1.0E-12);
            }
        }
    }

    @Test
    void sprintAndEnchantmentBonusesAddOnceWithoutSecondFrictionOrImmediateReset() {
        // 0.5 = sprint or Knockback I; 1.0 = Knockback II; 1.5 = sprint + Knockback II.
        for (float bonus : new float[]{0.5F, 1.0F, 1.5F}) {
            ServerPlayer victim = victim(new Vec3(0.3, 0.4, -0.2), false);
            Player attacker = mock(Player.class, CALLS_REAL_METHODS);
            attacker.legacyCombat = true;
            doReturn(0.0F).when(attacker).getYRot();
            doReturn(new Vec3(1, 0.2, 0.3)).when(attacker).getDeltaMovement();
            doNothing().when(attacker).setDeltaMovement(any(Vec3.class));
            doNothing().when(attacker).setSprinting(false);
            ServerLevel level = mock(ServerLevel.class);
            WorldConfiguration config = mock(WorldConfiguration.class);
            config.misc = config.new Misc();
            when(level.paperConfig()).thenReturn(config);
            doReturn(level).when(attacker).level();
            Vec3 preDamage = new Vec3(0.8, 0.6, 0.2);
            try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
                attacker.causeExtraKnockback(victim, bonus, preDamage, mock(DamageSource.class), 1, true);
            }
            ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
            verify(victim).setDeltaMovement(result.capture());
            assertEquals(0.3, result.getValue().x, 1.0E-12);
            assertEquals(0.5, result.getValue().y, 1.0E-12);
            assertEquals(-0.2 + bonus, result.getValue().z, 1.0E-12);
            verify(victim, never()).setDeltaMovement(preDamage);
            assertTrue(victim.hurtMarked); // The tracker still owns delivery.
        }
    }

    @Test
    void acceptedImmuneWindowAttackCanAddBonusWithoutReapplyingBase() {
        ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
        Player attacker = mock(Player.class);
        attacker.legacyCombat = true;
        try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
            // Native hurtOrSimulate controls whether the base stage runs. A stronger
            // immune-window hit reaches the bonus stage with its existing motion.
            victim.knockback(0.5F, 0, -1, mock(DamageSource.class), 1, true, attacker, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
        }
        ArgumentCaptor<Vec3> result = ArgumentCaptor.forClass(Vec3.class);
        verify(victim).setDeltaMovement(result.capture());
        assertEquals(new Vec3(0.8, 0.7, 0.7), result.getValue());
    }

    @Test
    void cancelledBonusAndNativeResistanceAttributeRemainEffective() {
        ServerPlayer victim = victim(new Vec3(0.8, 0.6, 0.2), false);
        doReturn(0.25).when(victim).getAttributeValue(any());
        Player attacker = mock(Player.class);
        attacker.legacyCombat = true;
        try (MockedStatic<CraftEventFactory> events = nativeEvents(true)) {
            victim.knockback(0.5F, 0, -1, mock(DamageSource.class), 1, true, attacker, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
            events.verify(() -> CraftEventFactory.callEntityKnockbackEvent(any(), any(), any(), any(), org.mockito.ArgumentMatchers.eq(0.375), any()));
        }
        verify(victim, never()).setDeltaMovement(any(Vec3.class));
    }

    @Test
    void completeResistanceDoesNotGainAnUnscaledVerticalBonus() {
        Vec3 before = new Vec3(0.8, 0.6, 0.2);
        ServerPlayer victim = victim(before, false);
        doReturn(1.0).when(victim).getAttributeValue(any());
        Player attacker = mock(Player.class);
        attacker.legacyCombat = true;
        try (MockedStatic<CraftEventFactory> events = nativeEvents(false)) {
            victim.knockback(0.5F, 0, -1, mock(DamageSource.class), 1, true, attacker, EntityKnockbackEvent.Cause.ENTITY_ATTACK);
        }
        verify(victim).setDeltaMovement(before);
    }

    @Test
    void noBonusAttackLeavesVelocityForTrackerInsteadOfRestoringPreDamageMotion() {
        ServerPlayer victim = victim(new Vec3(0.3, 0.4, -0.2), false);
        Player attacker = mock(Player.class, CALLS_REAL_METHODS);
        attacker.causeExtraKnockback(victim, 0, new Vec3(0.8, 0.6, 0.2), mock(DamageSource.class), 1, true);
        verify(victim, never()).setDeltaMovement(any(Vec3.class));
        assertTrue(victim.hurtMarked);
    }
    @Test
    void nativeZeroImpactsAcceptOnlyOptedInFamiliesWithoutChipDamageAndRespectDenials() throws Exception {
        for (boolean rod : new boolean[]{false, true}) {
            for (boolean flagged : new boolean[]{false, true}) {
                for (boolean cancelled : new boolean[]{false, true}) {
                    for (boolean immune : new boolean[]{false, true}) {
                        ServerPlayer victim = mock(ServerPlayer.class);
                        victim.legacyCombat = true;
                        victim.invulnerableDuration = 20;
                        when(victim.getAdvancements()).thenReturn(mock(net.minecraft.server.PlayerAdvancements.class));
                        victim.invulnerableTime = immune ? 11 : 0;
                        ServerLevel level = mock(ServerLevel.class);
                        when(victim.level()).thenReturn(level);
                        when(victim.getHealth()).thenReturn(20.0F);
                        when(victim.getUseItem()).thenReturn(ItemStack.EMPTY);
                        Field abilities = Player.class.getDeclaredField("abilities");
                        abilities.setAccessible(true);
                        abilities.set(victim, new Abilities());
                        Player owner = mock(Player.class);
                        when(victim.canHarmPlayer(owner)).thenReturn(true);
                        net.minecraft.world.entity.projectile.Projectile direct;
                        if (rod) {
                            var hook = mock(net.minecraft.world.entity.projectile.FishingHook.class);
                            hook.legacyFlight = flagged;
                            direct = hook;
                        } else {
                            var thrown = mock(net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball.class);
                            thrown.legacyFlight = flagged;
                            direct = thrown;
                        }
                        DamageSource source = mock(DamageSource.class);
                        when(source.getEntity()).thenReturn(owner);
                        when(source.getDirectEntity()).thenReturn(direct);
                        doCallRealMethod().when(victim).hurtServer(level, source, 0F);
                        doCallRealMethod().when((LivingEntity)victim).actuallyHurt(any(), any(), anyFloat(), any(EntityDamageEvent.class));
                        EntityDamageEvent event = mock(EntityDamageEvent.class);
                        when(event.isCancelled()).thenReturn(cancelled);
                        try (var events = mockStatic(CraftEventFactory.class, call -> {
                            if (call.getMethod().getName().equals("handleLivingEntityDamageEvent")) return event;
                            return RETURNS_DEFAULTS.answer(call);
                        })) {
                            assertEquals(flagged && !cancelled && !immune, victim.hurtServer(level, source, 0F));
                        }
                        verify(victim, never()).setHealth(anyFloat());
                        if (flagged && !cancelled && !immune) {
                            verify(victim).dealDefaultKnockback(source, 0F, false);
                            assertEquals(20, victim.invulnerableTime);
                        } else verify(victim, never()).dealDefaultKnockback(any(), anyFloat(), anyBoolean());
                    }
                }
            }
        }
    }

}
