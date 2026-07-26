package org.bukkit.event.entity;

import io.papermc.paper.event.entity.EntityDyeEvent;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a sheep's wool is dyed
 */
public class SheepDyeWoolEvent extends EntityDyeEvent {

    @ApiStatus.Internal
    @Deprecated(since = "1.17.1", forRemoval = true)
    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color) {
        this(sheep, color, null);
    }

    @ApiStatus.Internal
    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color, @Nullable Player player) {
        super(sheep, color, player);
    }

    @NotNull
    @Override
    public Sheep getEntity() {
        return (Sheep) this.entity;
    }
}
