package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.common.base.Preconditions;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import io.papermc.paper.util.MCUtil;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.util.StringUtil;
import org.bukkit.craftbukkit.util.Handleable;
import org.intellij.lang.annotations.Pattern;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public record PaperResolvableProfile(
    net.minecraft.world.item.component.ResolvableProfile impl
) implements ResolvableProfile, Handleable<net.minecraft.world.item.component.ResolvableProfile> {

    static PaperResolvableProfile toApi(final PlayerProfile profile) {
        return new PaperResolvableProfile(new net.minecraft.world.item.component.ResolvableProfile(CraftPlayerProfile.asAuthlibCopy(profile)));
    }

    @Override
    public net.minecraft.world.item.component.ResolvableProfile getHandle() {
        return this.impl;
    }

    @Override
    public @Nullable UUID uuid() {
        return this.impl.id().orElse(null);
    }

    @Override
    public @Nullable String name() {
        return this.impl.name().orElse(null);
    }

    @Override
    public @Unmodifiable Collection<ProfileProperty> properties() {
        return MCUtil.transformUnmodifiable(this.impl.properties().values(), input -> new ProfileProperty(input.name(), input.value(), input.signature()));
    }

    @Override
    public CompletableFuture<PlayerProfile> resolve() {
        return this.impl.resolve().thenApply(resolvableProfile -> CraftPlayerProfile.asBukkitCopy(resolvableProfile.gameProfile()));
    }

    static final class BuilderImpl implements ResolvableProfile.Builder {

        private final PropertyMap propertyMap = new PropertyMap();
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
            final PropertyMap shallowCopy = new PropertyMap();
            shallowCopy.putAll(this.propertyMap);

            return new PaperResolvableProfile(new net.minecraft.world.item.component.ResolvableProfile(
                Optional.ofNullable(this.name),
                Optional.ofNullable(this.uuid),
                shallowCopy
            ));
        }
    }
}
