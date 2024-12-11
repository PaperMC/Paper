package org.bukkit.craftbukkit.inventory.util;

import static org.bukkit.craftbukkit.inventory.util.CraftMenuBuilder.*;

import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.inventory.StonecutterMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
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
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, tileEntity(DispenserBlockEntity::new, Blocks.DISPENSER)));
        }
        if (menuType == MenuType.CRAFTER_3X3) {
            return CraftMenus.asType(new MenuTypeData<>(CrafterView.class, tileEntity(CrafterBlockEntity::new, Blocks.CRAFTER)));
        }
        if (menuType == MenuType.ANVIL) {
            return CraftMenus.asType(new MenuTypeData<>(AnvilView.class, worldAccess(AnvilMenu::new)));
        }
        if (menuType == MenuType.BEACON) {
            return CraftMenus.asType(new MenuTypeData<>(BeaconView.class, tileEntity(BeaconBlockEntity::new, Blocks.BEACON)));
        }
        if (menuType == MenuType.BLAST_FURNACE) {
            return CraftMenus.asType(new MenuTypeData<>(FurnaceView.class, tileEntity(BlastFurnaceBlockEntity::new, Blocks.BLAST_FURNACE)));
        }
        if (menuType == MenuType.BREWING_STAND) {
            return CraftMenus.asType(new MenuTypeData<>(BrewingStandView.class, tileEntity(BrewingStandBlockEntity::new, Blocks.BREWING_STAND)));
        }
        if (menuType == MenuType.CRAFTING) {
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, worldAccess(CraftingMenu::new)));
        }
        if (menuType == MenuType.ENCHANTMENT) {
            return CraftMenus.asType(new MenuTypeData<>(EnchantmentView.class, (player, type) -> {
                return new SimpleMenuProvider((syncId, inventory, human) -> {
                    return worldAccess(EnchantmentMenu::new).build(player, type);
                }, Component.empty()).createMenu(player.nextContainerCounter(), player.getInventory(), player);
            }));
        }
        if (menuType == MenuType.FURNACE) {
            return CraftMenus.asType(new MenuTypeData<>(FurnaceView.class, tileEntity(FurnaceBlockEntity::new, Blocks.FURNACE)));
        }
        if (menuType == MenuType.GRINDSTONE) {
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, worldAccess(GrindstoneMenu::new)));
        }
        // We really don't need to be creating a tile entity for hopper but currently InventoryType doesn't have capacity
        // to understand otherwise
        if (menuType == MenuType.HOPPER) {
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, tileEntity(HopperBlockEntity::new, Blocks.HOPPER)));
        }
        // We also don't need to create a tile entity for lectern, but again InventoryType isn't smart enough to know any better
        if (menuType == MenuType.LECTERN) {
            return CraftMenus.asType(new MenuTypeData<>(LecternView.class, tileEntity(LecternBlockEntity::new, Blocks.LECTERN)));
        }
        if (menuType == MenuType.LOOM) {
            return CraftMenus.asType(new MenuTypeData<>(LoomView.class, CraftMenus.STANDARD));
        }
        if (menuType == MenuType.MERCHANT) {
            return CraftMenus.asType(new MenuTypeData<>(MerchantView.class, CraftMenus.STANDARD));
        }
        if (menuType == MenuType.SMITHING) {
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, worldAccess(SmithingMenu::new)));
        }
        if (menuType == MenuType.SMOKER) {
            return CraftMenus.asType(new MenuTypeData<>(FurnaceView.class, tileEntity(SmokerBlockEntity::new, Blocks.SMOKER)));
        }
        if (menuType == MenuType.CARTOGRAPHY_TABLE) {
            return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, worldAccess(CartographyTableMenu::new)));
        }
        if (menuType == MenuType.STONECUTTER) {
            return CraftMenus.asType(new MenuTypeData<>(StonecutterView.class, worldAccess(StonecutterMenu::new)));
        }

        return CraftMenus.asType(new MenuTypeData<>(InventoryView.class, CraftMenus.STANDARD));
    }

    private static <V extends InventoryView> MenuTypeData<V> asType(MenuTypeData<?> data) {
        return (MenuTypeData<V>) data;
    }
}
