package org.bukkit.entity;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
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
     * Gets the time in ticks until the next arrow leaves the entity's body.
     *
     * @return ticks until arrow leaves
     */
    public int getArrowCooldown();

    /**
     * Sets the time in ticks until the next arrow leaves the entity's body.
     *
     * @param ticks time until arrow leaves
     */
    public void setArrowCooldown(int ticks);

    /**
     * Gets the amount of arrows in an entity's body.
     *
     * @return amount of arrows in body
     */
    public int getArrowsInBody();

    /**
     * Set the amount of arrows in the entity's body.
     *
     * @param count amount of arrows in entity's body
     */
    public void setArrowsInBody(int count);

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
     */
    @NotNull
    public EntityCategory getCategory();

    /**
     * Sets whether the entity is invisible or not.
     *
     * @param invisible If the entity is invisible
     */
    public void setInvisible(boolean invisible);

    /**
     * Gets whether the entity is invisible or not.
     *
     * @return Whether the entity is invisible
     */
    public boolean isInvisible();
}
