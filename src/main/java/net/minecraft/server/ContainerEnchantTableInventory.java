package net.minecraft.server;

public class ContainerEnchantTableInventory extends ContainerEnchantTableSubcontainer { // CraftBukkit -> public

    public final ContainerEnchantTable enchantTable; // CraftBukkit -> public

    ContainerEnchantTableInventory(ContainerEnchantTable containerenchanttable, String s, int i) {
        super(s, i);
        this.enchantTable = containerenchanttable;
        super.setMaxStackSize(1); // CraftBukkit
    }

    public int getMaxStackSize() {
        return super.getMaxStackSize(); // CraftBukkit
    }

    public void update() {
        super.update();
        this.enchantTable.a((IInventory) this);
    }
}
