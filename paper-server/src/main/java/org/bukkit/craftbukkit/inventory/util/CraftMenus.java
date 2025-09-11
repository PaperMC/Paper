package org.bukkit.craftbukkit.inventory.util;

import java.util.function.Supplier;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.craftbukkit.inventory.CraftMenuType;
import org.bukkit.craftbukkit.inventory.view.builder.CraftBlockEntityInventorySupportViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftBlockEntityInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftBlockLocationInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftDoubleChestInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftEnchantmentInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftInventorySupportViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftMerchantInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftStandardInventoryViewBuilder;
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
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class CraftMenus {

    public record MenuTypeData<V extends InventoryView, B extends InventoryViewBuilder<V>>(Class<V> viewClass,
                                                                                           Supplier<B> viewBuilder) {
    }

    public static <V extends InventoryView, B extends InventoryViewBuilder<V>> MenuTypeData<V, B> getMenuTypeData(final CraftMenuType<?, ?> menuType) {
        final net.minecraft.world.inventory.MenuType<?> handle = menuType.getHandle();
        if (menuType == MenuType.GENERIC_9X1) {
            return asType(new MenuTypeData<>(InventoryView.class, CraftInventorySupportViewBuilder.CHEST.apply(handle, 1)));
        }
        if (menuType == MenuType.GENERIC_9X2) {
            return asType(new MenuTypeData<>(InventoryView.class, CraftInventorySupportViewBuilder.CHEST.apply(handle, 2)));
        }
        if (menuType == MenuType.GENERIC_9X3) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventorySupportViewBuilder<>(handle, Blocks.CHEST, ChestBlockEntity::new, ChestMenu::threeRows, false)));
        }
        if (menuType == MenuType.GENERIC_9X4) {
            return asType(new MenuTypeData<>(InventoryView.class, CraftInventorySupportViewBuilder.CHEST.apply(handle, 4)));
        }
        if (menuType == MenuType.GENERIC_9X5) {
            return asType(new MenuTypeData<>(InventoryView.class, CraftInventorySupportViewBuilder.CHEST.apply(handle, 5)));
        }
        if (menuType == MenuType.GENERIC_9X6) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftDoubleChestInventoryViewBuilder<>(handle)));
        }
        if (menuType == MenuType.GENERIC_3X3) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventorySupportViewBuilder<>(handle, Blocks.DISPENSER, DispenserBlockEntity::new, DispenserMenu::new, true)));
        }
        if (menuType == MenuType.CRAFTER_3X3) {
            return asType(new MenuTypeData<>(CrafterView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.CRAFTER, CrafterBlockEntity::new, true)));
        }
        if (menuType == MenuType.ANVIL) {
            return asType(new MenuTypeData<>(AnvilView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.ANVIL)));
        }
        if (menuType == MenuType.BEACON) {
            return asType(new MenuTypeData<>(BeaconView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BEACON, BeaconBlockEntity::new, true)));
        }
        if (menuType == MenuType.BLAST_FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BLAST_FURNACE, BlastFurnaceBlockEntity::new, true)));
        }
        if (menuType == MenuType.BREWING_STAND) {
            return asType(new MenuTypeData<>(BrewingStandView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BREWING_STAND, BrewingStandBlockEntity::new, true)));
        }
        if (menuType == MenuType.CRAFTING) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.CRAFTING_TABLE)));
        }
        if (menuType == MenuType.ENCHANTMENT) {
            return asType(new MenuTypeData<>(EnchantmentView.class, () -> new CraftEnchantmentInventoryViewBuilder(handle)));
        }
        if (menuType == MenuType.FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.FURNACE, FurnaceBlockEntity::new, true)));
        }
        if (menuType == MenuType.GRINDSTONE) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.GRINDSTONE)));
        }
        if (menuType == MenuType.HOPPER) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventorySupportViewBuilder<>(handle, Blocks.HOPPER, HopperBlockEntity::new, HopperMenu::new, true)));
        }
        if (menuType == MenuType.LECTERN) {
            return asType(new MenuTypeData<>(LecternView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.LECTERN, LecternBlockEntity::new, true)));
        }
        if (menuType == MenuType.LOOM) {
            return asType(new MenuTypeData<>(LoomView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.LOOM)));
        }
        if (menuType == MenuType.MERCHANT) {
            return asType(new MenuTypeData<>(MerchantView.class, () -> new CraftMerchantInventoryViewBuilder<>(handle)));
        }
        if (menuType == MenuType.SHULKER_BOX) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventorySupportViewBuilder<>(handle, Blocks.SHULKER_BOX, ShulkerBoxBlockEntity::new, ShulkerBoxMenu::new, true)));
        }
        if (menuType == MenuType.SMITHING) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.SMITHING_TABLE)));
        }
        if (menuType == MenuType.SMOKER) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.SMOKER, SmokerBlockEntity::new, true)));
        }
        if (menuType == MenuType.CARTOGRAPHY_TABLE) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.CARTOGRAPHY_TABLE)));
        }
        if (menuType == MenuType.STONECUTTER) {
            return asType(new MenuTypeData<>(StonecutterView.class, () -> new CraftBlockLocationInventoryViewBuilder<>(handle, Blocks.STONECUTTER)));
        }

        // graceful fallback for new version content
        return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftStandardInventoryViewBuilder<>(handle)));
    }

    @SuppressWarnings("unchecked")
    private static <V extends InventoryView, B extends InventoryViewBuilder<V>> MenuTypeData<V, B> asType(final MenuTypeData<?, ?> data) {
        return (MenuTypeData<V, B>) data;
    }
}
