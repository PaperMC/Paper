package net.minecraft.world.entity.projectile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InterpolationHandler;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyFishingHookTest {
    private static final class NativeHook extends FishingHook {
        private NativeHook() { super(net.minecraft.world.entity.EntityTypes.FISHING_BOBBER, null); }
        @Override public void applyEffectsFromBlocks() {}
        @Override public void reapplyPosition() {}
    }

    private record Flight(FishingHook hook, ServerLevel level, Player owner, AtomicReference<Vec3> position, AtomicReference<Vec3> motion) {}

    private Flight flight(boolean legacy, boolean water, boolean server) throws Exception {
        ServerLevel level = mock(ServerLevel.class);
        when(level.isClientSide()).thenReturn(!server);
        NativeHook hook = mock(NativeHook.class, CALLS_REAL_METHODS);
        hook.legacyFlight = legacy;
        hook.hasBeenShot = true;
        Field despawn = Entity.class.getDeclaredField("despawnTime");
        despawn.setAccessible(true);
        despawn.setInt(hook, -1);
        hook.leftOwner = true;
        doReturn(level).when(hook).level();
        doReturn(mock(org.bukkit.craftbukkit.entity.CraftFishHook.class)).when(hook).getBukkitEntity();
        doReturn(new UUID(0, 0)).when(hook).getUUID();
        doReturn(mock(InterpolationHandler.class)).when(hook).getInterpolation();
        doNothing().when(hook).baseTick();
        doReturn(false).when(hook).isDescending();
        doNothing().when(hook).checkLeftOwner();
        doNothing().when(hook).applyEffectsFromBlocks();
        doNothing().when(hook).updateRotation();
        doReturn(mock(SynchedEntityData.class)).when(hook).getEntityData();
        Field random = FishingHook.class.getDeclaredField("syncronizedRandom");
        random.setAccessible(true);
        random.set(hook, mock(RandomSource.class));
        Field state = FishingHook.class.getDeclaredField("currentState");
        state.setAccessible(true);
        state.set(hook, FishingHook.FishHookState.FLYING);
        Player owner = mock(Player.class);
        when(owner.isAlive()).thenReturn(true);
        when(owner.position()).thenReturn(Vec3.ZERO);
        when(owner.canInteractWithLevel()).thenReturn(true);
        ItemStack rod = mock(ItemStack.class);
        when(rod.is(Items.FISHING_ROD)).thenReturn(true);
        when(owner.getMainHandItem()).thenReturn(rod);
        when(owner.getOffhandItem()).thenReturn(mock(ItemStack.class));
        doReturn(owner).when(hook).getPlayerOwner();
        doReturn(owner).when(hook).getOwner();
        AtomicReference<Vec3> position = new AtomicReference<>(Vec3.ZERO);
        AtomicReference<Vec3> motion = new AtomicReference<>(new Vec3(1, 0.5, 0.2));
        doAnswer(call -> position.get()).when(hook).position();
        doAnswer(call -> position.get().x).when(hook).getX();
        doAnswer(call -> position.get().y).when(hook).getY();
        doAnswer(call -> position.get().z).when(hook).getZ();
        doReturn(BlockPos.ZERO).when(hook).blockPosition();
        doReturn(new AABB(-0.25, 0, -0.25, 0.25, 0.5, 0.25)).when(hook).getBoundingBox();
        doAnswer(call -> motion.get()).when(hook).getDeltaMovement();
        doAnswer(call -> { motion.set(call.getArgument(0)); return null; }).when(hook).setDeltaMovement(any(Vec3.class));
        doAnswer(call -> { position.set(position.get().add((Vec3)call.getArgument(1))); return null; }).when(hook).move(eq(MoverType.SELF), any());
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of());
        when(level.clipIncludingBorder(any())).thenAnswer(call -> BlockHitResult.miss(position.get().add(motion.get()), Direction.UP, BlockPos.ZERO));
        FluidState fluid = mock(FluidState.class);
        when(fluid.is(FluidTags.WATER)).thenReturn(water);
        when(fluid.getHeight(level, BlockPos.ZERO)).thenReturn(water ? 1F : 0F);
        when(level.getFluidState(BlockPos.ZERO)).thenReturn(fluid);
        doReturn(ProjectileDeflection.NONE).when(hook).preHitTargetOrDeflectSelf(any());
        return new Flight(hook, level, owner, position, motion);
    }

    private void tick(Flight flight) {
        try (var api = mockStatic(org.bukkit.Bukkit.class)) {
            api.when(org.bukkit.Bukkit::getPluginManager).thenReturn(mock(org.bukkit.plugin.PluginManager.class));
            flight.hook.tick();
        }
    }

    @Test
    void actualTickMovesBeforeFloatGravityAndDragOnlyForMarkedAirborneHook() throws Exception {
        Flight legacy = flight(true, false, false);
        tick(legacy);
        assertEquals(new Vec3(1, 0.5, 0.2), legacy.position.get());
        assertEquals(new Vec3(1, 0.5 - (double)0.04F, 0.2).scale((double)0.92F), legacy.motion.get());
        Flight modern = flight(false, false, false);
        tick(modern);
        assertEquals(new Vec3(1, 0.47, 0.2), modern.position.get());
        assertEquals(new Vec3(1, 0.47, 0.2).scale(0.92), modern.motion.get());
    }

    @Test
    void markedGroundCollisionUsesHalfDragWhereUnmarkedNativeStops() throws Exception {
        for (boolean legacy : new boolean[]{false, true}) {
            Flight flight = flight(legacy, false, false);
            flight.hook.horizontalCollision = true;
            tick(flight);
            assertEquals(legacy ? new Vec3(1, 0.5 - (double)0.04F, 0.2).scale((double)0.5F) : Vec3.ZERO, flight.motion.get());
        }
    }

    @Test
    void ownerGraceIsFiveAirborneTicksAndNativePredicateRemainsTheGate() throws Exception {
        Flight flight = flight(true, false, false);
        Field ticks = FishingHook.class.getDeclaredField("legacyAirTicks");
        ticks.setAccessible(true);
        when(flight.owner.canBeHitByProjectile()).thenReturn(true);
        ticks.setInt(flight.hook, 4);
        assertFalse(flight.hook.canHitEntity(flight.owner));
        ticks.setInt(flight.hook, 5);
        assertTrue(flight.hook.canHitEntity(flight.owner));
        when(flight.owner.canBeHitByProjectile()).thenReturn(false);
        assertFalse(flight.hook.canHitEntity(flight.owner));
    }

    @Test
    void onlyAcceptedNativeZeroDamageLatchesMarkedHook() throws Exception {
        for (boolean accepted : new boolean[]{false, true}) {
            Flight flight = flight(true, false, true);
            Entity target = mock(Entity.class);
            DamageSources sources = mock(DamageSources.class);
            DamageSource source = mock(DamageSource.class);
            doReturn(sources).when(flight.hook).damageSources();
            when(sources.thrown(flight.hook, flight.owner)).thenReturn(source);
            when(target.hurtServer(flight.level, source, 0F)).thenReturn(accepted);
            flight.hook.onHitEntity(new EntityHitResult(target));
            verify(target).hurtServer(flight.level, source, 0F);
            if (accepted) verify(flight.hook).setHookedEntity(target); else verify(flight.hook, never()).setHookedEntity(any());
        }
        Flight modern = flight(false, false, true);
        Entity target = mock(Entity.class);
        modern.hook.onHitEntity(new EntityHitResult(target));
        verify(target, never()).hurtServer(any(), any(), anyFloat());
        verify(modern.hook).setHookedEntity(target);
    }

    @Test
    void underwaterBobbingStillChecksTargetsBeforeBuoyancy() throws Exception {
        Flight flight = flight(true, true, true);
        Field state = FishingHook.class.getDeclaredField("currentState");
        state.setAccessible(true);
        state.set(flight.hook, FishingHook.FishHookState.BOBBING);
        Entity target = mock(Entity.class);
        when(target.getBoundingBox()).thenReturn(new AABB(0.5, -1, -1, 0.8, 1, 1));
        when(flight.level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(target));
        doAnswer(call -> { flight.hook.setHookedEntity(target); return ProjectileDeflection.NONE; })
            .when(flight.hook).preHitTargetOrDeflectSelf(any(EntityHitResult.class));
        tick(flight);
        verify(flight.hook).preHitTargetOrDeflectSelf(any(EntityHitResult.class));
        assertEquals(FishingHook.FishHookState.HOOKED_IN_ENTITY, state.get(flight.hook));
    }

    @Test
    void nativeReelAddsLegacyFourthRootLiftOnlyWhenMarked() throws Exception {
        for (boolean legacy : new boolean[]{false, true}) {
            Flight flight = flight(legacy, false, false);
            when(flight.owner.getX()).thenReturn(2.0);
            when(flight.owner.getY()).thenReturn(3.0);
            when(flight.owner.getZ()).thenReturn(4.0);
            Entity target = mock(Entity.class);
            when(target.getDeltaMovement()).thenReturn(new Vec3(0.1, 0.2, 0.3));
            flight.hook.pullEntity(target);
            double distance = (double)(float)Math.sqrt(29.0);
            double lift = legacy ? (double)(float)Math.sqrt(distance) * 0.08 : 0.0;
            verify(target).setDeltaMovement(new Vec3(0.1 + 2.0 * 0.1, 0.2 + (3.0 * 0.1 + lift), 0.3 + 4.0 * 0.1));
        }
    }
    @Test
    void lockedResidualRodCannotKeepNativeHookAliveAfterMainHandSwitch() throws Exception {
        var stop = FishingHook.class.getDeclaredMethod("shouldStopFishing", Player.class);
        stop.setAccessible(true);
        for (boolean enabled : new boolean[]{false, true}) {
            Flight f = flight(false, false, true);
            f.owner.offHandEnabled = enabled;
            when(f.owner.canUseSlot(any())).thenCallRealMethod();
            when(f.owner.getMainHandItem()).thenReturn(ItemStack.EMPTY);
            when(f.owner.getOffhandItem()).thenReturn(new ItemStack(Items.FISHING_ROD));
            doNothing().when(f.hook).discard(any(EntityRemoveEvent.Cause.class));
            assertEquals(!enabled, stop.invoke(f.hook, f.owner));
            if (enabled) verify(f.hook, never()).discard(any(EntityRemoveEvent.Cause.class));
            else verify(f.hook).discard(EntityRemoveEvent.Cause.DESPAWN);
            when(f.owner.getMainHandItem()).thenReturn(new ItemStack(Items.FISHING_ROD));
            assertEquals(false, stop.invoke(f.hook, f.owner));
        }
    }

}
