package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.event.entity.PaperEntityDyeEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import org.bukkit.entity.Sheep;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.jspecify.annotations.Nullable;

public class CraftSheepDyeWoolEvent extends PaperEntityDyeEvent implements SheepDyeWoolEvent {

    public CraftSheepDyeWoolEvent(final net.minecraft.world.entity.animal.sheep.Sheep sheep, final DyeColor color, final @Nullable Player player) {
        super(sheep, color, player);
    }

    @Override
    public Sheep getEntity() {
        return (Sheep) this.entity;
    }
}
