package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.server.BlockPosition;
import net.minecraft.server.DimensionManager;
import net.minecraft.server.IInventory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.TileEntityBlastFurnace;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityDropper;
import net.minecraft.server.TileEntityFurnace;
import net.minecraft.server.TileEntityFurnaceFurnace;
import net.minecraft.server.TileEntityHopper;
import net.minecraft.server.TileEntityLectern;
import net.minecraft.server.TileEntityLootable;
import net.minecraft.server.TileEntitySmoker;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract IInventory getTileEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return getInventory(getTileEntity());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        IInventory te = getTileEntity();
        if (te instanceof TileEntityLootable) {
            ((TileEntityLootable) te).setCustomName(CraftChatMessage.fromStringOrNull(title));
        }

        return getInventory(te);
    }

    public Inventory getInventory(IInventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            TileEntityFurnace furnace = new TileEntityFurnaceFurnace();
            furnace.setLocation(MinecraftServer.getServer().getWorldServer(DimensionManager.OVERWORLD), BlockPosition.ZERO); // TODO: customize this if required
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            IInventory tileEntity = getTileEntity();
            ((TileEntityFurnace) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(IInventory tileEntity) {
            return new CraftInventoryFurnace((TileEntityFurnace) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityBrewingStand();
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            IInventory tileEntity = getTileEntity();
            if (tileEntity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) tileEntity).setCustomName(CraftChatMessage.fromStringOrNull(title));
            }
            return getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(IInventory tileEntity) {
            return new CraftInventoryBrewer(tileEntity);
        }
    }

    public static class Dispenser extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityDispenser();
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityDropper();
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityHopper();
        }
    }

    public static class BlastFurnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityBlastFurnace();
        }
    }

    public static class Lectern extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityLectern().inventory;
        }
    }

    public static class Smoker extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntitySmoker();
        }
    }
}
