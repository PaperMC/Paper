package org.bukkit.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import io.papermc.paper.world.damagesource.CombatTracker;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Block;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a living entity, such as a monster or player
 */
public interface LivingEntity extends Attributable, Damageable, ProjectileSource, io.papermc.paper.entity.Frictional { // Paper

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight();

    /**
     * Gets the height of the living entity's eyes above its Location.
     *
     * @param ignorePose if set to true, the effects of pose changes, eg
     *     sneaking and gliding will be ignored
     * @return height of the living entity's eyes above its location
     */
    public double getEyeHeight(boolean ignorePose);

    /**
     * Get a Location detailing the current eye position of the living entity.
     *
     * @return a location at the eyes of the living entity
     */
    @NotNull
    public Location getEyeLocation();

    /**
     * Gets all blocks along the living entity's line of sight.
     * <p>
     * This list contains all blocks from the living entity's eye position to
     * target inclusive. This method considers all blocks as 1x1x1 in size.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return list containing all blocks along the living entity's line of
     *     sight
     */
    @NotNull
    public List<Block> getLineOfSight(@Nullable Set<Material> transparent, int maxDistance);

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This method considers all blocks as 1x1x1 in size. To take exact block
     * collision shapes into account, see {@link #getTargetBlockExact(int,
     * FluidCollisionMode)}.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan (may be limited
     *     by server by at least 100 blocks, no less)
     * @return block that the living entity has targeted
     */
    @NotNull
    public Block getTargetBlock(@Nullable Set<Material> transparent, int maxDistance);

    // Paper start
    /**
     * Gets the block that the living entity has targeted, ignoring fluids
     *
     * @param maxDistance this is the maximum distance to scan
     * @return block that the living entity has targeted,
     *     or null if no block is within maxDistance
     * @deprecated use {@link #getTargetBlockExact(int)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public default Block getTargetBlock(int maxDistance) {
        return getTargetBlock(maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode.NEVER);
    }

    /**
     * Gets the block that the living entity has targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param fluidMode whether to check fluids or not
     * @return block that the living entity has targeted,
     *     or null if no block is within maxDistance
     * @deprecated use {@link #getTargetBlockExact(int, FluidCollisionMode)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public Block getTargetBlock(int maxDistance, @NotNull com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode);

    /**
     * Gets the blockface of that block that the living entity has targeted, ignoring fluids
     *
     * @param maxDistance this is the maximum distance to scan
     * @return blockface of the block that the living entity has targeted,
     *     or null if no block is targeted
     */
    @Nullable
    public default org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance) {
        return getTargetBlockFace(maxDistance, org.bukkit.FluidCollisionMode.NEVER);
    }

    /**
     * Gets the blockface of that block that the living entity has targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param fluidMode whether to check fluids or not
     * @return blockface of the block that the living entity has targeted,
     *     or null if no block is targeted
     * @deprecated use {@link #getTargetBlockFace(int, FluidCollisionMode)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, @NotNull com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode);

    /**
     * Gets the blockface of that block that the living entity has targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param fluidMode whether to check fluids or not
     * @return blockface of the block that the living entity has targeted,
     *     or null if no block is targeted
     */
    @Nullable
    public org.bukkit.block.BlockFace getTargetBlockFace(int maxDistance, @NotNull FluidCollisionMode fluidMode);

    /**
     * Gets information about the block the living entity has targeted, ignoring fluids
     *
     * @param maxDistance this is the maximum distance to scan
     * @return TargetBlockInfo about the block the living entity has targeted,
     *     or null if no block is targeted
     * @deprecated use {@link #rayTraceBlocks(double)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public default com.destroystokyo.paper.block.TargetBlockInfo getTargetBlockInfo(int maxDistance) {
        return getTargetBlockInfo(maxDistance, com.destroystokyo.paper.block.TargetBlockInfo.FluidMode.NEVER);
    }

    /**
     * Gets information about the block the living entity has targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param fluidMode whether to check fluids or not
     * @return TargetBlockInfo about the block the living entity has targeted,
     *     or null if no block is targeted
     * @deprecated use {@link #rayTraceBlocks(double, FluidCollisionMode)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public com.destroystokyo.paper.block.TargetBlockInfo getTargetBlockInfo(int maxDistance, @NotNull com.destroystokyo.paper.block.TargetBlockInfo.FluidMode fluidMode);

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @return entity being targeted, or null if no entity is targeted
     */
    @Nullable
    public default Entity getTargetEntity(int maxDistance) {
        return getTargetEntity(maxDistance, false);
    }

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param ignoreBlocks true to scan through blocks
     * @return entity being targeted, or null if no entity is targeted
     */
    @Nullable
    public Entity getTargetEntity(int maxDistance, boolean ignoreBlocks);

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @return TargetEntityInfo about the entity being targeted,
     *     or null if no entity is targeted
     * @deprecated use {@link #rayTraceEntities(int)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public default com.destroystokyo.paper.entity.TargetEntityInfo getTargetEntityInfo(int maxDistance) {
        return getTargetEntityInfo(maxDistance, false);
    }

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @return RayTraceResult about the entity being targeted,
     *     or null if no entity is targeted
     */
    @Nullable
    default RayTraceResult rayTraceEntities(int maxDistance) {
        return this.rayTraceEntities(maxDistance, false);
    }

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param ignoreBlocks true to scan through blocks
     * @return TargetEntityInfo about the entity being targeted,
     *     or null if no entity is targeted
     * @deprecated use {@link #rayTraceEntities(int, boolean)}
     */
    @Deprecated(forRemoval = true, since = "1.19.3")
    @Nullable
    public com.destroystokyo.paper.entity.TargetEntityInfo getTargetEntityInfo(int maxDistance, boolean ignoreBlocks);

    /**
     * Gets information about the entity being targeted
     *
     * @param maxDistance this is the maximum distance to scan
     * @param ignoreBlocks true to scan through blocks
     * @return RayTraceResult about the entity being targeted,
     *     or null if no entity is targeted
     */
    @Nullable
    RayTraceResult rayTraceEntities(int maxDistance, boolean ignoreBlocks);
    // Paper end

    /**
     * Gets the last two blocks along the living entity's line of sight.
     * <p>
     * The target block will be the last block in the list. This method
     * considers all blocks as 1x1x1 in size.
     *
     * @param transparent Set containing all transparent block Materials (set to
     *     null for only air)
     * @param maxDistance this is the maximum distance to scan. This may be
     *     further limited by the server, but never to less than 100 blocks
     * @return list containing the last 2 blocks along the living entity's
     *     line of sight
     */
    @NotNull
    public List<Block> getLastTwoTargetBlocks(@Nullable Set<Material> transparent, int maxDistance);

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return block that the living entity has targeted
     * @see #getTargetBlockExact(int, org.bukkit.FluidCollisionMode)
     */
    @Nullable
    default Block getTargetBlockExact(int maxDistance) {
        return this.getTargetBlockExact(maxDistance, FluidCollisionMode.NEVER);
    }

    /**
     * Gets the block that the living entity has targeted.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return block that the living entity has targeted
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Nullable
    public Block getTargetBlockExact(int maxDistance, @NotNull FluidCollisionMode fluidCollisionMode);

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account. Fluids are
     * ignored.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @return information on the targeted block, or <code>null</code> if there
     *     is no targeted block in range
     * @see #rayTraceBlocks(double, FluidCollisionMode)
     */
    @Nullable
    default RayTraceResult rayTraceBlocks(double maxDistance) {
        return this.rayTraceBlocks(maxDistance, FluidCollisionMode.NEVER);
    }

    /**
     * Performs a ray trace that provides information on the targeted block.
     * <p>
     * This takes the blocks' precise collision shapes into account.
     * <p>
     * This may cause loading of chunks! Some implementations may impose
     * artificial restrictions on the maximum distance.
     *
     * @param maxDistance the maximum distance to scan
     * @param fluidCollisionMode the fluid collision mode
     * @return information on the targeted block, or <code>null</code> if there
     *     is no targeted block in range
     * @see World#rayTraceBlocks(Location, Vector, double, FluidCollisionMode)
     */
    @Nullable
    public RayTraceResult rayTraceBlocks(double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode);

    /**
     * Returns the amount of air that the living entity has remaining, in
     * ticks.
     *
     * @return amount of air remaining
     */
    public int getRemainingAir();

    /**
     * Sets the amount of air that the living entity has remaining, in ticks.
     *
     * @param ticks amount of air remaining
     */
    public void setRemainingAir(int ticks);

    /**
     * Returns the maximum amount of air the living entity can have, in ticks.
     *
     * @return maximum amount of air
     */
    public int getMaximumAir();

    /**
     * Sets the maximum amount of air the living entity can have, in ticks.
     *
     * @param ticks maximum amount of air
     */
    public void setMaximumAir(int ticks);

    /**
     * Gets the item that the player is using (eating food, drawing back a bow,
     * blocking, etc.)
     *
     * @return the item being used by the player, or null if they are not using
     * an item
     * @deprecated Use {@link #getActiveItem()}
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "1.20.4") // Paper
    public ItemStack getItemInUse();

    /**
     * Gets the number of ticks remaining for the current item's usage.
     *
     * @return The number of ticks remaining
     * @deprecated use {@link #getActiveItemRemainingTime()}
     */
    @Deprecated(forRemoval = true, since = "1.20.4") // Paper
    public int getItemInUseTicks();

    /**
     * Sets the number of ticks that remain for the current item's usage.
     * Applies to items that take time to use, like eating food, drawing a bow,
     * or throwing a trident.
     *
     * @param ticks The number of ticks remaining
     * @deprecated use {@link #setActiveItemRemainingTime(int)}
     */
    @Deprecated(forRemoval = true, since = "1.20.4") // Paper
    public void setItemInUseTicks(int ticks);

    /**
     * Gets the time in ticks until the next arrow leaves the entity's body.
     *
     * @return ticks until arrow leaves
     */
    public @NonNegative int getArrowCooldown();

    /**
     * Sets the time in ticks until the next arrow leaves the entity's body.
     * <p>
     * A value of 0 will cause the server to re-calculate the time on the next tick.
     *
     * @param ticks time until arrow leaves
     */
    public void setArrowCooldown(@NonNegative int ticks);

    /**
     * Gets the amount of arrows in an entity's body.
     *
     * @return amount of arrows in body
     */
    public @NonNegative int getArrowsInBody();

    /**
     * Set the amount of arrows in the entity's body.
     * <p>
     * Does not fire the {@link org.bukkit.event.entity.ArrowBodyCountChangeEvent}.
     *
     * @param count amount of arrows in entity's body
     */
    default void setArrowsInBody(final @NonNegative int count) {
        this.setArrowsInBody(count, false);
    }

    /**
     * Set the amount of arrows in the entity's body.
     *
     * @param count amount of arrows in entity's body
     * @param fireEvent whether to fire the {@link org.bukkit.event.entity.ArrowBodyCountChangeEvent} event
     */
    void setArrowsInBody(@NonNegative int count, boolean fireEvent); // Paper

    /**
     * Sets the amount of ticks before the next arrow gets removed from the entities body.
     * <p>
     * A value of 0 will cause the server to re-calculate the amount of ticks on the next tick.
     *
     * @param ticks Amount of ticks
     * @deprecated use {@link #setArrowCooldown(int)}
     */
    @Deprecated(since = "1.21.10")
    default void setNextArrowRemoval(@NonNegative int ticks) {
        this.setArrowCooldown(ticks);
    }

    /**
     * Gets the amount of ticks before the next arrow gets removed from the entities body.
     *
     * @return ticks Amount of ticks
     * @deprecated use {@link #getArrowCooldown()}
     */
    @Deprecated(since = "1.21.10")
    default @NonNegative int getNextArrowRemoval() {
        return this.getArrowCooldown();
    }

    /**
     * Gets the time in ticks until the next bee stinger leaves the entity's body.
     *
     * @return ticks until bee stinger leaves
     */
    public @NonNegative int getBeeStingerCooldown();

    /**
     * Sets the time in ticks until the next stinger leaves the entity's body.
     * <p>
     * A value of 0 will cause the server to re-calculate the time on the next tick.
     *
     * @param ticks time until bee stinger leaves
     */
    public void setBeeStingerCooldown(@NonNegative int ticks);

    /**
     * Gets the amount of bee stingers in an entity's body.
     *
     * @return amount of bee stingers in body
     */
    public @NonNegative int getBeeStingersInBody();

    /**
     * Set the amount of bee stingers in the entity's body.
     *
     * @param count amount of bee stingers in entity's body
     */
    public void setBeeStingersInBody(@NonNegative int count);

    /**
     * Sets the amount of ticks before the next bee stinger gets removed from the entities body.
     * <p>
     * A value of 0 will cause the server to re-calculate the amount of ticks on the next tick.
     *
     * @param ticks Amount of ticks
     * @deprecated use {@link #setBeeStingerCooldown(int)}
     */
    @Deprecated(since = "1.21.10")
    default void setNextBeeStingerRemoval(@NonNegative int ticks) {
        this.setBeeStingerCooldown(ticks);
    }

    /**
     * Gets the amount of ticks before the next bee stinger gets removed from the entities body.
     *
     * @return ticks Amount of ticks
     * @deprecated use {@link #getBeeStingerCooldown()}
     */
    @Deprecated(since = "1.21.10")
    default @NonNegative int getNextBeeStingerRemoval() {
        return this.getBeeStingerCooldown();
    }

    /**
     * Returns the living entity's current maximum no damage ticks.
     * <p>
     * This is the maximum duration in which the living entity will not take
     * damage.
     *
     * @return maximum no damage ticks
     */
    public int getMaximumNoDamageTicks();

    /**
     * Sets the living entity's current maximum no damage ticks.
     *
     * @param ticks maximum amount of no damage ticks
     */
    public void setMaximumNoDamageTicks(int ticks);

    /**
     * Returns the living entity's last damage taken in the current no damage
     * ticks time.
     * <p>
     * Only damage higher than this amount will further damage the living
     * entity.
     *
     * @return damage taken since the last no damage ticks time period
     */
    public double getLastDamage();

    /**
     * Sets the damage dealt within the current no damage ticks time period.
     *
     * @param damage amount of damage
     */
    public void setLastDamage(double damage);

    /**
     * Returns the living entity's current no damage ticks.
     *
     * @return amount of no damage ticks
     */
    public int getNoDamageTicks();

    /**
     * Sets the living entity's current no damage ticks.
     *
     * @param ticks amount of no damage ticks
     */
    public void setNoDamageTicks(int ticks);

    /**
     * Get the ticks that this entity has performed no action.
     * <p>
     * The details of what "no action ticks" entails varies from entity to entity
     * and cannot be specifically defined. Some examples include squid using this
     * value to determine when to swim, raiders for when they are to be expelled
     * from raids, or creatures (such as withers) as a requirement to be despawned.
     *
     * @return amount of no action ticks
     */
    public int getNoActionTicks();

    /**
     * Set the ticks that this entity has performed no action.
     * <p>
     * The details of what "no action ticks" entails varies from entity to entity
     * and cannot be specifically defined. Some examples include squid using this
     * value to determine when to swim, raiders for when they are to be expelled
     * from raids, or creatures (such as withers) as a requirement to be despawned.
     *
     * @param ticks amount of no action ticks
     */
    public void setNoActionTicks(int ticks);

    /**
     * Gets the player identified as the killer of the living entity.
     * <p>
     * May be null.
     *
     * @return killer player, or null if none found
     */
    @Nullable
    public Player getKiller();

    // Paper start
    /**
     * Sets the player identified as the killer of the living entity.
     *
     * @param killer player
     */
    public void setKiller(@Nullable Player killer);
    // Paper end

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Note: {@link PotionEffect#getHiddenPotionEffect()} is ignored when
     * adding the effect to the entity.
     *
     * @param effect PotionEffect to be added
     * @return whether the effect could be added
     */
    default boolean addPotionEffect(@NotNull PotionEffect effect) {
        return this.addPotionEffect(effect, false);
    }

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Only one potion effect can be present for a given {@link PotionEffectType}.
     *
     * @param effect PotionEffect to be added
     * @param force whether conflicting effects should be removed
     * @return whether the effect could be added
     * @deprecated no need to force since multiple effects of the same type are
     * now supported.
     */
    @Deprecated(since = "1.15.2")
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force);

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living
     * entity.
     * <p>
     * Note: {@link PotionEffect#getHiddenPotionEffect()} is ignored when
     * adding the effect to the entity.
     *
     * @param effects the effects to add
     * @return whether all of the effects could be added
     */
    public boolean addPotionEffects(@NotNull Collection<PotionEffect> effects);

    /**
     * Returns whether the living entity already has an existing effect of
     * the given {@link PotionEffectType} applied to it.
     *
     * @param type the potion type to check
     * @return whether the living entity has this potion effect active on them
     */
    public boolean hasPotionEffect(@NotNull PotionEffectType type);

    /**
     * Returns the active {@link PotionEffect} of the specified type.
     * <p>
     * If the effect is not present on the entity then null will be returned.
     *
     * @param type the potion type to check
     * @return the effect active on this entity, or null if not active.
     */
    @Nullable
    public PotionEffect getPotionEffect(@NotNull PotionEffectType type);

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     *
     * @param type the potion type to remove
     */
    public void removePotionEffect(@NotNull PotionEffectType type);

    /**
     * Returns all currently active {@link PotionEffect}s on the living
     * entity.
     *
     * @return a collection of {@link PotionEffect}s
     */
    @NotNull
    public Collection<PotionEffect> getActivePotionEffects();

    /**
     * Removes all active potion effects for this entity.
     *
     * @return true if any were removed
     */
    boolean clearActivePotionEffects();

    /**
     * Checks whether the living entity has block line of sight to another.
     * <p>
     * This uses the same algorithm that hostile mobs use to find the closest
     * player.
     *
     * @param other the entity to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(@NotNull Entity other);

    /**
     * Checks whether the living entity has block line of sight to the given block.
     * <p>
     * This uses the same algorithm that hostile mobs use to find the closest
     * player.
     *
     * @param location the location to determine line of sight to
     * @return true if there is a line of sight, false if not
     */
    public boolean hasLineOfSight(@NotNull Location location);

    /**
     * Returns if the living entity despawns when away from players or not.
     * <p>
     * By default, animals are not removed while other mobs are.
     *
     * @return true if the living entity is removed when away from players
     */
    public boolean getRemoveWhenFarAway();

    /**
     * Sets whether or not the living entity despawns when away from players
     * or not.
     *
     * @param remove the removal status
     */
    public void setRemoveWhenFarAway(boolean remove);

    /**
     * Gets the inventory with the equipment worn by the living entity.
     *
     * @return the living entity's inventory
     */
    @Nullable
    public EntityEquipment getEquipment();

    /**
     * Sets whether or not the living entity can pick up items.
     *
     * @param pickup whether or not the living entity can pick up items
     */
    public void setCanPickupItems(boolean pickup);

    /**
     * Gets if the living entity can pick up items.
     *
     * @return whether or not the living entity can pick up items
     */
    public boolean getCanPickupItems();

    /**
     * Returns whether the entity is currently leashed.
     *
     * @return whether the entity is leashed
     */
    public boolean isLeashed();

    /**
     * Gets the entity that is currently leading this entity.
     *
     * @return the entity holding the leash
     * @throws IllegalStateException if not currently leashed
     */
    @NotNull
    public Entity getLeashHolder() throws IllegalStateException;

    /**
     * Sets the leash on this entity to be held by the supplied entity.
     * <p>
     * This method has no effect on players.
     * Non-living entities excluding leashes will not persist as leash
     * holders.
     *
     * @param holder the entity to leash this entity to, or null to unleash
     * @return whether the operation was successful
     */
    public boolean setLeashHolder(@Nullable Entity holder);

    /**
     * Checks to see if an entity is gliding, such as using an Elytra.
     * @return True if this entity is gliding.
     */
    public boolean isGliding();

    /**
     * Makes entity start or stop gliding. This will work even if an Elytra
     * is not equipped, but will be reverted by the server immediately after
     * unless an event-cancelling mechanism is put in place.
     * @param gliding True if the entity is gliding.
     */
    public void setGliding(boolean gliding);

    /**
     * Checks to see if an entity is swimming.
     *
     * @return True if this entity is swimming.
     */
    public boolean isSwimming();

    /**
     * Makes entity start or stop swimming.
     *
     * This may have unexpected results if the entity is not in water.
     *
     * @param swimming True if the entity is swimming.
     * @deprecated This does nothing and is immediately reverted by the server, in the next tick <!-- Paper - future note: should wait a mojang input client/server side -->
     */
    @Deprecated // Paper
    public void setSwimming(boolean swimming);

    /**
     * Checks to see if an entity is currently riptiding.
     *
     * @return True if this entity is currently riptiding.
     */
    public boolean isRiptiding();

    /**
     * Makes entity start or stop riptiding.
     * <p>
     * Note: This does not damage attackable entities.
     *
     * @param riptiding whether the entity should start riptiding.
     * @see HumanEntity#startRiptideAttack(int, float, ItemStack)
     */
    public void setRiptiding(boolean riptiding);

    /**
     * Returns whether this entity is slumbering.
     *
     * @return slumber state
     */
    public boolean isSleeping();

    /**
     * Gets if the entity is climbing.
     *
     * @return if the entity is climbing
     */
    public boolean isClimbing();

    /**
     * Sets whether an entity will have AI.
     *
     * The entity will be completely unable to move if it has no AI.
     *
     * @param ai whether the mob will have AI or not.
     */
    void setAI(boolean ai);

    /**
     * Checks whether an entity has AI.
     *
     * The entity will be completely unable to move if it has no AI.
     *
     * @return true if the entity has AI, otherwise false.
     */
    boolean hasAI();

    /**
     * Makes this entity attack the given entity with a melee attack.
     *
     * Attack damage is calculated by the server from the attributes and
     * equipment of this mob, and knockback is applied to {@code target} as
     * appropriate.
     *
     * @param target entity to attack.
     */
    public void attack(@NotNull Entity target);

    /**
     * Makes this entity swing their main hand.
     *
     * This method does nothing if this entity does not have an animation for
     * swinging their main hand.
     */
    public void swingMainHand();

    /**
     * Makes this entity swing their off hand.
     *
     * This method does nothing if this entity does not have an animation for
     * swinging their off hand.
     */
    public void swingOffHand();

    /**
     * Makes this entity flash red as if they were damaged.
     *
     * @param yaw The direction the damage is coming from in relation to the
     * entity, where 0 is in front of the player, 90 is to the right, 180 is
     * behind, and 270 is to the left
     */
    public void playHurtAnimation(float yaw);

    /**
     * Set if this entity will be subject to collisions with other entities.
     * <p>
     * Exemptions to this rule can be managed with
     * {@link #getCollidableExemptions()}
     * <p>
     * Note that the client may predict the collision between itself and another
     * entity, resulting in this flag not working for player collisions. This
     * method should therefore only be used to set the collision status of
     * non-player entities.
     * <p>
     * To control player collisions, use {@link Team.Option#COLLISION_RULE} in
     * combination with a {@link Scoreboard} and a {@link Team}.
     *
     * @param collidable collision status
     */
    void setCollidable(boolean collidable);

    /**
     * Gets if this entity is subject to collisions with other entities.
     * <p>
     * Some entities might be exempted from the collidable rule of this entity.
     * Use {@link #getCollidableExemptions()} to get these.
     * <p>
     * Please note that this method returns only the custom collidable state,
     * not whether the entity is non-collidable for other reasons such as being
     * dead.
     * <p>
     * Note that the client may predict the collision between itself and another
     * entity, resulting in this flag not being accurate for player collisions.
     * This method should therefore only be used to check the collision status
     * of non-player entities.
     * <p>
     * To check the collision behavior for a player, use
     * {@link Team.Option#COLLISION_RULE} in combination with a
     * {@link Scoreboard} and a {@link Team}.
     *
     * @return collision status
     */
    boolean isCollidable();

    /**
     * Gets a mutable set of UUIDs of the entities which are exempt from the
     * entity's collidable rule and which's collision with this entity will
     * behave the opposite of it.
     * <p>
     * This set can be modified to add or remove exemptions.
     * <p>
     * For example if collidable is true and an entity is in the exemptions set
     * then it will not collide with it. Similarly if collidable is false and an
     * entity is in this set then it will still collide with it.
     * <p>
     * Note these exemptions are not (currently) persistent.
     * <p>
     * Note that the client may predict the collision between itself and another
     * entity, resulting in those exemptions not being accurate for player
     * collisions. This method should therefore only be used to exempt
     * non-player entities.
     * <p>
     * To exempt collisions for a player, use {@link Team.Option#COLLISION_RULE}
     * in combination with a {@link Scoreboard} and a {@link Team}.
     *
     * @return the collidable exemption set
     */
    @NotNull
    Set<UUID> getCollidableExemptions();

    /**
     * Returns the value of the memory specified.
     * <p>
     * Note that the value is null when the specific entity does not have that
     * value by default.
     *
     * @param memoryKey memory to access
     * @param <T> the type of the return value
     * @return an instance of the memory section value or null if not present
     */
    @Nullable
    <T> T getMemory(@NotNull MemoryKey<T> memoryKey);

    /**
     * Sets the value of the memory specified.
     * <p>
     * Note that the value will not be persisted when the specific entity does
     * not have that value by default.
     *
     * @param memoryKey the memory to access
     * @param memoryValue a typed memory value
     * @param <T> the type of the passed value
     */
    <T> void setMemory(@NotNull MemoryKey<T> memoryKey, @Nullable T memoryValue);

    /**
     * Get the {@link Sound} this entity will make when damaged.
     *
     * @return the hurt sound, or null if the entity does not make any sound
     */
    @Nullable
    public Sound getHurtSound();

    /**
     * Get the {@link Sound} this entity will make on death.
     *
     * @return the death sound, or null if the entity does not make any sound
     */
    @Nullable
    public Sound getDeathSound();

    /**
     * Get the {@link Sound} this entity will make when falling from the given
     * height (in blocks). The sound will often differ between either a small
     * or a big fall damage sound if the height exceeds 4 blocks.
     *
     * @param fallHeight the fall height in blocks
     * @return the fall damage sound
     * @see #getFallDamageSoundSmall()
     * @see #getFallDamageSoundBig()
     */
    @NotNull
    public Sound getFallDamageSound(int fallHeight);

    /**
     * Get the {@link Sound} this entity will make when falling from a small
     * height.
     *
     * @return the fall damage sound
     */
    @NotNull
    public Sound getFallDamageSoundSmall();

    /**
     * Get the {@link Sound} this entity will make when falling from a large
     * height.
     *
     * @return the fall damage sound
     */
    @NotNull
    public Sound getFallDamageSoundBig();

    /**
     * Get the {@link Sound} this entity will make when drinking the given
     * {@link ItemStack}.
     *
     * @param itemStack the item stack being drank
     * @return the drinking sound
     */
    @NotNull
    public Sound getDrinkingSound(@NotNull ItemStack itemStack);

    /**
     * Get the {@link Sound} this entity will make when eating the given
     * {@link ItemStack}.
     *
     * @param itemStack the item stack being eaten
     * @return the eating sound
     */
    @NotNull
    public Sound getEatingSound(@NotNull ItemStack itemStack);

    /**
     * Returns true if this entity can breathe underwater and will not take
     * suffocation damage when its air supply reaches zero.
     *
     * @return <code>true</code> if the entity can breathe underwater
     */
    public boolean canBreatheUnderwater();

    /**
     * Get the category to which this entity belongs.
     *
     * Categories may subject this entity to additional effects, benefits or
     * debuffs.
     *
     * @return the entity category
     * @deprecated entity groupings are now managed by tags, not categories
     */
    @NotNull
    @Deprecated(since = "1.20.5", forRemoval = true) @org.jetbrains.annotations.Contract("-> fail") // Paper
    public EntityCategory getCategory();

    /**
     * Get the number of arrows stuck in this entity
     *
     * @return Number of arrows stuck
     * @deprecated use {@link #getArrowsInBody()}
     */
    @Deprecated
    default @NonNegative int getArrowsStuck() {
        return this.getArrowsInBody();
    }

    /**
     * Set the number of arrows stuck in this entity
     *
     * @param arrows Number of arrows to stick in this entity
     * @deprecated use {@link #setArrowsInBody(int, boolean)}. <b>This method previously fired {@link org.bukkit.event.entity.ArrowBodyCountChangeEvent} so if
     * you want to retain exact functionality, pass {@code true} for {@code fireEvent}.</b>
     */
    @Deprecated
    default void setArrowsStuck(@NonNegative int arrows) {
        this.setArrowsInBody(arrows, true);
    }

    /**
     * Get the delay (in ticks) before blocking is effective for this entity
     *
     * @return Delay in ticks
     * @deprecated no longer applicable, check the blocks_attacks component on the shield
     */
    @Deprecated(since = "1.21.9")
    default int getShieldBlockingDelay() {
        return 5;
    }

    /**
     * Set the delay (in ticks) before blocking is effective for this entity
     *
     * @param delay Delay in ticks
     * @deprecated no longer applicable, use the blocks_attacks component on the shield
     */
    @Deprecated(since = "1.21.9")
    default void setShieldBlockingDelay(int delay) {
    }

    /**
     * Retrieves the sideways movement direction of the entity.
     * <p>
     * The returned value ranges from -1 to 1, where:
     * <ul>
     *     <li>Positive 1 represents movement to the left.</li>
     *     <li>Negative 1 represents movement to the right.</li>
     * </ul>
     *
     * Please note that for entities of type {@link Player}, this value will only return whole numbers depending
     * on what keys are held, see {@link Player#getCurrentInput()}.
     * <p>
     * This method specifically provides information about the entity's sideways movement, whereas {@link #getVelocity()} returns
     * a vector representing the entity's overall current momentum.
     *
     * @return Sideways movement direction, ranging from -1 (right) to 1 (left).
     */
    float getSidewaysMovement();

    /**
     * Retrieves the upwards movement direction of the entity.
     * <p>
     * The returned value ranges from -1 to 1, where:
     * <ul>
     *     <li>Positive 1 represents upward movement.</li>
     *     <li>Negative 1 represents downward movement.</li>
     * </ul>
     *
     * Please note that for entities of type {@link Player}, this value is never updated.
     * <p>
     * This method specifically provides information about the entity's vertical movement,
     * whereas {@link #getVelocity()} returns a vector representing the entity's overall
     * current momentum.
     *
     * @return Upwards movement direction, ranging from -1 (downward) to 1 (upward).
     */
    float getUpwardsMovement();

    /**
     * Retrieves the forwards movement direction of the entity.
     * <p>
     * The returned value ranges from -1 to 1, where:
     * <ul>
     *     <li>Positive 1 represents movement forwards.</li>
     *     <li>Negative 1 represents movement backwards.</li>
     * </ul>
     *
     * Please note that for entities of type {@link Player}, this value will only return whole numbers depending
     * on what keys are held, see {@link Player#getCurrentInput()}.
     * <p>
     * This method specifically provides information about the entity's forward and backward movement,
     * whereas {@link #getVelocity()} returns a vector representing the entity's overall current momentum.
     *
     * @return Forwards movement direction, ranging from -1 (backward) to 1 (forward).
     */
    float getForwardsMovement();

    // Paper start - active item API
    /**
     * Starts using the item in the specified hand, making it the
     * currently active item. When, for example, called on a skeleton,
     * this will cause it to start drawing its bow.
     * <p>
     * Only HAND or OFF_HAND may be used for the hand parameter.
     * <p>
     * When used on a player, the client will stop using the item
     * if right click is held down.
     * <p>
     * This method does not make any guarantees about the effect of this method
     * as such depends on the entity and its state.
     *
     * @param hand the hand that contains the item to be used
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    void startUsingItem(@NotNull org.bukkit.inventory.EquipmentSlot hand);

    /**
     * Finishes using the currently active item. When, for example, a
     * skeleton is drawing its bow, this will cause it to release and
     * fire the arrow.
     * <p>
     * This method does not make any guarantees about the effect of this method
     * as such depends on the entity and its state.
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    void completeUsingActiveItem();

    /**
     * Gets the item being actively "used" or consumed.
     *
     * @return the item
     */
    org.bukkit.inventory.@NotNull ItemStack getActiveItem();

    /**
     * Interrupts any ongoing active "usage" or consumption or an item.
     */
    void clearActiveItem();

    /**
     * Gets the remaining number of ticks for {@link #getActiveItem()}'s usage.
     *
     * @return remaining ticks to use {@link #getActiveItem()}
     */
    int getActiveItemRemainingTime();

    /**
     * Sets the number of ticks that remain for {@link #getActiveItem()}'s
     * usage.
     * <p>
     * Valid values are between 0 and the max item use duration for
     * the specific item type.
     *
     * @param ticks time in ticks remaining
     */
    void setActiveItemRemainingTime(@org.jetbrains.annotations.Range(from = 0, to = Integer.MAX_VALUE) int ticks);

    /**
     * Gets if the entity is using an item (eating, drinking, etc).
     *
     * @return true if using an item
     */
    boolean hasActiveItem();

    /**
     * Get how long the {@link #getActiveItem()} has been in use for.
     *
     * @return time used in ticks
     */
    int getActiveItemUsedTime();

    /**
     * Get the hand using the active item. Will be either
     * {@link org.bukkit.inventory.EquipmentSlot#HAND} or
     * {@link org.bukkit.inventory.EquipmentSlot#OFF_HAND}.
     *
     * @return the hand being used
     */
    org.bukkit.inventory.@NotNull EquipmentSlot getActiveItemHand();

    /**
     * Gets remaining time a player needs to keep hands raised with an item to finish using it.
     *
     * @return remaining ticks to use the item
     * @see #getActiveItemRemainingTime()
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4")
    default int getItemUseRemainingTime() {
        return this.getActiveItemRemainingTime();
    }

    /**
     * Get how long the entity's hands have been raised (Charging Bow attack, using a potion, etc)
     *
     * @return Get how long the players hands have been raised (Charging Bow attack, using a potion, etc)
     * @see #getActiveItemUsedTime()
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4")
    default int getHandRaisedTime() {
        return this.getActiveItemUsedTime();
    }

    /**
     * Whether this entity is using or charging an attack (Bow pulled back, drinking potion, eating food)
     *
     * @return whether this entity is using or charging an attack (Bow pulled back, drinking potion, eating food)
     * @see #hasActiveItem()
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4")
    default boolean isHandRaised() {
        return this.hasActiveItem();
    }

    /**
     * Gets the hand raised by this living entity. Will be either
     * {@link org.bukkit.inventory.EquipmentSlot#HAND} or
     * {@link org.bukkit.inventory.EquipmentSlot#OFF_HAND}.
     *
     * @return the hand raised
     * @see #getActiveItemHand()
     */
    @NotNull
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4")
    default org.bukkit.inventory.EquipmentSlot getHandRaised() {
        return this.getActiveItemHand();
    }
    // Paper end - active item API

    // Paper start - entity jump API
    /**
     * Get entity jump state.
     * <p>
     * Jump state will be true when the entity has been marked to jump.
     *
     * @return entity jump state.
     */
    boolean isJumping();

    /**
     * Set entity jump state
     * <p>
     * Setting to true will mark the entity to jump.
     * <p>
     * Setting to false will unmark the entity to jump but will not stop a jump already in-progress.
     *
     * @param jumping entity jump state
     */
    void setJumping(boolean jumping);
    // Paper end - entity jump API

    // Paper start - pickup animation API
    /**
     * Plays pickup item animation towards this entity.
     * <p>
     * <b>This will remove the item on the client.</b>
     * <p>
     * Quantity is inferred to be that of the {@link Item}.
     *
     * @param item item to pickup
     */
    default void playPickupItemAnimation(@NotNull Item item) {
        playPickupItemAnimation(item, item.getItemStack().getAmount());
    }

    /**
     * Plays pickup item animation towards this entity.
     * <p>
     * <b>This will remove the item on the client.</b>
     *
     * @param item item to pickup
     * @param quantity quantity of item
     */
    void playPickupItemAnimation(@NotNull Item item, int quantity);
    // Paper end - pickup animation API

    // Paper start - hurt direction API
    /**
     * Gets player hurt direction
     *
     * @return hurt direction
     */
    float getHurtDirection();

    /**
     * Sets player hurt direction
     *
     * @param hurtDirection hurt direction
     * @deprecated use {@link Player#setHurtDirection(float)}
     */
    @Deprecated
    void setHurtDirection(float hurtDirection);
    // Paper end - hurt direction API

    // Paper start - swing hand API
    /**
     * Makes this entity swing their hand.
     *
     * <p>This method does nothing if this entity does not
     * have an animation for swinging their hand.
     *
     * @param hand hand to be swung, either {@link org.bukkit.inventory.EquipmentSlot#HAND} or {@link org.bukkit.inventory.EquipmentSlot#OFF_HAND}
     * @throws IllegalArgumentException if invalid hand is passed
     */
    default void swingHand(@NotNull org.bukkit.inventory.EquipmentSlot hand) {
        com.google.common.base.Preconditions.checkArgument(hand != null && hand.isHand(), String.format("Expected a valid hand, got \"%s\" instead!", hand));
        if (hand == org.bukkit.inventory.EquipmentSlot.HAND) {
            this.swingMainHand();
        } else {
            this.swingOffHand();
        }
    }
    // Paper end - swing hand API

    // Paper start - knockback API
    /**
     * Knocks back this entity from a specific direction with a specified strength. Mechanics such
     * as knockback resistance will be factored in.
     *
     * The direction specified in this method will be the direction of the source of the knockback,
     * so the entity will be pushed in the opposite direction.
     * @param strength The strength of the knockback. Must be greater than 0.
     * @param directionX The relative x position of the knockback source direction
     * @param directionZ The relative z position of the knockback source direction
     */
    void knockback(double strength, double directionX, double directionZ);
    // Paper end - knockback API

    // Paper start - ItemStack damage API
    /**
     * Notifies all clients tracking this entity that the item in
     * the slot of this entity broke.
     * <p>
     * NOTE: this does not mutate any entity state
     *
     * @param slot the slot
     */
    void broadcastSlotBreak(org.bukkit.inventory.@NotNull EquipmentSlot slot);

    /**
     * Notifies specified players that the item in the slot
     * of this entity broke.
     * <p>
     * NOTE: this does not mutate any entity state
     *
     * @param slot the slot
     * @param players the players to notify
     */
    void broadcastSlotBreak(org.bukkit.inventory.@NotNull EquipmentSlot slot, @NotNull Collection<Player> players);

    /**
     * Damages the itemstack in this slot by the specified amount.
     * <p>
     * This runs all logic associated with damaging an itemstack like
     * gamemode and enchantment checks, events, stat changes, and advancement
     * triggers.
     *
     * @param stack the itemstack to damage
     * @param amount the amount of damage to do
     * @return the damaged itemstack, or an empty stack if it broke. There are no
     * guarantees the returned itemstack is the same instance
     */
    @NotNull ItemStack damageItemStack(@NotNull ItemStack stack, int amount);

    /**
     * Damages the itemstack in this slot by the specified amount.
     * <p>
     * This runs all logic associated with damaging an itemstack like
     * gamemode and enchantment checks, events, stat changes, advancement
     * triggers, and notifying clients to play break animations.
     *
     * @param slot the slot of the stack to damage
     * @param amount the amount of damage to do
     */
    void damageItemStack(org.bukkit.inventory.@NotNull EquipmentSlot slot, int amount);
    // Paper end - ItemStack damage API

    // Paper start - body yaw API
    /**
     * Gets entity body yaw
     *
     * @return entity body yaw
     * @see Location#getYaw()
     */
    float getBodyYaw();

    /**
     * Sets entity body yaw
     *
     * @param bodyYaw new entity body yaw
     * @see Location#setYaw(float)
     */
    void setBodyYaw(float bodyYaw);
    // Paper end - body yaw API

    // Paper start - Expose canUseSlot
    /**
     * Checks whether this entity can use the equipment slot.
     * <br>For example, not all entities may have {@link org.bukkit.inventory.EquipmentSlot#BODY}.
     *
     * @param slot equipment slot
     * @return whether this entity can use the equipment slot
     */
    boolean canUseEquipmentSlot(org.bukkit.inventory.@NotNull EquipmentSlot slot);
    // Paper end - Expose canUseSlot

    /**
     * Gets the entity's combat tracker
     *
     * @return the entity's combat tracker
     */
    @ApiStatus.Experimental
    @NotNull CombatTracker getCombatTracker();
}
