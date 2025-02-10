package io.papermc.paper.entity;

import java.util.Collection;
import java.util.Collections;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPlayerGiveResult(
    @Unmodifiable Collection<ItemStack> leftovers,
    @Unmodifiable Collection<Item> drops
) implements PlayerGiveResult {

    public static final PlayerGiveResult EMPTY = new PaperPlayerGiveResult(
        Collections.emptyList(), Collections.emptyList()
    );

}
