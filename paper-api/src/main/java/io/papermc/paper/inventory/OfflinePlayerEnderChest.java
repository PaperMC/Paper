package io.papermc.paper.inventory;

import java.util.HashMap;
import java.util.List;
import io.papermc.paper.entity.PlayerDataFile;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents inventory of offline player
 */
public interface OfflinePlayerEnderChest extends Inventory{
    @Override
    @Nullable PlayerDataFile getHolder();

    @Override
    @Contract("_ -> fail")
    default void setMaxStackSize(final int size){
        throw new UnsupportedOperationException("Inventory is abstract");
    }

    @Override
    @Contract(" -> fail")
    default int close(){
        throw new UnsupportedOperationException("Inventory is abstract");
    }

    @Override
    @Contract(" -> fail")
    default @NotNull List<HumanEntity> getViewers(){
        throw new UnsupportedOperationException("Inventory is abstract");
    }

    default @Nullable InventoryHolder getHolder(final boolean useSnapshot){
        return getHolder();
    }

    @Override
    default @NotNull HashMap<Integer, ItemStack> removeItemAnySlot(final @NotNull ItemStack... items) throws IllegalArgumentException {
        return removeItem(items);
    }

    @Override
    default @Nullable ItemStack @NotNull [] getStorageContents() {
        return getContents();
    }

    @Override
    default void setStorageContents(@Nullable ItemStack @NotNull [] items) throws IllegalArgumentException{
        setContents(items);
    }

    @Override
    default @NotNull InventoryType getType() {
        return InventoryType.ENDER_CHEST;
    }
}
