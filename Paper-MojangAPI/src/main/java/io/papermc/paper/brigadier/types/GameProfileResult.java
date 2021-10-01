package io.papermc.paper.brigadier.types;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface GameProfileResult<C extends BukkitBrigadierCommandSource> {

    @NotNull Collection<PlayerProfile> getProfiles(@NotNull C source) throws CommandSyntaxException;
}

