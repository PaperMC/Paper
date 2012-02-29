package net.minecraft.server;

public class ContainerEnchantTableInventory extends ContainerEnchantTableSubcontainer { // CraftBukkit -> public

    public final ContainerEnchantTable a; // CraftBukkit -> public

    ContainerEnchantTableInventory(ContainerEnchantTable containerenchanttable, String s, int i) {
        super(s, i);
        this.a = containerenchanttable;
    }

    public int getMaxStackSize() {
        return 1;
    }

    public void update() {
        super.update();
        this.a.a((IInventory) this);
    }
}
