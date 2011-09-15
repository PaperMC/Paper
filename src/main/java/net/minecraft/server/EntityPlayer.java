package net.minecraft.server;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.ChunkCompressionThread;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
// CraftBukkit end

public class EntityPlayer extends EntityHuman implements ICrafting {

    public NetServerHandler netServerHandler;
    public MinecraftServer b;
    public ItemInWorldManager itemInWorldManager;
    public double d;
    public double e;
    public List chunkCoordIntPairQueue = new LinkedList();
    public Set playerChunkCoordIntPairs = new HashSet();
    private int cb = -99999999;
    private int cc = -99999999;
    private boolean cd = true;
    private int ce = -99999999;
    private int cf = 60;
    private ItemStack[] cg = new ItemStack[] { null, null, null, null, null};
    private int ch = 0;
    public boolean h;
    public int i;

    public EntityPlayer(MinecraftServer minecraftserver, World world, String s, ItemInWorldManager iteminworldmanager) {
        super(world);
        iteminworldmanager.player = this;
        this.itemInWorldManager = iteminworldmanager;
        ChunkCoordinates chunkcoordinates = world.getSpawn();
        int i = chunkcoordinates.x;
        int j = chunkcoordinates.z;
        int k = chunkcoordinates.y;

        if (!world.worldProvider.e) {
            i += this.random.nextInt(20) - 10;
            k = world.f(i, j);
            j += this.random.nextInt(20) - 10;
        }

        this.setPositionRotation((double) i + 0.5D, (double) k, (double) j + 0.5D, 0.0F, 0.0F);
        this.b = minecraftserver;
        this.bI = 0.0F;
        this.name = s;
        this.height = 0.0F;

        // CraftBukkit start
        this.displayName = this.name;
    }

    public String displayName;
    public org.bukkit.Location compassTarget;
    // CraftBukkit end

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("playerGameType")) {
            this.itemInWorldManager.a(nbttagcompound.e("playerGameType"));
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("playerGameType", this.itemInWorldManager.a());
    }

    public void spawnIn(World world) {
        super.spawnIn(world);
        // CraftBukkit - world fallback code, either respawn location or global spawn
        if (world == null) {
            this.dead = false;
            ChunkCoordinates position = null;
            if (this.spawnWorld != null && !this.spawnWorld.equals("")) {
                CraftWorld cworld = (CraftWorld) Bukkit.getServer().getWorld(this.spawnWorld);
                if (cworld != null && this.getBed() != null) {
                    world = cworld.getHandle();
                    position = EntityHuman.getBed(cworld.getHandle(), this.getBed());
                }
            }
            if (world == null || position == null) {
                world = ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle();
                position = world.getSpawn();
            }
            this.world = world;
            this.setPosition(position.x + 0.5, position.y, position.z + 0.5);
        }
        this.dimension = ((WorldServer) this.world).dimension;
        int oldMode = itemInWorldManager.a();
        this.itemInWorldManager = new ItemInWorldManager((WorldServer) world);
        this.itemInWorldManager.player = this;
        this.itemInWorldManager.a(oldMode);
        // CraftBukkit end
    }

    public void syncInventory() {
        this.activeContainer.a((ICrafting) this);
    }

    public ItemStack[] getEquipment() {
        return this.cg;
    }

    protected void m_() {
        this.height = 0.0F;
    }

    public float t() {
        return 1.62F;
    }

    public void s_() {
        this.itemInWorldManager.c();
        --this.cf;
        this.activeContainer.a();

        for (int i = 0; i < 5; ++i) {
            ItemStack itemstack = this.b(i);

            if (itemstack != this.cg[i]) {
                this.b.getTracker(this.dimension).a(this, new Packet5EntityEquipment(this.id, i, itemstack));
                this.cg[i] = itemstack;
            }
        }
    }

    public ItemStack b(int i) {
        return i == 0 ? this.inventory.getItemInHand() : this.inventory.armor[i - 1];
    }

    public void die(DamageSource damagesource) {
        this.b.serverConfigurationManager.sendAll(new Packet3Chat(damagesource.a((EntityHuman) this)));
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        for (int i = 0; i < this.inventory.items.length; ++i) {
            if (this.inventory.items[i] != null) {
                loot.add(new CraftItemStack(this.inventory.items[i]));
            }
        }

        for (int i = 0; i < this.inventory.armor.length; ++i) {
            if (this.inventory.armor[i] != null) {
                loot.add(new CraftItemStack(this.inventory.armor[i]));
            }
        }

        org.bukkit.entity.Entity bukkitEntity = this.getBukkitEntity();
        CraftWorld bworld = this.world.getWorld();

        EntityDeathEvent event = new EntityDeathEvent(bukkitEntity, loot);
        this.world.getServer().getPluginManager().callEvent(event);

        // CraftBukkit - we clean the player's inventory after the EntityDeathEvent is called so plugins can get the exact state of the inventory.
        for (int i = 0; i < this.inventory.items.length; ++i) {
            this.inventory.items[i] = null;
        }

        for (int i = 0; i < this.inventory.armor.length; ++i) {
            this.inventory.armor[i] = null;
        }

        for (org.bukkit.inventory.ItemStack stack: event.getDrops()) {
            bworld.dropItemNaturally(bukkitEntity.getLocation(), stack);
        }

        this.y();
        // CraftBukkit end
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.cf > 0) {
            return false;
        } else {
            // CraftBukkit - this.b.pvpMode -> this.world.pvpMode
            if (!this.world.pvpMode && damagesource instanceof EntityDamageSource) {
                Entity entity = damagesource.a();

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

            return super.damageEntity(damagesource, i);
        }
    }

    protected boolean n_() {
        return this.b.pvpMode;
    }

    public void c(int i) {
        super.c(i, RegainReason.EATING);
    }

    public void b(boolean flag) {
        super.s_();

        for (int i = 0; i < this.inventory.getSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);

            if (itemstack != null && Item.byId[itemstack.id].i_() && this.netServerHandler.b() <= 2) {
                Packet packet = ((ItemWorldMapBase) Item.byId[itemstack.id]).c(itemstack, this.world, this);

                if (packet != null) {
                    this.netServerHandler.sendPacket(packet);
                }
            }
        }

        if (flag && !this.chunkCoordIntPairQueue.isEmpty()) {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair) this.chunkCoordIntPairQueue.get(0);

            if (chunkcoordintpair != null) {
                boolean flag1 = false;

                if (this.netServerHandler.b() + ChunkCompressionThread.getPlayerQueueSize(this) < 4) { // CraftBukkit - Add check against Chunk Packets in the ChunkCompressionThread.
                    flag1 = true;
                }

                if (flag1) {
                    WorldServer worldserver = this.b.getWorldServer(this.dimension);

                    this.chunkCoordIntPairQueue.remove(chunkcoordintpair);
                    NetServerHandler netserverhandler = this.netServerHandler;

                    int j = chunkcoordintpair.x * 16;
                    int k = chunkcoordintpair.z * 16;

                    worldserver.getClass();
                    Packet51MapChunk packet51mapchunk = new Packet51MapChunk(j, 0, k, 16, 128, 16, worldserver);

                    netserverhandler.sendPacket(packet51mapchunk);
                    int l = chunkcoordintpair.x * 16;

                    j = chunkcoordintpair.z * 16;
                    int i1 = chunkcoordintpair.x * 16 + 16;

                    worldserver.getClass();
                    List list = worldserver.getTileEntities(l, 0, j, i1, 128, chunkcoordintpair.z * 16 + 16);

                    for (int j1 = 0; j1 < list.size(); ++j1) {
                        this.a((TileEntity) list.get(j1));
                    }
                }
            }
        }

        if (this.I) {
            //if (this.b.propertyManager.getBoolean("allow-nether", true)) { // CraftBukkit
                if (this.activeContainer != this.defaultContainer) {
                    this.x();
                }

                if (this.vehicle != null) {
                    this.mount(this.vehicle);
                } else {
                    this.J += 0.0125F;
                    if (this.J >= 1.0F) {
                        this.J = 1.0F;
                        this.H = 10;
                        this.b.serverConfigurationManager.f(this);
                        this.ce = -1;
                        this.cb = -1;
                        this.cc = -1;
                    }
                }

                this.I = false;
            //} // CraftBukkit
        } else {
            if (this.J > 0.0F) {
                this.J -= 0.05F;
            }

            if (this.J < 0.0F) {
                this.J = 0.0F;
            }
        }

        if (this.H > 0) {
            --this.H;
        }

        if (this.health != this.cb || this.cc != this.m.a() || this.m.c() == 0.0F != this.cd) {
            this.netServerHandler.sendPacket(new Packet8UpdateHealth(this.health, this.m.a(), this.m.c()));
            this.cb = this.health;
            this.cc = this.m.a();
            this.cd = this.m.c() == 0.0F;
        }

        if (this.expTotal != this.ce) {
            this.ce = this.expTotal;
            this.netServerHandler.sendPacket(new Packet43SetExperience(this.exp, this.expTotal, this.expLevel));
        }
    }

    private void a(TileEntity tileentity) {
        if (tileentity != null) {
            Packet packet = tileentity.l();

            if (packet != null) {
                this.netServerHandler.sendPacket(packet);
            }
        }
    }

    public void receive(Entity entity, int i) {
        if (!entity.dead) {
            EntityTracker entitytracker = this.b.getTracker(this.dimension);

            if (entity instanceof EntityItem) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityArrow) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }

            if (entity instanceof EntityExperienceOrb) {
                entitytracker.a(entity, new Packet22Collect(entity.id, this.id));
            }
        }

        super.receive(entity, i);
        this.activeContainer.a();
    }

    public void v() {
        if (!this.s) {
            this.t = -1;
            this.s = true;
            EntityTracker entitytracker = this.b.getTracker(this.dimension);

            entitytracker.a(this, new Packet18ArmAnimation(this, 1));
        }
    }

    public void w() {}

    public EnumBedError a(int i, int j, int k) {
        EnumBedError enumbederror = super.a(i, j, k);

        if (enumbederror == EnumBedError.OK) {
            EntityTracker entitytracker = this.b.getTracker(this.dimension);
            Packet17 packet17 = new Packet17(this, 0, i, j, k);

            entitytracker.a(this, packet17);
            this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
            this.netServerHandler.sendPacket(packet17);
        }

        return enumbederror;
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.isSleeping()) {
            EntityTracker entitytracker = this.b.getTracker(this.dimension);

            entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(this, 3));
        }

        super.a(flag, flag1, flag2);
        if (this.netServerHandler != null) {
            this.netServerHandler.a(this.locX, this.locY, this.locZ, this.yaw, this.pitch);
        }
    }

    public void mount(Entity entity) {
        // CraftBukkit start
        this.setPassengerOf(entity);
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

    private void au() {
        this.ch = this.ch % 100 + 1;
    }

    public void b(int i, int j, int k) {
        this.au();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.ch, 1, "Crafting", 9));
        this.activeContainer = new ContainerWorkbench(this.inventory, this.world, i, j, k);
        this.activeContainer.windowId = this.ch;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(IInventory iinventory) {
        this.au();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.ch, 0, iinventory.getName(), iinventory.getSize()));
        this.activeContainer = new ContainerChest(this.inventory, iinventory);
        this.activeContainer.windowId = this.ch;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityFurnace tileentityfurnace) {
        this.au();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.ch, 2, tileentityfurnace.getName(), tileentityfurnace.getSize()));
        this.activeContainer = new ContainerFurnace(this.inventory, tileentityfurnace);
        this.activeContainer.windowId = this.ch;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(TileEntityDispenser tileentitydispenser) {
        this.au();
        this.netServerHandler.sendPacket(new Packet100OpenWindow(this.ch, 3, tileentitydispenser.getName(), tileentitydispenser.getSize()));
        this.activeContainer = new ContainerDispenser(this.inventory, tileentitydispenser);
        this.activeContainer.windowId = this.ch;
        this.activeContainer.a((ICrafting) this);
    }

    public void a(Container container, int i, ItemStack itemstack) {
        if (!(container.b(i) instanceof SlotResult)) {
            if (!this.h) {
                this.netServerHandler.sendPacket(new Packet103SetSlot(container.windowId, i, itemstack));
            }
        }
    }

    public void updateInventory(Container container) {
        this.a(container, container.b());
    }

    public void a(Container container, List list) {
        this.netServerHandler.sendPacket(new Packet104WindowItems(container.windowId, list));
        this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.l()));
    }

    public void a(Container container, int i, int j) {
        this.netServerHandler.sendPacket(new Packet105CraftProgressBar(container.windowId, i, j));
    }

    public void a(ItemStack itemstack) {}

    public void x() {
        this.netServerHandler.sendPacket(new Packet101CloseWindow(this.activeContainer.windowId));
        this.z();
    }

    public void y() {
        if (!this.h) {
            this.netServerHandler.sendPacket(new Packet103SetSlot(-1, -1, this.inventory.l()));
        }
    }

    public void z() {
        this.activeContainer.a((EntityHuman) this);
        this.activeContainer = this.defaultContainer;
    }

    public void a(float f, float f1, boolean flag, boolean flag1, float f2, float f3) {
        this.aP = f;
        this.aQ = f1;
        this.aS = flag;
        this.setSneak(flag1);
        this.pitch = f2;
        this.yaw = f3;
    }

    public void a(Statistic statistic, int i) {
        if (statistic != null) {
            if (!statistic.g) {
                while (i > 100) {
                    this.netServerHandler.sendPacket(new Packet200Statistic(statistic.e, 100));
                    i -= 100;
                }

                this.netServerHandler.sendPacket(new Packet200Statistic(statistic.e, i));
            }
        }
    }

    public void A() {
        if (this.vehicle != null) {
            this.mount(this.vehicle);
        }

        if (this.passenger != null) {
            this.passenger.mount(this);
        }

        if (this.sleeping) {
            this.a(true, false, false);
        }
    }

    public void B() {
        this.cb = -99999999;
    }

    public void a(String s) {
        StatisticStorage statisticstorage = StatisticStorage.a();
        String s1 = statisticstorage.a(s);

        this.netServerHandler.sendPacket(new Packet3Chat(s1));
    }

    protected void C() {
        this.netServerHandler.sendPacket(new Packet38EntityStatus(this.id, (byte) 9));
        super.C();
    }

    public void a(ItemStack itemstack, int i) {
        super.a(itemstack, i);
        if (itemstack != null && itemstack.getItem() != null && itemstack.getItem().b(itemstack) == EnumAnimation.b) {
            EntityTracker entitytracker = this.b.getTracker(this.dimension);

            entitytracker.sendPacketToEntity(this, new Packet18ArmAnimation(this, 5));
        }
    }

    protected void a(MobEffect mobeffect) {
        super.a(mobeffect);
        this.netServerHandler.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void b(MobEffect mobeffect) {
        super.b(mobeffect);
        this.netServerHandler.sendPacket(new Packet41MobEffect(this.id, mobeffect));
    }

    protected void c(MobEffect mobeffect) {
        super.c(mobeffect);
        this.netServerHandler.sendPacket(new Packet42RemoveMobEffect(this.id, mobeffect));
    }

    // CraftBukkit start
    public long timeOffset = 0;
    public boolean relativeTime = true;

    public long getPlayerTime() {
        if (this.relativeTime) {
            // Adds timeOffset to the current server time.
            return this.world.getTime() + this.timeOffset;
        } else {
            // Adds timeOffset to the beginning of this day.
            return this.world.getTime() - (this.world.getTime() % 24000) + this.timeOffset;
        }
    }

    @Override
    public String toString() {
        return super.toString() + "(" + this.name + " at " + this.locX + "," + this.locY + "," + this.locZ + ")";
    }
    // CraftBukkit end
}
