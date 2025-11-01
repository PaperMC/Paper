package io.papermc.paper.event.entity;

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class PaperEntityTeleportEvent extends EntityTeleportEvent {

    @ApiStatus.Internal
    public PaperEntityTeleportEvent(final Entity entity, final Location from, @Nullable final Location to) {
        super(entity, from, to);
    }

    @Override
    public void setTo(@Nullable final Location to) {
        if (to != null) {
            Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(to), "destination Location is not in spawnable bounds");
        }
        super.setTo(to);
    }

    @Override
    public void setFrom(final Location from) {
        Preconditions.checkArgument(CraftLocation.isInSpawnableBounds(from), "origin Location is not in spawnable bounds");
        super.setFrom(from);
    }
}
