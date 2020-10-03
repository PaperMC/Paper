package net.minecraft.server;

public class ContainerLectern extends Container {

    private final IInventory inventory;
    private final IContainerProperties containerProperties;

    public ContainerLectern(int i) {
        this(i, new InventorySubcontainer(1), new ContainerProperties(1));
    }

    public ContainerLectern(int i, IInventory iinventory, IContainerProperties icontainerproperties) {
        super(Containers.LECTERN, i);
        a(iinventory, 1);
        a(icontainerproperties, 1);
        this.inventory = iinventory;
        this.containerProperties = icontainerproperties;
        this.a(new Slot(iinventory, 0, 0, 0) {
            @Override
            public void d() {
                super.d();
                ContainerLectern.this.a(this.inventory);
            }
        });
        this.a(icontainerproperties);
    }

    @Override
    public boolean a(EntityHuman entityhuman, int i) {
        int j;

        if (i >= 100) {
            j = i - 100;
            this.a(0, j);
            return true;
        } else {
            switch (i) {
                case 1:
                    j = this.containerProperties.getProperty(0);
                    this.a(0, j - 1);
                    return true;
                case 2:
                    j = this.containerProperties.getProperty(0);
                    this.a(0, j + 1);
                    return true;
                case 3:
                    if (!entityhuman.eJ()) {
                        return false;
                    }

                    ItemStack itemstack = this.inventory.splitWithoutUpdate(0);

                    this.inventory.update();
                    if (!entityhuman.inventory.pickup(itemstack)) {
                        entityhuman.drop(itemstack, false);
                    }

                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public void a(int i, int j) {
        super.a(i, j);
        this.c();
    }

    @Override
    public boolean canUse(EntityHuman entityhuman) {
        return this.inventory.a(entityhuman);
    }
}
