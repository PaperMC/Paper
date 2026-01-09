package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.Optional;
import net.kyori.adventure.util.TriState;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.attribute.EnvironmentAttributes;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.CraftLootTable;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftSound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.loot.LootTable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class CraftMob extends CraftLivingEntity implements Mob, io.papermc.paper.entity.PaperLeashable { // Paper - Leashable API

    private final com.destroystokyo.paper.entity.PaperPathfinder paperPathfinder; // Paper - Mob Pathfinding API

    public CraftMob(CraftServer server, net.minecraft.world.entity.Mob entity) {
        super(server, entity);
        this.paperPathfinder = new com.destroystokyo.paper.entity.PaperPathfinder(entity); // Paper - Mob Pathfinding API
    }

    @Override
    public net.minecraft.world.entity.Mob getHandle() {
        return (net.minecraft.world.entity.Mob) this.entity;
    }

    @Override
    public void setHandle(net.minecraft.world.entity.Entity entity) {
        super.setHandle(entity);
        this.paperPathfinder.setHandle(this.getHandle());
    }

    @Override
    public boolean shouldDespawnInPeaceful() {
        return this.getHandle().shouldDespawnInPeaceful();
    }

    @Override
    public void setDespawnInPeacefulOverride(final TriState state) {
        Preconditions.checkArgument(state != null, "TriState cannot be null");
        this.getHandle().despawnInPeacefulOverride = state;
    }

    @Override
    public TriState getDespawnInPeacefulOverride() {
        return this.getHandle().despawnInPeacefulOverride;
    }

    @Override
    public com.destroystokyo.paper.entity.Pathfinder getPathfinder() {
        return this.paperPathfinder;
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        Preconditions.checkState(!this.getHandle().generation, "Cannot set target during world generation");

        net.minecraft.world.entity.Mob entity = this.getHandle();
        if (target == null) {
            entity.setTarget(null, null);
        } else if (target instanceof CraftLivingEntity) {
            entity.setTarget(((CraftLivingEntity) target).getHandle(), null);
        }
    }

    @Override
    public @Nullable CraftLivingEntity getTarget() {
        if (this.getHandle().getTarget() == null) return null;

        return (CraftLivingEntity) this.getHandle().getTarget().getBukkitEntity();
    }

    @Override
    public void setAware(boolean aware) {
        this.getHandle().aware = aware;
    }

    @Override
    public boolean isAware() {
        return this.getHandle().aware;
    }

    @Override
    public @Nullable Sound getAmbientSound() {
        SoundEvent sound = this.getHandle().getAmbientSound();
        return (sound != null) ? CraftSound.minecraftToBukkit(sound) : null;
    }

    @Override
    public void setLootTable(@Nullable LootTable table) {
        this.getHandle().lootTable = Optional.ofNullable(CraftLootTable.bukkitToMinecraft(table));
    }

    @Override
    public @Nullable LootTable getLootTable() {
        return CraftLootTable.minecraftToBukkit(this.getHandle().getLootTable().orElse(null));
    }

    @Override
    public void setSeed(long seed) {
        this.getHandle().lootTableSeed = seed;
    }

    @Override
    public long getSeed() {
        return this.getHandle().lootTableSeed;
    }

    @Override
    public boolean isInDaylight() {
        return getHandle().isSunBurnTick();
    }

    @Override
    public void lookAt(Location location) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "location in a different world");
        getHandle().getLookControl().setLookAt(location.getX(), location.getY(), location.getZ());
    }

    @Override
    public void lookAt(Location location, float headRotationSpeed, float maxHeadPitch) {
        Preconditions.checkArgument(location != null, "location cannot be null");
        Preconditions.checkArgument(location.getWorld().equals(getWorld()), "location in a different world");
        getHandle().getLookControl().setLookAt(location.getX(), location.getY(), location.getZ(), headRotationSpeed, maxHeadPitch);
    }

    @Override
    public void lookAt(Entity entity) {
        Preconditions.checkArgument(entity != null, "entity cannot be null");
        Preconditions.checkArgument(entity.getWorld().equals(getWorld()), "entity in a different world");
        getHandle().getLookControl().setLookAt(((CraftEntity) entity).getHandle());
    }

    @Override
    public void lookAt(Entity entity, float headRotationSpeed, float maxHeadPitch) {
        Preconditions.checkArgument(entity != null, "entity cannot be null");
        Preconditions.checkArgument(entity.getWorld().equals(getWorld()), "entity in a different world");
        getHandle().getLookControl().setLookAt(((CraftEntity) entity).getHandle(), headRotationSpeed, maxHeadPitch);
    }

    @Override
    public void lookAt(double x, double y, double z) {
        getHandle().getLookControl().setLookAt(x, y, z);
    }

    @Override
    public void lookAt(double x, double y, double z, float headRotationSpeed, float maxHeadPitch) {
        getHandle().getLookControl().setLookAt(x, y, z, headRotationSpeed, maxHeadPitch);
    }

    @Override
    public int getHeadRotationSpeed() {
        return getHandle().getHeadRotSpeed();
    }

    @Override
    public int getMaxHeadPitch() {
        return getHandle().getMaxHeadXRot();
    }

    @Override
    public boolean isLeftHanded() {
        return getHandle().isLeftHanded();
    }

    @Override
    public void setLeftHanded(boolean leftHanded) {
        getHandle().setLeftHanded(leftHanded);
    }

    @Override
    public boolean isAggressive() {
        return this.getHandle().isAggressive();
    }

    @Override
    public void setAggressive(boolean aggressive) {
        this.getHandle().setAggressive(aggressive);
    }

    @Override
    public int getPossibleExperienceReward() {
        return getHandle().getExperienceReward((net.minecraft.server.level.ServerLevel) this.getHandle().level(), null);
    }

    @Override
    public boolean isLeashed() {
        return io.papermc.paper.entity.PaperLeashable.super.isLeashed();
    }

    @Override
    public org.bukkit.entity.Entity getLeashHolder() throws IllegalStateException {
        return io.papermc.paper.entity.PaperLeashable.super.getLeashHolder();
    }

    @Override
    public boolean setLeashHolder(final @Nullable Entity holder) {
        return io.papermc.paper.entity.PaperLeashable.super.setLeashHolder(holder);
    }

    @Override
    public boolean burnsInDaylight() {
        final net.minecraft.world.entity.Mob handle = this.getHandle();

        return handle.burnInDaylightOverride.toBooleanOrElse(handle.getType().is(EntityTypeTags.BURN_IN_DAYLIGHT))
            && handle.level().environmentAttributes().getValue(EnvironmentAttributes.MONSTERS_BURN, handle.position());
    }

    @Override
    public void setBurnInDaylightOverride(final TriState state) {
        Preconditions.checkArgument(state != null, "TriState cannot be null");
        this.getHandle().burnInDaylightOverride = state;
    }

    @Override
    public TriState getBurnInDaylightOverride() {
        return this.getHandle().burnInDaylightOverride;
    }
}
