package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.datacomponent.DataComponentHolder;
import io.papermc.paper.registry.RegistryKey;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Translatable;
import org.bukkit.UndefinedNullability;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a stack of items.
 * <p>
 * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain <i>items</i>. Do not
 * use this class to encapsulate Materials for which {@link Material#isItem()}
 * returns false.</b>
 */
public class ItemStack implements Cloneable, ConfigurationSerializable, Translatable, net.kyori.adventure.text.event.HoverEventSource<net.kyori.adventure.text.event.HoverEvent.ShowItem>, net.kyori.adventure.translation.Translatable, io.papermc.paper.persistence.PersistentDataViewHolder, DataComponentHolder { // Paper
    private ItemStack craftDelegate; // Paper - always delegate to server-backed stack
    private MaterialData data = null;

    // Paper start - add static factory methods
    /**
     * Creates an itemstack with the specified item type and a count of 1.
     *
     * @param type the item type to use
     * @return a new itemstack
     * @throws IllegalArgumentException if the Material provided is not an item ({@link Material#isItem()})
     */
    @org.jetbrains.annotations.Contract(value = "_ -> new", pure = true)
    public static @NotNull ItemStack of(final @NotNull Material type) {
        return of(type, 1);
    }

    /**
     * Creates an itemstack with the specified item type and count.
     *
     * @param type the item type to use
     * @param amount the count of items in the stack
     * @return a new itemstack
     * @throws IllegalArgumentException if the Material provided is not an item ({@link Material#isItem()})
     * @throws IllegalArgumentException if the amount is less than 1
     */
    @org.jetbrains.annotations.Contract(value = "_, _ -> new", pure = true)
    public static @NotNull ItemStack of(final @NotNull Material type, final int amount) {
        Preconditions.checkArgument(type.asItemType() != null, "%s isn't an item", type);
        Preconditions.checkArgument(amount > 0, "amount must be greater than 0");
        return java.util.Objects.requireNonNull(type.asItemType(), type + " is not an item").createItemStack(amount); // Paper - delegate
    }
    // Paper end

    // Paper start - pdc
    /**
     * @see #editPersistentDataContainer(Consumer)
     */
    @Override
    public io.papermc.paper.persistence.@NotNull PersistentDataContainerView getPersistentDataContainer() {
        return this.craftDelegate.getPersistentDataContainer();
    }

    /**
     * Edits the {@link PersistentDataContainer} of this stack. The
     * {@link PersistentDataContainer} instance is only valid inside the
     * consumer.
     *
     * @param consumer the persistent data container consumer
     * @return {@code true} if the edit was successful, {@code false} otherwise. Failure to edit the persistent data
     * container may be caused by empty or invalid itemstacks.
     */
    public boolean editPersistentDataContainer(@NotNull Consumer<PersistentDataContainer> consumer) {
        return this.craftDelegate.editPersistentDataContainer(consumer);
    }
    // Paper end - pdc

    @Utility
    protected ItemStack() {}

    /**
     * Defaults stack size to 1, with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     * @apiNote use {@link #of(Material)}
     * @see #of(Material)
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.21") // Paper
    public ItemStack(@NotNull final Material type) {
        this(type, 1);
    }

    /**
     * An item stack with no extra data.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type item material
     * @param amount stack size
     * @apiNote Use {@link #of(Material, int)}
     * @see #of(Material, int)
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.21") // Paper
    public ItemStack(@NotNull final Material type, final int amount) {
        this(type, amount, (short) 0);
    }

    /**
     * An item stack with the specified damage / durability
     *
     * @param type item material
     * @param amount stack size
     * @param damage durability / damage
     * @deprecated see {@link #setDurability(short)}
     */
    @Deprecated(since = "1.20.5")
    public ItemStack(@NotNull final Material type, final int amount, final short damage) {
        this(type, amount, damage, null);
    }

    /**
     * @param type the type
     * @param amount the amount in the stack
     * @param damage the damage value of the item
     * @param data the data value or null
     * @deprecated this method uses an ambiguous data byte object
     */
    @Deprecated(since = "1.4.5", forRemoval = true)
    public ItemStack(@NotNull Material type, final int amount, final short damage, @Nullable final Byte data) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        if (type.isLegacy()) {
            if (type.getMaxDurability() > 0) {
                type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, data == null ? 0 : data), true);
            } else {
                type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, data == null ? (byte) damage : data), true);
            }
        }
        // Paper start - create delegate
        this.craftDelegate = type == Material.AIR ? ItemStack.empty() : ItemStack.of(type, amount);
        // Paper end - create delegate
        if (damage != 0) {
            setDurability(damage);
        }
        if (data != null) {
            createData(data);
        }
    }

    /**
     * Creates a new item stack derived from the specified stack
     *
     * @param stack the stack to copy
     * @throws IllegalArgumentException if the specified stack is null or
     *     returns an item meta not created by the item factory
     * @apiNote Use {@link #clone()}
     * @see #clone()
     */
    @org.jetbrains.annotations.ApiStatus.Obsolete(since = "1.21") // Paper
    public ItemStack(@NotNull final ItemStack stack) throws IllegalArgumentException {
        Preconditions.checkArgument(stack != null, "Cannot copy null stack");
        this.craftDelegate = stack.clone(); // Paper - delegate
        if (stack.getType().isLegacy()) {
            this.data = stack.getData();
        }
    }

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    @NotNull
    public Material getType() {
        return this.craftDelegate.getType(); // Paper - delegate
    }

    /**
     * Sets the type of this item
     * <p>
     * Note that in doing so you will reset the MaterialData for this stack.
     * <p>
     * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain
     * <i>items</i>. Do not use this class to encapsulate Materials for which
     * {@link Material#isItem()} returns false.</b>
     *
     * @param type New type to set the items in this stack to
     * @deprecated <b>Setting the material type of ItemStacks is no longer supported.</b>
     * <p>
     * This method is deprecated due to potential illegal behavior that may occur
     * during the context of which this ItemStack is being used, allowing for certain item validation to be bypassed.
     * It is recommended to instead create a new ItemStack object with the desired
     * Material type, and if possible, set it in the appropriate context.
     *
     * Using this method in ItemStacks passed in events will result in undefined behavior.
     * @see ItemStack#withType(Material)
     */
    @Deprecated // Paper
    public void setType(@NotNull Material type) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        this.craftDelegate.setType(type); // Paper - delegate
    }
    // Paper start
    /**
     * Creates a new ItemStack with the specified Material type, where the item count and item meta is preserved.
     *
     * @param type The Material type of the new ItemStack.
     * @return A new ItemStack instance with the specified Material type.
     */
    @NotNull
    @org.jetbrains.annotations.Contract(value = "_ -> new", pure = true)
    public ItemStack withType(@NotNull Material type) {
        return this.craftDelegate.withType(type); // Paper - delegate
    }
    // Paper end

    /**
     * Gets the amount of items in this stack
     *
     * @return Amount of items in this stack
     */
    public int getAmount() {
        return this.craftDelegate.getAmount(); // Paper - delegate
    }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount New amount of items in this stack
     */
    public void setAmount(int amount) {
        this.craftDelegate.setAmount(amount); // Paper - delegate
    }

    /**
     * Gets the MaterialData for this stack of items
     *
     * @return MaterialData for this item
     * @deprecated cast to {@link org.bukkit.inventory.meta.BlockDataMeta} and use {@link org.bukkit.inventory.meta.BlockDataMeta#getBlockData(Material)}
     */
    @Nullable
    @Deprecated(forRemoval = true, since = "1.13")
    public MaterialData getData() {
        Material mat = Bukkit.getUnsafe().toLegacy(getType());
        if (data == null && mat != null && mat.getData() != null) {
            data = mat.getNewData((byte) this.getDurability());
        }

        return data;
    }

    /**
     * Sets the MaterialData for this stack of items
     *
     * @param data New MaterialData for this item
     * @deprecated cast to {@link org.bukkit.inventory.meta.BlockDataMeta} and use {@link org.bukkit.inventory.meta.BlockDataMeta#setBlockData(org.bukkit.block.data.BlockData)}
     */
    @Deprecated(forRemoval = true, since = "1.13")
    public void setData(@Nullable MaterialData data) {
        if (data == null) {
            this.data = data;
        } else {
            Material mat = Bukkit.getUnsafe().toLegacy(getType());

            if ((data.getClass() == mat.getData()) || (data.getClass() == MaterialData.class)) {
                this.data = data;
            } else {
                throw new IllegalArgumentException("Provided data is not of type " + mat.getData().getName() + ", found " + data.getClass().getName());
            }
        }
    }

    /**
     * Sets the durability of this item
     *
     * @param durability Durability of this item
     * @deprecated durability is now part of ItemMeta. To avoid confusion and
     * misuse, {@link #getItemMeta()}, {@link #setItemMeta(ItemMeta)} and
     * {@link Damageable#setDamage(int)} should be used instead. This is because
     * any call to this method will be overwritten by subsequent setting of
     * ItemMeta which was created before this call.
     */
    @Deprecated(since = "1.13")
    public void setDurability(final short durability) {
        this.craftDelegate.setDurability(durability); // Paper - delegate
    }

    /**
     * Gets the durability of this item
     *
     * @return Durability of this item
     * @deprecated see {@link #setDurability(short)}
     */
    @Deprecated(since = "1.13")
    public short getDurability() {
        return this.craftDelegate.getDurability(); // Paper - delegate
    }

    /**
     * Get the maximum stack size for this item. If this item has a max stack
     * size component ({@link ItemMeta#hasMaxStackSize()}), the value of that
     * component will be returned. Otherwise, this item's Material's {@link
     * Material#getMaxStackSize() default maximum stack size} will be returned
     * instead.
     *
     * @return The maximum you can stack this item to.
     */
    public int getMaxStackSize() {
        return this.craftDelegate.getMaxStackSize(); // Paper - delegate
    }

    private void createData(final byte data) {
        this.data = this.craftDelegate.getType().getNewData(data); // Paper
    }

    @Override
    @Utility
    public String toString() {
        StringBuilder toString = new StringBuilder("ItemStack{").append(getType().name()).append(" x ").append(getAmount());
        if (hasItemMeta()) {
            toString.append(", ").append(getItemMeta());
        }
        return toString.append('}').toString();
    }

    @Override
    public boolean equals(Object obj) {
        return this.craftDelegate.equals(obj); // Paper - delegate
    }

    /**
     * This method is the same as equals, but does not consider stack size
     * (amount).
     *
     * @param stack the item stack to compare to
     * @return true if the two stacks are equal, ignoring the amount
     */
    public boolean isSimilar(@Nullable ItemStack stack) {
        return this.craftDelegate.isSimilar(stack); // Paper - delegate
    }

    @NotNull
    @Override
    public ItemStack clone() {
        return this.craftDelegate.clone(); // Paper - delegate
    }

    @Override
    public int hashCode() {
        return this.craftDelegate.hashCode(); // Paper - delegate
    }

    /**
     * Checks if this ItemStack contains the given {@link Enchantment}
     *
     * @param enchant Enchantment to test
     * @return True if this has the given enchantment
     */
    public boolean containsEnchantment(@NotNull Enchantment enchant) {
        return this.craftDelegate.containsEnchantment(enchant); // Paper - delegate
    }

    /**
     * Gets the level of the specified enchantment on this item stack
     *
     * @param enchant Enchantment to check
     * @return Level of the enchantment, or 0
     */
    public int getEnchantmentLevel(@NotNull Enchantment enchant) {
        return this.craftDelegate.getEnchantmentLevel(enchant); // Paper - delegate
    }

    /**
     * Gets a map containing all enchantments and their levels on this item.
     *
     * @return Map of enchantments.
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return this.craftDelegate.getEnchantments(); // Paper - delegate
    }

    /**
     * Adds the specified enchantments to this item stack.
     * <p>
     * This method is the same as calling {@link
     * #addEnchantment(org.bukkit.enchantments.Enchantment, int)} for each
     * element of the map.
     *
     * @param enchantments Enchantments to add
     * @throws IllegalArgumentException if the specified enchantments is null
     * @throws IllegalArgumentException if any specific enchantment or level
     *     is null. <b>Warning</b>: Some enchantments may be added before this
     *     exception is thrown.
     */
    @Utility
    public void addEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        Preconditions.checkArgument(enchantments != null, "Enchantments cannot be null");
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     *
     * @param enchant Enchantment to add
     * @param level Level of the enchantment
     * @throws IllegalArgumentException if enchantment null, or enchantment is
     *     not applicable
     */
    @Utility
    public void addEnchantment(@NotNull Enchantment enchant, int level) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        if ((level < enchant.getStartLevel()) || (level > enchant.getMaxLevel())) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + enchant.getStartLevel() + " to " + enchant.getMaxLevel() + ")");
        } else if (!enchant.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }

        addUnsafeEnchantment(enchant, level);
    }

    /**
     * Adds the specified enchantments to this item stack in an unsafe manner.
     * <p>
     * This method is the same as calling {@link
     * #addUnsafeEnchantment(org.bukkit.enchantments.Enchantment, int)} for
     * each element of the map.
     *
     * @param enchantments Enchantments to add
     */
    @Utility
    public void addUnsafeEnchantments(@NotNull Map<Enchantment, Integer> enchantments) {
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds the specified {@link Enchantment} to this item stack.
     * <p>
     * If this item stack already contained the given enchantment (at any
     * level), it will be replaced.
     * <p>
     * This method is unsafe and will ignore level restrictions or item type.
     * Use at your own discretion.
     *
     * @param enchant Enchantment to add
     * @param level Level of the enchantment
     */
    public void addUnsafeEnchantment(@NotNull Enchantment enchant, int level) {
        this.craftDelegate.addUnsafeEnchantment(enchant, level); // Paper - delegate
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this
     * ItemStack
     *
     * @param enchant Enchantment to remove
     * @return Previous level, or 0
     */
    public int removeEnchantment(@NotNull Enchantment enchant) {
        return this.craftDelegate.removeEnchantment(enchant); // Paper - delegate
    }

    /**
     * Removes all enchantments on this ItemStack.
     */
    public void removeEnchantments() {
        this.craftDelegate.removeEnchantments(); // Paper - delegate
    }

    @Override
    @NotNull
    @Utility
    public Map<String, Object> serialize() {
        return Bukkit.getUnsafe().serializeStack(this);
    }

    /**
     * Required method for configuration serialization
     *
     * @param args map to deserialize
     * @return deserialized item stack
     * @see ConfigurationSerializable
     */
    @NotNull
    public static ItemStack deserialize(@NotNull Map<String, Object> args) {
        // Parse internally, if schema_version is not defined, assume legacy and fall through to unsafe legacy deserialization logic
        if (args.containsKey("schema_version")) {
            return org.bukkit.Bukkit.getUnsafe().deserializeStack(args);
        }

        int version = (args.containsKey("v")) ? ((Number) args.get("v")).intValue() : -1;
        short damage = 0;
        int amount = 1;

        if (args.containsKey("damage")) {
            damage = ((Number) args.get("damage")).shortValue();
        }

        Material type;
        if (version < 0) {
            type = Material.getMaterial(Material.LEGACY_PREFIX + (String) args.get("type"));

            byte dataVal = (type != null && type.getMaxDurability() == 0) ? (byte) damage : 0; // Actually durable items get a 0 passed into conversion
            type = Bukkit.getUnsafe().fromLegacy(new MaterialData(type, dataVal), true);

            // We've converted now so the data val isn't a thing and can be reset
            if (dataVal != 0) {
                damage = 0;
            }
        } else {
            type = Bukkit.getUnsafe().getMaterial((String) args.get("type"), version);
        }

        if (args.containsKey("amount")) {
            amount = ((Number) args.get("amount")).intValue();
        }

        ItemStack result = new ItemStack(type, amount, damage);

        if (args.containsKey("enchantments")) { // Backward compatibility, @deprecated
            Object raw = args.get("enchantments");

            if (raw instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) raw;

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String stringKey = entry.getKey().toString();
                    stringKey = Bukkit.getUnsafe().get(Enchantment.class, stringKey);
                    NamespacedKey key = NamespacedKey.fromString(stringKey.toLowerCase(Locale.ROOT));

                    Enchantment enchantment = Bukkit.getUnsafe().get(RegistryKey.ENCHANTMENT, key);

                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                ((ItemMeta) raw).setVersion(version);
                // Paper start - for pre 1.20.5 itemstacks, add HIDE_STORED_ENCHANTS flag if HIDE_ADDITIONAL_TOOLTIP is set
                if (version < 3837) { // 1.20.5
                    if (((ItemMeta) raw).hasItemFlag(ItemFlag.HIDE_ADDITIONAL_TOOLTIP)) {
                        ((ItemMeta) raw).addItemFlags(ItemFlag.HIDE_STORED_ENCHANTS);
                    }
                }
                // Paper end
                result.setItemMeta((ItemMeta) raw);
            }
        }

        if (version < 0) {
            // Set damage again incase meta overwrote it
            if (args.containsKey("damage")) {
                result.setDurability(damage);
            }
        }

        return result.ensureServerConversions(); // Paper
    }

    // Paper start
    /**
     * Edits the {@link ItemMeta} of this stack.
     * <p>
     * The {@link java.util.function.Consumer} must only interact
     * with this stack's {@link ItemMeta} through the provided {@link ItemMeta} instance.
     * Calling this method or any other meta-related method of the {@link ItemStack} class
     * (such as {@link #getItemMeta()}, {@link #addItemFlags(ItemFlag...)}, {@link #lore()}, etc.)
     * from inside the consumer is disallowed and will produce undefined results or exceptions.
     * </p>
     *
     * @param consumer the meta consumer
     * @return {@code true} if the edit was successful, {@code false} otherwise
     */
    public boolean editMeta(final @NotNull java.util.function.Consumer<? super ItemMeta> consumer) {
        return editMeta(ItemMeta.class, consumer);
    }

    /**
     * Edits the {@link ItemMeta} of this stack if the meta is of the specified type.
     * <p>
     * The {@link java.util.function.Consumer} must only interact
     * with this stack's {@link ItemMeta} through the provided {@link ItemMeta} instance.
     * Calling this method or any other meta-related method of the {@link ItemStack} class
     * (such as {@link #getItemMeta()}, {@link #addItemFlags(ItemFlag...)}, {@link #lore()}, etc.)
     * from inside the consumer is disallowed and will produce undefined results or exceptions.
     * </p>
     *
     * @param metaClass the type of meta to edit
     * @param consumer the meta consumer
     * @param <M> the meta type
     * @return {@code true} if the edit was successful, {@code false} otherwise
     */
    public <M extends ItemMeta> boolean editMeta(final @NotNull Class<M> metaClass, final @NotNull java.util.function.Consumer<@NotNull ? super M> consumer) {
        final @Nullable ItemMeta meta = this.getItemMeta();
        if (metaClass.isInstance(meta)) {
            consumer.accept((M) meta);
            this.setItemMeta(meta);
            return true;
        }
        return false;
    }
    // Paper end

    /**
     * Get a copy of this ItemStack's {@link ItemMeta}.
     *
     * @return a copy of the current ItemStack's ItemData
     */
    @UndefinedNullability // Paper
    public ItemMeta getItemMeta() {
        return this.craftDelegate.getItemMeta(); // Paper - delegate
    }

    /**
     * Checks to see if any meta data has been defined.
     *
     * @return Returns true if some meta data has been set for this item
     */
    public boolean hasItemMeta() {
        return this.craftDelegate.hasItemMeta(); // Paper - delegate
    }

    /**
     * Set the ItemMeta of this ItemStack.
     *
     * @param itemMeta new ItemMeta, or null to indicate meta data be cleared.
     * @return True if successfully applied ItemMeta, see {@link
     *     ItemFactory#isApplicable(ItemMeta, ItemStack)}
     * @throws IllegalArgumentException if the item meta was not created by
     *     the {@link ItemFactory}
     */
    public boolean setItemMeta(@Nullable ItemMeta itemMeta) {
        return this.craftDelegate.setItemMeta(itemMeta); // Paper - delegate
    }

    // Paper - delegate

    @Override
    @NotNull
    @Deprecated(forRemoval = true) // Paper
    public String getTranslationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    // Paper start
    /**
     * Randomly enchants a copy of this {@link ItemStack} using the given experience levels.
     *
     * <p>If this ItemStack is already enchanted, the existing enchants will be removed before enchanting.</p>
     *
     * <p>Enchantment tables use levels in the range {@code [1, 30]}.</p>
     *
     * @param levels levels to use for enchanting
     * @param allowTreasure whether to allow enchantments where {@link org.bukkit.enchantments.Enchantment#isTreasure()} returns true
     * @param random {@link java.util.Random} instance to use for enchanting
     * @return enchanted copy of the provided ItemStack
     * @throws IllegalArgumentException on bad arguments
     */
    @NotNull
    public ItemStack enchantWithLevels(final int levels, final boolean allowTreasure, final @NotNull java.util.Random random) {
        return Bukkit.getServer().getItemFactory().enchantWithLevels(this, levels, allowTreasure, random);
    }

    /**
     * Randomly enchants a copy of this {@link ItemStack} using the given experience levels.
     *
     * <p>If the provided ItemStack is already enchanted, the existing enchants will be removed before enchanting.</p>
     *
     * <p>Enchantment tables use levels in the range {@code [1, 30]}.</p>
     *
     * @param levels levels to use for enchanting
     * @param keySet registry key set defining the set of possible enchantments, e.g. {@link io.papermc.paper.registry.keys.tags.EnchantmentTagKeys#IN_ENCHANTING_TABLE}.
     * @param random {@link java.util.Random} instance to use for enchanting
     * @return enchanted copy of the provided ItemStack
     * @throws IllegalArgumentException on bad arguments
     */
    public @NotNull ItemStack enchantWithLevels(final int levels, final @NotNull io.papermc.paper.registry.set.RegistryKeySet<@NotNull Enchantment> keySet, final @NotNull java.util.Random random) {
        return Bukkit.getItemFactory().enchantWithLevels(this, levels, keySet, random);
    }

    /**
     * {@inheritDoc}
     *
     * @param op transformation on value
     * @return a hover event
     * @throws IllegalArgumentException if the {@link ItemStack#getAmount()} is not between 1 and 99
     */
    @NotNull
    @Override
    public net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowItem> asHoverEvent(final @NotNull java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowItem> op) {
        return org.bukkit.Bukkit.getServer().getItemFactory().asHoverEvent(this, op);
    }

    /**
     * Get the formatted display name of the {@link ItemStack}.
     *
     * @apiNote this component include a {@link net.kyori.adventure.text.event.HoverEvent item hover event}.
     * When used in chat, make sure to follow the ItemStack rules regarding amount, type, and other properties.
     * @return display name of the {@link ItemStack}
     */
    public net.kyori.adventure.text.@NotNull Component displayName() {
        return Bukkit.getServer().getItemFactory().displayName(this);
    }

    /**
     * Gets the effective name of this item stack shown to player in inventory.
     * It takes into account the display name (with italics) from the item meta,
     * the potion effect, translatable name, rarity etc.
     *
     * @return the effective name of this item stack
     */
    public @NotNull Component effectiveName() {
        return this.craftDelegate.effectiveName();
    }

    /**
     * Minecraft updates are converting simple item stacks into more complex NBT oriented Item Stacks.
     *
     * Use this method to ensure any desired data conversions are processed.
     * The input itemstack will not be the same as the returned itemstack.
     *
     * @return A potentially Data Converted ItemStack
     */
    @NotNull
    public ItemStack ensureServerConversions() {
        return Bukkit.getServer().getItemFactory().ensureServerConversions(this);
    }

    /**
     * Deserializes this itemstack from raw NBT bytes. NBT is safer for data migrations as it will
     * use the built in data converter instead of bukkits dangerous serialization system.
     *
     * This expects that the DataVersion was stored on the root of the Compound, as saved from
     * the {@link #serializeAsBytes()} API returned.
     * @param bytes bytes representing an item in NBT
     * @return ItemStack migrated to this version of Minecraft if needed.
     */
    public static @NotNull ItemStack deserializeBytes(final byte @NotNull [] bytes) {
        return org.bukkit.Bukkit.getUnsafe().deserializeItem(bytes);
    }

    /**
     * Serializes this itemstack to raw bytes in NBT. NBT is safer for data migrations as it will
     * use the built in data converter instead of bukkits dangerous serialization system.
     * @return bytes representing this item in NBT.
     */
    public byte @NotNull [] serializeAsBytes() {
        return org.bukkit.Bukkit.getUnsafe().serializeItem(this);
    }

    /**
     * The current version byte of the item array format used in {@link #serializeItemsAsBytes(java.util.Collection)}
     * and {@link #deserializeItemsFromBytes(byte[])} respectively.
     */
    private static final byte ARRAY_SERIALIZATION_VERSION = 1;

    /**
     * Serializes a collection of items to raw bytes in NBT. Serializes null items as {@link #empty()}.
     * <p>
     * If you need a string representation to put into a file, you can for example use {@link java.util.Base64} encoding.
     *
     * @param items items to serialize
     * @return bytes representing the items in NBT
     * @see #serializeAsBytes()
     */
    public static byte @NotNull [] serializeItemsAsBytes(java.util.@NotNull Collection<ItemStack> items) {
        try (final java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream()) {
            final java.io.DataOutput output = new java.io.DataOutputStream(outputStream);
            output.writeByte(ARRAY_SERIALIZATION_VERSION);
            output.writeInt(items.size());
            for (final ItemStack item : items) {
                if (item == null || item.isEmpty()) {
                    // Ensure the correct order by including empty/null items
                    output.writeInt(0);
                    continue;
                }

                final byte[] itemBytes = item.serializeAsBytes();
                output.writeInt(itemBytes.length);
                output.write(itemBytes);
            }
            return outputStream.toByteArray();
        } catch (final java.io.IOException e) {
            throw new RuntimeException("Error while writing itemstack", e);
        }
    }

    /**
     * Serializes a collection of items to raw bytes in NBT. Serializes null items as {@link #empty()}.
     * <p>
     * If you need a string representation to put into a file, you can for example use {@link java.util.Base64} encoding.
     *
     * @param items items to serialize
     * @return bytes representing the items in NBT
     * @see #serializeAsBytes()
     */
    public static byte @NotNull [] serializeItemsAsBytes(@Nullable ItemStack @NotNull [] items) {
        return serializeItemsAsBytes(java.util.Arrays.asList(items));
    }

    /**
     * Deserializes this itemstack from raw NBT bytes.
     * <p>
     * If you need a string representation to put into a file, you can for example use {@link java.util.Base64} encoding.
     *
     * @param bytes bytes representing an item in NBT
     * @return ItemStack array migrated to this version of Minecraft if needed
     * @see #deserializeBytes(byte[])
     */
    public static @NotNull ItemStack @NotNull [] deserializeItemsFromBytes(final byte @NotNull [] bytes) {
        try (final java.io.ByteArrayInputStream inputStream = new java.io.ByteArrayInputStream(bytes)) {
            final java.io.DataInputStream input = new java.io.DataInputStream(inputStream);
            final byte version = input.readByte();
            if (version != ARRAY_SERIALIZATION_VERSION) {
                throw new IllegalArgumentException("Unsupported version or bad data: " + version);
            }

            final int count = input.readInt();
            final ItemStack[] items = new ItemStack[count];
            for (int i = 0; i < count; i++) {
                final int length = input.readInt();
                if (length == 0) {
                    // Empty item, keep entry as empty
                    items[i] = ItemStack.empty();
                    continue;
                }

                final byte[] itemBytes = new byte[length];
                input.read(itemBytes);
                items[i] = ItemStack.deserializeBytes(itemBytes);
            }
            return items;
        } catch (final java.io.IOException e) {
            throw new RuntimeException("Error while reading itemstack", e);
        }
    }

    /**
     * Gets the Display name as seen in the Client.
     * Currently the server only supports the English language. To override this,
     * You must replace the language file embedded in the server jar.
     *
     * @return Display name of Item
     * @deprecated {@link ItemStack} implements {@link net.kyori.adventure.translation.Translatable}; use that and
     * {@link net.kyori.adventure.text.Component#translatable(net.kyori.adventure.translation.Translatable)} instead.
     */
    @Nullable
    @Deprecated
    public String getI18NDisplayName() {
        return Bukkit.getServer().getItemFactory().getI18NDisplayName(this);
    }

    /**
     * @deprecated use {@link #getMaxItemUseDuration(org.bukkit.entity.LivingEntity)}; crossbows, later possibly more items require an entity parameter
     */
    @Deprecated(forRemoval = true)
    public int getMaxItemUseDuration() {
        return getMaxItemUseDuration(null);
    }

    public int getMaxItemUseDuration(@NotNull final org.bukkit.entity.LivingEntity entity) {
        return this.craftDelegate.getMaxItemUseDuration(entity); // Paper - delegate
    }

    /**
     * Clones the itemstack and returns it a single quantity.
     * @return The new itemstack with 1 quantity
     */
    @NotNull
    public ItemStack asOne() {
        return asQuantity(1);
    }

    /**
     * Clones the itemstack and returns it as the specified quantity
     * @param qty The quantity of the cloned item
     * @return The new itemstack with specified quantity
     */
    @NotNull
    public ItemStack asQuantity(int qty) {
        ItemStack clone = clone();
        clone.setAmount(qty);
        return clone;
    }

    /**
     * Adds 1 to this itemstack. Will not go over the items max stack size.
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack add() {
        return add(1);
    }

    /**
     * Adds quantity to this itemstack. Will not go over the items max stack size.
     *
     * @param qty The amount to add
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack add(int qty) {
        setAmount(Math.min(getMaxStackSize(), getAmount() + qty));
        return this;
    }

    /**
     * Subtracts 1 to this itemstack.  Going to 0 or less will invalidate the item.
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack subtract() {
        return subtract(1);
    }

    /**
     * Subtracts quantity to this itemstack. Going to 0 or less will invalidate the item.
     *
     * @param qty The amount to add
     * @return The same item (not a clone)
     */
    @NotNull
    public ItemStack subtract(int qty) {
        setAmount(Math.max(0, getAmount() - qty));
        return this;
    }

    /**
     * If the item has lore, returns it, else it will return null
     * @return The lore, or null
     * @deprecated in favor of {@link #lore()}
     */
    @Deprecated
    public @Nullable java.util.List<String> getLore() {
        if (!hasItemMeta()) {
            return null;
        }
        ItemMeta itemMeta = getItemMeta();
        if (!itemMeta.hasLore()) {
            return null;
        }
        return itemMeta.getLore();
    }

    /**
     * If the item has lore, returns it, else it will return null
     * @return The lore, or null
     */
    public @Nullable java.util.List<net.kyori.adventure.text.Component> lore() {
        if (!this.hasItemMeta()) {
            return null;
        }
        final ItemMeta itemMeta = getItemMeta();
        if (!itemMeta.hasLore()) {
            return null;
        }
        return itemMeta.lore();
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     * @deprecated in favour of {@link #lore(java.util.List)}
     */
    @Deprecated
    public void setLore(@Nullable java.util.List<String> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot set lore on " + getType());
        }
        itemMeta.setLore(lore);
        setItemMeta(itemMeta);
    }

    /**
     * Sets the lore for this item.
     * Removes lore when given null.
     *
     * @param lore the lore that will be set
     */
    public void lore(@Nullable java.util.List<? extends net.kyori.adventure.text.Component> lore) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot set lore on " + getType());
        }
        itemMeta.lore(lore);
        this.setItemMeta(itemMeta);
    }

    /**
     * Set itemflags which should be ignored when rendering a ItemStack in the Client. This Method does silently ignore double set itemFlags.
     *
     * @param itemFlags The hideflags which shouldn't be rendered
     */
    public void addItemFlags(@NotNull ItemFlag... itemFlags) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot add flags on " + getType());
        }
        itemMeta.addItemFlags(itemFlags);
        setItemMeta(itemMeta);
    }

    /**
     * Remove specific set of itemFlags. This tells the Client it should render it again. This Method does silently ignore double removed itemFlags.
     *
     * @param itemFlags Hideflags which should be removed
     */
    public void removeItemFlags(@NotNull ItemFlag... itemFlags) {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            throw new IllegalStateException("Cannot remove flags on " + getType());
        }
        itemMeta.removeItemFlags(itemFlags);
        setItemMeta(itemMeta);
    }

    /**
     * Get current set itemFlags. The collection returned is unmodifiable.
     *
     * @return A set of all itemFlags set
     */
    @NotNull
    public java.util.Set<ItemFlag> getItemFlags() {
        ItemMeta itemMeta = getItemMeta();
        if (itemMeta == null) {
            return java.util.Collections.emptySet();
        }
        return itemMeta.getItemFlags();
    }

    /**
     * Check if the specified flag is present on this item.
     *
     * @param flag the flag to check
     * @return if it is present
     */
    public boolean hasItemFlag(@NotNull ItemFlag flag) {
        ItemMeta itemMeta = getItemMeta();
        return itemMeta != null && itemMeta.hasItemFlag(flag);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This is not the same as getting the translation key
     * for the material of this itemstack.
     */
    @Override
    public @NotNull String translationKey() {
        return Bukkit.getUnsafe().getTranslationKey(this);
    }

    /**
     * Gets the item rarity of the itemstack. The rarity can change based on enchantments.
     *
     * @return the itemstack rarity
     * @deprecated Use {@link ItemMeta#hasRarity()} and {@link ItemMeta#getRarity()}
     */
    @NotNull
    @Deprecated(forRemoval = true, since = "1.20.5")
    public io.papermc.paper.inventory.ItemRarity getRarity() {
        return io.papermc.paper.inventory.ItemRarity.valueOf(this.getItemMeta().getRarity().name());
    }

    /**
     * Checks if an itemstack can repair this itemstack.
     * Returns false if {@code this} or {@code repairMaterial}'s type is not an item ({@link Material#isItem()}).
     *
     * @param repairMaterial the repair material
     * @return true if it is repairable by, false if not
     */
    public boolean isRepairableBy(@NotNull ItemStack repairMaterial) {
        return Bukkit.getUnsafe().isValidRepairItemStack(this, repairMaterial);
    }

    /**
     * Checks if this itemstack can repair another.
     * Returns false if {@code this} or {@code toBeRepaired}'s type is not an item ({@link Material#isItem()}).
     *
     * @param toBeRepaired the itemstack to be repaired
     * @return true if it can repair, false if not
     */
    public boolean canRepair(@NotNull ItemStack toBeRepaired) {
        return Bukkit.getUnsafe().isValidRepairItemStack(toBeRepaired, this);
    }

    /**
     * Damages this itemstack by the specified amount. This
     * runs all logic associated with damaging an itemstack like
     * events and stat changes.
     *
     * @param amount the amount of damage to do
     * @param livingEntity the entity related to the damage
     * @return the damaged itemstack or an empty one if it broke. May return the same instance of ItemStack
     * @see org.bukkit.entity.LivingEntity#damageItemStack(EquipmentSlot, int) to damage itemstacks in equipment slots
     */
    public @NotNull ItemStack damage(int amount, @NotNull org.bukkit.entity.LivingEntity livingEntity) {
        return livingEntity.damageItemStack(this, amount);
    }

    /**
     * Returns an empty item stack, consists of an air material and a stack size of 0.
     *
     * Any item stack with a material of air or a stack size of 0 is seen
     * as being empty by {@link ItemStack#isEmpty}.
     */
    @NotNull
    public static ItemStack empty() {
        //noinspection deprecation
        return Bukkit.getUnsafe().createEmptyStack(); // Paper - proxy ItemStack
    }

    /**
     * Returns whether this item stack is empty and contains no item. This means
     * it is either air or the stack has a size of 0.
     */
    public boolean isEmpty() {
        return this.craftDelegate.isEmpty(); // Paper - delegate
    }
    // Paper end
    // Paper start - expose itemstack tooltip lines
    /**
     * Computes the tooltip lines for this stack.
     * <p>
     * <b>Disclaimer:</b>
     * Tooltip contents are not guaranteed to be consistent across different
     * Minecraft versions.
     *
     * @param tooltipContext the tooltip context
     * @param player a player for player-specific tooltip lines
     * @return an immutable list of components (can be empty)
     */
    @SuppressWarnings("deprecation") // abusing unsafe as a bridge
    public java.util.@NotNull @org.jetbrains.annotations.Unmodifiable List<net.kyori.adventure.text.Component> computeTooltipLines(final @NotNull io.papermc.paper.inventory.tooltip.TooltipContext tooltipContext, final @Nullable org.bukkit.entity.Player player) {
        return Bukkit.getUnsafe().computeTooltipLines(this, tooltipContext, player);
    }
    // Paper end - expose itemstack tooltip lines

    // Paper start - data component API
    /**
     * Gets the value for the data component type on this stack.
     *
     * @param type the data component type
     * @param <T> the value type
     * @return the value for the data component type, or {@code null} if not set or marked as removed
     * @see #hasData(io.papermc.paper.datacomponent.DataComponentType) for DataComponentType.NonValued
     */
    @org.jetbrains.annotations.Contract(pure = true)
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> @Nullable T getData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type) {
        return this.craftDelegate.getData(type);
    }

    /**
     * Gets the value for the data component type on this stack with
     * a fallback value.
     *
     * @param type the data component type
     * @param fallback the fallback value if the value isn't present
     * @param <T> the value type
     * @return the value for the data component type or the fallback value
     */
    @Utility
    @org.jetbrains.annotations.Contract(value = "_, !null -> !null", pure = true)
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> @Nullable T getDataOrDefault(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<? extends T> type, final @Nullable T fallback) {
        final T object = this.getData(type);
        return object != null ? object : fallback;
    }

    /**
     * Checks if the data component type is set on the itemstack.
     *
     * @param type the data component type
     * @return {@code true} if set, {@code false} otherwise
     */
    @org.jetbrains.annotations.Contract(pure = true)
    @org.jetbrains.annotations.ApiStatus.Experimental
    public boolean hasData(final io.papermc.paper.datacomponent.@NotNull DataComponentType type) {
        return this.craftDelegate.hasData(type);
    }

    /**
     * Gets all the data component types set on this stack.
     *
     * @return an immutable set of data component types
     */
    @org.jetbrains.annotations.Contract("-> new")
    @org.jetbrains.annotations.ApiStatus.Experimental
    public java.util.@org.jetbrains.annotations.Unmodifiable Set<io.papermc.paper.datacomponent.@NotNull DataComponentType> getDataTypes() {
        return this.craftDelegate.getDataTypes();
    }

    /**
     * Sets the value of the data component type for this itemstack. To
     * reset the value to the default for the {@link #getType() item type}, use
     * {@link #resetData(io.papermc.paper.datacomponent.DataComponentType)}. To mark the data component type
     * as removed, use {@link #unsetData(io.papermc.paper.datacomponent.DataComponentType)}.
     *
     * @param type the data component type
     * @param valueBuilder value builder
     * @param <T> value type
     */
    @Utility
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type, final @NotNull io.papermc.paper.datacomponent.DataComponentBuilder<T> valueBuilder) {
        this.setData(type, valueBuilder.build());
    }

    // /**
    //  * Modifies the value of the specified data component type for this item stack based on the result
    //  * of applying a given function to the current value.
    //  *
    //  * <p>If the function returns {@code null}, the data component type will be reset using
    //  * {@link #unsetData(DataComponentType)}. Otherwise, the
    //  * component value will be updated with the new result using {@link #setData(DataComponentType.Valued, Object)}.</p>
    //  *
    //  * @param <T>      the type of the data component's value
    //  * @param type     the data component type to be modified
    //  * @param consumer a function that takes the current component value (can be {@code null}) and
    //  *                 returns the modified value (or {@code null} to unset)
    //  */
    // @Utility
    // public <T> void editData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type, final @NotNull java.util.function.Function<@Nullable T, @Nullable T> consumer) {
    //     T value = getData(type);
    //     T newType = consumer.apply(value);
    //     if (newType == null) {
    //         unsetData(type);
    //     } else {
    //         setData(type, newType);
    //     }
    // }

    /**
     * Sets the value of the data component type for this itemstack. To
     * reset the value to the default for the {@link #getType() item type}, use
     * {@link #resetData(io.papermc.paper.datacomponent.DataComponentType)}. To mark the data component type
     * as removed, use {@link #unsetData(io.papermc.paper.datacomponent.DataComponentType)}.
     *
     * @param type the data component type
     * @param value value to set
     * @param <T> value type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public <T> void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull Valued<T> type, final @NotNull T value) {
        this.craftDelegate.setData(type, value);
    }

    /**
     * Marks this non-valued data component type as present in this itemstack.
     *
     * @param type the data component type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void setData(final io.papermc.paper.datacomponent.DataComponentType.@NotNull NonValued type) {
        this.craftDelegate.setData(type);
    }

    /**
     * Marks this data component as removed for this itemstack.
     *
     * @param type the data component type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void unsetData(final io.papermc.paper.datacomponent.@NotNull DataComponentType type) {
        this.craftDelegate.unsetData(type);
    }

    /**
     * Resets the value of this component to be the default
     * value for the item type from {@link Material#getDefaultData(io.papermc.paper.datacomponent.DataComponentType.Valued)}.
     *
     * @param type the data component type
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void resetData(final io.papermc.paper.datacomponent.@NotNull DataComponentType type) {
        this.craftDelegate.resetData(type);
    }

    /**
     * Copies component values and component removals from the provided ItemStack.
     * <p>
     * Example:
     * <pre>{@code
     * Set<DataComponentType> types = Set.of(
     *     DataComponentTypes.CONSUMABLE,
     *     DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE,
     *     DataComponentTypes.RARITY
     * );
     *
     * ItemStack source = ItemStack.of(Material.ENCHANTED_GOLDEN_APPLE);
     * ItemStack target = ItemStack.of(Material.GOLDEN_CARROT);
     *
     * target.copyDataFrom(source, types::contains);
     * }</pre>
     *
     * @param source the item stack to copy from
     * @param filter predicate for which components to copy
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public void copyDataFrom(final @NotNull ItemStack source, final @NotNull Predicate<io.papermc.paper.datacomponent.@NotNull DataComponentType> filter) {
        this.craftDelegate.copyDataFrom(source, filter);
    }

    /**
     * Checks if the data component type is overridden from the default for the
     * item type.
     *
     * @param type the data component type
     * @return {@code true} if the data type is overridden
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public boolean isDataOverridden(final io.papermc.paper.datacomponent.@NotNull DataComponentType type) {
        return this.craftDelegate.isDataOverridden(type);
    }

    /**
     * Checks if this itemstack matches another given itemstack excluding the provided components.
     * This is useful if you are wanting to ignore certain properties of itemstacks, such as durability.
     *
     * @param item the item to compare
     * @param excludeTypes the data component types to ignore
     * @return {@code true} if the provided item is equal, ignoring the provided components
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public boolean matchesWithoutData(final @NotNull ItemStack item, final @NotNull java.util.Set<io.papermc.paper.datacomponent.@NotNull DataComponentType> excludeTypes) {
        return this.matchesWithoutData(item, excludeTypes, false);
    }

    /**
     * Checks if this itemstack matches another given itemstack excluding the provided components.
     * This is useful if you are wanting to ignore certain properties of itemstacks, such as durability.
     *
     * @param item the item to compare
     * @param excludeTypes the data component types to ignore
     * @param ignoreCount ignore the count of the item
     * @return {@code true} if the provided item is equal, ignoring the provided components
     */
    @org.jetbrains.annotations.ApiStatus.Experimental
    public boolean matchesWithoutData(final @NotNull ItemStack item, final @NotNull java.util.Set<io.papermc.paper.datacomponent.@NotNull DataComponentType> excludeTypes, final boolean ignoreCount) {
        return this.craftDelegate.matchesWithoutData(item, excludeTypes, ignoreCount);
    }
    // Paper end - data component API
}
