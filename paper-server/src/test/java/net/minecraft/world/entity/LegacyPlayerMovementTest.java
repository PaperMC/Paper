package net.minecraft.world.entity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyPlayerMovementTest {
    private ServerPlayer player(boolean legacy, Vec3 velocity) {
        ServerPlayer player = mock(ServerPlayer.class);
        player.legacyCombat = legacy;
        when(player.getDeltaMovement()).thenReturn(velocity);
        return player;
    }

    @Test
    void nativeJumpReplacesVerticalMotionOnlyForOptedInPlayer() {
        Player legacy = mock(Player.class);
        legacy.legacyCombat = true;
        when(legacy.getDeltaMovement()).thenReturn(new Vec3(0.2, 0.8, 0.3));
        when(legacy.getJumpPower()).thenReturn(0.42F);
        // Invoke LivingEntity's actual implementation without ServerPlayer's event wrapper.
        LivingEntity living = legacy;
        doCallRealMethod().when(living).jumpFromGround();
        living.jumpFromGround();
        verify(legacy).setDeltaMovement(0.2, (double) 0.42F, 0.3);
        Player modern = mock(Player.class);
        when(modern.getDeltaMovement()).thenReturn(new Vec3(0.2, 0.8, 0.3));
        when(modern.getJumpPower()).thenReturn(0.42F);
        doCallRealMethod().when((LivingEntity) modern).jumpFromGround();
        modern.jumpFromGround();
        verify(modern).setDeltaMovement(0.2, 0.8, 0.3);
    }

    @Test
    void nonAxialSprintJumpUsesFloatProductOnlyWhenOptedIn() {
        for (boolean legacy : new boolean[]{true, false}) {
            Player player = mock(Player.class);
            player.legacyCombat = legacy;
            when(player.getDeltaMovement()).thenReturn(Vec3.ZERO);
            when(player.getJumpPower()).thenReturn(0.42F);
            when(player.isSprinting()).thenReturn(true);
            when(player.getYRot()).thenReturn(37.0F);
            doCallRealMethod().when((LivingEntity) player).jumpFromGround();
            player.jumpFromGround();
            float angle = 37.0F * net.minecraft.util.Mth.DEG_TO_RAD;
            double x = legacy ? (double)(-net.minecraft.util.Mth.sin(angle) * 0.2F) : -net.minecraft.util.Mth.sin(angle) * 0.2;
            double z = legacy ? (double)(net.minecraft.util.Mth.cos(angle) * 0.2F) : net.minecraft.util.Mth.cos(angle) * 0.2;
            verify(player).addDeltaMovement(new Vec3(x, 0, z));
        }
    }

    @Test
    void classicCadenceIgnoresFatigueSpeedButKeepsModernWeapons() {
        for (net.minecraft.world.item.Item item : new net.minecraft.world.item.Item[]{
            net.minecraft.world.item.Items.AIR, net.minecraft.world.item.Items.DIAMOND_SWORD,
            net.minecraft.world.item.Items.DIAMOND_AXE, net.minecraft.world.item.Items.BOW,
            net.minecraft.world.item.Items.MACE, net.minecraft.world.item.Items.TRIDENT,
            net.minecraft.world.item.Items.WOODEN_SPEAR, net.minecraft.world.item.Items.DIAMOND_SPEAR
        }) {
            for (boolean legacy : new boolean[]{true, false}) {
                Player player = mock(Player.class);
                player.legacyCombat = legacy;
                when(player.getMainHandItem()).thenReturn(new net.minecraft.world.item.ItemStack(item));
                // High attack-speed fixture with native Mining Fatigue I (-10%).
                when(player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_SPEED)).thenReturn(39.6);
                doCallRealMethod().when(player).getCurrentItemAttackStrengthDelay();
                doCallRealMethod().when(player).getAttackStrengthScale(anyFloat());
                boolean modern = item == net.minecraft.world.item.Items.MACE || item == net.minecraft.world.item.Items.TRIDENT
                    || item == net.minecraft.world.item.Items.WOODEN_SPEAR || item == net.minecraft.world.item.Items.DIAMOND_SPEAR;
                float nativeScale = net.minecraft.util.Mth.clamp(0.5F / (float)(20.0 / 39.6), 0, 1);
                assertEquals(legacy && !modern ? 1.0F : nativeScale, player.getAttackStrengthScale(0.5F), item + " legacy=" + legacy);
                verify(player, never()).resetAttackStrengthTicker();
            }
        }
    }

    @Test
    void componentDefinedModernWeaponKeepsNativeCadence() {
        for (boolean kinetic : new boolean[]{true, false}) {
            net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.DIAMOND_SWORD);
            net.minecraft.world.item.ItemStack spear = new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.WOODEN_SPEAR);
            if (kinetic) stack.set(net.minecraft.core.component.DataComponents.KINETIC_WEAPON, spear.get(net.minecraft.core.component.DataComponents.KINETIC_WEAPON));
            else stack.set(net.minecraft.core.component.DataComponents.PIERCING_WEAPON, spear.get(net.minecraft.core.component.DataComponents.PIERCING_WEAPON));
            Player player = mock(Player.class);
            player.legacyCombat = true;
            when(player.getMainHandItem()).thenReturn(stack);
            when(player.getCurrentItemAttackStrengthDelay()).thenReturn(5.0F);
            doCallRealMethod().when(player).getAttackStrengthScale(anyFloat());
            assertEquals(0.1F, player.getAttackStrengthScale(0.5F));
        }
    }

    private void aiStepClip(ServerPlayer player) throws Exception {
        Field inventory = Player.class.getDeclaredField("inventory");
        inventory.setAccessible(true);
        inventory.set(player, mock(Inventory.class));
        Field abilities = Player.class.getDeclaredField("abilities");
        abilities.setAccessible(true);
        abilities.set(player, new Abilities());
        Field equipment = LivingEntity.class.getDeclaredField("equipment");
        equipment.setAccessible(true);
        equipment.set(player, mock(EntityEquipment.class));
        when(player.canSimulateMovement()).thenReturn(true);
        class StopAfterClip extends RuntimeException {}
        doThrow(new StopAfterClip()).when((LivingEntity) player).applyInput();
        doCallRealMethod().when(player).aiStep();
        assertThrows(StopAfterClip.class, player::aiStep);
    }

    @Test
    void actualAiStepClipsEachLegacyAxisAtPointZeroZeroFive() throws Exception {
        ServerPlayer legacy = player(true, new Vec3(0.0049, -0.0049, 0.005));
        aiStepClip(legacy);
        verify(legacy).setDeltaMovement(0, 0, 0.005);
        ServerPlayer modern = player(false, new Vec3(0.0049, -0.0049, 0.005));
        aiStepClip(modern);
        verify(modern).setDeltaMovement(0.0049, -0.0049, 0.005);
    }

    @Test
    void actualAiStepRetainsModernCombinedHorizontalThresholdWhenDisabled() throws Exception {
        ServerPlayer modern = player(false, new Vec3(0.0025, 0.003, 0.0025));
        when(modern.is(net.minecraft.world.entity.EntityTypes.PLAYER)).thenReturn(true);
        aiStepClip(modern);
        verify(modern).setDeltaMovement(0.0025, 0.003, 0.0025);
        ServerPlayer legacy = player(true, new Vec3(0.0025, 0.003, 0.0025));
        aiStepClip(legacy);
        verify(legacy).setDeltaMovement(0, 0, 0);
    }
    private Player fallPlayer(boolean legacy) {
        Player player = mock(Player.class);
        player.legacyCombat = legacy;
        when(player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.SAFE_FALL_DISTANCE)).thenReturn(3.0);
        when(player.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.FALL_DAMAGE_MULTIPLIER)).thenReturn(1.0);
        doCallRealMethod().when((LivingEntity)player).calculateFallDamage(anyDouble(), anyFloat());
        return player;
    }

    @Test
    void ordinaryLegacyFallsUseFloatInputAndCeilingWithNativeAttributes() {
        Player legacy = fallPlayer(true);
        assertEquals(0, legacy.calculateFallDamage(3, 1));
        assertEquals(0, legacy.calculateFallDamage(3.00000001, 1));
        assertEquals(1, legacy.calculateFallDamage(3.5, 1));
        assertEquals(2, legacy.calculateFallDamage(4.5, 1));
        when(legacy.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.SAFE_FALL_DISTANCE)).thenReturn(4.0);
        assertEquals(1, legacy.calculateFallDamage(4.5, 1)); // Native Jump Boost safe-distance attribute.
        when(legacy.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.FALL_DAMAGE_MULTIPLIER)).thenReturn(0.5);
        assertEquals(1, legacy.calculateFallDamage(5.5, 1));
        Player modern = fallPlayer(false);
        assertEquals(0, modern.calculateFallDamage(3.5, 1));
        assertEquals(1, modern.calculateFallDamage(4.5, 1));
        when(legacy.isIgnoringFallDamageFromCurrentImpulse()).thenReturn(true);
        assertEquals(0, legacy.calculateFallDamage(4.5, 1));
    }

    @Test
    void cappedModernImpulseLandingKeepsFloorEvenWhenContextExpiresDuringLanding() throws Exception {
        Player player = fallPlayer(true);
        Field abilities = Player.class.getDeclaredField("abilities");
        abilities.setAccessible(true);
        abilities.set(player, new Abilities());
        player.currentImpulseImpactPos = new Vec3(0, 3.5, 0);
        doCallRealMethod().when(player).isIgnoringFallDamageFromCurrentImpulse();
        doCallRealMethod().when(player).tryResetCurrentImpulseContext();
        doCallRealMethod().when(player).resetCurrentImpulseContext();
        doCallRealMethod().when(player).causeFallDamage(anyDouble(), anyFloat(), any());
        assertFalse(player.causeFallDamage(10, 1, mock(net.minecraft.world.damagesource.DamageSource.class)));
        assertNull(player.currentImpulseImpactPos);
        verify(player, never()).hurtServer(any(), any(), anyFloat());
    }

    @Test
    void modernLungeKeepsDirectForwardSelfImpulseAndNativeGrace() {
        var impulse = new net.minecraft.world.item.enchantment.effects.ApplyEntityImpulse(
            new Vec3(0, 0, 1), new Vec3(1, 0, 1), new net.minecraft.world.item.enchantment.LevelBasedValue.Constant(0.458F));
        for (boolean legacy : new boolean[]{false, true}) {
            Player player = mock(Player.class);
            player.legacyCombat = legacy;
            when(player.getLookAngle()).thenReturn(new Vec3(0, 0, 1));
            impulse.apply(mock(net.minecraft.server.level.ServerLevel.class), 1, null, player, Vec3.ZERO);
            var delivered = org.mockito.ArgumentCaptor.forClass(Vec3.class);
            verify(player).addDeltaMovement(delivered.capture());
            assertEquals(0, delivered.getValue().x, 1.0E-7);
            assertEquals(0, delivered.getValue().y, 0.0);
            assertEquals((double)0.458F, delivered.getValue().z, 1.0E-7);
            assertTrue(player.hurtMarked);
            assertTrue(player.needsSync);
            verify(player).applyPostImpulseGraceTime(10);
        }
    }

    @Test
    void classicFireOnsetKeepsSoulFireNativeResistanceAndCombustionArmourControls() throws Exception {
        for (boolean legacy : new boolean[]{false, true}) {
            for (boolean cancel : new boolean[]{false, true}) {
                for (double burningTime : new double[]{1.0, 0.5}) {
                    for (net.minecraft.world.level.block.Block block : new net.minecraft.world.level.block.Block[]{
                        net.minecraft.world.level.block.Blocks.FIRE, net.minecraft.world.level.block.Blocks.SOUL_FIRE
                    }) {
                        var player = mock(ServerPlayer.class, CALLS_REAL_METHODS);
                        player.legacyCombat = legacy;
                        Field fireAbilities = Player.class.getDeclaredField("abilities");
                        fireAbilities.setAccessible(true);
                        fireAbilities.set(player, new Abilities());
                        var level = mock(net.minecraft.server.level.ServerLevel.class);
                        doReturn(level).when(player).level();
                        when(level.getBlockState(net.minecraft.core.BlockPos.ZERO)).thenReturn(block.defaultBlockState());
                        doReturn(mock(org.bukkit.craftbukkit.entity.CraftPlayer.class)).when(player).getBukkitEntity();
                        doReturn(false).when(player).fireImmune();
                        doReturn(burningTime).when(player).getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.BURNING_TIME);
                        doNothing().when(player).clearFreeze();
                        var server = mock(org.bukkit.craftbukkit.CraftServer.class);
                        var manager = mock(org.bukkit.plugin.PluginManager.class);
                        when(level.getCraftServer()).thenReturn(server);
                        when(server.getPluginManager()).thenReturn(manager);
                        doAnswer(call -> {
                            var event = (org.bukkit.event.entity.EntityCombustByBlockEvent)call.getArgument(0);
                            assertEquals(8.0F, event.getDuration());
                            event.setCancelled(cancel);
                            return null;
                        }).when(manager).callEvent(any(org.bukkit.event.entity.EntityCombustByBlockEvent.class));
                        boolean classic = legacy && block == net.minecraft.world.level.block.Blocks.FIRE;
                        player.setRemainingFireTicks(-player.getFireImmuneTicks());
                        net.minecraft.world.level.block.BaseFireBlock.fireIgnite(player, net.minecraft.core.BlockPos.ZERO);
                        if (!classic) {
                            assertEquals(-19, player.getRemainingFireTicks());
                            verify(manager, never()).callEvent(any());
                        } else if (cancel) {
                            assertEquals(-1, player.getRemainingFireTicks());
                        } else {
                            assertEquals((int)(160 * burningTime), player.getRemainingFireTicks());
                        }
                    }
                }
            }
        }
    }

    @Test
    void repeatedClassicFireContactBalancesActualBaseTickAndExitWhileModernFireRetainsRandomGrowth() throws Throwable {
        var tick = java.lang.invoke.MethodHandles.privateLookupIn(Entity.class, java.lang.invoke.MethodHandles.lookup())
            .findSpecial(Entity.class, "baseTick", java.lang.invoke.MethodType.methodType(void.class), Entity.class);
        for (boolean legacy : new boolean[]{true, false}) {
            for (net.minecraft.world.level.block.Block block : new net.minecraft.world.level.block.Block[]{
                net.minecraft.world.level.block.Blocks.FIRE, net.minecraft.world.level.block.Blocks.SOUL_FIRE
            }) {
                var player = mock(ServerPlayer.class, CALLS_REAL_METHODS);
                player.legacyCombat = legacy;
                Field abilities = Player.class.getDeclaredField("abilities");
                abilities.setAccessible(true);
                abilities.set(player, new Abilities());
                var level = mock(net.minecraft.server.level.ServerLevel.class);
                doReturn(level).when(player).level();
                doReturn(false).when(player).fireImmune();
                doReturn(false).when(player).isInLava();
                doReturn(false).when((Entity)player).canSpawnSprintParticle();
                doReturn(false).when(player).isEyeInFluid(any());
                doReturn(false).when((Entity)player).updateFluidInteraction();
                doNothing().when((Entity)player).computeSpeed();
                doNothing().when((Entity)player).handlePortal();
                doNothing().when(player).updateSwimming();
                doNothing().when(player).checkBelowWorld();
                doNothing().when(player).setSharedFlagOnFire(anyBoolean());
                doNothing().when(player).clearFreeze();
                doReturn(1.0).when(player).getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.BURNING_TIME);
                doReturn(false).when(player).hurtServer(any(), any(), anyFloat());
                var sources = mock(net.minecraft.world.damagesource.DamageSources.class);
                when(level.damageSources()).thenReturn(sources);
                var random = mock(net.minecraft.util.RandomSource.class);
                when(level.getRandom()).thenReturn(random);
                when(random.nextInt(1, 3)).thenReturn(2);
                when(level.getBlockState(net.minecraft.core.BlockPos.ZERO)).thenReturn(block.defaultBlockState());
                var manager = mock(org.bukkit.plugin.PluginManager.class);
                var server = mock(org.bukkit.craftbukkit.CraftServer.class);
                when(level.getCraftServer()).thenReturn(server);
                when(server.getPluginManager()).thenReturn(manager);
                doReturn(mock(org.bukkit.craftbukkit.entity.CraftPlayer.class)).when(player).getBukkitEntity();
                player.setRemainingFireTicks(160);
                boolean classic = legacy && block == net.minecraft.world.level.block.Blocks.FIRE;
                for (int i = 0; i < 5; i++) {
                    tick.invoke(player); // Actual native Entity.baseTick fire decrement, excluding unrelated LivingEntity tick work.
                    net.minecraft.world.level.block.BaseFireBlock.fireIgnite(player, net.minecraft.core.BlockPos.ZERO);
                    assertEquals(160 + (classic ? 0 : i + 1), player.getRemainingFireTicks());
                }
                tick.invoke(player); // Exit: no contact callback, countdown resumes.
                assertEquals(classic ? 159 : 164, player.getRemainingFireTicks());
                if (classic) verify(random, never()).nextInt(1, 3);
                else verify(random, times(5)).nextInt(1, 3);
                verify(manager, times(5)).callEvent(any(org.bukkit.event.entity.EntityCombustByBlockEvent.class));
            }
        }
    }

}
