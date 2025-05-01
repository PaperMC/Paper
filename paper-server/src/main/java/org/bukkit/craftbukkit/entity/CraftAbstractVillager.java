package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.trading.Merchant;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftAbstractVillager extends CraftAgeable implements CraftMerchant, AbstractVillager, InventoryHolder {

    public CraftAbstractVillager(CraftServer server, net.minecraft.world.entity.npc.AbstractVillager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.AbstractVillager getHandle() {
        return (Villager) this.entity;
    }

    @Override
    public Merchant getMerchant() {
        return this.getHandle();
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(this.getHandle().getInventory());
    }

    @Override
    public void resetOffers() {
        getHandle().resetOffers();
    }
}
