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

public abstract class CraftBlockInventoryConverter implements CraftInventoryCreator.InventoryConverter {

    public abstract Container getBlockEntity();

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type) {
        return this.getInventory(holder, type, this.getBlockEntity()); // Paper
    }

    @Override
    public Inventory createInventory(InventoryHolder owner, InventoryType type, net.kyori.adventure.text.Component title) {
        Container blockEntity = getBlockEntity();
        if (blockEntity instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) blockEntity).name = io.papermc.paper.adventure.PaperAdventure.asVanilla(title);
        }

        return this.getInventory(owner, type, blockEntity); // Paper
    }

    @Override
    public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
        Container blockEntity = this.getBlockEntity();
        if (blockEntity instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) blockEntity).name = CraftChatMessage.fromStringOrNull(title);
        }

        return this.getInventory(holder, type, blockEntity); // Paper
    }

    @Deprecated // Paper - use getInventory with owner and type
    public Inventory getInventory(Container blockEntity) {
        return this.getInventory(null, null, blockEntity);
    }

    public Inventory getInventory(InventoryHolder owner, InventoryType type, Container blockEntity) { // Paper
        if (owner != null) return new org.bukkit.craftbukkit.inventory.CraftInventoryCustom(owner, type, blockEntity); // Paper
        return new CraftInventory(blockEntity);
    }

    public static class Furnace extends AbstractFurnaceInventoryConverter { // Paper - Furnace, BlastFurnace, and Smoker are pretty much identical

        @Override
        public Container getBlockEntity() {
            AbstractFurnaceBlockEntity furnace = new FurnaceBlockEntity(BlockPos.ZERO, Blocks.FURNACE.defaultBlockState()); // TODO: customize this if required
            return furnace;
        }

    // Paper start - abstract furnace converter to apply to all 3 furnaces
    }

    public static abstract class AbstractFurnaceInventoryConverter extends CraftBlockInventoryConverter {
    // Paper end - abstract furnace converter to apply to all 3 furnaces

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, net.kyori.adventure.text.Component title) {
            Container blockEntity = getBlockEntity();
            ((AbstractFurnaceBlockEntity) blockEntity).name = io.papermc.paper.adventure.PaperAdventure.asVanilla(title);
            return this.getInventory(owner, type, blockEntity); // Paper
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, String title) {
            Container blockEntity = this.getBlockEntity();
            ((AbstractFurnaceBlockEntity) blockEntity).name = CraftChatMessage.fromStringOrNull(title);
            return this.getInventory(owner, type, blockEntity); // Paper
        }

        @Override
        public Inventory getInventory(Container blockEntity) {
            return this.getInventory(null, null, blockEntity);
        }

        @Override
        public Inventory getInventory(InventoryHolder owner, InventoryType type, net.minecraft.world.Container blockEntity) { // Paper
            if (owner != null) return new org.bukkit.craftbukkit.inventory.CraftInventoryCustom(owner, type, blockEntity); // Paper
            return new CraftInventoryFurnace((AbstractFurnaceBlockEntity) blockEntity);
        }
    }

    public static class BrewingStand extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new BrewingStandBlockEntity(BlockPos.ZERO, Blocks.BREWING_STAND.defaultBlockState());
        }

        @Override
        public Inventory createInventory(InventoryHolder owner, InventoryType type, net.kyori.adventure.text.Component title) {
            // BrewingStand does not extend RandomizableContainerBlockEntity
            Container blockEntity = getBlockEntity();
            if (blockEntity instanceof BrewingStandBlockEntity) {
                ((BrewingStandBlockEntity) blockEntity).name = io.papermc.paper.adventure.PaperAdventure.asVanilla(title);
            }
            return this.getInventory(owner, type, blockEntity); // Paper
        }

        @Override
        public Inventory createInventory(InventoryHolder holder, InventoryType type, String title) {
            // BrewingStand does not extend RandomizableContainerBlockEntity
            Container blockEntity = this.getBlockEntity();
            if (blockEntity instanceof BrewingStandBlockEntity) {
                ((BrewingStandBlockEntity) blockEntity).name = CraftChatMessage.fromStringOrNull(title);
            }
            return this.getInventory(holder, type, blockEntity); // Paper
        }

        @Override
        public Inventory getInventory(Container blockEntity) {
            return this.getInventory(null, null, blockEntity);
        }

        @Override
        public Inventory getInventory(InventoryHolder owner, InventoryType type, net.minecraft.world.Container blockEntity) { // Paper
            if (owner != null) return new org.bukkit.craftbukkit.inventory.CraftInventoryCustom(owner, type, blockEntity); // Paper
            return new CraftInventoryBrewer(blockEntity);
        }
    }

    public static class Dispenser extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new DispenserBlockEntity(BlockPos.ZERO, Blocks.DISPENSER.defaultBlockState());
        }
    }

    public static class Dropper extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new DropperBlockEntity(BlockPos.ZERO, Blocks.DROPPER.defaultBlockState());
        }
    }

    public static class Hopper extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new HopperBlockEntity(BlockPos.ZERO, Blocks.HOPPER.defaultBlockState());
        }
    }

    public static class BlastFurnace extends AbstractFurnaceInventoryConverter { // Paper - Furnace, BlastFurnace, and Smoker are pretty much identical

        @Override
        public Container getBlockEntity() {
            return new BlastFurnaceBlockEntity(BlockPos.ZERO, Blocks.BLAST_FURNACE.defaultBlockState());
        }
    }

    public static class Lectern extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new LecternBlockEntity(BlockPos.ZERO, Blocks.LECTERN.defaultBlockState()).bookAccess;
        }
    }

    public static class Smoker extends AbstractFurnaceInventoryConverter { // Paper - Furnace, BlastFurnace, and Smoker are pretty much identical

        @Override
        public Container getBlockEntity() {
            return new SmokerBlockEntity(BlockPos.ZERO, Blocks.SMOKER.defaultBlockState());
        }
    }

    public static class Crafter extends CraftBlockInventoryConverter {

        @Override
        public Container getBlockEntity() {
            return new CrafterBlockEntity(BlockPos.ZERO, Blocks.CRAFTER.defaultBlockState());
        }
    }
}
