package net.minecraft.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    public NetServerHandler netServerHandler;
    public MinecraftServer b;
    public ItemInWorldManager itemInWorldManager;
    public double d;
    public double e;
    public List f = new LinkedList();
    public Set g = new HashSet();
    private int bE = -99999999;
    private int bF = 60;
    private ItemStack[] bG = new ItemStack[] { null, null, null, null, null};
    private int bH = 0;
    public boolean h;

    public EntityPlayer(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(world);
        ChunkCoordinates chunkcoordinates = world.getSpawn();
        int i = chunkcoordinates.x;
        int j = chunkcoordinates.z;
        int k = chunkcoordinates.y;

        if (!world.worldProvider.e) {
            i += this.random.nextInt(20) - 10;
            k = world.e(i, j);
            j += this.random.nextInt(20) - 10;
        }

        this.setPositionRotation((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);
        this.b = minecraftserver;
        this.bm = 0.0F;
        iteminworldmanager.player = this;
        this.name = s;
        this.itemInWorldManager = iteminworldmanager;
        this.height = 0.0F;

        // CraftBukkit start
        this.displayName = this.name;
    }

    public String displayName;
    public Location compassTarget;
    // CraftBukkit end

    public void syncInventory() {
        this.activeContainer.a((ICrafting) this);
    }

    public ItemStack[] getEquipment() {
        return this.bG;
    }

    protected void l_() {
        this.height = 0.0F;
    }

    public float q() {
        return 1.62F;
    }

    public void f_() {
        this.itemInWorldManager.a();
        --this.bF;
        this.activeContainer.a();

        for (int i = 0; i < 5; ++i) {
            ItemStack itemstack = this.b_(i);

            if (itemstack != this.bG[i]) {
                this.b.tracker.a(this, new Packet5EntityEquipment(this.id, i, itemstack));
                this.bG[i] = itemstack;
            }
        }
    }

    public ItemStack b_(int i) {
        return i == 0 ? this.inventory.getItemInHand() : this.inventory.armor[i - 1];
    }

    public void a(Entity entity) {
        // CraftBukkit start
        List<org.bukkit.inventory.ItemStack> loot = new ArrayList<org.bukkit.inventory.ItemStack>();

        for (int i = 0; i < inventory.items.length; ++i) {
            if (inventory.items[i] != null) {
                loot.add(new CraftItemStack(inventory.items[i]));
                inventory.items[i] = null;
            }
        }

        for (int i = 0; i < inventory.armor.length; ++i) {
            if (inventory.armor[i] != null) {
                loot.add(new CraftItemStack(inventory.armor[i]));
                inventory.armor[i] = null;
            }
        }

        CraftEntity craftEntity = (CraftEntity) getBukkitEntity();
        CraftWorld cworld = ((WorldServer) world).getWorld();
        Server server = ((WorldServer) world).getServer();

        EntityDeathEvent event = new EntityDeathEvent(craftEntity, loot);
        server.getPluginManager().callEvent(event);

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            cworld.dropItemNaturally(craftEntity.getLocation(), stack);
        }
        // CraftBukkit end
    }

    public boolean damageEntity(Entity entity, int i) {
        if (this.bF > 0) {
            return false;
        } else {
            if (!this.b.pvpMode) {
                if (entity instanceof EntityHuman) {
                    return false;
                }

                if (entity instanceof EntityArrow) {
                    EntityArrow entityarrow = (EntityArrow) entity;

                    if (entityarrow.shooter instanceof EntityHuman) {
                        return false;
                    }
                }
            }

            return super.damageEntity(entity, i);
        }
    }

    public void b(int i) {
        super.b(i);
    }

    public void a(boolean flag) {
        super.f_();
        if (flag && !this.f.isEmpty()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) this.f.get(0);

            if (chunkcoordintpair != null) {
                boolean flag1 = false;

                if (this.netServerHandler.b() < 2) {
                    flag1 = true;
                }

                if (flag1) {
                    this.f.remove(chunkcoordintpair);

                    // CraftBukkit start
                    this.netServerHandler.sendPacket(new Packet51MapChunk(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, 16, 128, 16, this.world));
                    List list = ((WorldServer) world).getTileEntities(chunkcoordintpair.x * 16, 0, chunkcoordintpair.z * 16, chunkcoordintpair.x * 16 + 16, 128, chunkcoordintpair.z * 16 + 16);
                    // CraftBukkit end

                    for (int i = 0; i < list.size(); ++i) {
                        this.a((TileEntity) list.get(i));
                    }
                }
            }
        }

        if (this.health != this.bE) {
            this.netServerHandler.sendPacket(new Packet8UpdateHealth(this.health));
            this.bE = this.health;
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.e();

            if (packet != null) {
                this.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void r() {
        super.r();
    }

    public void receive(Entity entity, int i) {
        if (!entity.dead) {
            if (entity instanceof EntityItem) {
                this.b.tracker.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityArrow) {
                this.b.tracker.a(entity, new Packet22Collect(entity.id, this.id));
            }
        }

        super.receive(entity, i);
        this.activeContainer.a();
    }

    public void m_() {
        if (!this.p) {
            this.q = -1;
            this.p = true;
            this.b.tracker.a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public void t() {}

    public EnumBedError a(int i, int j, int k) {
        EnumBedError enumbederror = super.a(i, j, k);

        if (enumbederror == EnumBedError.OK) {
            this.b.tracker.a(this, new Packet17(this, 0, i, j, k));
        }

        return enumbederror;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.isSleeping()) {
            this.b.tracker.b(this, new Packet18ArmAnimation(this, 3));
        }

        super.a(flag, flag1, flag2);
        this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // mount(null) doesn't really fly for overloaded methods,
        // so this method is needed

        super.setPassengerOf(entity);
        // CraftBukkit end

        this.netServerHandler.sendPacket(new Packet39AttachEntity(this, this.vehicle));
        this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
    }

    protected void a(double d0, boolean flag) {}

    public void b(double d0, boolean flag) {
        super.a(d0, flag);
    }

    private void aa() {
        this.bH = this.bH % 100 + 1;
    }

    public void b(int i, int j, int k) {
        this.aa();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.bH, 1, "Crafting", 9));
        this.activeContainer = new ContainerWorkbench(this.inventory, this.world, i, j, k);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(IInventory iinventory) {
        this.aa();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.bH, 0, iinventory.getName(), iinventory.getSize()));
        this.activeContainer = new ContainerChest(this.inventory, iinventory);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityFurnace tileentityfurnace) {
        this.aa();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.bH, 2, tileentityfurnace.getName(), tileentityfurnace.getSize()));
        this.activeContainer = new ContainerFurnace(this.inventory, tileentityfurnace);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityDispenser tileentitydispenser) {
        this.aa();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.bH, 3, tileentitydispenser.getName(), tileentitydispenser.getSize()));
        this.activeContainer = new ContainerDispenser(this.inventory, tileentitydispenser);
        this.activeContainer.f = this.bH;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.a(i) instanceof SlotResult)) {
            if (!this.h) {
                this.netServerHandler.sendPacket(new Packet103SetSlot(container.f, i, itemstack));
            }
        }
    }

    public void a(Container container, List list) {
        this.netServerHandler.sendPacket(new Packet104WindowItems(container.f, list));
        this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.j()));
    }

    public void a(Container container, int i, int j) {
        this.netServerHandler.sendPacket(new Packet105CraftProgressBar(container.f, i, j));
    }

    public void a(ItemStack itemstack) {}

    public void u() {
        this.netServerHandler.sendPacket(new Packet101CloseWindow(this.activeContainer.f));
        this.w();
    }

    public void v() {
        if (!this.h) {
            this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.j()));
        }
    }

    public void w() {
        this.activeContainer.a((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1, float f2, float f3) {
        this.au = f;
        this.av = f1;
        this.ax = flag;
        this.setSneak(flag1);
        this.pitch = f2;
        this.yaw = f3;
    }

    // Craftbukkit start
    @Override
    public String toString() {
        return super.toString() + "(" + name + " at " + locX + "," + locY + "," + locZ + ")";
    }
    // Craftbukkit end
}
