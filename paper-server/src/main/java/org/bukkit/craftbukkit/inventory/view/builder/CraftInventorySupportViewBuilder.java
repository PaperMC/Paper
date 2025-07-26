package org.bukkit.craftbukkit.inventory.view.builder;

import com.google.common.base.Preconditions;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryCustom;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.view.builder.InventorySupport;
import org.bukkit.inventory.view.builder.InventoryViewBuilder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class CraftInventorySupportViewBuilder<V extends InventoryView> extends CraftInventoryViewBuilder<V> implements InventorySupport {

    public static final BiFunction<MenuType<?>, Integer, Supplier<InventoryViewBuilder<InventoryView>>> CHEST = (type, rows) -> () ->
        new CraftInventorySupportViewBuilder<>(type, (type1, player, container) ->
            new ChestMenu(type1, player.nextContainerCounter(), player.getInventory(), container, rows));

    private final MenuWithContainerBuilder menuBuilder;
    private @Nullable Inventory inventory;

    public CraftInventorySupportViewBuilder(final MenuType<?> handle, MenuWithContainerBuilder menuBuilder) {
        super(handle);
        this.menuBuilder = menuBuilder;
        super.defaultTitle = Component.translatable("container.chest");
    }

    @Override
    public InventoryViewBuilder<V> copy() {
        final CraftInventorySupportViewBuilder<V> builder = new CraftInventorySupportViewBuilder<>(super.handle, this.menuBuilder);
        builder.title = super.title;
        builder.inventory = this.inventory;
        return builder;
    }

    @Override
    public InventoryViewBuilder<InventoryView> inventory(final Inventory inventory) {
        Preconditions.checkArgument(inventory != null, "the provided inventory must not be null");
        Preconditions.checkArgument(!(inventory instanceof CraftInventoryCustom), "Can not set CraftInventoryCustom as a inventory for a view please use Server#createMenuInventory");
        this.inventory = inventory;
        return (InventoryViewBuilder<InventoryView>) this;
    }

    @Override
    protected AbstractContainerMenu buildContainer(final ServerPlayer player) {
        if (inventory == null) {
            return handle.create(player.nextContainerCounter(), player.getInventory());
        }
        return menuBuilder.build(super.handle, player, ((CraftInventory) inventory).getInventory());
    }

    public interface MenuWithContainerBuilder {
        AbstractContainerMenu build(MenuType<?> type, ServerPlayer player, Container container);
    }
}
