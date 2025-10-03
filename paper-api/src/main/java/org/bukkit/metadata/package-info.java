/**
 * Classes dedicated to providing a layer of plugin specified data on various
 * Minecraft concepts.
 *
 * @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
 * removed. It is recommended that when wanting persistent metadata, you use {@link org.bukkit.persistence.PersistentDataContainer}.
 * If you want temporary values on an entity, just use the entity lifecycle events. (See {@link com.destroystokyo.paper.event.entity.EntityAddToWorldEvent}0
 */
@Deprecated
package org.bukkit.metadata;

