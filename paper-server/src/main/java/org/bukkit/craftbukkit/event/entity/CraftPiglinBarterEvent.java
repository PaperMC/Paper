package org.bukkit.craftbukkit.event.entity;

import io.papermc.paper.util.MCUtil;
import java.util.List;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Piglin;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.PiglinBarterEvent;
import org.bukkit.inventory.ItemStack;

public class CraftPiglinBarterEvent extends CraftEntityEvent implements PiglinBarterEvent {

    private final List<ItemStack> outcome;
    private final ItemStack input;

    private boolean cancelled;

    public CraftPiglinBarterEvent(final Piglin piglin, final ItemStack input, final List<ItemStack> outcome) {
        super(piglin);
        this.input = input;
        this.outcome = outcome;
    }

    public CraftPiglinBarterEvent(final net.minecraft.world.entity.monster.piglin.Piglin piglin, final net.minecraft.world.item.ItemStack input, final List<net.minecraft.world.item.ItemStack> outcome) {
        this(
            (Piglin) piglin.getBukkitEntity(),
            CraftItemStack.asCraftMirror(input),
            MCUtil.mutableTransform(outcome, CraftItemStack::asCraftMirror, CraftItemStack::asNMSCopy)
        );
    }

    @Override
    public Piglin getEntity() {
        return (Piglin) this.entity;
    }

    @Override
    public ItemStack getInput() {
        return this.input.clone();
    }

    @Override
    public List<ItemStack> getOutcome() {
        return this.outcome;
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
        return PiglinBarterEvent.getHandlerList();
    }
}
