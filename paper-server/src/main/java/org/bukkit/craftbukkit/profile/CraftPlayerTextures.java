package org.bukkit.craftbukkit.profile;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.bukkit.craftbukkit.util.JsonHelper;
import org.bukkit.profile.PlayerTextures;

final class CraftPlayerTextures implements PlayerTextures {

    static final String PROPERTY_NAME = "textures";
    private static final String MINECRAFT_HOST = "textures.minecraft.net";
    private static final String MINECRAFT_PATH = "/texture/";

    private static void validateTextureUrl(@Nullable URL url) {
        // Null represents an unset texture and is therefore valid.
        if (url == null) return;

        Preconditions.checkArgument(url.getHost().equals(MINECRAFT_HOST), "Expected host '%s' but got '%s'", MINECRAFT_HOST, url.getHost());
        Preconditions.checkArgument(url.getPath().startsWith(MINECRAFT_PATH), "Expected path starting with '%s' but got '%s", MINECRAFT_PATH, url.getPath());
    }

    @Nullable
    private static URL parseUrl(@Nullable String urlString) {
        if (urlString == null) return null;
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    @Nullable
    private static SkinModel parseSkinModel(@Nullable String skinModelName) {
        if (skinModelName == null) return null;
        try {
            return SkinModel.valueOf(skinModelName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private final CraftPlayerProfile profile;

    // The textures data is loaded lazily:
    private boolean loaded = false;
    private JsonObject data; // Immutable contents (only read)
    private long timestamp;

    // Lazily decoded textures data that can subsequently be overwritten:
    private URL skin;
    private SkinModel skinModel = SkinModel.CLASSIC;
    private URL cape;

    // Dirty: Indicates a change that requires a rebuild of the property.
    // This also indicates an invalidation of any previously present textures data that is specific to official
    // GameProfiles, such as the property signature, timestamp, profileId, playerName, etc.: Any modifications by
    // plugins that affect the textures property immediately invalidate all attributes that are specific to official
    // GameProfiles (even if these modifications are later reverted).
    private boolean dirty = false;

    CraftPlayerTextures(@Nonnull CraftPlayerProfile profile) {
        this.profile = profile;
    }

    void copyFrom(@Nonnull PlayerTextures other) {
        if (other == this) return;
        Preconditions.checkArgument(other instanceof CraftPlayerTextures, "Expecting CraftPlayerTextures, got %s", other.getClass().getName());
        CraftPlayerTextures otherTextures = (CraftPlayerTextures) other;
        clear();
        Property texturesProperty = otherTextures.getProperty();
        profile.setProperty(PROPERTY_NAME, texturesProperty);
        if (texturesProperty != null
            && (!Objects.equals(profile.getUniqueId(), otherTextures.profile.getUniqueId())
                || !Objects.equals(profile.getName(), otherTextures.profile.getName()))) {
            // We might need to rebuild the textures property for this profile:
            // TODO Only rebuild if the textures property actually stores an incompatible profileId/playerName?
            ensureLoaded();
            markDirty();
            rebuildPropertyIfDirty();
        }
    }

    private void ensureLoaded() {
        if (loaded) return;
        loaded = true;

        Property property = getProperty();
        if (property == null) return;

        data = CraftProfileProperty.decodePropertyValue(property.value());
        if (data != null) {
            JsonObject texturesMap = JsonHelper.getObjectOrNull(data, "textures");
            loadSkin(texturesMap);
            loadCape(texturesMap);
            loadTimestamp();
        }
    }

    private void loadSkin(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        JsonObject texture = JsonHelper.getObjectOrNull(texturesMap, MinecraftProfileTexture.Type.SKIN.name());
        if (texture == null) return;

        String skinUrlString = JsonHelper.getStringOrNull(texture, "url");
        this.skin = parseUrl(skinUrlString);
        this.skinModel = loadSkinModel(texture);

        // Special case: If a skin is present, but no skin model, we use the default classic skin model.
        if (skinModel == null && skin != null) {
            skinModel = SkinModel.CLASSIC;
        }
    }

    @Nullable
    private static SkinModel loadSkinModel(@Nullable JsonObject texture) {
        if (texture == null) return null;
        JsonObject metadata = JsonHelper.getObjectOrNull(texture, "metadata");
        if (metadata == null) return null;

        String skinModelName = JsonHelper.getStringOrNull(metadata, "model");
        return parseSkinModel(skinModelName);
    }

    private void loadCape(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        JsonObject texture = JsonHelper.getObjectOrNull(texturesMap, MinecraftProfileTexture.Type.CAPE.name());
        if (texture == null) return;

        String skinUrlString = JsonHelper.getStringOrNull(texture, "url");
        this.cape = parseUrl(skinUrlString);
    }

    private void loadTimestamp() {
        if (data == null) return;
        JsonPrimitive timestamp = JsonHelper.getPrimitiveOrNull(data, "timestamp");
        if (timestamp == null) return;

        try {
            this.timestamp = timestamp.getAsLong();
        } catch (NumberFormatException e) {
        }
    }

    private void markDirty() {
        dirty = true;

        // Clear any cached but no longer valid data:
        data = null;
        timestamp = 0L;
    }

    @Override
    public boolean isEmpty() {
        ensureLoaded();
        return (skin == null) && (cape == null);
    }

    @Override
    public void clear() {
        profile.removeProperty(PROPERTY_NAME);
        loaded = false;
        data = null;
        timestamp = 0L;
        skin = null;
        skinModel = SkinModel.CLASSIC;
        cape = null;
        dirty = false;
    }

    @Override
    public URL getSkin() {
        ensureLoaded();
        return skin;
    }

    @Override
    public void setSkin(URL skinUrl) {
        setSkin(skinUrl, SkinModel.CLASSIC);
    }

    @Override
    public void setSkin(URL skinUrl, SkinModel skinModel) {
        validateTextureUrl(skinUrl);
        if (skinModel == null) skinModel = SkinModel.CLASSIC;
        // This also loads the textures if necessary:
        if (Objects.equals(getSkin(), skinUrl) && Objects.equals(getSkinModel(), skinModel)) return;
        this.skin = skinUrl;
        this.skinModel = (skinUrl != null) ? skinModel : SkinModel.CLASSIC;
        markDirty();
    }

    @Override
    public SkinModel getSkinModel() {
        ensureLoaded();
        return skinModel;
    }

    @Override
    public URL getCape() {
        ensureLoaded();
        return cape;
    }

    @Override
    public void setCape(URL capeUrl) {
        validateTextureUrl(capeUrl);
        // This also loads the textures if necessary:
        if (Objects.equals(getCape(), capeUrl)) return;
        this.cape = capeUrl;
        markDirty();
    }

    @Override
    public long getTimestamp() {
        ensureLoaded();
        return timestamp;
    }

    @Override
    public boolean isSigned() {
        if (dirty) return false;
        Property property = getProperty();
        return property != null && CraftProfileProperty.hasValidSignature(property);
    }

    @Nullable
    Property getProperty() {
        rebuildPropertyIfDirty();
        return profile.getProperty(PROPERTY_NAME);
    }

    void rebuildPropertyIfDirty() {
        if (!dirty) return;
        // Assert: loaded
        dirty = false;

        if (isEmpty()) {
            profile.removeProperty(PROPERTY_NAME);
            return;
        }

        // This produces a new textures property that does not contain any attributes that are specific to official
        // GameProfiles (such as the property signature, timestamp, profileId, playerName, etc.).
        // Information on the format of the textures property:
        // * https://minecraft.wiki/w/Head#Item_data
        // * https://wiki.vg/Mojang_API#UUID_to_Profile_and_Skin.2FCape
        // The order of Json object elements is important.
        JsonObject propertyData = new JsonObject();

        if (skin != null) {
            JsonObject texturesMap = JsonHelper.getOrCreateObject(propertyData, "textures");
            JsonObject skinTexture = JsonHelper.getOrCreateObject(texturesMap, MinecraftProfileTexture.Type.SKIN.name());
            skinTexture.addProperty("url", skin.toExternalForm());

            // Special case: If the skin model is classic (i.e. default), omit it.
            // Assert: skinModel != null
            if (skinModel != SkinModel.CLASSIC) {
                JsonObject metadata = JsonHelper.getOrCreateObject(skinTexture, "metadata");
                metadata.addProperty("model", skinModel.name().toLowerCase(Locale.ROOT));
            }
        }

        if (cape != null) {
            JsonObject texturesMap = JsonHelper.getOrCreateObject(propertyData, "textures");
            JsonObject skinTexture = JsonHelper.getOrCreateObject(texturesMap, MinecraftProfileTexture.Type.CAPE.name());
            skinTexture.addProperty("url", cape.toExternalForm());
        }

        this.data = propertyData;

        // We use the compact formatter here since this is more likely to match the output of existing popular tools
        // that also create profiles with custom textures:
        String encodedTexturesData = CraftProfileProperty.encodePropertyValue(propertyData, CraftProfileProperty.JsonFormatter.COMPACT);
        Property property = new Property(PROPERTY_NAME, encodedTexturesData);
        profile.setProperty(PROPERTY_NAME, property);
    }

    private JsonObject getData() {
        ensureLoaded();
        rebuildPropertyIfDirty();
        return data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CraftPlayerTextures [data=");
        builder.append(getData());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        Property property = getProperty();
        return (property == null) ? 0 : CraftProfileProperty.hashCode(property);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CraftPlayerTextures)) return false;
        CraftPlayerTextures other = (CraftPlayerTextures) obj;
        Property property = getProperty();
        Property otherProperty = other.getProperty();
        return CraftProfileProperty.equals(property, otherProperty);
    }
}
