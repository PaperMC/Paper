package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
// CraftBukkit end

public abstract class EntityHuman extends EntityLiving {

    public InventoryPlayer inventory = new InventoryPlayer(this);
    public Container defaultContainer;
    public Container activeContainer;
    public byte l = 0;
    public int m = 0;
    public float n;
    public float o;
    public boolean p = false;
    public int q = 0;
    public String name;
    public int dimension;
    public double t;
    public double u;
    public double v;
    public double w;
    public double x;
    public double y;
    // CraftBukkit start
    public boolean sleeping;
    public boolean fauxSleeping;
    // CraftBukkit end
    private ChunkCoordinates a;
    public int sleepTicks; // CraftBukkit - private -> public
    public float A;
    public float B;
    private ChunkCoordinates c;
    private ChunkCoordinates d;
    public int C = 20;
    protected boolean D = false;
    public float E;
    private int e = 0;
    public EntityFish hookedFish = null;

    public EntityHuman(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawn();

        this.setPositionRotation((double) chunkcoordinates.x + 0.5D, (double) (chunkcoordinates.y + 1), (double) chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
        this.health = 20;
        this.T = "humanoid";
        this.S = 180.0F;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void p_() {
        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.o()) {
                this.a(true, true, false);
            } else if (!this.world.isStatic && this.world.d()) {
                this.a(false, true, true);
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.p_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.x();
            this.activeContainer = this.defaultContainer;
        }

        this.t = this.w;
        this.u = this.x;
        this.v = this.y;
        double d0 = this.locX - this.w;
        double d1 = this.locY - this.x;
        double d2 = this.locZ - this.y;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.t = this.w = this.locX;
        }

        if (d2 > d3) {
            this.v = this.y = this.locZ;
        }

        if (d1 > d3) {
            this.u = this.x = this.locY;
        }

        if (d0 < -d3) {
            this.t = this.w = this.locX;
        }

        if (d2 < -d3) {
            this.v = this.y = this.locZ;
        }

        if (d1 < -d3) {
            this.u = this.x = this.locY;
        }

        this.w += d0 * 0.25D;
        this.y += d2 * 0.25D;
        this.x += d1 * 0.25D;
        this.a(StatisticList.k, 1);
        if (this.vehicle == null) {
            this.d = null;
        }
    }

    protected boolean A() {
        return this.health <= 0 || this.isSleeping();
    }

    protected void x() {
        this.activeContainer = this.defaultContainer;
    }

    public void B() {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        super.B();
        this.n = this.o;
        this.o = 0.0F;
        this.i(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    protected void c_() {
        if (this.p) {
            ++this.q;
            if (this.q >= 8) {
                this.q = 0;
                this.p = false;
            }
        } else {
            this.q = 0;
        }

        this.Z = (float) this.q / 8.0F;
    }

    public void u() {
        if (this.world.spawnMonsters == 0 && this.health < 20 && this.ticksLived % 20 * 12 == 0) {
            this.b(1);
        }

        this.inventory.f();
        this.n = this.o;
        super.u();
        float f = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);
        // CraftBukkit - Math -> TrigMath
        float f1 = (float) TrigMath.atan(-this.motY * 0.20000000298023224D) * 15.0F;

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.onGround || this.health <= 0) {
            f = 0.0F;
        }

        if (this.onGround || this.health <= 0) {
            f1 = 0.0F;
        }

        this.o += (f - this.o) * 0.4F;
        this.ai += (f1 - this.ai) * 0.8F;
        if (this.health > 0) {
            List list = this.world.b((Entity) this, this.boundingBox.b(1.0D, 0.0D, 1.0D));

            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (!entity.dead) {
                        this.i(entity);
                    }
                }
            }
        }
    }

    private void i(Entity entity) {
        entity.b(this);
    }

    public void a(Entity entity) {
        super.a(entity);
        this.b(0.2F, 0.2F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612D;
        if (this.name.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }

        this.inventory.h();
        if (entity != null) {
            this.motX = (double) (-MathHelper.cos((this.ae + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.sin((this.ae + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
        this.a(StatisticList.y, 1);
    }

    public void c(Entity entity, int i) {
        this.m += i;
        if (entity instanceof EntityHuman) {
            this.a(StatisticList.A, 1);
        } else {
            this.a(StatisticList.z, 1);
        }
    }

    public void C() {
        this.a(this.inventory.a(this.inventory.itemInHandIndex, 1), false);
    }

    public void b(ItemStack itemstack) {
        this.a(itemstack, false);
    }

    public void a(ItemStack itemstack, boolean flag) {
        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.s(), this.locZ, itemstack);

            entityitem.pickupDelay = 40;
            float f = 0.1F;
            float f1;

            if (flag) {
                f1 = this.random.nextFloat() * 0.5F;
                float f2 = this.random.nextFloat() * 3.1415927F * 2.0F;

                entityitem.motX = (double) (-MathHelper.sin(f2) * f1);
                entityitem.motZ = (double) (MathHelper.cos(f2) * f1);
                entityitem.motY = 0.20000000298023224D;
            } else {
                f = 0.3F;
                entityitem.motX = (double) (-MathHelper.sin(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
                entityitem.motZ = (double) (MathHelper.cos(this.yaw / 180.0F * 3.1415927F) * MathHelper.cos(this.pitch / 180.0F * 3.1415927F) * f);
                entityitem.motY = (double) (-MathHelper.sin(this.pitch / 180.0F * 3.1415927F) * f + 0.1F);
                f = 0.02F;
                f1 = this.random.nextFloat() * 3.1415927F * 2.0F;
                f *= this.random.nextFloat();
                entityitem.motX += Math.cos((double) f1) * (double) f;
                entityitem.motY += (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.1F);
                entityitem.motZ += Math.sin((double) f1) * (double) f;
            }

            // CraftBukkit start
            Player player = (Player) this.getBukkitEntity();
            CraftServer server = ((WorldServer) world).getServer();
            CraftItem drop = new CraftItem(server, entityitem);

            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            server.getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                org.bukkit.inventory.ItemStack stack = drop.getItemStack();
                stack.setAmount(1);
                player.getInventory().addItem(stack);

                return;
            }
            // CraftBukkit end

            this.a(entityitem);
            this.a(StatisticList.v, 1);
        }
    }

    protected void a(EntityItem entityitem) {
        this.world.addEntity(entityitem);
    }

    public float a(Block block) {
        float f = this.inventory.a(block);

        if (this.a(Material.WATER)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean b(Block block) {
        return this.inventory.b(block);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Inventory");

        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.e("Dimension");
        this.sleeping = nbttagcompound.m("Sleeping");
        this.sleepTicks = nbttagcompound.d("SleepTimer");
        if (this.sleeping) {
            this.a = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
            this.a(true, true, false);
        }

        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            this.c = new ChunkCoordinates(nbttagcompound.e("SpawnX"), nbttagcompound.e("SpawnY"), nbttagcompound.e("SpawnZ"));
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Inventory", (NBTBase) this.inventory.a(new NBTTagList()));
        nbttagcompound.a("Dimension", this.dimension);
        nbttagcompound.a("Sleeping", this.sleeping);
        nbttagcompound.a("SleepTimer", (short) this.sleepTicks);
        if (this.c != null) {
            nbttagcompound.a("SpawnX", this.c.x);
            nbttagcompound.a("SpawnY", this.c.y);
            nbttagcompound.a("SpawnZ", this.c.z);
        }
    }

    public void a(IInventory iinventory) {}

    public void b(int i, int j, int k) {}

    public void receive(Entity entity, int i) {}

    public float s() {
        return 0.12F;
    }

    protected void j_() {
        this.height = 1.62F;
    }

    public boolean damageEntity(Entity entity, int i) {
        this.ax = 0;
        if (this.health <= 0) {
            return false;
        } else {
            if (this.isSleeping()) {
                this.a(true, true, false);
            }

            if (entity instanceof EntityMonster || entity instanceof EntityArrow) {
                if (this.world.spawnMonsters == 0) {
                    i = 0;
                }

                if (this.world.spawnMonsters == 1) {
                    i = i / 3 + 1;
                }

                if (this.world.spawnMonsters == 3) {
                    i = i * 3 / 2;
                }
            }

            if (i == 0) {
                return false;
            } else {
                Object object = entity;

                if (entity instanceof EntityArrow && ((EntityArrow) entity).shooter != null) {
                    object = ((EntityArrow) entity).shooter;
                }

                if (object instanceof EntityLiving) {
                    // CraftBukkit start - this is here instead of EntityMonster because EntityLiving(s) that aren't monsters
                    // also damage the player in this way. For example, EntitySlime.
                    CraftServer server = ((WorldServer) this.world).getServer();
                    org.bukkit.entity.Entity damager = ((Entity) object).getBukkitEntity();
                    org.bukkit.entity.Entity damagee = this.getBukkitEntity();
                    DamageCause damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
                    EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, i);
                    server.getPluginManager().callEvent(event);

                    if (event.isCancelled() || event.getDamage() == 0) {
                        return false;
                    }

                    i = event.getDamage();
                    // CraftBukkit end

                    this.a((EntityLiving) object, false);
                }

                this.a(StatisticList.x, i);
                return super.damageEntity(entity, i);
            }
        }
    }

    protected boolean t() {
        return false;
    }

    protected void a(EntityLiving entityliving, boolean flag) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.m_() && this.name.equals(entitywolf.x())) {
                    return;
                }
            }

            if (!(entityliving instanceof EntityHuman) || this.t()) {
                List list = this.world.a(EntityWolf.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    EntityWolf entitywolf1 = (EntityWolf) entity;

                    if (entitywolf1.m_() && entitywolf1.D() == null && this.name.equals(entitywolf1.x()) && (!flag || !entitywolf1.isSitting())) {
                        entitywolf1.setSitting(false);
                        entitywolf1.c(entityliving);
                    }
                }
            }
        }
    }

    protected void c(int i) {
        int j = 25 - this.inventory.g();
        int k = i * j + this.e;

        this.inventory.c(i);
        i = k / 25;
        this.e = k % 25;
        super.c(i);
    }

    public void a(TileEntityFurnace tileentityfurnace) {}

    public void a(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void c(Entity entity) {
        if (!entity.a(this)) {
            ItemStack itemstack = this.D();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.E();
                }
            }
        }
    }

    public ItemStack D() {
        return this.inventory.getItemInHand();
    }

    public void E() {
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack) null);
    }

    public double F() {
        return (double) (this.height - 0.5F);
    }

    public void k_() {
        this.q = -1;
        this.p = true;
    }

    public void d(Entity entity) {
        int i = this.inventory.a(entity);

        if (i > 0) {
            if (this.motY < 0.0D) {
                ++i;
            }

            // CraftBukkit start
            if (entity instanceof EntityLiving) {
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damager = this.getBukkitEntity();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, i);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled() || event.getDamage() == 0) {
                    return;
                }

                i = event.getDamage();
            }
            // CraftBukkit end

            entity.damageEntity(this, i);
            ItemStack itemstack = this.D();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity, this);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.E();
                }
            }

            if (entity instanceof EntityLiving) {
                if (entity.Q()) {
                    this.a((EntityLiving) entity, true);
                }

                this.a(StatisticList.w, i);
            }
        }
    }

    public void a(ItemStack itemstack) {}

    public void die() {
        super.die();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }

    public boolean H() {
        return !this.sleeping && super.H();
    }

    public EnumBedError a(int i, int j, int k) {
        if (!this.isSleeping() && this.Q()) {
            if (this.world.worldProvider.c) {
                return EnumBedError.NOT_POSSIBLE_HERE;
            } else if (this.world.d()) {
                return EnumBedError.NOT_POSSIBLE_NOW;
            } else if (Math.abs(this.locX - (double) i) <= 3.0D && Math.abs(this.locY - (double) j) <= 2.0D && Math.abs(this.locZ - (double) k) <= 3.0D) {
                // CraftBukkit start
                if (this.getBukkitEntity() instanceof Player) {
                    Player player = (Player) this.getBukkitEntity();
                    CraftServer server = ((WorldServer) world).getServer();
                    org.bukkit.block.Block bed = ((WorldServer) world).getWorld().getBlockAt(i, j, k);
                    PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
                    server.getPluginManager().callEvent(event);

                    if (event.isCancelled()) {
                        return EnumBedError.OTHER_PROBLEM;
                    }
                }
                // CraftBukkit end

                this.b(0.2F, 0.2F);
                this.height = 0.2F;
                if (this.world.isLoaded(i, j, k)) {
                    int l = this.world.getData(i, j, k);
                    int i1 = BlockBed.c(l);
                    float f = 0.5F;
                    float f1 = 0.5F;

                    switch (i1) {
                    case 0:
                        f1 = 0.9F;
                        break;

                    case 1:
                        f = 0.1F;
                        break;

                    case 2:
                        f1 = 0.1F;
                        break;

                    case 3:
                        f = 0.9F;
                    }

                    this.e(i1);
                    this.setPosition((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
                } else {
                    this.setPosition((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
                }

                this.sleeping = true;
                this.sleepTicks = 0;
                this.a = new ChunkCoordinates(i, j, k);
                this.motX = this.motZ = this.motY = 0.0D;
                if (!this.world.isStatic) {
                    this.world.everyoneSleeping();
                }

                return EnumBedError.OK;
            } else {
                return EnumBedError.TOO_FAR_AWAY;
            }
        } else {
            return EnumBedError.OTHER_PROBLEM;
        }
    }

    private void e(int i) {
        this.A = 0.0F;
        this.B = 0.0F;
        switch (i) {
        case 0:
            this.B = -1.8F;
            break;

        case 1:
            this.A = 1.8F;
            break;

        case 2:
            this.B = 1.8F;
            break;

        case 3:
            this.A = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        this.b(0.6F, 1.8F);
        this.j_();
        ChunkCoordinates chunkcoordinates = this.a;
        ChunkCoordinates chunkcoordinates1 = this.a;

        if (chunkcoordinates != null && this.world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Block.BED.id) {
            BlockBed.a(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
            chunkcoordinates1 = BlockBed.g(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
            if (chunkcoordinates1 == null) {
                chunkcoordinates1 = new ChunkCoordinates(chunkcoordinates.x, chunkcoordinates.y + 1, chunkcoordinates.z);
            }

            this.setPosition((double) ((float) chunkcoordinates1.x + 0.5F), (double) ((float) chunkcoordinates1.y + this.height + 0.1F), (double) ((float) chunkcoordinates1.z + 0.5F));
        }

        this.sleeping = false;
        if (!this.world.isStatic && flag1) {
            this.world.everyoneSleeping();
        }

        // CraftBukkit start
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            CraftServer server = ((WorldServer) world).getServer();

            org.bukkit.block.Block bed;
            if (chunkcoordinates != null) {
                bed = ((WorldServer) world).getWorld().getBlockAt(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
            } else {
                bed = ((WorldServer) world).getWorld().getBlockAt(player.getLocation());
            }

            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            server.getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        if (flag) {
            this.sleepTicks = 0;
        } else {
            this.sleepTicks = 100;
        }

        if (flag2) {
            this.a(this.a);
        }
    }

    private boolean o() {
        return this.world.getTypeId(this.a.x, this.a.y, this.a.z) == Block.BED.id;
    }

    public static ChunkCoordinates getBed(World world, ChunkCoordinates chunkcoordinates) {
        IChunkProvider ichunkprovider = world.o();

        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
        if (world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) != Block.BED.id) {
            return null;
        } else {
            ChunkCoordinates chunkcoordinates1 = BlockBed.g(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);

            return chunkcoordinates1;
        }
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public boolean isDeeplySleeping() {
        return this.sleeping && this.sleepTicks >= 100;
    }

    public void a(String s) {}

    public ChunkCoordinates K() {
        return this.c;
    }

    public void a(ChunkCoordinates chunkcoordinates) {
        if (chunkcoordinates != null) {
            this.c = new ChunkCoordinates(chunkcoordinates);
        } else {
            this.c = null;
        }
    }

    public void a(Statistic statistic) {
        this.a(statistic, 1);
    }

    public void a(Statistic statistic, int i) {}

    protected void L() {
        super.L();
        this.a(StatisticList.u, 1);
    }

    public void a(float f, float f1) {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        super.a(f, f1);
        this.h(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    private void h(double d0, double d1, double d2) {
        if (this.vehicle == null) {
            int i;

            if (this.a(Material.WATER)) {
                i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.q, i);
                }
            } else if (this.aa()) {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                }
            } else if (this.p()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.o, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.l, i);
                }
            } else {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.p, i);
                }
            }
        }
    }

    private void i(double d0, double d1, double d2) {
        if (this.vehicle != null) {
            int i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.vehicle instanceof EntityMinecart) {
                    this.a(StatisticList.r, i);
                    if (this.d == null) {
                        this.d = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    } else if (this.d.a(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000.0D) {
                        this.a(AchievementList.q, 1);
                    }
                } else if (this.vehicle instanceof EntityBoat) {
                    this.a(StatisticList.s, i);
                } else if (this.vehicle instanceof EntityPig) {
                    this.a(StatisticList.t, i);
                }
            }
        }
    }

    protected void a(float f) {
        if (f >= 2.0F) {
            this.a(StatisticList.n, (int) Math.round((double) f * 100.0D));
        }

        super.a(f);
    }

    public void a(EntityLiving entityliving) {
        if (entityliving instanceof EntityMonster) {
            this.a((Statistic) AchievementList.s);
        }
    }

    public void M() {
        if (this.C > 0) {
            this.C = 10;
        } else {
            this.D = true;
        }
    }
}
