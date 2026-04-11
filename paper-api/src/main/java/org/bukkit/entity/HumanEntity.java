package org.bukkit.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;
import io.papermc.paper.datacomponent.item.UseCooldown;
import net.kyori.adventure.key.Key;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Represents a human entity, such as an NPC or a player
 */
@NullMarked
public interface HumanEntity extends LivingEntity, AnimalTamer, InventoryHolder {

    // Paper start
    @Override
    EntityEquipment getEquipment();
    // Paper end

    /**
     * Returns the name of this player
     *
     * @return Player name
     */
    @Override
    public String getName();

    /**
     * Get the player's inventory.
     *
     * @return The inventory of the player, this also contains the armor
     *     slots.
     */
    @Override
    public PlayerInventory getInventory();

    /**
     * Get the player's EnderChest inventory
     *
     * @return The EnderChest of the player
     */
    public Inventory getEnderChest();

    /**
     * Gets the player's selected main hand
     *
     * @return the players main hand
     */
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
    public boolean setWindowProperty(InventoryView.Property prop, int value);

    /**
     * Gets the player's current enchantment seed.
     *
     * The Seed is used to generate enchantment options in the enchanting table
     * for the player.
     *
     * @return the player's enchantment seed
     */
    public int getEnchantmentSeed();

    /**
     * Sets the player's enchantment seed.
     *
     * The Seed is used to generate enchantment options in the enchanting table
     * for the player.
     *
     * @param seed the player's new enchantment seed
     */
    public void setEnchantmentSeed(int seed);

    /**
     * Gets the inventory view the player is currently viewing. If they do not
     * have an inventory window open, it returns their internal crafting view.
     *
     * @return The inventory view.
     */
    public InventoryView getOpenInventory();

    /**
     * Opens an inventory window with the specified inventory on the top and
     * the player's inventory on the bottom.
     *
     * @param inventory The inventory to open
     * @return The newly opened inventory view
     */
    @Nullable
    public InventoryView openInventory(Inventory inventory);

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
     * @deprecated This method should be replaced by {@link MenuType#CRAFTING}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#ENCHANTMENT}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     */
    public void openInventory(InventoryView inventory);

    /**
     * Starts a trade between the player and the villager.
     *
     * Note that only one player may trade with a villager at once. You must use
     * the force parameter for this.
     *
     * @param trader The merchant to trade with. Cannot be null.
     * @param force whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * @deprecated This method can be replaced by using {@link MenuType#MERCHANT}
     * in conjunction with {@link #openInventory(InventoryView)}.
     */
    @Deprecated(since = "1.21.4")
    @Nullable
    default InventoryView openMerchant(Villager trader, boolean force) {
        return this.openMerchant((Merchant) trader, force);
    }

    /**
     * Starts a trade between the player and the merchant.
     *
     * Note that only one player may trade with a merchant at once. You must use
     * the force parameter for this.
     *
     * @param merchant The merchant to trade with. Cannot be null.
     * @param force whether to force the trade even if another player is trading
     * @return The newly opened inventory view, or null if it could not be
     * @deprecated This method can be replaced by using {@link MenuType#MERCHANT}
     * in conjunction with {@link #openInventory(InventoryView)}.
     */
    @Deprecated(since = "1.21.4")
    @Nullable
    public InventoryView openMerchant(Merchant merchant, boolean force);

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
     * @deprecated This method should be replaced by {@link MenuType#ANVIL}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#CARTOGRAPHY_TABLE}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#GRINDSTONE}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#LOOM}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#SMITHING}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
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
     * @deprecated This method should be replaced by {@link MenuType#STONECUTTER}
     * see {@link MenuType.Typed#builder()} and its options for more information.
     */
    @Deprecated(since = "1.21.4")
    @Nullable
    public InventoryView openStonecutter(@Nullable Location location, boolean force);
    // Paper end

    /**
     * Force-closes the currently open inventory view for this player, if any.
     */
    default void closeInventory() {
        this.closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason.PLUGIN);
    }

    /**
     * Force-closes the currently open inventory view for this player, if any.
     *
     * @param reason why the inventory is closing
     */
    void closeInventory(org.bukkit.event.inventory.InventoryCloseEvent.Reason reason);

    /**
     * Returns the ItemStack currently in your hand, can be empty.
     *
     * @return The ItemStack of the item you are currently holding.
     * @deprecated Humans may now dual wield in their off hand, use explicit
     * methods in {@link PlayerInventory}.
     */
    @Deprecated(since = "1.9")
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
     */
    public ItemStack getItemOnCursor();

    /**
     * Sets the item to the given ItemStack, this will replace whatever the
     * user was moving. Will always be empty if the player currently has no
     * open window.
     *
     * @param item The ItemStack which will end up in the hand
     */
    public void setItemOnCursor(@Nullable ItemStack item);

    /**
     * Check whether a cooldown is active on the specified material.
     *
     * @param material the material to check
     * @return if a cooldown is active on the material
     * @throws IllegalArgumentException if the material is not an item
     */
    public boolean hasCooldown(Material material);

    /**
     * Get the cooldown time in ticks remaining for the specified material.
     *
     * @param material the material to check
     * @return the remaining cooldown time in ticks
     * @throws IllegalArgumentException if the material is not an item
     */
    public int getCooldown(Material material);

    /**
     * Set a cooldown on the specified material for a certain amount of ticks.
     * 0 ticks will result in the removal of the cooldown.
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
     */
    default void setCooldown(Material material, int ticks) {
        this.setCooldown(ItemStack.of(material), ticks);
    }

    /**
     * Sets player hurt direction
     *
     * @param hurtDirection hurt direction
     */
    @Override
    void setHurtDirection(float hurtDirection);

    /**
     * If the player has slept enough to count towards passing the night.
     *
     * @return true if the player has slept enough
     */
    public boolean isDeeplySleeping();

    /**
     * Check whether a cooldown is active on the specified item.
     *
     * @param item the item to check
     * @return if a cooldown is active on the item
     */
    public boolean hasCooldown(ItemStack item);

    /**
     * Get the cooldown time in ticks remaining for the specified item.
     *
     * @param item the item to check
     * @return the remaining cooldown time in ticks
     */
    public int getCooldown(ItemStack item);

    /**
     * Set a cooldown on the specified item for a certain amount of ticks.
     * 0 ticks will result in the removal of the cooldown.
     * <p>
     * Cooldowns are used by the server for items such as ender pearls and
     * shields to prevent them from being used repeatedly.
     * <p>
     * Note that cooldowns will not by themselves stop an item from being used
     * for attacking.
     *
     * @param item the item to set the cooldown for
     * @param ticks the amount of ticks to set or 0 to remove
     */
    public void setCooldown(ItemStack item, int ticks);

    /**
     * Get the cooldown time in ticks remaining for the specified cooldown group.
     *
     * @param key the cooldown group to check
     * @return the remaining cooldown time in ticks
     * @see UseCooldown#cooldownGroup()
     */
    public int getCooldown(Key key);

    /**
     * Set a cooldown on items with the specified cooldown group for a certain amount of ticks.
     * 0 ticks will result in the removal of the cooldown.
     * <p>
     * Cooldowns are used by the server for items such as ender pearls and
     * shields to prevent them from being used repeatedly.
     * <p>
     * Note that cooldowns will not by themselves stop an item from being used
     * for attacking.
     *
     * @param key cooldown group to set the cooldown for
     * @param ticks the amount of ticks to set or 0 to remove
     * @see UseCooldown#cooldownGroup()
     */
    public void setCooldown(Key key, int ticks);

    /**
     * Get the sleep ticks of the player. This value may be capped.
     *
     * @return slumber ticks
     */
    public int getSleepTicks();


    /**
     * Gets the Location of the player's bed, null if they have not slept
     * in one. This method will not attempt to validate if the current bed
     * is still valid.
     *
     * @return Bed Location if has slept in one, otherwise null.
     * @deprecated Misleading name. This method also returns the location of
     * respawn anchors, use {@link Player#getRespawnLocation(boolean)} with
     * loadLocationAndValidate = false instead
     */
    @Nullable
    @Deprecated(since = "1.21.4")
    default Location getPotentialBedLocation() {
        return this.getPotentialRespawnLocation();
    }

    /**
     * Gets the Location where the player will spawn at, null if they
     * don't have a valid respawn point. This method will not attempt
     * to validate if the current respawn location is still valid.
     *
     * @return respawn location if exists, otherwise null.
     * @deprecated this method doesn't take the respawn angle into account, use
     * {@link Player#getRespawnLocation(boolean)} with loadLocationAndValidate = false instead
     */
    @Deprecated(since = "1.21.5")
    @Nullable Location getPotentialRespawnLocation();

    /**
     * @return the player's fishing hook if they are fishing
     */
    @Nullable
    FishHook getFishHook();

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
     */
    public boolean sleep(Location location, boolean force);

    /**
     * Causes the player to wakeup if they are currently sleeping.
     *
     * @param setSpawnLocation whether to set their spawn location to the bed
     * they are currently sleeping in
     * @throws IllegalStateException if not sleeping
     */
    public void wakeup(boolean setSpawnLocation);

    /**
     * Make the player start a riptide spin attack.
     *
     * @param duration spin attack duration in ticks.
     * @param attackStrength damage value inflicted upon entities hit by spin attack.
     * @param attackItem item used to attack.
     */
    public void startRiptideAttack(int duration, float attackStrength, @Nullable ItemStack attackItem);

    /**
     * Gets the location of the bed the player is currently sleeping in
     *
     * @return location
     * @throws IllegalStateException if not sleeping
     */
    public Location getBedLocation();

    /**
     * Gets this human's current {@link GameMode}
     *
     * @return Current game mode
     */
    public GameMode getGameMode();

    /**
     * Sets this human's current {@link GameMode}
     *
     * @param mode New game mode
     */
    public void setGameMode(GameMode mode);

    /**
     * Check if the player is currently blocking (ie with a shield).
     *
     * @return Whether they are blocking.
     */
    public boolean isBlocking();

    /**
     * Check if the player currently has their hand raised (ie about to begin
     * blocking).
     *
     * @return Whether their hand is raised
     * @see LivingEntity#hasActiveItem()
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.20.4") // Paper - active item API
    public boolean isHandRaised();

    /**
     * Get the total amount of experience required for the player to level
     *
     * @return Experience required to level up
     */
    public int getExpToLevel();

    // Paper start
    /**
     * If there is an Entity on this entities left shoulder, it will be released to the world and returned.
     * If no Entity is released, null will be returned.
     *
     * @return The released entity, or null
     */
    @Nullable
    public Entity releaseLeftShoulderEntity();

    /**
     * If there is an Entity on this entities left shoulder, it will be released to the world and returned.
     * If no Entity is released, null will be returned.
     *
     * @return The released entity, or null
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
     */
    default boolean discoverRecipe(NamespacedKey recipe) {
        return this.discoverRecipes(Arrays.asList(recipe)) != 0;
    }

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
    public int discoverRecipes(Collection<NamespacedKey> recipes);

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
    default boolean undiscoverRecipe(NamespacedKey recipe) {
        return this.undiscoverRecipes(Arrays.asList(recipe)) != 0;
    }

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
    public int undiscoverRecipes(Collection<NamespacedKey> recipes);

    /**
     * Check whether or not this entity has discovered the specified recipe.
     *
     * @param recipe the key of the recipe to check
     *
     * @return true if discovered, false otherwise
     */
    public boolean hasDiscoveredRecipe(NamespacedKey recipe);

    /**
     * Get an immutable set of recipes this entity has discovered.
     *
     * @return all discovered recipes
     */
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
     */
    @Deprecated(since = "1.12")
    public void setShoulderEntityRight(@Nullable Entity entity);
    // Paper start - Add method to open already placed sign
    /**
     * Opens an editor window for the specified sign
     *
     * @param sign The sign to open
     * @deprecated use {@link #openSign(org.bukkit.block.Sign, org.bukkit.block.sign.Side)}
     */
    @Deprecated
    default void openSign(org.bukkit.block.Sign sign) {
        this.openSign(sign, org.bukkit.block.sign.Side.FRONT);
    }

    /**
     * Opens an editor window for the specified sign
     *
     * @param sign The sign to open
     * @param side The side of the sign to open
     */
    void openSign(org.bukkit.block.Sign sign, org.bukkit.block.sign.Side side);
    // Paper end

    /**
     * Make the entity drop the item in their hand.
     * <br>
     * This will force the entity to drop the item they are holding with
     * an option to drop the entire {@link ItemStack} or just 1 of the items.
     *
     * @param dropAll True to drop entire stack, false to drop 1 of the stack
     * @return True if item was dropped successfully
     * @apiNote You should instead use {@link #dropItem(EquipmentSlot, int)} or {@link #dropItem(EquipmentSlot)} with a {@link EquipmentSlot#HAND} parameter.
     */
    @ApiStatus.Obsolete(since = "1.21.4")
    boolean dropItem(boolean dropAll);

    /**
     * Makes the player drop all items from their inventory based on the inventory slot.
     *
     * @param slot the equipment slot to drop
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    default Item dropItem(final int slot) {
        return this.dropItem(slot, Integer.MAX_VALUE);
    }

    /**
     * Makes the player drop an item from their inventory based on the inventory slot.
     *
     * @param slot   the slot to drop
     * @param amount the number of items to drop from this slot. Values below one always return null
     * @return the dropped item entity, or null if the action was unsuccessful
     * @throws IllegalArgumentException if the slot is negative or bigger than the player's inventory
     */
    @Nullable
    default Item dropItem(final int slot, final int amount) {
        return this.dropItem(slot, amount, false, null);
    }

    /**
     * Makes the player drop an item from their inventory based on the inventory slot.
     *
     * @param slot            the slot to drop
     * @param amount          the number of items to drop from this slot. Values below one always return null
     * @param throwRandomly   controls the randomness of the dropped items velocity, where {@code true} mimics dropped
     *                        items during a player's death, while {@code false} acts like a normal item drop.
     * @param entityOperation the function to be run before adding the entity into the world
     * @return the dropped item entity, or null if the action was unsuccessful
     * @throws IllegalArgumentException if the slot is negative or bigger than the player's inventory
     */
    @Nullable
    Item dropItem(int slot, int amount, boolean throwRandomly, @Nullable Consumer<Item> entityOperation);

    /**
     * Makes the player drop all items from their inventory based on the equipment slot.
     *
     * @param slot the equipment slot to drop
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    default Item dropItem(final EquipmentSlot slot) {
        return this.dropItem(slot, Integer.MAX_VALUE);
    }

    /**
     * Makes the player drop an item from their inventory based on the equipment slot.
     *
     * @param slot   the equipment slot to drop
     * @param amount the amount of items to drop from this equipment slot. Values below one always return null
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    default Item dropItem(final EquipmentSlot slot, final int amount) {
        return this.dropItem(slot, amount, false, null);
    }

    /**
     * Makes the player drop an item from their inventory based on the equipment slot.
     *
     * @param slot            the equipment slot to drop
     * @param amount          The amount of items to drop from this equipment slot. Values below one always return null
     * @param throwRandomly   controls the randomness of the dropped items velocity, where {@code true} mimics dropped
     *                        items during a player's death, while {@code false} acts like a normal item drop.
     * @param entityOperation the function to be run before adding the entity into the world
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    Item dropItem(EquipmentSlot slot, int amount, boolean throwRandomly, @Nullable Consumer<Item> entityOperation);

    /**
     * Makes the player drop any arbitrary {@link ItemStack}, independently of whether the player actually
     * has that item in their inventory.
     * <p>
     * This method modifies neither the item nor the player's inventory.
     * Item removal has to be handled by the method caller.
     *
     * @param itemStack the itemstack to drop
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    default Item dropItem(final ItemStack itemStack) {
        return this.dropItem(itemStack, false, null);
    }

    /**
     * Makes the player drop any arbitrary {@link ItemStack}, independently of whether the player actually
     * has that item in their inventory.
     * <p>
     * This method modifies neither the item nor the player's inventory.
     * Item removal has to be handled by the method caller.
     *
     * @param itemStack       the itemstack to drop
     * @param throwRandomly   controls the randomness of the dropped items velocity, where {@code true} mimics dropped
     *                        items during a player's death, while {@code false} acts like a normal item drop.
     * @param entityOperation the function to be run before adding the entity into the world
     * @return the dropped item entity, or null if the action was unsuccessful
     */
    @Nullable
    Item dropItem(final ItemStack itemStack, boolean throwRandomly, @Nullable Consumer<Item> entityOperation);

    /**
     * Gets the players current exhaustion level.
     * <p>
     * Exhaustion controls how fast the food level drops. While you have a
     * certain amount of exhaustion, your saturation will drop to zero, and
     * then your food will drop to zero.
     *
     * @return Exhaustion level
     */
    public float getExhaustion();

    /**
     * Sets the players current exhaustion level
     *
     * @param value Exhaustion level
     */
    public void setExhaustion(float value);

    /**
     * Gets the players current saturation level.
     * <p>
     * Saturation is a buffer for food level. Your food level will not drop if
     * you are saturated {@literal >} 0.
     *
     * @return Saturation level
     */
    public float getSaturation();

    /**
     * Sets the players current saturation level
     *
     * @param value Saturation level
     */
    public void setSaturation(float value);

    /**
     * Gets the players current food level
     *
     * @return Food level
     */
    public int getFoodLevel();

    /**
     * Sets the players current food level
     *
     * @param value New food level
     */
    public void setFoodLevel(int value);

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have saturation and
     * their food level is {@literal >=} 20. Default is 10.
     *
     * @return the regeneration rate
     */
    public int getSaturatedRegenRate();

    /**
     * Set the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have saturation and
     * their food level is {@literal >=} 20. Default is 10.
     * Not affected if the world's difficulty is peaceful.
     *
     * @param ticks the amount of ticks to gain 1 health.
     */
    public void setSaturatedRegenRate(int ticks);

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have no saturation and
     * their food level is {@literal >=} 18. Default is 80.
     *
     * @return the regeneration rate
     */
    public int getUnsaturatedRegenRate();

    /**
     * Get the regeneration rate (1 health per x ticks) of
     * the HumanEntity when they have no saturation and
     * their food level is {@literal >=} 18. Default is 80.
     * Not affected if the world's difficulty is peaceful.
     *
     * @param ticks the amount of ticks to gain 1 health.
     */
    public void setUnsaturatedRegenRate(int ticks);

    /**
     * Get the starvation rate (1 health per x ticks) of
     * the HumanEntity. Default is 80.
     *
     * @return the starvation rate
     */
    public int getStarvationRate();

    /**
     * Get the starvation rate (1 health per x ticks) of
     * the HumanEntity. Default is 80.
     *
     * @param ticks the amount of ticks to lose 1 health
     */
    public void setStarvationRate(int ticks);

    /**
     * Gets the player's last death location.
     *
     * @return the last death location if it exists, otherwise null.
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
     */
    @Nullable
    public Firework fireworkBoost(ItemStack fireworkItemStack);

}
