package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.destroystokyo.paper.profile.SharedPlayerProfile;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.datafixers.util.Either;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.profile.MutablePropertyMap;
import io.papermc.paper.util.MCUtil;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.minecraft.core.ClientAsset;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.PlayerSkin;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public record PaperResolvableProfile(
    net.minecraft.world.item.component.ResolvableProfile impl
) implements ResolvableProfile, Handleable<net.minecraft.world.item.component.ResolvableProfile> {

    static PaperResolvableProfile toApi(final PlayerProfile profile) {
        return new PaperResolvableProfile(((SharedPlayerProfile) profile).buildResolvableProfile());
    }

    @Override
    public net.minecraft.world.item.component.ResolvableProfile getHandle() {
        return this.impl;
    }

    @Override
    public @Nullable UUID uuid() {
        return this.impl.unpack().map(GameProfile::id, p -> p.id().orElse(null));
    }

    @Override
    public @Nullable String name() {
        return this.impl.unpack().map(GameProfile::name, p -> p.name().orElse(null));
    }

    @Override
    public @Unmodifiable Collection<ProfileProperty> properties() {
        return MCUtil.transformUnmodifiable(
            this.impl.unpack().map(GameProfile::properties, net.minecraft.world.item.component.ResolvableProfile.Partial::properties).values(),
            input -> new ProfileProperty(input.name(), input.value(), input.signature())
        );
    }

    @Override
    public boolean dynamic() {
        return this.impl instanceof net.minecraft.world.item.component.ResolvableProfile.Dynamic;
    }

    @Override
    public CompletableFuture<PlayerProfile> resolve() {
        return this.impl.resolveProfile(MinecraftServer.getServer().services().profileResolver())
            .thenApply(CraftPlayerProfile::asBukkitCopy);
    }

    @Override
    public void applySkinToPlayerHeadContents(final PlayerHeadObjectContents.@NotNull Builder builder) {
        if (this.dynamic()) {
            if (this.uuid() != null) {
                builder.id(this.uuid());
            } else {
                builder.name(this.name());
            }
            return;
        }
        builder
            .id(this.uuid())
            .name(this.name())
            .profileProperties(
                this.impl.unpack()
                    .map(GameProfile::properties, net.minecraft.world.item.component.ResolvableProfile.Partial::properties)
                    .values()
                    .stream()
                    .map(prop -> PlayerHeadObjectContents.property(prop.name(), prop.value(), prop.signature()))
                    .toList()
            )
            .texture(this.impl.skinPatch().body().map(ClientAsset.ResourceTexture::id).map(PaperAdventure::asAdventure).orElse(null));
    }

    static final class BuilderImpl implements ResolvableProfile.Builder {

        private final PropertyMap propertyMap = new MutablePropertyMap();
        private @Nullable String name;
        private @Nullable UUID uuid;

        @Override
        public ResolvableProfile.Builder name(final @Nullable String name) {
            if (name != null) {
                Preconditions.checkArgument(name.length() <= 16, "name cannot be more than 16 characters, was %s", name.length());
                Preconditions.checkArgument(StringUtil.isValidPlayerName(name), "name cannot include invalid characters, was %s", name);
            }
            this.name = name;
            return this;
        }

        @Override
        public ResolvableProfile.Builder uuid(final @Nullable UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        @Override
        public ResolvableProfile.Builder addProperty(final ProfileProperty property) {
            // ProfileProperty constructor already has specific validations
            final Property newProperty = new Property(property.getName(), property.getValue(), property.getSignature());
            if (!this.propertyMap.containsEntry(property.getName(), newProperty)) { // underlying map is a multimap that doesn't allow duplicate key-value pair
                final int newSize = this.propertyMap.size() + 1;
                Preconditions.checkArgument(newSize <= 16, "Cannot have more than 16 properties, was %s", newSize);
            }

            this.propertyMap.put(property.getName(), newProperty);
            return this;
        }

        @Override
        public ResolvableProfile.Builder addProperties(final Collection<ProfileProperty> properties) {
            properties.forEach(this::addProperty);
            return this;
        }

        @Override
        public ResolvableProfile build() {
            if (this.propertyMap.isEmpty() && (this.uuid == null) != (this.name == null)) {
                return new PaperResolvableProfile(new net.minecraft.world.item.component.ResolvableProfile.Static(
                    Either.right(new net.minecraft.world.item.component.ResolvableProfile.Partial(
                        Optional.ofNullable(this.name),
                        Optional.ofNullable(this.uuid),
                        new PropertyMap(this.propertyMap)
                    )),
                    PlayerSkin.Patch.EMPTY
                ));
            } else {
                return new PaperResolvableProfile(new net.minecraft.world.item.component.ResolvableProfile.Dynamic(
                    this.name != null ? Either.left(this.name) : Either.right(this.uuid),
                    PlayerSkin.Patch.EMPTY
                ));
            }
        }
    }
}
