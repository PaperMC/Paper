package org.bukkit.metadata;

import org.bukkit.plugin.Plugin;

import java.util.*;

public abstract class MetadataStoreBase<T> {
    private Map<String, List<MetadataValue>> metadataMap = new HashMap<String, List<MetadataValue>>();
    private WeakHashMap<T, Map<String, String>> disambiguationCache = new WeakHashMap<T, Map<String, String>>();

    /**
     * Adds a metadata value to an object. Each metadata value is owned by a specific{@link Plugin}.
     * If a plugin has already added a metadata value to an object, that value
     * will be replaced with the value of {@code newMetadataValue}. Multiple plugins can set independent values for
     * the same {@code metadataKey} without conflict.
     * <p/>
     * Implementation note: I considered using a {@link java.util.concurrent.locks.ReadWriteLock} for controlling
     * access to {@code metadataMap}, but decided that the added overhead wasn't worth the finer grained access control.
     * Bukkit is almost entirely single threaded so locking overhead shouldn't pose a problem.
     *
     * @param subject          The object receiving the metadata.
     * @param metadataKey      A unique key to identify this metadata.
     * @param newMetadataValue The metadata value to apply.
     * @see MetadataStore#setMetadata(Object, String, MetadataValue)
     */
    public synchronized void setMetadata(T subject, String metadataKey, MetadataValue newMetadataValue) {
        String key = cachedDisambiguate(subject, metadataKey);
        if (!metadataMap.containsKey(key)) {
            metadataMap.put(key, new ArrayList<MetadataValue>());
        }
        // we now have a list of subject's metadata for the given metadata key. If newMetadataValue's owningPlugin
        // is found in this list, replace the value rather than add a new one.
        List<MetadataValue> metadataList = metadataMap.get(key);
        for (int i = 0; i < metadataList.size(); i++) {
            if (metadataList.get(i).getOwningPlugin().equals(newMetadataValue.getOwningPlugin())) {
                metadataList.set(i, newMetadataValue);
                return;
            }
        }
        // we didn't find a duplicate...add the new metadata value
        metadataList.add(newMetadataValue);
    }

    /**
     * Returns all metadata values attached to an object. If multiple plugins have attached metadata, each will value
     * will be included.
     *
     * @param subject     the object being interrogated.
     * @param metadataKey the unique metadata key being sought.
     * @return A list of values, one for each plugin that has set the requested value.
     * @see MetadataStore#getMetadata(Object, String)
     */
    public synchronized List<MetadataValue> getMetadata(T subject, String metadataKey) {
        String key = cachedDisambiguate(subject, metadataKey);
        if (metadataMap.containsKey(key)) {
            return Collections.unmodifiableList(metadataMap.get(key));
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Tests to see if a metadata attribute has been set on an object.
     *
     * @param subject     the object upon which the has-metadata test is performed.
     * @param metadataKey the unique metadata key being queried.
     * @return the existence of the metadataKey within subject.
     */
    public synchronized boolean hasMetadata(T subject, String metadataKey) {
        String key = cachedDisambiguate(subject, metadataKey);
        return metadataMap.containsKey(key);
    }

    /**
     * Removes a metadata item owned by a plugin from a subject.
     *
     * @param subject      the object to remove the metadata from.
     * @param metadataKey  the unique metadata key identifying the metadata to remove.
     * @param owningPlugin the plugin attempting to remove a metadata item.
     * @see MetadataStore#removeMetadata(Object, String, org.bukkit.plugin.Plugin)
     */
    public synchronized void removeMetadata(T subject, String metadataKey, Plugin owningPlugin) {
        String key = cachedDisambiguate(subject, metadataKey);
        List<MetadataValue> metadataList = metadataMap.get(key);
        if (metadataList == null) return;
        for (int i = 0; i < metadataList.size(); i++) {
            if (metadataList.get(i).getOwningPlugin().equals(owningPlugin)) {
                metadataList.remove(i);
                if (metadataList.isEmpty()) {
                    metadataMap.remove(key);
                }
            }
        }
    }

    /**
     * Invalidates all metadata in the metadata store that originates from the given plugin. Doing this will force
     * each invalidated metadata item to be recalculated the next time it is accessed.
     *
     * @param owningPlugin the plugin requesting the invalidation.
     * @see MetadataStore#invalidateAll(org.bukkit.plugin.Plugin)
     */
    public synchronized void invalidateAll(Plugin owningPlugin) {
        if (owningPlugin == null) {
            throw new IllegalArgumentException("owningPlugin cannot be null");
        }

        for (List<MetadataValue> values : metadataMap.values()) {
            for (MetadataValue value : values) {
                if (value.getOwningPlugin().equals(owningPlugin)) {
                    value.invalidate();
                }
            }
        }
    }

    /**
     * Caches the results of calls to {@link MetadataStoreBase#disambiguate(Object, String)} in a {@link WeakHashMap}. Doing so maintains a
     * <a href="http://www.codeinstructions.com/2008/09/weakhashmap-is-not-cache-understanding.html">canonical list</a>
     * of disambiguation strings for objects in memory. When those objects are garbage collected, the disambiguation string
     * in the list is aggressively garbage collected as well.
     *
     * @param subject     The object for which this key is being generated.
     * @param metadataKey The name identifying the metadata value.
     * @return a unique metadata key for the given subject.
     */
    private String cachedDisambiguate(T subject, String metadataKey) {
        if (disambiguationCache.containsKey(subject) && disambiguationCache.get(subject).containsKey(metadataKey)) {
            return disambiguationCache.get(subject).get(metadataKey);
        } else {
            if (!disambiguationCache.containsKey(subject)) {
                disambiguationCache.put(subject, new HashMap<String, String>());
            }
            String disambiguation = disambiguate(subject, metadataKey);
            disambiguationCache.get(subject).put(metadataKey, disambiguation);
            return disambiguation;
        }
    }

    /**
     * Creates a unique name for the object receiving metadata by combining unique data from the subject with a metadataKey.
     * The name created must be globally unique for the given object and any two equivalent objects must generate the
     * same unique name. For example, two Player objects must generate the same string if they represent the same player,
     * even if the objects would fail a reference equality test.
     *
     * @param subject     The object for which this key is being generated.
     * @param metadataKey The name identifying the metadata value.
     * @return a unique metadata key for the given subject.
     */
    protected abstract String disambiguate(T subject, String metadataKey);
}
