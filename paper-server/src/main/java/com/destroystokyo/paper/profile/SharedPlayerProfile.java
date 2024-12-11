package com.destroystokyo.paper.profile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface SharedPlayerProfile {

    @Nullable UUID getUniqueId();

    @Nullable String getName();

    boolean removeProperty(@NotNull String property);

    @Nullable Property getProperty(@NotNull String propertyName);

    @Nullable void setProperty(@NotNull String propertyName, @Nullable Property property);

    @NotNull GameProfile buildGameProfile();

    @NotNull ResolvableProfile buildResolvableProfile();
}
