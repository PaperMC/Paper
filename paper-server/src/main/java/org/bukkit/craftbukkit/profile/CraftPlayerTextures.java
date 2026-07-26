package org.bukkit.craftbukkit.profile;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Locale;
import java.util.Objects;
import org.bukkit.craftbukkit.util.JsonHelper;
import org.bukkit.profile.PlayerTextures;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CraftPlayerTextures implements PlayerTextures {

    static final String PROPERTY_NAME = "textures";
    private static final String MINECRAFT_HOST = "textures.minecraft.net";
    private static final String MINECRAFT_PATH = "/texture/";

    private static void validateTextureUrl(@Nullable URL url) {
        // Null represents an unset texture and is therefore valid.
        if (url == null) return;

        Preconditions.checkArgument(url.getHost().equals(CraftPlayerTextures.MINECRAFT_HOST), "Expected host '%s' but got '%s'", CraftPlayerTextures.MINECRAFT_HOST, url.getHost());
        Preconditions.checkArgument(url.getPath().startsWith(CraftPlayerTextures.MINECRAFT_PATH), "Expected path starting with '%s' but got '%s", CraftPlayerTextures.MINECRAFT_PATH, url.getPath());
    }

    private static @Nullable URL parseUrl(@Nullable String urlString) {
        if (urlString == null) return null;
        try {
            return URI.create(urlString).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            return null;
        }
    }

    private static @Nullable SkinModel parseSkinModel(@Nullable String skinModelName) {
        if (skinModelName == null) return null;
        try {
            return SkinModel.valueOf(skinModelName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private final com.destroystokyo.paper.profile.SharedPlayerProfile profile; // Paper

    // The textures data is loaded lazily:
    private boolean loaded = false;
    private @Nullable JsonObject data; // Immutable contents (only read)
    private long timestamp;

    // Lazily decoded textures data that can subsequently be overwritten:
    private @Nullable URL skin;
    private SkinModel skinModel = SkinModel.CLASSIC;
    private @Nullable URL cape;

    // Dirty: Indicates a change that requires a rebuild of the property.
    // This also indicates an invalidation of any previously present textures data that is specific to official
    // GameProfiles, such as the property signature, timestamp, profileId, playerName, etc.: Any modifications by
    // plugins that affect the textures property immediately invalidate all attributes that are specific to official
    // GameProfiles (even if these modifications are later reverted).
    private boolean dirty = false;

    public CraftPlayerTextures(com.destroystokyo.paper.profile.SharedPlayerProfile profile) { // Paper
        this.profile = profile;
    }

    public void copyFrom(PlayerTextures other) {
        if (other == this) return;
        Preconditions.checkArgument(other instanceof CraftPlayerTextures, "Expecting CraftPlayerTextures, got %s", other.getClass().getName());
        CraftPlayerTextures otherTextures = (CraftPlayerTextures) other;
        this.clear();
        Property texturesProperty = otherTextures.getProperty();
        this.profile.setProperty(CraftPlayerTextures.PROPERTY_NAME, texturesProperty);
        if (texturesProperty != null
            && (!Objects.equals(this.profile.getUniqueId(), otherTextures.profile.getUniqueId())
                || !Objects.equals(this.profile.getName(), otherTextures.profile.getName()))) {
            // We might need to rebuild the textures property for this profile:
            // TODO Only rebuild if the textures property actually stores an incompatible profileId/playerName?
            this.ensureLoaded();
            this.markDirty();
            this.rebuildPropertyIfDirty();
        }
    }

    private void ensureLoaded() {
        if (this.loaded) return;
        this.loaded = true;

        Property property = this.getProperty();
        if (property == null) return;

        this.data = CraftProfileProperty.decodePropertyValue(property.value());
        if (this.data != null) {
            JsonObject texturesMap = JsonHelper.getObjectOrNull(this.data, "textures");
            this.loadSkin(texturesMap);
            this.loadCape(texturesMap);
            this.loadTimestamp();
        }
    }

    private void loadSkin(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        JsonObject texture = JsonHelper.getObjectOrNull(texturesMap, MinecraftProfileTexture.Type.SKIN.name());
        if (texture == null) return;

        String skinUrlString = JsonHelper.getStringOrNull(texture, "url");
        this.skin = CraftPlayerTextures.parseUrl(skinUrlString);
        this.skinModel = CraftPlayerTextures.loadSkinModel(texture);

        // Special case: If a skin is present, but no skin model, we use the default classic skin model.
        if (this.skinModel == null && this.skin != null) {
            this.skinModel = SkinModel.CLASSIC;
        }
    }

    private static @Nullable SkinModel loadSkinModel(@Nullable JsonObject texture) {
        if (texture == null) return null;
        JsonObject metadata = JsonHelper.getObjectOrNull(texture, "metadata");
        if (metadata == null) return null;

        String skinModelName = JsonHelper.getStringOrNull(metadata, "model");
        return CraftPlayerTextures.parseSkinModel(skinModelName);
    }

    private void loadCape(@Nullable JsonObject texturesMap) {
        if (texturesMap == null) return;
        JsonObject texture = JsonHelper.getObjectOrNull(texturesMap, MinecraftProfileTexture.Type.CAPE.name());
        if (texture == null) return;

        String skinUrlString = JsonHelper.getStringOrNull(texture, "url");
        this.cape = CraftPlayerTextures.parseUrl(skinUrlString);
    }

    private void loadTimestamp() {
        if (this.data == null) return;
        JsonPrimitive timestamp = JsonHelper.getPrimitiveOrNull(this.data, "timestamp");
        if (timestamp == null) return;

        try {
            this.timestamp = timestamp.getAsLong();
        } catch (NumberFormatException ignored) {
        }
    }

    private void markDirty() {
        this.dirty = true;

        // Clear any cached but no longer valid data:
        this.data = null;
        this.timestamp = 0L;
    }

    @Override
    public boolean isEmpty() {
        this.ensureLoaded();
        return (this.skin == null) && (this.cape == null);
    }

    @Override
    public void clear() {
        this.profile.removeProperty(CraftPlayerTextures.PROPERTY_NAME);
        this.loaded = false;
        this.data = null;
        this.timestamp = 0L;
        this.skin = null;
        this.skinModel = SkinModel.CLASSIC;
        this.cape = null;
        this.dirty = false;
    }

    @Override
    public @Nullable URL getSkin() {
        this.ensureLoaded();
        return this.skin;
    }

    @Override
    public void setSkin(@Nullable URL skinUrl) {
        this.setSkin(skinUrl, SkinModel.CLASSIC);
    }

    @Override
    public void setSkin(@Nullable URL skinUrl, @Nullable SkinModel skinModel) {
        CraftPlayerTextures.validateTextureUrl(skinUrl);
        if (skinModel == null) skinModel = SkinModel.CLASSIC;
        // This also loads the textures if necessary:
        if (Objects.equals(this.getSkin(), skinUrl) && Objects.equals(this.getSkinModel(), skinModel)) return;
        this.skin = skinUrl;
        this.skinModel = (skinUrl != null) ? skinModel : SkinModel.CLASSIC;
        this.markDirty();
    }

    @Override
    public SkinModel getSkinModel() {
        this.ensureLoaded();
        return this.skinModel;
    }

    @Override
    public @Nullable URL getCape() {
        this.ensureLoaded();
        return this.cape;
    }

    @Override
    public void setCape(@Nullable URL capeUrl) {
        CraftPlayerTextures.validateTextureUrl(capeUrl);
        // This also loads the textures if necessary:
        if (Objects.equals(this.getCape(), capeUrl)) return;
        this.cape = capeUrl;
        this.markDirty();
    }

    @Override
    public long getTimestamp() {
        this.ensureLoaded();
        return this.timestamp;
    }

    @Override
    public boolean isSigned() {
        if (this.dirty) return false;
        Property property = this.getProperty();
        return property != null && CraftProfileProperty.hasValidSignature(property);
    }

    @Nullable
    Property getProperty() {
        this.rebuildPropertyIfDirty();
        return this.profile.getProperty(CraftPlayerTextures.PROPERTY_NAME);
    }

    public void rebuildPropertyIfDirty() {
        if (!this.dirty) return;
        // Assert: loaded
        this.dirty = false;

        if (this.isEmpty()) {
            this.profile.removeProperty(CraftPlayerTextures.PROPERTY_NAME);
            return;
        }

        // This produces a new textures property that does not contain any attributes that are specific to official
        // GameProfiles (such as the property signature, timestamp, profileId, playerName, etc.).
        // Information on the format of the textures property:
        // * https://minecraft.wiki/w/Head#Item_data
        // * https://minecraft.wiki/w/Mojang_API#Query_player's_skin_and_cape
        // The order of Json object elements is important.
        JsonObject propertyData = new JsonObject();

        if (this.skin != null) {
            JsonObject texturesMap = JsonHelper.getOrCreateObject(propertyData, "textures");
            JsonObject skinTexture = JsonHelper.getOrCreateObject(texturesMap, MinecraftProfileTexture.Type.SKIN.name());
            skinTexture.addProperty("url", this.skin.toExternalForm());

            // Special case: If the skin model is classic (i.e. default), omit it.
            // Assert: skinModel != null
            if (this.skinModel != SkinModel.CLASSIC) {
                JsonObject metadata = JsonHelper.getOrCreateObject(skinTexture, "metadata");
                metadata.addProperty("model", this.skinModel.name().toLowerCase(Locale.ROOT));
            }
        }

        if (this.cape != null) {
            JsonObject texturesMap = JsonHelper.getOrCreateObject(propertyData, "textures");
            JsonObject skinTexture = JsonHelper.getOrCreateObject(texturesMap, MinecraftProfileTexture.Type.CAPE.name());
            skinTexture.addProperty("url", this.cape.toExternalForm());
        }

        this.data = propertyData;

        // We use the compact formatter here since this is more likely to match the output of existing popular tools
        // that also create profiles with custom textures:
        String encodedTexturesData = CraftProfileProperty.encodePropertyValue(propertyData, CraftProfileProperty.JsonFormatter.COMPACT);
        Property property = new Property(CraftPlayerTextures.PROPERTY_NAME, encodedTexturesData);
        this.profile.setProperty(CraftPlayerTextures.PROPERTY_NAME, property);
    }

    private @Nullable JsonObject getData() {
        this.ensureLoaded();
        this.rebuildPropertyIfDirty();
        return this.data;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CraftPlayerTextures [data=");
        builder.append(this.getData());
        builder.append("]");
        return builder.toString();
    }

    @Override
    public int hashCode() {
        Property property = this.getProperty();
        return (property == null) ? 0 : CraftProfileProperty.hashCode(property);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CraftPlayerTextures)) return false;
        CraftPlayerTextures other = (CraftPlayerTextures) obj;
        Property property = this.getProperty();
        Property otherProperty = other.getProperty();
        return CraftProfileProperty.equals(property, otherProperty);
    }
}
