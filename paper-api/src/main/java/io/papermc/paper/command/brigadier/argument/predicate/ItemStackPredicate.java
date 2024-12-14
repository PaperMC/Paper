package io.papermc.paper.command.brigadier.argument.predicate;

import java.util.function.Predicate;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

/**
 * A predicate for ItemStack.
 *
 * @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#itemPredicate()
 * @since 1.20.6
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ItemStackPredicate extends Predicate<ItemStack> {
}
