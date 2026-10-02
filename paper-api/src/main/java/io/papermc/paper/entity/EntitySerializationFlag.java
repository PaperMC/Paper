package io.papermc.paper.entity;

import org.bukkit.UnsafeValues;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/// Represents flags for entity serialization.
///
/// @see UnsafeValues#serializeEntity(Entity, EntitySerializationFlag... serializationFlags)
/// @since 1.21.4
public enum EntitySerializationFlag {

    /// Serialize entities that wouldn't be serialized normally
    /// (e.g. dead, despawned, non-persistent, etc.).
    ///
    /// @see Entity#isValid()
    /// @see Entity#isPersistent()
    FORCE,
    /// Serialize misc non-saveable entities like lighting bolts, fishing bobbers, etc.
    ///
    /// Note: players require a separate flag: [#PLAYER].
    MISC,
    /// Include passengers in the serialized data.
    PASSENGERS,
    /// Allow serializing [Player]s.
    ///
    /// Note: deserializing player data will always fail.
    PLAYER

}
