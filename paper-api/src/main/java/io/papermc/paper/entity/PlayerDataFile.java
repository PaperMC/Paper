package io.papermc.paper.entity;


import io.papermc.paper.inventory.OfflinePlayerEnderChest;
import io.papermc.paper.inventory.OfflinePlayerInventory;
import io.papermc.paper.math.BlockPosition;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.util.TriState;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.jetbrains.annotations.Unmodifiable;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * Represents player <b>.dat</b> file
 */
public interface PlayerDataFile extends Attributable, Identified, InventoryHolder, PersistentDataHolder {
    /**
     * Gets player's saved location
     * <p><b>Note:</b> Location's world can be <b>null</b></p>
     *
     * @return Player location
     */
    @NotNull Location getLocation();

    /**
     * Sets player's location
     * <p><b>Note:</b> If location's world is <b>null</b> old player's world will not be overwritten on save</p>
     *
     * @param value Player location
     */
    void setLocation(@NotNull Location value);

    /**
     * Get player's saved velocity
     *
     * @return Player velocity
     */
    @NotNull
    Vector getVelocity();

    /**
     * Sets player's velocity
     *
     * @param value Player velocity
     */
    void setVelocity(@NotNull Vector value);

    /**
     * Returns the player's saved fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @return int fireTicks
     */
    int getFireTicks();

    /**
     * Sets the player's saved fire ticks (ticks before the entity stops
     * being on fire).
     *
     * @param ticks Ticks remaining
     */
    void setFireTicks(int ticks);

    /**
     * Retrieves the visual fire state of the player.
     *
     * @return A TriState indicating the current visual fire state.
     */
    @NotNull
    TriState getVisualFire();

    /**
     * Sets if the player has visual fire (it will always appear to be on fire).
     * <ul>
     *     <li>{@link TriState#NOT_SET} – will revert the entity's visual fire to default</li>
     *     <li>{@link TriState#TRUE} – will make the entity appear to be on fire</li>
     *     <li>{@link TriState#FALSE} – will make the entity appear to be not on fire</li>
     * </ul>
     *
     * @param fire a TriState value representing the state of the visual fire.
     */
    void setVisualFire(@NotNull TriState fire);

    /**
     * Returns the player's saved freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @return int freeze ticks
     */
    int getFreezeTicks();

    /**
     * Sets the player's saved freeze ticks (amount of ticks the entity has
     * been in powdered snow).
     *
     * @param ticks freeze ticks
     */
    void setFreezeTicks(int ticks);

    /**
     * Returns the distance this player has fallen
     *
     * @return The distance.
     */
    float getFallDistance();

    /**
     * Sets the fall distance for this player
     *
     * @param distance The new distance.
     */
    void setFallDistance(float distance);

    /**
     * Returns a unique and persistent id for this player
     *
     * @return unique id
     */
    @NotNull
    UUID getUniqueId();

    /**
     * Gets the amount of ticks this player has lived for.
     * <p>This is the equivalent to "age" in entities.</p>
     *
     * @return Age of player
     */
    int getTicksLived();

    /**
     * Sets the amount of ticks this player has lived for.
     * <p>This is the equivalent to "age" in entities. May not be less than one
     * tick.</p>
     *
     * @param value Age of player
     */
    void setTicksLived(int value);

    /**
     * Sets whether the player is invulnerable or not.
     * <p>When an entity is invulnerable it can only be damaged by players in
     * creative mode.</p>
     *
     * @param flag if the player is invulnerable
     */
    void setInvulnerable(boolean flag);

    /**
     * Gets whether the player is invulnerable or not.
     *
     * @return whether the player is
     */
    boolean isInvulnerable();

    /**
     * Returns a set of tags for this player.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @return a set of tags for this player
     */
    @NotNull
    Set<String> getScoreboardTags();

    /**
     * Add a tag to this player.
     * <br>
     * Entities can have no more than 1024 tags.
     *
     * @param tag the tag to add
     * @return true if the tag was successfully added
     */
    boolean addScoreboardTag(@NotNull String tag);

    /**
     * Removes a given tag from this player.
     *
     * @param tag the tag to remove
     * @return true if the tag was successfully removed
     */
    boolean removeScoreboardTag(@NotNull String tag);

    /**
     * Gets the player's health from 0 to player's max health, where 0 is dead.
     *
     * @return Health represented from 0 to max
     */
    float getHealth();

    /**
     * Sets the player's health from 0 to player's max health, where 0 is
     * dead.
     *
     * @param health New health represented from 0 to max
     * @throws IllegalArgumentException Thrown if the health is {@literal < 0 or >}
     */
    void setHealth(float health);

    /**
     * Gets the player's absorption amount.
     *
     * @return absorption amount from 0
     */
    float getAbsorptionAmount();

    /**
     * Gets the maximum health this player has.
     *
     * @return Maximum health
     * @deprecated use {@link Attribute#MAX_HEALTH}.
     */
    @Deprecated(since = "1.11")
    double getMaxHealth();

    /**
     * Sets the player's absorption amount.
     * <p>
     * Note: The amount is capped to the value of
     * {@link Attribute#MAX_ABSORPTION}. The effect of this method on
     * that attribute is currently unspecified and subject to change.
     *
     * @param amount new absorption amount from 0
     * @throws IllegalArgumentException thrown if health is {@literal < 0} or
     * non-finite.
     */
    void setAbsorptionAmount(float amount);

    /**
     * Returns the amount of air that the living player has remaining, in
     * ticks.
     *
     * @return amount of air remaining
     */
    int getRemainingAir();

    /**
     * Sets the amount of air that the player has remaining, in ticks.
     *
     * @param ticks amount of air remaining
     */
    void setRemainingAir(int ticks);

    /**
     * Returns the player's current no damage ticks.
     *
     * @return amount of no damage ticks
     */
    int getNoDamageTicks();

    /**
     * Sets the player's current no damage ticks.
     *
     * @param ticks amount of no damage ticks
     */
    void setNoDamageTicks(int ticks);

    /**
     * Adds the given {@link PotionEffect} to the player.
     * <p>
     * Note: {@link PotionEffect#getHiddenPotionEffect()} is ignored when
     * adding the effect to the entity.
     *
     * @param effect PotionEffect to be added
     * @return whether the effect could be added
     */
    boolean addPotionEffect(@NotNull PotionEffect effect);

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
    boolean addPotionEffects(@NotNull Collection<PotionEffect> effects);

    /**
     * Returns whether the player already has an existing effect of
     * the given {@link PotionEffectType} applied to it.
     *
     * @param type the potion type to check
     * @return whether the player has this potion effect active on them
     */
    boolean hasPotionEffect(@NotNull PotionEffectType type);

    /**
     * Returns the active {@link PotionEffect} of the specified type.
     * <p>
     * If the effect is not present on the player then null will be returned.
     *
     * @param type the potion type to check
     * @return the effect active on this player, or null if not active.
     */
    @Nullable
    PotionEffect getPotionEffect(@NotNull PotionEffectType type);

    /**
     * Removes any effects present of the given {@link PotionEffectType}.
     *
     * @param type the potion type to remove
     */
    void removePotionEffect(@NotNull PotionEffectType type);

    /**
     * Returns all currently active {@link PotionEffect}s on the player.
     *
     * @return a collection of {@link PotionEffect}s
     */
    @NotNull
    Collection<PotionEffect> getActivePotionEffects();

    /**
     * Removes all active potion effects for this player.
     *
     * @return true if any were removed
     */
    boolean clearActivePotionEffects();

    /**
     * Returns whether this player is slumbering.
     *
     * @return slumber state
     */
    boolean isSleeping();

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    @Nullable String getName();

    /**
     * Get the player's inventory.
     *
     * @return The inventory of the player
     */
    @Override
    @NotNull OfflinePlayerInventory getInventory();

    /**
     * Get the player's EnderChest inventory
     *
     * @return The EnderChest of the player
     */
    @NotNull OfflinePlayerEnderChest getEnderChest();

    /**
     * Gets the player's current enchantment seed.
     * <p>The Seed is used to generate enchantment options in the enchanting table
     * for the player.</p>
     *
     * @return the player's enchantment seed
     */
    int getEnchantmentSeed();

    /**
     * Sets the player's enchantment seed.
     * <p>The Seed is used to generate enchantment options in the enchanting table
     * for the player.</p>
     *
     * @param seed the player's new enchantment seed
     */
    void setEnchantmentSeed(int seed);

    /**
     * Gets the location of the bed the player is currently sleeping in
     *
     * @return location
     */
    @Nullable BlockPosition getBedLocation();

    /**
     * Gets this human's current {@link GameMode}
     *
     * @return Current game mode
     */
    @NotNull GameMode getGameMode();

    /**
     * Sets this player's current {@link GameMode}
     *
     * @param mode New game mode
     */
    void setGameMode(@NotNull GameMode mode);

    /**
     * Get an set of recipes this player has discovered.
     *
     * @return all discovered recipes
     */
     @Unmodifiable
     @NotNull Set<NamespacedKey> getDiscoveredRecipeSet();

    /**
     * Discover a recipe for this player such that it has not already been
     * discovered. This method will add the key's associated recipe to the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to discover
     *
     * @return whether or not the recipe was newly discovered
     */
    boolean discoverRecipe(@NotNull NamespacedKey recipe);

    /**
     * Discover a collection of recipes for this player such that they have not
     * already been discovered. This method will add the keys' associated
     * recipes to the player's recipe book. If a recipe in the provided
     * collection has already been discovered, it will be silently ignored.
     *
     * @param recipes the keys of the recipes to discover
     *
     * @return the amount of newly discovered recipes where 0 indicates that
     * none were newly discovered and a number equal to {@code recipes.size()}
     * indicates that all were new
     */
    int discoverRecipes(@NotNull Collection<NamespacedKey> recipes);

    /**
     * Undiscover a recipe for this player such that it has already been
     * discovered. This method will remove the key's associated recipe from the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to undiscover
     *
     * @return whether or not the recipe was successfully undiscovered (i.e. it
     * was previously discovered)
     */
    boolean undiscoverRecipe(@NotNull NamespacedKey recipe);

    /**
     * Undiscover a collection of recipes for this player such that they have
     * already been discovered. This method will remove the keys' associated
     * recipes from the player's recipe book. If a recipe in the provided
     * collection has not yet been discovered, it will be silently ignored.
     *
     * @param recipes the keys of the recipes to undiscover
     *
     * @return the amount of undiscovered recipes where 0 indicates that none
     * were undiscovered and a number equal to {@code recipes.size()} indicates
     * that all were undiscovered
     */
    int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes);

    /**
     * Check whether or not this player has discovered the specified recipe.
     *
     * @param recipe the key of the recipe to check
     *
     * @return true if discovered, false otherwise
     */
    boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe);

    /**
     * Gets the players current exhaustion level.
     * <p>
     * Exhaustion controls how fast the food level drops. While you have a
     * certain amount of exhaustion, your saturation will drop to zero, and
     * then your food will drop to zero.
     *
     * @return Exhaustion level
     */
    float getExhaustion();

    /**
     * Sets the players current exhaustion level
     *
     * @param value Exhaustion level
     */
    void setExhaustion(float value);

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated {@literal >} 0.
     *
     * @return Saturation level
     */
    float getSaturation();

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     */
    void setSaturation(float value);

    /**
     * Gets the players current food level
     *
     * @return Food level
     */
    int getFoodLevel();

    /**
     * Sets the players current food level
     *
     * @param value New food level
     */
    void setFoodLevel(int value);

    /**
     * Gets the player's last death location.
     *
     * @return the last death location if it exists, otherwise null.
     */
    @Nullable
    Location getLastDeathLocation();

    /**
     * Sets the player's last death location.
     * <br>
     * <b>Note:</b> This data is updated in the player's client only when the
     * player respawns.
     *
     * @param location where to set the last death player location
     */
    void setLastDeathLocation(@Nullable Location location);

    /**
     * Returns true if the player is supported by a block.
     * <p>This value is a state updated by the client after each movement.</p>
     *
     * @return True if player is on ground.
     */
    boolean isOnGround();

    /**
     * Gets the Location where the player will spawn at, {@code null} if they
     * don't have a valid respawn point.
     * <br>
     * Unlike offline players, the location if found will be loaded to validate by default.
     *
     * @return respawn location if exists, otherwise {@code null}.
     */
    @Nullable Location getRespawnLocation();

    /**
     * Sets the Location where the player will respawn.
     *
     * @param location where to set the respawn location
     */
    void setRespawnLocation(@Nullable Location location);

    /**
     * Returns whether this player has seen the win screen before.
     * When a player leaves the end the win screen is shown to them if they have not seen it before.
     *
     * @return Whether this player has seen the win screen before
     * @see #setHasSeenWinScreen(boolean)
     * @see <a href="https://minecraft.wiki/wiki/End_Poem">https://minecraft.wiki/wiki/End_Poem</a>
     */
    boolean hasSeenWinScreen();

    /**
     * Changes whether this player has seen the win screen before.
     * When a player leaves the end the win screen is shown to them if they have not seen it before.
     *
     * @param hasSeenWinScreen Whether this player has seen the win screen before
     * @see #hasSeenWinScreen()
     * @see <a href="https://minecraft.wiki/wiki/End_Poem">https://minecraft.wiki/wiki/End_Poem</a>
     */
    void setHasSeenWinScreen(boolean hasSeenWinScreen);

    /**
     * Gets this player's previous {@link GameMode}
     *
     * @return Previous game mode or null
     */
    @Nullable GameMode getPreviousGameMode();

    /**
     * Gets the players current experience points towards the next level.
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @return Current experience points
     */
    float getExp();

    /**
     * Sets the players current experience points towards the next level
     * <p>
     * This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @param exp New experience points
     */
    void setExp(float exp);

    /**
     * Gets the players current experience level
     *
     * @return Current experience level
     */
    int getLevel();

    /**
     * Sets the players current experience level
     *
     * @param level New experience level
     */
    void setLevel(int level);

    /**
     * Gets the players total experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time and is not currently displayed to the client.
     *
     * @return Current total experience points
     */
    int getTotalExperience();

    /**
     * Sets the players current experience points.
     * <br>
     * This refers to the total amount of experience the player has collected
     * over time and is not currently displayed to the client.
     *
     * @param exp New total experience points
     */
    void setTotalExperience(int exp);

    /**
     * Gets the players total amount of experience points he collected to reach the current level and level progress.
     *
     * <p>This method differs from {@link #getTotalExperience()} in that this method always returns an
     * up-to-date value that reflects the players{@link #getLevel() level} and {@link #getExp() level progress}</p>
     *
     * @return Current total experience points
     * @see #getLevel()
     * @see #getExp()
     * @see #setExperienceLevelAndProgress(int)
     */
    @Range(from = 0, to = Integer.MAX_VALUE) int calculateTotalExperiencePoints();

    /**
     * Gets the period of time (in ticks) before this player can use a portal.
     *
     * @return portal cooldown ticks
     */
    int getPortalCooldown();

    /**
     * Sets the period of time (in ticks) before this player can use a portal.
     *
     * @param cooldown portal cooldown ticks
     */
    void setPortalCooldown(int cooldown);

    /**
     * Updates the players level and level progress to that what would be reached when the total amount of experience
     * had been collected.
     *
     * <p>This method differs from {@link #setTotalExperience(int)} in that this method actually updates the
     * {@link #getLevel() level} and {@link #getExp() level progress} so that a subsequent call of
     * {@link #calculateTotalExperiencePoints()} yields the same amount of points that have been set</p>
     *
     * @param totalExperience New total experience points
     * @see #setLevel(int)
     * @see #setExp(float)
     * @see #calculateTotalExperiencePoints()
     */
    void setExperienceLevelAndProgress(@Range(from = 0, to = Integer.MAX_VALUE) int totalExperience);

    /**
     * Gets the total amount of experience points that are needed to reach the next level from zero progress towards it.
     *
     * <p>Can be used with {@link #getExp()} to calculate the current points for the current level and alike</p>
     *
     * @return The required experience points
     * @see #getExp()
     */
    int getExperiencePointsNeededForNextLevel();

    /**
     * Determines if the Player is allowed to fly via jump key double-tap like
     * in creative mode.
     *
     * @return True if the player is allowed to fly.
     */
    boolean getAllowFlight();

    /**
     * Sets if the Player is allowed to fly via jump key double-tap like in
     * creative mode.
     *
     * @param flight If flight should be allowed.
     */
    void setAllowFlight(boolean flight);

    /**
     * Checks to see if this player is currently flying or not.
     *
     * @return True if the player is flying, else false.
     */
    boolean isFlying();

    /**
     * Makes this player start or stop flying.
     *
     * @param value True to fly.
     */
    void setFlying(boolean value);

    /**
     * Sets the speed at which a client will fly. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    void setFlySpeed(float value) throws IllegalArgumentException;

    /**
     * Sets the speed at which a client will walk. Negative values indicate
     * reverse directions.
     *
     * @param value The new speed, from -1 to 1.
     * @throws IllegalArgumentException If new speed is less than -1 or
     *     greater than 1
     */
    void setWalkSpeed(float value) throws IllegalArgumentException;

    /**
     * Gets the current allowed speed that a client can fly.
     *
     * @return The current allowed speed, from -1 to 1
     */
    float getFlySpeed();

    /**
     * Gets the current allowed speed that a client can walk.
     *
     * @return The current allowed speed, from -1 to 1
     */
    float getWalkSpeed();

    /**
     * Get the score that shows in the death screen of the player.
     * <p>This amount is added to when the player gains experience.</p>
     *
     * @return Death screen score of player
     */
    int getDeathScreenScore();

    /**
     * Set the score that shows in the death screen of the player.
     * <p>This amount is added to when the player gains experience.</p>
     *
     * @param score New death screen score of player
     */
    void setDeathScreenScore(int score);

    /**
     * Gets player selected hotbar slot
     *
     * @return selected hotbar slot
     */
    int getSelectedHotbarSlot();

    /**
     * Sets selected hotbar slot of player
     *
     * @param value hotbar slot change to
     */
    void setSelectedHotbarSlot(int value);

    /**
     * Save data file
     *
     * @throws IOException on error in file writing
     * @throws PlayerSerializationException on error in serializing
     */
    void save() throws IOException, PlayerSerializationException;

    @Override
    default @NotNull Identity identity() {
        return Identity.identity(getUniqueId());
    }
}
