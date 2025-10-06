package org.bukkit.craftbukkit.profile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.yggdrasil.ProfileResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import com.mojang.datafixers.util.Either;
import io.papermc.paper.profile.MutablePropertyMap;
import net.minecraft.Util;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.component.ResolvableProfile;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.configuration.ConfigSerializationUtil;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SerializableAs("PlayerProfile")
public final class CraftPlayerProfile implements PlayerProfile, com.destroystokyo.paper.profile.SharedPlayerProfile, com.destroystokyo.paper.profile.PlayerProfile { // Paper

    public static GameProfile validateSkullProfile(GameProfile gameProfile) {
        // The GameProfile needs to contain either both a uuid and textures, or a name.
        // The GameProfile always has a name or a uuid, so checking if it has a name is sufficient.
        boolean isValidSkullProfile = (gameProfile.name() != null)
                || gameProfile.properties().containsKey(CraftPlayerTextures.PROPERTY_NAME);
        Preconditions.checkArgument(isValidSkullProfile, "The skull profile is missing a name or textures!");
        Preconditions.checkArgument(gameProfile.name().length() <= 16, "The name of the profile is longer than 16 characters");
        Preconditions.checkArgument(net.minecraft.util.StringUtil.isValidPlayerName(gameProfile.name()), "The name of the profile contains invalid characters: %s", gameProfile.name());
        final PropertyMap properties = gameProfile.properties();
        Preconditions.checkArgument(properties.size() <= 16, "The profile contains more than 16 properties");
        for (final Property property : properties.values()) {
            Preconditions.checkArgument(property.name().length() <= 64, "The name of a property is longer than 64 characters");
            Preconditions.checkArgument(property.value().length() <= Short.MAX_VALUE, "The value of a property is longer than 32767 characters");
            Preconditions.checkArgument(property.signature() == null || property.signature().length() <= 1024, "The signature of a property is longer than 1024 characters");
        }
        return gameProfile;
    }

    public static @Nullable Property getProperty(GameProfile profile, String propertyName) {
        return Iterables.getFirst(profile.properties().get(propertyName), null);
    }

    private final @Nullable UUID uniqueId;
    private final @Nullable String name;

    private final PropertyMap properties = new MutablePropertyMap();
    private final CraftPlayerTextures textures = new CraftPlayerTextures(this);

    private CraftPlayerProfile(@Nullable UUID uniqueId, @Nullable String name, boolean applyPreconditions) {
        if (applyPreconditions) {
            Preconditions.checkArgument((uniqueId != null) || !StringUtils.isBlank(name), "uniqueId is null or name is blank");
        }
        Preconditions.checkArgument(name == null || name.length() <= 16, "The name of the profile is longer than 16 characters"); // Paper - Validate
        Preconditions.checkArgument(name == null || net.minecraft.util.StringUtil.isValidPlayerName(name), "The name of the profile contains invalid characters: %s", name); // Paper - Validate
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public CraftPlayerProfile(@Nullable UUID uniqueId, @Nullable String name) {
        this(uniqueId, name, true);
    }

    // The ResolvableProfile used in Components can have just the properties then need ignore all checks internally
    @ApiStatus.Internal
    public CraftPlayerProfile(ResolvableProfile resolvableProfile) {
        this(
            resolvableProfile.unpack().map(GameProfile::id, p -> p.id().orElse(null)),
            resolvableProfile.unpack().map(GameProfile::name, p -> p.name().orElse(null)),
            false
        );
        this.properties.putAll(resolvableProfile.partialProfile().properties());
    }

    // The Map of properties of the given GameProfile is not immutable. This captures a snapshot of the properties of
    // the given GameProfile at the time this CraftPlayerProfile is created.
    public CraftPlayerProfile(GameProfile gameProfile) {
        this(gameProfile.id(), gameProfile.name());
        this.properties.putAll(gameProfile.properties());
    }

    private CraftPlayerProfile(CraftPlayerProfile other) {
        this(other.uniqueId, other.name);
        this.properties.putAll(other.properties);
        this.textures.copyFrom(other.textures);
    }

    @Override
    public @Nullable UUID getUniqueId() {
        return Objects.equals(this.uniqueId, Util.NIL_UUID) ? null : this.uniqueId;
    }

    @Override
    public @Nullable String getName() {
        return StringUtils.isBlank(this.name) ? null : this.name;
    }

    public @Nullable Property getProperty(String propertyName) {
        return Iterables.getFirst(this.properties.get(propertyName), null);
    }

    public void setProperty(String propertyName, @Nullable Property property) {
        // Assert: (property == null) || property.getName().equals(propertyName)
        this.removeProperty(propertyName);
        if (property != null) {
            Preconditions.checkArgument(this.properties.size() < 16, "The profile contains more than 16 properties"); // Paper - Validate
            this.properties.put(property.name(), property);
        }
    }

    @Override
    public boolean removeProperty(String propertyName) {
        return !this.properties.removeAll(propertyName).isEmpty();
    }

    void rebuildDirtyProperties() {
        this.textures.rebuildPropertyIfDirty();
    }

    @Override
    public CraftPlayerTextures getTextures() {
        return this.textures;
    }

    @Override
    public void setTextures(@Nullable PlayerTextures textures) {
        if (textures == null) {
            this.textures.clear();
        } else {
            this.textures.copyFrom(textures);
        }
    }

    @Override
    public boolean isComplete() {
        return (this.getUniqueId() != null) && (this.getName() != null) && !this.textures.isEmpty();
    }

    @Override
    public CompletableFuture update() { // Paper - have to remove generic to avoid clashing between bukkit.PlayerProfile and paper.PlayerProfile
        return CompletableFuture.supplyAsync(this::getUpdatedProfile, Util.nonCriticalIoPool()); // Paper - don't submit BLOCKING PROFILE LOOKUPS to the world gen thread
    }

    private CraftPlayerProfile getUpdatedProfile() {
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile profile = this.buildGameProfile();

        // If missing, look up the uuid by name:
        if (profile.id().equals(Util.NIL_UUID)) {
            profile = server.services().nameToIdCache().get(profile.name())
                .map(NameAndId::toUncompletedGameProfile)
                .orElse(profile);
        }

        // Look up properties such as the textures:
        if (!profile.id().equals(Util.NIL_UUID)) {
            ProfileResult newProfile = server.services().sessionService().fetchProfile(profile.id(), true);
            if (newProfile != null) {
                profile = newProfile.profile();
            }
        }

        return new CraftPlayerProfile(profile);
    }

    // This always returns a new GameProfile instance to ensure that property changes to the original or previously
    // built ResolvableProfile don't affect the use of this profile in other contexts.
    @Override
    public ResolvableProfile buildResolvableProfile() {
        this.rebuildDirtyProperties();
        return this.properties.isEmpty() && (name == null) != (uniqueId == null) // Heuristic copied from net.minecraft.world.item.component.ResolvableProfile.create
            ? new ResolvableProfile.Dynamic(name == null ? Either.right(uniqueId) : Either.left(name), PlayerSkin.Patch.EMPTY)
            : ResolvableProfile.createResolved(this.buildGameProfile());
    }

    // This always returns a new GameProfile instance to ensure that property changes to the original or previously
    // built GameProfiles don't affect the use of this profile in other contexts.
    @Override
    public GameProfile buildGameProfile() {
        this.rebuildDirtyProperties();
        return new GameProfile(
            this.uniqueId != null ? this.uniqueId : Util.NIL_UUID,
            this.name != null ? this.name : "",
            new PropertyMap(this.properties)
        );
    }

    @Override
    public String toString() {
        this.rebuildDirtyProperties();
        StringBuilder builder = new StringBuilder();
        builder.append("CraftPlayerProfile [uniqueId=");
        builder.append(this.getUniqueId());
        builder.append(", name=");
        builder.append(this.getName());
        builder.append(", properties=");
        builder.append(CraftPlayerProfile.toString(this.properties));
        builder.append("]");
        return builder.toString();
    }

    public static String toString(PropertyMap propertyMap) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        propertyMap.asMap().forEach((propertyName, properties) -> {
            builder.append(propertyName);
            builder.append("=");
            builder.append(properties.stream().map(CraftProfileProperty::toString).collect(Collectors.joining(",", "[", "]")));
        });
        builder.append("}");
        return builder.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof CraftPlayerProfile other)) return false;
        if (!Objects.equals(this.uniqueId, other.uniqueId)) return false;
        if (!Objects.equals(this.name, other.name)) return false;

        this.rebuildDirtyProperties();
        other.rebuildDirtyProperties();
        if (!CraftPlayerProfile.equals(this.properties, other.properties)) return false;
        return true;
    }

    private static boolean equals(PropertyMap propertyMap, PropertyMap other) {
        if (propertyMap.size() != other.size()) return false;
        // We take the order of properties into account here, because it is
        // also relevant in the serialized and NBT forms of GameProfiles.
        Iterator<Property> iterator1 = propertyMap.values().iterator();
        Iterator<Property> iterator2 = other.values().iterator();
        while (iterator1.hasNext()) {
            if (!iterator2.hasNext()) return false;
            Property property1 = iterator1.next();
            Property property2 = iterator2.next();
            if (!CraftProfileProperty.equals(property1, property2)) {
                return false;
            }
        }
        return !iterator2.hasNext();
    }

    @Override
    public int hashCode() {
        this.rebuildDirtyProperties();
        int result = 1;
        result = 31 * result + Objects.hashCode(this.uniqueId);
        result = 31 * result + Objects.hashCode(this.name);
        result = 31 * result + CraftPlayerProfile.hashCode(this.properties);
        return result;
    }

    private static int hashCode(PropertyMap propertyMap) {
        int result = 1;
        for (Property property : propertyMap.values()) {
            result = 31 * result + CraftProfileProperty.hashCode(property);
        }
        return result;
    }

    @Override
    public CraftPlayerProfile clone() {
        return new CraftPlayerProfile(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (this.uniqueId != null) {
            map.put("uniqueId", this.uniqueId.toString());
        }
        if (this.name != null) {
            map.put("name", this.getName());
        }
        this.rebuildDirtyProperties();
        if (!this.properties.isEmpty()) {
            List<Object> propertiesData = new ArrayList<>();
            this.properties.forEach((propertyName, property) -> propertiesData.add(CraftProfileProperty.serialize(property)));
            map.put("properties", propertiesData);
        }
        return map;
    }

    public static CraftPlayerProfile deserialize(Map<String, Object> map) {
        UUID uniqueId = ConfigSerializationUtil.getUuid(map, "uniqueId", true);
        String name = ConfigSerializationUtil.getString(map, "name", true);

        // This also validates the deserialized unique id and name (ensures that not both are null):
        CraftPlayerProfile profile = new CraftPlayerProfile(uniqueId, name);

        if (map.containsKey("properties")) {
            for (Object propertyData : (List<?>) map.get("properties")) {
                Preconditions.checkArgument(propertyData instanceof Map, "Property data (%s) is not a valid Map", propertyData);
                Property property = CraftProfileProperty.deserialize((Map<?, ?>) propertyData);
                profile.properties.put(property.name(), property);
            }
        }
        return profile;
    }

    // Paper start - This must implement our PlayerProfile so generic casts succeed from cb.CraftPlayerProfile to paper.PlayerProfile
    // The methods don't actually have to be implemented, because the profile should immediately be cast to SharedPlayerProfile
    @Override
    public String setName(final String name) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public UUID getId() {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public UUID setId(final UUID uuid) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public Set<com.destroystokyo.paper.profile.ProfileProperty> getProperties() {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean hasProperty(final String property) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public void setProperty(final com.destroystokyo.paper.profile.ProfileProperty property) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public void setProperties(final java.util.Collection<com.destroystokyo.paper.profile.ProfileProperty> properties) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public void clearProperties() {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean completeFromCache() {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean completeFromCache(final boolean onlineMode) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean completeFromCache(final boolean lookupUUID, final boolean onlineMode) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean complete(final boolean textures) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }

    @Override
    public boolean complete(final boolean textures, final boolean onlineMode) {
        throw new UnsupportedOperationException("Do not cast to com.destroystokyo.paper.profile.PlayerProfile");
    }
}
