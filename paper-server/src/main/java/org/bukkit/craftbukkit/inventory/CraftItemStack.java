package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.PaperDataComponentType;
import io.papermc.paper.inventory.tooltip.TooltipContext;
import io.papermc.paper.persistence.PaperPersistentDataContainerView;
import io.papermc.paper.persistence.PersistentDataContainerView;
import io.papermc.paper.util.MCUtil;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.text.Component;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentExactPredicate;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

@DelegateDeserialization(ItemStack.class)
public final class CraftItemStack extends ItemStack {

    // Paper start - delegate api-ItemStack to CraftItemStack
    private static final VarHandle API_ITEM_STACK_CRAFT_DELEGATE_FIELD;
    static {
        try {
            API_ITEM_STACK_CRAFT_DELEGATE_FIELD = MethodHandles.privateLookupIn(
                ItemStack.class,
                MethodHandles.lookup()
            ).findVarHandle(ItemStack.class, "craftDelegate", ItemStack.class);
        } catch (final IllegalAccessException | NoSuchFieldException exception) {
            throw new RuntimeException(exception);
        }
    }

    private static CraftItemStack getCraftStack(final ItemStack bukkit) {
        if (bukkit instanceof final CraftItemStack craftItemStack) {
            return craftItemStack;
        } else {
            return (CraftItemStack) API_ITEM_STACK_CRAFT_DELEGATE_FIELD.get(bukkit);
        }
    }

    @Override
    public int hashCode() {
        if (this.handle == null || this.handle.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY.hashCode();
        } else {
            int hash = net.minecraft.world.item.ItemStack.hashItemAndComponents(this.handle);
            hash = hash * 31 + this.handle.getCount();
            return hash;
        }
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof final ItemStack bukkit)) return false;
        final CraftItemStack craftStack = getCraftStack(bukkit);
        if (this.handle == craftStack.handle) return true;
        if (this.handle == null || craftStack.handle == null) return false;
        if (this.handle.isEmpty() && craftStack.handle.isEmpty()) return true;
        return net.minecraft.world.item.ItemStack.matches(this.handle, craftStack.handle);
    }
    // Paper end

    // Paper start - MC Utils
    public static net.minecraft.world.item.ItemStack unwrap(ItemStack bukkit) {
        // Paper start - re-implement after delegating all api ItemStack calls to CraftItemStack
        final CraftItemStack craftItemStack = getCraftStack(bukkit);
        return craftItemStack.handle == null ? net.minecraft.world.item.ItemStack.EMPTY : craftItemStack.handle;
        // Paper end - re-implement after delegating all api ItemStack calls to CraftItemStack
    }

    public static net.minecraft.world.item.ItemStack getOrCloneOnMutation(ItemStack initial, ItemStack current) {
        return initial == current ? unwrap(initial) : asNMSCopy(current);
    }
    // Paper end - MC Utils

    // Paper start - override isEmpty to use vanilla's impl
    @Override
    public boolean isEmpty() {
        return handle == null || handle.isEmpty();
    }
    // Paper end - override isEmpty to use vanilla's impl

    public static net.minecraft.world.item.ItemStack asNMSCopy(@Nullable ItemStack original) {
        // Paper start - re-implement after delegating all api ItemStack calls to CraftItemStack
        if (original == null || original.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        final CraftItemStack stack = getCraftStack(original);
        return stack.handle == null ? net.minecraft.world.item.ItemStack.EMPTY : stack.handle.copy();
        // Paper end - re-implement after delegating all api ItemStack calls to CraftItemStack
    }

    public static List<net.minecraft.world.item.ItemStack> asNMSCopy(List<? extends ItemStack> originals) {
        final List<net.minecraft.world.item.ItemStack> items = new ArrayList<>(originals.size());
        for (final ItemStack original : originals) {
            items.add(asNMSCopy(original));
        }
        return items;
    }

    public static ItemStackTemplate asTemplate(ItemStack bukkit) {
        return ItemStackTemplate.fromNonEmptyStack(asNMSCopy(bukkit));
    }

    public static net.minecraft.world.item.ItemStack copyNMSStack(net.minecraft.world.item.ItemStack original, int amount) {
        net.minecraft.world.item.ItemStack stack = original.copy();
        stack.setCount(amount);
        return stack;
    }

    /**
     * Copies the NMS stack to return as a strictly-Bukkit stack
     */
    public static ItemStack asBukkitCopy(net.minecraft.world.item.ItemStack original) {
        // no such thing as a "strictly-Bukkit stack" anymore
        // we copy the stack since it should be a complete copy not a mirror
        return asCraftMirror(original.copy());
    }

    public static ItemStack asBukkitCopy(ItemStackTemplate template) {
        return asCraftMirror(template.create()); // No need to copy the result again
    }

    public static ItemStack asBukkitCopy(ItemInstance original) {
        return switch (original) {
            case ItemStackTemplate template -> asBukkitCopy(template);
            case net.minecraft.world.item.ItemStack item -> asBukkitCopy(item);
            default -> throw new AssertionError();
        };
    }

    public static CraftItemStack asCraftMirror(net.minecraft.world.item.ItemStack original) {
        return new CraftItemStack((original == null || original.isEmpty()) ? null : original);
    }

    public static CraftItemStack asCraftCopy(ItemStack original) {
        if (original instanceof CraftItemStack) {
            CraftItemStack stack = (CraftItemStack) original;
            return new CraftItemStack(stack.handle == null ? null : stack.handle.copy());
        }
        return new CraftItemStack(original);
    }

    public static CraftItemStack asNewCraftStack(Item item) {
        return CraftItemStack.asNewCraftStack(item, 1);
    }

    public static CraftItemStack asNewCraftStack(Item item, int amount) {
        return new CraftItemStack(CraftItemType.minecraftToBukkit(item), amount, (short) 0, null);
    }

    public static ItemPredicate asCriterionConditionItem(ItemStack key) {
        net.minecraft.world.item.ItemStack item = CraftItemStack.unwrap(key);

        return ItemPredicate.Builder.item()
            .of(CraftRegistry.getMinecraftRegistry(Registries.ITEM), item.getItem())
            .withComponents(DataComponentMatchers.Builder.components()
                .exact(DataComponentExactPredicate.allOf(
                    PatchedDataComponentMap.fromPatch(DataComponentMap.EMPTY, item.getComponentsPatch())
                ))
                .build())
            .build();
    }

    public net.minecraft.world.item.ItemStack handle;

    private CraftItemStack(net.minecraft.world.item.ItemStack item) {
        this.handle = item;
    }

    private CraftItemStack(ItemStack item) {
        this(item.getType(), item.getAmount(), item.getDurability(), item.hasItemMeta() ? item.getItemMeta() : null);
    }

    private CraftItemStack(Material type, int amount, short durability, ItemMeta itemMeta) {
        this.setType(type);
        this.setAmount(amount);
        this.setDurability(durability);
        this.setItemMeta(itemMeta);
    }

    @Override
    public MaterialData getData() {
        return this.handle != null ? CraftMagicNumbers.getMaterialData(this.handle.getItem()) : super.getData();
    }

    @Override
    public Material getType() {
        return this.handle != null ? CraftItemType.minecraftToBukkit(this.handle.getItem()) : Material.AIR;
    }

    @Override
    public void setType(Material type) {
        if (this.getType() == type) {
            return;
        } else if (type == Material.AIR) {
            this.handle = null;
        } else if (CraftItemType.bukkitToMinecraft(type) == null) { // :(
            this.handle = null;
        } else if (this.handle == null) {
            this.handle = new net.minecraft.world.item.ItemStack(CraftItemType.bukkitToMinecraft(type), 1);
        } else {
            final Material oldType = CraftMagicNumbers.getMaterial(this.handle.getItem()); // Paper
            this.handle.setItem(CraftItemType.bukkitToMinecraft(type));
            if (this.hasItemMeta()) {
                // This will create the appropriate item meta, which will contain all the data we intend to keep
                this.adjustTagForItemMeta(oldType); // Paper
            }
        }
        this.setData((MaterialData) null); // Paper
    }

    @Override
    public int getAmount() {
        return this.handle != null ? this.handle.getCount() : 0;
    }

    @Override
    public void setAmount(int amount) {
        if (this.handle == null) {
            return;
        }

        this.handle.setCount(amount);
    }

    @Override
    public void setDurability(final short durability) {
        // Ignore damage if item is null
        if (this.handle != null) {
            this.handle.setDamageValue(durability);
        }
    }

    @Override
    public short getDurability() {
        if (this.handle != null) {
            return (short) this.handle.getDamageValue();
        } else {
            return -1;
        }
    }

    @Override
    public int getMaxStackSize() {
        return (this.handle == null) ? Item.DEFAULT_MAX_STACK_SIZE : this.handle.getMaxStackSize(); // Paper - air stacks to 64
    }

    @Override
    public byte @NotNull [] serializeAsBytes() {
        Preconditions.checkArgument(!this.isEmpty(), "Empty item cannot be serialized");

        return MCUtil.serializeTagToBytes(
            (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
                CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE),
                this.handle
            ).getOrThrow()
        );
    }

    @Override
    public int getMaxItemUseDuration(final LivingEntity entity) {
        if (this.handle == null) {
            return 0;
        }

        return this.handle.getUseDuration(entity != null ? ((CraftLivingEntity) entity).getHandle() : null); // TODO - check on each update if passing null is fine
    }

    @Override
    public void addUnsafeEnchantment(Enchantment enchant, int level) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");

        if (this.handle == null) {
            return;
        }

        EnchantmentHelper.updateEnchantments(this.handle, mutable -> { // data component api doesn't really support mutable things once already set yet
            mutable.set(CraftEnchantment.bukkitToMinecraftHolder(enchant), level);
        }, true);
    }

    @Override
    public boolean containsEnchantment(Enchantment enchant) {
        return this.getEnchantmentLevel(enchant) > 0;
    }

    @Override
    public int getEnchantmentLevel(Enchantment enchant) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        if (this.handle == null) {
            return 0;
        }
        return EnchantmentHelper.getItemEnchantmentLevel(CraftEnchantment.bukkitToMinecraftHolder(enchant), this.handle);
    }

    @Override
    public int removeEnchantment(Enchantment enchant) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");

        if (this.handle == null) {
            return 0;
        }

        ItemEnchantments itemEnchantments = this.handle.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (itemEnchantments.isEmpty()) {
            return 0;
        }

        Holder<net.minecraft.world.item.enchantment.Enchantment> removedEnchantment = CraftEnchantment.bukkitToMinecraftHolder(enchant);
        if (itemEnchantments.keySet().contains(removedEnchantment)) {
            int previousLevel = itemEnchantments.getLevel(removedEnchantment);

            ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(itemEnchantments); // data component api doesn't really support mutable things once already set yet
            mutable.removeIf(enchantment -> enchantment.equals(removedEnchantment));
            this.handle.set(DataComponents.ENCHANTMENTS, mutable.toImmutable());
            return previousLevel;
        }

        return 0;
    }

    @Override
    public void removeEnchantments() {
        if (this.handle != null) {
            this.handle.set(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY); // Paper - set to default instead of removing the component
        }
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        if (this.isEmpty()) {
            return Map.of("id", "minecraft:air", SharedConstants.DATA_VERSION_TAG, Bukkit.getUnsafe().getDataVersion(), "schema_version", 1);
        }
        final CompoundTag tag = (CompoundTag) net.minecraft.world.item.ItemStack.CODEC.encodeStart(
            CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE),
            CraftItemStack.asNMSCopy(this)
        ).getOrThrow();
        NbtUtils.addCurrentDataVersion(tag);

        final Map<String, Object> ret = new LinkedHashMap<>();
        tag.asCompound().get().forEach((key, value) -> {
            switch (key) {
                case "id" -> {
                    ret.put("id", value.asString().get());
                }
                case "count" -> {
                    ret.put("count", value.asInt().get());
                }
                case "components" -> {
                    final Map<String, Object> components = new LinkedHashMap<>();
                    value.asCompound().ifPresent((compoundTag) -> {
                        compoundTag.forEach((componentKey, componentTag) -> {
                            final String serializedComponent = componentTag.toString();
                            components.put(componentKey, serializedComponent);
                        });
                    });
                    ret.put("components", components);
                }
                case SharedConstants.DATA_VERSION_TAG -> {
                    ret.put(SharedConstants.DATA_VERSION_TAG, value.asInt().get());
                }
                default -> throw new IllegalStateException("Unexpected value: " + key);
            }
        });
        ret.put("schema_version", 1);
        return ret;
    }

    @Override
    public Map<Enchantment, Integer> getEnchantments() {
        io.papermc.paper.datacomponent.item.ItemEnchantments itemEnchantments = this.getData(DataComponentTypes.ENCHANTMENTS); // empty constant might be useful here
        if (itemEnchantments == null) {
            return Collections.emptyMap();
        }
        return itemEnchantments.enchantments();
    }

    @Override
    public CraftItemStack clone() {
        return new CraftItemStack(this.handle != null ? this.handle.copy() : null); // Paper
    }

    @Override
    public ItemMeta getItemMeta() {
        return CraftItemStack.getItemMeta(this.handle);
    }
    // Paper start - improve handled tags on type change
    public void adjustTagForItemMeta(final Material oldType) {
        final CraftMetaItem oldMeta = (CraftMetaItem) CraftItemFactory.instance().getItemMeta(oldType);
        final ItemMeta newMeta;
        if (oldMeta == null) {
            newMeta = getItemMeta(this.handle);
        } else {
            final Set<DataComponentType<?>> extraHandledComponents = new HashSet<>(CraftMetaItem.getTopLevelHandledComponents(oldMeta.getClass()));
            newMeta = getItemMeta(this.handle, CraftItemType.minecraftToBukkitNew(this.handle.getItem()), extraHandledComponents);
        }
        this.setItemMeta(newMeta);
    }
    // Paper end - improve handled tags on type change

    public static void applyMetaToItem(net.minecraft.world.item.ItemStack itemStack, ItemMeta itemMeta) {
        // Paper start - support updating profile after resolving it
        final CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator() {
            @Override
            void skullCallback(final ResolvableProfile profile) {
                itemStack.set(DataComponents.PROFILE, profile);
            }
        };
        // Paper end - support updating profile after resolving it
        ((CraftMetaItem) itemMeta).applyToItem(tag);
        itemStack.applyComponents(tag.build());
    }

    public static ItemMeta getItemMeta(net.minecraft.world.item.ItemStack item) {
        return getItemMeta(item, null);
    }

    public static ItemMeta getItemMeta(net.minecraft.world.item.ItemStack item, ItemType metaForType) {
        // Paper start - handled tags on type change
        return getItemMeta(item, metaForType, null);
    }
    public static ItemMeta getItemMeta(net.minecraft.world.item.ItemStack item, ItemType metaForType, final Set<DataComponentType<?>> extraHandledComponents) {
        // Paper end - handled tags on type change
        if (!CraftItemStack.hasItemMeta(item)) {
            return CraftItemFactory.instance().getItemMeta(CraftItemStack.getType(item));
        }

        if (metaForType != null) { return ((CraftItemType<?>) metaForType).getItemMeta(item, extraHandledComponents); } // Paper
        return ((CraftItemType<?>) CraftItemType.minecraftToBukkitNew(item.getItem())).getItemMeta(item, extraHandledComponents); // Paper
    }

    static Material getType(net.minecraft.world.item.ItemStack item) {
        return item == null ? Material.AIR : CraftItemType.minecraftToBukkit(item.getItem());
    }

    @Override
    public boolean setItemMeta(ItemMeta itemMeta) {
        return CraftItemStack.setItemMeta(this.handle, itemMeta);
    }

    public static boolean setItemMeta(net.minecraft.world.item.ItemStack item, ItemMeta itemMeta) {
        if (item == null) {
            return false;
        }
        if (CraftItemFactory.instance().equals(itemMeta, null)) {
            item.restorePatch(DataComponentPatch.EMPTY);
            return true;
        }
        if (!CraftItemFactory.instance().isApplicable(itemMeta, CraftItemStack.getType(item))) {
            return false;
        }

        itemMeta = CraftItemFactory.instance().asMetaFor(itemMeta, CraftItemStack.getType(item));
        if (itemMeta == null) return true;

        if (!((CraftMetaItem) itemMeta).isEmpty()) {
            // Paper start - support updating profile after resolving it
            CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator() {
                @Override
                void skullCallback(final ResolvableProfile resolvableProfile) {
                    item.set(DataComponents.PROFILE, resolvableProfile);
                }
            };
            // Paper end - support updating profile after resolving it

            ((CraftMetaItem) itemMeta).applyToItem(tag);
            item.restorePatch(DataComponentPatch.EMPTY); // Paper - properly apply the new patch from itemmeta
            item.applyComponents(tag.build()); // Paper - properly apply the new patch from itemmeta
        }
        // Paper - this is no longer needed

        return true;
    }

    @Override
    public @NotNull String translationKey() {
        if (this.handle == null) {
            return Items.AIR.getDescriptionId();
        }
        return this.handle.getItem().getDescriptionId();
    }

    @Override
    public @NotNull Component effectiveName() {
        return this.handle == null ? Component.empty() : PaperAdventure.asAdventure(this.handle.getStyledHoverName());
    }

    @Override
    public boolean isSimilar(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack == this) {
            return true;
        }
        final CraftItemStack that = getCraftStack(stack); // Paper - re-implement after delegating all api ItemStack calls to CraftItemStack
        if (this.handle == that.handle) {
            return true;
        }
        if (this.handle == null || that.handle == null) {
            return false;
        }
        return net.minecraft.world.item.ItemStack.isSameItemSameComponents(this.handle, that.handle); // Paper - re-implement after delegating all api ItemStack calls to CraftItemStack
    }

    @Override
    public boolean hasItemMeta() {
        return CraftItemStack.hasItemMeta(this.handle) && (this.handle.getDamageValue() != 0 || this.handle.getComponentsPatch().size() >= (this.handle.getComponentsPatch().get(DataComponentMap.EMPTY, CraftMetaItem.DAMAGE.TYPE) != null ? 2 : 1)); // Paper - keep 1.12 CraftBukkit behavior without calling getItemMeta
    }

    static boolean hasItemMeta(net.minecraft.world.item.ItemStack item) {
        return !(item == null || item.getComponentsPatch().isEmpty());
    }
    // Paper start - with type
    @Override
    public ItemStack withType(final Material type) {
        if (type == Material.AIR) {
            return CraftItemStack.asCraftMirror(null);
        }

        final net.minecraft.world.item.ItemStack copy = new net.minecraft.world.item.ItemStack(
            CraftItemType.bukkitToMinecraft(type), this.getAmount()
        );

        if (this.handle != null) {
            copy.applyComponents(this.handle.getComponentsPatch());
        }

        final CraftItemStack mirrored = CraftItemStack.asCraftMirror(copy);
        mirrored.setItemMeta(mirrored.getItemMeta());
        return mirrored;
    }
    // Paper end

    @Override
    public boolean isRepairableBy(@NotNull final ItemStack repairMaterial) {
        if (this.handle == null) {
            return false;
        }
        return this.handle.isValidRepairItem(CraftItemStack.unwrap(repairMaterial));
    }

    @Override
    public @NotNull @Unmodifiable List<Component> computeTooltipLines(final TooltipContext tooltipContext, final Player player) {
        Preconditions.checkArgument(tooltipContext != null, "tooltipContext cannot be null");
        net.minecraft.world.item.ItemStack item = this.handle == null ? net.minecraft.world.item.ItemStack.EMPTY : this.handle;
        net.minecraft.world.item.TooltipFlag.Default flag = tooltipContext.isAdvanced() ? net.minecraft.world.item.TooltipFlag.ADVANCED : net.minecraft.world.item.TooltipFlag.NORMAL;
        if (tooltipContext.isCreative()) {
            flag = flag.asCreative();
        }
        final List<net.minecraft.network.chat.Component> lines = item.getTooltipLines(
            net.minecraft.world.item.Item.TooltipContext.of(player == null ? CraftRegistry.getMinecraftRegistry() : ((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle().level().registryAccess()),
            player == null ? null : ((org.bukkit.craftbukkit.entity.CraftPlayer) player).getHandle(), flag);
        return lines.stream().map(PaperAdventure::asAdventure).toList();
    }

    public static final String PDC_CUSTOM_DATA_KEY = "PublicBukkitValues";
    private CompoundTag getPdcTag() {
        if (this.handle == null) {
            return new CompoundTag();
        }
        final CustomData customData = this.handle.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        // getUnsafe is OK here because we are only ever *reading* the data so immutability is preserved
        //noinspection deprecation
        return customData.getUnsafe().getCompoundOrEmpty(PDC_CUSTOM_DATA_KEY);
    }

    private static final CraftPersistentDataTypeRegistry REGISTRY = new CraftPersistentDataTypeRegistry();
    private final PaperPersistentDataContainerView pdcView = new PaperPersistentDataContainerView(REGISTRY) {

        @Override
        public int getSize() {
            return CraftItemStack.this.getPdcTag().size();
        }

        @Override
        public CompoundTag toTagCompound() {
            return CraftItemStack.this.getPdcTag();
        }

        @Override
        public Tag getTag(final String key) {
            return CraftItemStack.this.getPdcTag().get(key);
        }
    };
    @Override
    public PersistentDataContainerView getPersistentDataContainer() {
        return this.pdcView;
    }

    @Override
    public boolean editPersistentDataContainer(final Consumer<PersistentDataContainer> consumer) {
        if (this.handle == null || this.handle.isEmpty()) return false;

        final CraftPersistentDataContainer container = new CraftPersistentDataContainer(REGISTRY);
        CustomData customData = this.handle.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        //noinspection deprecation // we copy only the pdc tag
        final CompoundTag pdcTag = customData.getUnsafe().getCompoundOrEmpty(PDC_CUSTOM_DATA_KEY).copy();
        container.putAll(pdcTag);
        consumer.accept(container);

        final CompoundTag newPdcTag = container.toTagCompound();
        if (!newPdcTag.isEmpty()) {
            customData = customData.update(tag -> tag.put(PDC_CUSTOM_DATA_KEY, newPdcTag));
        } else if (customData.contains(PDC_CUSTOM_DATA_KEY)) {
            customData = customData.update(tag -> tag.remove(PDC_CUSTOM_DATA_KEY));
        }

        // mirror CraftMetaItem behavior of clearing component if it's empty.
        this.handle.set(DataComponents.CUSTOM_DATA, customData.isEmpty() ? null : customData);
        return true;
    }

    // Paper start - data component API
    @Override
    public <T> T getData(final io.papermc.paper.datacomponent.DataComponentType.Valued<T> type) {
        if (this.isEmpty()) {
            return null;
        }
        return PaperDataComponentType.convertDataComponentValue(this.handle.getComponents(), (PaperDataComponentType.ValuedImpl<T, ?>) type);
    }

    @Override
    public boolean hasData(final io.papermc.paper.datacomponent.DataComponentType type) {
        if (this.isEmpty()) {
            return false;
        }
        return this.handle.has(PaperDataComponentType.bukkitToMinecraft(type));
    }

    @Override
    public Set<io.papermc.paper.datacomponent.DataComponentType> getDataTypes() {
        if (this.isEmpty()) {
            return Collections.emptySet();
        }
        return PaperDataComponentType.minecraftToBukkit(this.handle.getComponents().keySet());
    }

    @Override
    public <T> void setData(final io.papermc.paper.datacomponent.DataComponentType.Valued<T> type, final T value) {
        Preconditions.checkArgument(value != null, "value cannot be null");
        if (this.isEmpty()) {
            return;
        }
        this.setDataInternal((PaperDataComponentType.ValuedImpl<T, ?>) type, value);
    }

    @Override
    public void setData(final io.papermc.paper.datacomponent.DataComponentType.NonValued type) {
        if (this.isEmpty()) {
            return;
        }
        this.setDataInternal((PaperDataComponentType.NonValuedImpl<?, ?>) type, null);
    }

    private <A, V> void setDataInternal(final PaperDataComponentType<A, V> type, final A value) {
        this.handle.set(type.getHandle(), type.getAdapter().toVanilla(value, type.getHolder()));
    }

    @Override
    public void unsetData(final io.papermc.paper.datacomponent.DataComponentType type) {
        if (this.isEmpty()) {
            return;
        }
        this.handle.remove(PaperDataComponentType.bukkitToMinecraft(type));
    }

    @Override
    public void resetData(final io.papermc.paper.datacomponent.DataComponentType type) {
        if (this.isEmpty()) {
            return;
        }
        this.resetData((PaperDataComponentType<?, ?>) type);
    }

    private <M> void resetData(final PaperDataComponentType<?, M> type) {
        final DataComponentType<M> nms = PaperDataComponentType.bukkitToMinecraft(type);
        final M nmsValue = this.handle.getItem().components().get(nms);
        // if nmsValue is null, it will clear any set patch
        // if nmsValue is not null, it will still clear any set patch because it will equal the default value
        this.handle.set(nms, nmsValue);
    }

    @Override
    public void copyDataFrom(final ItemStack source, final Predicate<io.papermc.paper.datacomponent.DataComponentType> filter) {
        Preconditions.checkArgument(source != null, "source cannot be null");
        Preconditions.checkArgument(filter != null, "filter cannot be null");
        if (this.isEmpty() || source.isEmpty()) {
            return;
        }

        final Predicate<DataComponentType<?>> nmsFilter = nms -> filter.test(PaperDataComponentType.minecraftToBukkit(nms));
        net.minecraft.world.item.ItemStack sourceNmsStack = getCraftStack(source).handle;
        this.handle.applyComponents(sourceNmsStack.getPrototype().filter(nmsType -> {
            return !sourceNmsStack.hasNonDefault(nmsType) && nmsFilter.test(nmsType);
        }));

        final DataComponentPatch.SplitResult split = sourceNmsStack.getComponentsPatch().split();
        this.handle.applyComponents(split.added().filter(nmsFilter));
        split.removed().stream().filter(nmsFilter).forEach(this.handle::remove);
    }

    @Override
    public boolean isDataOverridden(final io.papermc.paper.datacomponent.DataComponentType type) {
        if (this.isEmpty()) {
            return false;
        }
        final DataComponentType<?> nms = PaperDataComponentType.bukkitToMinecraft(type);
        return this.handle.hasNonDefault(nms);
    }

    @Override
    public boolean matchesWithoutData(final ItemStack item, final Set<io.papermc.paper.datacomponent.DataComponentType> exclude, final boolean ignoreCount) {
        // Extracted from base equals
        final CraftItemStack craftStack = getCraftStack(item);
        if (this.handle == craftStack.handle) return true;
        if (this.handle == null || craftStack.handle == null) return false;
        if (this.handle.isEmpty() && craftStack.handle.isEmpty()) return true;

        net.minecraft.world.item.ItemStack left = this.handle;
        net.minecraft.world.item.ItemStack right = craftStack.handle;
        if (!ignoreCount && left.getCount() != right.getCount()) {
            return false;
        }
        if (!left.is(right.getItem())) {
            return false;
        }

        // It can be assumed that the prototype is equal since the type is the same. This way all we need to check is the patch

        // Fast path when excluded types is empty
        if (exclude.isEmpty()) {
            return left.getComponentsPatch().equals(right.getComponentsPatch());
        }

        // Collect all the NMS types into a set
        Set<DataComponentType<?>> skippingTypes = new HashSet<>(exclude.size());
        for (io.papermc.paper.datacomponent.DataComponentType api : exclude) {
            skippingTypes.add(PaperDataComponentType.bukkitToMinecraft(api));
        }

        // Check the patch by first stripping excluded types and then compare the trimmed patches
        return left.getComponentsPatch().forget(skippingTypes::contains).equals(right.getComponentsPatch().forget(skippingTypes::contains));
    }

    // Paper end - data component API
}
