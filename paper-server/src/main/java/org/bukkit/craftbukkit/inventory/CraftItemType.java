package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.block.ComposterBlock;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftItemType<M extends ItemMeta> implements ItemType.Typed<M>, Handleable<Item> {

    private final NamespacedKey key;
    private final Item item;
    private final Supplier<CraftItemMetas.ItemMetaData<M>> itemMetaData;

    public static Material minecraftToBukkit(Item item) {
        return CraftMagicNumbers.getMaterial(item);
    }

    public static Item bukkitToMinecraft(Material material) {
        return CraftMagicNumbers.getItem(material);
    }

    public static ItemType minecraftToBukkitNew(Item minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ITEM, Registry.ITEM);
    }

    public static Item bukkitToMinecraftNew(ItemType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public CraftItemType(NamespacedKey key, Item item) {
        this.key = key;
        this.item = item;
        this.itemMetaData = Suppliers.memoize(() -> CraftItemMetas.getItemMetaData(this));
    }

    @NotNull
    @Override
    public Typed<ItemMeta> typed() {
        return this.typed(ItemMeta.class);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <Other extends ItemMeta> Typed<Other> typed(@NotNull final Class<Other> itemMetaType) {
        if (itemMetaType.isAssignableFrom(this.itemMetaData.get().metaClass())) return (Typed<Other>) this;

        throw new IllegalArgumentException("Cannot type item type " + this.key.toString() + " to meta type " + itemMetaType.getSimpleName());
    }

    @NotNull
    @Override
    public ItemStack createItemStack() {
        return this.createItemStack(1, null);
    }

    @NotNull
    @Override
    public ItemStack createItemStack(final int amount) {
        return this.createItemStack(amount, null);
    }

    @NotNull
    @Override
    public ItemStack createItemStack(Consumer<? super M> metaConfigurator) {
        return this.createItemStack(1, metaConfigurator);
    }

    @NotNull
    @Override
    public ItemStack createItemStack(final int amount, @Nullable final Consumer<? super M> metaConfigurator) {
        // Paper start - re-implement to return CraftItemStack
        final net.minecraft.world.item.ItemStack stack = new net.minecraft.world.item.ItemStack(this.item, amount);
        final CraftItemStack mirror = CraftItemStack.asCraftMirror(stack);
        if (metaConfigurator != null) {
            mirror.editMeta(this.getItemMetaClass(), metaConfigurator);
        }
        return mirror;
        // Paper start - reimplement to return CraftItemStack
    }

    @Override
    public Item getHandle() {
        return this.item;
    }

    public M getItemMeta(net.minecraft.world.item.ItemStack itemStack, final java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        return this.itemMetaData.get().fromItemStack().apply(itemStack, extraHandledDcts);
    }

    public M getItemMeta(ItemMeta itemMeta) {
        return this.itemMetaData.get().fromItemMeta().apply(this, (CraftMetaItem) itemMeta);
    }

    @Override
    public boolean hasBlockType() {
        return this.item instanceof BlockItem;
    }

    @NotNull
    @Override
    public BlockType getBlockType() {
        if (!(this.item instanceof BlockItem block)) {
            throw new IllegalStateException("The item type " + this.getKey() + " has no corresponding block type");
        }

        return CraftBlockType.minecraftToBukkitNew(block.getBlock());
    }

    @Override
    public Class<M> getItemMetaClass() {
        if (this == ItemType.AIR) {
            throw new UnsupportedOperationException("Air does not have ItemMeta");
        }
        return this.itemMetaData.get().metaClass();
    }

    @Override
    public int getMaxStackSize() {
        // Based of the material enum air is only 0, in PerMaterialTest it is also set as special case
        // the item info itself would return 64
        if (this == AIR) {
            return 0;
        }
        return this.item.components().getOrDefault(DataComponents.MAX_STACK_SIZE, 64);
    }

    @Override
    public short getMaxDurability() {
        return this.item.components().getOrDefault(DataComponents.MAX_DAMAGE, 0).shortValue();
    }

    @Override
    public boolean isEdible() {
        return this.item.components().has(DataComponents.FOOD);
    }

    @Override
    public boolean isRecord() {
        return this.item.components().has(DataComponents.JUKEBOX_PLAYABLE);
    }

    @Override
    public boolean isFuel() {
        return MinecraftServer.getServer().fuelValues().isFuel(new net.minecraft.world.item.ItemStack(this.item));
    }

    @Override
    public boolean isCompostable() {
        return ComposterBlock.COMPOSTABLES.containsKey(this.item);
    }

    @Override
    public float getCompostChance() {
        Preconditions.checkArgument(this.isCompostable(), "The item type " + this.getKey() + " is not compostable");
        return ComposterBlock.COMPOSTABLES.getFloat(this.item);
    }

    @Override
    public ItemType getCraftingRemainingItem() {
        net.minecraft.world.item.ItemStack expectedItem = this.item.getCraftingRemainder();
        return expectedItem.isEmpty() ? null : CraftItemType.minecraftToBukkitNew(expectedItem.getItem());
    }

//    @Override
//    public EquipmentSlot getEquipmentSlot() {
//        return CraftEquipmentSlot.getSlot(EntityInsentient.getEquipmentSlotForItem(CraftItemStack.asNMSCopy(ItemStack.of(this))));
//    }

    // Paper start - improve default attribute API
    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers() {
        return this.getDefaultAttributeModifiers(sg -> true);
    }
    // Paper end - improve default attribute API

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        // Paper start - improve/fix item default attribute API
        final net.minecraft.world.entity.EquipmentSlot nmsSlot = CraftEquipmentSlot.getNMS(slot);
        return this.getDefaultAttributeModifiers(sg -> sg.test(nmsSlot));
    }

    private Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(final java.util.function.Predicate<net.minecraft.world.entity.EquipmentSlotGroup> slotPredicate) {
        // Paper end - improve/fix item default attribute API
        ImmutableMultimap.Builder<Attribute, AttributeModifier> defaultAttributes = ImmutableMultimap.builder();

        ItemAttributeModifiers nmsDefaultAttributes = this.item.components().getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);
        // Paper start - improve/fix item default attribute API
        for (final net.minecraft.world.item.component.ItemAttributeModifiers.Entry entry : nmsDefaultAttributes.modifiers()) {
            if (!slotPredicate.test(entry.slot())) continue;
            final Attribute attribute = CraftAttribute.minecraftHolderToBukkit(entry.attribute());
            final AttributeModifier modifier = CraftAttributeInstance.convert(entry.modifier(), entry.slot());
            defaultAttributes.put(attribute, modifier);
        }
        // Paper end - improve/fix item default attribute API

        return defaultAttributes.build();
    }

    @Override
    public CreativeCategory getCreativeCategory() {
        return CreativeCategory.BUILDING_BLOCKS;
    }

    @Override
    public boolean isEnabledByFeature(@NotNull World world) {
        Preconditions.checkNotNull(world, "World cannot be null");
        return this.getHandle().isEnabled(((CraftWorld) world).getHandle().enabledFeatures());
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return this.item.getDescriptionId();
    }

    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public Material asMaterial() {
        return Registry.MATERIAL.get(this.key);
    }

    // Paper start - add Translatable
    @Override
    public String translationKey() {
        return this.item.getDescriptionId();
    }
    // Paper end - add Translatable

    // Paper start - expand ItemRarity API
    @Override
    public org.bukkit.inventory.ItemRarity getItemRarity() {
        final net.minecraft.world.item.Rarity rarity = this.item.components().get(DataComponents.RARITY);
        return rarity == null ? null : org.bukkit.inventory.ItemRarity.valueOf(rarity.name());
    }
    // Paper end - expand ItemRarity API
}
