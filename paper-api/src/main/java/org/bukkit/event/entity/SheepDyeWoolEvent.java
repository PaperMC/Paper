package org.bukkit.event.entity;

import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Called when a sheep's wool is dyed
 */
public class SheepDyeWoolEvent extends io.papermc.paper.event.entity.EntityDyeEvent implements Cancellable {
    // Paper - move everything to superclass

    @Deprecated(since = "1.17.1")
    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color) {
        this(sheep, color, null);
    }

    public SheepDyeWoolEvent(@NotNull final Sheep sheep, @NotNull final DyeColor color, @Nullable Player player) {
        super(sheep, color, player); // Paper
    }

    @NotNull
    @Override
    public Sheep getEntity() {
        return (Sheep) entity;
    }
}
