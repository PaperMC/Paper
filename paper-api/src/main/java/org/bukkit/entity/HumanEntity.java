package org.bukkit.entity;

import java.util.Collection;
import java.util.Set;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a human entity, such as an NPC or a player
 *
 * @since 1.0.0 R1
 */
public interface HumanEntity extends LivingEntity, AnimalTamer, InventoryHolder {

    /**
     * @since 1.17.1
     */
    // Paper start
    @Override
    org.bukkit.inventory.@NotNull EntityEquipment getEquipment();
    // Paper end

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    @NotNull
    @Override
    public String getName();

    /**
     * Get the player's inventory.
     *
     * @return The inventory of the player, this also contains the armor
     *     slots.
     */
    @NotNull
    @Override
    public PlayerInventory getInventory();

    /**
     * Get the player's EnderChest inventory
     *
     * @return The EnderChest of the player
     * @since 1.3.2 R1.0
     */
    @NotNull
    public Inventory getEnderChest();

    /**
     * Gets the player's selected main hand
     *
     * @return the players main hand
     * @since 1.9.4
     */
    @NotNull
    public MainHand getMainHand();

    /**
     * If the player currently has an inventory window open, this method will
     * set a property of that window, such as the state of a progress bar.
     *
     * @param prop The property.
     * @param value The value to set the property to.
     * @return True if the property was successfully set.
     * @deprecated use {@link InventoryView} and its children.
     */
    @Deprecated(forRemoval = true, since = "1.21")
    public boolean setWindowProperty(@NotNull InventoryView.Property prop, int value);

    /**
     * Gets the player's current enchantment seed.
     *
     * The Seed is used to generate enchantment options in the enchanting table
     * for the player.
     *
     * @return the player's enchantment seed
     * @since 1.19.3
     */
    public int getEnchantmentSeed();

    /**
     * Sets the player's enchantment seed.
     *
     * The Seed is used to generate enchantment options in the enchanting table
     * for the player.
     *
     * @param seed the player's new enchantment seed
     * @since 1.19.3
     */
    public void setEnchantmentSeed(int seed);

    /**
     * Gets the inventory view the player is currently viewing. If they do not
     * have an inventory window open, it returns their internal crafting view.
     *
     * @return The inventory view.
     * @since 1.1.0 R5
     */
    @NotNull
    public InventoryView getOpenInventory();

    /**
     * Opens an inventory window with the specified inventory on the top and
     * the player's inventory on the bottom.
     *
     * @param inventory The inventory to open
     * @return The newly opened inventory view
     * @since 1.1.0 R5
     */
    @Nullable
    public InventoryView openInventory(@NotNull Inventory inventory);

    /**
     * Opens an empty workbench inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no workbench block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.1.0 R5
     */
    @Nullable
    public InventoryView openWorkbench(@Nullable Location location, boolean force);

    /**
     * Opens an empty enchanting inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no enchanting table at the
     *     location, no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.1.0 R5
     */
    @Nullable
    public InventoryView openEnchanting(@Nullable Location location, boolean force);

    /**
     * Opens an inventory window to the specified inventory view.
     * <p>
     * The player associated with the InventoryView must be the same as this
     * instance of HumanEntity.
     * <p>
     * The player of the InventoryView can be checked using
     * {@link InventoryView#getPlayer()}.
     *
     * @param inventory The view to open
     * @since 1.1.0 R5
     */
    public void openInventory(@NotNull InventoryView inventory);

    /**
     * Starts a trade between the player and the villager.
     *
     * Note that only one player may trade with a villager at once. You must use
     * the force parameter for this.
     *
     * @param trader The merchant to trade with. Cannot be null.
     * @param force whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     * @since 1.9.4
     */
    @Nullable
    public InventoryView openMerchant(@NotNull Villager trader, boolean force);

    /**
     * Starts a trade between the player and the merchant.
     *
     * Note that only one player may trade with a merchant at once. You must use
     * the force parameter for this.
     *
     * @param merchant The merchant to trade with. Cannot be null.
     * @param force whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * opened.
     * @since 1.11
     */
    @Nullable
    public InventoryView openMerchant(@NotNull Merchant merchant, boolean force);

    // Paper start - Add additional containers
    /**
     * Opens an empty anvil inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no anvil block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openAnvil(@Nullable Location location, boolean force);

    /**
     * Opens an empty cartography table inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no cartography table block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openCartographyTable(@Nullable Location location, boolean force);

    /**
     * Opens an empty grindstone inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no grindstone block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openGrindstone(@Nullable Location location, boolean force);

    /**
     * Opens an empty loom inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no loom block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openLoom(@Nullable Location location, boolean force);

    /**
     * Opens an empty smithing table inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no smithing table block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openSmithingTable(@Nullable Location location, boolean force);

    /**
     * Opens an empty stonecutter inventory window with the player's inventory
     * on the bottom.
     *
     * @param location The location to attach it to. If null, the player's
     *     location is used.
     * @param force If false, and there is no stonecutter block at the location,
     *     no inventory will be opened and null will be returned.
     * @return The newly opened inventory view, or null if it could not be
     *     opened.
     * @since 1.16.2
     */
    @Nullable
    public InventoryView openStonecutter(@Nullable Location location, boolean force);
    // Paper end

    /**
     * Force-closes the currently open inventory view for this player, if any.
     *
     * @since 1.1.0 R5
     */
    public void closeInventory();

    // Paper start
    /**
     * Force-closes the currently open inventory view for this player, if any.
     *
     * @param reason why the inventory is closing
     */
    public void closeInventory(@NotNull org.bukkit.event.inventory.InventoryCloseEvent.Reason reason);
    // Paper end

    /**
     * Returns the ItemStack currently in your hand, can be empty.
     *
     * @return The ItemStack of the item you are currently holding.
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated(since = "1.9")
    @NotNull
    public ItemStack getItemInHand();

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was holding.
     *
     * @param item The ItemStack which will end up in the hand
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated(since = "1.9")
    public void setItemInHand(@Nullable ItemStack item);

    /**
     * Returns the ItemStack currently on your cursor, can be empty. Will
     * always be empty if the player currently has no open window.
     *
     * @return The ItemStack of the item you are currently moving around.
     * @since 1.1.0 R5
     */
    @NotNull
    public ItemStack getItemOnCursor();

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was moving. Will always be empty if the player currently has no
     * open window.
     *
     * @param item The ItemStack which will end up in the hand
     * @since 1.1.0 R5
     */
    public void setItemOnCursor(@Nullable ItemStack item);

    /**
     * Check whether a cooldown is active on the specified material.
     *
     * @param material the material to check
     * @return if a cooldown is active on the material
     * @throws IllegalArgumentException if the material is not an item
     * @since 1.11.2
     */
    public boolean hasCooldown(@NotNull Material material);

    /**
     * Get the cooldown time in ticks remaining for the specified material.
     *
     * @param material the material to check
     * @return the remaining cooldown time in ticks
     * @throws IllegalArgumentException if the material is not an item
     * @since 1.11.2
     */
    public int getCooldown(@NotNull Material material);

    /**
     * Set a cooldown on the specified material for a certain amount of ticks.
     * ticks. 0 ticks will result in the removal of the cooldown.
     * <p>
     * Cooldowns are used by the server for items such as ender pearls and
     * shields to prevent them from being used repeatedly.
     * <p>
     * Note that cooldowns will not by themselves stop an item from being used
     * for attacking.
     *
     * @param material the material to set the cooldown for
     * @param ticks the amount of ticks to set or 0 to remove
     * @throws IllegalArgumentException if the material is not an item
     * @since 1.11.2
     */
    public void setCooldown(@NotNull Material material, int ticks);

    // Paper start
    /**
     * Sets player hurt direction
     *
     * @param hurtDirection hurt direction
     * @since 1.19.4
     */
    @Override
    void setHurtDirection(float hurtDirection);
    // Paper end

    // Paper start
    /**
     * If the player has slept enough to count towards passing the night.
     *
     * @return true if the player has slept enough
     * @since 1.16.5
     */
    public boolean isDeeplySleeping();
    // Paper end

    /**
     * Check whether a cooldown is active on the specified item.
     *
     * @param item the item to check
     * @return if a cooldown is active on the item
     * @since 1.21.3
     */
    public boolean hasCooldown(@NotNull ItemStack item);

    /**
     * Get the cooldown time in ticks remaining for the specified item.
     *
     * @param item the item to check
     * @return the remaining cooldown time in ticks
     * @since 1.21.3
     */
    public int getCooldown(@NotNull ItemStack item);

    /**
     * Set a cooldown on the specified item for a certain amount of ticks.
     * ticks. 0 ticks will result in the removal of the cooldown.
     * <p>
     * Cooldowns are used by the server for items such as ender pearls and
     * shields to prevent them from being used repeatedly.
     * <p>
     * Note that cooldowns will not by themselves stop an item from being used
     * for attacking.
     *
     * @param item the item to set the cooldown for
     * @param ticks the amount of ticks to set or 0 to remove
     * @since 1.21.3
     */
    public void setCooldown(@NotNull ItemStack item, int ticks);

    /**
     * Get the sleep ticks of the player. This value may be capped.
     *
     * @return slumber ticks
     */
    public int getSleepTicks();


    // Paper start - Potential bed api
    /**
     * Gets the Location of the player's bed, null if they have not slept
     * in one. This method will not attempt to validate if the current bed
     * is still valid.
     *
     * @return Bed Location if has slept in one, otherwise null.
     * @since 1.15.2
     */
    @Nullable
    public Location getPotentialBedLocation();
    // Paper end
    // Paper start
    /**
     * @return the player's fishing hook if they are fishing
     * @since 1.19
     */
    @Nullable
    FishHook getFishHook();
    // Paper end

    /**
     * Attempts to make the entity sleep at the given location.
     * <br>
     * The location must be in the current world and have a bed placed at the
     * location. The game may also enforce other requirements such as proximity
     * to bed, monsters, and dimension type if force is not set.
     *
     * @param location the location of the bed
     * @param force whether to try and sleep at the location even if not
     * normally possible
     * @return whether the sleep was successful
     * @since 1.13.2
     */
    public boolean sleep(@NotNull Location location, boolean force);

    /**
     * Causes the player to wakeup if they are currently sleeping.
     *
     * @param setSpawnLocation whether to set their spawn location to the bed
     * they are currently sleeping in
     * @throws IllegalStateException if not sleeping
     * @since 1.13.2
     */
    public void wakeup(boolean setSpawnLocation);

    /**
     * Make the player start a riptide spin attack.
     *
     * @param duration spin attack duration in ticks.
     * @param attackStrength damage value inflicted upon entities hit by spin attack.
     * @param attackItem item used to attack.
     * @since 1.21.1
     */
    public void startRiptideAttack(int duration, float attackStrength, @Nullable ItemStack attackItem);

    /**
     * Gets the location of the bed the player is currently sleeping in
     *
     * @return location
     * @throws IllegalStateException if not sleeping
     * @since 1.13.2
     */
    @NotNull
    public Location getBedLocation();

    /**
     * Gets this human's current {@link GameMode}
     *
     * @return Current game mode
     */
    @NotNull
    public GameMode getGameMode();

    /**
     * Sets this human's current {@link GameMode}
     *
     * @param mode New game mode
     */
    public void setGameMode(@NotNull GameMode mode);

    /**
     * Check if the player is currently blocking (ie with a shield).
     *
     * @return Whether they are blocking.
     * @since 1.2.5 R0.1
     */
    public boolean isBlocking();

    /**
     * Check if the player currently has their hand raised (ie about to begin
     * blocking).
     *
     * @return Whether their hand is raised
     * @see LivingEntity#hasActiveItem()
     * @since 1.10.2
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4") // Paper - active item API
    public boolean isHandRaised();

    /**
     * Get the total amount of experience required for the player to level
     *
     * @return Experience required to level up
     * @since 1.3.1 R1.0
     */
    public int getExpToLevel();

    // Paper start
    /**
     * If there is an Entity on this entities left shoulder, it will be released to the world and returned.
     * If no Entity is released, null will be returned.
     *
     * @return The released entity, or null
     * @since 1.12
     */
    @Nullable
    public Entity releaseLeftShoulderEntity();

    /**
     * If there is an Entity on this entities left shoulder, it will be released to the world and returned.
     * If no Entity is released, null will be returned.
     *
     * @return The released entity, or null
     * @since 1.12
     */
    @Nullable
    public Entity releaseRightShoulderEntity();
    // Paper end

    /**
     * Gets the current cooldown for a player's attack.
     *
     * This is used to calculate damage, with 1.0 representing a fully charged
     * attack and 0.0 representing a non-charged attack
     *
     * @return A float between 0.0-1.0 representing the progress of the charge
     * @since 1.15.2
     */
    public float getAttackCooldown();

    /**
     * Discover a recipe for this player such that it has not already been
     * discovered. This method will add the key's associated recipe to the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to discover
     *
     * @return whether or not the recipe was newly discovered
     * @since 1.13.1
     */
    public boolean discoverRecipe(@NotNull NamespacedKey recipe);

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
     * @since 1.13.1
     */
    public int discoverRecipes(@NotNull Collection<NamespacedKey> recipes);

    /**
     * Undiscover a recipe for this player such that it has already been
     * discovered. This method will remove the key's associated recipe from the
     * player's recipe book.
     *
     * @param recipe the key of the recipe to undiscover
     *
     * @return whether or not the recipe was successfully undiscovered (i.e. it
     * was previously discovered)
     * @since 1.13.1
     */
    public boolean undiscoverRecipe(@NotNull NamespacedKey recipe);

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
     * @since 1.13.1
     */
    public int undiscoverRecipes(@NotNull Collection<NamespacedKey> recipes);

    /**
     * Check whether or not this entity has discovered the specified recipe.
     *
     * @param recipe the key of the recipe to check
     *
     * @return true if discovered, false otherwise
     * @since 1.16.1
     */
    public boolean hasDiscoveredRecipe(@NotNull NamespacedKey recipe);

    /**
     * Get an immutable set of recipes this entity has discovered.
     *
     * @return all discovered recipes
     * @since 1.16.1
     */
    @NotNull
    public Set<NamespacedKey> getDiscoveredRecipes();

    /**
     * Gets the entity currently perched on the left shoulder or null if no
     * entity.
     * <br>
     * The returned entity will not be spawned within the world, so most
     * operations are invalid unless the entity is first spawned in.
     *
     * @return left shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     * @since 1.12
     */
    @Deprecated(since = "1.12")
    @Nullable
    public Entity getShoulderEntityLeft();

    /**
     * Sets the entity currently perched on the left shoulder, or null to
     * remove. This method will remove the entity from the world.
     * <br>
     * Note that only a copy of the entity will be set to display on the
     * shoulder.
     * <br>
     * Also note that the client will currently only render {@link Parrot}
     * entities.
     *
     * @param entity left shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     * @since 1.12
     */
    @Deprecated(since = "1.12")
    public void setShoulderEntityLeft(@Nullable Entity entity);

    /**
     * Gets the entity currently perched on the right shoulder or null if no
     * entity.
     * <br>
     * The returned entity will not be spawned within the world, so most
     * operations are invalid unless the entity is first spawned in.
     *
     * @return right shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     * @since 1.12
     */
    @Deprecated(since = "1.12")
    @Nullable
    public Entity getShoulderEntityRight();

    /**
     * Sets the entity currently perched on the right shoulder, or null to
     * remove. This method will remove the entity from the world.
     * <br>
     * Note that only a copy of the entity will be set to display on the
     * shoulder.
     * <br>
     * Also note that the client will currently only render {@link Parrot}
     * entities.
     *
     * @param entity right shoulder entity
     * @deprecated There are currently no well defined semantics regarding
     * serialized entities in Bukkit. Use with care.
     * @since 1.12
     */
    @Deprecated(since = "1.12")
    public void setShoulderEntityRight(@Nullable Entity entity);
    // Paper start - Add method to open already placed sign
    /**
     * Opens an editor window for the specified sign
     *
     * @param sign The sign to open
     * @deprecated use {@link #openSign(org.bukkit.block.Sign, org.bukkit.block.sign.Side)}
     * @since 1.12.2
     */
    @Deprecated
    default void openSign(@NotNull org.bukkit.block.Sign sign) {
        this.openSign(sign, org.bukkit.block.sign.Side.FRONT);
    }

    /**
     * Opens an editor window for the specified sign
     *
     * @param sign The sign to open
     * @param side The side of the sign to open
     * @since 1.20
     */
    void openSign(org.bukkit.block.@NotNull Sign sign, org.bukkit.block.sign.@NotNull Side side);
    // Paper end

    /**
     * Make the entity drop the item in their hand.
     * <br>
     * This will force the entity to drop the item they are holding with
     * an option to drop the entire {@link ItemStack} or just 1 of the items.
     *
     * @param dropAll True to drop entire stack, false to drop 1 of the stack
     * @return True if item was dropped successfully
     * @since 1.16.2
     */
    public boolean dropItem(boolean dropAll);

    /**
     * Gets the players current exhaustion level.
     * <p>
     * Exhaustion controls how fast the food level drops. While you have a
     * certain amount of exhaustion, your saturation will drop to zero, and
     * then your food will drop to zero.
     *
     * @return Exhaustion level
     * @since 1.16.5
     */
    public float getExhaustion();

    /**
     * Sets the players current exhaustion level
     *
     * @param value Exhaustion level
     * @since 1.16.5
     */
    public void setExhaustion(float value);

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated {@literal >} 0.
     *
     * @return Saturation level
     * @since 1.16.5
     */
    public float getSaturation();

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     * @since 1.16.5
     */
    public void setSaturation(float value);

    /**
     * Gets the players current food level
     *
     * @return Food level
     * @since 1.16.5
     */
    public int getFoodLevel();

    /**
     * Sets the players current food level
     *
     * @param value New food level
     * @since 1.16.5
     */
    public void setFoodLevel(int value);

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have saturation and
     * their food level is {@literal >=} 20. Default is 10.
     *
     * @return the regeneration rate
     * @since 1.16.5
     */
    public int getSaturatedRegenRate();

    /**
     * Set the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have saturation and
     * their food level is {@literal >=} 20. Default is 10.
     * Not affected if the world's difficulty is peaceful.
     *
     * @param ticks the amount of ticks to gain 1 health.
     * @since 1.16.5
     */
    public void setSaturatedRegenRate(int ticks);

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have no saturation and
     * their food level is {@literal >=} 18. Default is 80.
     *
     * @return the regeneration rate
     * @since 1.16.5
     */
    public int getUnsaturatedRegenRate();

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have no saturation and
     * their food level is {@literal >=} 18. Default is 80.
     * Not affected if the world's difficulty is peaceful.
     *
     * @param ticks the amount of ticks to gain 1 health.
     * @since 1.16.5
     */
    public void setUnsaturatedRegenRate(int ticks);

    /**
     * Get the starvation rate (1 health per x ticks) of
     * the HumanEntity. Default is 80.
     *
     * @return the starvation rate
     * @since 1.16.5
     */
    public int getStarvationRate();

    /**
     * Get the starvation rate (1 health per x ticks) of
     * the HumanEntity. Default is 80.
     *
     * @param ticks the amount of ticks to lose 1 health
     * @since 1.16.5
     */
    public void setStarvationRate(int ticks);

    /**
     * Gets the player's last death location.
     *
     * @return the last death location if it exists, otherwise null.
     * @since 1.19
     */
    @Nullable
    public Location getLastDeathLocation();

    /**
     * Sets the player's last death location.
     * <br>
     * <b>Note:</b> This data is updated in the player's client only when the
     * player respawns.
     *
     * @param location where to set the last death player location
     * @since 1.19
     */
    public void setLastDeathLocation(@Nullable Location location);

    /**
     * Perform a firework boost.
     * <p>
     * This method will only work such that {@link #isGliding()} is true and
     * the entity is actively gliding with an elytra. Additionally, the supplied
     * {@code fireworkItemStack} must be a firework rocket. The power of the boost
     * will directly correlate to {@link FireworkMeta#getPower()}.
     *
     * @param fireworkItemStack the firework item stack to use to glide
     * @return the attached {@link Firework}, or null if the entity could not
     * be boosted
     * @throws IllegalArgumentException if the fireworkItemStack is not a firework
     * @since 1.19.2
     */
    @Nullable
    public Firework fireworkBoost(@NotNull ItemStack fireworkItemStack);

}
