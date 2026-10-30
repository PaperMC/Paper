package io.papermc.paper.datacomponent.item;

import org.bukkit.Tag;
import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * Represents items that villagers can eat.
 * Villagers will pick up items with this component in addition to items in the {@link Tag#ITEMS_VILLAGER_PICKS_UP} tag.
 *
 * @see io.papermc.paper.datacomponent.DataComponentTypes#VILLAGER_FOOD
 */
@ApiStatus.NonExtendable
public interface VillagerFood {

    @Contract(value = "_ -> new", pure = true)
    static VillagerFood villagerFood(final @Positive int nutrition) {
        return ItemComponentTypesBridge.bridge().villagerFood(nutrition);
    }

    /**
     * Gets the nutrition value of this item.
     *
     * @return the nutrition value
     */
    @Contract(pure = true)
    @Positive int nutrition();
}
