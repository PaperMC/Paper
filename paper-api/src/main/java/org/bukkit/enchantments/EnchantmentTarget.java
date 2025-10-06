package org.bukkit.enchantments;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the applicable target for a {@link Enchantment}
 *
 * @deprecated enchantment groupings are now managed by tags, not categories
 */
@Deprecated(since = "1.20.5", forRemoval = true)
public enum EnchantmentTarget {
    /**
     * Allows the Enchantment to be placed on all items
     *
     * @deprecated this target no longer exists in Vanilla
     */
    @Deprecated(since = "1.16.1", forRemoval = true)
    ALL {
        @Override
        public boolean includes(@NotNull Material item) {
            for (EnchantmentTarget target : EnchantmentTarget.values()) {
                if (target != this && target.includes(item)) {
                    return true;
                }
            }

            return false;
        }
    },

    /**
     * Allows the Enchantment to be placed on armor
     */
    ARMOR {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_ARMOR.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on feet slot armor
     */
    ARMOR_FEET {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_FOOT_ARMOR.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on leg slot armor
     */
    ARMOR_LEGS {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_LEG_ARMOR.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on torso slot armor
     */
    ARMOR_TORSO {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_CHEST_ARMOR.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on head slot armor
     */
    ARMOR_HEAD {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_HEAD_ARMOR.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on weapons (swords)
     */
    WEAPON {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_SWORD.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on tools (spades, pickaxe, axes)
     */
    TOOL {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_MINING_LOOT.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on bows.
     */
    BOW {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_BOW.isTagged(item);
        }
    },

    /**
     * Allows the Enchantment to be placed on fishing rods.
     */
    FISHING_ROD {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_FISHING.isTagged(item);
        }
    },

    /**
     * Allows the enchantment to be placed on items with durability.
     */
    BREAKABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_DURABILITY.isTagged(item);
        }
    },

    /**
     * Allows the enchantment to be placed on wearable items.
     */
    WEARABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_EQUIPPABLE.isTagged(item);
        }
    },

    /**
     * Allow the Enchantment to be placed on tridents.
     */
    TRIDENT {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_TRIDENT.isTagged(item);
        }
    },

    /**
     * Allow the Enchantment to be placed on crossbows.
     */
    CROSSBOW {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_CROSSBOW.isTagged(item);
        }
    },

    /**
     * Allow the Enchantment to be placed on vanishing items.
     */
    VANISHABLE {
        @Override
        public boolean includes(@NotNull Material item) {
            return Tag.ITEMS_ENCHANTABLE_VANISHING.isTagged(item);
        }
    };

    /**
     * Check whether this target includes the specified item.
     *
     * @param item The item to check
     * @return True if the target includes the item
     */
    public abstract boolean includes(@NotNull Material item);

    /**
     * Check whether this target includes the specified item.
     *
     * @param item The item to check
     * @return True if the target includes the item
     */
    public boolean includes(@NotNull ItemStack item) {
        return includes(item.getType());
    }
}
