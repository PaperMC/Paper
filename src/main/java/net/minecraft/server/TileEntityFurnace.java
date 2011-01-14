package net.minecraft.server;

public class TileEntityFurnace extends TileEntity implements IInventory {

    private ItemStack h[];
    public int e;
    public int f;
    public int g;

    // CraftBukkit start
    public ItemStack[] getContents() {
        return h;
    }
    // CraftBukkit end

    public TileEntityFurnace() {
        h = new ItemStack[3];
        e = 0;
        f = 0;
        g = 0;
    }

    public int h_() {
        return h.length;
    }

    public ItemStack a(int j) {
        return h[j];
    }

    public ItemStack b(int j, int k) {
        if (h[j] != null) {
            if (h[j].a <= k) {
                ItemStack itemstack = h[j];

                h[j] = null;
                return itemstack;
            }
            ItemStack itemstack1 = h[j].a(k);

            if (h[j].a == 0) {
                h[j] = null;
            }
            return itemstack1;
        } else {
            return null;
        }
    }

    public void a(int j, ItemStack itemstack) {
        h[j] = itemstack;
        if (itemstack != null && itemstack.a > c()) {
            itemstack.a = c();
        }
    }

    public String b() {
        return "Furnace";
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.k("Items");

        h = new ItemStack[h_()];
        for (int j = 0; j < nbttaglist.b(); j++) {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.a(j);
            byte byte0 = nbttagcompound1.b("Slot");

            if (byte0 >= 0 && byte0 < h.length) {
                h[byte0] = new ItemStack(nbttagcompound1);
            }
        }

        e = ((int) (nbttagcompound.c("BurnTime")));
        g = ((int) (nbttagcompound.c("CookTime")));
        f = a(h[1]);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("BurnTime", (short) e);
        nbttagcompound.a("CookTime", (short) g);
        NBTTagList nbttaglist = new NBTTagList();

        for (int j = 0; j < h.length; j++) {
            if (h[j] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();

                nbttagcompound1.a("Slot", (byte) j);
                h[j].a(nbttagcompound1);
                nbttaglist.a(((NBTBase) (nbttagcompound1)));
            }
        }

        nbttagcompound.a("Items", ((NBTBase) (nbttaglist)));
    }

    public int c() {
        return 64;
    }

    public boolean e() {
        return e > 0;
    }

    public void f() {
        boolean flag = e > 0;
        boolean flag1 = false;

        if (e > 0) {
            e--;
        }
        if (!a.z) {
            if (e == 0 && i()) {
                f = e = a(h[1]);
                if (e > 0) {
                    flag1 = true;
                    if (h[1] != null) {
                        h[1].a--;
                        if (h[1].a == 0) {
                            h[1] = null;
                        }
                    }
                }
            }
            if (e() && i()) {
                g++;
                if (g == 200) {
                    g = 0;
                    h();
                    flag1 = true;
                }
            } else {
                g = 0;
            }
            if (flag != (e > 0)) {
                flag1 = true;
                BlockFurnace.a(e > 0, a, b, c, d);
            }
        }
        if (flag1) {
            d();
        }
    }

    private boolean i() {
        if (h[0] == null) {
            return false;
        }
        ItemStack itemstack = FurnaceRecipes.a().a(h[0].a().ba);

        if (itemstack == null) {
            return false;
        }
        if (h[2] == null) {
            return true;
        }
        if (!h[2].a(itemstack)) {
            return false;
        }
        if (h[2].a < c() && h[2].a < h[2].b()) {
            return true;
        }
        return h[2].a < itemstack.b();
    }

    public void h() {
        if (!i()) {
            return;
        }
        ItemStack itemstack = FurnaceRecipes.a().a(h[0].a().ba);

        if (h[2] == null) {
            h[2] = itemstack.j();
        } else if (h[2].c == itemstack.c) {
            h[2].a++;
        }
        h[0].a--;
        if (h[0].a <= 0) {
            h[0] = null;
        }
    }

    private int a(ItemStack itemstack) {
        if (itemstack == null) {
            return 0;
        }
        int j = itemstack.a().ba;

        if (j < 256 && Block.m[j].bt == Material.c) {
            return 300;
        }
        if (j == Item.B.ba) {
            return 100;
        }
        if (j == Item.k.ba) {
            return 1600;
        }
        return j != Item.aw.ba ? 0 : 20000;
    }

    public boolean a_(EntityPlayer entityplayer) {
        if (a.m(b, c, d) != this) {
            return false;
        }
        return entityplayer.d((double) b + 0.5D, (double) c + 0.5D, (double) d + 0.5D) <= 64D;
    }
}