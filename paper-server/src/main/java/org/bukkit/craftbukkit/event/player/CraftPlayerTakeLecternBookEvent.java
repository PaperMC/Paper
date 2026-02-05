package org.bukkit.craftbukkit.event.player;

import net.minecraft.world.level.block.entity.LecternBlockEntity;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerTakeLecternBookEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftPlayerTakeLecternBookEvent extends CraftPlayerEvent implements PlayerTakeLecternBookEvent {

    private final Lectern lectern;
    private boolean cancelled;

    public CraftPlayerTakeLecternBookEvent(final Player player, final Lectern lectern) {
        super(player);
        this.lectern = lectern;
    }

    @Override
    public Lectern getLectern() {
        return this.lectern;
    }

    @Override
    public @Nullable ItemStack getBook() {
        return this.lectern.getInventory().getItem(LecternBlockEntity.SLOT_BOOK);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(final boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return PlayerTakeLecternBookEvent.getHandlerList();
    }
}
