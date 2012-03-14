package org.bukkit.craftbukkit.entity;

import java.util.Set;

import net.minecraft.server.Container;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ICrafting;
import net.minecraft.server.Packet100OpenWindow;
import net.minecraft.server.Packet101CloseWindow;
import net.minecraft.server.TileEntityBrewingStand;
import net.minecraft.server.TileEntityDispenser;
import net.minecraft.server.TileEntityFurnace;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.inventory.CraftInventory;
import org.bukkit.craftbukkit.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class CraftHumanEntity extends CraftLivingEntity implements HumanEntity {
    private CraftInventoryPlayer inventory;
    protected final PermissibleBase perm = new PermissibleBase(this);
    private boolean op;
    private GameMode mode;

    public CraftHumanEntity(final CraftServer server, final EntityHuman entity) {
        super(server, entity);
        mode = server.getDefaultGameMode();
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    public String getName() {
        return getHandle().name;
    }

    public PlayerInventory getInventory() {
        return inventory;
    }

    public ItemStack getItemInHand() {
        return getInventory().getItemInHand();
    }

    public void setItemInHand(ItemStack item) {
        getInventory().setItemInHand(item);
    }

    public ItemStack getItemOnCursor() {
        return new CraftItemStack(getHandle().inventory.getCarried());
    }

    public void setItemOnCursor(ItemStack item) {
        net.minecraft.server.ItemStack stack = CraftItemStack.createNMSItemStack(item);
        getHandle().inventory.setCarried(stack);
        if (this instanceof CraftPlayer) {
            ((EntityPlayer) getHandle()).broadcastCarriedItem(); // Send set slot for cursor
        }
    }

    public boolean isSleeping() {
        return getHandle().sleeping;
    }

    public int getSleepTicks() {
        return getHandle().sleepTicks;
    }

    public boolean isOp() {
        return op;
    }

    public boolean isPermissionSet(String name) {
        return perm.isPermissionSet(name);
    }

    public boolean isPermissionSet(Permission perm) {
        return this.perm.isPermissionSet(perm);
    }

    public boolean hasPermission(String name) {
        return perm.hasPermission(name);
    }

    public boolean hasPermission(Permission perm) {
        return this.perm.hasPermission(perm);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return perm.addAttachment(plugin, name, value);
    }

    public PermissionAttachment addAttachment(Plugin plugin) {
        return perm.addAttachment(plugin);
    }

    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return perm.addAttachment(plugin, name, value, ticks);
    }

    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return perm.addAttachment(plugin, ticks);
    }

    public void removeAttachment(PermissionAttachment attachment) {
        perm.removeAttachment(attachment);
    }

    public void recalculatePermissions() {
        perm.recalculatePermissions();
    }

    public void setOp(boolean value) {
        this.op = value;
        perm.recalculatePermissions();
    }

    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return perm.getEffectivePermissions();
    }

    public GameMode getGameMode() {
        return mode;
    }

    public void setGameMode(GameMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("Mode cannot be null");
        }

        this.mode = mode;
    }

    @Override
    public EntityHuman getHandle() {
        return (EntityHuman) entity;
    }

    public void setHandle(final EntityHuman entity) {
        super.setHandle(entity);
        this.inventory = new CraftInventoryPlayer(entity.inventory);
    }

    @Override
    public String toString() {
        return "CraftHumanEntity{" + "id=" + getEntityId() + "name=" + getName() + '}';
    }

    public InventoryView getOpenInventory() {
        return getHandle().activeContainer.getBukkitView();
    }

    public InventoryView openInventory(Inventory inventory) {
        InventoryType type = inventory.getType();
        // TODO: Should we check that it really IS a CraftInventory first?
        CraftInventory craftinv = (CraftInventory) inventory;
        switch(type) {
        case PLAYER:
        case CHEST:
            getHandle().openContainer(craftinv.getInventory());
            break;
        case DISPENSER:
            getHandle().openDispenser((TileEntityDispenser)craftinv.getInventory());
            break;
        case FURNACE:
            getHandle().openFurnace((TileEntityFurnace)craftinv.getInventory());
            break;
        case WORKBENCH:
            getHandle().startCrafting(getLocation().getBlockX(), getLocation().getBlockY(), getLocation().getBlockZ());
            break;
        case BREWING:
            getHandle().openBrewingStand((TileEntityBrewingStand)craftinv.getInventory());
            break;
        case ENCHANTING:
            getHandle().startEnchanting(getLocation().getBlockX(), getLocation().getBlockY(), getLocation().getBlockZ());
            break;
        case CREATIVE:
        case CRAFTING:
            throw new IllegalArgumentException("Can't open a " + type + " inventory!");
        }
        getHandle().activeContainer.checkReachable = false;
        return getHandle().activeContainer.getBukkitView();
    }

    public InventoryView openWorkbench(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.WORKBENCH) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }
        getHandle().startCrafting(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (force) {
            getHandle().activeContainer.checkReachable = false;
        }
        return getHandle().activeContainer.getBukkitView();
    }

    public InventoryView openEnchanting(Location location, boolean force) {
        if (!force) {
            Block block = location.getBlock();
            if (block.getType() != Material.ENCHANTMENT_TABLE) {
                return null;
            }
        }
        if (location == null) {
            location = getLocation();
        }
        getHandle().startEnchanting(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (force) {
            getHandle().activeContainer.checkReachable = false;
        }
        return getHandle().activeContainer.getBukkitView();
    }

    public void openInventory(InventoryView inventory) {
        if (!(getHandle() instanceof EntityPlayer)) return; // TODO: NPC support?
        if (getHandle().activeContainer != getHandle().defaultContainer) {
            // fire INVENTORY_CLOSE if one already open
            ((EntityPlayer)getHandle()).netServerHandler.handleContainerClose(new Packet101CloseWindow(getHandle().activeContainer.windowId));
        }
        EntityPlayer player = (EntityPlayer) getHandle();
        Container container;
        if (inventory instanceof CraftInventoryView) {
            container = ((CraftInventoryView) inventory).getHandle();
        } else {
            container = new CraftContainer(inventory, player.nextContainerCounter());
        }

        // Trigger an INVENTORY_OPEN event
        InventoryOpenEvent event = new InventoryOpenEvent(inventory);
        player.activeContainer.transferTo(container, this);
        server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            container.transferTo(player.activeContainer, this);
            return;
        }

        // Now open the window
        player.netServerHandler.sendPacket(new Packet100OpenWindow(container.windowId, 1, "Crafting", 9));
        player.activeContainer = container;
        player.activeContainer.addSlotListener((ICrafting) player);
    }

    public void closeInventory() {
        getHandle().closeInventory();
    }

    public boolean isBlocking() {
        return getHandle().O();
    }

    public boolean setWindowProperty(InventoryView.Property prop, int value) {
        return false;
    }
}
