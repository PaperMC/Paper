package net.minecraft.server;

public class TileEntityChest extends TileEntity implements IInventory {

    private ItemStack e[];

    // CraftBukkit start
    public ItemStack[] getContents() {
        return e;
    }
    // CraftBukkit end

    public TileEntityChest() {
        e = new ItemStack[36];
    }

    public int a() {
        return 27;
    }

    public ItemStack a(int i) {
        return e[i];
    }

    public ItemStack a(int i, int j) {
        if (e[i] != null) {
            if (e[i].a <= j) {
                ItemStack itemstack = e[i];

                e[i] = null;
                d();
                return itemstack;
            }
            ItemStack itemstack1 = e[i].a(j);

            if (e[i].a == 0) {
                e[i] = null;
            }
            d();
            return itemstack1;
        } else {
            return null;
        }
    }

    public void a(int i, ItemStack itemstack) {
        e[i] = itemstack;
        if (itemstack != null && itemstack.a > c()) {
            itemstack.a = c();
        }
        d();
    }

    public String b() {
        return "Chest";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.k("Items");

        e = new ItemStack[a()];
        for (int i = 0; i < nbttaglist.b(); i++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(i);
            int j = nbttagcompound1.b("Slot") & 0xff;

            if (j >= 0 && j < e.length) {
                e[j] = new ItemStack(nbttagcompound1);
            }
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < e.length; i++) {
            if (e[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) i);
                e[i].a(nbttagcompound1);
                nbttaglist.a(((NBTBase) (nbttagcompound1)));
            }
        }

        nbttagcompound.a("Items", ((NBTBase) (nbttaglist)));
    }

    public int c() {
        return 64;
    }

    public boolean a_(EntityPlayer entityplayer) {
        if (a.l(b, c, d) != this) {
            return false;
        }
        return entityplayer.d((double) b + 0.5D, (double) c + 0.5D, (double) d + 0.5D) <= 64D;
    }
}
