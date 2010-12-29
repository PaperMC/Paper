package net.minecraft.server;


import java.util.*;

import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;


public class EntityPlayerMP extends EntityPlayer
        implements ICrafting {

    public NetServerHandler a;
    public MinecraftServer b;
    public ItemInWorldManager c;
    public double d;
    public double e;
    public List f;
    public Set aj;
    public double ak;
    public boolean al;
    private int bE;
    private int bF;
    private int bG[] = {
        -1, -1, -1, -1, -1
    };
    private int bH;
    public boolean am;
    private CraftServer server;             // Craftbukkit
    private Location oldLocation = null;    // Craftbukkit

    public EntityPlayerMP(MinecraftServer minecraftserver, World world, String s1, ItemInWorldManager iteminworldmanager) {
        super(world);
        f = new LinkedList();
        aj = new HashSet();
        al = false;
        bE = 0xfa0a1f01;
        bF = 60;
        bH = 0;
        int i = world.m;
        int j = world.o;
        int l = world.n;


        if (!world.q.e) {
            i += W.nextInt(20) - 10;
            l = world.e(i, j);
            j += W.nextInt(20) - 10;
        }
        c((double) i + 0.5D, l, (double) j + 0.5D, 0.0F, 0.0F);
        b = minecraftserver;
        S = 0.0F;
        iteminworldmanager.a = this;
        aw = s1;
        c = iteminworldmanager;
        H = 0.0F;
        
        server = minecraftserver.server; // Craftbukkit
        oldLocation = new Location(server.getWorld(b.e), 0,0,-1); // Craftbukkit
    }
    
    /**
     * Craftbukkit: Overloaded version of b(double,double,double,float, float)
     * 
     * Enables monitoring of PLAYER_MOVE events.
     */
    public void b(double x, double y, double z, float rotation, float pitch) {
        Location newLocation = new Location(server.getWorld(b.e),x,y,z);
        
        // Only send an event if player position has changed.
        if (x != oldLocation.getX() || y != oldLocation.getY() || z != oldLocation.getZ()) {
            PlayerMoveEvent pm = new PlayerMoveEvent(Event.Type.PLAYER_MOVE, server.getPlayer(this), 
                    oldLocation, newLocation);
            
            server.getPluginManager().callEvent(pm);
            if (pm.isCancelled())
                newLocation = pm.getFrom();
        }
        oldLocation = newLocation;
        super.b(newLocation.getX(),newLocation.getY(),newLocation.getZ(),rotation, pitch);
    }

    public void k() {
        ap.a((ICrafting)this);
    }

    public int[] E() {
        return bG;
    }

    public void b_() {
        bF--;
        ap.a();
        for (int i = 0; i < 5; i++) {
            int j = a(i);

            if (j != bG[i]) {
                b.k.a(this, new Packet5PlayerInventory(g, i, j));
                bG[i] = j;
            }
        }

    }

    public int a(int i) {
        if (i == 0) {
            return c(an.e());
        } else {
            return c(an.b[i - 1]);
        }
    }

    private int c(ItemStack itemstack) {
        if (itemstack == null) {
            return -1;
        } else {
            return itemstack.c;
        }
    }

    public void f(Entity entity) {
        an.h();
    }

    public boolean a(Entity entity, int i) {
        if (bF > 0) {
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

    public void c(int i) {
        super.c(i);
    }

    public void F() {
        super.b_();
        ChunkCoordIntPair chunkcoordintpair = null;
        double d1 = 0.0D;

        for (int i = 0; i < f.size(); i++) {
            ChunkCoordIntPair chunkcoordintpair1 = (ChunkCoordIntPair) f.get(i);
            double d2 = chunkcoordintpair1.a(this);

            if (i == 0 || d2 < d1) {
                chunkcoordintpair = chunkcoordintpair1;
                d1 = chunkcoordintpair1.a(this);
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
                f.remove(chunkcoordintpair);
                a.b(new Packet51MapChunk(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, 16, 128, 16, b.e));
                List list = b.e.d(chunkcoordintpair.a * 16, 0, chunkcoordintpair.b * 16, chunkcoordintpair.a * 16 + 16, 128, chunkcoordintpair.b * 16 + 16);

                for (int j = 0; j < list.size(); j++) {
                    a((TileEntity) list.get(j));
                }

            }
        }
        if (ba != bE) {
            a.b(new Packet8(ba));
            bE = ba;
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.f();

            if (packet != null) {
                a.b(packet);
            }
        }
    }

    public void G() {
        s = t = u = 0.0D;
        bB = false;
        super.G();
    }

    public void c(Entity entity, int i) {
        if (!entity.G) {
            if (entity instanceof EntityItem) {
                b.k.a(entity, new Packet22Collect(entity.g, g));
            }
            if (entity instanceof EntityArrow) {
                b.k.a(entity, new Packet22Collect(entity.g, g));
            }
        }
        super.c(entity, i);
        ap.a();
    }

    public void H() {
        if (!au) {
            av = -1;
            au = true;
            b.k.a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public float s() {
        return 1.62F;
    }

    public void e(Entity entity) {
        super.e(entity);
        a.b(new Packet39(this, k));
        a.a(p, q, r, v, w);
    }

    protected void a(double d1, boolean flag) {}

    public void b(double d1, boolean flag) {
        super.a(d1, flag);
    }

    public boolean p() {
        return al;
    }

    private void R() {
        bH = bH % 100 + 1;
    }

    public void a(int i, int j, int l) {
        R();
        a.b(new Packet100(bH, 1, "Crafting", 9));
        ap = new CraftingInventoryWorkbenchCB(an, this.l, i, j, l);
        ap.f = bH;
        ap.a((ICrafting)this);
    }

    public void a(IInventory iinventory) {
        R();
        a.b(new Packet100(bH, 0, iinventory.b(), iinventory.a()));
        ap = new CraftingInventoryChestCB(an, iinventory);
        ap.f = bH;
        ap.a((ICrafting)this);
    }

    public void a(TileEntityFurnace tileentityfurnace) {
        R();
        a.b(new Packet100(bH, 2, tileentityfurnace.b(), tileentityfurnace.a()));
        ap = new CraftingInventoryFurnaceCB(an, tileentityfurnace);
        ap.f = bH;
        ap.a((ICrafting)this);
    }

    public void a(CraftingInventoryCB craftinginventorycb, int i, ItemStack itemstack) {
        if (craftinginventorycb.a(i) instanceof SlotCrafting) {
            return;
        }
        if (am) {
            return;
        } else {
            a.b(new Packet103(craftinginventorycb.f, i, itemstack));
            return;
        }
    }

    public void a(CraftingInventoryCB craftinginventorycb, List list) {
        a.b(new Packet104(craftinginventorycb.f, list));
        a.b(new Packet103(-1, -1, an.i()));
    }

    public void a(CraftingInventoryCB craftinginventorycb, int i, int j) {
        a.b(new Packet105(craftinginventorycb.f, i, j));
    }

    public void a(ItemStack itemstack) {}

    public void I() {
        a.b(new Packet101(ap.f));
        K();
    }

    public void J() {
        if (am) {
            return;
        } else {
            a.b(new Packet103(-1, -1, an.i()));
            return;
        }
    }

    public void K() {
        ap.a((ICrafting)this);
        ap = ao;
    }
}
