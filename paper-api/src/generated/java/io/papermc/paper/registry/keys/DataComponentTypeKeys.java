package io.papermc.paper.registry.keys;

import static net.kyori.adventure.key.Key.key;

import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.generated.GeneratedFrom;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import net.kyori.adventure.key.Key;
import org.jspecify.annotations.NullMarked;

/**
 * Vanilla keys for {@link RegistryKey#DATA_COMPONENT_TYPE}.
 *
 * @apiNote The fields provided here are a direct representation of
 * what is available from the vanilla game source. They may be
 * changed (including removals) on any Minecraft version
 * bump, so cross-version compatibility is not provided on the
 * same level as it is on most of the other API.
 */
@SuppressWarnings({
        "unused",
        "SpellCheckingInspection"
})
@NullMarked
@GeneratedFrom("1.21.6-pre1")
public final class DataComponentTypeKeys {
    /**
     * {@code minecraft:attribute_modifiers}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ATTRIBUTE_MODIFIERS = create(key("attribute_modifiers"));

    /**
     * {@code minecraft:axolotl/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> AXOLOTL_VARIANT = create(key("axolotl/variant"));

    /**
     * {@code minecraft:banner_patterns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BANNER_PATTERNS = create(key("banner_patterns"));

    /**
     * {@code minecraft:base_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BASE_COLOR = create(key("base_color"));

    /**
     * {@code minecraft:bees}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BEES = create(key("bees"));

    /**
     * {@code minecraft:block_entity_data}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BLOCK_ENTITY_DATA = create(key("block_entity_data"));

    /**
     * {@code minecraft:block_state}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BLOCK_STATE = create(key("block_state"));

    /**
     * {@code minecraft:blocks_attacks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BLOCKS_ATTACKS = create(key("blocks_attacks"));

    /**
     * {@code minecraft:break_sound}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BREAK_SOUND = create(key("break_sound"));

    /**
     * {@code minecraft:bucket_entity_data}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BUCKET_ENTITY_DATA = create(key("bucket_entity_data"));

    /**
     * {@code minecraft:bundle_contents}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> BUNDLE_CONTENTS = create(key("bundle_contents"));

    /**
     * {@code minecraft:can_break}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CAN_BREAK = create(key("can_break"));

    /**
     * {@code minecraft:can_place_on}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CAN_PLACE_ON = create(key("can_place_on"));

    /**
     * {@code minecraft:cat/collar}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CAT_COLLAR = create(key("cat/collar"));

    /**
     * {@code minecraft:cat/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CAT_VARIANT = create(key("cat/variant"));

    /**
     * {@code minecraft:charged_projectiles}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CHARGED_PROJECTILES = create(key("charged_projectiles"));

    /**
     * {@code minecraft:chicken/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CHICKEN_VARIANT = create(key("chicken/variant"));

    /**
     * {@code minecraft:consumable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CONSUMABLE = create(key("consumable"));

    /**
     * {@code minecraft:container}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CONTAINER = create(key("container"));

    /**
     * {@code minecraft:container_loot}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CONTAINER_LOOT = create(key("container_loot"));

    /**
     * {@code minecraft:cow/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> COW_VARIANT = create(key("cow/variant"));

    /**
     * {@code minecraft:creative_slot_lock}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CREATIVE_SLOT_LOCK = create(key("creative_slot_lock"));

    /**
     * {@code minecraft:custom_data}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CUSTOM_DATA = create(key("custom_data"));

    /**
     * {@code minecraft:custom_model_data}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CUSTOM_MODEL_DATA = create(key("custom_model_data"));

    /**
     * {@code minecraft:custom_name}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> CUSTOM_NAME = create(key("custom_name"));

    /**
     * {@code minecraft:damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> DAMAGE = create(key("damage"));

    /**
     * {@code minecraft:damage_resistant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> DAMAGE_RESISTANT = create(key("damage_resistant"));

    /**
     * {@code minecraft:death_protection}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> DEATH_PROTECTION = create(key("death_protection"));

    /**
     * {@code minecraft:debug_stick_state}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> DEBUG_STICK_STATE = create(key("debug_stick_state"));

    /**
     * {@code minecraft:dyed_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> DYED_COLOR = create(key("dyed_color"));

    /**
     * {@code minecraft:enchantable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ENCHANTABLE = create(key("enchantable"));

    /**
     * {@code minecraft:enchantment_glint_override}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ENCHANTMENT_GLINT_OVERRIDE = create(key("enchantment_glint_override"));

    /**
     * {@code minecraft:enchantments}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ENCHANTMENTS = create(key("enchantments"));

    /**
     * {@code minecraft:entity_data}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ENTITY_DATA = create(key("entity_data"));

    /**
     * {@code minecraft:equippable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> EQUIPPABLE = create(key("equippable"));

    /**
     * {@code minecraft:firework_explosion}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> FIREWORK_EXPLOSION = create(key("firework_explosion"));

    /**
     * {@code minecraft:fireworks}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> FIREWORKS = create(key("fireworks"));

    /**
     * {@code minecraft:food}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> FOOD = create(key("food"));

    /**
     * {@code minecraft:fox/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> FOX_VARIANT = create(key("fox/variant"));

    /**
     * {@code minecraft:frog/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> FROG_VARIANT = create(key("frog/variant"));

    /**
     * {@code minecraft:glider}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> GLIDER = create(key("glider"));

    /**
     * {@code minecraft:horse/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> HORSE_VARIANT = create(key("horse/variant"));

    /**
     * {@code minecraft:instrument}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> INSTRUMENT = create(key("instrument"));

    /**
     * {@code minecraft:intangible_projectile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> INTANGIBLE_PROJECTILE = create(key("intangible_projectile"));

    /**
     * {@code minecraft:item_model}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ITEM_MODEL = create(key("item_model"));

    /**
     * {@code minecraft:item_name}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> ITEM_NAME = create(key("item_name"));

    /**
     * {@code minecraft:jukebox_playable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> JUKEBOX_PLAYABLE = create(key("jukebox_playable"));

    /**
     * {@code minecraft:llama/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> LLAMA_VARIANT = create(key("llama/variant"));

    /**
     * {@code minecraft:lock}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> LOCK = create(key("lock"));

    /**
     * {@code minecraft:lodestone_tracker}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> LODESTONE_TRACKER = create(key("lodestone_tracker"));

    /**
     * {@code minecraft:lore}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> LORE = create(key("lore"));

    /**
     * {@code minecraft:map_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAP_COLOR = create(key("map_color"));

    /**
     * {@code minecraft:map_decorations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAP_DECORATIONS = create(key("map_decorations"));

    /**
     * {@code minecraft:map_id}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAP_ID = create(key("map_id"));

    /**
     * {@code minecraft:map_post_processing}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAP_POST_PROCESSING = create(key("map_post_processing"));

    /**
     * {@code minecraft:max_damage}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAX_DAMAGE = create(key("max_damage"));

    /**
     * {@code minecraft:max_stack_size}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MAX_STACK_SIZE = create(key("max_stack_size"));

    /**
     * {@code minecraft:mooshroom/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> MOOSHROOM_VARIANT = create(key("mooshroom/variant"));

    /**
     * {@code minecraft:note_block_sound}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> NOTE_BLOCK_SOUND = create(key("note_block_sound"));

    /**
     * {@code minecraft:ominous_bottle_amplifier}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> OMINOUS_BOTTLE_AMPLIFIER = create(key("ominous_bottle_amplifier"));

    /**
     * {@code minecraft:painting/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PAINTING_VARIANT = create(key("painting/variant"));

    /**
     * {@code minecraft:parrot/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PARROT_VARIANT = create(key("parrot/variant"));

    /**
     * {@code minecraft:pig/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PIG_VARIANT = create(key("pig/variant"));

    /**
     * {@code minecraft:pot_decorations}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> POT_DECORATIONS = create(key("pot_decorations"));

    /**
     * {@code minecraft:potion_contents}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> POTION_CONTENTS = create(key("potion_contents"));

    /**
     * {@code minecraft:potion_duration_scale}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> POTION_DURATION_SCALE = create(key("potion_duration_scale"));

    /**
     * {@code minecraft:profile}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PROFILE = create(key("profile"));

    /**
     * {@code minecraft:provides_banner_patterns}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PROVIDES_BANNER_PATTERNS = create(key("provides_banner_patterns"));

    /**
     * {@code minecraft:provides_trim_material}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> PROVIDES_TRIM_MATERIAL = create(key("provides_trim_material"));

    /**
     * {@code minecraft:rabbit/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> RABBIT_VARIANT = create(key("rabbit/variant"));

    /**
     * {@code minecraft:rarity}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> RARITY = create(key("rarity"));

    /**
     * {@code minecraft:recipes}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> RECIPES = create(key("recipes"));

    /**
     * {@code minecraft:repair_cost}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> REPAIR_COST = create(key("repair_cost"));

    /**
     * {@code minecraft:repairable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> REPAIRABLE = create(key("repairable"));

    /**
     * {@code minecraft:salmon/size}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> SALMON_SIZE = create(key("salmon/size"));

    /**
     * {@code minecraft:sheep/color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> SHEEP_COLOR = create(key("sheep/color"));

    /**
     * {@code minecraft:shulker/color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> SHULKER_COLOR = create(key("shulker/color"));

    /**
     * {@code minecraft:stored_enchantments}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> STORED_ENCHANTMENTS = create(key("stored_enchantments"));

    /**
     * {@code minecraft:suspicious_stew_effects}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> SUSPICIOUS_STEW_EFFECTS = create(key("suspicious_stew_effects"));

    /**
     * {@code minecraft:tool}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TOOL = create(key("tool"));

    /**
     * {@code minecraft:tooltip_display}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TOOLTIP_DISPLAY = create(key("tooltip_display"));

    /**
     * {@code minecraft:tooltip_style}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TOOLTIP_STYLE = create(key("tooltip_style"));

    /**
     * {@code minecraft:trim}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TRIM = create(key("trim"));

    /**
     * {@code minecraft:tropical_fish/base_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TROPICAL_FISH_BASE_COLOR = create(key("tropical_fish/base_color"));

    /**
     * {@code minecraft:tropical_fish/pattern}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TROPICAL_FISH_PATTERN = create(key("tropical_fish/pattern"));

    /**
     * {@code minecraft:tropical_fish/pattern_color}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> TROPICAL_FISH_PATTERN_COLOR = create(key("tropical_fish/pattern_color"));

    /**
     * {@code minecraft:unbreakable}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> UNBREAKABLE = create(key("unbreakable"));

    /**
     * {@code minecraft:use_cooldown}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> USE_COOLDOWN = create(key("use_cooldown"));

    /**
     * {@code minecraft:use_remainder}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> USE_REMAINDER = create(key("use_remainder"));

    /**
     * {@code minecraft:villager/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> VILLAGER_VARIANT = create(key("villager/variant"));

    /**
     * {@code minecraft:weapon}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WEAPON = create(key("weapon"));

    /**
     * {@code minecraft:wolf/collar}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WOLF_COLLAR = create(key("wolf/collar"));

    /**
     * {@code minecraft:wolf/sound_variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WOLF_SOUND_VARIANT = create(key("wolf/sound_variant"));

    /**
     * {@code minecraft:wolf/variant}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WOLF_VARIANT = create(key("wolf/variant"));

    /**
     * {@code minecraft:writable_book_content}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WRITABLE_BOOK_CONTENT = create(key("writable_book_content"));

    /**
     * {@code minecraft:written_book_content}
     *
     * @apiNote This field is version-dependant and may be removed in future Minecraft versions
     */
    public static final TypedKey<DataComponentType> WRITTEN_BOOK_CONTENT = create(key("written_book_content"));

    private DataComponentTypeKeys() {
    }

    private static TypedKey<DataComponentType> create(final Key key) {
        return TypedKey.create(RegistryKey.DATA_COMPONENT_TYPE, key);
    }
}
