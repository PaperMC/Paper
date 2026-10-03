package io.papermc.paper.persistence;

import io.papermc.paper.InternalAPIBridge;
import net.kyori.adventure.key.Key;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NullMarked;

/**
 * This class represents the union of a {@link Key} and a
 * {@link PersistentDataType}.
 * <p>
 * The advantage of this class is more direct, typed access to data stored in a {@link PersistentDataContainer}.
 * Instead of having to keep track of the {@link NamespacedKey} and {@link PersistentDataType} separately
 * when working with PDC, this class allows you to store both together.
 * <p>
 * <h2>Example usage</h2>
 * <pre>{@code
 * public class CustomLogic {
 *   public static final PersistentDataKey<Integer> TRACKED_VALUE = PersistentDataKey.of(
 *     Key.key("custom:tracked_value"),
 *     PersistentDataType.INTEGER
 *   );
 *   public static final PersistentDataKey<UUID> OWNING_PLAYER = PersistentDataKey.of(
 *     Key.key("custom:owning_player"),
 *     new UUIDTagType()
 *   );
 *
 *   // Checks if the stored PDC value for OWNING_PLAYER matches the executor UUID and adds
 *   // 1 to the TRACKED_VALUE, if it does.
 *   public static void incrementIfOwner(PersistentDataContainer pdc, UUID executor) {
 *     if (!Objects.equals(pdc.get(OWNING_PLAYER), executor) {
 *       return;
 *     }
 *
 *     pdc.set(TRACKED_VALUE, pdc.getOrDefault(TRACKED_VALUE, 0) + 1);
 *   }
 * }
 * }</pre>
 *
 * @param <C> the retrieved object type when applying this tag type
 */
@NullMarked
public interface PersistentDataKey<C> {

    /**
     * Creates a new {@link PersistentDataKey} with the given {@link Key} and {@link PersistentDataType}.
     *
     * @param key  the access key to store
     * @param type the persistent data type to reference
     * @param <C>  the retrieved object type when applying this tag type
     * @return a new {@link PersistentDataKey} of the given key and type
     * @throws IllegalArgumentException if either key or type are null
     */
    static <C> PersistentDataKey<C> of(Key key, PersistentDataType<?, C> type) {
        return InternalAPIBridge.get().createPersistentDataKey(key, type);
    }

    /**
     * {@return the access key of this {@link PersistentDataKey}}
     */
    Key getKey();

    /**
     * {@return the access key of this {@link PersistentDataKey}, as a {@link NamespacedKey}}
     */
    NamespacedKey getNamespacedKey();

    /**
     * {@return the data type of this {@link PersistentDataKey}}
     */
    PersistentDataType<?, C> getDataType();
}
