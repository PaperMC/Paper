package net.minecraft.world.entity.projectile.arrow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class LegacyArrowFlightTest {

    private Arrow arrow(Level level) {
        Arrow arrow = mock(Arrow.class, CALLS_REAL_METHODS);
        arrow.legacyFlight = true;
        doReturn(level).when(arrow).level();
        doReturn(Vec3.ZERO).when(arrow).getDeltaMovement();
        doReturn(false).when(arrow).isInGround();
        doReturn((byte) 0).when(arrow).getPierceLevel();
        doReturn(new AABB(-0.25, 0, -0.25, 0.25, 0.5, 0.25)).when(arrow).getBoundingBox();
        return arrow;
    }

    private Entity target(AABB bounds) {
        Entity target = mock(Entity.class);
        when(target.getBoundingBox()).thenReturn(bounds);
        when(target.canBeHitByProjectile()).thenReturn(true);
        when(target.position()).thenReturn(bounds.getCenter());
        return target;
    }

    @Test
    void ownerImmunityUsesAirborneTicksInsteadOfLeavingTheBoundingBox() throws Exception {
        Arrow arrow = arrow(mock(Level.class));
        Entity owner = mock(Entity.class);
        when(owner.isPassengerOfSameVehicle(owner)).thenReturn(true);
        Field airTicks = AbstractArrow.class.getDeclaredField("legacyAirTicks");
        airTicks.setAccessible(true);
        for (int tick = 0; tick < 5; tick++) {
            airTicks.setInt(arrow, tick);
            assertFalse(arrow.canCollideWithOwner(owner, owner));
        }
        airTicks.setInt(arrow, 5);
        assertTrue(arrow.canCollideWithOwner(owner, owner));
        assertTrue(arrow.canCollideWithOwner(owner, mock(Entity.class)));
        arrow.legacyFlight = false;
        assertFalse(arrow.canCollideWithOwner(owner, owner));
    }

    @Test
    void selectsNearestRayIntersectionRatherThanNearestEntityCenter() {
        Level level = mock(Level.class);
        Arrow arrow = arrow(level);
        // The long box has a farther center, but its ray entry is closest.
        Entity nearerEntry = target(new AABB(2, -1, -1, 12, 1, 1));
        Entity nearerCenter = target(new AABB(4, -1, -1, 5, 1, 1));
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(nearerCenter, nearerEntry));
        Collection<EntityHitResult> hits = arrow.findHitEntities(Vec3.ZERO, new Vec3(20, 0, 0));
        assertEquals(1, hits.size());
        assertEquals(nearerEntry, hits.iterator().next().getEntity());
    }

    @Test
    void youngArrowHasConstantMarginAndCannotHitBeyondClippedBlockEndpoint() {
        Level level = mock(Level.class);
        Arrow arrow = arrow(level);
        Entity withinMargin = target(new AABB(1, -1, 0.25, 2, 1, 1));
        Entity beyondWall = target(new AABB(4, -1, -1, 5, 1, 1));
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(beyondWall, withinMargin));
        Collection<EntityHitResult> hits = arrow.findHitEntities(Vec3.ZERO, new Vec3(3, 0, 0));
        assertEquals(withinMargin, hits.iterator().next().getEntity());
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(beyondWall));
        assertTrue(arrow.findHitEntities(Vec3.ZERO, new Vec3(3, 0, 0)).isEmpty());
    }

    @Test
    void legacyOwnerRuleDoesNotBypassNativeEntityEligibility() throws Exception {
        Arrow arrow = arrow(mock(Level.class));
        Entity owner = mock(Entity.class);
        doReturn(owner).when(arrow).getOwner();
        Field airTicks = AbstractArrow.class.getDeclaredField("legacyAirTicks");
        airTicks.setAccessible(true);
        airTicks.setInt(arrow, 5);
        assertFalse(arrow.canHitEntity(owner));
        when(owner.canBeHitByProjectile()).thenReturn(true);
        assertTrue(arrow.canHitEntity(owner));
    }

    @Test
    void selectedCollisionPassesThroughCancellableNativeHitBoundary() throws Exception {
        Level level = mock(Level.class);
        Arrow arrow = arrow(level);
        Entity target = target(new AABB(1, -1, -1, 2, 1, 1));
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(target));
        doReturn(Vec3.ZERO).when(arrow).position();
        doReturn(true).when(arrow).isAlive();
        doNothing().when(arrow).setPos(any(Vec3.class));
        doNothing().when(arrow).applyEffectsFromBlocks(any(Vec3.class), any(Vec3.class));
        // A cancelled native projectile-hit callback returns NONE without damage.
        doReturn(ProjectileDeflection.NONE).when(arrow).preHitTargetOrDeflectSelf(any());
        BlockHitResult endpoint = mock(BlockHitResult.class);
        when(endpoint.getLocation()).thenReturn(new Vec3(3, 0, 0));
        Method step = AbstractArrow.class.getDeclaredMethod("stepMoveAndHit", BlockHitResult.class);
        step.setAccessible(true);
        step.invoke(arrow, endpoint);
        verify(arrow).preHitTargetOrDeflectSelf(any(EntityHitResult.class));
        verify(arrow, never()).onHitEntity(any());
    }

    @Test
    void profileSurvivesSaveLoadAndOldArrowsRemainNativeByDefault() throws Exception {
        Arrow arrow = arrow(mock(Level.class));
        doReturn(false).when(arrow).isInGround();
        doReturn(false).when(arrow).isCritArrow();
        ValueOutput output = mock(ValueOutput.class);
        arrow.addAdditionalSaveData(output);
        verify(output).putBoolean("LegacyFlight", true);

        ValueInput input = mock(ValueInput.class);
        when(input.read(anyString(), any())).thenReturn(java.util.Optional.empty());
        when(input.getBooleanOr("LegacyFlight", false)).thenReturn(true);
        doNothing().when(arrow).setInGround(false);
        doNothing().when(arrow).setCritArrow(false);
        doNothing().when(arrow).setPierceLevel((byte) 0);
        doNothing().when(arrow).setPickupItemStack(any());
        Field age = AbstractArrow.class.getDeclaredField("legacyAirTicks");
        age.setAccessible(true);
        age.setInt(arrow, 9);
        arrow.legacyFlight = false;
        arrow.readAdditionalSaveData(input);
        assertTrue(arrow.legacyFlight);
        assertEquals(0, age.getInt(arrow));

        when(input.getBooleanOr("LegacyFlight", false)).thenReturn(false);
        arrow.readAdditionalSaveData(input);
        assertFalse(arrow.legacyFlight);
    }

    @Test
    void nativeDragAndGravityFollowLegacyAirAndWaterTrajectories() throws Exception {
        Method drag = AbstractArrow.class.getDeclaredMethod("applyFlightDrag", Vec3.class);
        drag.setAccessible(true);
        Method gravity = Entity.class.getDeclaredMethod("applyGravity");
        gravity.setAccessible(true);
        for (boolean water : new boolean[]{false, true}) {
            Arrow arrow = arrow(mock(Level.class));
            doReturn(water).when(arrow).isInWater();
            doReturn(false).when(arrow).isNoGravity();
            java.util.concurrent.atomic.AtomicReference<Vec3> movement =
                new java.util.concurrent.atomic.AtomicReference<>(new Vec3(2, 0.4, 0));
            doAnswer(call -> movement.get()).when(arrow).getDeltaMovement();
            doAnswer(call -> { movement.set(call.getArgument(0)); return null; }).when(arrow).setDeltaMovement(any(Vec3.class));
            Vec3 position = Vec3.ZERO;
            for (int tick = 0; tick < 3; tick++) {
                position = position.add(movement.get());
                drag.invoke(arrow, position);
                gravity.invoke(arrow);
            }
            assertEquals(water ? 4.88 : 5.9402, position.x, 1.0E-6);
            assertEquals(water ? 0.836 : 1.03854, position.y, 1.0E-6);
        }
    }

    @Test
    void gravityPreservesLegacyFloatValueOnlyWhenEnabled() {
        Arrow arrow = arrow(mock(Level.class));
        assertEquals((double) 0.05F, arrow.getDefaultGravity());
        arrow.legacyFlight = false;
        assertEquals(0.05, arrow.getDefaultGravity());
    }
    @Test
    void markedRayIncludesInsideExitAndBothBoundaryEndpoints() {
        assertLegacyRay(new Vec3(0.5, 0.5, 0.5), new Vec3(2, 0.5, 0.5), new Vec3(1, 0.5, 0.5));
        assertLegacyRay(new Vec3(0, 0.5, 0.5), new Vec3(2, 0.5, 0.5), new Vec3(0, 0.5, 0.5));
        assertLegacyRay(new Vec3(-1, 0.5, 0.5), new Vec3(0, 0.5, 0.5), new Vec3(0, 0.5, 0.5));
        assertLegacyRay(new Vec3(-1, -1, 0.5), new Vec3(1, 1, 0.5), new Vec3(0, 0, 0.5));
    }

    @Test
    void markedRayUsesLegacySquaredAxisInterpolationCutoff() {
        assertLegacyRay(new Vec3(-0.00005, 0.5, 0.5), new Vec3(0.00005, 0.5, 0.5), null);
        assertLegacyRay(new Vec3(-0.0002, 0.5, 0.5), new Vec3(0.0002, 0.5, 0.5), new Vec3(0, 0.5, 0.5));
    }

    private void assertLegacyRay(Vec3 from, Vec3 to, Vec3 expected) {
        Level level = mock(Level.class);
        Arrow arrow = arrow(level);
        double margin = (double)0.3F;
        Entity target = target(new AABB(margin, margin, margin, 1 - margin, 1 - margin, 1 - margin));
        when(level.getEntities(any(Entity.class), any(AABB.class), any())).thenReturn(List.of(target));
        var hits = arrow.findHitEntities(from, to);
        if (expected == null) assertTrue(hits.isEmpty());
        else {
            assertEquals(1, hits.size());
            assertEquals(expected, hits.iterator().next().getLocation());
        }
    }

    @Test
    void markedObliqueBlockImpactKeepsFloatSegmentAndReleasedMotion() throws Exception {
        blockImpact(true, false);
    }

    @Test
    void unmarkedObliqueBlockImpactRetainsNativeEmbeddingAndVerticalRelease() throws Exception {
        blockImpact(false, false);
    }

    @Test
    void cancelledBlockHitRetainsNativeEmbeddingButSuppressesBlockCallbacks() throws Exception {
        blockImpact(true, true);
        blockImpact(false, true);
    }

    private void blockImpact(boolean legacy, boolean cancelled) throws Exception {
        Level level = mock(Level.class);
        io.papermc.paper.configuration.WorldConfiguration config = mock(io.papermc.paper.configuration.WorldConfiguration.class);
        config.entities = mock(io.papermc.paper.configuration.WorldConfiguration.Entities.class);
        config.entities.spawning = mock(io.papermc.paper.configuration.WorldConfiguration.Entities.Spawning.class);
        config.entities.spawning.maxArrowDespawnInvulnerability = new io.papermc.paper.configuration.type.number.IntOr.Disabled(java.util.OptionalInt.of(200));
        when(level.paperConfig()).thenReturn(config);
        var block = mock(net.minecraft.world.level.block.state.BlockState.class);
        when(level.getBlockState(any())).thenReturn(block);
        Arrow arrow = arrow(level);
        arrow.legacyFlight = legacy;
        Field cancelledField = net.minecraft.world.entity.projectile.Projectile.class.getDeclaredField("hitCancelled");
        cancelledField.setAccessible(true);
        cancelledField.setBoolean(arrow, cancelled);
        Field randomField = Entity.class.getDeclaredField("random");
        randomField.setAccessible(true);
        var random = mock(net.minecraft.util.RandomSource.class);
        when(random.nextFloat()).thenReturn(0.5F);
        randomField.set(arrow, random);
        Vec3 initial = new Vec3(0.2, 0.3, 0.4);
        Vec3 hit = new Vec3(1.1, 0.9, 2.0);
        var position = new java.util.concurrent.atomic.AtomicReference<>(initial);
        var movement = new java.util.concurrent.atomic.AtomicReference<>(new Vec3(2, 1, 3));
        var ground = new java.util.concurrent.atomic.AtomicBoolean();
        doAnswer(call -> position.get()).when(arrow).position();
        doAnswer(call -> { position.set(call.getArgument(0)); return null; }).when(arrow).setPos(any(Vec3.class));
        doAnswer(call -> movement.get()).when(arrow).getDeltaMovement();
        doAnswer(call -> { movement.set(call.getArgument(0)); return null; }).when(arrow).setDeltaMovement(any(Vec3.class));
        doAnswer(call -> ground.get()).when(arrow).isInGround();
        doAnswer(call -> { ground.set(call.getArgument(0)); return null; }).when(arrow).setInGround(org.mockito.ArgumentMatchers.anyBoolean());
        doNothing().when(arrow).setCritArrow(org.mockito.ArgumentMatchers.anyBoolean());
        doNothing().when(arrow).setPierceLevel(org.mockito.ArgumentMatchers.anyByte());
        doNothing().when(arrow).playSound(any(), org.mockito.ArgumentMatchers.anyFloat(), org.mockito.ArgumentMatchers.anyFloat());
        doReturn(List.of()).when(arrow).findHitEntities(any(), any());
        doReturn(true).when(arrow).isAlive();
        doNothing().when(arrow).applyEffectsFromBlocks(any(), any());
        BlockHitResult result = new BlockHitResult(hit, net.minecraft.core.Direction.WEST, net.minecraft.core.BlockPos.ZERO, false);
        doAnswer(call -> { arrow.onHitBlock(call.getArgument(0)); return ProjectileDeflection.NONE; })
            .when(arrow).preHitTargetOrDeflectSelf(any(BlockHitResult.class));
        Method step = AbstractArrow.class.getDeclaredMethod("stepMoveAndHit", BlockHitResult.class);
        step.setAccessible(true);
        step.invoke(arrow, result);
        Vec3 segment = new Vec3((float)(hit.x - initial.x), (float)(hit.y - initial.y), (float)(hit.z - initial.z));
        float length = (float)Math.sqrt(segment.lengthSqr());
        Vec3 embedded = legacy ? initial.subtract(new Vec3(segment.x / length * (double)0.05F, segment.y / length * (double)0.05F, segment.z / length * (double)0.05F)).add(segment)
            : hit.subtract(new Vec3(0.05F, 0.05F, 0.05F));
        assertEquals(embedded.x, position.get().x, 1.0E-15);
        assertEquals(embedded.y, position.get().y, 1.0E-15);
        assertEquals(embedded.z, position.get().z, 1.0E-15);
        assertEquals(legacy ? segment : Vec3.ZERO, movement.get());
        assertTrue(ground.get());
        if (cancelled) verify(block, never()).onProjectileHit(level, block, result, arrow);
        else verify(block).onProjectileHit(level, block, result, arrow);
        Method drag = AbstractArrow.class.getDeclaredMethod("applyFlightDrag", Vec3.class);
        drag.setAccessible(true);
        doReturn(false).when(arrow).isInWater();
        doReturn(false).when(arrow).isNoGravity();
        drag.invoke(arrow, initial);
        Vec3 beforeRelease = legacy ? segment.scale((double)0.99F).add(0, -(double)0.05F, 0) : Vec3.ZERO;
        assertEquals(beforeRelease, movement.get());
        Method shouldFall = AbstractArrow.class.getDeclaredMethod("shouldFall");
        shouldFall.setAccessible(true);
        assertFalse((boolean) shouldFall.invoke(arrow));
        when(level.noCollision(any(AABB.class))).thenReturn(true); // Supporting collision removed.
        assertTrue((boolean) shouldFall.invoke(arrow));
        Method falling = AbstractArrow.class.getDeclaredMethod("startFalling");
        falling.setAccessible(true);
        falling.invoke(arrow);
        assertFalse(ground.get());
        assertEquals(beforeRelease.multiply((double)0.1F, (double)0.1F, (double)0.1F), movement.get());
    }

}
