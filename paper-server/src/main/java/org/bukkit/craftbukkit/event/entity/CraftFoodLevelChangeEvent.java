package org.bukkit.craftbukkit.event.entity;

import net.minecraft.Optionull;
import net.minecraft.world.entity.player.Player;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class CraftFoodLevelChangeEvent extends CraftEntityEvent implements FoodLevelChangeEvent {

    private int level;
    private final @Nullable ItemStack item;

    private boolean cancelled;

    public CraftFoodLevelChangeEvent(final HumanEntity human, final int level, final @Nullable ItemStack item) {
        super(human);
        this.level = level;
        this.item = item;
    }

    public CraftFoodLevelChangeEvent(final Player player, final int level, final net.minecraft.world.item.@Nullable ItemStack item) {
        this(player.getBukkitEntity(), level, Optionull.map(item, CraftItemStack::asCraftMirror));
    }

    @Override
    public HumanEntity getEntity() {
        return (HumanEntity) this.entity;
    }

    @Override
    public @Nullable ItemStack getItem() {
        return this.item == null ? null : this.item.clone();
    }

    @Override
    public int getFoodLevel() {
        return this.level;
    }

    @Override
    public void setFoodLevel(final int level) {
        this.level = Math.max(level, 0);
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
        return FoodLevelChangeEvent.getHandlerList();
    }
}
