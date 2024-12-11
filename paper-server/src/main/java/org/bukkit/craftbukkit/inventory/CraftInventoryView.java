package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class CraftInventoryView<T extends AbstractContainerMenu, I extends Inventory> extends CraftAbstractInventoryView {
    protected final T container;
    private final CraftHumanEntity player;
    private final I viewing;
    private final String originalTitle;
    private String title;

    public CraftInventoryView(HumanEntity player, I viewing, T container) {
        // TODO: Should we make sure it really IS a CraftHumanEntity first? And a CraftInventory?
        this.player = (CraftHumanEntity) player;
        this.viewing = viewing;
        this.container = container;
        this.originalTitle = CraftChatMessage.fromComponent(container.getTitle());
        this.title = this.originalTitle;
    }

    @Override
    public I getTopInventory() {
        return this.viewing;
    }

    @Override
    public Inventory getBottomInventory() {
        return this.player.getInventory();
    }

    @Override
    public HumanEntity getPlayer() {
        return this.player;
    }

    @Override
    public InventoryType getType() {
        InventoryType type = this.viewing.getType();
        if (type == InventoryType.CRAFTING && this.player.getGameMode() == GameMode.CREATIVE) {
            return InventoryType.CREATIVE;
        }
        return type;
    }

    @Override
    public void setItem(int slot, ItemStack item) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        if (slot >= 0) {
            this.container.getSlot(slot).set(stack);
        } else {
            this.player.getHandle().drop(stack, false);
        }
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot < 0) {
            return null;
        }
        return CraftItemStack.asCraftMirror(this.container.getSlot(slot).getItem());
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getOriginalTitle() {
        return this.originalTitle;
    }

    @Override
    public void setTitle(String title) {
        CraftInventoryView.sendInventoryTitleChange(this, title);
        this.title = title;
    }

    public boolean isInTop(int rawSlot) {
        return rawSlot < this.viewing.getSize();
    }

    public AbstractContainerMenu getHandle() {
        return this.container;
    }

    public static void sendInventoryTitleChange(InventoryView view, String title) {
        Preconditions.checkArgument(view != null, "InventoryView cannot be null");
        Preconditions.checkArgument(title != null, "Title cannot be null");
        Preconditions.checkArgument(view.getPlayer() instanceof Player, "NPCs are not currently supported for this function");
        Preconditions.checkArgument(view.getTopInventory().getType().isCreatable(), "Only creatable inventories can have their title changed");

        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) view.getPlayer()).getHandle();
        final int containerId = entityPlayer.containerMenu.containerId;
        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(view.getTopInventory());
        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromString(title)[0]));
        ((Player) view.getPlayer()).updateInventory();
    }
}
