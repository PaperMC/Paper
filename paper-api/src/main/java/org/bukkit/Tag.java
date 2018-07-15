package org.bukkit;

import java.util.Set;

/**
 * Represents a tag that may be defined by the server or a resource pack to
 * group like things together.
 *
 * @param <T> the type of things grouped by this tag
 */
public interface Tag<T extends Keyed> {

    /**
     * Key for the built in block registry.
     */
    String REGISTRY_BLOCKS = "blocks";
    /**
     * Vanilla block tag representing all colors of wool.
     */
    Tag<Material> WOOL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wool"), Material.class);
    /**
     * Vanilla block tag representing all plank variants.
     */
    Tag<Material> PLANKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("planks"), Material.class);
    /**
     * Vanilla block tag representing all regular/mossy/cracked/chiseled stone
     * bricks.
     */
    Tag<Material> STONE_BRICKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stone_bricks"), Material.class);
    /**
     * Vanilla block tag representing all wooden buttons.
     */
    Tag<Material> WOODEN_BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_buttons"), Material.class);
    /**
     * Vanilla block tag representing all buttons (inherits from
     * {@link #WOODEN_BUTTONS}.
     */
    Tag<Material> BUTTONS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("buttons"), Material.class);
    /**
     * Vanilla block tag representing all colors of carpet.
     */
    Tag<Material> CARPETS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("carpets"), Material.class);
    /**
     * Vanilla block tag representing all wooden doors.
     */
    Tag<Material> WOODEN_DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_doors"), Material.class);
    /**
     * Vanilla block tag representing all wooden stairs.
     */
    Tag<Material> WOODEN_STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_stairs"), Material.class);
    /**
     * Vanilla block tag representing all wooden slabs.
     */
    Tag<Material> WOODEN_SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_slabs"), Material.class);
    /**
     * Vanilla block tag representing all wooden pressure plates.
     */
    Tag<Material> WOODEN_PRESSURE_PLATES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("wooden_pressure_plates"), Material.class);
    /**
     * Vanilla block tag representing all doors (inherits from
     * {@link #WOODEN_DOORS}.
     */
    Tag<Material> DOORS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("doors"), Material.class);
    /**
     * Vanilla block tag representing all sapling variants.
     */
    Tag<Material> SAPLINGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("saplings"), Material.class);
    /**
     * Vanilla block tag representing all log and bark variants.
     */
    Tag<Material> LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("logs"), Material.class);
    /**
     * Vanilla block tag representing all dark oak log and bark variants.
     */
    Tag<Material> DARK_OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dark_oak_logs"), Material.class);
    /**
     * Vanilla block tag representing all oak log and bark variants.
     */
    Tag<Material> OAK_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("oak_logs"), Material.class);
    /**
     * Vanilla block tag representing all birch log and bark variants.
     */
    Tag<Material> BIRCH_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("birch_logs"), Material.class);
    /**
     * Vanilla block tag representing all acacia log and bark variants.
     */
    Tag<Material> ACACIA_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("acacia_logs"), Material.class);
    /**
     * Vanilla block tag representing all jungle log and bark variants.
     */
    Tag<Material> JUNGLE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("jungle_logs"), Material.class);
    /**
     * Vanilla block tag representing all spruce log and bark variants.
     */
    Tag<Material> SPRUCE_LOGS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("spruce_logs"), Material.class);
    /**
     * Vanilla block tag representing all banner blocks.
     */
    Tag<Material> BANNERS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla block tag representing all sand blocks.
     */
    Tag<Material> SAND = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("sand"), Material.class);
    /**
     * Vanilla block tag representing all stairs.
     */
    Tag<Material> STAIRS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("stairs"), Material.class);
    /**
     * Vanilla block tag representing all slabs.
     */
    Tag<Material> SLABS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("slabs"), Material.class);
    /**
     * Vanilla block tag representing all damaged and undamaged anvils.
     */
    Tag<Material> ANVIL = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("anvil"), Material.class);
    /**
     * Vanilla block tag representing all damaged and undamaged anvils.
     */
    Tag<Material> RAILS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("rails"), Material.class);
    /**
     * Vanilla block tag representing all live coral.
     */
    Tag<Material> LIVE_CORAL_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("live_coral_blocks"), Material.class);
    /**
     * Vanilla block tag representing all dead coral.
     */
    Tag<Material> DEAD_CORAL_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("dead_coral_blocks"), Material.class);
    /**
     * Vanilla block tag representing all coral.
     */
    Tag<Material> CORAL_BLOCKS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_blocks"), Material.class);
    /**
     * Vanilla block tag representing all coral.
     */
    Tag<Material> CORALS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("corals"), Material.class);
    /**
     * Vanilla block tag representing all coral fans.
     */
    Tag<Material> CORAL_FANS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("coral_fans"), Material.class);
    /**
     * Vanilla block tag representing all leaves fans.
     */
    Tag<Material> LEAVES = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("leaves"), Material.class);
    /**
     * Vanilla block tag representing all empty and filled flower pots.
     */
    Tag<Material> FLOWER_POTS = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("flower_pots"), Material.class);
    /**
     * Vanilla block tag denoting blocks that enderman may pick up and hold.
     */
    Tag<Material> ENDERMAN_HOLDABLE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("enderman_holdable"), Material.class);
    /**
     * Vanilla block tag denoting ice blocks.
     */
    Tag<Material> ICE = Bukkit.getTag(REGISTRY_BLOCKS, NamespacedKey.minecraft("ice"), Material.class);
    /**
     * Key for the built in item registry.
     */
    String REGISTRY_ITEMS = "items";
    /**
     * Vanilla item tag representing all banner items.
     */
    Tag<Material> ITEMS_BANNERS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("banners"), Material.class);
    /**
     * Vanilla item tag representing all boat items.
     */
    Tag<Material> ITEMS_BOATS = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("boats"), Material.class);
    /**
     * Vanilla item tag representing all fish items.
     */
    Tag<Material> ITEMS_FISHES = Bukkit.getTag(REGISTRY_ITEMS, NamespacedKey.minecraft("fishes"), Material.class);

    /**
     * Returns whether or not this tag has an entry for the specified item.
     *
     * @param item to check
     * @return if it is tagged
     */
    boolean isTagged(T item);

    /**
     * Gets an immutable set of all tagged items.
     *
     * @return set of tagged items
     */
    Set<T> getValues();

}
