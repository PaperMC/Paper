package io.papermc.paper.tag;

import io.papermc.paper.plugin.lifecycle.event.registrar.Registrar;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.tag.TagKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

/**
 * Registrar for tags before they are flattened. Flattened
 * tags are tags which have any nested tags resolved to the tagged
 * values the nested tags point to. This registrar, being a pre-flatten
 * registrar, allows for modification before that flattening has happened, when
 * tags both point to individual entries and other nested tags.
 * <p>
 * An example of a tag being created in a pre-flatten registrar:
 * <pre>{@code
 * class YourBootstrapClass implements PluginBootstrap {
 *
 *     public static final TagKey<ItemType> AXE_PICKAXE = ItemTypeTagKeys.create(Key.key("papermc:axe_pickaxe"));
 *
 *     @Override
 *     public void bootstrap(BootstrapContext context) {
 *         final LifecycleEventManager<BootstrapContext> manager = context.getLifecycleManager();
 *         manager.registerEventHandler(LifecycleEvents.TAGS.preFlatten(RegistryKey.ITEM), event -> {
 *             final PreFlattenTagRegistrar<ItemType> registrar = event.registrar();
 *             registrar.setTag(AXE_PICKAXE, Set.of(
 *                 TagEntry.tagEntry(ItemTypeTagKeys.PICKAXES),
 *                 TagEntry.tagEntry(ItemTypeTagKeys.AXES)
 *             ));
 *         });
 *     }
 * }
 * }</pre>
 *
 * @param <T> the type of value in the tag
 * @see PostFlattenTagRegistrar
 */
@ApiStatus.Experimental
@NullMarked
@ApiStatus.NonExtendable
public interface PreFlattenTagRegistrar<T> extends Registrar {

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
    @Unmodifiable Map<TagKey<T>, Collection<TagEntry<T>>> getAllTags();

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
    @Unmodifiable List<TagEntry<T>> getTag(TagKey<T> tagKey);

    /**
     * Adds entries to the given tag. If the tag does not exist, it will be created.
     *
     * @param tagKey the key of the tag to add to
     * @param entries the entries to add
     * @see #setTag(TagKey, Collection)
     */
    @Contract(mutates = "this")
    void addToTag(TagKey<T> tagKey, Collection<TagEntry<T>> entries);

    /**
     * Sets the entries of the given tag. If the tag does not exist, it will be created.
     * If the tag does exist, it will be overwritten.
     *
     * @param tagKey the key of the tag to set
     * @param entries the entries to set
     * @see #addToTag(TagKey, Collection)
     */
    @Contract(mutates = "this")
    void setTag(TagKey<T> tagKey, Collection<TagEntry<T>> entries);
}
