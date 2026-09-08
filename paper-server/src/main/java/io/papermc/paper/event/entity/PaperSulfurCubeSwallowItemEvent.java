package io.papermc.paper.event.entity;

import org.bukkit.craftbukkit.event.entity.CraftEntityEvent;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.entity.SulfurCube;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PaperSulfurCubeSwallowItemEvent extends CraftEntityEvent implements SulfurCubeSwallowItemEvent {

    private final Player player;
    private final ItemStack oldItem;
    private ItemStack newItem;

    private boolean cancelled;

    public PaperSulfurCubeSwallowItemEvent(final SulfurCube sulfurCube, final Player player, final ItemStack oldItem, final ItemStack newItem) {
        super(sulfurCube);
        this.player = player;
        this.oldItem = oldItem;
        this.newItem = newItem;
    }

    public PaperSulfurCubeSwallowItemEvent(
        final net.minecraft.world.entity.monster.cubemob.SulfurCube sulfurCube,
        final net.minecraft.world.entity.player.Player player,
        final net.minecraft.world.item.ItemStack oldItem,
        final net.minecraft.world.item.ItemStack newItem
    ) {
        this(
            (SulfurCube) sulfurCube.getBukkitEntity(),
            (Player) player.getBukkitEntity(),
            CraftItemStack.asCraftMirror(oldItem),
            CraftItemStack.asCraftMirror(newItem)
        );
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public ItemStack getOldItem() {
        return this.oldItem.clone();
    }

    @Override
    public ItemStack getNewItem() {
        return this.newItem.clone();
    }

    public ItemStack getNewItemNoClone() {
        return this.newItem;
    }

    @Override
    public void setNewItem(final ItemStack newItem) {
        this.newItem = newItem;
    }

    @Override
    public SulfurCube getEntity() {
        return (SulfurCube) this.entity;
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
        return SulfurCubeSwallowItemEvent.getHandlerList();
    }
}
