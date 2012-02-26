package net.minecraft.server;

class ContainerEnchantTableInventory extends ContainerEnchantTableSubcontainer {

    final ContainerEnchantTable a;

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
