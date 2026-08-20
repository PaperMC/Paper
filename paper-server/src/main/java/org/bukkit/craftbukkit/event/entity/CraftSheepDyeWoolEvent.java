package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.event.entity.PaperEntityDyeEvent;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.jspecify.annotations.Nullable;

public class CraftSheepDyeWoolEvent extends PaperEntityDyeEvent implements SheepDyeWoolEvent {

    public CraftSheepDyeWoolEvent(final Sheep sheep, final DyeColor color, final @Nullable Player player) {
        super(sheep, color, player);
    }

    @Override
    public Sheep getEntity() {
        return (Sheep) this.entity;
    }
}
