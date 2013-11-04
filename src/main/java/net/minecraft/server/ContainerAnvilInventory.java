package net.minecraft.server;

// CraftBukkit start
import java.util.List;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.entity.HumanEntity;
// CraftBukkit end

public class ContainerAnvilInventory extends InventorySubcontainer { // CraftBukkit - public

    final ContainerAnvil a;

    // CraftBukkit start
    public List<HumanEntity> transaction = new java.util.ArrayList<HumanEntity>();
    public org.bukkit.entity.Player player;
    private int maxStack = MAX_STACK;

    public ItemStack[] getContents() {
        return this.items;
    }

    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }

    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }

    public List<HumanEntity> getViewers() {
        return transaction;
    }

    public org.bukkit.inventory.InventoryHolder getOwner() {
        return this.player;
    }

    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    // CraftBukkit end

    ContainerAnvilInventory(ContainerAnvil containeranvil, String s, boolean flag, int i) {
        super(s, flag, i);
        this.a = containeranvil;
        this.setMaxStackSize(1); // CraftBukkit
    }

    public void update() {
        super.update();
        this.a.a((IInventory) this);
    }
}
