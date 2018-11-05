package io.papermc.paper.inventory;

import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

@DefaultQualifier(NonNull.class)
public final class PaperInventoryCustomHolderContainer implements Container {

    private final InventoryHolder owner;
    private final Container delegate;
    private final InventoryType type;
    private final String title;
    private final Component adventure$title;

    public PaperInventoryCustomHolderContainer(InventoryHolder owner, Container delegate, InventoryType type) {
        this.owner = owner;
        this.delegate = delegate;
        this.type = type;
        @Nullable Component adventure$title = null;
        if (delegate instanceof BaseContainerBlockEntity blockEntity) {
            adventure$title = blockEntity.getCustomName() != null ? PaperAdventure.asAdventure(blockEntity.getCustomName()) : null;
        }
        if (adventure$title == null) {
            adventure$title = type.defaultTitle();
        }
        this.adventure$title = adventure$title;
        this.title = LegacyComponentSerializer.legacySection().serialize(this.adventure$title);
    }

    public Component title() {
        return this.adventure$title;
    }

    public String getTitle() {
        return this.title;
    }

    public InventoryType getType() {
        return this.type;
    }

    @Override
    public int getContainerSize() {
        return this.delegate.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.delegate.getItem(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return this.delegate.removeItem(slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return this.delegate.removeItemNoUpdate(slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.delegate.setItem(slot, stack);
    }

    @Override
    public int getMaxStackSize() {
        return this.delegate.getMaxStackSize();
    }

    @Override
    public void setChanged() {
        this.delegate.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return this.delegate.stillValid(player);
    }

    @Override
    public List<ItemStack> getContents() {
        return this.delegate.getContents();
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        this.delegate.onOpen(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        this.delegate.onClose(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.delegate.getViewers();
    }

    @Override
    public InventoryHolder getOwner() {
        return this.owner;
    }

    @Override
    public void setMaxStackSize(int size) {
        this.delegate.setMaxStackSize(size);
    }

    @Override
    public Location getLocation() {
        return this.delegate.getLocation();
    }

    @Override
    public void clearContent() {
        this.delegate.clearContent();
    }
}
