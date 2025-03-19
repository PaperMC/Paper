package org.bukkit.craftbukkit.inventory.util;

import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffers;
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
import org.bukkit.craftbukkit.inventory.CraftMerchant;
import org.bukkit.craftbukkit.inventory.view.builder.CraftAccessLocationInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftBlockEntityInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftDoubleChestInventoryViewBuilder;
import org.bukkit.craftbukkit.inventory.view.builder.CraftEnchantmentInventoryViewBuilder;
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

import java.util.function.Supplier;

@NullMarked
public final class CraftMenus {

    public record MenuTypeData<V extends InventoryView, B extends InventoryViewBuilder<V>>(Class<V> viewClass, Supplier<B> viewBuilder) {
    }

    // This is a temporary measure that will likely be removed with the rewrite of HumanEntity#open[] methods
    public static void openMerchantMenu(final ServerPlayer player, final MerchantMenu merchant) {
        final Merchant minecraftMerchant = ((CraftMerchant) merchant.getBukkitView().getMerchant()).getMerchant();
        int level = 1;
        if (minecraftMerchant instanceof final Villager villager) {
            level = villager.getVillagerData().level();
        }

        if (minecraftMerchant.getTradingPlayer() != null) { // merchant's can only have one trader
            minecraftMerchant.getTradingPlayer().closeContainer();
        }

        minecraftMerchant.setTradingPlayer(player);

        player.connection.send(new ClientboundOpenScreenPacket(merchant.containerId, net.minecraft.world.inventory.MenuType.MERCHANT, merchant.getTitle()));
        player.containerMenu = merchant;
        player.initMenu(merchant);

        // Copy Merchant#openTradingScreen
        MerchantOffers offers = minecraftMerchant.getOffers();
        if (!offers.isEmpty()) {
            player.sendMerchantOffers(merchant.containerId, offers, level, minecraftMerchant.getVillagerXp(), minecraftMerchant.showProgressBar(), minecraftMerchant.canRestock());
        }
        // End Copy Merchant#openTradingScreen
    }

    public static <V extends InventoryView, B extends InventoryViewBuilder<V>> MenuTypeData<V, B> getMenuTypeData(final CraftMenuType<?, ?> menuType) {
        final net.minecraft.world.inventory.MenuType<?> handle = menuType.getHandle();
        // this sucks horribly but it should work for now
        if (menuType == MenuType.GENERIC_9X6) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftDoubleChestInventoryViewBuilder<>(handle)));
        }
        if (menuType == MenuType.GENERIC_9X3) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.CHEST, ChestBlockEntity::new, false)));
        }
        // this isn't ideal as both dispenser and dropper are 3x3, InventoryType can't currently handle generic 3x3s with size 9
        // this needs to be removed when inventory creation is overhauled
        if (menuType == MenuType.GENERIC_3X3) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.DISPENSER, DispenserBlockEntity::new)));
        }
        if (menuType == MenuType.CRAFTER_3X3) {
            return asType(new MenuTypeData<>(CrafterView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.CRAFTER, CrafterBlockEntity::new)));
        }
        if (menuType == MenuType.ANVIL) {
            return asType(new MenuTypeData<>(AnvilView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.ANVIL)));
        }
        if (menuType == MenuType.BEACON) {
            return asType(new MenuTypeData<>(BeaconView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BEACON, BeaconBlockEntity::new)));
        }
        if (menuType == MenuType.BLAST_FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BLAST_FURNACE, BlastFurnaceBlockEntity::new)));
        }
        if (menuType == MenuType.BREWING_STAND) {
            return asType(new MenuTypeData<>(BrewingStandView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.BREWING_STAND, BrewingStandBlockEntity::new)));
        }
        if (menuType == MenuType.CRAFTING) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.CRAFTING_TABLE)));
        }
        if (menuType == MenuType.ENCHANTMENT) {
            return asType(new MenuTypeData<>(EnchantmentView.class, () -> new CraftEnchantmentInventoryViewBuilder(handle)));
        }
        if (menuType == MenuType.FURNACE) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.FURNACE, FurnaceBlockEntity::new)));
        }
        if (menuType == MenuType.GRINDSTONE) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.GRINDSTONE)));
        }
        // We really don't need to be creating a block entity for hopper but currently InventoryType doesn't have capacity
        // to understand otherwise
        if (menuType == MenuType.HOPPER) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.HOPPER, HopperBlockEntity::new)));
        }
        // We also don't need to create a block entity for lectern, but again InventoryType isn't smart enough to know any better
        if (menuType == MenuType.LECTERN) {
            return asType(new MenuTypeData<>(LecternView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.LECTERN, LecternBlockEntity::new)));
        }
        if (menuType == MenuType.LOOM) {
            return asType(new MenuTypeData<>(LoomView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.LOOM)));
        }
        if (menuType == MenuType.MERCHANT) {
            return asType(new MenuTypeData<>(MerchantView.class, () -> new CraftMerchantInventoryViewBuilder<>(handle)));
        }
        if (menuType == MenuType.SHULKER_BOX) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.SHULKER_BOX, ShulkerBoxBlockEntity::new)));
        }
        if (menuType == MenuType.SMITHING) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.SMITHING_TABLE)));
        }
        if (menuType == MenuType.SMOKER) {
            return asType(new MenuTypeData<>(FurnaceView.class, () -> new CraftBlockEntityInventoryViewBuilder<>(handle, Blocks.SMOKER, SmokerBlockEntity::new)));
        }
        if (menuType == MenuType.CARTOGRAPHY_TABLE) {
            return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.CARTOGRAPHY_TABLE)));
        }
        if (menuType == MenuType.STONECUTTER) {
            return asType(new MenuTypeData<>(StonecutterView.class, () -> new CraftAccessLocationInventoryViewBuilder<>(handle, Blocks.STONECUTTER)));
        }

        return asType(new MenuTypeData<>(InventoryView.class, () -> new CraftStandardInventoryViewBuilder<>(handle)));
    }

    @SuppressWarnings("unchecked")
    private static <V extends InventoryView, B extends InventoryViewBuilder<V>> MenuTypeData<V, B> asType(final MenuTypeData<?, ?> data) {
        return (MenuTypeData<V, B>) data;
    }
}
