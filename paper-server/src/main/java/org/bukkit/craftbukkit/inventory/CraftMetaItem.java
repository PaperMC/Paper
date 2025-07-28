package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.SequencedSet;
import java.util.Set;
import java.util.StringJoiner;
import it.unimi.dsi.fastutil.objects.ReferenceLinkedOpenHashSet;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.AdventureModePredicate;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSongs;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.CustomModelData;
import net.minecraft.world.item.component.DamageResistant;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.item.component.UseRemainder;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.CraftEquipmentSlot;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.Overridden;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.ItemMetaKey.Specific;
import org.bukkit.craftbukkit.inventory.components.CraftCustomModelDataComponent;
import org.bukkit.craftbukkit.inventory.components.CraftEquippableComponent;
import org.bukkit.craftbukkit.inventory.components.CraftFoodComponent;
import org.bukkit.craftbukkit.inventory.components.CraftJukeboxComponent;
import org.bukkit.craftbukkit.inventory.components.CraftToolComponent;
import org.bukkit.craftbukkit.inventory.components.CraftUseCooldownComponent;
import org.bukkit.craftbukkit.inventory.tags.DeprecatedCustomTagContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer;
import org.bukkit.craftbukkit.persistence.CraftPersistentDataTypeRegistry;
import org.bukkit.craftbukkit.tag.CraftDamageTag;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.CraftNBTTagConfigSerializer;
import org.bukkit.craftbukkit.util.CraftNamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.bukkit.inventory.meta.components.EquippableComponent;
import org.bukkit.inventory.meta.components.FoodComponent;
import org.bukkit.inventory.meta.components.JukeboxPlayableComponent;
import org.bukkit.inventory.meta.components.ToolComponent;
import org.bukkit.inventory.meta.components.UseCooldownComponent;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.tag.DamageTypeTags;
import org.slf4j.Logger;

/**
 * Children must include the following:
 *
 * <li> Constructor(CraftMetaItem meta)
 * <li> Constructor(DataComponentPatch tag, Set&lt;DataComponentType&lt;?&gt;&gt; extraHandledDcts)
 * <li> Constructor(Map&lt;String, Object&gt; map)
 * <br><br>
 * <li> void applyToItem(CraftMetaItem.Applicator tag)
 * <li> boolean applicableTo(Material type)
 * <br><br>
 * <li> boolean equalsCommon(CraftMetaItem meta)
 * <li> boolean notUncommon(CraftMetaItem meta)
 * <br><br>
 * <li> boolean isEmpty()
 * <li> boolean is{Type}Empty()
 * <br><br>
 * <li> int applyHash()
 * <li> public Class clone()
 * <br><br>
 * <li> Builder&lt;String, Object&gt; serialize(Builder&lt;String, Object&gt; builder)
 * <li> SerializableMeta.Deserializers deserializer()
 */
@DelegateDeserialization(SerializableMeta.class)
// Important: ItemMeta needs to be the first interface see #applicableTo(Material)
class CraftMetaItem implements ItemMeta, Damageable, Repairable, BlockDataMeta {

    static final Logger LOGGER = LogUtils.getLogger();

    static class ItemMetaKey {

        @Retention(RetentionPolicy.SOURCE)
        @Target(ElementType.FIELD)
        @interface Specific {
            enum To {
                BUKKIT,
                NBT,
                ;
            }
            To value();
        }

        final String BUKKIT;
        final String NBT;

        ItemMetaKey(final String both) {
            this(both, both);
        }

        ItemMetaKey(final String nbt, final String bukkit) {
            this.NBT = nbt;
            this.BUKKIT = bukkit;
        }
    }

    static final class ItemMetaKeyType<T> extends ItemMetaKey {

        final DataComponentType<T> TYPE;

        ItemMetaKeyType(final DataComponentType<T> type) {
            this(type, null, null);
        }

        ItemMetaKeyType(final DataComponentType<T> type, final String both) {
            this(type, both, both);
        }

        ItemMetaKeyType(final DataComponentType<T> type, final String nbt, final String bukkit) {
            super(nbt, bukkit);
            this.TYPE = type;
        }
    }

    static abstract class Applicator { // Paper - support updating profile after resolving it

        final DataComponentPatch.Builder builder = DataComponentPatch.builder();
        void skullCallback(net.minecraft.world.item.component.ResolvableProfile resolvableProfile) {} // Paper - support updating profile after resolving it

        <T> Applicator put(ItemMetaKeyType<T> key, T value) {
            this.builder.set(key.TYPE, value);
            return this;
        }

        DataComponentPatch build() {
            return this.builder.build();
        }
    }

    static final ItemMetaKeyType<Component> NAME = new ItemMetaKeyType(DataComponents.CUSTOM_NAME, "display-name");
    static final ItemMetaKeyType<Component> ITEM_NAME = new ItemMetaKeyType(DataComponents.ITEM_NAME, "item-name");
    static final ItemMetaKeyType<ItemLore> LORE = new ItemMetaKeyType<>(DataComponents.LORE, "lore");
    static final ItemMetaKeyType<CustomModelData> CUSTOM_MODEL_DATA = new ItemMetaKeyType<>(DataComponents.CUSTOM_MODEL_DATA, "custom-model-data");
    static final ItemMetaKeyType<Enchantable> ENCHANTABLE = new ItemMetaKeyType<>(DataComponents.ENCHANTABLE, "enchantable");
    static final ItemMetaKeyType<ItemEnchantments> ENCHANTMENTS = new ItemMetaKeyType<>(DataComponents.ENCHANTMENTS, "enchants");
    static final ItemMetaKeyType<Integer> REPAIR = new ItemMetaKeyType<>(DataComponents.REPAIR_COST, "repair-cost");
    static final ItemMetaKeyType<ItemAttributeModifiers> ATTRIBUTES = new ItemMetaKeyType<>(DataComponents.ATTRIBUTE_MODIFIERS, "attribute-modifiers");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_IDENTIFIER = new ItemMetaKey("AttributeName");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey ATTRIBUTES_SLOT = new ItemMetaKey("Slot");
    @Specific(Specific.To.NBT)
    static final ItemMetaKey HIDEFLAGS = new ItemMetaKey("ItemFlags");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<TooltipDisplay> TOOLTIP_DISPLAY = new ItemMetaKeyType<>(DataComponents.TOOLTIP_DISPLAY, "tool-tip-display");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<ResourceLocation> TOOLTIP_STYLE = new ItemMetaKeyType<>(DataComponents.TOOLTIP_STYLE, "tool-tip-style");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<ResourceLocation> ITEM_MODEL = new ItemMetaKeyType<>(DataComponents.ITEM_MODEL, "item-model");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unit> UNBREAKABLE = new ItemMetaKeyType<>(DataComponents.UNBREAKABLE, "Unbreakable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = new ItemMetaKeyType<>(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, "enchantment-glint-override");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Unit> GLIDER = new ItemMetaKeyType<>(DataComponents.GLIDER, "glider");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<DamageResistant> DAMAGE_RESISTANT = new ItemMetaKeyType<>(DataComponents.DAMAGE_RESISTANT, "damage-resistant");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> MAX_STACK_SIZE = new ItemMetaKeyType<>(DataComponents.MAX_STACK_SIZE, "max-stack-size");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Rarity> RARITY = new ItemMetaKeyType<>(DataComponents.RARITY, "rarity");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<UseRemainder> USE_REMAINDER = new ItemMetaKeyType<>(DataComponents.USE_REMAINDER, "use-remainder");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<UseCooldown> USE_COOLDOWN = new ItemMetaKeyType<>(DataComponents.USE_COOLDOWN, "use-cooldown");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<FoodProperties> FOOD = new ItemMetaKeyType<>(DataComponents.FOOD, "food");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Tool> TOOL = new ItemMetaKeyType<>(DataComponents.TOOL, "tool");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Equippable> EQUIPPABLE = new ItemMetaKeyType<>(DataComponents.EQUIPPABLE, "equippable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<JukeboxPlayable> JUKEBOX_PLAYABLE = new ItemMetaKeyType<>(DataComponents.JUKEBOX_PLAYABLE, "jukebox-playable");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> DAMAGE = new ItemMetaKeyType<>(DataComponents.DAMAGE, "Damage");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<Integer> MAX_DAMAGE = new ItemMetaKeyType<>(DataComponents.MAX_DAMAGE, "max-damage");
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<BlockItemStateProperties> BLOCK_DATA = new ItemMetaKeyType<>(DataComponents.BLOCK_STATE, "BlockStateTag");
    static final ItemMetaKeyType<AdventureModePredicate> CAN_PLACE_ON = new ItemMetaKeyType<>(DataComponents.CAN_PLACE_ON);
    static final ItemMetaKeyType<AdventureModePredicate> CAN_BREAK = new ItemMetaKeyType<>(DataComponents.CAN_BREAK);
    static final ItemMetaKey BUKKIT_CUSTOM_TAG = new ItemMetaKey(CraftItemStack.PDC_CUSTOM_DATA_KEY);
    @Specific(Specific.To.NBT)
    static final ItemMetaKeyType<CustomData> CUSTOM_DATA = new ItemMetaKeyType<>(DataComponents.CUSTOM_DATA);

    // We store the raw original JSON representation of all text data. See SPIGOT-5063, SPIGOT-5656, SPIGOT-5304
    private Component displayName;
    private Component itemName;
    private List<Component> lore; // null and empty are two different states internally
    private CraftCustomModelDataComponent customModelData;
    private Integer enchantableValue;
    private Map<String, String> blockData;
    private EnchantmentMap enchantments;
    private Multimap<Attribute, AttributeModifier> attributeModifiers;
    private int repairCost;
    private @Nullable TooltipDisplay tooltipDisplay;
    private NamespacedKey tooltipStyle;
    private NamespacedKey itemModel;
    private boolean unbreakable;
    private Boolean enchantmentGlintOverride;
    private boolean glider;
    private TagKey<net.minecraft.world.damagesource.DamageType> damageResistant;
    private Integer maxStackSize;
    private ItemRarity rarity;
    private ItemStack useRemainder;
    private CraftUseCooldownComponent useCooldown;
    private CraftFoodComponent food;
    private CraftToolComponent tool;
    private CraftEquippableComponent equippable;
    private CraftJukeboxComponent jukebox;
    private Integer damage;
    private Integer maxDamage;
    private List<net.minecraft.advancements.critereon.BlockPredicate> canPlaceOnPredicates;
    private List<net.minecraft.advancements.critereon.BlockPredicate> canBreakPredicates;

    // hide_additional_tooltip backward compatibility based on TooltipDisplayComponentFix#CONVERTED_ADDITIONAL_TOOLTIP_TYPES
    private static final Set<DataComponentType<?>> HIDDEN_COMPONENTS_PREVIOUSLY = Set.of(
        DataComponents.BANNER_PATTERNS,
        DataComponents.BEES,
        DataComponents.BLOCK_ENTITY_DATA,
        DataComponents.BLOCK_STATE,
        DataComponents.BUNDLE_CONTENTS,
        DataComponents.CHARGED_PROJECTILES,
        DataComponents.CONTAINER,
        DataComponents.CONTAINER_LOOT,
        DataComponents.FIREWORK_EXPLOSION,
        DataComponents.FIREWORKS,
        DataComponents.INSTRUMENT,
        DataComponents.JUKEBOX_PLAYABLE,
        DataComponents.MAP_ID,
        DataComponents.PAINTING_VARIANT,
        DataComponents.POT_DECORATIONS,
        DataComponents.POTION_CONTENTS,
        DataComponents.TROPICAL_FISH_PATTERN,
        DataComponents.WRITTEN_BOOK_CONTENT
    );

    private static final Map<ItemFlag, Set<DataComponentType<?>>> ITEM_FLAG_EQUIVALENTS = ImmutableMap.<ItemFlag, Set<DataComponentType<?>>>builder()
        .put(ItemFlag.HIDE_ATTRIBUTES, Set.of(DataComponents.ATTRIBUTE_MODIFIERS))
        .put(ItemFlag.HIDE_ENCHANTS, Set.of(DataComponents.ENCHANTMENTS))
        .put(ItemFlag.HIDE_STORED_ENCHANTS, Set.of(DataComponents.STORED_ENCHANTMENTS))
        .put(ItemFlag.HIDE_UNBREAKABLE, Set.of(DataComponents.UNBREAKABLE))
        .put(ItemFlag.HIDE_DYE, Set.of(DataComponents.DYED_COLOR))
        .put(ItemFlag.HIDE_ARMOR_TRIM, Set.of(DataComponents.TRIM))
        .put(ItemFlag.HIDE_PLACED_ON, Set.of(DataComponents.CAN_PLACE_ON))
        .put(ItemFlag.HIDE_DESTROYS, Set.of(DataComponents.CAN_BREAK))
        .put(ItemFlag.HIDE_ADDITIONAL_TOOLTIP, HIDDEN_COMPONENTS_PREVIOUSLY)
        .buildOrThrow();

    private static final CraftPersistentDataTypeRegistry DATA_TYPE_REGISTRY = new CraftPersistentDataTypeRegistry();

    private CompoundTag customTag;
    protected DataComponentPatch.Builder unhandledTags = DataComponentPatch.builder();
    private Set<DataComponentType<?>> removedTags = Sets.newHashSet();
    private CraftPersistentDataContainer persistentDataContainer = new CraftPersistentDataContainer(CraftMetaItem.DATA_TYPE_REGISTRY);

    private int version = CraftMagicNumbers.INSTANCE.getDataVersion(); // Internal use only

    CraftMetaItem(CraftMetaItem meta) {
        if (meta == null) {
            return;
        }

        this.displayName = meta.displayName;
        this.itemName = meta.itemName;

        if (meta.lore != null) {
            this.lore = new ArrayList<Component>(meta.lore);
        }

        if (meta.hasCustomModelDataComponent()) {
            this.customModelData = new CraftCustomModelDataComponent(meta.customModelData);
        }
        this.enchantableValue = meta.enchantableValue;
        this.blockData = meta.blockData;

        if (meta.enchantments != null) {
            this.enchantments = new EnchantmentMap(meta.enchantments);
        }

        if (meta.attributeModifiers != null) {
            this.attributeModifiers = LinkedHashMultimap.create(meta.attributeModifiers);
        }

        this.repairCost = meta.repairCost;
        if (meta.tooltipDisplay != null) {
            this.tooltipDisplay = new TooltipDisplay(meta.tooltipDisplay.hideTooltip(), new ReferenceLinkedOpenHashSet<>(meta.tooltipDisplay.hiddenComponents()));
        }
        this.tooltipStyle = meta.tooltipStyle;
        this.itemModel = meta.itemModel;
        this.unbreakable = meta.unbreakable;
        this.enchantmentGlintOverride = meta.enchantmentGlintOverride;
        this.glider = meta.glider;
        this.damageResistant = meta.damageResistant;
        this.maxStackSize = meta.maxStackSize;
        this.rarity = meta.rarity;
        if (meta.hasUseRemainder()) {
            this.useRemainder = meta.useRemainder.clone();
        }
        if (meta.hasUseCooldown()) {
            this.useCooldown = new CraftUseCooldownComponent(meta.useCooldown);
        }
        if (meta.hasFood()) {
            this.food = new CraftFoodComponent(meta.food);
        }
        if (meta.hasTool()) {
            this.tool = new CraftToolComponent(meta.tool);
        }
        if (meta.hasEquippable()) {
            this.equippable = new CraftEquippableComponent(meta.equippable);
        }
        if (meta.hasJukeboxPlayable()) {
            this.jukebox = new CraftJukeboxComponent(meta.jukebox);
        }
        this.damage = meta.damage;
        this.maxDamage = meta.maxDamage;
        this.unhandledTags.copy(meta.unhandledTags.build());
        this.removedTags.addAll(meta.removedTags);
        this.persistentDataContainer.putAll(meta.persistentDataContainer.getTagsCloned()); // Paper - deep clone NBT tags
        this.canPlaceOnPredicates = meta.canPlaceOnPredicates;
        this.canBreakPredicates = meta.canBreakPredicates;

        this.customTag = meta.customTag;

        this.version = meta.version;
    }

    CraftMetaItem(DataComponentPatch tag, Set<DataComponentType<?>> extraHandledTags) { // Paper - improve handled tags on type changes
        // Paper start - properly support data components in BlockEntity
        this.updateFromPatch(tag, extraHandledTags);
    }
    protected final void updateFromPatch(DataComponentPatch tag, Set<DataComponentType<?>> extraHandledTags) {
        // Paper end - properly support data components in BlockEntity
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.NAME).ifPresent((component) -> {
            this.displayName = component;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ITEM_NAME).ifPresent((component) -> {
            this.itemName = component;
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.LORE).ifPresent((l) -> {
            this.lore = new ArrayList<>(l.lines());
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CUSTOM_MODEL_DATA).ifPresent((i) -> {
            this.customModelData = new CraftCustomModelDataComponent(i);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ENCHANTABLE).ifPresent((i) -> {
            this.enchantableValue = i.value();
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.BLOCK_DATA).ifPresent((t) -> {
            this.blockData = t.properties();
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ENCHANTMENTS).ifPresent((en) -> {
            this.enchantments = CraftMetaItem.buildEnchantments(en);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ATTRIBUTES).ifPresent((en) -> {
            this.attributeModifiers = CraftMetaItem.buildModifiers(en);
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.REPAIR).ifPresent((i) -> {
            this.repairCost = i;
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.TOOLTIP_DISPLAY).ifPresent((en) -> {
            this.tooltipDisplay = new TooltipDisplay(en.hideTooltip(), new ReferenceLinkedOpenHashSet<>(en.hiddenComponents()));
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.TOOLTIP_STYLE).ifPresent((key) -> {
            this.tooltipStyle = CraftNamespacedKey.fromMinecraft(key);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ITEM_MODEL).ifPresent((key) -> {
            this.itemModel = CraftNamespacedKey.fromMinecraft(key);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.UNBREAKABLE).ifPresent((u) -> {
            this.unbreakable = true;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE).ifPresent((override) -> {
            this.enchantmentGlintOverride = override;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.GLIDER).ifPresent((u) -> {
            this.glider = true;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.DAMAGE_RESISTANT).ifPresent((tags) -> {
            this.damageResistant = tags.types();
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.MAX_STACK_SIZE).ifPresent((i) -> {
            this.maxStackSize = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.RARITY).ifPresent((enumItemRarity) -> {
            this.rarity = ItemRarity.valueOf(enumItemRarity.name());
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.USE_REMAINDER).ifPresent((remainder) -> {
            this.useRemainder = CraftItemStack.asCraftMirror(remainder.convertInto());
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.USE_COOLDOWN).ifPresent((cooldown) -> {
            this.useCooldown = new CraftUseCooldownComponent(cooldown);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.FOOD).ifPresent((foodInfo) -> {
            this.food = new CraftFoodComponent(foodInfo);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.TOOL).ifPresent((toolInfo) -> {
            this.tool = new CraftToolComponent(toolInfo);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.EQUIPPABLE).ifPresent((equippableInfo) -> {
            this.equippable = new CraftEquippableComponent(equippableInfo);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.JUKEBOX_PLAYABLE).ifPresent((jukeboxPlayable) -> {
            this.jukebox = new CraftJukeboxComponent(jukeboxPlayable);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.DAMAGE).ifPresent((i) -> {
            this.damage = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.MAX_DAMAGE).ifPresent((i) -> {
            this.maxDamage = i;
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CUSTOM_DATA).ifPresent((customData) -> {
            this.customTag = customData.copyTag();
            CompoundTag customDataTag = this.customTag.getCompoundOrEmpty(CraftMetaItem.BUKKIT_CUSTOM_TAG.NBT);
            if (!customDataTag.isEmpty()) {
                for (Entry<String, net.minecraft.nbt.Tag> entry : customDataTag.entrySet()) {
                    this.persistentDataContainer.put(entry.getKey(), entry.getValue().copy());
                }

                this.customTag.remove(CraftMetaItem.BUKKIT_CUSTOM_TAG.NBT);
            }

            if (this.customTag.isEmpty()) {
                this.customTag = null;
            }
        });

        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CAN_PLACE_ON.TYPE).ifPresent(data -> {
            this.canPlaceOnPredicates = List.copyOf(data.predicates);
        });
        CraftMetaItem.getOrEmpty(tag, CraftMetaItem.CAN_BREAK).ifPresent(data -> {
            this.canBreakPredicates = List.copyOf(data.predicates);
        });

        // Paper start - improve checking handled data component types
        Set<DataComponentType<?>> handledTags = getTopLevelHandledDcts(this.getClass());
        if (extraHandledTags != null) {
            extraHandledTags.addAll(handledTags);
            handledTags = extraHandledTags;
        }
        // Paper end - improve checking handled data component types
        Set<Map.Entry<DataComponentType<?>, Optional<?>>> keys = tag.entrySet();
        for (Map.Entry<DataComponentType<?>, Optional<?>> key : keys) {
            if (!handledTags.contains(key.getKey())) { // Paper - improve checking handled data component types
                key.getValue().ifPresent((value) -> {
                    this.unhandledTags.set((DataComponentType) key.getKey(), value);
                });
            }

            if (key.getValue().isEmpty()) {
                this.removedTags.add(key.getKey());
            }
        }
    }

    static EnchantmentMap buildEnchantments(ItemEnchantments tag) {
        EnchantmentMap enchantments = new EnchantmentMap();

        tag.entrySet().forEach((entry) -> {
            Holder<net.minecraft.world.item.enchantment.Enchantment> id = entry.getKey();
            int level = entry.getIntValue();

            Enchantment enchant = CraftEnchantment.minecraftHolderToBukkit(id);
            if (enchant != null) {
                enchantments.put(enchant, level);
            }
        });

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(ItemAttributeModifiers tag) {
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        List<ItemAttributeModifiers.Entry> mods = tag.modifiers();
        int size = mods.size();

        for (int i = 0; i < size; i++) {
            ItemAttributeModifiers.Entry entry = mods.get(i);
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = entry.modifier();

            AttributeModifier attribMod = CraftAttributeInstance.convert(nmsModifier);

            Attribute attribute = CraftAttribute.minecraftHolderToBukkit(entry.attribute());
            if (attribute == null) {
                continue;
            }

            EquipmentSlotGroup slotName = entry.slot();

            org.bukkit.inventory.EquipmentSlotGroup slot = null;
            try {
                slot = CraftEquipmentSlot.getSlotGroup(slotName);
            } catch (IllegalArgumentException ex) {
                // SPIGOT-4551 - Slot is invalid, should really match nothing but this is undefined behaviour anyway
            }

            if (slot == null) {
                modifiers.put(attribute, attribMod);
                continue;
            }

            attribMod = new AttributeModifier(attribMod.getKey(), attribMod.getAmount(), attribMod.getOperation(), slot);
            modifiers.put(attribute, attribMod);
        }
        return modifiers;
    }

    CraftMetaItem(Map<String, Object> map) {
        this.displayName = CraftChatMessage.fromJSONOrString(SerializableMeta.getString(map, CraftMetaItem.NAME.BUKKIT, true), true, false);
        this.itemName = CraftChatMessage.fromJSONOrNull(SerializableMeta.getString(map, CraftMetaItem.ITEM_NAME.BUKKIT, true));

        Iterable<?> lore = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.LORE.BUKKIT, true);
        if (lore != null) {
            CraftMetaItem.safelyAdd(lore, this.lore = new ArrayList<>(), true);
        }

        Object customModelData = SerializableMeta.getObject(Object.class, map, CraftMetaItem.CUSTOM_MODEL_DATA.BUKKIT, true);
        if (customModelData instanceof CustomModelDataComponent component) {
            this.setCustomModelDataComponent(component);
        } else {
            this.setCustomModelData((Integer) customModelData);
        }
        Integer enchantmentValue = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.ENCHANTABLE.BUKKIT, true);
        if (enchantmentValue != null) {
            this.setEnchantable(enchantmentValue);
        }

        Object blockData = SerializableMeta.getObject(Object.class, map, CraftMetaItem.BLOCK_DATA.BUKKIT, true);
        if (blockData != null) {
            Map<String, String> mapBlockData = new HashMap<>();

            if (blockData instanceof Map) {
                for (Entry<?, ?> entry : ((Map<?, ?>) blockData).entrySet()) {
                    mapBlockData.put(entry.getKey().toString(), entry.getValue().toString());
                }
            } else {
                // Legacy pre 1.20.5:
                CompoundTag blockDataTag = (CompoundTag) CraftNBTTagConfigSerializer.deserialize(blockData);
                for (String key : blockDataTag.keySet()) {
                    mapBlockData.put(key, blockDataTag.getString(key).orElseThrow());
                }
            }

            this.blockData = mapBlockData;
        }

        this.enchantments = CraftMetaItem.buildEnchantments(map, CraftMetaItem.ENCHANTMENTS);
        this.attributeModifiers = CraftMetaItem.buildModifiers(map, CraftMetaItem.ATTRIBUTES);

        Integer repairCost = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.REPAIR.BUKKIT, true);
        if (repairCost != null) {
            this.setRepairCost(repairCost);
        }

        Iterable<?> hideFlags = SerializableMeta.getObject(Iterable.class, map, CraftMetaItem.HIDEFLAGS.BUKKIT, true);
        if (hideFlags != null) {
            for (Object hideFlagObject : hideFlags) {
                String hideFlagString = (String) hideFlagObject;
                try {
                    ItemFlag hideFlatEnum = CraftItemFlag.stringToBukkit(hideFlagString);
                    this.addItemFlags(hideFlatEnum);
                } catch (IllegalArgumentException ex) {
                    // Ignore when we got a old String which does not map to a Enum value anymore
                }
            }
        }

        String tooltipStyle = SerializableMeta.getString(map, CraftMetaItem.TOOLTIP_STYLE.BUKKIT, true);
        if (tooltipStyle != null) {
            this.setTooltipStyle(NamespacedKey.fromString(tooltipStyle));
        }

        String itemModel = SerializableMeta.getString(map, CraftMetaItem.ITEM_MODEL.BUKKIT, true);
        if (itemModel != null) {
            this.setItemModel(NamespacedKey.fromString(itemModel));
        }

        Boolean unbreakable = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.UNBREAKABLE.BUKKIT, true);
        if (unbreakable != null) {
            this.setUnbreakable(unbreakable);
        }

        Boolean enchantmentGlintOverride = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.BUKKIT, true);
        if (enchantmentGlintOverride != null) {
            this.setEnchantmentGlintOverride(enchantmentGlintOverride);
        }

        Boolean glider = SerializableMeta.getObject(Boolean.class, map, CraftMetaItem.GLIDER.BUKKIT, true);
        if (glider != null) {
            this.setGlider(glider);
        }

        String damageResistant = SerializableMeta.getString(map, CraftMetaItem.DAMAGE_RESISTANT.BUKKIT, true);
        if (damageResistant != null) {
            Tag<DamageType> tag = Bukkit.getTag(DamageTypeTags.REGISTRY_DAMAGE_TYPES, NamespacedKey.fromString(damageResistant), DamageType.class);
            if (tag != null) {
                this.setDamageResistant(tag);
            }
        }

        Integer maxStackSize = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.MAX_STACK_SIZE.BUKKIT, true);
        if (maxStackSize != null) {
            this.setMaxStackSize(maxStackSize);
        }

        String rarity = SerializableMeta.getString(map, CraftMetaItem.RARITY.BUKKIT, true);
        if (rarity != null) {
            this.setRarity(ItemRarity.valueOf(rarity));
        }

        ItemStack remainder = SerializableMeta.getObject(ItemStack.class, map, CraftMetaItem.USE_REMAINDER.BUKKIT, true);
        if (remainder != null) {
            this.setUseRemainder(remainder);
        }

        CraftUseCooldownComponent cooldown = SerializableMeta.getObject(CraftUseCooldownComponent.class, map, CraftMetaItem.USE_COOLDOWN.BUKKIT, true);
        if (cooldown != null) {
            this.setUseCooldown(cooldown);
        }

        CraftFoodComponent food = SerializableMeta.getObject(CraftFoodComponent.class, map, CraftMetaItem.FOOD.BUKKIT, true);
        if (food != null) {
            this.setFood(food);
        }

        CraftToolComponent tool = SerializableMeta.getObject(CraftToolComponent.class, map, CraftMetaItem.TOOL.BUKKIT, true);
        if (tool != null) {
            this.setTool(tool);
        }

        CraftEquippableComponent equippable = SerializableMeta.getObject(CraftEquippableComponent.class, map, CraftMetaItem.EQUIPPABLE.BUKKIT, true);
        if (equippable != null) {
            this.setEquippable(equippable);
        }

        CraftJukeboxComponent jukeboxPlayable = SerializableMeta.getObject(CraftJukeboxComponent.class, map, CraftMetaItem.JUKEBOX_PLAYABLE.BUKKIT, true);
        if (jukeboxPlayable != null) {
            this.setJukeboxPlayable(jukeboxPlayable);
        }

        Integer damage = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.DAMAGE.BUKKIT, true);
        if (damage != null) {
            this.setDamage(damage);
        }

        Integer maxDamage = SerializableMeta.getObject(Integer.class, map, CraftMetaItem.MAX_DAMAGE.BUKKIT, true);
        if (maxDamage != null) {
            this.setMaxDamage(maxDamage);
        }

        String internal = SerializableMeta.getString(map, "internal", true);
        if (internal != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(internal));
            try {
                CompoundTag internalTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
                this.deserializeInternal(internalTag, map);
            } catch (IOException ex) {
                LOGGER.error("Failed to read internal tag from object", ex);
            }
        }

        String unhandled = SerializableMeta.getString(map, "unhandled", true);
        if (unhandled != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(unhandled));
            try {
                CompoundTag unhandledTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
                DataComponentPatch unhandledPatch = DataComponentPatch.CODEC.parse(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), unhandledTag).result().get();

                CraftMetaItem.getOrEmpty(unhandledPatch, CraftMetaItem.CAN_PLACE_ON).ifPresent(data -> {
                    this.canPlaceOnPredicates = List.copyOf(data.predicates);
                });
                CraftMetaItem.getOrEmpty(unhandledPatch, CraftMetaItem.CAN_BREAK).ifPresent(data -> {
                    this.canBreakPredicates = List.copyOf(data.predicates);
                });
                this.unhandledTags.copy(unhandledPatch.forget(type -> type == CraftMetaItem.CAN_PLACE_ON.TYPE || type == CraftMetaItem.CAN_BREAK.TYPE));

                for (Entry<DataComponentType<?>, Optional<?>> entry : unhandledPatch.entrySet()) {
                    // Move removed unhandled tags to dedicated removedTags
                    if (entry.getValue().isEmpty()) {
                        DataComponentType<?> key = entry.getKey();

                        this.unhandledTags.clear(key);
                        this.removedTags.add(key);
                    }
                }
            } catch (IOException ex) {
                LOGGER.error("Failed to read unhandled tag for item", ex);
            }
        }

        Iterable<?> removed = SerializableMeta.getObject(Iterable.class, map, "removed", true);
        if (removed != null) {
            RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
            Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);

            for (Object removedObject : removed) {
                String removedString = (String) removedObject;

                DataComponentType<?> component = componentTypeRegistry.getValue(ResourceLocation.parse(removedString));
                if (component != null) {
                    this.removedTags.add(component);
                }
            }
        }

        Object nbtMap = SerializableMeta.getObject(Object.class, map, CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, true); // We read both legacy maps and potential modern snbt strings here
        if (nbtMap != null) {
            this.persistentDataContainer.putAll((CompoundTag) CraftNBTTagConfigSerializer.deserialize(nbtMap));
        }

        String custom = SerializableMeta.getString(map, "custom", true);
        if (custom != null) {
            ByteArrayInputStream buf = new ByteArrayInputStream(Base64.getDecoder().decode(custom));
            try {
                this.customTag = NbtIo.readCompressed(buf, NbtAccounter.unlimitedHeap());
            } catch (IOException ex) {
                LOGGER.error("Failed to read custom tag for item", ex);
            }
        }
    }

    void deserializeInternal(CompoundTag tag, Object context) {
        // SPIGOT-4576: Need to migrate from internal to proper data
        if (tag.getList(CraftMetaItem.ATTRIBUTES.NBT).isPresent()) {
            this.attributeModifiers = CraftMetaItem.buildModifiersLegacy(tag, CraftMetaItem.ATTRIBUTES);
        }
    }

    private static Multimap<Attribute, AttributeModifier> buildModifiersLegacy(CompoundTag tag, ItemMetaKey key) {
        Multimap<Attribute, AttributeModifier> modifiers = LinkedHashMultimap.create();
        ListTag mods = tag.getListOrEmpty(key.NBT);
        for (int i = 0, size = mods.size(); i < size; i++) {
            CompoundTag entry = mods.getCompoundOrEmpty(i);
            if (entry.isEmpty()) {
                // entry is not an actual CompoundTag. getCompoundOrEmpty returns empty CompoundTag in that case
                continue;
            }
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = net.minecraft.world.entity.ai.attributes.AttributeModifier.CODEC.parse(NbtOps.INSTANCE, entry).resultOrPartial().orElse(null);
            if (nmsModifier == null) {
                continue;
            }

            AttributeModifier attribMod = CraftAttributeInstance.convert(nmsModifier);

            String attributeName = entry.getString(CraftMetaItem.ATTRIBUTES_IDENTIFIER.NBT).map(CraftAttributeMap::convertIfNeeded).orElse(null);
            if (attributeName == null || attributeName.isEmpty()) {
                continue;
            }

            Attribute attribute = CraftAttribute.stringToBukkit(attributeName);
            if (attribute == null) {
                continue;
            }

            Optional<String> attributeSlot = entry.getString(CraftMetaItem.ATTRIBUTES_SLOT.NBT);
            if (attributeSlot.isPresent()) {
                String slotName = attributeSlot.get();
                if (slotName.isEmpty()) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                EquipmentSlot slot = null;
                try {
                    slot = CraftEquipmentSlot.getSlot(net.minecraft.world.entity.EquipmentSlot.byName(slotName.toLowerCase(Locale.ROOT)));
                } catch (IllegalArgumentException ex) {
                    // SPIGOT-4551 - Slot is invalid, should really match nothing but this is undefined behaviour anyway
                }

                if (slot == null) {
                    modifiers.put(attribute, attribMod);
                    continue;
                }

                attribMod = new AttributeModifier(attribMod.getKey(), attribMod.getAmount(), attribMod.getOperation(), slot.getGroup());
            }
            modifiers.put(attribute, attribMod);
        }
        return modifiers;
    }

    static EnchantmentMap buildEnchantments(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> ench = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        if (ench == null) {
            return null;
        }

        EnchantmentMap enchantments = new EnchantmentMap();
        for (Map.Entry<?, ?> entry : ench.entrySet()) {
            Enchantment enchantment = CraftEnchantment.stringToBukkit(entry.getKey().toString());
            if ((enchantment != null) && (entry.getValue() instanceof Integer)) {
                enchantments.put(enchantment, (Integer) entry.getValue());
            }
        }

        return enchantments;
    }

    static Multimap<Attribute, AttributeModifier> buildModifiers(Map<String, Object> map, ItemMetaKey key) {
        Map<?, ?> mods = SerializableMeta.getObject(Map.class, map, key.BUKKIT, true);
        Multimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        if (mods == null) {
            return null; // Paper - null is different from an empty map
        }

        for (Object obj : mods.keySet()) {
            if (!(obj instanceof final String attributeName)) {
                continue;
            }
            if (Strings.isNullOrEmpty(attributeName)) {
                continue;
            }
            List<?> list = SerializableMeta.getObject(List.class, mods, attributeName, true);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (Object o : list) {
                if (!(o instanceof final AttributeModifier modifier)) { // this catches null
                    continue;
                }
                Attribute attribute = CraftAttribute.stringToBukkit(attributeName);
                if (attribute == null) {
                    continue;
                }

                result.put(attribute, modifier);
            }
        }
        return result;
    }

    @Overridden
    void applyToItem(CraftMetaItem.Applicator tag) {
        if (this.hasDisplayName()) {
            tag.put(CraftMetaItem.NAME, this.displayName);
        }

        if (this.hasItemName()) {
            tag.put(CraftMetaItem.ITEM_NAME, this.itemName);
        }

        if (this.lore != null) {
            tag.put(CraftMetaItem.LORE, new ItemLore(this.lore));
        }

        if (this.hasCustomModelDataComponent()) {
            tag.put(CraftMetaItem.CUSTOM_MODEL_DATA, this.customModelData.getHandle());
        }

        if (this.hasEnchantable()) {
            tag.put(CraftMetaItem.ENCHANTABLE, new Enchantable(this.enchantableValue));
        }

        if (this.hasBlockData()) {
            tag.put(CraftMetaItem.BLOCK_DATA, new BlockItemStateProperties(this.blockData));
        }

        this.applyEnchantments(this.enchantments, tag, CraftMetaItem.ENCHANTMENTS);
        this.applyModifiers(this.attributeModifiers, tag);

        if (this.hasRepairCost()) {
            tag.put(CraftMetaItem.REPAIR, this.repairCost);
        }

        if (this.tooltipDisplay != null) {
            tag.put(CraftMetaItem.TOOLTIP_DISPLAY, new TooltipDisplay(this.tooltipDisplay.hideTooltip(), new ReferenceLinkedOpenHashSet<>(this.tooltipDisplay.hiddenComponents())));
        }

        if (this.hasTooltipStyle()) {
            tag.put(CraftMetaItem.TOOLTIP_STYLE, CraftNamespacedKey.toMinecraft(this.getTooltipStyle()));
        }

        if (this.hasItemModel()) {
            tag.put(CraftMetaItem.ITEM_MODEL, CraftNamespacedKey.toMinecraft(this.getItemModel()));
        }

        if (this.isUnbreakable()) {
            tag.put(CraftMetaItem.UNBREAKABLE, Unit.INSTANCE);
        }

        if (this.hasEnchantmentGlintOverride()) {
            tag.put(CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE, this.getEnchantmentGlintOverride());
        }

        if (this.isGlider()) {
            tag.put(CraftMetaItem.GLIDER, Unit.INSTANCE);
        }

        if (this.hasDamageResistant()) {
            tag.put(CraftMetaItem.DAMAGE_RESISTANT, new DamageResistant(this.damageResistant));
        }

        if (this.hasMaxStackSize()) {
            tag.put(CraftMetaItem.MAX_STACK_SIZE, this.maxStackSize);
        }

        if (this.hasRarity()) {
            tag.put(CraftMetaItem.RARITY, Rarity.valueOf(this.rarity.name()));
        }

        if (this.hasUseRemainder()) {
            tag.put(CraftMetaItem.USE_REMAINDER, new UseRemainder(CraftItemStack.asNMSCopy(this.useRemainder)));
        }

        if (this.hasUseCooldown()) {
            tag.put(CraftMetaItem.USE_COOLDOWN, this.useCooldown.getHandle());
        }

        if (this.hasFood()) {
            tag.put(CraftMetaItem.FOOD, this.food.getHandle());
        }

        if (this.hasTool()) {
            tag.put(CraftMetaItem.TOOL, this.tool.getHandle());
        }

        if (this.hasEquippable()) {
            tag.put(CraftMetaItem.EQUIPPABLE, this.equippable.getHandle());
        }

        if (this.hasJukeboxPlayable()) {
            tag.put(CraftMetaItem.JUKEBOX_PLAYABLE, this.jukebox.getHandle());
        }

        if (this.hasDamageValue()) { // Paper - preserve empty/0 damage
            tag.put(CraftMetaItem.DAMAGE, this.damage);
        }

        if (this.hasMaxDamage()) {
            tag.put(CraftMetaItem.MAX_DAMAGE, this.maxDamage);
        }

        if (this.canPlaceOnPredicates != null && !this.canPlaceOnPredicates.isEmpty()) {
            tag.put(CraftMetaItem.CAN_PLACE_ON, new net.minecraft.world.item.AdventureModePredicate(this.canPlaceOnPredicates));
        }
        if (this.canBreakPredicates != null && !this.canBreakPredicates.isEmpty()) {
            tag.put(CraftMetaItem.CAN_BREAK, new net.minecraft.world.item.AdventureModePredicate(this.canBreakPredicates));
        }

        for (Map.Entry<DataComponentType<?>, Optional<?>> e : this.unhandledTags.build().entrySet()) {
            e.getValue().ifPresent((value) -> {
                tag.builder.set((DataComponentType) e.getKey(), value);
            });
        }

        for (DataComponentType<?> removed : this.removedTags) {
            if (!tag.builder.isSet(removed)) {
                tag.builder.remove(removed);
            }
        }

        CompoundTag customTag = (this.customTag != null) ? this.customTag.copy() : null;
        if (!this.persistentDataContainer.isEmpty()) {
            CompoundTag pdcTag = new CompoundTag();
            Map<String, net.minecraft.nbt.Tag> rawPublicMap = this.persistentDataContainer.getRaw();

            for (Map.Entry<String, net.minecraft.nbt.Tag> entry : rawPublicMap.entrySet()) {
                pdcTag.put(entry.getKey(), entry.getValue());
            }

            if (customTag == null) {
                customTag = new CompoundTag();
            }
            customTag.put(CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, pdcTag);
        }

        if (customTag != null) {
            tag.put(CraftMetaItem.CUSTOM_DATA, CustomData.of(customTag));
        }
    }

    void applyEnchantments(Map<Enchantment, Integer> enchantments, CraftMetaItem.Applicator tag, ItemMetaKeyType<ItemEnchantments> key) {
        if (enchantments == null) { // only emit enchantment component if enchantments are defined
            return;
        }

        ItemEnchantments.Mutable list = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            list.set(CraftEnchantment.bukkitToMinecraftHolder(entry.getKey()), entry.getValue());
        }
        tag.put(key, list.toImmutable());
    }

    void applyModifiers(Multimap<Attribute, AttributeModifier> modifiers, CraftMetaItem.Applicator tag) {
        if (modifiers == null) { // empty modifiers has a specific meaning, they should still be saved
            // don't save ItemFlag if the underlying data isn't present
            return;
        }

        ItemAttributeModifiers.Builder list = ItemAttributeModifiers.builder();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null || entry.getValue() == null) {
                continue;
            }
            net.minecraft.world.entity.ai.attributes.AttributeModifier nmsModifier = CraftAttributeInstance.convert(entry.getValue());

            Holder<net.minecraft.world.entity.ai.attributes.Attribute> name = CraftAttribute.bukkitToMinecraftHolder(entry.getKey());
            if (name == null) {
                continue;
            }

            EquipmentSlotGroup group = CraftEquipmentSlot.getNMSGroup(entry.getValue().getSlotGroup());
            list.add(name, nmsModifier, group);
        }
        tag.put(CraftMetaItem.ATTRIBUTES, list.build());
    }

    boolean applicableTo(Material type) {
        if (type == Material.AIR || !type.isItem()) {
            return false;
        }

        if (this.getClass() == CraftMetaItem.class) {
            return true;
        }

        // We assume that the corresponding bukkit interface is always the first one
        return type.asItemType().getItemMetaClass() == this.getClass().getInterfaces()[0];
    }

    @Overridden
    boolean isEmpty() {
        return !(this.hasDisplayName() || this.hasItemName() || this.hasLocalizedName() || this.hasEnchants() || (this.lore != null) || this.hasCustomModelDataComponent() || this.hasEnchantable() || this.hasBlockData() || this.hasRepairCost() || !this.unhandledTags.build().isEmpty() || !this.removedTags.isEmpty() || !this.persistentDataContainer.isEmpty() || this.hasAnyItemFlag() || this.isHideTooltip() || this.hasTooltipStyle() || this.hasItemModel() || this.isUnbreakable() || this.hasEnchantmentGlintOverride() || this.isGlider() || this.hasDamageResistant() || this.hasMaxStackSize() || this.hasRarity() || this.hasUseRemainder() || this.hasUseCooldown() || this.hasFood() || this.hasTool() || this.hasJukeboxPlayable() || this.hasEquippable() || this.hasDamageValue() || this.hasMaxDamage() || this.hasAttributeModifiers() || this.customTag != null || this.canPlaceOnPredicates != null || this.canBreakPredicates != null);
    }

    @Override
    public net.kyori.adventure.text.Component customName() {
        return displayName == null ? null : io.papermc.paper.adventure.PaperAdventure.asAdventure(displayName);
    }

    @Override
    public void customName(final net.kyori.adventure.text.Component customName) {
        this.displayName = customName == null ? null : io.papermc.paper.adventure.PaperAdventure.asVanilla(customName);
    }

    @Override
    public String getDisplayName() {
        return CraftChatMessage.fromComponent(this.displayName);
    }

    @Override
    public net.md_5.bungee.api.chat.BaseComponent[] getDisplayNameComponent() {
        return displayName == null ? new net.md_5.bungee.api.chat.BaseComponent[0] : CraftChatMessage.vanillaToBungee(displayName);
    }

    @Override
    public final void setDisplayName(String name) {
        this.displayName = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public void setDisplayNameComponent(net.md_5.bungee.api.chat.BaseComponent[] component) {
        this.displayName = CraftChatMessage.bungeeToVanilla(component);
    }

    @Override
    public boolean hasCustomName() {
        return this.displayName != null;
    }

    @Override
    public String getItemName() {
        return CraftChatMessage.fromComponent(this.itemName);
    }

    @Override
    public final void setItemName(String name) {
        this.itemName = CraftChatMessage.fromStringOrNull(name);
    }

    @Override
    public boolean hasItemName() {
        return this.itemName != null;
    }

    @Override
    public net.kyori.adventure.text.Component itemName() {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(this.itemName);
    }

    @Override
    public void itemName(final net.kyori.adventure.text.Component name) {
        this.itemName = io.papermc.paper.adventure.PaperAdventure.asVanilla(name);
    }

    @Override
    public String getLocalizedName() {
        return this.getDisplayName();
    }

    @Override
    public void setLocalizedName(String name) {
    }

    @Override
    public boolean hasLocalizedName() {
        return false;
    }

    @Override
    public boolean hasLore() {
        return this.lore != null && !this.lore.isEmpty();
    }

    @Override
    public List<net.kyori.adventure.text.Component> lore() {
        return this.lore != null ? io.papermc.paper.adventure.PaperAdventure.asAdventure(this.lore) : null;
    }

    @Override
    public void lore(final List<? extends net.kyori.adventure.text.Component> lore) {
        if (lore == null) {
            this.lore = null;
            return;
        }

        Preconditions.checkArgument(lore.size() <= ItemLore.MAX_LINES, "lore cannot have more than %s lines", ItemLore.MAX_LINES); // Paper - limit lore lines

        for (int i = 0; i < lore.size(); i++) {
            Preconditions.checkArgument(lore.get(i) != null, "lore contains null entry at index: %s", i);
        }

        this.lore = io.papermc.paper.adventure.PaperAdventure.asVanilla(lore);
    }

    @Override
    public boolean hasRepairCost() {
        return this.repairCost > 0;
    }

    @Override
    public boolean hasEnchant(Enchantment enchant) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        return this.hasEnchants() && this.enchantments.containsKey(enchant);
    }

    @Override
    public int getEnchantLevel(Enchantment enchant) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        Integer level = this.hasEnchants() ? this.enchantments.get(enchant) : null;
        if (level == null) {
            return 0;
        }
        return level;
    }

    @Override
    public Map<Enchantment, Integer> getEnchants() {
        return this.hasEnchants() ? ImmutableSortedMap.copyOfSorted(this.enchantments) : ImmutableMap.<Enchantment, Integer>of(); // Paper
    }

    @Override
    public boolean addEnchant(Enchantment enchant, int level, boolean ignoreRestrictions) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        if (this.enchantments == null) {
            this.enchantments = new EnchantmentMap();
        }

        if (ignoreRestrictions || level >= enchant.getStartLevel() && level <= enchant.getMaxLevel()) {
            Integer old = this.enchantments.put(enchant, level);
            return old == null || old != level;
        }
        return false;
    }

    @Override
    public boolean removeEnchant(Enchantment enchant) {
        Preconditions.checkArgument(enchant != null, "Enchantment cannot be null");
        boolean enchantmentRemoved = this.hasEnchants() && this.enchantments.remove(enchant) != null;
        // If we no longer have any enchantments, then clear enchantment tag
        if (enchantmentRemoved && this.enchantments.isEmpty()) {
            this.enchantments = null;
        }
        return enchantmentRemoved;
    }

    @Override
    public void removeEnchantments() {
        if (this.hasEnchants()) {
            this.enchantments = null; // Paper - Correctly clear enchantments
        }
    }

    @Override
    public boolean hasEnchants() {
        return !(this.enchantments == null || this.enchantments.isEmpty());
    }

    @Override
    public boolean hasConflictingEnchant(Enchantment enchant) {
        return CraftMetaItem.checkConflictingEnchants(this.enchantments, enchant);
    }

    @Override
    public void addItemFlags(ItemFlag... hideFlags) {
        if (this.tooltipDisplay == null) {
            this.tooltipDisplay = new TooltipDisplay(false, new ReferenceLinkedOpenHashSet<>());
        }
        for (ItemFlag f : hideFlags) {
            this.tooltipDisplay.hiddenComponents().addAll(ITEM_FLAG_EQUIVALENTS.get(f));
        }
    }

    @Override
    public void removeItemFlags(ItemFlag... hideFlags) {
        if (this.tooltipDisplay == null) {
            return;
        }
        for (ItemFlag f : hideFlags) {
            final Set<DataComponentType<?>> dataComponentTypes = ITEM_FLAG_EQUIVALENTS.get(f);
            // Specifically ensure *all* expected data components are available. Otherwise we might remove components
            // not tracked by the ItemFlag system (e.g. HIDE_ADDITIONAL removal should not remove a single POTION_CONTENTS
            // if that was set as the item does not have the HIDE_ADDITIONAL ItemFlag.
            if (this.tooltipDisplay.hiddenComponents().containsAll(dataComponentTypes)) {
                this.tooltipDisplay.hiddenComponents().removeAll(dataComponentTypes);
            }
        }
    }

    @Override
    public Set<ItemFlag> getItemFlags() {
        Set<ItemFlag> currentFlags = EnumSet.noneOf(ItemFlag.class);

        for (ItemFlag f : ItemFlag.values()) {
            if (this.hasItemFlag(f)) {
                currentFlags.add(f);
            }
        }

        return currentFlags;
    }

    @Override
    public boolean hasItemFlag(ItemFlag flag) {
        if (this.tooltipDisplay == null) {
            return false;
        }
        return this.tooltipDisplay.hiddenComponents().containsAll(ITEM_FLAG_EQUIVALENTS.get(flag));
    }

    private boolean hasAnyItemFlag() {
        return this.tooltipDisplay != null && !this.tooltipDisplay.hiddenComponents().isEmpty();
    }

    @Override
    public List<String> getLore() {
        return this.lore == null ? null : new ArrayList<>(Lists.transform(this.lore, CraftChatMessage::fromComponent));
    }

    @Override
    public List<net.md_5.bungee.api.chat.BaseComponent[]> getLoreComponents() {
        return this.lore == null ? null : new ArrayList<>(Lists.transform(this.lore, CraftChatMessage::vanillaToBungee));
    }

    @Override
    public void setLore(List<String> lore) {
        Preconditions.checkArgument(lore == null || lore.size() <= ItemLore.MAX_LINES, "lore cannot have more than %s lines", ItemLore.MAX_LINES); // Paper - limit lore lines
        if (lore == null || lore.isEmpty()) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                this.lore = new ArrayList<Component>(lore.size());
            } else {
                this.lore.clear();
            }
            CraftMetaItem.safelyAdd(lore, this.lore, false);
        }
    }

    @Override
    public void setLoreComponents(List<net.md_5.bungee.api.chat.BaseComponent[]> lore) {
        Preconditions.checkArgument(lore == null || lore.size() <= ItemLore.MAX_LINES, "lore cannot have more than %s lines", ItemLore.MAX_LINES); // Paper - limit lore lines
        if (lore == null) {
            this.lore = null;
        } else {
            if (this.lore == null) {
                safelyAdd(lore, this.lore = new ArrayList<>(lore.size()), false);
            } else {
                this.lore.clear();
                safelyAdd(lore, this.lore, false);
            }
        }
    }

    @Override
    public boolean hasCustomModelData() {
        if (customModelData != null) {
            List<Float> floats = customModelData.getFloats();
            return !floats.isEmpty();
        }

        return false;
    }

    @Override
    public int getCustomModelData() {
        Preconditions.checkState(this.hasCustomModelData(), "We don't have CustomModelData! Check hasCustomModelData first!");

        return customModelData.getFloats().get(0).intValue();
    }

    @Override
    public boolean hasCustomModelDataComponent() {
        return customModelData != null;
    }

    @Override
    public CustomModelDataComponent getCustomModelDataComponent() {
        return (this.hasCustomModelDataComponent()) ? new CraftCustomModelDataComponent(this.customModelData) : new CraftCustomModelDataComponent(new CustomModelData(List.of(), List.of(), List.of(), List.of()));
    }

    @Override
    public void setCustomModelData(Integer data) {
        this.customModelData = (data == null) ? null : new CraftCustomModelDataComponent(new CustomModelData(List.of(data.floatValue()), List.of(), List.of(), List.of()));
    }

    @Override
    public void setCustomModelDataComponent(CustomModelDataComponent customModelData) {
        this.customModelData = (customModelData == null) ? null : new CraftCustomModelDataComponent((CraftCustomModelDataComponent) customModelData);
    }

    @Override
    public boolean hasEnchantable() {
        return this.enchantableValue != null;
    }

    @Override
    public int getEnchantable() {
        Preconditions.checkState(this.hasEnchantable(), "We don't have Enchantable! Check hasEnchantable first!");
        return this.enchantableValue;
    }

    @Override
    public void setEnchantable(Integer data) {
        Preconditions.checkArgument(data == null || data > 0, "Enchantability must be positive");
        this.enchantableValue = data;
    }

    @Override
    public boolean hasBlockData() {
        return this.blockData != null;
    }

    @Override
    public BlockData getBlockData(Material material) {
        BlockState defaultData = CraftBlockType.bukkitToMinecraft(material).defaultBlockState();
        return CraftBlockData.fromData((this.hasBlockData()) ? new BlockItemStateProperties(this.blockData).apply(defaultData) : defaultData);
    }

    @Override
    public void setBlockData(BlockData blockData) {
        this.blockData = (blockData == null) ? null : ((CraftBlockData) blockData).toStates(true);
    }

    @Override
    public int getRepairCost() {
        return this.repairCost;
    }

    @Override
    public void setRepairCost(int cost) { // TODO: Does this have limits?
        this.repairCost = cost;
    }

    @Override
    public boolean isHideTooltip() {
        return this.tooltipDisplay != null && this.tooltipDisplay.hideTooltip();
    }

    @Override
    public void setHideTooltip(boolean hideTooltip) {
        if (this.tooltipDisplay == null) {
            this.tooltipDisplay = new TooltipDisplay(hideTooltip, new ReferenceLinkedOpenHashSet<>());
        } else {
            this.tooltipDisplay = new TooltipDisplay(hideTooltip, this.tooltipDisplay.hiddenComponents());
        }
    }

    @Override
    public boolean hasTooltipStyle() {
        return this.tooltipStyle != null;
    }

    @Override
    public NamespacedKey getTooltipStyle() {
        return this.tooltipStyle;
    }

    @Override
    public void setTooltipStyle(NamespacedKey tooltipStyle) {
        this.tooltipStyle = tooltipStyle;
    }

    @Override
    public boolean hasItemModel() {
        return this.itemModel != null;
    }

    @Override
    public NamespacedKey getItemModel() {
        return this.itemModel;
    }

    @Override
    public void setItemModel(NamespacedKey itemModel) {
        this.itemModel = itemModel;
    }

    @Override
    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    @Override
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    @Override
    public boolean hasEnchantmentGlintOverride() {
        return this.enchantmentGlintOverride != null;
    }

    @Override
    public Boolean getEnchantmentGlintOverride() {
        Preconditions.checkState(this.hasEnchantmentGlintOverride(), "We don't have enchantment_glint_override! Check hasEnchantmentGlintOverride first!");
        return this.enchantmentGlintOverride;
    }

    @Override
    public void setEnchantmentGlintOverride(Boolean override) {
        this.enchantmentGlintOverride = override;
    }

    @Override
    public boolean isGlider() {
        return this.glider;
    }

    @Override
    public void setGlider(boolean glider) {
        this.glider = glider;
    }

    @Override
    public boolean isFireResistant() {
        return this.hasDamageResistant() && this.damageResistant == net.minecraft.tags.DamageTypeTags.IS_FIRE;
    }

    @Override
    public void setFireResistant(boolean fireResistant) {
        this.damageResistant = net.minecraft.tags.DamageTypeTags.IS_FIRE;
    }

    @Override
    public boolean hasDamageResistant() {
        return this.damageResistant != null;
    }

    @Override
    public Tag<DamageType> getDamageResistant() {
        return (this.hasDamageResistant()) ? Bukkit.getTag(DamageTypeTags.REGISTRY_DAMAGE_TYPES, CraftNamespacedKey.fromMinecraft(this.damageResistant.location()), DamageType.class) : null;
    }

    @Override
    public void setDamageResistant(Tag<DamageType> tag) {
        this.damageResistant = (tag != null) ? ((CraftDamageTag) tag).getHandle().key() : null;
    }

    @Override
    public boolean hasMaxStackSize() {
        return this.maxStackSize != null;
    }

    @Override
    public int getMaxStackSize() {
        Preconditions.checkState(this.hasMaxStackSize(), "We don't have max_stack_size! Check hasMaxStackSize first!");
        return this.maxStackSize;
    }

    @Override
    public void setMaxStackSize(Integer max) {
        Preconditions.checkArgument(max == null || max > 0, "max_stack_size must be > 0");
        Preconditions.checkArgument(max == null || max <= net.minecraft.world.item.Item.ABSOLUTE_MAX_STACK_SIZE, "max_stack_size must be <= 99");
        this.maxStackSize = max;
    }

    @Override
    public boolean hasRarity() {
        return this.rarity != null;
    }

    @Override
    public ItemRarity getRarity() {
        Preconditions.checkState(this.hasRarity(), "We don't have rarity! Check hasRarity first!");
        return this.rarity;
    }

    @Override
    public void setRarity(ItemRarity rarity) {
        this.rarity = rarity;
    }

    @Override
    public boolean hasUseRemainder() {
        return this.useRemainder != null;
    }

    @Override
    public ItemStack getUseRemainder() {
        return this.useRemainder;
    }

    @Override
    public void setUseRemainder(ItemStack useRemainder) {
        Preconditions.checkArgument(useRemainder == null || !useRemainder.isEmpty(), "Item cannot be empty");
        this.useRemainder = useRemainder;
    }

    @Override
    public boolean hasUseCooldown() {
        return this.useCooldown != null;
    }

    @Override
    public UseCooldownComponent getUseCooldown() {
        return (this.hasUseCooldown()) ? new CraftUseCooldownComponent(this.useCooldown) : new CraftUseCooldownComponent(new UseCooldown(1));
    }

    @Override
    public void setUseCooldown(UseCooldownComponent cooldown) {
        this.useCooldown = (cooldown == null) ? null : new CraftUseCooldownComponent((CraftUseCooldownComponent) cooldown);
    }

    @Override
    public boolean hasFood() {
        return this.food != null;
    }

    @Override
    public FoodComponent getFood() {
        return (this.hasFood()) ? new CraftFoodComponent(this.food) : new CraftFoodComponent(new FoodProperties(0, 0, false));
    }

    @Override
    public void setFood(FoodComponent food) {
        this.food = (food == null) ? null : new CraftFoodComponent((CraftFoodComponent) food);
    }

    @Override
    public boolean hasTool() {
        return this.tool != null;
    }

    @Override
    public ToolComponent getTool() {
        return (this.hasTool()) ? new CraftToolComponent(this.tool) : new CraftToolComponent(new Tool(Collections.emptyList(), 1.0F, 0, true));
    }

    @Override
    public void setTool(ToolComponent tool) {
        this.tool = (tool == null) ? null : new CraftToolComponent((CraftToolComponent) tool);
    }

    @Override
    public boolean hasEquippable() {
        return this.equippable != null;
    }

    @Override
    public EquippableComponent getEquippable() {
        return (this.hasEquippable()) ? new CraftEquippableComponent(this.equippable) : new CraftEquippableComponent(Equippable.builder(net.minecraft.world.entity.EquipmentSlot.HEAD).build());
    }

    @Override
    public void setEquippable(EquippableComponent equippable) {
        this.equippable = (equippable == null) ? null : new CraftEquippableComponent((CraftEquippableComponent) equippable);
    }

    @Override
    public boolean hasJukeboxPlayable() {
        return this.jukebox != null;
    }

    @Override
    public JukeboxPlayableComponent getJukeboxPlayable() {
        return (this.hasJukeboxPlayable()) ? new CraftJukeboxComponent(this.jukebox) : new CraftJukeboxComponent(new JukeboxPlayable(new EitherHolder<>(JukeboxSongs.THIRTEEN)));
    }

    @Override
    public void setJukeboxPlayable(JukeboxPlayableComponent jukeboxPlayable) {
        this.jukebox = (jukeboxPlayable == null) ? null : new CraftJukeboxComponent((CraftJukeboxComponent) jukeboxPlayable);
    }

    @Override
    public boolean hasAttributeModifiers() {
        return this.attributeModifiers != null && !this.attributeModifiers.isEmpty();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers() {
        return this.hasAttributeModifiers() ? ImmutableMultimap.copyOf(this.attributeModifiers) : null;
    }

    private void checkAttributeList() {
        if (this.attributeModifiers == null) {
            this.attributeModifiers = LinkedHashMultimap.create();
        }
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(@Nullable EquipmentSlot slot) {
        if (this.attributeModifiers == null) return LinkedHashMultimap.create(); // Paper - don't change the components
        SetMultimap<Attribute, AttributeModifier> result = LinkedHashMultimap.create();
        for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
            if (entry.getValue().getSlotGroup().test(slot)) { // Paper - correctly test slot against group
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }

    @Override
    public Collection<AttributeModifier> getAttributeModifiers(@Nonnull Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "Attribute cannot be null");
        if (this.attributeModifiers == null) return null;
        return this.attributeModifiers.containsKey(attribute) ? ImmutableList.copyOf(this.attributeModifiers.get(attribute)) : null;
    }

    @Override
    public boolean addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkArgument(attribute != null, "Attribute cannot be null");
        Preconditions.checkArgument(modifier != null, "AttributeModifier cannot be null");
        if (this.attributeModifiers != null) {
            for (Map.Entry<Attribute, AttributeModifier> entry : this.attributeModifiers.entries()) {
                Preconditions.checkArgument(!(entry.getValue().getKey().equals(modifier.getKey()) && entry.getKey() == attribute), "Cannot register AttributeModifier. Modifier is already applied! %s", modifier); // Paper - attribute modifiers with same namespaced key but on different attributes are fine
            }
        }
        this.checkAttributeList(); // Paper - moved down
        return this.attributeModifiers.put(attribute, modifier);
    }

    @Override
    public void setAttributeModifiers(@Nullable Multimap<Attribute, AttributeModifier> attributeModifiers) {
        // Paper start - distinguish between null and empty
        if (attributeModifiers == null) {
            this.attributeModifiers = null;
            return;
        }
        if (attributeModifiers.isEmpty()) {
            // Paper end - distinguish between null and empty
            this.attributeModifiers = LinkedHashMultimap.create();
            return;
        }

        // Paper start - fix modifiers meta
        if (this.attributeModifiers != null) {
            this.attributeModifiers.clear();
        }
        // Paper end

        Iterator<Map.Entry<Attribute, AttributeModifier>> iterator = attributeModifiers.entries().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> next = iterator.next();

            if (next.getKey() == null || next.getValue() == null) {
                iterator.remove();
                continue;
            }
            this.checkAttributeList(); // Paper - moved down
            this.attributeModifiers.put(next.getKey(), next.getValue());
        }
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "Attribute cannot be null");
        if (this.attributeModifiers == null) return false;
        return !this.attributeModifiers.removeAll(attribute).isEmpty();
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull EquipmentSlot slot) {
        if (this.attributeModifiers == null) return false;
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = this.attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            if (entry.getValue().getSlotGroup().test(slot)) { // Paper - correctly test slot against group
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @Override
    public boolean removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier modifier) {
        Preconditions.checkArgument(attribute != null, "Attribute cannot be null");
        Preconditions.checkArgument(modifier != null, "AttributeModifier cannot be null");
        if (this.attributeModifiers == null) return false;
        int removed = 0;
        Iterator<Map.Entry<Attribute, AttributeModifier>> iter = this.attributeModifiers.entries().iterator();

        while (iter.hasNext()) {
            Map.Entry<Attribute, AttributeModifier> entry = iter.next();
            if (entry.getKey() == null || entry.getValue() == null) {
                iter.remove();
                ++removed;
                continue; // remove all null values while we are here
            }

            if (entry.getKey() == attribute && entry.getValue().getKey().equals(modifier.getKey())) {
                iter.remove();
                ++removed;
            }
        }
        return removed > 0;
    }

    @Override
    public String getAsString() {
        CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator() {}; // Paper - support updating profile after resolving it
        this.applyToItem(tag);
        DataComponentPatch patch = tag.build();
        net.minecraft.nbt.Tag nbt = DataComponentPatch.CODEC.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), patch).getOrThrow();
        return nbt.toString();
    }

    @Override
    public String getAsComponentString() {
        CraftMetaItem.Applicator tag = new CraftMetaItem.Applicator() {};
        this.applyToItem(tag);
        DataComponentPatch patch = tag.build();

        RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
        DynamicOps<net.minecraft.nbt.Tag> ops = registryAccess.createSerializationContext(NbtOps.INSTANCE);
        Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);

        StringJoiner componentString = new StringJoiner(",", "[", "]");
        for (Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) {
            DataComponentType<?> componentType = entry.getKey();
            Optional<?> componentValue = entry.getValue();
            String componentKey = componentTypeRegistry.getResourceKey(componentType).orElseThrow().location().toString();

            if (componentValue.isPresent()) {
                net.minecraft.nbt.Tag componentValueAsNBT = (net.minecraft.nbt.Tag) ((DataComponentType) componentType).codecOrThrow().encodeStart(ops, componentValue.get()).getOrThrow();
                String componentValueAsNBTString = new SnbtPrinterTagVisitor("", 0, new ArrayList<>()).visit(componentValueAsNBT);
                componentString.add(componentKey + "=" + componentValueAsNBTString);
            } else {
                componentString.add("!" + componentKey);
            }
        }

        return componentString.toString();
    }

    @Override
    public CustomItemTagContainer getCustomTagContainer() {
        return new DeprecatedCustomTagContainer(this.getPersistentDataContainer());
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    private static boolean compareModifiers(Multimap<Attribute, AttributeModifier> first, Multimap<Attribute, AttributeModifier> second) {
        if (first == null || second == null) {
            return false;
        }
        if (first.isEmpty() && second.isEmpty()) return true; // Paper - empty modifiers are equivalent
        for (Map.Entry<Attribute, AttributeModifier> entry : first.entries()) {
            if (!second.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        for (Map.Entry<Attribute, AttributeModifier> entry : second.entries()) {
            if (!first.containsEntry(entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean hasDamage() {
        return this.damage != null && this.damage > 0;
    }

    @Override
    public int getDamage() {
        return this.damage == null ? 0 : this.damage;
    }

    @Override
    public void setDamage(int damage) {
        Preconditions.checkArgument(damage >= 0, "Damage cannot be negative");
        Preconditions.checkArgument(!this.hasMaxDamage() || damage <= this.maxDamage, "Damage cannot exceed max damage");
        this.damage = damage;
    }

    // Paper start - preserve empty/0 damage
    @Override
    public boolean hasDamageValue() {
        return this.damage != null;
    }

    @Override
    public void resetDamage() {
        this.damage = null;
    }
    // Paper end - preserve empty/0 damage

    @Override
    public boolean hasMaxDamage() {
        return this.maxDamage != null;
    }

    @Override
    public int getMaxDamage() {
        Preconditions.checkState(this.hasMaxDamage(), "We don't have max_damage! Check hasMaxDamage first!");
        return this.maxDamage;
    }

    @Override
    public void setMaxDamage(Integer maxDamage) {
        Preconditions.checkArgument(maxDamage == null || maxDamage > 0, "Max damage should be positive");
        this.maxDamage = maxDamage;
    }

    @Override
    public final boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof CraftMetaItem)) {
            return false;
        }
        return CraftItemFactory.instance().equals(this, (ItemMeta) object);
    }

    /**
     * This method is almost as weird as notUncommon.
     * Only return false if your common internals are unequal.
     * Checking your own internals is redundant if you are not common, as notUncommon is meant for checking those 'not common' variables.
     */
    @Overridden
    boolean equalsCommon(CraftMetaItem meta) {
        return ((this.hasDisplayName() ? meta.hasDisplayName() && this.displayName.equals(meta.displayName) : !meta.hasDisplayName()))
                && (this.hasItemName() ? meta.hasItemName() && this.itemName.equals(meta.itemName) : !meta.hasItemName())
                && (this.hasEnchants() ? meta.hasEnchants() && this.enchantments.equals(meta.enchantments) : !meta.hasEnchants())
                && (Objects.equals(this.lore, meta.lore))
                && (this.hasCustomModelDataComponent() ? meta.hasCustomModelDataComponent() && this.customModelData.equals(meta.customModelData) : !meta.hasCustomModelDataComponent())
                && (this.hasEnchantable() ? meta.hasEnchantable() && this.enchantableValue.equals(meta.enchantableValue) : !meta.hasEnchantable())
                && (this.hasBlockData() ? meta.hasBlockData() && this.blockData.equals(meta.blockData) : !meta.hasBlockData())
                && (this.hasRepairCost() ? meta.hasRepairCost() && this.repairCost == meta.repairCost : !meta.hasRepairCost())
                && (this.attributeModifiers != null ? meta.attributeModifiers != null && CraftMetaItem.compareModifiers(this.attributeModifiers, meta.attributeModifiers) : meta.attributeModifiers == null) // Paper - track only null modifiers
                && (this.unhandledTags.equals(meta.unhandledTags))
                && (this.removedTags.equals(meta.removedTags))
                && (Objects.equals(this.customTag, meta.customTag))
                && (this.persistentDataContainer.equals(meta.persistentDataContainer))
                && (Objects.equals(this.tooltipDisplay, meta.tooltipDisplay))
                && (this.isHideTooltip() == meta.isHideTooltip())
                && (this.hasTooltipStyle() ? meta.hasTooltipStyle() && this.tooltipStyle.equals(meta.tooltipStyle) : !meta.hasTooltipStyle())
                && (this.hasItemModel() ? meta.hasItemModel() && this.itemModel.equals(meta.itemModel) : !meta.hasItemModel())
                && (this.isUnbreakable() == meta.isUnbreakable())
                && (this.hasEnchantmentGlintOverride() ? meta.hasEnchantmentGlintOverride() && this.enchantmentGlintOverride.equals(meta.enchantmentGlintOverride) : !meta.hasEnchantmentGlintOverride())
                && (this.glider == meta.glider)
                && (this.hasDamageResistant() ? meta.hasDamageResistant() && this.damageResistant.equals(meta.damageResistant) : !meta.hasDamageResistant())
                && (this.hasMaxStackSize() ? meta.hasMaxStackSize() && this.maxStackSize.equals(meta.maxStackSize) : !meta.hasMaxStackSize())
                && (this.rarity == meta.rarity)
                && (this.hasUseRemainder() ? meta.hasUseRemainder() && this.useRemainder.equals(meta.useRemainder) : !meta.hasUseRemainder())
                && (this.hasUseCooldown() ? meta.hasUseCooldown() && this.useCooldown.equals(meta.useCooldown) : !meta.hasUseCooldown())
                && (this.hasFood() ? meta.hasFood() && this.food.equals(meta.food) : !meta.hasFood())
                && (this.hasTool() ? meta.hasTool() && this.tool.equals(meta.tool) : !meta.hasTool())
                && (this.hasEquippable() ? meta.hasEquippable() && this.equippable.equals(meta.equippable) : !meta.hasEquippable())
                && (this.hasJukeboxPlayable() ? meta.hasJukeboxPlayable() && this.jukebox.equals(meta.jukebox) : !meta.hasJukeboxPlayable())
                && (Objects.equals(this.damage, meta.damage)) // Paper - preserve empty/0 damage
                && (this.hasMaxDamage() ? meta.hasMaxDamage() && this.maxDamage.equals(meta.maxDamage) : !meta.hasMaxDamage())
                && (this.canPlaceOnPredicates != null ? meta.canPlaceOnPredicates != null && this.canPlaceOnPredicates.equals(meta.canPlaceOnPredicates) : meta.canPlaceOnPredicates == null) // Paper
                && (this.canBreakPredicates != null ? meta.canBreakPredicates != null && this.canBreakPredicates.equals(meta.canBreakPredicates) : meta.canBreakPredicates == null) // Paper
                && (this.version == meta.version);
    }

    /**
     * This method is a bit weird...
     * Return true if you are a common class OR your uncommon parts are empty.
     * Empty uncommon parts implies the NBT data would be equivalent if both were applied to an item
     */
    @Overridden
    boolean notUncommon(CraftMetaItem meta) {
        return true;
    }

    @Override
    public final int hashCode() {
        return this.applyHash();
    }

    @Overridden
    int applyHash() {
        int hash = 3;
        hash = 61 * hash + (this.hasDisplayName() ? this.displayName.hashCode() : 0);
        hash = 61 * hash + (this.hasItemName() ? this.itemName.hashCode() : 0);
        hash = 61 * hash + ((this.lore != null) ? this.lore.hashCode() : 0);
        hash = 61 * hash + (this.hasCustomModelDataComponent() ? this.customModelData.hashCode() : 0);
        hash = 61 * hash + (this.hasEnchantable() ? this.enchantableValue.hashCode() : 0);
        hash = 61 * hash + (this.hasBlockData() ? this.blockData.hashCode() : 0);
        hash = 61 * hash + (this.hasEnchants() ? this.enchantments.hashCode() : 0);
        hash = 61 * hash + (this.hasRepairCost() ? this.repairCost : 0);
        hash = 61 * hash + this.unhandledTags.hashCode();
        hash = 61 * hash + this.removedTags.hashCode();
        hash = 61 * hash + ((this.customTag != null) ? this.customTag.hashCode() : 0);
        hash = 61 * hash + (!this.persistentDataContainer.isEmpty() ? this.persistentDataContainer.hashCode() : 0);
        hash = 61 * hash + ((this.tooltipDisplay != null) ? this.tooltipDisplay.hashCode() : 0);
        hash = 61 * hash + (this.isHideTooltip() ? 1231 : 1237);
        hash = 61 * hash + (this.hasTooltipStyle() ? this.tooltipStyle.hashCode() : 0);
        hash = 61 * hash + (this.hasItemModel() ? this.itemModel.hashCode() : 0);
        hash = 61 * hash + (this.isUnbreakable() ? 1231 : 1237);
        hash = 61 * hash + (this.hasEnchantmentGlintOverride() ? this.enchantmentGlintOverride.hashCode() : 0);
        hash = 61 * hash + (this.isGlider() ? 1231 : 1237);
        hash = 61 * hash + (this.hasDamageResistant() ? this.damageResistant.hashCode() : 0);
        hash = 61 * hash + (this.hasMaxStackSize() ? this.maxStackSize.hashCode() : 0);
        hash = 61 * hash + (this.hasRarity() ? this.rarity.hashCode() : 0);
        hash = 61 * hash + (this.hasUseRemainder() ? this.useRemainder.hashCode() : 0);
        hash = 61 * hash + (this.hasUseCooldown() ? this.useCooldown.hashCode() : 0);
        hash = 61 * hash + (this.hasFood() ? this.food.hashCode() : 0);
        hash = 61 * hash + (this.hasTool() ? this.tool.hashCode() : 0);
        hash = 61 * hash + (this.hasJukeboxPlayable() ? this.jukebox.hashCode() : 0);
        hash = 61 * hash + (this.hasEquippable() ? this.equippable.hashCode() : 0);
        hash = 61 * hash + (this.hasDamageValue() ? this.damage : -1); // Paper - preserve empty/0 damage
        hash = 61 * hash + (this.hasMaxDamage() ? this.maxDamage.hashCode() : 0);
        hash = 61 * hash + (this.attributeModifiers != null ? this.attributeModifiers.hashCode() : 0); // Paper - track only null attributes
        hash = 61 * hash + (this.canPlaceOnPredicates != null ? this.canPlaceOnPredicates.hashCode() : 0);
        hash = 61 * hash + (this.canBreakPredicates != null ? this.canBreakPredicates.hashCode() : 0);
        hash = 61 * hash + this.version;
        return hash;
    }

    @Overridden
    @Override
    public CraftMetaItem clone() {
        try {
            CraftMetaItem clone = (CraftMetaItem) super.clone();
            if (this.lore != null) {
                clone.lore = new ArrayList<>(this.lore);
            }
            if (this.hasCustomModelDataComponent()) {
                clone.customModelData = new CraftCustomModelDataComponent(this.customModelData);
            }
            clone.enchantableValue = this.enchantableValue;
            clone.blockData = this.blockData;
            if (this.enchantments != null) {
                clone.enchantments = new EnchantmentMap(this.enchantments);
            }
            if (this.attributeModifiers != null) {
                clone.attributeModifiers = LinkedHashMultimap.create(this.attributeModifiers);
            }
            if (this.customTag != null) {
                clone.customTag = this.customTag.copy();
            }
            clone.removedTags = Sets.newHashSet(this.removedTags);
            clone.persistentDataContainer = new CraftPersistentDataContainer(this.persistentDataContainer.getTagsCloned(), CraftMetaItem.DATA_TYPE_REGISTRY); // Paper - deep clone NBT tags
            if (this.tooltipDisplay != null) {
                clone.tooltipDisplay = new TooltipDisplay(this.tooltipDisplay.hideTooltip(), new ReferenceLinkedOpenHashSet<>(this.tooltipDisplay.hiddenComponents()));
            }
            clone.tooltipStyle = this.tooltipStyle;
            clone.itemModel = this.itemModel;
            clone.unbreakable = this.unbreakable;
            clone.enchantmentGlintOverride = this.enchantmentGlintOverride;
            clone.glider = this.glider;
            clone.damageResistant = this.damageResistant;
            clone.maxStackSize = this.maxStackSize;
            clone.rarity = this.rarity;
            if (this.hasUseRemainder()) {
                clone.useRemainder = this.useRemainder.clone();
            }
            if (this.hasUseCooldown()) {
                clone.useCooldown = new CraftUseCooldownComponent(this.useCooldown);
            }
            if (this.hasFood()) {
                clone.food = new CraftFoodComponent(this.food);
            }
            if (this.hasTool()) {
                clone.tool = new CraftToolComponent(this.tool);
            }
            if (this.hasEquippable()) {
                clone.equippable = new CraftEquippableComponent(this.equippable);
            }
            if (this.hasJukeboxPlayable()) {
                clone.jukebox = new CraftJukeboxComponent(this.jukebox);
            }
            clone.damage = this.damage;
            clone.maxDamage = this.maxDamage;
            clone.version = this.version;

            if (this.canPlaceOnPredicates != null) {
                clone.canPlaceOnPredicates = List.copyOf(this.canPlaceOnPredicates);
            }
            if (this.canBreakPredicates != null) {
                clone.canBreakPredicates = List.copyOf(this.canBreakPredicates);
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    @Override
    public final Map<String, Object> serialize() {
        ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();
        map.put(SerializableMeta.TYPE_FIELD, SerializableMeta.classMap.get(this.getClass()));
        this.serialize(map);
        return map.build();
    }

    @Overridden
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        if (this.hasDisplayName()) {
            builder.put(CraftMetaItem.NAME.BUKKIT, CraftChatMessage.toJSON(this.displayName));
        }

        if (this.hasItemName()) {
            builder.put(CraftMetaItem.ITEM_NAME.BUKKIT, CraftChatMessage.toJSON(this.itemName));
        }

        if (this.hasLore()) {
            // SPIGOT-7625: Convert lore to json before serializing it
            List<String> jsonLore = new ArrayList<>();

            for (Component component : this.lore) {
                jsonLore.add(CraftChatMessage.toJSON(component));
            }

            builder.put(CraftMetaItem.LORE.BUKKIT, jsonLore);
        }

        if (this.hasCustomModelDataComponent()) {
            builder.put(CraftMetaItem.CUSTOM_MODEL_DATA.BUKKIT, this.customModelData);
        }
        if (this.hasEnchantable()) {
            builder.put(CraftMetaItem.ENCHANTABLE.BUKKIT, this.enchantableValue);
        }
        if (this.hasBlockData()) {
            builder.put(CraftMetaItem.BLOCK_DATA.BUKKIT, this.blockData);
        }

        CraftMetaItem.serializeEnchantments(this.enchantments, builder, CraftMetaItem.ENCHANTMENTS);
        CraftMetaItem.serializeModifiers(this.attributeModifiers, builder, CraftMetaItem.ATTRIBUTES);

        if (this.hasRepairCost()) {
            builder.put(CraftMetaItem.REPAIR.BUKKIT, this.repairCost);
        }

        List<String> hideFlags = new ArrayList<>();
        for (ItemFlag hideFlagEnum : this.getItemFlags()) {
            hideFlags.add(CraftItemFlag.bukkitToString(hideFlagEnum));
        }
        if (!hideFlags.isEmpty()) {
            builder.put(CraftMetaItem.HIDEFLAGS.BUKKIT, hideFlags);
        }

        if (this.hasTooltipStyle()) {
            builder.put(CraftMetaItem.TOOLTIP_STYLE.BUKKIT, this.tooltipStyle.toString());
        }

        if (this.hasItemModel()) {
            builder.put(CraftMetaItem.ITEM_MODEL.BUKKIT, this.itemModel.toString());
        }

        if (this.isUnbreakable()) {
            builder.put(CraftMetaItem.UNBREAKABLE.BUKKIT, this.unbreakable);
        }

        if (this.hasEnchantmentGlintOverride()) {
            builder.put(CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.BUKKIT, this.enchantmentGlintOverride);
        }

        if (this.isGlider()) {
            builder.put(CraftMetaItem.GLIDER.BUKKIT, this.glider);
        }

        if (this.hasDamageResistant()) {
            builder.put(CraftMetaItem.DAMAGE_RESISTANT.BUKKIT, this.damageResistant.location().toString());
        }

        if (this.hasMaxStackSize()) {
            builder.put(CraftMetaItem.MAX_STACK_SIZE.BUKKIT, this.maxStackSize);
        }

        if (this.hasRarity()) {
            builder.put(CraftMetaItem.RARITY.BUKKIT, this.rarity.name());
        }

        if (this.hasUseRemainder()) {
            builder.put(CraftMetaItem.USE_REMAINDER.BUKKIT, this.useRemainder);
        }

        if (this.hasUseCooldown()) {
            builder.put(CraftMetaItem.USE_COOLDOWN.BUKKIT, this.useCooldown);
        }

        if (this.hasFood()) {
            builder.put(CraftMetaItem.FOOD.BUKKIT, this.food);
        }

        if (this.hasTool()) {
            builder.put(CraftMetaItem.TOOL.BUKKIT, this.tool);
        }

        if (this.hasEquippable()) {
            builder.put(CraftMetaItem.EQUIPPABLE.BUKKIT, this.equippable);
        }

        if (this.hasJukeboxPlayable()) {
            builder.put(CraftMetaItem.JUKEBOX_PLAYABLE.BUKKIT, this.jukebox);
        }

        if (this.hasDamageValue()) { // Paper - preserve empty/0 damage
            builder.put(CraftMetaItem.DAMAGE.BUKKIT, this.damage);
        }

        if (this.hasMaxDamage()) {
            builder.put(CraftMetaItem.MAX_DAMAGE.BUKKIT, this.maxDamage);
        }

        final Map<String, net.minecraft.nbt.Tag> internalTags = new HashMap<>();
        this.serializeInternal(internalTags);
        if (!internalTags.isEmpty()) {
            CompoundTag internal = new CompoundTag();
            for (Map.Entry<String, net.minecraft.nbt.Tag> e : internalTags.entrySet()) {
                internal.put(e.getKey(), e.getValue());
            }
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed(internal, buf);
                builder.put("internal", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                LOGGER.error("Failed to write internal tag for item", ex);
            }
        }

        final boolean canBreakAddToUnhandled = this.canBreakPredicates != null && !this.canBreakPredicates.isEmpty();
        if (canBreakAddToUnhandled) {
            this.unhandledTags.set(DataComponents.CAN_BREAK, new net.minecraft.world.item.AdventureModePredicate(this.canBreakPredicates));
        }
        final boolean canPlaceOnAddToUnhandled = this.canPlaceOnPredicates != null && !this.canPlaceOnPredicates.isEmpty();
        if (canPlaceOnAddToUnhandled) {
            this.unhandledTags.set(DataComponents.CAN_PLACE_ON, new net.minecraft.world.item.AdventureModePredicate(this.canPlaceOnPredicates));
        }

        if (!this.unhandledTags.isEmpty()) {
            net.minecraft.nbt.Tag unhandled = DataComponentPatch.CODEC.encodeStart(CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE), this.unhandledTags.build()).getOrThrow(IllegalStateException::new);
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed((CompoundTag) unhandled, buf);
                builder.put("unhandled", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                LOGGER.error("Failed to write unhandled tag for item", ex);
            }
        }

        if (canBreakAddToUnhandled) {
            this.unhandledTags.clear(DataComponents.CAN_BREAK);
        }
        if (canPlaceOnAddToUnhandled) {
            this.unhandledTags.clear(DataComponents.CAN_PLACE_ON);
        }

        if (!this.removedTags.isEmpty()) {
            RegistryAccess registryAccess = CraftRegistry.getMinecraftRegistry();
            Registry<DataComponentType<?>> componentTypeRegistry = registryAccess.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);

            List<String> removedTags = new ArrayList<>();
            for (DataComponentType<?> removed : this.removedTags) {
                String componentKey = componentTypeRegistry.getResourceKey(removed).orElseThrow().location().toString();

                removedTags.add(componentKey);
            }

            builder.put("removed", removedTags);
        }

        if (!this.persistentDataContainer.isEmpty()) { // Store custom tags, wrapped in their compound
            builder.put(CraftMetaItem.BUKKIT_CUSTOM_TAG.BUKKIT, this.persistentDataContainer.serialize());
        }

        if (this.customTag != null) {
            try {
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                NbtIo.writeCompressed(this.customTag, buf);
                builder.put("custom", Base64.getEncoder().encodeToString(buf.toByteArray()));
            } catch (IOException ex) {
                LOGGER.error("Failed to write custom tag for item", ex);
            }
        }

        return builder;
    }

    void serializeInternal(final Map<String, net.minecraft.nbt.Tag> unhandledTags) {
    }

    static void serializeEnchantments(Map<Enchantment, Integer> enchantments, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (enchantments == null || enchantments.isEmpty()) {
            return;
        }

        ImmutableMap.Builder<String, Integer> enchants = ImmutableMap.builder();
        for (Map.Entry<? extends Enchantment, Integer> enchant : enchantments.entrySet()) {
            enchants.put(CraftEnchantment.bukkitToString(enchant.getKey()), enchant.getValue());
        }

        builder.put(key.BUKKIT, enchants.build());
    }

    static void serializeModifiers(Multimap<Attribute, AttributeModifier> modifiers, ImmutableMap.Builder<String, Object> builder, ItemMetaKey key) {
        if (modifiers == null) { // null and an empty map have different behaviors
            return;
        }

        Map<String, List<Object>> mods = new LinkedHashMap<>();
        for (Map.Entry<Attribute, AttributeModifier> entry : modifiers.entries()) {
            if (entry.getKey() == null) {
                continue;
            }
            Collection<AttributeModifier> modCollection = modifiers.get(entry.getKey());
            if (modCollection == null || modCollection.isEmpty()) {
                continue;
            }
            mods.put(CraftAttribute.bukkitToString(entry.getKey()), new ArrayList<>(modCollection));
        }
        builder.put(key.BUKKIT, mods);
    }

    static void safelyAdd(Iterable<?> addFrom, Collection<Component> addTo, boolean possiblyJsonInput) {
        if (addFrom == null) {
            return;
        }

        for (Object object : addFrom) {
            if (object instanceof net.md_5.bungee.api.chat.BaseComponent[] baseComponentArr) {
                addTo.add(CraftChatMessage.bungeeToVanilla(baseComponentArr));
            } else if (!(object instanceof String)) {
                if (object != null) {
                    // SPIGOT-7399: Null check via if is important,
                    // otherwise object.getClass().getName() could throw an error for a valid argument -> when it is null which is valid,
                    // when using Preconditions
                    throw new IllegalArgumentException(addFrom + " cannot contain non-string " + object.getClass().getName());
                }

                addTo.add(Component.empty());
            } else {
                String entry = object.toString();
                Component component = (possiblyJsonInput) ? CraftChatMessage.fromJSONOrString(entry) : CraftChatMessage.fromStringOrNull(entry);

                if (component != null) {
                    addTo.add(component);
                } else {
                    addTo.add(Component.empty());
                }
            }
        }
    }

    static boolean checkConflictingEnchants(Map<Enchantment, Integer> enchantments, Enchantment ench) {
        if (enchantments == null || enchantments.isEmpty()) {
            return false;
        }

        for (Enchantment enchant : enchantments.keySet()) {
            if (enchant.conflictsWith(ench)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public final String toString() {
        return SerializableMeta.classMap.get(this.getClass()) + "_META:" + this.serialize(); // TODO: cry
    }

    public int getVersion() {
        return this.version;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    // Paper start - improve checking handled tags
    @org.jetbrains.annotations.VisibleForTesting
    public static final Map<Class<? extends CraftMetaItem>, Set<DataComponentType<?>>> HANDLED_DCTS_PER_TYPE = new HashMap<>();
    protected static final Set<DataComponentType<?>> DEFAULT_HANDLED_DCTS = Set.of(
        CraftMetaItem.NAME.TYPE,
        CraftMetaItem.ITEM_NAME.TYPE,
        CraftMetaItem.LORE.TYPE,
        CraftMetaItem.CUSTOM_MODEL_DATA.TYPE,
        CraftMetaItem.ENCHANTABLE.TYPE,
        CraftMetaItem.BLOCK_DATA.TYPE,
        CraftMetaItem.REPAIR.TYPE,
        CraftMetaItem.ENCHANTMENTS.TYPE,
        CraftMetaItem.TOOLTIP_DISPLAY.TYPE,
        CraftMetaItem.TOOLTIP_STYLE.TYPE,
        CraftMetaItem.ITEM_MODEL.TYPE,
        CraftMetaItem.UNBREAKABLE.TYPE,
        CraftMetaItem.ENCHANTMENT_GLINT_OVERRIDE.TYPE,
        CraftMetaItem.GLIDER.TYPE,
        CraftMetaItem.DAMAGE_RESISTANT.TYPE,
        CraftMetaItem.MAX_STACK_SIZE.TYPE,
        CraftMetaItem.RARITY.TYPE,
        CraftMetaItem.USE_REMAINDER.TYPE,
        CraftMetaItem.USE_COOLDOWN.TYPE,
        CraftMetaItem.FOOD.TYPE,
        CraftMetaItem.TOOL.TYPE,
        CraftMetaItem.EQUIPPABLE.TYPE,
        CraftMetaItem.JUKEBOX_PLAYABLE.TYPE,
        CraftMetaItem.DAMAGE.TYPE,
        CraftMetaItem.MAX_DAMAGE.TYPE,
        CraftMetaItem.CUSTOM_DATA.TYPE,
        CraftMetaItem.ATTRIBUTES.TYPE,
        CraftMetaItem.CAN_PLACE_ON.TYPE,
        CraftMetaItem.CAN_BREAK.TYPE
    );
    public static Set<DataComponentType<?>> getTopLevelHandledDcts(final Class<? extends CraftMetaItem> clazz) {
        synchronized (HANDLED_DCTS_PER_TYPE) {
            if (HANDLED_DCTS_PER_TYPE.isEmpty()) {
                final Map<Class<? extends CraftMetaItem>, Set<DataComponentType<?>>> map = new HashMap<>();
                map.put(CraftMetaArmor.class, Set.of(CraftMetaArmor.TRIM.TYPE));
                map.put(CraftMetaArmorStand.class, Set.of(CraftMetaArmorStand.ENTITY_TAG.TYPE));
                map.put(CraftMetaAxolotlBucket.class, Set.of(CraftMetaAxolotlBucket.ENTITY_TAG.TYPE, CraftMetaAxolotlBucket.BUCKET_ENTITY_TAG.TYPE));
                map.put(CraftMetaBanner.class, Set.of(CraftMetaBanner.PATTERNS.TYPE)); // banner uses same tag as block state
                map.put(CraftMetaShield.class, Set.of(CraftMetaShield.BASE_COLOR.TYPE, CraftMetaBanner.PATTERNS.TYPE));
                map.put(CraftMetaBlockState.class, Set.of(CraftMetaBlockState.BLOCK_ENTITY_TAG.TYPE));
                map.put(CraftMetaBook.class, Set.of(CraftMetaBook.BOOK_CONTENT.TYPE));
                map.put(CraftMetaBookSigned.class, Set.of(CraftMetaBookSigned.BOOK_CONTENT.TYPE));
                map.put(CraftMetaBundle.class, Set.of(CraftMetaBundle.ITEMS.TYPE));
                map.put(CraftMetaCharge.class, Set.of(CraftMetaCharge.EXPLOSION.TYPE));
                map.put(CraftMetaColorableArmor.class, Set.of(CraftMetaArmor.TRIM.TYPE, CraftMetaLeatherArmor.COLOR.TYPE));
                map.put(CraftMetaCompass.class, Set.of(CraftMetaCompass.LODESTONE_TARGET.TYPE));
                map.put(CraftMetaCrossbow.class, Set.of(CraftMetaCrossbow.CHARGED_PROJECTILES.TYPE));
                map.put(CraftMetaEnchantedBook.class, Set.of(CraftMetaEnchantedBook.STORED_ENCHANTMENTS.TYPE));
                map.put(CraftMetaEntityTag.class, Set.of(CraftMetaEntityTag.ENTITY_TAG.TYPE));
                map.put(CraftMetaFirework.class, Set.of(CraftMetaFirework.FIREWORKS.TYPE));
                map.put(CraftMetaKnowledgeBook.class, Set.of(CraftMetaKnowledgeBook.BOOK_RECIPES.TYPE));
                map.put(CraftMetaLeatherArmor.class, Set.of(CraftMetaLeatherArmor.COLOR.TYPE));
                map.put(CraftMetaMap.class, Set.of(CraftMetaMap.MAP_COLOR.TYPE, CraftMetaMap.MAP_POST_PROCESSING.TYPE, CraftMetaMap.MAP_ID.TYPE));
                map.put(CraftMetaMusicInstrument.class, Set.of(CraftMetaMusicInstrument.GOAT_HORN_INSTRUMENT.TYPE));
                map.put(CraftMetaOminousBottle.class, Set.of(CraftMetaOminousBottle.OMINOUS_BOTTLE_AMPLIFIER.TYPE));
                map.put(CraftMetaPotion.class, Set.of(CraftMetaPotion.POTION_CONTENTS.TYPE));
                map.put(CraftMetaSkull.class, Set.of(CraftMetaSkull.SKULL_PROFILE.TYPE, CraftMetaSkull.NOTE_BLOCK_SOUND.TYPE));
                map.put(CraftMetaSpawnEgg.class, Set.of(CraftMetaSpawnEgg.ENTITY_TAG.TYPE));
                map.put(CraftMetaSuspiciousStew.class, Set.of(CraftMetaSuspiciousStew.EFFECTS.TYPE));
                map.put(CraftMetaTropicalFishBucket.class, Set.of(CraftMetaTropicalFishBucket.ENTITY_TAG.TYPE, CraftMetaTropicalFishBucket.BUCKET_ENTITY_TAG.TYPE));

                for (final Map.Entry<Class<? extends CraftMetaItem>, Set<DataComponentType<?>>> entry : map.entrySet()) {
                    final ArrayList<DataComponentType<?>> topLevelTags = new ArrayList<>(entry.getValue());
                    // add tags common to CraftMetaItem to all
                    topLevelTags.addAll(DEFAULT_HANDLED_DCTS);
                    HANDLED_DCTS_PER_TYPE.put(entry.getKey(), Set.copyOf(topLevelTags));
                }
            }
            return HANDLED_DCTS_PER_TYPE.getOrDefault(clazz, DEFAULT_HANDLED_DCTS);
        }
    }
    // Paper end - improve checking handled data component types

    protected static <T> Optional<? extends T> getOrEmpty(DataComponentPatch tag, ItemMetaKeyType<T> type) {
        return getOrEmpty(tag, type.TYPE);
    }

    protected static <T> Optional<? extends T> getOrEmpty(final DataComponentPatch tag, final DataComponentType<T> type) {
        Optional<? extends T> result = tag.get(type);
        return (result != null) ? result : Optional.empty();
    }

    private static class EnchantmentMap extends java.util.TreeMap<org.bukkit.enchantments.Enchantment, Integer> {
        private EnchantmentMap(Map<Enchantment, Integer> enchantments) {
            this();
            putAll(enchantments);
        }

        private EnchantmentMap() {
            super(Comparator.comparing(o -> o.getKey().toString()));
        }

        public EnchantmentMap clone() {
            return (EnchantmentMap) super.clone();
        }
    }

    @Override
    public Set<Material> getCanDestroy() {
        return !this.hasDestroyableKeys() ? Collections.emptySet() : convertToLegacyMaterial(this.canBreakPredicates);
    }

    @Override
    public void setCanDestroy(final Set<Material> canDestroy) {
        Preconditions.checkArgument(canDestroy != null, "Cannot replace with null set!");
        this.canBreakPredicates = convertFromLegacyMaterial(canDestroy);
    }

    @Override
    public Set<Material> getCanPlaceOn() {
        return !this.hasPlaceableKeys() ? Collections.emptySet() : convertToLegacyMaterial(this.canPlaceOnPredicates);
    }

    @Override
    public void setCanPlaceOn(final Set<Material> canPlaceOn) {
        Preconditions.checkArgument(canPlaceOn != null, "Cannot replace with null set!");
        this.canPlaceOnPredicates = convertFromLegacyMaterial(canPlaceOn);
    }

    private static List<net.minecraft.advancements.critereon.BlockPredicate> convertFromLegacyMaterial(final Collection<Material> materials) {
        final net.minecraft.core.Registry<net.minecraft.world.level.block.Block> blockRegistry = CraftRegistry.getMinecraftRegistry().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK);
        return materials.stream().map(m -> {
            return net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(blockRegistry, CraftBlockType.bukkitToMinecraft(m)).build();
        }).toList();
    }

    private static Set<Material> convertToLegacyMaterial(final List<net.minecraft.advancements.critereon.BlockPredicate> predicates) {
        return predicates.stream()
            .flatMap(p -> p.blocks().map(net.minecraft.core.HolderSet::stream).orElse(java.util.stream.Stream.empty()))
            .map(holder -> CraftBlockType.minecraftToBukkit(holder.value()))
            .collect(java.util.stream.Collectors.toSet());
    }

    @Override
    public Set<com.destroystokyo.paper.Namespaced> getDestroyableKeys() {
        return !this.hasDestroyableKeys() ? Collections.emptySet() : convertToLegacyNamespaced(this.canBreakPredicates);
    }

    @Override
    public void setDestroyableKeys(final Collection<com.destroystokyo.paper.Namespaced> canDestroy) {
        Preconditions.checkArgument(canDestroy != null, "Cannot replace with null collection!");
        Preconditions.checkArgument(ofAcceptableType(canDestroy), "Can only use NamespacedKey or NamespacedTag objects!");
        this.canBreakPredicates = convertFromLegacyNamespaced(canDestroy);
    }

    @Override
    public Set<com.destroystokyo.paper.Namespaced> getPlaceableKeys() {
        return !this.hasPlaceableKeys() ? Collections.emptySet() : convertToLegacyNamespaced(this.canPlaceOnPredicates);
    }

    @Override
    public void setPlaceableKeys(final Collection<com.destroystokyo.paper.Namespaced> canPlaceOn) {
        Preconditions.checkArgument(canPlaceOn != null, "Cannot replace with null collection!");
        Preconditions.checkArgument(ofAcceptableType(canPlaceOn), "Can only use NamespacedKey or NamespacedTag objects!");
        this.canPlaceOnPredicates = convertFromLegacyNamespaced(canPlaceOn);
    }

    private static List<net.minecraft.advancements.critereon.BlockPredicate> convertFromLegacyNamespaced(final Collection<com.destroystokyo.paper.Namespaced> namespaceds) {
        final List<net.minecraft.advancements.critereon.BlockPredicate> predicates = new ArrayList<>();
        final net.minecraft.core.Registry<net.minecraft.world.level.block.Block> blockRegistry = CraftRegistry.getMinecraftRegistry().lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK);
        for (final com.destroystokyo.paper.Namespaced namespaced : namespaceds) {
            if (namespaced instanceof final org.bukkit.NamespacedKey key) {
                predicates.add(net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(blockRegistry, CraftBlockType.bukkitToMinecraft(Objects.requireNonNull(org.bukkit.Registry.MATERIAL.get(key)))).build());
            } else if (namespaced instanceof final com.destroystokyo.paper.NamespacedTag tag) {
                predicates.add(net.minecraft.advancements.critereon.BlockPredicate.Builder.block().of(blockRegistry, net.minecraft.tags.TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(tag.getNamespace(), tag.getKey()))).build());
            }
        }
        return predicates;
    }

    private static Set<com.destroystokyo.paper.Namespaced> convertToLegacyNamespaced(final Collection<net.minecraft.advancements.critereon.BlockPredicate> predicates) {
        final Set<com.destroystokyo.paper.Namespaced> namespaceds = Sets.newHashSet();
        for (final net.minecraft.advancements.critereon.BlockPredicate predicate : predicates) {
            if (predicate.blocks().isEmpty()) {
                continue;
            }
            final net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> holders = predicate.blocks().get();
            if (holders instanceof final net.minecraft.core.HolderSet.Named<net.minecraft.world.level.block.Block> named) {
                namespaceds.add(new com.destroystokyo.paper.NamespacedTag(named.key().location().getNamespace(), named.key().location().getPath()));
            } else {
                holders.forEach(h -> {
                    h.unwrapKey().ifPresent(key -> {
                        namespaceds.add(new org.bukkit.NamespacedKey(key.location().getNamespace(), key.location().getPath()));
                    });
                });
            }
        }
        return namespaceds;
    }

    @Override
    public boolean hasPlaceableKeys() {
        return this.canPlaceOnPredicates != null;
    }

    @Override
    public boolean hasDestroyableKeys() {
        return this.canBreakPredicates != null;
    }

    // not a fan of this
    private static boolean ofAcceptableType(final Collection<com.destroystokyo.paper.Namespaced> namespacedResources) {
        for (com.destroystokyo.paper.Namespaced resource : namespacedResources) {
            if (!(resource instanceof org.bukkit.NamespacedKey || resource instanceof com.destroystokyo.paper.NamespacedTag)) {
                return false;
            }
        }

        return true;
    }
}
