package org.bukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Translatable;
import org.bukkit.UndefinedNullability;
import org.bukkit.Utility;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a stack of items.
 * <p>
 * <b>IMPORTANT: An <i>Item</i>Stack is only designed to contain <i>items</i>. Do not
 * use this class to encapsulate Materials for which {@link Material#isItem()}
 * returns false.</b>
 */
public class ItemStack implements Cloneable, ConfigurationSerializable, Translatable, net.kyori.adventure.text.event.HoverEventSource<net.kyori.adventure.text.event.HoverEvent.ShowItem>, net.kyori.adventure.translation.Translatable { // Paper
    private Material type = Material.AIR;
    private int amount = 0;
    private MaterialData data = null;
    private ItemMeta meta;

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
     */
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
     */
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
        this.type = type;
        this.amount = amount;
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
     */
    public ItemStack(@NotNull final ItemStack stack) throws IllegalArgumentException {
        Preconditions.checkArgument(stack != null, "Cannot copy null stack");
        this.type = stack.getType();
        this.amount = stack.getAmount();
        if (this.type.isLegacy()) {
            this.data = stack.getData();
        }
        if (stack.hasItemMeta()) {
            setItemMeta0(stack.getItemMeta(), type);
        }
    }

    /**
     * Gets the type of this item
     *
     * @return Type of the items in this stack
     */
    @Utility
    @NotNull
    public Material getType() {
        return type;
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
     */
    @Utility
    public void setType(@NotNull Material type) {
        Preconditions.checkArgument(type != null, "Material cannot be null");
        this.type = type;
        if (this.meta != null) {
            this.meta = Bukkit.getItemFactory().asMetaFor(meta, type);
        }
        if (type.isLegacy()) {
            createData((byte) 0);
        } else {
            this.data = null;
        }
    }

    /**
     * Gets the amount of items in this stack
     *
     * @return Amount of items in this stack
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of items in this stack
     *
     * @param amount New amount of items in this stack
     */
    public void setAmount(int amount) {
        this.amount = amount;
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
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            ((Damageable) meta).setDamage(durability);
            setItemMeta(meta);
        }
    }

    /**
     * Gets the durability of this item
     *
     * @return Durability of this item
     * @deprecated see {@link #setDurability(short)}
     */
    @Deprecated(since = "1.13")
    public short getDurability() {
        ItemMeta meta = getItemMeta();
        return (meta == null) ? 0 : (short) ((Damageable) meta).getDamage();
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
    @Utility
    public int getMaxStackSize() {
        if (meta != null && meta.hasMaxStackSize()) {
            return meta.getMaxStackSize();
        }

        return getType().getMaxStackSize();
    }

    private void createData(final byte data) {
        this.data = type.getNewData(data);
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
    @Utility
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ItemStack)) {
            return false;
        }

        ItemStack stack = (ItemStack) obj;
        return getAmount() == stack.getAmount() && isSimilar(stack);
    }

    /**
     * This method is the same as equals, but does not consider stack size
     * (amount).
     *
     * @param stack the item stack to compare to
     * @return true if the two stacks are equal, ignoring the amount
     */
    @Utility
    public boolean isSimilar(@Nullable ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        Material comparisonType = (this.type.isLegacy()) ? Bukkit.getUnsafe().fromLegacy(this.getData(), true) : this.type; // This may be called from legacy item stacks, try to get the right material
        return comparisonType == stack.getType() && getDurability() == stack.getDurability() && hasItemMeta() == stack.hasItemMeta() && (hasItemMeta() ? Bukkit.getItemFactory().equals(getItemMeta(), stack.getItemMeta()) : true);
    }

    @NotNull
    @Override
    public ItemStack clone() {
        try {
            ItemStack itemStack = (ItemStack) super.clone();

            if (this.meta != null) {
                itemStack.meta = this.meta.clone();
            }

            if (this.data != null) {
                itemStack.data = this.data.clone();
            }

            return itemStack;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    @Utility
    public int hashCode() {
        int hash = 1;

        hash = hash * 31 + getType().hashCode();
        hash = hash * 31 + getAmount();
        hash = hash * 31 + (getDurability() & 0xffff);
        hash = hash * 31 + (hasItemMeta() ? (meta == null ? getItemMeta().hashCode() : meta.hashCode()) : 0);

        return hash;
    }

    /**
     * Checks if this ItemStack contains the given {@link Enchantment}
     *
     * @param ench Enchantment to test
     * @return True if this has the given enchantment
     */
    public boolean containsEnchantment(@NotNull Enchantment ench) {
        return meta == null ? false : meta.hasEnchant(ench);
    }

    /**
     * Gets the level of the specified enchantment on this item stack
     *
     * @param ench Enchantment to check
     * @return Level of the enchantment, or 0
     */
    public int getEnchantmentLevel(@NotNull Enchantment ench) {
        return meta == null ? 0 : meta.getEnchantLevel(ench);
    }

    /**
     * Gets a map containing all enchantments and their levels on this item.
     *
     * @return Map of enchantments.
     */
    @NotNull
    public Map<Enchantment, Integer> getEnchantments() {
        return meta == null ? ImmutableMap.<Enchantment, Integer>of() : meta.getEnchants();
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
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     * @throws IllegalArgumentException if enchantment null, or enchantment is
     *     not applicable
     */
    @Utility
    public void addEnchantment(@NotNull Enchantment ench, int level) {
        Preconditions.checkArgument(ench != null, "Enchantment cannot be null");
        if ((level < ench.getStartLevel()) || (level > ench.getMaxLevel())) {
            throw new IllegalArgumentException("Enchantment level is either too low or too high (given " + level + ", bounds are " + ench.getStartLevel() + " to " + ench.getMaxLevel() + ")");
        } else if (!ench.canEnchantItem(this)) {
            throw new IllegalArgumentException("Specified enchantment cannot be applied to this itemstack");
        }

        addUnsafeEnchantment(ench, level);
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
     * @param ench Enchantment to add
     * @param level Level of the enchantment
     */
    public void addUnsafeEnchantment(@NotNull Enchantment ench, int level) {
        ItemMeta itemMeta = (meta == null ? meta = Bukkit.getItemFactory().getItemMeta(type) : meta);
        if (itemMeta != null) {
            itemMeta.addEnchant(ench, level, true);
        }
    }

    /**
     * Removes the specified {@link Enchantment} if it exists on this
     * ItemStack
     *
     * @param ench Enchantment to remove
     * @return Previous level, or 0
     */
    public int removeEnchantment(@NotNull Enchantment ench) {
        int level = getEnchantmentLevel(ench);
        if (level == 0 || meta == null) {
            return level;
        }
        meta.removeEnchant(ench);
        return level;
    }

    /**
     * Removes all enchantments on this ItemStack.
     */
    public void removeEnchantments() {
        if (meta == null) {
            return;
        }

        meta.removeEnchantments();
    }

    @Override
    @NotNull
    @Utility
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();

        result.put("v", Bukkit.getUnsafe().getDataVersion()); // Include version to indicate we are using modern material names (or LEGACY prefix)
        result.put("type", getType().name());

        if (getAmount() != 1) {
            result.put("amount", getAmount());
        }

        ItemMeta meta = getItemMeta();
        if (!Bukkit.getItemFactory().equals(meta, null)) {
            result.put("meta", meta);
        }

        return result;
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

        if (args.containsKey("enchantments")) { // Backward compatiblity, @deprecated
            Object raw = args.get("enchantments");

            if (raw instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) raw;

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    String stringKey = entry.getKey().toString();
                    stringKey = Bukkit.getUnsafe().get(Enchantment.class, stringKey);
                    NamespacedKey key = NamespacedKey.fromString(stringKey.toLowerCase(Locale.ROOT));

                    Enchantment enchantment = Bukkit.getUnsafe().get(Registry.ENCHANTMENT, key);

                    if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                        result.addUnsafeEnchantment(enchantment, (Integer) entry.getValue());
                    }
                }
            }
        } else if (args.containsKey("meta")) { // We cannot and will not have meta when enchantments (pre-ItemMeta) exist
            Object raw = args.get("meta");
            if (raw instanceof ItemMeta) {
                ((ItemMeta) raw).setVersion(version);
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
        return this.meta == null ? Bukkit.getItemFactory().getItemMeta(this.type) : this.meta.clone();
    }

    /**
     * Checks to see if any meta data has been defined.
     *
     * @return Returns true if some meta data has been set for this item
     */
    public boolean hasItemMeta() {
        return !Bukkit.getItemFactory().equals(meta, null);
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
        return setItemMeta0(itemMeta, type);
    }

    /*
     * Cannot be overridden, so it's safe for constructor call
     */
    private boolean setItemMeta0(@Nullable ItemMeta itemMeta, @NotNull Material material) {
        if (itemMeta == null) {
            this.meta = null;
            return true;
        }
        if (!Bukkit.getItemFactory().isApplicable(itemMeta, material)) {
            return false;
        }
        this.meta = Bukkit.getItemFactory().asMetaFor(itemMeta, material);

        if (this.meta == itemMeta) {
            this.meta = itemMeta.clone();
        }

        return true;
    }

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
     * <p>Levels must be in range {@code [1, 30]}.</p>
     *
     * @param levels levels to use for enchanting
     * @param allowTreasure whether to allow enchantments where {@link org.bukkit.enchantments.Enchantment#isTreasure()} returns true
     * @param random {@link java.util.Random} instance to use for enchanting
     * @return enchanted copy of the provided ItemStack
     * @throws IllegalArgumentException on bad arguments
     */
    @NotNull
    public ItemStack enchantWithLevels(final @org.jetbrains.annotations.Range(from = 1, to = 30) int levels, final boolean allowTreasure, final @NotNull java.util.Random random) {
        return Bukkit.getServer().getItemFactory().enchantWithLevels(this, levels, allowTreasure, random);
    }

    @NotNull
    @Override
    public net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowItem> asHoverEvent(final @NotNull java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowItem> op) {
        return org.bukkit.Bukkit.getServer().getItemFactory().asHoverEvent(this, op);
    }

    /**
     * Get the formatted display name of the {@link ItemStack}.
     *
     * @return display name of the {@link ItemStack}
     */
    public net.kyori.adventure.text.@NotNull Component displayName() {
        return Bukkit.getServer().getItemFactory().displayName(this);
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
    @NotNull
    public static ItemStack deserializeBytes(@NotNull byte[] bytes) {
        return org.bukkit.Bukkit.getUnsafe().deserializeItem(bytes);
    }

    /**
     * Serializes this itemstack to raw bytes in NBT. NBT is safer for data migrations as it will
     * use the built in data converter instead of bukkits dangerous serialization system.
     * @return bytes representing this item in NBT.
     */
    @NotNull
    public byte[] serializeAsBytes() {
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
        if (type == null || type == Material.AIR || !type.isItem()) {
            return 0;
        }
        // Requires access to NMS
        return ensureServerConversions().getMaxItemUseDuration(entity);
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
    // Paper end
}
