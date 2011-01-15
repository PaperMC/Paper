package net.minecraft.server;

import java.util.*;

public class EntityPlayerMP extends EntityPlayer implements ICrafting {

    public NetServerHandler a;
    public MinecraftServer b;
    public ItemInWorldManager c;
    public double d;
    public double e;
    public List f;
    public Set ak;
    public double al;
    private int bD;
    private int bE;
    private ItemStack bF[] = {
        null, null, null, null, null
    };
    private int bG;
    public boolean am;

    public EntityPlayerMP(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(world);
        f = ((List) (new LinkedList()));
        ak = ((Set) (new HashSet()));
        bD = 0xfa0a1f01;
        bE = 60;
        bG = 0;
        int i = world.m;
        int j = world.o;
        int k = world.n;

        if (!world.q.e) {
            i += W.nextInt(20) - 10;
            k = world.e(i, j);
            j += W.nextInt(20) - 10;
        }
        c((double) i + 0.5D, k, (double) j + 0.5D, 0.0F, 0.0F);
        b = minecraftserver;
        S = 0.0F;
        iteminworldmanager.a = ((EntityPlayer) (this));
        aw = s;
        c = iteminworldmanager;
        H = 0.0F;
    }

    public void l() {
        ap.a(((ICrafting) (this)));
    }

    public ItemStack[] I() {
        return bF;
    }

    public void b_() {
        bE--;
        ap.a();
        for (int i = 0; i < 5; i++) {
            ItemStack itemstack = a(i);

            if (itemstack != bF[i]) {
                b.k.a(((Entity) (this)), ((Packet) (new Packet5PlayerInventory(g, i, itemstack))));
                bF[i] = itemstack;
            }
        }
    }

    public ItemStack a(int i) {
        if (i == 0) {
            return an.e();
        } else {
            return an.b[i - 1];
        }
    }

    public void f(Entity entity) {
        an.h();
    }

    public boolean a(Entity entity, int i) {
        if (bE > 0) {
            return false;
        }
        if (!b.n) {
            if (entity instanceof EntityPlayer) {
                return false;
            }
            if (entity instanceof EntityArrow) {
                EntityArrow entityarrow = (EntityArrow) entity;

                if (entityarrow.b instanceof EntityPlayer) {
                    return false;
                }
            }
        }
        return super.a(entity, i);
    }

    public void d(int i) {
        super.d(i);
    }

    public void n() {
        super.b_();
        ChunkCoordIntPair chunkcoordintpair = null;
        double d1 = 0.0D;

        for (int i = 0; i < f.size(); i++) {
            ChunkCoordIntPair chunkcoordintpair1 = (ChunkCoordIntPair) f.get(i);
            double d2 = chunkcoordintpair1.a(((Entity) (this)));

            if (i == 0 || d2 < d1) {
                chunkcoordintpair = chunkcoordintpair1;
                d1 = chunkcoordintpair1.a(((Entity) (this)));
            }
        }

        if (chunkcoordintpair != null) {
            boolean flag = false;

            if (d1 < 1024D) {
                flag = true;
            }
            if (a.b() < 2) {
                flag = true;
            }
            if (flag) {
                f.remove(((chunkcoordintpair)));
                a.b(((Packet) (new Packet51MapChunk(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, 16, 128, 16, ((World) (b.e))))));
                List list = b.e.d(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, chunkcoordintpair.a * 16 + 16, 128, chunkcoordintpair.b * 16 + 16);

                for (int j = 0; j < list.size(); j++) {
                    a((TileEntity) list.get(j));
                }
            }
        }
        if (aZ != bD) {
            a.b(((Packet) (new Packet8(aZ))));
            bD = aZ;
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.g();

            if (packet != null) {
                a.b(packet);
            }
        }
    }

    public void o() {
        s = t = u = 0.0D;
        bA = false;
        super.o();
    }

    public void c(Entity entity, int i) {
        if (!entity.G) {
            if (entity instanceof EntityItem) {
                b.k.a(entity, ((Packet) (new Packet22Collect(entity.g, g))));
            }
            if (entity instanceof EntityArrow) {
                b.k.a(entity, ((Packet) (new Packet22Collect(entity.g, g))));
            }
        }
        super.c(entity, i);
        ap.a();
    }

    public void K() {
        if (!au) {
            av = -1;
            au = true;
            b.k.a(((Entity) (this)), ((Packet) (new Packet18ArmAnimation(((Entity) (this)), 1))));
        }
    }

    public float w() {
        return 1.62F;
    }

    public void e(Entity entity) {
        // CraftBukkit start
        setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // e(null) doesn't really fly for overloaded methods,
        // so this method is needed

        //CraftBukkit end
        super.setPassengerOf(entity);
        a.b(((Packet) (new Packet39(((Entity) (this)), k))));
        a.a(p, q, r, v, w);
    }

    protected void a(double d1, boolean flag) {}

    public void b(double d1, boolean flag) {
        super.a(d1, flag);
    }

    private void U() {
        bG = bG % 100 + 1;
    }

    public void a(int i, int j, int k) {
        U();
        System.out.println("OPEN_WINDOW for WorkBench");
        a.b(((Packet) (new Packet100(bG, 1, "Crafting", 9))));
        ap = ((CraftingInventoryCB) (new CraftingInventoryWorkbenchCB(an, l, i, j, k)));
        ap.f = bG;
        ap.a(((ICrafting) (this)));
    }

    public void a(IInventory iinventory) {
        U();
        System.out.println("OPEN_WINDOW for Chest");
        a.b(((Packet) (new Packet100(bG, 0, iinventory.b(), iinventory.h_()))));
        ap = ((CraftingInventoryCB) (new CraftingInventoryChestCB(((IInventory) (an)), iinventory)));
        ap.f = bG;
        ap.a(((ICrafting) (this)));
    }

    public void a(TileEntityFurnace tileentityfurnace) {
        U();
        System.out.println("OPEN_WINDOW for Furnace");
        a.b(((Packet) (new Packet100(bG, 2, tileentityfurnace.b(), tileentityfurnace.h_()))));
        ap = ((CraftingInventoryCB) (new CraftingInventoryFurnaceCB(((IInventory) (an)), tileentityfurnace)));
        ap.f = bG;
        ap.a(((ICrafting) (this)));
    }

    public void a(TileEntityDispenser tileentitydispenser) {
        U();
        System.out.println("OPEN_WINDOW for Dispenser");
        a.b(((Packet) (new Packet100(bG, 3, tileentitydispenser.b(), tileentitydispenser.h_()))));
        ap = ((CraftingInventoryCB) (new CraftingInventoryDispenserCB(((IInventory) (an)), tileentitydispenser)));
        ap.f = bG;
        ap.a(((ICrafting) (this)));
    }

    public void a(CraftingInventoryCB craftinginventorycb, int i, ItemStack itemstack) {
        if (craftinginventorycb.a(i) instanceof SlotCrafting) {
            return;
        }
        System.out.print("SET_SLOT for window: "+craftinginventorycb.f+" index: "+i+"and stack: "+itemstack);
        if (am) {
            System.out.println(" -- NOT SEND!");
            return;
        } else {
            System.out.println(" -- SEND!");
            a.b(((Packet) (new Packet103(craftinginventorycb.f, i, itemstack))));
            return;
        }
    }

    public void a(CraftingInventoryCB craftinginventorycb, List list) {
        System.out.println("WINDOW_SLOTS for window: "+craftinginventorycb.f );
        a.b(((Packet) (new Packet104(craftinginventorycb.f, list))));
        System.out.println("SET_SLOT for item in hand: "+i+"and stack: "+an.i());
        a.b(((Packet) (new Packet103(-1, -1, an.i()))));
    }

    public void a(CraftingInventoryCB craftinginventorycb, int i, int j) {
        System.out.println(String.format("UPDATE_PROGRESS_BAR for window: %d [%d;%d]", craftinginventorycb.f, i, j));
        a.b(((Packet) (new Packet105(craftinginventorycb.f, i, j))));
    }

    public void a(ItemStack itemstack) {}

    public void L() {
        a.b(((Packet) (new Packet101(ap.f))));
        N();
    }

    public void M() {
        if (am) {
            return;
        } else {
            a.b(((Packet) (new Packet103(-1, -1, an.i()))));
            return;
        }
    }

    public void N() {
        ap.a(((EntityPlayer) (this)));
        ap = ao;
    }
}