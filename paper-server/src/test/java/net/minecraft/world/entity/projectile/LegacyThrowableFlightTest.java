package net.minecraft.world.entity.projectile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyThrowableFlightTest {
    private static final class NativeSnowball extends Snowball {
        private NativeSnowball() { super(EntityTypes.SNOWBALL, null); }
        @Override public void applyEffectsFromBlocks() {}
    }
    private record Flight(NativeSnowball projectile, ServerLevel level, AtomicReference<Vec3> position, AtomicReference<Vec3> velocity) {}

    private Flight flight(boolean legacy, boolean startWater, boolean endWater, boolean cachedWater) throws Exception {
        NativeSnowball projectile = mock(NativeSnowball.class, CALLS_REAL_METHODS);
        projectile.legacyFlight = legacy;
        projectile.hasBeenShot = true;
        projectile.leftOwner = true;
        Field despawn = Entity.class.getDeclaredField("despawnTime");
        despawn.setAccessible(true);
        despawn.setInt(projectile, -1);
        ServerLevel level = mock(ServerLevel.class);
        doReturn(level).when(projectile).level();
        doReturn(false).when(projectile).isNoGravity();
        doReturn(false).when(projectile).isDescending();
        doReturn(true).when(projectile).isAlive();
        doNothing().when(projectile).checkLeftOwner();
        doNothing().when(projectile).updateRotation();
        AtomicReference<Vec3> position = new AtomicReference<>(Vec3.ZERO);
        AtomicReference<Vec3> velocity = new AtomicReference<>(new Vec3(1, 0.5, 0.2));
        AtomicBoolean water = new AtomicBoolean(cachedWater);
        doAnswer(call -> { water.set(position.get().x < 0.5 ? startWater : endWater); return null; }).when(projectile).baseTick();
        doAnswer(call -> water.get()).when(projectile).isInWater();
        doAnswer(call -> position.get()).when(projectile).position();
        doAnswer(call -> position.get().y).when(projectile).getY();
        doAnswer(call -> { position.set(call.getArgument(0)); return null; }).when(projectile).setPos(any(Vec3.class));
        doAnswer(call -> velocity.get()).when(projectile).getDeltaMovement();
        doAnswer(call -> { velocity.set(call.getArgument(0)); return null; }).when(projectile).setDeltaMovement(any(Vec3.class));
        doReturn(new AABB(-0.125, 0, -0.125, 0.125, 0.25, 0.125)).when(projectile).getBoundingBox();
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of());
        when(level.clipIncludingBorder(any())).thenAnswer(call -> BlockHitResult.miss(position.get().add(velocity.get()), Direction.UP, BlockPos.ZERO));
        doReturn(ProjectileDeflection.NONE).when(projectile).preHitTargetOrDeflectSelf(any());
        return new Flight(projectile, level, position, velocity);
    }

    @Test
    void markedDryTickMovesBeforeFloatDragAndGravityAndUnmarkedStaysNative() throws Exception {
        Flight legacy = flight(true, false, false, false);
        legacy.projectile.tick();
        assertEquals(new Vec3(1, 0.5, 0.2), legacy.position.get());
        assertEquals(new Vec3(1, 0.5, 0.2).scale((double)0.99F).add(0, -(double)0.03F, 0), legacy.velocity.get());
        verify(legacy.projectile).baseTick();
        Flight modern = flight(false, false, false, false);
        modern.projectile.tick();
        assertEquals(new Vec3(1, 0.5 - 0.03, 0.2).scale((double)0.99F), modern.position.get());
        assertEquals(modern.position.get(), modern.velocity.get());
        verify(modern.projectile).baseTick();
    }

    @Test
    void startingWaterControlsDragForEntryExitAndFirstSpawn() throws Exception {
        for (boolean starting : new boolean[]{false, true}) {
            for (boolean ending : new boolean[]{false, true}) {
                Flight flight = flight(true, starting, ending, false);
                flight.projectile.tick();
                assertEquals(new Vec3(1, 0.5, 0.2), flight.position.get());
                float drag = starting ? 0.8F : 0.99F;
                assertEquals(new Vec3(1, 0.5, 0.2).scale((double)drag).add(0, -(double)0.03F, 0), flight.velocity.get());
                verify(flight.projectile).baseTick();
            }
        }
        Flight modernFirstSpawn = flight(false, true, true, false);
        modernFirstSpawn.projectile.tick();
        assertEquals(new Vec3(1, 0.47, 0.2).scale((double)0.99F), modernFirstSpawn.velocity.get());
    }

    @Test
    void impactOriginExistsOnlyInsideNativeCallbackEvenIfItThrows() throws Exception {
        Flight flight = flight(true, false, false, false);
        BlockHitResult hit = new BlockHitResult(new Vec3(0.25, 0.125, 0.05), Direction.WEST, BlockPos.ZERO, false);
        when(flight.level.clipIncludingBorder(any())).thenReturn(hit);
        RuntimeException cancelled = new RuntimeException("fixture callback failure");
        doAnswer(call -> {
            assertEquals(Vec3.ZERO, flight.projectile.getLegacyImpactOrigin());
            assertEquals(hit.getLocation(), flight.position.get());
            throw cancelled;
        }).when(flight.projectile).preHitTargetOrDeflectSelf(hit);
        assertSame(cancelled, assertThrows(RuntimeException.class, flight.projectile::tick));
        assertNull(flight.projectile.getLegacyImpactOrigin());
    }

    @Test
    void ownerGraceAndSavedProfileDoNotReplaceNativeEligibility() throws Exception {
        Flight flight = flight(true, false, false, false);
        Entity owner = mock(Entity.class);
        doReturn(owner).when(flight.projectile).getOwner();
        when(owner.canBeHitByProjectile()).thenReturn(true);
        Field age = ThrowableProjectile.class.getDeclaredField("legacyAirTicks");
        age.setAccessible(true);
        age.setInt(flight.projectile, 4);
        assertFalse(flight.projectile.canHitEntity(owner));
        age.setInt(flight.projectile, 5);
        assertTrue(flight.projectile.canHitEntity(owner));
        when(owner.canBeHitByProjectile()).thenReturn(false);
        assertFalse(flight.projectile.canHitEntity(owner));
        doReturn(mock(net.minecraft.world.item.ItemStack.class)).when(flight.projectile).getItem();
        ValueOutput output = mock(ValueOutput.class);
        ((ThrowableProjectile)flight.projectile).addAdditionalSaveData(output);
        verify(output).putBoolean("LegacyFlight", true);
        ValueInput input = mock(ValueInput.class);
        when(input.read(anyString(), any())).thenReturn(java.util.Optional.empty());
        when(input.getBooleanOr("LegacyFlight", false)).thenReturn(true);
        doNothing().when(flight.projectile).setItem(any());
        ((ThrowableProjectile)flight.projectile).readAdditionalSaveData(input);
        assertTrue(flight.projectile.legacyFlight);
        assertEquals(0, age.getInt(flight.projectile));
        when(input.getBooleanOr("LegacyFlight", false)).thenReturn(false);
        ((ThrowableProjectile)flight.projectile).readAdditionalSaveData(input);
        assertFalse(flight.projectile.legacyFlight);
    }
    @Test
    void eachClassicFamilyUsesItsFloatGravityOnlyWhenFlagged() {
        var snow = mock(Snowball.class, CALLS_REAL_METHODS);
        var potion = mock(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownSplashPotion.class, CALLS_REAL_METHODS);
        var xp = mock(net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle.class, CALLS_REAL_METHODS);
        for (ThrowableProjectile projectile : new ThrowableProjectile[]{snow, potion, xp}) {
            double modern = projectile == snow ? 0.03 : projectile == potion ? 0.05 : 0.07;
            assertEquals(modern, projectile.getDefaultGravity());
            projectile.legacyFlight = true;
            assertEquals((double)(float)modern, projectile.getDefaultGravity());
        }
    }

}
