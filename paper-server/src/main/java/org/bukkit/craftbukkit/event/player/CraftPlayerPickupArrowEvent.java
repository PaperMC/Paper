package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.entity.item.ItemEntity;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

public class CraftPlayerPickupArrowEvent extends CraftPlayerPickupItemEvent implements PlayerPickupArrowEvent {

    private final AbstractArrow arrow;

    public CraftPlayerPickupArrowEvent(final Player player, final Item item, final AbstractArrow arrow) {
        super(player, item, 0);
        this.arrow = arrow;
    }

    public CraftPlayerPickupArrowEvent(
        final net.minecraft.world.entity.player.Player player, final ItemEntity item, final net.minecraft.world.entity.projectile.arrow.AbstractArrow arrow
    ) {
        this((Player) player.getBukkitEntity(), (Item) item.getBukkitEntity(), (AbstractArrow) arrow.getBukkitEntity());
    }

    @Override
    public AbstractArrow getArrow() {
        return this.arrow;
    }
}
