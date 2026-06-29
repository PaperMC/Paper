package io.papermc.paper.registry.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.papermc.paper.advancement.CriteriaTrigger;
import io.papermc.paper.registry.RegistryBuilder;
import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific registry entry for the {@link CriteriaTrigger} type.
 *
 * @param <I> the type of the instance for the trigger
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface CriteriaTriggerRegistryEntry<I> {

    /**
     * Provides the deserializer for the trigger instance.
     *
     * @return the deserializer for the trigger instance
     */
    @Contract(pure = true)
    Function<JsonObject, I> deserializer();

    /**
     * A mutable builder for {@link CriteriaTriggerRegistryEntry} which plugins may change
     * in applicable registry events.
     * <p>
     * The following values are required for each builder:
     * <ul>
     *     <li>{@link #deserializer(Function)}</li>
     * </ul>
     *
     * @param <I> the type of the instance for the trigger
     */
    @ApiStatus.Experimental
    @ApiStatus.NonExtendable
    interface Builder<I> extends CriteriaTriggerRegistryEntry<I>, RegistryBuilder<CriteriaTrigger<I>> {

        /**
         * Sets the deserializer for the trigger instance.
         *
         * @param deserializer the deserializer for the trigger instance
         * @return this builder instance
         */
        @Contract(value = "_ -> this", mutates = "this")
        Builder<I> deserializer(Function<JsonObject, I> deserializer);
    }
}
