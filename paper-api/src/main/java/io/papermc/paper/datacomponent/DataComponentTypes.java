package io.papermc.paper.datacomponent;

import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.BlockItemDataProperties;
import io.papermc.paper.datacomponent.item.BundleContents;
import io.papermc.paper.datacomponent.item.ChargedProjectiles;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.DamageResistant;
import io.papermc.paper.datacomponent.item.DeathProtection;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.Enchantable;
import io.papermc.paper.datacomponent.item.Equippable;
import io.papermc.paper.datacomponent.item.Fireworks;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ItemAdventurePredicate;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemAttributeModifiers;
import io.papermc.paper.datacomponent.item.ItemContainerContents;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.JukeboxPlayable;
import io.papermc.paper.datacomponent.item.LodestoneTracker;
import io.papermc.paper.datacomponent.item.MapDecorations;
import io.papermc.paper.datacomponent.item.MapId;
import io.papermc.paper.datacomponent.item.MapItemColor;
import io.papermc.paper.datacomponent.item.OminousBottleAmplifier;
import io.papermc.paper.datacomponent.item.PotDecorations;
import io.papermc.paper.datacomponent.item.PotionContents;
import io.papermc.paper.datacomponent.item.Repairable;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.SeededContainerLoot;
import io.papermc.paper.datacomponent.item.SuspiciousStewEffects;
import io.papermc.paper.datacomponent.item.Tool;
import io.papermc.paper.datacomponent.item.Unbreakable;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.UseRemainder;
import io.papermc.paper.datacomponent.item.WritableBookContent;
import io.papermc.paper.datacomponent.item.WrittenBookContent;
import io.papermc.paper.item.MapPostProcessing;
import java.util.List;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemRarity;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.common.value.qual.IntRange;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import static java.util.Objects.requireNonNull;

/**
 * All the different types of data that {@link org.bukkit.inventory.ItemStack ItemStacks}
 * and {@link org.bukkit.inventory.ItemType ItemTypes} can have.
 */
@NullMarked
@ApiStatus.Experimental
public final class DataComponentTypes {

    // Start generate - DataComponentTypes
    // @GeneratedFrom 1.21.4
    /**
     * Holds attribute modifiers applied to any item,
     * if not set, has an implicit default value based on the item type's
     * default attributes (e.g. attack damage for weapons).
     */
    public static final DataComponentType.Valued<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = valued("attribute_modifiers");

    /**
     * Stores the additional patterns applied to a Banner or Shield.
     */
    public static final DataComponentType.Valued<BannerPatternLayers> BANNER_PATTERNS = valued("banner_patterns");

    /**
     * Stores the base color for a Shield.
     */
    public static final DataComponentType.Valued<DyeColor> BASE_COLOR = valued("base_color");

    /**
     * Holds block state properties to apply when placing a block.
     */
    public static final DataComponentType.Valued<BlockItemDataProperties> BLOCK_DATA = valued("block_state");

    /**
     * Holds all items stored inside a Bundle.
     * If removed, items cannot be added to the Bundle.
     */
    public static final DataComponentType.Valued<BundleContents> BUNDLE_CONTENTS = valued("bundle_contents");

    /**
     * Controls which blocks a player in Adventure mode can break with this item.
     */
    public static final DataComponentType.Valued<ItemAdventurePredicate> CAN_BREAK = valued("can_break");

    /**
     * Controls which blocks a player in Adventure mode can place on with this item.
     */
    public static final DataComponentType.Valued<ItemAdventurePredicate> CAN_PLACE_ON = valued("can_place_on");

    /**
     * Holds all projectiles that have been loaded into a Crossbow.
     * If not present, the Crossbow is not charged.
     */
    public static final DataComponentType.Valued<ChargedProjectiles> CHARGED_PROJECTILES = valued("charged_projectiles");

    public static final DataComponentType.Valued<Consumable> CONSUMABLE = valued("consumable");

    /**
     * Holds the contents of container blocks (Chests, Shulker Boxes) in item form.
     */
    public static final DataComponentType.Valued<ItemContainerContents> CONTAINER = valued("container");

    /**
     * Holds the unresolved loot table and seed of a container-like block.
     */
    public static final DataComponentType.Valued<SeededContainerLoot> CONTAINER_LOOT = valued("container_loot");

    /**
     * Controls the minecraft:custom_model_data property in the item model.
     */
    public static final DataComponentType.Valued<CustomModelData> CUSTOM_MODEL_DATA = valued("custom_model_data");

    /**
     * Custom name override for an item (as set by renaming with an Anvil).
     *
     * @see #ITEM_NAME
     */
    public static final DataComponentType.Valued<Component> CUSTOM_NAME = valued("custom_name");

    /**
     * The amount of durability removed from an item,
     * for damageable items (with the {@link #MAX_DAMAGE} component), has an implicit default value of: {@code 0}.
     *
     * @see #MAX_DAMAGE
     */
    public static final DataComponentType.Valued<@NonNegative Integer> DAMAGE = valued("damage");

    /**
     * If present, this item will not take damage from the specified damage types.
     */
    public static final DataComponentType.Valued<DamageResistant> DAMAGE_RESISTANT = valued("damage_resistant");

    public static final DataComponentType.Valued<DeathProtection> DEATH_PROTECTION = valued("death_protection");

    /**
     * Represents a color applied to a dyeable item (in the {@link io.papermc.paper.registry.keys.tags.ItemTypeTagKeys#DYEABLE} item tag).
     */
    public static final DataComponentType.Valued<DyedItemColor> DYED_COLOR = valued("dyed_color");

    public static final DataComponentType.Valued<Enchantable> ENCHANTABLE = valued("enchantable");

    /**
     * Overrides the enchantment glint effect on an item.
     * If not present, default behaviour is used.
     */
    public static final DataComponentType.Valued<Boolean> ENCHANTMENT_GLINT_OVERRIDE = valued("enchantment_glint_override");

    /**
     * Controls the enchantments on an item.
     * <br>
     * If not present on a non-enchantment book, this item will not work in an anvil.
     *
     * @see #STORED_ENCHANTMENTS
     */
    public static final DataComponentType.Valued<ItemEnchantments> ENCHANTMENTS = valued("enchantments");

    public static final DataComponentType.Valued<Equippable> EQUIPPABLE = valued("equippable");

    /**
     * Stores the explosion crafted in a Firework Star.
     */
    public static final DataComponentType.Valued<FireworkEffect> FIREWORK_EXPLOSION = valued("firework_explosion");

    /**
     * Stores all explosions crafted into a Firework Rocket, as well as flight duration.
     */
    public static final DataComponentType.Valued<Fireworks> FIREWORKS = valued("fireworks");

    /**
     * Controls potential food benefits gained when consuming the item the component is applied on.
     * Requires the {@link #CONSUMABLE} component to allow consumption in the first place.
     */
    public static final DataComponentType.Valued<FoodProperties> FOOD = valued("food");

    public static final DataComponentType.NonValued GLIDER = unvalued("glider");

    /**
     * If set, disables 'additional' tooltip part which comes from the item type
     * (e.g. content of a shulker).
     */
    public static final DataComponentType.NonValued HIDE_ADDITIONAL_TOOLTIP = unvalued("hide_additional_tooltip");

    /**
     * If set, it will completely hide whole item tooltip (that includes item name).
     */
    public static final DataComponentType.NonValued HIDE_TOOLTIP = unvalued("hide_tooltip");

    /**
     * Holds the instrument type used by a Goat Horn.
     */
    public static final DataComponentType.Valued<MusicInstrument> INSTRUMENT = valued("instrument");

    /**
     * Marks that a projectile item would be intangible when fired
     * (i.e. can only be picked up by a creative mode player).
     */
    public static final DataComponentType.NonValued INTANGIBLE_PROJECTILE = unvalued("intangible_projectile");

    public static final DataComponentType.Valued<Key> ITEM_MODEL = valued("item_model");

    /**
     * When present, replaces default item name with contained chat component.
     * <p>
     * Differences from {@link #CUSTOM_NAME}:
     * <ul>
     * <li>can't be changed or removed in Anvil</li>
     * <li>is not styled with italics when displayed to player</li>
     * <li>does not show labels where applicable
     *      (for example: banner markers, names in item frames)</li>
     * </ul>
     *
     * @see #CUSTOM_NAME
     */
    public static final DataComponentType.Valued<Component> ITEM_NAME = valued("item_name");

    public static final DataComponentType.Valued<JukeboxPlayable> JUKEBOX_PLAYABLE = valued("jukebox_playable");

    /**
     * If present, specifies that the Compass is a Lodestone Compass.
     */
    public static final DataComponentType.Valued<LodestoneTracker> LODESTONE_TRACKER = valued("lodestone_tracker");

    /**
     * Additional lines to include in an item's tooltip.
     */
    public static final DataComponentType.Valued<ItemLore> LORE = valued("lore");

    /**
     * Represents the tint of the decorations on the {@link org.bukkit.inventory.ItemType#FILLED_MAP} item.
     */
    public static final DataComponentType.Valued<MapItemColor> MAP_COLOR = valued("map_color");

    /**
     * Holds a list of markers to be placed on a {@link org.bukkit.inventory.ItemType#FILLED_MAP} (used for Explorer Maps).
     */
    public static final DataComponentType.Valued<MapDecorations> MAP_DECORATIONS = valued("map_decorations");

    /**
     * References the shared map state holding map contents and markers for a {@link org.bukkit.inventory.ItemType#FILLED_MAP}.
     */
    public static final DataComponentType.Valued<MapId> MAP_ID = valued("map_id");

    /**
     * Internal map item state used in the map crafting recipe.
     */
    public static final DataComponentType.Valued<MapPostProcessing> MAP_POST_PROCESSING = valued("map_post_processing");

    /**
     * Controls the maximum amount of damage than an item can take,
     * if not present, the item cannot be damaged.
     * <br>
     * Mutually exclusive with the {@link #MAX_STACK_SIZE} component greater than 1.
     *
     * @see #DAMAGE
     */
    public static final DataComponentType.Valued<@Positive Integer> MAX_DAMAGE = valued("max_damage");

    /**
     * Controls the maximum stacking size of this item.
     * <br>
     * Values greater than 1 are mutually exclusive with the {@link #MAX_DAMAGE} component.
     */
    public static final DataComponentType.Valued<Integer> MAX_STACK_SIZE = valued("max_stack_size");

    /**
     * Controls the sound played by a Player Head when placed on a Note Block.
     */
    public static final DataComponentType.Valued<Key> NOTE_BLOCK_SOUND = valued("note_block_sound");

    /**
     * Controls the amplifier amount for an Ominous Bottle's Bad Omen effect.
     */
    public static final DataComponentType.Valued<OminousBottleAmplifier> OMINOUS_BOTTLE_AMPLIFIER = valued("ominous_bottle_amplifier");

    /**
     * Stores the Sherds applied to each side of a Decorated Pot.
     */
    public static final DataComponentType.Valued<PotDecorations> POT_DECORATIONS = valued("pot_decorations");

    /**
     * Holds the contents of a potion (Potion, Splash Potion, Lingering Potion),
     * or potion applied to a Tipped Arrow.
     */
    public static final DataComponentType.Valued<PotionContents> POTION_CONTENTS = valued("potion_contents");

    /**
     * Controls the skin displayed on a Player Head.
     */
    public static final DataComponentType.Valued<ResolvableProfile> PROFILE = valued("profile");

    /**
     * Controls the color of the item name.
     */
    public static final DataComponentType.Valued<ItemRarity> RARITY = valued("rarity");

    /**
     * List of recipes that should be unlocked when using the Knowledge Book item.
     */
    public static final DataComponentType.Valued<List<Key>> RECIPES = valued("recipes");

    /**
     * The additional experience cost required to modify an item in an Anvil.
     * If not present, has an implicit default value of: {@code 0}.
     */
    public static final DataComponentType.Valued<@NonNegative Integer> REPAIR_COST = valued("repair_cost");

    public static final DataComponentType.Valued<Repairable> REPAIRABLE = valued("repairable");

    /**
     * Stores list of enchantments and their levels for an Enchanted Book.
     * Unlike {@link #ENCHANTMENTS}, the effects provided by enchantments
     * do not apply from this component.
     * <br>
     * If not present on an Enchanted Book, it will not work in an anvil.
     * <p>
     * Has an undefined behaviour if present on an item that is not an Enchanted Book
     * (currently the presence of this component allows enchantments from {@link #ENCHANTMENTS}
     * to be applied as if this item was an Enchanted Book).
     *
     * @see #ENCHANTMENTS
     */
    public static final DataComponentType.Valued<ItemEnchantments> STORED_ENCHANTMENTS = valued("stored_enchantments");

    /**
     * Holds the effects that will be applied when consuming Suspicious Stew.
     */
    public static final DataComponentType.Valued<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = valued("suspicious_stew_effects");

    /**
     * Controls the behavior of the item as a tool.
     */
    public static final DataComponentType.Valued<Tool> TOOL = valued("tool");

    public static final DataComponentType.Valued<Key> TOOLTIP_STYLE = valued("tooltip_style");

    /**
     * Holds the trims applied to an item in recipes
     */
    public static final DataComponentType.Valued<ItemArmorTrim> TRIM = valued("trim");

    /**
     * If set, the item will not lose any durability when used.
     */
    public static final DataComponentType.Valued<Unbreakable> UNBREAKABLE = valued("unbreakable");

    public static final DataComponentType.Valued<UseCooldown> USE_COOLDOWN = valued("use_cooldown");

    public static final DataComponentType.Valued<UseRemainder> USE_REMAINDER = valued("use_remainder");

    /**
     * Holds the contents in a Book and Quill.
     */
    public static final DataComponentType.Valued<WritableBookContent> WRITABLE_BOOK_CONTENT = valued("writable_book_content");

    /**
     * Holds the contents and metadata of a Written Book.
     */
    public static final DataComponentType.Valued<WrittenBookContent> WRITTEN_BOOK_CONTENT = valued("written_book_content");
    // End generate - DataComponentTypes
    // /**
    //  * Causes an item to not be pickable in the creative menu, currently not very useful.
    //  */
    // public static final DataComponentType.NonValued CREATIVE_SLOT_LOCK = unvalued("creative_slot_lock");
    // debug_stick_state - Block Property API
    // entity_data
    // bucket_entity_data
    // block_entity_data
    // bees
    // /**
    //  * Holds the lock state of a container-like block,
    //  * copied to container block when placed.
    //  * <br>
    //  * An item with a custom name of the same value must be used
    //  * to open this container.
    //  */
    // public static final DataComponentType.Valued<LockCode> LOCK = valued("lock");

    private static DataComponentType.NonValued unvalued(final String name) {
        return (DataComponentType.NonValued) requireNonNull(Registry.DATA_COMPONENT_TYPE.get(NamespacedKey.minecraft(name)), name + " unvalued data component type couldn't be found, this is a bug.");
    }

    @SuppressWarnings("unchecked")
    private static <T> DataComponentType.Valued<T> valued(final String name) {
        return (DataComponentType.Valued<T>) requireNonNull(Registry.DATA_COMPONENT_TYPE.get(NamespacedKey.minecraft(name)), name + " valued data component type couldn't be found, this is a bug.");
    }

    private DataComponentTypes() {
    }
}
