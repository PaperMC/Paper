package org.bukkit.craftbukkit.inventory.view.builder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.jspecify.annotations.Nullable;

// This name SUCKS big time
public class CraftInventorySupportBlockEntityInventoryViewBuilder<V extends InventoryView> extends CraftBlockEntityInventoryViewBuilder<V> implements InventorySupport<V> {
    private final SimpleMenuBuilder smb;

    private @Nullable Inventory inventory;

    public CraftInventorySupportBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final @Nullable CraftBlockInventoryBuilder builder, final SimpleMenuBuilder smb, final boolean useFakeBlockEntity) {
        super(handle, block, builder, useFakeBlockEntity);
        this.smb = smb;
    }

    public CraftInventorySupportBlockEntityInventoryViewBuilder(final MenuType<?> handle, final Block block, final @Nullable CraftBlockInventoryBuilder builder, final SimpleMenuBuilder smb) {
        super(handle, block, builder);
        this.smb = smb;
    }

    @Override
    public LocationInventoryViewBuilder<V> inventory(final Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (inventory == null) {
            return super.buildContainer(player);
        }

        if (this.world == null) {
            this.world = player.level();
        }

        if (this.position == null) {
            this.position = player.blockPosition();
        }

        // this is for default title
        final MenuProvider provider = this.builder.build(this.position, this.block.defaultBlockState());
        if (provider instanceof final BlockEntity blockEntity) {
            blockEntity.setLevel(this.world);
            super.defaultTitle = provider.getDisplayName();
        }

        return this.smb.build(player.nextContainerCounter(), player.getInventory(), ((CraftInventory) this.inventory).getInventory());
    }

    @Override
    public LocationInventoryViewBuilder<V> copy() {
        final CraftInventorySupportBlockEntityInventoryViewBuilder<V> copy = new CraftInventorySupportBlockEntityInventoryViewBuilder<>(super.handle, this.block, this.builder, this.smb, this.useFakeBlockEntity);
        copy.world = this.world;
        copy.position = this.position;
        copy.checkReachable = super.checkReachable;
        copy.title = title;
        return copy;
    }

    public interface SimpleMenuBuilder {
        static SimpleMenuBuilder chest(int rows, MenuType<?> type) {
            return (id, pi, c) -> new ChestMenu(type, id, pi, c, rows);
        }

        AbstractContainerMenu build(int containerId, net.minecraft.world.entity.player.Inventory playerInventory, Container container);
    }
}
