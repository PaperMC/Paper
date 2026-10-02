package io.papermc.paper.command.brigadier.argument.resolvers.selector;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.resolvers.ArgumentResolver;
import java.util.List;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

/// An [ArgumentResolver] that's capable of resolving
/// a player selector argument value using a [CommandSourceStack].
///
/// @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#player()
/// @see io.papermc.paper.command.brigadier.argument.ArgumentTypes#players()
@ApiStatus.NonExtendable
public interface PlayerSelectorArgumentResolver extends SelectorArgumentResolver<List<Player>> {
}
