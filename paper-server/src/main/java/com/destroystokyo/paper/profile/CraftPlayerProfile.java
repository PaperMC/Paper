package com.destroystokyo.paper.profile;

import com.google.common.base.Preconditions;
import com.mojang.authlib.yggdrasil.ProfileResult;
import io.papermc.paper.configuration.GlobalConfiguration;
import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.util.StringUtil;
import net.minecraft.world.item.component.ResolvableProfile;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.craftbukkit.configuration.ConfigSerializationUtil;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.profile.CraftPlayerTextures;
import org.bukkit.craftbukkit.profile.CraftProfileProperty;
import org.bukkit.profile.PlayerTextures;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@SerializableAs("PlayerProfile")
public class CraftPlayerProfile implements PlayerProfile, SharedPlayerProfile {

    private boolean emptyName;
    private boolean emptyUUID;
    private GameProfile profile;
    private final PropertySet properties = new PropertySet();

    public CraftPlayerProfile(CraftPlayer player) {
        this.profile = player.getHandle().getGameProfile();
    }

    public CraftPlayerProfile(UUID id, String name) {
        this.profile = createAuthLibProfile(id, name);
        this.emptyName = name == null;
        this.emptyUUID = id == null;
    }

    public CraftPlayerProfile(GameProfile profile) {
        Validate.notNull(profile, "GameProfile cannot be null!");
        this.profile = profile;
    }

    public CraftPlayerProfile(ResolvableProfile resolvableProfile) {
        this(resolvableProfile.id().orElse(null), resolvableProfile.name().orElse(null));
        copyProfileProperties(resolvableProfile.gameProfile(), this.profile);
    }

    @Override
    public boolean hasProperty(String property) {
        return profile.getProperties().containsKey(property);
    }

    @Override
    public void setProperty(ProfileProperty property) {
        String name = property.getName();
        PropertyMap properties = profile.getProperties();
        properties.removeAll(name);

        Preconditions.checkArgument(properties.size() < 16, "Cannot add more than 16 properties to a profile");
        properties.put(name, new Property(name, property.getValue(), property.getSignature()));
    }

    @Override
    public CraftPlayerTextures getTextures() {
        return new CraftPlayerTextures(this);
    }

    @Override
    public void setTextures(@Nullable PlayerTextures textures) {
        if (textures == null) {
            this.removeProperty("textures");
        } else {
            CraftPlayerTextures craftPlayerTextures = new CraftPlayerTextures(this);
            craftPlayerTextures.copyFrom(textures);
            craftPlayerTextures.rebuildPropertyIfDirty();
        }
    }

    public GameProfile getGameProfile() {
        return profile;
    }

    @Nullable
    @Override
    public UUID getId() {
        return this.emptyUUID ? null : this.profile.getId();
    }

    @Override
    @Deprecated(forRemoval = true)
    public UUID setId(@Nullable UUID uuid) {
        final GameProfile previousProfile = this.profile;
        final UUID previousId = this.getId();
        this.profile = createAuthLibProfile(uuid, previousProfile.getName());
        copyProfileProperties(previousProfile, this.profile);
        this.emptyUUID = uuid == null;
        return previousId;
    }

    @Override
    public UUID getUniqueId() {
        return getId();
    }

    @Nullable
    @Override
    public String getName() {
        return this.emptyName ? null : this.profile.getName();
    }

    @Override
    @Deprecated(forRemoval = true)
    public String setName(@Nullable String name) {
        GameProfile prev = this.profile;
        this.profile = createAuthLibProfile(prev.getId(), name);
        copyProfileProperties(prev, this.profile);
        this.emptyName = name == null;
        return prev.getName();
    }

    @Nonnull
    @Override
    public Set<ProfileProperty> getProperties() {
        return properties;
    }

    @Override
    public void setProperties(Collection<ProfileProperty> properties) {
        properties.forEach(this::setProperty);
    }

    @Override
    public void clearProperties() {
        profile.getProperties().clear();
    }

    @Override
    public boolean removeProperty(String property) {
        return !profile.getProperties().removeAll(property).isEmpty();
    }

    @Nullable
    @Override
    public Property getProperty(String property) {
        return Iterables.getFirst(this.profile.getProperties().get(property), null);
    }

    @Nullable
    @Override
    public void setProperty(@NotNull String propertyName, @Nullable Property property) {
        if (property != null) {
            this.setProperty(new ProfileProperty(propertyName, property.value(), property.signature()));
        } else {
            profile.getProperties().removeAll(propertyName);
        }
    }

    @Override
    public @NotNull GameProfile buildGameProfile() {
        GameProfile profile = new GameProfile(this.profile.getId(), this.profile.getName());
        profile.getProperties().putAll(this.profile.getProperties());
        return profile;
    }

    @Override
    public @NotNull ResolvableProfile buildResolvableProfile() {
        if (this.emptyName || this.emptyUUID) {
            return new ResolvableProfile(this.emptyName ? Optional.empty() : Optional.of(this.profile.getName()), this.emptyUUID ? Optional.empty() : Optional.of(this.profile.getId()), this.profile.getProperties());
        } else {
            return new ResolvableProfile(this.buildGameProfile());
        }
    }

    @Override
    public CraftPlayerProfile clone() {
        CraftPlayerProfile clone = new CraftPlayerProfile(this.getId(), this.getName());
        clone.setProperties(getProperties());
        return clone;
    }

    @Override
    public boolean isComplete() {
        return this.getId() != null && StringUtils.isNotBlank(this.getName());
    }

    @Override
    public @NotNull CompletableFuture<PlayerProfile> update() {
        return CompletableFuture.supplyAsync(() -> {
            final CraftPlayerProfile clone = clone();
            clone.complete(true);
            return clone;
        }, Util.PROFILE_EXECUTOR);
    }

    @Override
    public boolean completeFromCache() {
        return completeFromCache(false, GlobalConfiguration.get().proxies.isProxyOnlineMode());
    }

    public boolean completeFromCache(boolean onlineMode) {
        return completeFromCache(false, onlineMode);
    }

    public boolean completeFromCache(boolean lookupUUID, boolean onlineMode) {
        MinecraftServer server = MinecraftServer.getServer();
        String name = profile.getName();
        GameProfileCache userCache = server.getProfileCache();
        if (this.getId() == null) {
            final GameProfile profile;
            if (onlineMode) {
                profile = lookupUUID ? userCache.get(name).orElse(null) : userCache.getProfileIfCached(name);
            } else {
                // Make an OfflinePlayer using an offline mode UUID since the name has no profile
                profile = new GameProfile(UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8)), name);
            }
            if (profile != null) {
                // if old has it, assume its newer, so overwrite, else use cached if it was set and ours wasn't
                copyProfileProperties(this.profile, profile);
                this.profile = profile;
                this.emptyUUID = false; // UUID was just retrieved from user cache and profile isn't null (so a completed profile was found)
            }
        }

        if ((profile.getName().isEmpty() || !hasTextures()) && this.getId() != null) {
            Optional<GameProfile> optProfile = userCache.get(this.profile.getId());
            if (optProfile.isPresent()) {
                GameProfile profile = optProfile.get();
                if (this.profile.getName().isEmpty()) {
                    // if old has it, assume its newer, so overwrite, else use cached if it was set and ours wasn't
                    copyProfileProperties(this.profile, profile);
                    this.profile = profile;
                    this.emptyName = false; // Name was just retrieved via the userCache
                } else if (profile != this.profile) {
                    copyProfileProperties(profile, this.profile);
                }
            }
        }
        return this.isComplete();
    }

    public boolean complete(boolean textures) {
        return complete(textures, GlobalConfiguration.get().proxies.isProxyOnlineMode());
    }

    public boolean complete(boolean textures, boolean onlineMode) {
        if (this.isComplete() && (!textures || hasTextures())) { // Don't do lookup if we already have everything
            return true;
        }

        MinecraftServer server = MinecraftServer.getServer();
        boolean isCompleteFromCache = this.completeFromCache(true, onlineMode);
        if (onlineMode && (!isCompleteFromCache || (textures && !hasTextures()))) {
            ProfileResult result = server.getSessionService().fetchProfile(this.profile.getId(), true);
            if (result != null && result.profile() != null) {
                copyProfileProperties(result.profile(), this.profile, true);
            }
            if (this.isComplete()) {
                server.getProfileCache().add(this.profile);
            }
        }
        return this.isComplete() && (!onlineMode || !textures || hasTextures());
    }

    private static void copyProfileProperties(GameProfile source, GameProfile target) {
        copyProfileProperties(source, target, false);
    }

    private static void copyProfileProperties(GameProfile source, GameProfile target, boolean clearTarget) {
        if (source == target) {
            throw new IllegalArgumentException("Source and target profiles are the same (" + source + ")");
        }
        PropertyMap sourceProperties = source.getProperties();
        PropertyMap targetProperties = target.getProperties();
        if (clearTarget) targetProperties.clear();
        if (sourceProperties.isEmpty()) {
            return;
        }

        for (Property property : sourceProperties.values()) {
            targetProperties.removeAll(property.name());
            targetProperties.put(property.name(), property);
        }
    }

    private static GameProfile createAuthLibProfile(UUID uniqueId, String name) {
        Preconditions.checkArgument(name == null || name.length() <= 16, "Name cannot be longer than 16 characters");
        Preconditions.checkArgument(name == null || StringUtil.isValidPlayerName(name), "The name of the profile contains invalid characters: %s", name);
        return new GameProfile(
            uniqueId != null ? uniqueId : Util.NIL_UUID,
            name != null ? name : ""
        );
    }

    private static ProfileProperty toBukkit(Property property) {
        return new ProfileProperty(property.name(), property.value(), property.signature());
    }

    public static PlayerProfile asBukkitCopy(GameProfile gameProfile) {
        CraftPlayerProfile profile = new CraftPlayerProfile(gameProfile.getId(), gameProfile.getName());
        copyProfileProperties(gameProfile, profile.profile);
        return profile;
    }

    public static PlayerProfile asBukkitMirror(GameProfile profile) {
        return new CraftPlayerProfile(profile);
    }

    public static Property asAuthlib(ProfileProperty property) {
        return new Property(property.getName(), property.getValue(), property.getSignature());
    }

    public static GameProfile asAuthlibCopy(PlayerProfile profile) {
        CraftPlayerProfile craft = ((CraftPlayerProfile) profile);
        return asAuthlib(craft.clone());
    }

    public static GameProfile asAuthlib(PlayerProfile profile) {
        CraftPlayerProfile craft = ((CraftPlayerProfile) profile);
        return craft.getGameProfile();
    }

    public static ResolvableProfile asResolvableProfileCopy(PlayerProfile profile) {
        return ((SharedPlayerProfile) profile).buildResolvableProfile();
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>();
        if (!this.emptyUUID) {
            map.put("uniqueId", this.getId().toString());
        }
        if (!this.emptyName) {
            map.put("name", getName());
        }
        if (!this.properties.isEmpty()) {
            List<Object> propertiesData = new ArrayList<>();
            for (ProfileProperty property : properties) {
                propertiesData.add(CraftProfileProperty.serialize(new Property(property.getName(), property.getValue(), property.getSignature())));
            }
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
                if (!(propertyData instanceof Map)) {
                    throw new IllegalArgumentException("Property data (" + propertyData + ") is not a valid Map");
                }
                Property property = CraftProfileProperty.deserialize((Map<?, ?>) propertyData);
                profile.profile.getProperties().put(property.name(), property);
            }
        }

        return profile;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final CraftPlayerProfile that = (CraftPlayerProfile) o;
        return this.emptyName == that.emptyName && this.emptyUUID == that.emptyUUID && Objects.equals(this.profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.emptyName, this.emptyUUID, this.profile);
    }

    @Override
    public String toString() {
        return "CraftPlayerProfile [uniqueId=" + getId() +
            ", name=" + getName() +
            ", properties=" + org.bukkit.craftbukkit.profile.CraftPlayerProfile.toString(this.profile.getProperties()) +
            "]";
    }

    private class PropertySet extends AbstractSet<ProfileProperty> {

        @Override
        @Nonnull
        public Iterator<ProfileProperty> iterator() {
            return new ProfilePropertyIterator(profile.getProperties().values().iterator());
        }

        @Override
        public int size() {
            return profile.getProperties().size();
        }

        @Override
        public boolean add(ProfileProperty property) {
            setProperty(property);
            return true;
        }

        @Override
        public boolean addAll(Collection<? extends ProfileProperty> c) {
            //noinspection unchecked
            setProperties((Collection<ProfileProperty>) c);
            return true;
        }

        @Override
        public boolean contains(Object o) {
            return o instanceof ProfileProperty && profile.getProperties().containsKey(((ProfileProperty) o).getName());
        }

        private class ProfilePropertyIterator implements Iterator<ProfileProperty> {
            private final Iterator<Property> iterator;

            ProfilePropertyIterator(Iterator<Property> iterator) {
                this.iterator = iterator;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public ProfileProperty next() {
                return toBukkit(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        }
    }
}
