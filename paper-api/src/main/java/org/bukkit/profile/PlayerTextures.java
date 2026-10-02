package org.bukkit.profile;

import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// Provides access to the textures stored inside a [PlayerProfile].
///
/// Modifying the textures immediately invalidates and clears any previously
/// present attributes that are specific to official player profiles, such as the
/// [`timestamp`][#getTimestamp()] and [`signature`][#isSigned()].
public interface PlayerTextures {

    /// The different Minecraft skin models.
    enum SkinModel {
        /// The classic Minecraft skin model.
        CLASSIC,
        /// The slim model has slimmer arms than the classic model.
        SLIM;
    }

    /// Checks if the profile stores no textures.
    ///
    /// @return `true` if the profile stores no textures
    boolean isEmpty();

    /// Clears the textures.
    void clear();

    /// Gets the URL that points to the player's skin.
    ///
    /// @return the URL of the player's skin, or `null` if not set
    @Nullable
    URL getSkin();

    /// Sets the player's skin to the specified URL, and the skin model to
    /// [SkinModel#CLASSIC].
    ///
    /// The URL **must** point to the Minecraft texture server. Example URL:
    ///
    /// <pre>
    /// http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac
    /// </pre>
    ///
    /// @param skinUrl the URL of the player's skin, or `null` to
    /// unset it
    void setSkin(@Nullable URL skinUrl);

    /// Sets the player's skin and [SkinModel].
    ///
    /// The URL **must** point to the Minecraft texture server. Example URL:
    ///
    /// <pre>
    /// http://textures.minecraft.net/texture/b3fbd454b599df593f57101bfca34e67d292a8861213d2202bb575da7fd091ac
    /// </pre>
    ///
    /// A skin model of `null` results in [SkinModel#CLASSIC] to
    /// be used.
    ///
    /// @param skinUrl the URL of the player's skin, or `null` to
    /// unset it
    /// @param skinModel the skin model, ignored if the skin URL is
    /// `null`
    void setSkin(@Nullable URL skinUrl, @Nullable SkinModel skinModel);

    /// Gets the model of the player's skin.
    ///
    /// This returns [SkinModel#CLASSIC] if no skin is set.
    ///
    /// @return the model of the player's skin
    @NotNull
    SkinModel getSkinModel();

    /// Gets the URL that points to the player's cape.
    ///
    /// @return the URL of the player's cape, or `null` if not set
    @Nullable
    URL getCape();

    /// Sets the URL that points to the player's cape.
    ///
    /// The URL **must** point to the Minecraft texture server. Example URL:
    ///
    /// <pre>
    /// http://textures.minecraft.net/texture/2340c0e03dd24a11b15a8b33c2a7e9e32abb2051b2481d0ba7defd635ca7a933
    /// </pre>
    ///
    /// @param capeUrl the URL of the player's cape, or `null` to
    /// unset it
    void setCape(@Nullable URL capeUrl);

    /// Gets the timestamp at which the profile was last updated.
    ///
    /// @return the timestamp, or `0` if unknown
    long getTimestamp();

    /// Checks if the textures are signed and the signature is valid.
    ///
    /// @return `true` if the textures are signed and the signature is
    /// valid
    boolean isSigned();
}
