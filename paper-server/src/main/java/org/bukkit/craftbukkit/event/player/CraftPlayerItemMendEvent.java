package org.bukkit.craftbukkit.event.player;

import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class CraftPlayerItemMendEvent extends CraftPlayerEvent implements PlayerItemMendEvent {

    private final ItemStack item;
    private final EquipmentSlot slot;
    private final ExperienceOrb experienceOrb;
    private final int consumedExperience;
    private int repairAmount;

    private boolean cancelled;

    public CraftPlayerItemMendEvent(final Player player, final ItemStack item, final EquipmentSlot slot, final ExperienceOrb experienceOrb, final int repairAmount, final int consumedExperience) {
        super(player);
        this.item = item;
        this.slot = slot;
        this.experienceOrb = experienceOrb;
        this.repairAmount = repairAmount;
        this.consumedExperience = consumedExperience;
    }

    @Override
    public ItemStack getItem() {
        return this.item;
    }

    @Override
    public EquipmentSlot getSlot() {
        return this.slot;
    }

    @Override
    public ExperienceOrb getExperienceOrb() {
        return this.experienceOrb;
    }

    @Override
    public int getRepairAmount() {
        return this.repairAmount;
    }

    @Override
    public void setRepairAmount(final int amount) {
        this.repairAmount = amount;
    }

    @Override
    public int getConsumedExperience() {
        return this.consumedExperience;
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
        return PlayerItemMendEvent.getHandlerList();
    }
}
