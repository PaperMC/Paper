package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FishHook;
import org.bukkit.inventory.EquipmentSlot;

public class CraftFishHook extends CraftProjectile implements FishHook {

    private double biteChance = -1;

    public CraftFishHook(CraftServer server, FishingHook entity) {
        super(server, entity);
    }

    @Override
    public FishingHook getHandle() {
        return (FishingHook) this.entity;
    }

    @Override
    public int getMinWaitTime() {
        return this.getHandle().minWaitTime;
    }

    @Override
    public void setMinWaitTime(int minWaitTime) {
        Preconditions.checkArgument(minWaitTime >= 0 && minWaitTime <= this.getMaxWaitTime(), "The minimum wait time should be between %s and %s (the maximum wait time)", 0, this.getMaxWaitTime());
        FishingHook hook = this.getHandle();
        hook.minWaitTime = minWaitTime;
    }

    @Override
    public int getMaxWaitTime() {
        return this.getHandle().maxWaitTime;
    }

    @Override
    public void setMaxWaitTime(int maxWaitTime) {
        Preconditions.checkArgument(maxWaitTime >= 0 && maxWaitTime >= this.getMinWaitTime(), "The maximum wait time should be between %s and %s (the minimum wait time)", 0, this.getMinWaitTime());
        FishingHook hook = this.getHandle();
        hook.maxWaitTime = maxWaitTime;
    }

    @Override
    public void setWaitTime(int min, int max) {
        Preconditions.checkArgument(min >= 0 && max >= 0 && min <= max, "The minimum/maximum wait time should be higher than or equal to 0 and the minimum wait time");
        this.getHandle().minWaitTime = min;
        this.getHandle().maxWaitTime = max;
    }

    @Override
    public int getMinLureTime() {
        return this.getHandle().minLureTime;
    }

    @Override
    public void setMinLureTime(int minLureTime) {
        Preconditions.checkArgument(minLureTime >= 0 && minLureTime <= this.getMaxLureTime(), "The minimum lure time (%s) should be between 0 and %s (the maximum wait time)", minLureTime, this.getMaxLureTime());
        this.getHandle().minLureTime = minLureTime;
    }

    @Override
    public int getMaxLureTime() {
        return this.getHandle().maxLureTime;
    }

    @Override
    public void setMaxLureTime(int maxLureTime) {
        Preconditions.checkArgument(maxLureTime >= 0 && maxLureTime >= this.getMinLureTime(), "The maximum lure time (%s) should be higher than or equal to 0 and %s (the minimum wait time)", maxLureTime, this.getMinLureTime());
        this.getHandle().maxLureTime = maxLureTime;
    }

    @Override
    public void setLureTime(int min, int max) {
        Preconditions.checkArgument(min >= 0 && max >= 0 && min <= max, "The minimum/maximum lure time should be higher than or equal to 0 and the minimum wait time.");
        this.getHandle().minLureTime = min;
        this.getHandle().maxLureTime = max;
    }

    @Override
    public float getMinLureAngle() {
        return this.getHandle().minLureAngle;
    }

    @Override
    public void setMinLureAngle(float minLureAngle) {
        Preconditions.checkArgument(minLureAngle <= this.getMaxLureAngle(), "The minimum lure angle (%s) should be less than %s (the maximum lure angle)", minLureAngle, this.getMaxLureAngle());
        this.getHandle().minLureAngle = minLureAngle;
    }

    @Override
    public float getMaxLureAngle() {
        return this.getHandle().maxLureAngle;
    }

    @Override
    public void setMaxLureAngle(float maxLureAngle) {
        Preconditions.checkArgument(maxLureAngle >= this.getMinLureAngle(), "The minimum lure angle (%s) should be less than %s (the maximum lure angle)", maxLureAngle, this.getMinLureAngle());
        this.getHandle().maxLureAngle = maxLureAngle;
    }

    @Override
    public void setLureAngle(float min, float max) {
        Preconditions.checkArgument(min <= max, "The minimum lure (%s) angle should be less than the maximum lure angle (%s)", min, max);
        this.getHandle().minLureAngle = min;
        this.getHandle().maxLureAngle = max;
    }

    @Override
    public boolean isSkyInfluenced() {
        return this.getHandle().skyInfluenced;
    }

    @Override
    public void setSkyInfluenced(boolean skyInfluenced) {
        this.getHandle().skyInfluenced = skyInfluenced;
    }

    @Override
    public boolean isRainInfluenced() {
        return this.getHandle().rainInfluenced;
    }

    @Override
    public void setRainInfluenced(boolean rainInfluenced) {
        this.getHandle().rainInfluenced = rainInfluenced;
    }

    @Override
    public boolean getApplyLure() {
        return this.getHandle().applyLure;
    }

    @Override
    public void setApplyLure(boolean applyLure) {
        this.getHandle().applyLure = applyLure;
    }

    @Override
    public double getBiteChance() {
        FishingHook hook = this.getHandle();

        if (this.biteChance == -1) {
            if (hook.level().isRainingAt(BlockPos.containing(hook.position()).offset(0, 1, 0))) {
                return 1 / 300.0;
            }
            return 1 / 500.0;
        }
        return this.biteChance;
    }

    @Override
    public void setBiteChance(double chance) {
        Preconditions.checkArgument(chance >= 0 && chance <= 1, "The bite chance must be between 0 and 1");
        this.biteChance = chance;
    }

    @Override
    public boolean isInOpenWater() {
        return this.getHandle().outOfWaterTime < 10 && this.getHandle().calculateOpenWater(this.getHandle().blockPosition()); // Paper - isOpenWaterFishing is only calculated when a "fish" is approaching the hook
    }

    @Override
    public Entity getHookedEntity() {
        net.minecraft.world.entity.Entity hooked = this.getHandle().hookedIn;
        return (hooked != null) ? hooked.getBukkitEntity() : null;
    }

    @Override
    public void setHookedEntity(Entity entity) {
        FishingHook hook = this.getHandle();

        hook.hookedIn = (entity != null) ? ((CraftEntity) entity).getHandle() : null;
        hook.getEntityData().set(FishingHook.DATA_HOOKED_ENTITY, hook.hookedIn != null ? hook.hookedIn.getId() + 1 : 0);
    }

    @Override
    public boolean pullHookedEntity() {
        FishingHook hook = this.getHandle();
        if (hook.hookedIn == null) {
            return false;
        }

        hook.pullEntity(hook.hookedIn);
        return true;
    }

    @Override
    public HookState getState() {
        return HookState.values()[this.getHandle().currentState.ordinal()];
    }

    @Override
    public int getWaitTime() {
        return this.getHandle().timeUntilLured;
    }

    @Override
    public void setWaitTime(int ticks) {
        this.getHandle().timeUntilLured = ticks;
    }

    @Override
    public int getTimeUntilBite() {
        return this.getHandle().timeUntilHooked;
    }

    @Override
    public void setTimeUntilBite(final int ticks) {
        com.google.common.base.Preconditions.checkArgument(ticks >= 1, "Cannot set time until bite to less than 1 (%s<1)", ticks);
        final FishingHook hook = this.getHandle();

        // Reset the fish angle hook only when this call "enters" the fish into the lure stage.
        final boolean alreadyInLuringPhase = hook.timeUntilHooked > 0 && hook.timeUntilLured <= 0;
        if (!alreadyInLuringPhase) {
            hook.fishAngle = net.minecraft.util.Mth.nextFloat(hook.random, hook.minLureAngle, hook.maxLureAngle);
            hook.timeUntilLured = 0;
        }

        hook.timeUntilHooked = ticks;
    }

    @Override
    public void resetFishingState() {
        final FishingHook hook = this.getHandle();
        hook.resetTimeUntilLured();
        hook.timeUntilHooked = 0; // Reset time until hooked, will be repopulated once lured time is ticked down.
    }

    @Override
    public int retrieve(EquipmentSlot slot) {
        Preconditions.checkArgument(slot == EquipmentSlot.HAND || slot == EquipmentSlot.OFF_HAND, "Equipment slot must be HAND or OFF_HAND");
        final FishingHook fishingHook = getHandle();
        final Player playerOwner = fishingHook.getPlayerOwner();
        Preconditions.checkState(playerOwner != null, "Player owner cannot be null");

        final InteractionHand hand = CraftEquipmentSlot.getHand(slot);
        final ItemStack itemInHand = playerOwner.getItemInHand(hand);
        Preconditions.checkState(itemInHand.is(Items.FISHING_ROD), "Item in slot is not a FISHING_ROD");

        return fishingHook.retrieve(itemInHand, hand);
    }
}
