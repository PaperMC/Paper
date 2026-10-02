/// Classes dedicated to providing a layer of plugin specified data on various
/// Minecraft concepts.
///
/// @deprecated This system is extremely misleading and does not cleanup values for metadatable entities that have been
/// removed. It is recommended that when wanting persistent metadata, you use [org.bukkit.persistence.PersistentDataContainer].
///
/// If you want temporary values on an entity, use the entity lifecycle events and a [java.util.Map] of your own. (See [com.destroystokyo.paper.event.entity.EntityAddToWorldEvent] and [com.destroystokyo.paper.event.entity.EntityRemoveFromWorldEvent])
@Deprecated
package org.bukkit.metadata;

