package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.server.DimensionManager;
import net.minecraft.server.ITileInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntityBeacon;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityDropper;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.TileEntityHopper;
import net.minecraft.server.TileEntityLootable;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBeacon;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract ITileInventory getTileEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(getTileEntity());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        ITileInventory te = getTileEntity();
        if (te instanceof TileEntityLootable) {
            ((TileEntityLootable) te).setCustomName(CraftChatMessage.fromStringOrNull(title));
        }

        return getInventory(te);
    }

    public Inventory getInventory(ITileInventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public ITileInventory getTileEntity() {
            TileEntityFurnace furnace = new TileEntityFurnace();
            furnace.setWorld(MinecraftServer.getServer().getWorldServer(DimensionManager.OVERWORLD)); // TODO: customize this if required
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            ITileInventory tileEntity = getTileEntity();
            ((TileEntityFurnace) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(ITileInventory tileEntity) {
            return new CraftInventoryFurnace((TileEntityFurnace) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        @Override
        public ITileInventory getTileEntity() {
            return new TileEntityBrewingStand();
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            ITileInventory tileEntity = getTileEntity();
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

        @Override
        public ITileInventory getTileEntity() {
            return new TileEntityBeacon();
        }

        @Override
        public Inventory getInventory(ITileInventory tileInventory) {
            return new CraftInventoryBeacon((TileEntityBeacon) tileInventory);
        }
    }

    public static class Dispenser extends CraftTileInventoryConverter {

        @Override
        public ITileInventory getTileEntity() {
            return new TileEntityDispenser();
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public ITileInventory getTileEntity() {
            return new TileEntityDropper();
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public ITileInventory getTileEntity() {
            return new TileEntityHopper();
        }
    }
}
