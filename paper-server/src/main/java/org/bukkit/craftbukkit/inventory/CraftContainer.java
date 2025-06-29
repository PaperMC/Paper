package org.bukkit.craftbukkit.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.BrewingStandMenu;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.inventory.LecternMenu;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.inventory.MerchantMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.ItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;

public class CraftContainer extends AbstractContainerMenu {

    private final InventoryView view;
    private InventoryType cachedType;
    private AbstractContainerMenu delegate;

    public CraftContainer(InventoryView view, Player player, int id) {
        super(CraftContainer.getNotchInventoryType(view.getTopInventory()), id);
        this.view = view;
        // TODO: Do we need to check that it really is a CraftInventory?
        Container top = ((CraftInventory) view.getTopInventory()).getInventory();
        net.minecraft.world.entity.player.Inventory bottom = (net.minecraft.world.entity.player.Inventory) ((CraftInventory) view.getBottomInventory()).getInventory();
        this.cachedType = view.getType();
        this.setupSlots(top, bottom, player);
    }

    public CraftContainer(final Inventory inventory, final Player player, int id) {
        this(new CraftAbstractInventoryView() {

            private final String originalTitle = inventory instanceof CraftInventoryCustom ? ((CraftInventoryCustom) inventory).getTitle() : inventory.getType().getDefaultTitle(); // Paper
            private String title = this.originalTitle;

            @Override
            public Inventory getTopInventory() {
                return inventory;
            }

            @Override
            public Inventory getBottomInventory() {
                return this.getPlayer().getInventory();
            }

            @Override
            public HumanEntity getPlayer() {
                return player.getBukkitEntity();
            }

            @Override
            public InventoryType getType() {
                return inventory.getType();
            }

            // Paper start
            @Override
            public net.kyori.adventure.text.Component title() {
                return inventory instanceof CraftInventoryCustom custom ? custom.title() : inventory.getType().defaultTitle(); // Paper
            }
            // Paper end

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

            @Override
            public MenuType getMenuType() {
                return CraftMenuType.minecraftToBukkit(getNotchInventoryType(inventory));
            }

        }, player, id);
    }

    @Override
    public InventoryView getBukkitView() {
        return this.view;
    }

    public static net.minecraft.world.inventory.MenuType getNotchInventoryType(Inventory inventory) {
        final InventoryType type = inventory.getType();
        switch (type) {
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                switch (inventory.getSize()) {
                    case 9:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x1;
                    case 18:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x2;
                    case 27:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x3;
                    case 36:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x4;
                    case 45:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x5;
                    case 54:
                        return net.minecraft.world.inventory.MenuType.GENERIC_9x6;
                    default:
                        throw new IllegalArgumentException("Unsupported custom inventory size " + inventory.getSize());
                }
            default:
                final MenuType menu = type.getMenuType();
                if (menu == null) {
                    return net.minecraft.world.inventory.MenuType.GENERIC_9x3;
                } else {
                    return ((CraftMenuType<?, ?>) menu).getHandle();
                }
        }
    }

    private void setupSlots(Container top, net.minecraft.world.entity.player.Inventory bottom, Player entityhuman) {
        int windowId = -1;
        switch (this.cachedType) {
            case CREATIVE:
                break; // TODO: This should be an error?
            case PLAYER:
            case CHEST:
            case ENDER_CHEST:
            case BARREL:
                this.delegate = new ChestMenu(net.minecraft.world.inventory.MenuType.GENERIC_9x3, windowId, bottom, top, top.getContainerSize() / 9);
                break;
            case DISPENSER:
            case DROPPER:
                this.delegate = new DispenserMenu(windowId, bottom, top);
                break;
            case FURNACE:
                this.delegate = new FurnaceMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case CRAFTING: // TODO: This should be an error?
            case WORKBENCH:
                this.setupWorkbench(top, bottom); // SPIGOT-3812 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case ENCHANTING:
                this.delegate = new EnchantmentMenu(windowId, bottom);
                break;
            case BREWING:
                this.delegate = new BrewingStandMenu(windowId, bottom, top, new io.papermc.paper.inventory.BrewingSimpleContainerData()); // Paper - Add recipeBrewTime
                break;
            case HOPPER:
                this.delegate = new HopperMenu(windowId, bottom, top);
                break;
            case ANVIL:
                this.setupAnvil(top, bottom); // SPIGOT-6783 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case BEACON:
                this.delegate = new BeaconMenu(windowId, bottom);
                break;
            case SHULKER_BOX:
                this.delegate = new ShulkerBoxMenu(windowId, bottom, top);
                break;
            case BLAST_FURNACE:
                this.delegate = new BlastFurnaceMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case LECTERN:
                this.delegate = new LecternMenu(windowId, top, new SimpleContainerData(1), bottom);
                break;
            case SMOKER:
                this.delegate = new SmokerMenu(windowId, bottom, top, new SimpleContainerData(4));
                break;
            case LOOM:
                this.delegate = new LoomMenu(windowId, bottom);
                break;
            case CARTOGRAPHY:
                this.delegate = new CartographyTableMenu(windowId, bottom);
                break;
            case GRINDSTONE:
                this.delegate = new GrindstoneMenu(windowId, bottom);
                break;
            case STONECUTTER:
                this.setupStoneCutter(top, bottom); // SPIGOT-7757 - manual setup required for individual slots
                break;
            case MERCHANT:
                this.delegate = new MerchantMenu(windowId, bottom);
                break;
            case SMITHING:
            case SMITHING_NEW:
                this.setupSmithing(top, bottom); // SPIGOT-6783 - manually set up slots so we can use the delegated inventory and not the automatically created one
                break;
            case CRAFTER:
                this.delegate = new CrafterMenu(windowId, bottom);
                break;
        }

        if (this.delegate != null) {
            this.lastSlots = this.delegate.lastSlots;
            this.slots = this.delegate.slots;
            this.remoteSlots = this.delegate.remoteSlots;
            // Paper start - copy data slots for InventoryView#set/getProperty
            this.dataSlots = this.delegate.dataSlots;
            this.remoteDataSlots = this.delegate.remoteDataSlots;
            // Paper end
        }

        // SPIGOT-4598 - we should still delegate the shift click handler
        switch (this.cachedType) {
            case WORKBENCH:
                this.delegate = new CraftingMenu(windowId, bottom);
                break;
            case ANVIL:
                this.delegate = new AnvilMenu(windowId, bottom);
                break;
        }
    }

    private void setupWorkbench(Container top, Container bottom) {
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

    private void setupAnvil(Container top, Container bottom) {
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

    private void setupSmithing(Container top, Container bottom) {
        // This code copied from ContainerSmithing
        this.addSlot(new Slot(top, 0, 8, 48));
        this.addSlot(new Slot(top, 1, 26, 48));
        this.addSlot(new Slot(top, 2, 44, 48));
        this.addSlot(new Slot(top, 3, 98, 48));

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
        // End copy from ContainerSmithing
    }

    private void setupStoneCutter(Container top, Container bottom) {
        // This code copied from ContainerStonecutter
        this.addSlot(new Slot(top, 0, 20, 33));
        this.addSlot(new Slot(top, 1, 143, 33));

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
        // End copy from ContainerSmithing
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slot) {
        return (this.delegate != null) ? this.delegate.quickMoveStack(player, slot) : ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public net.minecraft.world.inventory.MenuType<?> getType() {
        return CraftContainer.getNotchInventoryType(this.view.getTopInventory());
    }
}
