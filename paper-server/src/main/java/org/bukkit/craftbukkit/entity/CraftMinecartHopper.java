package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.vehicle.MinecartHopper;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.inventory.Inventory;
import org.jspecify.annotations.Nullable;
import java.util.Optional;

public class CraftMinecartHopper extends CraftMinecartContainer implements HopperMinecart, com.destroystokyo.paper.loottable.PaperLootableEntityInventory { // Paper

    private final CraftInventory inventory;

    public CraftMinecartHopper(CraftServer server, MinecartHopper entity) {
        super(server, entity);
        this.inventory = new CraftInventory(entity);
    }

    @Override
    public net.minecraft.world.entity.vehicle.MinecartHopper getHandle() {
        return (net.minecraft.world.entity.vehicle.MinecartHopper) this.entity;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean isEnabled() {
        return this.getHandle().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.getHandle().setEnabled(enabled);
    }

    @Override
    public int getPickupCooldown() {
        throw new UnsupportedOperationException("Hopper minecarts don't have cooldowns");
    }

    @Override
    public void setPickupCooldown(int cooldown) {
        throw new UnsupportedOperationException("Hopper minecarts don't have cooldowns");
    }

    // Paper start - Allow you to set the number of items that a hopper moves
    @Override
    public void setTransferAmount(@Nullable final Integer transferAmount) {
        if (transferAmount != null) {
            com.google.common.base.Preconditions.checkArgument(transferAmount > 0, "Hopper transfer amount cannot be less than 1");
        }
        this.getHandle().setTransferAmount(transferAmount);
    }

    @Override
    public Optional<Integer> getTransferAmount() {
        return this.getHandle().getTransferAmount();
    }
    // Paper end - Allow you to set the number of items that a hopper moves

}
