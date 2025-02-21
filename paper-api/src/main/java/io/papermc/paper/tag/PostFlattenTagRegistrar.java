package io.papermc.paper.tag;

import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Registrar for tags after they have been flattened. Flattened
 * tags are tags which have any nested tags resolved to the tagged
 * values the nested tags point to. This registrar, being a post-flatten
 * registrar, allows for modification after that flattening has happened, when
 * tags only point to individual entries and not other nested tags.
 * <p>
 * An example of a custom enchant being registered to the vanilla
 * {@code #minecraft:in_enchanting_table} tag:
 * <pre>{@code
 * class YourBootstrapClass implements PluginBootstrap {
 *
 *     public static final TypedKey<Enchantment> CUSTOM_POINTY_ENCHANT = EnchantmentKeys.create(Key.key("papermc:pointy"));
 *
 *     @Override
 *     public void bootstrap(BootstrapContext context) {
 *         final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
 *         manager.registerEventHandler(LifecycleEvents.TAGS.postFlatten(RegistryKey.ENCHANTMENT), event -> {
 *             final PostFlattenTagRegistrar<Enchantment> registrar = event.registrar();
 *             registrar.addToTag(
 *                 EnchantmentTagKeys.IN_ENCHANTING_TABLE,
 *                 Set.of(CUSTOM_POINTY_ENCHANT)
 *             );
 *         });
 *     }
 * }
 * }</pre>
 *
 * @param <T> the type of value in the tag
 * @see PreFlattenTagRegistrar
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface PostFlattenTagRegistrar<T> extends Registrar {

    /**
     * Get the registry key for this tag registrar.
     *
     * @return the registry key
     */
    RegistryKey<T> registryKey();

    /**
     * Get a copy of all tags currently held in this registrar.
     *
     * @return an immutable map of all tags
     */
    @Contract(value = "-> new", pure = true)
    @Unmodifiable Map<TagKey<T>, Collection<TypedKey<T>>> getAllTags();

    /**
     * Checks if this registrar has a tag with the given key.
     *
     * @param tagKey the key to check for
     * @return true if the tag exists, false otherwise
     */
    @Contract(pure = true)
    boolean hasTag(TagKey<T> tagKey);

    /**
     * Get the tag with the given key. Use {@link #hasTag(TagKey)} to check
     * if a tag exists first.
     *
     * @param tagKey the key of the tag to get
     * @return an immutable list of tag entries
     * @throws java.util.NoSuchElementException if the tag does not exist
     * @see #hasTag(TagKey)
     */
    @Contract(value = "_ -> new", pure = true)
    @Unmodifiable Collection<TypedKey<T>> getTag(TagKey<T> tagKey);

    /**
     * Adds values to the given tag. If the tag does not exist, it will be created.
     *
     * @param tagKey the key of the tag to add to
     * @param values the values to add
     * @see #setTag(TagKey, Collection)
     */
    @Contract(mutates = "this")
    void addToTag(TagKey<T> tagKey, Collection<TypedKey<T>> values);

    /**
     * Sets the values of the given tag. If the tag does not exist, it will be created.
     * If the tag does exist, it will be overwritten.
     *
     * @param tagKey the key of the tag to set
     * @param values the values to set
     * @see #addToTag(TagKey, Collection)
     */
    @Contract(mutates = "this")
    void setTag(TagKey<T> tagKey, Collection<TypedKey<T>> values);
}
