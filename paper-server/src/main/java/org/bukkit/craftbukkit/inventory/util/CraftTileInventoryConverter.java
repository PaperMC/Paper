package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.core.BlockPosition;
import net.minecraft.world.IInventory;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.TileEntityBlastFurnace;
import net.minecraft.world.level.block.entity.TileEntityBrewingStand;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import net.minecraft.world.level.block.entity.TileEntityDropper;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import net.minecraft.world.level.block.entity.TileEntityHopper;
import net.minecraft.world.level.block.entity.TileEntityLectern;
import net.minecraft.world.level.block.entity.TileEntityLootable;
import net.minecraft.world.level.block.entity.TileEntitySmoker;
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
            ((TileEntityLootable) te).name = CraftChatMessage.fromStringOrNull(title);
        }

        return getInventory(te);
    }

    public Inventory getInventory(IInventory tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            TileEntityFurnace furnace = new TileEntityFurnaceFurnace(BlockPosition.ZERO, Blocks.FURNACE.defaultBlockState()); // TODO: customize this if required
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            IInventory tileEntity = getTileEntity();
            ((TileEntityFurnace) tileEntity).name = CraftChatMessage.fromStringOrNull(title);
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
            return new TileEntityBrewingStand(BlockPosition.ZERO, Blocks.BREWING_STAND.defaultBlockState());
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            IInventory tileEntity = getTileEntity();
            if (tileEntity instanceof TileEntityBrewingStand) {
                ((TileEntityBrewingStand) tileEntity).name = CraftChatMessage.fromStringOrNull(title);
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
            return new TileEntityDispenser(BlockPosition.ZERO, Blocks.DISPENSER.defaultBlockState());
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityDropper(BlockPosition.ZERO, Blocks.DROPPER.defaultBlockState());
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityHopper(BlockPosition.ZERO, Blocks.HOPPER.defaultBlockState());
        }
    }

    public static class BlastFurnace extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityBlastFurnace(BlockPosition.ZERO, Blocks.BLAST_FURNACE.defaultBlockState());
        }
    }

    public static class Lectern extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntityLectern(BlockPosition.ZERO, Blocks.LECTERN.defaultBlockState()).bookAccess;
        }
    }

    public static class Smoker extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new TileEntitySmoker(BlockPosition.ZERO, Blocks.SMOKER.defaultBlockState());
        }
    }

    public static class Crafter extends CraftTileInventoryConverter {

        @Override
        public IInventory getTileEntity() {
            return new CrafterBlockEntity(BlockPosition.ZERO, Blocks.CRAFTER.defaultBlockState());
        }
    }
}
