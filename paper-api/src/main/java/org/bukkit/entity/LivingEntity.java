package org.bukkit.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.block.Block;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a living entity, such as a monster or player
 */
public interface LivingEntity extends Attributable, Damageable, ProjectileSource {

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
    public Block getTargetBlockExact(int maxDistance);

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
    public RayTraceResult rayTraceBlocks(double maxDistance);

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
     * Gets the player identified as the killer of the living entity.
     * <p>
     * May be null.
     *
     * @return killer player, or null if none found
     */
    @Nullable
    public Player getKiller();

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     *
     * @param effect PotionEffect to be added
     * @return whether the effect could be added
     */
    public boolean addPotionEffect(@NotNull PotionEffect effect);

    /**
     * Adds the given {@link PotionEffect} to the living entity.
     * <p>
     * Only one potion effect can be present for a given {@link
     * PotionEffectType}.
     *
     * @param effect PotionEffect to be added
     * @param force whether conflicting effects should be removed
     * @return whether the effect could be added
     * @deprecated no need to force since multiple effects of the same type are
     * now supported.
     */
    @Deprecated
    public boolean addPotionEffect(@NotNull PotionEffect effect, boolean force);

    /**
     * Attempts to add all of the given {@link PotionEffect} to the living
     * entity.
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
     * This method has no effect on EnderDragons, Withers, Players, or Bats.
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
     */
    public void setSwimming(boolean swimming);

    /**
     * Checks to see if an entity is currently using the Riptide enchantment.
     *
     * @return True if this entity is currently riptiding.
     */
    public boolean isRiptiding();

    /**
     * Returns whether this entity is slumbering.
     *
     * @return slumber state
     */
    public boolean isSleeping();

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
     * Set if this entity will be subject to collisions other entities.
     * <p>
     * Note that collisions are bidirectional, so this method would need to be
     * set to false on both the collidee and the collidant to ensure no
     * collisions take place.
     *
     * @param collidable collision status
     */
    void setCollidable(boolean collidable);

    /**
     * Gets if this entity is subject to collisions with other entities.
     * <p>
     * Please note that this method returns only the custom collidable state,
     * not whether the entity is non-collidable for other reasons such as being
     * dead.
     *
     * @return collision status
     */
    boolean isCollidable();

    /**
     * Returns the value of the memory specified.
     * <p>
     * Note that the value is null when the specific entity does not have that
     * value by default.
     *
     * @param memoryKey memory to access
     * @param <T> the type of the return value
     * @return a instance of the memory section value or null if not present
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
}
