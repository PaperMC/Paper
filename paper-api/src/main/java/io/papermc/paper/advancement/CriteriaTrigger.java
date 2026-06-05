package io.papermc.paper.advancement;

import java.util.function.Predicate;
import org.bukkit.Keyed;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

/**
 * Trigger type for advancements.
 *
 * @param <I> the type of the instance for the trigger
 */
@NullMarked
@ApiStatus.NonExtendable
@ApiStatus.Experimental
public interface CriteriaTrigger<I> extends Keyed {

    /**
     * Activates the trigger for a specific player and predicate against the trigger instance.
     *
     * @param player the player to activate the trigger for
     * @param instancePredicate the predicate to test the trigger instance against
     * @apiNote Only works for custom triggers registered via {@link io.papermc.paper.registry.event.RegistryEvents#TRIGGER_TYPE}
     */
    void trigger(Player player, Predicate<? super I> instancePredicate);
}
