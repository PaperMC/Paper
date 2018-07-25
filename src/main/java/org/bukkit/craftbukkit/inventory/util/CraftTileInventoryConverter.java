package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.server.ITileInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntityBeacon;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.TileEntityLootable;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    protected final ITileInventory tileEntity;

    public CraftTileInventoryConverter(ITileInventory tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(tileEntity);
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        if (tileEntity instanceof TileEntityLootable) {
            ((TileEntityLootable) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
        }

        return getInventory(tileEntity);
    }

    public Inventory getInventory(ITileInventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        public Furnace() {
            super(new TileEntityFurnace());
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type) {
            return getInventory(tileEntity);
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            ((TileEntityFurnace) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(ITileInventory tileEntity) {
            ((TileEntityFurnace) tileEntity).setWorld(MinecraftServer.getServer().getWorldServer(0)); // TODO: customize this if required
            return new CraftInventoryFurnace((TileEntityFurnace) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        public BrewingStand() {
            super(new TileEntityBrewingStand());
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            if (tileEntity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            }
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(ITileInventory tileEntity) {
            return new CraftInventoryBrewer(tileEntity);
        }
    }

    public static class Beacon extends CraftTileInventoryConverter {

        public Beacon() {
            super(new TileEntityBeacon());
        }

        @Override
        public Inventory getInventory(ITileInventory tileInventory) {
            return new CraftInventoryBeacon((TileEntityBeacon) tileInventory);
        }
    }
}
