package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MerchantContainer;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.block.entity.CrafterBlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.entity.LecternBlockEntity;
import net.minecraft.world.level.block.entity.ShelfBlockEntity;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.entity.SmokerBlockEntity;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftLegacy;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class CraftInventory implements Inventory {
    protected final Container inventory;

    public CraftInventory(Container inventory) {
        this.inventory = inventory;
    }

    public Container getInventory() {
        return this.inventory;
    }

    @Override
    public int getSize() {
        return this.getInventory().getContainerSize();
    }

    @Override
    public ItemStack getItem(int index) {
        net.minecraft.world.item.ItemStack item = this.getInventory().getItem(index);
        return item.isEmpty() ? null : CraftItemStack.asCraftMirror(item);
    }

    protected ItemStack[] asCraftMirror(List<net.minecraft.world.item.ItemStack> mcItems) {
        int size = mcItems.size();
        ItemStack[] items = new ItemStack[size];

        for (int i = 0; i < size; i++) {
            net.minecraft.world.item.ItemStack mcItem = mcItems.get(i);
            items[i] = (mcItem.isEmpty()) ? null : CraftItemStack.asCraftMirror(mcItem);
        }

        return items;
    }

    @Override
    public ItemStack[] getStorageContents() {
        return this.getContents();
    }

    @Override
    public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
        this.setContents(items);
    }

    @Override
    public ItemStack[] getContents() {
        List<net.minecraft.world.item.ItemStack> mcItems = this.getInventory().getContents();

        return this.asCraftMirror(mcItems);
    }

    @Override
    public void setContents(ItemStack[] items) {
        Preconditions.checkArgument(items.length <= this.getSize(), "Invalid inventory size (%s); expected %s or less", items.length, this.getSize());

        for (int i = 0; i < this.getSize(); i++) {
            if (i >= items.length) {
                this.setItem(i, null);
            } else {
                this.setItem(i, items[i]);
            }
        }
    }

    @Override
    public void setItem(int index, ItemStack item) {
        this.getInventory().setItem(index, CraftItemStack.asNMSCopy(item));
    }

    @Override
    public boolean contains(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        for (ItemStack item : this.getStorageContents()) {
            if (item != null && item.getType() == material) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item) {
        if (item == null) {
            return false;
        }
        for (ItemStack i : this.getStorageContents()) {
            if (item.equals(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Material material, int amount) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        if (amount <= 0) {
            return true;
        }
        for (ItemStack item : this.getStorageContents()) {
            if (item != null && item.getType() == material) {
                if ((amount -= item.getAmount()) <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean contains(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : this.getStorageContents()) {
            if (item.equals(i) && --amount <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAtLeast(ItemStack item, int amount) {
        if (item == null) {
            return false;
        }
        if (amount <= 0) {
            return true;
        }
        for (ItemStack i : this.getStorageContents()) {
            if (item.isSimilar(i) && (amount -= i.getAmount()) <= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public HashMap<Integer, ItemStack> all(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        HashMap<Integer, ItemStack> slots = new HashMap<>();

        ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                slots.put(i, item);
            }
        }
        return slots;
    }

    @Override
    public HashMap<Integer, ItemStack> all(ItemStack item) {
        HashMap<Integer, ItemStack> slots = new HashMap<>();
        if (item != null) {
            ItemStack[] inventory = this.getStorageContents();
            for (int i = 0; i < inventory.length; i++) {
                if (item.equals(inventory[i])) {
                    slots.put(i, inventory[i]);
                }
            }
        }
        return slots;
    }

    @Override
    public int first(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getType() == material) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(ItemStack item) {
        return this.first(item, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        // Paper start
        return first(item, withAmount, getStorageContents());
    }

    private int first(ItemStack item, boolean withAmount, ItemStack[] inventory) {
        // Paper end
        if (item == null) {
            return -1;
        }
        // ItemStack[] inventory = this.getStorageContents(); // Paper - let param deal
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) continue;

            if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = this.getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return this.inventory.isEmpty();
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = this.getStorageContents();
        ItemStack filteredItem = CraftItemStack.asCraftCopy(item);
        if (item == null) {
            return -1;
        }
        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];
            if (cItem != null && cItem.getAmount() < this.getMaxItemStack(cItem) && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        Preconditions.checkArgument(items != null, "items cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<>();

        /* TODO: some optimization
         *  - Create a 'firstPartial' with a 'fromIndex'
         *  - Record the lastPartial per Material
         *  - Cache firstEmpty result
         */

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            Preconditions.checkArgument(item != null, "ItemStack cannot be null");
            while (true) {
                // Do we already have a stack of it?
                int firstPartial = this.firstPartial(item);

                // Drat! no partial stack
                if (firstPartial == -1) {
                    // Find a free spot!
                    int firstFree = this.firstEmpty();

                    if (firstFree == -1) {
                        // No space at all!
                        leftover.put(i, item);
                        break;
                    } else {
                        // More than a single stack!
                        int maxAmount = this.getMaxItemStack(item);
                        if (item.getAmount() > maxAmount) {
                            CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                            stack.setAmount(maxAmount);
                            this.setItem(firstFree, stack);
                            item.setAmount(item.getAmount() - maxAmount);
                        } else {
                            // Just store it
                            this.setItem(firstFree, item);
                            break;
                        }
                    }
                } else {
                    // So, apparently it might only partially fit, well lets do just that
                    ItemStack partialItem = this.getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = this.getMaxItemStack(partialItem);

                    // Check if it fully fits
                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);
                        // To make sure the packet is sent to the client
                        this.setItem(firstPartial, partialItem);
                        break;
                    }

                    // It fits partially
                    partialItem.setAmount(maxAmount);
                    // To make sure the packet is sent to the client
                    this.setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }
        return leftover;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        // Paper start
        return removeItem(false, items);
    }

    @Override
    public HashMap<Integer, ItemStack> removeItemAnySlot(ItemStack... items) {
        return removeItem(true, items);
    }

    private HashMap<Integer, ItemStack> removeItem(boolean searchEntire, ItemStack... items) {
        // Paper end
        Preconditions.checkArgument(items != null, "items cannot be null");
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        // TODO: optimization

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];
            Preconditions.checkArgument(item != null, "ItemStack cannot be null");
            int toDelete = item.getAmount();

            while (true) {
                // Paper start - Allow searching entire contents
                ItemStack[] toSearch = searchEntire ? getContents() : getStorageContents();
                int first = this.first(item, false, toSearch);
                // Paper end

                // Drat! we don't have this type in the inventory
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(i, item);
                    break;
                } else {
                    ItemStack itemStack = this.getItem(first);
                    int amount = itemStack.getAmount();

                    if (amount <= toDelete) {
                        toDelete -= amount;
                        // clear the slot, all used up
                        this.clear(first);
                    } else {
                        // split the stack and store
                        itemStack.setAmount(amount - toDelete);
                        this.setItem(first, itemStack);
                        toDelete = 0;
                    }
                }

                // Bail when done
                if (toDelete <= 0) {
                    break;
                }
            }
        }
        return leftover;
    }

    private int getMaxItemStack(ItemStack itemstack) {
        return Math.min(itemstack.getMaxStackSize(), this.getInventory().getMaxStackSize());
    }

    @Override
    public void remove(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        material = CraftLegacy.fromLegacy(material);
        ItemStack[] items = this.getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getType() == material) {
                this.clear(i);
            }
        }
    }

    @Override
    public void remove(ItemStack item) {
        ItemStack[] items = this.getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].equals(item)) {
                this.clear(i);
            }
        }
    }

    @Override
    public void clear(int index) {
        this.setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < this.getSize(); i++) {
            this.clear(i);
        }
    }
    // Paper start
    @Override
    public int close() {
        int count = this.inventory.getViewers().size();
        com.google.common.collect.Lists.newArrayList(this.inventory.getViewers()).forEach(HumanEntity::closeInventory);
        return count;
    }
    // Paper end

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(this);
    }

    @Override
    public ListIterator<ItemStack> iterator(int index) {
        if (index < 0) {
            index += this.getSize() + 1; // ie, with -1, previous() will return the last element
        }
        return new InventoryIterator(this, index);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return this.inventory.getViewers();
    }

    @Override
    public InventoryType getType() {
        // Thanks to Droppers extending Dispensers, Blast Furnaces & Smokers extending Furnace, order is important.
        if (this.inventory instanceof CraftingContainer) {
            if (this.inventory instanceof CrafterBlockEntity) {
                return InventoryType.CRAFTER;
            } else {
                return this.inventory.getContainerSize() >= 9 ? InventoryType.WORKBENCH : InventoryType.CRAFTING;
            }
        } else if (this.inventory instanceof net.minecraft.world.entity.player.Inventory) {
            return InventoryType.PLAYER;
        } else if (this.inventory instanceof DropperBlockEntity) {
            return InventoryType.DROPPER;
        } else if (this.inventory instanceof DispenserBlockEntity) {
            return InventoryType.DISPENSER;
        } else if (this.inventory instanceof BlastFurnaceBlockEntity) {
            return InventoryType.BLAST_FURNACE;
        } else if (this.inventory instanceof SmokerBlockEntity) {
            return InventoryType.SMOKER;
        } else if (this.inventory instanceof AbstractFurnaceBlockEntity) {
            return InventoryType.FURNACE;
        } else if (this instanceof CraftInventoryEnchanting) {
            return InventoryType.ENCHANTING;
        } else if (this.inventory instanceof BrewingStandBlockEntity) {
            return InventoryType.BREWING;
        } else if (this.inventory instanceof CraftInventoryCustom.MinecraftInventory) {
            return ((CraftInventoryCustom.MinecraftInventory) this.inventory).getType();
            // Paper start
        } else if (this.inventory instanceof io.papermc.paper.inventory.PaperInventoryCustomHolderContainer holderContainer) {
            return holderContainer.getType();
        // Paper end
        } else if (this.inventory instanceof PlayerEnderChestContainer) {
            return InventoryType.ENDER_CHEST;
        } else if (this.inventory instanceof MerchantContainer) {
            return InventoryType.MERCHANT;
        } else if (this instanceof CraftInventoryBeacon) {
            return InventoryType.BEACON;
        } else if (this instanceof CraftInventoryAnvil) {
            return InventoryType.ANVIL;
        } else if (this instanceof CraftInventorySmithing) {
            return InventoryType.SMITHING;
        } else if (this.inventory instanceof Hopper) {
            return InventoryType.HOPPER;
        } else if (this.inventory instanceof ShulkerBoxBlockEntity) {
            return InventoryType.SHULKER_BOX;
        } else if (this.inventory instanceof BarrelBlockEntity) {
            return InventoryType.BARREL;
        } else if (this.inventory instanceof LecternBlockEntity.LecternInventory) {
            return InventoryType.LECTERN;
        } else if (this.inventory instanceof ChiseledBookShelfBlockEntity) {
            return InventoryType.CHISELED_BOOKSHELF;
        } else if (this.inventory instanceof ShelfBlockEntity) {
            return InventoryType.SHELF;
        } else if (this instanceof CraftInventoryLoom) {
            return InventoryType.LOOM;
        } else if (this instanceof CraftInventoryCartography) {
            return InventoryType.CARTOGRAPHY;
        } else if (this instanceof CraftInventoryGrindstone) {
            return InventoryType.GRINDSTONE;
        } else if (this instanceof CraftInventoryStonecutter) {
            return InventoryType.STONECUTTER;
        } else if (this.inventory instanceof ComposterBlock.EmptyContainer || this.inventory instanceof ComposterBlock.InputContainer || this.inventory instanceof ComposterBlock.OutputContainer) {
            return InventoryType.COMPOSTER;
        } else if (this.inventory instanceof JukeboxBlockEntity) {
            return InventoryType.JUKEBOX;
            // Paper start
        } else if (this.inventory instanceof net.minecraft.world.level.block.entity.DecoratedPotBlockEntity) {
            return org.bukkit.event.inventory.InventoryType.DECORATED_POT;
            // Paper end
        } else {
            return InventoryType.CHEST;
        }
    }

    @Override
    public InventoryHolder getHolder() {
        return this.inventory.getOwner();
    }

    // Paper start - getHolder without snapshot
    @Override
    public InventoryHolder getHolder(boolean useSnapshot) {
        return this.inventory instanceof net.minecraft.world.level.block.entity.BlockEntity blockEntity ? blockEntity.getOwner(useSnapshot) : getHolder();
    }
    // Paper end

    @Override
    public int getMaxStackSize() {
        return this.inventory.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int size) {
        this.inventory.setMaxStackSize(size);
    }

    @Override
    public int hashCode() {
        return this.inventory.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj instanceof CraftInventory && ((CraftInventory) obj).inventory.equals(this.inventory);
    }

    @Override
    public Location getLocation() {
        return this.inventory.getLocation();
    }
}
