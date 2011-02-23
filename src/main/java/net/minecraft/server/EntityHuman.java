package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
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
    private boolean sleeping;
    private ChunkCoordinates b;
    private int sleepTicks;
    public float z;
    public float A;
    private int d = 0;
    public EntityFish hookedFish = null;

    public EntityHuman(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        ChunkCoordinates chunkcoordinates = world.l();

        this.c((double) chunkcoordinates.a + 0.5D, (double) (chunkcoordinates.b + 1), (double) chunkcoordinates.c + 0.5D, 0.0F, 0.0F);
        this.health = 20;
        this.P = "humanoid";
        this.O = 180.0F;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public void f_() {
        if (this.E()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.l()) {
                this.a(true, true);
            } else if (!this.world.isStatic && this.world.c()) {
                this.a(false, true);
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.f_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.t();
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
    }

    protected boolean w() {
        return this.health <= 0 || this.E();
    }

    protected void t() {
        this.activeContainer = this.defaultContainer;
    }

    public void x() {
        super.x();
        this.n = this.o;
        this.o = 0.0F;
    }

    protected void c_() {
        if (this.p) {
            ++this.q;
            if (this.q == 8) {
                this.q = 0;
                this.p = false;
            }
        } else {
            this.q = 0;
        }

        this.V = (float) this.q / 8.0F;
    }

    public void q() {
        if (this.world.j == 0 && this.health < 20 && this.ticksLived % 20 * 12 == 0) {
            this.b(1);
        }

        this.inventory.e();
        this.n = this.o;
        super.q();
        float f = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);
        // CraftBukkit -- Math -> TrigMath
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
        this.ae += (f1 - this.ae) * 0.8F;
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
        this.a(0.2F, 0.2F);
        this.a(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612D;
        if (this.name.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }

        this.inventory.g();
        if (entity != null) {
            this.motX = (double) (-MathHelper.b((this.aa + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.a((this.aa + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
    }

    public void c(Entity entity, int i) {
        this.m += i;
    }

    public void y() {
        this.a(this.inventory.a(this.inventory.c, 1), false);
    }

    public void b(ItemStack itemstack) {
        this.a(itemstack, false);
    }

    public void a(ItemStack itemstack, boolean flag) {
        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.p(), this.locZ, itemstack);

            entityitem.c = 40;
            float f = 0.1F;
            float f1;

            if (flag) {
                f1 = this.random.nextFloat() * 0.5F;
                float f2 = this.random.nextFloat() * 3.1415927F * 2.0F;

                entityitem.motX = (double) (-MathHelper.a(f2) * f1);
                entityitem.motZ = (double) (MathHelper.b(f2) * f1);
                entityitem.motY = 0.20000000298023224D;
            } else {
                f = 0.3F;
                entityitem.motX = (double) (-MathHelper.a(this.yaw / 180.0F * 3.1415927F) * MathHelper.b(this.pitch / 180.0F * 3.1415927F) * f);
                entityitem.motZ = (double) (MathHelper.b(this.yaw / 180.0F * 3.1415927F) * MathHelper.b(this.pitch / 180.0F * 3.1415927F) * f);
                entityitem.motY = (double) (-MathHelper.a(this.pitch / 180.0F * 3.1415927F) * f + 0.1F);
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
        }
    }

    protected void a(EntityItem entityitem) {
        this.world.a((Entity) entityitem);
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

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.l("Inventory");

        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.e("Dimension");
        this.sleeping = nbttagcompound.m("Sleeping");
        this.sleepTicks = nbttagcompound.d("SleepTimer");
        if (this.sleeping) {
            this.b = new ChunkCoordinates(MathHelper.b(this.locX), MathHelper.b(this.locY), MathHelper.b(this.locZ));
            this.a(true, true);
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Inventory", (NBTBase) this.inventory.a(new NBTTagList()));
        nbttagcompound.a("Dimension", this.dimension);
        nbttagcompound.a("Sleeping", this.sleeping);
        nbttagcompound.a("SleepTimer", (short) this.sleepTicks);
    }

    public void a(IInventory iinventory) {}

    public void b(int i, int j, int k) {}

    public void b(Entity entity, int i) {}

    public float p() {
        return 0.12F;
    }

    protected void l_() {
        this.height = 1.62F;
    }

    public boolean a(Entity entity, int i) {
        this.at = 0;
        if (this.health <= 0) {
            return false;
        } else {
            if (this.E()) {
                this.a(true, true);
            }

            if (entity instanceof EntityMonster || entity instanceof EntityArrow) {
                if (this.world.j == 0) {
                    i = 0;
                }

                if (this.world.j == 1) {
                    i = i / 3 + 1;
                }

                if (this.world.j == 3) {
                    i = i * 3 / 2;
                }
            }

            return i == 0 ? false : super.a(entity, i);
        }
    }

    protected void c(int i) {
        int j = 25 - this.inventory.f();
        int k = i * j + this.d;

        this.inventory.c(i);
        i = k / 25;
        this.d = k % 25;
        super.c(i);
    }

    public void a(TileEntityFurnace tileentityfurnace) {}

    public void a(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void c(Entity entity) {
        if (!entity.a(this)) {
            ItemStack itemstack = this.z();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.b((EntityLiving) entity);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.A();
                }
            }
        }
    }

    public ItemStack z() {
        return this.inventory.b();
    }

    public void A() {
        this.inventory.a(this.inventory.c, (ItemStack) null);
    }

    public double B() {
        return (double) (this.height - 0.5F);
    }

    public void r() {
        this.q = -1;
        this.p = true;
    }

    public void d(Entity entity) {
        int i = this.inventory.a(entity);

        if (i > 0) {
            // CraftBukkit start
            if (entity instanceof EntityLiving) {
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damager = this.getBukkitEntity();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, i);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    return;
                }

                i = event.getDamage();
            }
            // CraftBukkit end

            entity.a(this, i);
            ItemStack itemstack = this.z();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.A();
                }
            }
        }
    }

    public void a(ItemStack itemstack) {}

    public void C() {
        super.C();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }

    public boolean D() {
        return !this.sleeping && super.D();
    }

    public boolean a(int i, int j, int k) {
        if (!this.E() && this.J()) {
            if (this.world.m.c) {
                return false;
            } else if (this.world.c()) {
                return false;
            } else if (Math.abs(this.locX - (double) i) <= 3.0D && Math.abs(this.locY - (double) j) <= 2.0D && Math.abs(this.locZ - (double) k) <= 3.0D) {
                this.a(0.2F, 0.2F);
                this.height = 0.2F;
                if (this.world.f(i, j, k)) {
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
                    this.a((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
                } else {
                    this.a((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
                }

                this.sleeping = true;
                this.sleepTicks = 0;
                this.b = new ChunkCoordinates(i, j, k);
                this.motX = this.motZ = this.motY = 0.0D;
                if (!this.world.isStatic) {
                    this.world.o();
                }

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void e(int i) {
        this.z = 0.0F;
        this.A = 0.0F;
        switch (i) {
        case 0:
            this.A = -1.8F;
            break;

        case 1:
            this.z = 1.8F;
            break;

        case 2:
            this.A = 1.8F;
            break;

        case 3:
            this.z = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1) {
        this.a(0.6F, 1.8F);
        this.l_();
        ChunkCoordinates chunkcoordinates = this.b;

        if (chunkcoordinates != null && this.world.getTypeId(chunkcoordinates.a, chunkcoordinates.b, chunkcoordinates.c) == Block.BED.id) {
            BlockBed.a(this.world, chunkcoordinates.a, chunkcoordinates.b, chunkcoordinates.c, false);
            ChunkCoordinates chunkcoordinates1 = BlockBed.g(this.world, chunkcoordinates.a, chunkcoordinates.b, chunkcoordinates.c, 0);

            this.a((double) ((float) chunkcoordinates1.a + 0.5F), (double) ((float) chunkcoordinates1.b + this.height + 0.1F), (double) ((float) chunkcoordinates1.c + 0.5F));
        }

        this.sleeping = false;
        if (!this.world.isStatic && flag1) {
            this.world.o();
        }

        if (flag) {
            this.sleepTicks = 0;
        } else {
            this.sleepTicks = 100;
        }
    }

    private boolean l() {
        return this.world.getTypeId(this.b.a, this.b.b, this.b.c) == Block.BED.id;
    }

    public boolean E() {
        return this.sleeping;
    }

    public boolean F() {
        return this.sleeping && this.sleepTicks >= 100;
    }

    public void a(String s) {}
}
