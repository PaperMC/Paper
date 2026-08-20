package io.papermc.paper.event.player;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerPickEntityEvent extends PaperPlayerPickItemEvent implements PlayerPickEntityEvent {

    private final Entity entity;

    public PaperPlayerPickEntityEvent(final Player player, final Entity entity, final ItemStack item, final boolean includeData, final int targetSlot, final int sourceSlot) {
        super(player, item, includeData, targetSlot, sourceSlot);
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }
}
