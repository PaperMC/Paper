package com.destroystokyo.paper.brigadier;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import java.util.function.Predicate;

/**
 * Brigadier {@link Command}, {@link SuggestionProvider}, and permission checker for Bukkit {@link Command}s.
 *
 * @param <S> command source type
 * @deprecated For removal, see {@link io.papermc.paper.command.brigadier.Commands} on how to use the new Brigadier API.
 */
@Deprecated(forRemoval = true, since = "1.20.6")
public interface BukkitBrigadierCommand <S extends BukkitBrigadierCommandSource> extends Command<S>, Predicate<S>, SuggestionProvider<S> {
}
