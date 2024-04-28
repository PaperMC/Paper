package io.papermc.paper.datacomponent.item;

import org.checkerframework.checker.index.qual.Positive;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

/**
 * Holds if an item is enchantable, allowing for enchantments of the type to be seen in an enchanting table.
 * @see io.papermc.paper.datacomponent.DataComponentTypes#ENCHANTABLE
 */
@NullMarked
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface Enchantable {

    @Contract(value = "_ -> new", pure = true)
    static Enchantable enchantable(final @Positive int level) {
        return ItemComponentTypesBridge.bridge().enchantable(level);
    }

    /**
     * Gets the current enchantment value level allowed,
     * a higher value allows enchantments with a higher cost to be picked.
     *
     * @return the value
     * @see <a href="https://minecraft.wiki/w/Enchanting_mechanics#Java_Edition_2">Minecraft Wiki</a>
     */
    @Contract(pure = true)
    @Positive int value();
}
