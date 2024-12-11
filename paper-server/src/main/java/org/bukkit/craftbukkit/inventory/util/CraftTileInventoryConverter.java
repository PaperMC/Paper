package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryBrewer;
import org.bukkit.craftbukkit.inventory.CraftInventoryFurnace;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class CraftTileInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract Container getTileEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return this.getInventory(this.getTileEntity());
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        Container te = this.getTileEntity();
        if (te instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) te).name = CraftChatMessage.fromStringOrNull(title);
        }

        return this.getInventory(te);
    }

    public Inventory getInventory(Container tileEntity) {
        return new CraftInventory(tileEntity);
    }

    public static class Furnace extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            AbstractFurnaceBlockEntity furnace = new FurnaceBlockEntity(BlockPos.ZERO, Blocks.FURNACE.defaultBlockState()); // TODO: customize this if required
            return furnace;
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            Container tileEntity = this.getTileEntity();
            ((AbstractFurnaceBlockEntity) tileEntity).name = CraftChatMessage.fromStringOrNull(title);
            return this.getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(Container tileEntity) {
            return new CraftInventoryFurnace((AbstractFurnaceBlockEntity) tileEntity);
        }
    }

    public static class BrewingStand extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new BrewingStandBlockEntity(BlockPos.ZERO, Blocks.BREWING_STAND.defaultBlockState());
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend TileEntityLootable
            Container tileEntity = this.getTileEntity();
            if (tileEntity instanceof BrewingStandBlockEntity) {
                ((BrewingStandBlockEntity) tileEntity).name = CraftChatMessage.fromStringOrNull(title);
            }
            return this.getInventory(tileEntity);
        }

        @Override
        public Inventory getInventory(Container tileEntity) {
            return new CraftInventoryBrewer(tileEntity);
        }
    }

    public static class Dispenser extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new DispenserBlockEntity(BlockPos.ZERO, Blocks.DISPENSER.defaultBlockState());
        }
    }

    public static class Dropper extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new DropperBlockEntity(BlockPos.ZERO, Blocks.DROPPER.defaultBlockState());
        }
    }

    public static class Hopper extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new HopperBlockEntity(BlockPos.ZERO, Blocks.HOPPER.defaultBlockState());
        }
    }

    public static class BlastFurnace extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new BlastFurnaceBlockEntity(BlockPos.ZERO, Blocks.BLAST_FURNACE.defaultBlockState());
        }
    }

    public static class Lectern extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new LecternBlockEntity(BlockPos.ZERO, Blocks.LECTERN.defaultBlockState()).bookAccess;
        }
    }

    public static class Smoker extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new SmokerBlockEntity(BlockPos.ZERO, Blocks.SMOKER.defaultBlockState());
        }
    }

    public static class Crafter extends CraftTileInventoryConverter {

        @Override
        public Container getTileEntity() {
            return new CrafterBlockEntity(BlockPos.ZERO, Blocks.CRAFTER.defaultBlockState());
        }
    }
}
