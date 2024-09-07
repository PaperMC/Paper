package org.bukkit.craftbukkit.inventory.util;

import static org.bukkit.craftbukkit.inventory.util.CraftMenuBuilder.*;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.world.TileInventory;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.ContainerCartography;
import net.minecraft.world.inventory.ContainerEnchantTable;
import net.minecraft.world.inventory.ContainerGrindstone;
import net.minecraft.world.inventory.ContainerSmithing;
import net.minecraft.world.inventory.ContainerStonecutter;
import net.minecraft.world.inventory.ContainerWorkbench;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.TileEntityBeacon;
import net.minecraft.world.level.block.entity.TileEntityBlastFurnace;
import net.minecraft.world.level.block.entity.TileEntityBrewingStand;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import net.minecraft.world.level.block.entity.TileEntityFurnaceFurnace;
import net.minecraft.world.level.block.entity.TileEntityHopper;
import net.minecraft.world.level.block.entity.TileEntityLectern;
import net.minecraft.world.level.block.entity.TileEntitySmoker;
import org.bukkit.craftbukkit.inventory.CraftMenuType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;
import org.bukkit.inventory.view.BeaconView;
import org.bukkit.inventory.view.BrewingStandView;
import org.bukkit.inventory.view.CrafterView;
import org.bukkit.inventory.view.EnchantmentView;
import org.bukkit.inventory.view.FurnaceView;
import org.bukkit.inventory.view.LecternView;
import org.bukkit.inventory.view.LoomView;
import org.bukkit.inventory.view.MerchantView;
import org.bukkit.inventory.view.StonecutterView;

public final class CraftMenus {

    public record MenuTypeData<V extends InventoryView>(Class<V> viewClass, CraftMenuBuilder menuBuilder) {
    }

    private static final CraftMenuBuilder STANDARD = (player, menuType) -> menuType.create(player.nextContainerCounter(), player.getInventory());

    public static <V extends InventoryView> MenuTypeData<V> getMenuTypeData(CraftMenuType<?> menuType) {
        // this isn't ideal as both dispenser and dropper are 3x3, InventoryType can't currently handle generic 3x3s with size 9
        // this needs to be removed when inventory creation is overhauled
        if (menuType == MenuType.GENERIC_3X3) {
            return asType(new MenuTypeData<>(InventoryView.class, tileEntity(TileEntityDispenser::new, Blocks.DISPENSER)));
        }
        if (menuType == MenuType.CRAFTER_3X3) {
            return asType(new MenuTypeData<>(CrafterView.class, tileEntity(CrafterBlockEntity::new, Blocks.CRAFTER)));
        }
        if (menuType == MenuType.ANVIL) {
            return asType(new MenuTypeData<>(AnvilView.class, worldAccess(ContainerAnvil::new)));
        }
        if (menuType == MenuType.BEACON) {
            return asType(new MenuTypeData<>(BeaconView.class, tileEntity(TileEntityBeacon::new, Blocks.BEACON)));
        }
        if (menuType == MenuType.BLAST_FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, tileEntity(TileEntityBlastFurnace::new, Blocks.BLAST_FURNACE)));
        }
        if (menuType == MenuType.BREWING_STAND) {
            return asType(new MenuTypeData<>(BrewingStandView.class, tileEntity(TileEntityBrewingStand::new, Blocks.BREWING_STAND)));
        }
        if (menuType == MenuType.CRAFTING) {
            return asType(new MenuTypeData<>(InventoryView.class, worldAccess(ContainerWorkbench::new)));
        }
        if (menuType == MenuType.ENCHANTMENT) {
            return asType(new MenuTypeData<>(EnchantmentView.class, (player, type) -> {
                return new TileInventory((syncId, inventory, human) -> {
                    return worldAccess(ContainerEnchantTable::new).build(player, type);
                }, IChatBaseComponent.empty()).createMenu(player.nextContainerCounter(), player.getInventory(), player);
            }));
        }
        if (menuType == MenuType.FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, tileEntity(TileEntityFurnaceFurnace::new, Blocks.FURNACE)));
        }
        if (menuType == MenuType.GRINDSTONE) {
            return asType(new MenuTypeData<>(InventoryView.class, worldAccess(ContainerGrindstone::new)));
        }
        // We really don't need to be creating a tile entity for hopper but currently InventoryType doesn't have capacity
        // to understand otherwise
        if (menuType == MenuType.HOPPER) {
            return asType(new MenuTypeData<>(InventoryView.class, tileEntity(TileEntityHopper::new, Blocks.HOPPER)));
        }
        // We also don't need to create a tile entity for lectern, but again InventoryType isn't smart enough to know any better
        if (menuType == MenuType.LECTERN) {
            return asType(new MenuTypeData<>(LecternView.class, tileEntity(TileEntityLectern::new, Blocks.LECTERN)));
        }
        if (menuType == MenuType.LOOM) {
            return asType(new MenuTypeData<>(LoomView.class, STANDARD));
        }
        if (menuType == MenuType.MERCHANT) {
            return asType(new MenuTypeData<>(MerchantView.class, STANDARD));
        }
        if (menuType == MenuType.SMITHING) {
            return asType(new MenuTypeData<>(InventoryView.class, worldAccess(ContainerSmithing::new)));
        }
        if (menuType == MenuType.SMOKER) {
            return asType(new MenuTypeData<>(FurnaceView.class, tileEntity(TileEntitySmoker::new, Blocks.SMOKER)));
        }
        if (menuType == MenuType.CARTOGRAPHY_TABLE) {
            return asType(new MenuTypeData<>(InventoryView.class, worldAccess(ContainerCartography::new)));
        }
        if (menuType == MenuType.STONECUTTER) {
            return asType(new MenuTypeData<>(StonecutterView.class, worldAccess(ContainerStonecutter::new)));
        }

        return asType(new MenuTypeData<>(InventoryView.class, STANDARD));
    }

    private static <V extends InventoryView> MenuTypeData<V> asType(MenuTypeData<?> data) {
        return (MenuTypeData<V>) data;
    }
}
