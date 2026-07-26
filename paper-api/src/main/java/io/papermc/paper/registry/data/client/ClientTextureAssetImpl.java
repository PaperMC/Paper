package io.papermc.paper.registry.data.client;

import net.kyori.adventure.key.Key;
import org.intellij.lang.annotations.Subst;
import org.jspecify.annotations.NullMarked;

/**
 * Package local implementation of the {@link ClientTextureAsset} type.
 * Chosen over bridging into server internals as no internal types are required for this.
 */
@NullMarked
record ClientTextureAssetImpl(
    Key identifier,
    Key texturePath
) implements ClientTextureAsset {

    /**
     * Constructs the default asset path from the identifier of the asset.
     * Mirrors internal logic in net.minecraft.core.ClientAsset
     *
     * @param identifier the identifier of the asset.
     * @return the key/path of the asset.
     */
    static Key pathFromIdentifier(@Subst("") final Key identifier) {
        return Key.key(
            identifier.namespace(),
            "textures/" + identifier.value() + ".png"
        );
    }

}
