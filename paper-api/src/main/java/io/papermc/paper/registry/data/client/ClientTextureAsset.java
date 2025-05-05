package io.papermc.paper.registry.data.client;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.key.KeyPattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

/**
 * A data-centric version-specific representation of a client texture asset, composed of an identifier and a path.
 * Follows the same, version-specific, compatibility guarantees as the RegistryEntry types it is used in.
 */
@ApiStatus.Experimental
@ApiStatus.NonExtendable
public interface ClientTextureAsset {

    /**
     * The identifier of the client texture asset, uniquely identifying the asset on the client.
     *
     * @return the identifier.
     */
    Key identifier();

    /**
     * The path of the texture on the client, split into a namespace and a path/key.
     *
     * @return the texture path.
     */
    Key texturePath();

    /**
     * Creates a new {@link ClientTextureAsset} with the specified identifier and texture path.
     *
     * @param identifier  the unique identifier for the client texture asset.
     * @param texturePath the path where the asset is located on the client.
     * @return a new {@code ClientAsset} instance.
     */
    @Contract("_,_ -> new")
    static ClientTextureAsset clientTextureAsset(final Key identifier, final Key texturePath) {
        return new ClientTextureAssetImpl(identifier, texturePath);
    }

    /**
     * Creates a new {@link ClientTextureAsset} using the provided identifier and infers the texture path from it.
     *
     * @param identifier the unique identifier for the client texture asset.
     * @return a new {@code ClientAsset} instance.
     */
    @Contract("_ -> new")
    static ClientTextureAsset clientTextureAsset(final Key identifier) {
        return new ClientTextureAssetImpl(identifier, ClientTextureAssetImpl.pathFromIdentifier(identifier));
    }

    /**
     * Creates a new {@link ClientTextureAsset} from the provided string-formatted {@link Key}
     * and infers the texture path from it.
     * <p>
     * The identifier string must conform to the {@link KeyPattern} format.
     *
     * @param identifier the string representation of the asset's identifier.
     * @return a new {@code ClientAsset} instance.
     */
    @Contract("_ -> new")
    static ClientTextureAsset clientTextureAsset(final @KeyPattern String identifier) {
        return clientTextureAsset(Key.key(identifier));
    }
}
