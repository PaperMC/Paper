package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.IInventory;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.Container;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.ContainerBeacon;
import net.minecraft.world.inventory.ContainerBlastFurnace;
import net.minecraft.world.inventory.ContainerBrewingStand;
import net.minecraft.world.inventory.ContainerCartography;
import net.minecraft.world.inventory.ContainerChest;
import net.minecraft.world.inventory.ContainerDispenser;
import net.minecraft.world.inventory.ContainerEnchantTable;
import net.minecraft.world.inventory.ContainerFurnaceFurnace;
import net.minecraft.world.inventory.ContainerGrindstone;
import net.minecraft.world.inventory.ContainerHopper;
import net.minecraft.world.inventory.ContainerLectern;
import net.minecraft.world.inventory.ContainerLoom;
import net.minecraft.world.inventory.ContainerMerchant;
import net.minecraft.world.inventory.ContainerProperties;
import net.minecraft.world.inventory.ContainerShulkerBox;
import net.minecraft.world.inventory.ContainerSmithing;
import net.minecraft.world.inventory.ContainerSmoker;
import net.minecraft.world.inventory.ContainerStonecutter;
import net.minecraft.world.inventory.ContainerWorkbench;
import net.minecraft.world.inventory.Containers;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class CraftContainer extends Container {

    private final InventoryView view;
    private InventoryType cachedType;
    private Container delegate;

    public CraftContainer(InventoryView view, EntityHuman player, int id) {
        super(getNotchInventoryType(view.getTopInventory()), id);
        this.view = view;
        // TODO: Do we need to check that it really is a CraftInventory?
        IInventory top = ((CraftInventory) view.getTopInventory()).getInventory();
        PlayerInventory bottom = (PlayerInventory) ((CraftInventory) view.getBottomInventory()).getInventory();
        cachedType = view.getType();
        setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final EntityHuman player, int id) {
        this(new InventoryView() {
            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return getPlayer().getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player.getBukkitEntity();
            }

            @Override
            public InventoryType getType() {
                return inventory.getType();
            }

            @Override
            public String getTitle() {
                return inventory instanceof CraftInventoryCustom ? ((CraftInventoryCustom.MinecraftInventory) ((CraftInventory) inventory).getInventory()).getTitle() : inventory.getType().getDefaultTitle();
            }
        }, player, id);
    }

    @Override
    public InventoryView getBukkitView() {
        return view;
    }

    public static Containers getNotchInventoryType(Inventory inventory) {
        switch (inventory.getType()) {
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (inventory.getSize()) {
                    case 9:
                        return Containers.GENERIC_9x1;
                    case 18:
                        return Containers.GENERIC_9x2;
                    case 27:
                        return Containers.GENERIC_9x3;
                    case 36:
                    case 41: // PLAYER
                        return Containers.GENERIC_9x4;
                    case 45:
                        return Containers.GENERIC_9x5;
                    case 54:
                        return Containers.GENERIC_9x6;
                    default:
                        throw new IllegalArgumentException("Unsupported custom inventory size " + inventory.getSize());
                }
            case WORKBENCH:
                return Containers.CRAFTING;
            case FURNACE:
                return Containers.FURNACE;
            case DISPENSER:
                return Containers.GENERIC_3x3;
            case ENCHANTING:
                return Containers.ENCHANTMENT;
            case BREWING:
                return Containers.BREWING_STAND;
            case BEACON:
                return Containers.BEACON;
            case ANVIL:
                return Containers.ANVIL;
            case SMITHING:
                return Containers.SMITHING;
            case HOPPER:
                return Containers.HOPPER;
            case DROPPER:
                return Containers.GENERIC_3x3;
            case SHULKER_BOX:
                return Containers.SHULKER_BOX;
            case BLAST_FURNACE:
                return Containers.BLAST_FURNACE;
            case LECTERN:
                return Containers.LECTERN;
            case SMOKER:
                return Containers.SMOKER;
            case LOOM:
                return Containers.LOOM;
            case CARTOGRAPHY:
                return Containers.CARTOGRAPHY_TABLE;
            case GRINDSTONE:
                return Containers.GRINDSTONE;
            case STONECUTTER:
                return Containers.STONECUTTER;
            case CREATIVE:
            case CRAFTING:
            case MERCHANT:
                throw new IllegalArgumentException("Can't open a " + inventory.getType() + " inventory!");
            default:
                // TODO: If it reaches the default case, should we throw an error?
                return Containers.GENERIC_9x3;
        }
    }

    private void setupSlots(IInventory top, PlayerInventory bottom, EntityHuman entityhuman) {
        int windowId = -1;
        switch (cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                delegate = new ContainerChest(Containers.GENERIC_9x3, windowId, bottom, top, top.getContainerSize() / 9);
                break;
            case DISPENSER:
            case DROPPER:
                delegate = new ContainerDispenser(windowId, bottom, top);
                break;
            case FURNACE:
                delegate = new ContainerFurnaceFurnace(windowId, bottom, top, new ContainerProperties(4));
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                delegate = new ContainerEnchantTable(windowId, bottom);
                break;
            case BREWING:
                delegate = new ContainerBrewingStand(windowId, bottom, top, new ContainerProperties(2));
                break;
            case HOPPER:
                delegate = new ContainerHopper(windowId, bottom, top);
                break;
            case ANVIL:
                setupAnvil(top, bottom); // SPIGOT-6783 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case SMITHING:
                delegate = new ContainerSmithing(windowId, bottom);
                break;
            case BEACON:
                delegate = new ContainerBeacon(windowId, bottom);
                break;
            case SHULKER_BOX:
                delegate = new ContainerShulkerBox(windowId, bottom, top);
                break;
            case BLAST_FURNACE:
                delegate = new ContainerBlastFurnace(windowId, bottom, top, new ContainerProperties(4));
                break;
            case LECTERN:
                delegate = new ContainerLectern(windowId, top, new ContainerProperties(1), bottom);
                break;
            case SMOKER:
                delegate = new ContainerSmoker(windowId, bottom, top, new ContainerProperties(4));
                break;
            case LOOM:
                delegate = new ContainerLoom(windowId, bottom);
                break;
            case CARTOGRAPHY:
                delegate = new ContainerCartography(windowId, bottom);
                break;
            case GRINDSTONE:
                delegate = new ContainerGrindstone(windowId, bottom);
                break;
            case STONECUTTER:
                delegate = new ContainerStonecutter(windowId, bottom);
                break;
            case MERCHANT:
                delegate = new ContainerMerchant(windowId, bottom);
                break;
        }

        if (delegate != null) {
            this.lastSlots = delegate.lastSlots;
            this.slots = delegate.slots;
            this.remoteSlots = delegate.remoteSlots;
        }

        // SPIGOT-4598 - we should still delegate the shift click handler
        switch (cachedType) {
            case WORKBENCH:
                delegate = new ContainerWorkbench(windowId, bottom);
                break;
            case ANVIL:
                delegate = new ContainerAnvil(windowId, bottom);
                break;
        }
    }

    private void setupWorkbench(IInventory top, IInventory bottom) {
        // This code copied from ContainerWorkbench
        this.addSlot(new Slot(top, 0, 124, 35));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 3; ++col) {
                this.addSlot(new Slot(top, 1 + col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (col = 0; col < 9; ++col) {
            this.addSlot(new Slot(bottom, col, 8 + col * 18, 142));
        }
        // End copy from ContainerWorkbench
    }

    private void setupAnvil(IInventory top, IInventory bottom) {
        // This code copied from ContainerAnvilAbstract
        this.addSlot(new Slot(top, 0, 27, 47));
        this.addSlot(new Slot(top, 1, 76, 47));
        this.addSlot(new Slot(top, 2, 134, 47));

        int row;
        int col;

        for (row = 0; row < 3; ++row) {
            for (col = 0; col < 9; ++col) {
                this.addSlot(new Slot(bottom, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (row = 0; row < 9; ++row) {
            this.addSlot(new Slot(bottom, row, 8 + row * 18, 142));
        }
        // End copy from ContainerAnvilAbstract
    }

    @Override
    public ItemStack quickMoveStack(EntityHuman entityhuman, int i) {
        return (delegate != null) ? delegate.quickMoveStack(entityhuman, i) : ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(EntityHuman entity) {
        return true;
    }

    @Override
    public Containers<?> getType() {
        return getNotchInventoryType(view.getTopInventory());
    }
}
