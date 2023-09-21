package org.bukkit.craftbukkit.profile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.SystemUtils;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.level.block.entity.TileEntitySkull;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.configuration.ConfigSerializationUtil;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

@SerializableAs("PlayerProfile")
public final class CraftPlayerProfile implements PlayerProfile {

    @Nonnull
    public static GameProfile validateSkullProfile(@Nonnull GameProfile gameProfile) {
        // The GameProfile needs to contain either both a uuid and textures, or a name.
        // The GameProfile always has a name or a uuid, so checking if it has a name is sufficient.
        boolean isValidSkullProfile = (gameProfile.getName() != null)
                || gameProfile.getProperties().containsKey(CraftPlayerTextures.PROPERTY_NAME);
        Preconditions.checkArgument(isValidSkullProfile, "The skull profile is missing a name or textures!");
        return gameProfile;
    }

    @Nullable
    public static Property getProperty(@Nonnull GameProfile profile, String propertyName) {
        return Iterables.getFirst(profile.getProperties().get(propertyName), null);
    }

    private final UUID uniqueId;
    private final String name;

    private final PropertyMap properties = new PropertyMap();
    private final CraftPlayerTextures textures = new CraftPlayerTextures(this);

    public CraftPlayerProfile(UUID uniqueId, String name) {
        Preconditions.checkArgument((uniqueId != null) || !StringUtils.isBlank(name), "uniqueId is null or name is blank");
        this.uniqueId = (uniqueId == null) ? SystemUtils.NIL_UUID : uniqueId;
        this.name = (name == null) ? "" : name;
    }

    // The Map of properties of the given GameProfile is not immutable. This captures a snapshot of the properties of
    // the given GameProfile at the time this CraftPlayerProfile is created.
    public CraftPlayerProfile(@Nonnull GameProfile gameProfile) {
        this(gameProfile.getId(), gameProfile.getName());
        properties.putAll(gameProfile.getProperties());
    }

    private CraftPlayerProfile(@Nonnull CraftPlayerProfile other) {
        this(other.uniqueId, other.name);
        this.properties.putAll(other.properties);
        this.textures.copyFrom(other.textures);
    }

    @Override
    public UUID getUniqueId() {
        return (uniqueId.equals(SystemUtils.NIL_UUID)) ? null : uniqueId;
    }

    @Override
    public String getName() {
        return (name.isEmpty()) ? null : name;
    }

    @Nullable
    Property getProperty(String propertyName) {
        return Iterables.getFirst(properties.get(propertyName), null);
    }

    void setProperty(String propertyName, @Nullable Property property) {
        // Assert: (property == null) || property.getName().equals(propertyName)
        removeProperty(propertyName);
        if (property != null) {
            properties.put(property.name(), property);
        }
    }

    void removeProperty(String propertyName) {
        properties.removeAll(propertyName);
    }

    void rebuildDirtyProperties() {
        textures.rebuildPropertyIfDirty();
    }

    @Override
    public CraftPlayerTextures getTextures() {
        return textures;
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
        return (getUniqueId() != null) && (getName() != null) && !textures.isEmpty();
    }

    @Override
    public CompletableFuture<PlayerProfile> update() {
        return CompletableFuture.supplyAsync(this::getUpdatedProfile, SystemUtils.backgroundExecutor());
    }

    private CraftPlayerProfile getUpdatedProfile() {
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile profile = this.buildGameProfile();

        // If missing, look up the uuid by name:
        if (profile.getId().equals(SystemUtils.NIL_UUID)) {
            profile = server.getProfileCache().get(profile.getName()).orElse(profile);
        }

        // Look up properties such as the textures:
        if (!profile.getId().equals(SystemUtils.NIL_UUID)) {
            GameProfile newProfile;
            try {
                newProfile = TileEntitySkull.fillProfileTextures(profile).get().orElse(null); // TODO: replace with CompletableFuture
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException("Exception filling profile textures", ex);
            }
            if (newProfile != null) {
                profile = newProfile;
            }
        }

        return new CraftPlayerProfile(profile);
    }

    // This always returns a new GameProfile instance to ensure that property changes to the original or previously
    // built GameProfiles don't affect the use of this profile in other contexts.
    @Nonnull
    public GameProfile buildGameProfile() {
        rebuildDirtyProperties();
        GameProfile profile = new GameProfile(uniqueId, name);
        profile.getProperties().putAll(properties);
        return profile;
    }

    @Override
    public String toString() {
        rebuildDirtyProperties();
        StringBuilder builder = new StringBuilder();
        builder.append("CraftPlayerProfile [uniqueId=");
        builder.append(uniqueId);
        builder.append(", name=");
        builder.append(name);
        builder.append(", properties=");
        builder.append(toString(properties));
        builder.append("]");
        return builder.toString();
    }

    private static String toString(@Nonnull PropertyMap propertyMap) {
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
        if (!(obj instanceof CraftPlayerProfile)) return false;
        CraftPlayerProfile other = (CraftPlayerProfile) obj;
        if (!Objects.equals(uniqueId, other.uniqueId)) return false;
        if (!Objects.equals(name, other.name)) return false;

        rebuildDirtyProperties();
        other.rebuildDirtyProperties();
        if (!equals(properties, other.properties)) return false;
        return true;
    }

    private static boolean equals(@Nonnull PropertyMap propertyMap, @Nonnull PropertyMap other) {
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
        rebuildDirtyProperties();
        int result = 1;
        result = 31 * result + Objects.hashCode(uniqueId);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + hashCode(properties);
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
        if (getUniqueId() != null) {
            map.put("uniqueId", getUniqueId().toString());
        }
        if (getName() != null) {
            map.put("name", getName());
        }
        rebuildDirtyProperties();
        if (!properties.isEmpty()) {
            List<Object> propertiesData = new ArrayList<>();
            properties.forEach((propertyName, property) -> {
                propertiesData.add(CraftProfileProperty.serialize(property));
            });
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
                Preconditions.checkArgument(propertyData instanceof Map, "Propertu data (%s) is not a valid Map", propertyData);
                Property property = CraftProfileProperty.deserialize((Map<?, ?>) propertyData);
                profile.properties.put(property.name(), property);
            }
        }

        return profile;
    }
}
