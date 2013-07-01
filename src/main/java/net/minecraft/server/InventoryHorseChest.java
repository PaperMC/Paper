package net.minecraft.server;

// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class InventoryHorseChest extends InventorySubcontainer {

    public InventoryHorseChest(String s, int i) {
        super(s, false, i);
    }

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public org.bukkit.entity.Player player;
    private int maxStack = MAX_STACK;

    @Override
    public ItemStack[] getContents() {
        return this.items;
    }

    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    @Override
    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }

    @Override
    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player;
    }

    @Override
    public void setMaxStackSize(int size) {
        maxStack = size;
    }

    @Override
    public int getMaxStackSize() {
        return maxStack;
    }
    // CraftBukkit end
}