package net.minecraft.world.entity.projectile.throwableitemprojectile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

@VanillaFeature
class LegacySplashOriginTest {
    @Test void markedSplashUsesPreStepFeetDistanceWhileUnmarkedRetainsBoxDistance() {
        splash(true, false, false, 0.25, 50);
        splash(false, false, false, 0.8125, 163);
    }
    @Test void directHitHasFullLegacyIntensityAndCancelledSplashAppliesNothing() {
        splash(true, true, false, 1.0, 200);
        splash(true, false, true, 0.25, 0);
    }
    private void splash(boolean legacy, boolean direct, boolean cancelled, double intensity, int duration) {
        ServerLevel level = mock(ServerLevel.class);
        ThrownSplashPotion potion = mock(ThrownSplashPotion.class, CALLS_REAL_METHODS);
        potion.legacyFlight = legacy;
        doReturn(level).when(potion).level();
        Vec3 actualHit = new Vec3(2, 0, 0);
        doReturn(actualHit).when(potion).position();
        doReturn(Vec3.ZERO).when(potion).getLegacyImpactOrigin();
        doReturn(null).when(potion).getOwner();
        AABB impactBox = new AABB(1.875, 0, -0.125, 2.125, 0.25, 0.125);
        doReturn(impactBox).when(potion).getBoundingBox();
        LivingEntity target = mock(LivingEntity.class);
        when(target.position()).thenReturn(new Vec3(direct ? 2 : 3, 0, 0));
        when(target.getBoundingBox()).thenReturn(impactBox.move(direct ? 0 : 1, 0, 0));
        when(target.isAffectedByPotions()).thenReturn(true);
        CraftLivingEntity bukkit = mock(CraftLivingEntity.class);
        when(bukkit.getHandle()).thenReturn(target);
        when(target.getBukkitLivingEntity()).thenReturn(bukkit);
        when(level.getEntitiesOfClass(eq(LivingEntity.class), any(AABB.class))).thenReturn(List.of(target));
        ItemStack item = mock(ItemStack.class);
        PotionContents contents = mock(PotionContents.class);
        when(contents.getAllEffects()).thenReturn(List.of(new MobEffectInstance(MobEffects.SPEED, 200)));
        when(item.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)).thenReturn(contents);
        when(item.getOrDefault(DataComponents.POTION_DURATION_SCALE, 1.0F)).thenReturn(1.0F);
        HitResult hit = direct ? new EntityHitResult(target, actualHit) : new BlockHitResult(actualHit, Direction.WEST, BlockPos.ZERO, false);
        try (var events = mockStatic(CraftEventFactory.class)) {
            events.when(() -> CraftEventFactory.callPotionSplashEvent(same(potion), same(hit), anyMap())).thenAnswer(call -> {
                Map<org.bukkit.entity.LivingEntity, Double> affected = call.getArgument(2);
                assertEquals(intensity, affected.get(bukkit), 0.0);
                PotionSplashEvent event = mock(PotionSplashEvent.class);
                when(event.isCancelled()).thenReturn(cancelled);
                when(event.getAffectedEntities()).thenReturn(affected.keySet());
                when(event.getIntensity(bukkit)).thenReturn(affected.get(bukkit));
                return event;
            });
            assertEquals(!cancelled, potion.onHitAsPotion(level, item, hit));
        }
        ArgumentCaptor<AABB> search = ArgumentCaptor.forClass(AABB.class);
        verify(level).getEntitiesOfClass(eq(LivingEntity.class), search.capture());
        assertEquals(legacy ? -4.125 : -2.125, search.getValue().minX, 0.0);
        assertEquals(actualHit, potion.position());
        if (cancelled) verify(target, never()).addEffect(any(), any(), any(EntityPotionEffectEvent.Cause.class));
        else {
            var effect = ArgumentCaptor.forClass(MobEffectInstance.class);
            verify(target).addEffect(effect.capture(), same(potion), eq(EntityPotionEffectEvent.Cause.POTION_SPLASH));
            assertEquals(duration, effect.getValue().getDuration());
        }
    }
}
