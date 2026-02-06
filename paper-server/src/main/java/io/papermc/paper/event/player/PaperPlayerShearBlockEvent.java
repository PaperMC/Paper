package io.papermc.paper.event.player;

import io.papermc.paper.event.block.PlayerShearBlockEvent;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.event.player.CraftPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PaperPlayerShearBlockEvent extends CraftPlayerEvent implements PlayerShearBlockEvent {

    private final Block block;
    private final ItemStack item;
    private final EquipmentSlot hand;
    private final List<ItemStack> drops;

    private boolean cancelled;

    public PaperPlayerShearBlockEvent(final Player player, final Block block, final ItemStack item, final EquipmentSlot hand, final List<ItemStack> drops) {
        super(player);
        this.block = block;
        this.item = item;
        this.hand = hand;
        this.drops = drops;
    }

    @Override
    public Block getBlock() {
        return this.block;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public EquipmentSlot getHand() {
        return this.hand;
    }

    @Override
    public List<ItemStack> getDrops() {
        return this.drops;
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
        return PlayerShearBlockEvent.getHandlerList();
    }
}
