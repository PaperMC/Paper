package io.papermc.paper.brigadier.types;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public interface Position<C extends BukkitBrigadierCommandSource> {

    @NotNull Vector getPositionVector(@NotNull C source);

    float getXRot(@NotNull C source);

    float getYRot(@NotNull C source);

    @NotNull Location getLocation(@NotNull C source);

    boolean isXRelative();

    boolean isYRelative();

    boolean isZRelative();
}
