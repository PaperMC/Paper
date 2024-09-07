package org.bukkit.craftbukkit.entity;

import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.item.trading.IMerchant;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftAbstractVillager extends CraftAgeable implements CraftMerchant, AbstractVillager, InventoryHolder {

    public CraftAbstractVillager(CraftServer server, EntityVillagerAbstract entity) {
        super(server, entity);
    }

    @Override
    public EntityVillagerAbstract getHandle() {
        return (EntityVillager) entity;
    }

    @Override
    public IMerchant getMerchant() {
        return getHandle();
    }

    @Override
    public String toString() {
        return "CraftAbstractVillager";
    }

    @Override
    public Inventory getInventory() {
        return new CraftInventory(getHandle().getInventory());
    }
}
