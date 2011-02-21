package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.craftbukkit.entity.CraftItemDrop;
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
    public byte aq = 0;
    public int ar = 0;
    public float as;
    public float at;
    public boolean au = false;
    public int av = 0;
    public String name;
    public int dimension;
    public double ay;
    public double az;
    public double aA;
    public double aB;
    public double aC;
    public double aD;
    private int a = 0;
    public EntityFish hookedFish = null;

    public EntityHuman(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        this.c((double) world.spawnX + 0.5D, (double) (world.spawnY + 1), (double) world.spawnZ + 0.5D, 0.0F, 0.0F);
        this.health = 20;
        this.aS = "humanoid";
        this.aR = 180.0F;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }

    public void b_() {
        super.b_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.L();
            this.activeContainer = this.defaultContainer;
        }

        this.ay = this.aB;
        this.az = this.aC;
        this.aA = this.aD;
        double d0 = this.locX - this.aB;
        double d1 = this.locY - this.aC;
        double d2 = this.locZ - this.aD;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.ay = this.aB = this.locX;
        }

        if (d2 > d3) {
            this.aA = this.aD = this.locZ;
        }

        if (d1 > d3) {
            this.az = this.aC = this.locY;
        }

        if (d0 < -d3) {
            this.ay = this.aB = this.locX;
        }

        if (d2 < -d3) {
            this.aA = this.aD = this.locZ;
        }

        if (d1 < -d3) {
            this.az = this.aC = this.locY;
        }

        this.aB += d0 * 0.25D;
        this.aD += d2 * 0.25D;
        this.aC += d1 * 0.25D;
    }

    protected void L() {
        this.activeContainer = this.defaultContainer;
    }

    public void D() {
        super.D();
        this.as = this.at;
        this.at = 0.0F;
    }

    protected void d() {
        if (this.au) {
            ++this.av;
            if (this.av == 8) {
                this.av = 0;
                this.au = false;
            }
        } else {
            this.av = 0;
        }

        this.aY = (float) this.av / 8.0F;
    }

    public void o() {
        if (this.world.k == 0 && this.health < 20 && this.ticksLived % 20 * 12 == 0) {
            this.d(1);
        }

        this.inventory.f();
        this.as = this.at;
        super.o();
        float f = MathHelper.a(this.motX * this.motX + this.motZ * this.motZ);
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

        this.at += (f - this.at) * 0.4F;
        this.bh += (f1 - this.bh) * 0.8F;
        if (this.health > 0) {
            List list = this.world.b((Entity) this, this.boundingBox.b(1.0D, 0.0D, 1.0D));

            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (!entity.dead) {
                        this.j(entity);
                    }
                }
            }
        }
    }

    private void j(Entity entity) {
        entity.b(this);
    }

    public void f(Entity entity) {
        super.f(entity);
        this.a(0.2F, 0.2F);
        this.a(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612D;
        if (this.name.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }

        this.inventory.h();
        if (entity != null) {
            this.motX = (double) (-MathHelper.b((this.bd + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.a((this.bd + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
    }

    public void b(Entity entity, int i) {
        this.ar += i;
    }

    public void O() {
        this.a(this.inventory.b(this.inventory.c, 1), false);
    }

    public void b(ItemStack itemstack) {
        this.a(itemstack, false);
    }

    public void a(ItemStack itemstack, boolean flag) {
        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.w(), this.locZ, itemstack);

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
            Player player = (Player)this.getBukkitEntity();
            CraftServer server = ((WorldServer)world).getServer();
            CraftItemDrop drop = new CraftItemDrop(server, entityitem);
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
        NBTTagList nbttaglist = nbttagcompound.k("Inventory");

        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.d("Dimension");
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Inventory", (NBTBase) this.inventory.a(new NBTTagList()));
        nbttagcompound.a("Dimension", this.dimension);
    }

    public void a(IInventory iinventory) {}

    public void a(int i, int j, int k) {}

    public void c(Entity entity, int i) {}

    public float w() {
        return 0.12F;
    }

    public boolean a(Entity entity, int i) {
        this.bw = 0;
        if (this.health <= 0) {
            return false;
        } else {
            if (entity instanceof EntityMonster || entity instanceof EntityArrow) {
                if (this.world.k == 0) {
                    i = 0;
                }

                if (this.world.k == 1) {
                    i = i / 3 + 1;
                }

                if (this.world.k == 3) {
                    i = i * 3 / 2;
                }
            }

            return i == 0 ? false : super.a(entity, i);
        }
    }

    protected void e(int i) {
        int j = 25 - this.inventory.g();
        int k = i * j + this.a;

        this.inventory.c(i);
        i = k / 25;
        this.a = k % 25;
        super.e(i);
    }

    public void a(TileEntityFurnace tileentityfurnace) {}

    public void a(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void g(Entity entity) {
        if (!entity.a(this)) {
            ItemStack itemstack = this.P();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.b((EntityLiving) entity);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.Q();
                }
            }
        }
    }

    public ItemStack P() {
        return this.inventory.e();
    }

    public void Q() {
        this.inventory.a(this.inventory.c, (ItemStack) null);
    }

    public double F() {
        return (double) (this.height - 0.5F);
    }

    public void K() {
        this.av = -1;
        this.au = true;
    }

    public void h(Entity entity) {
        int i = this.inventory.a(entity);

        if (i > 0) {
            // CraftBukkit start
            if(entity instanceof EntityLiving) {
                CraftServer server = ((WorldServer) this.world).getServer();
                org.bukkit.entity.Entity damager = this.getBukkitEntity();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();
                DamageCause damageType = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
                int damageDone = i;

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, damageType, damageDone);
                server.getPluginManager().callEvent(event);

                if (event.isCancelled()){
                    return;
                }

                entity.a(this, event.getDamage());
            } else {
                entity.a(this, i);
            }
            // CraftBukkit end

            ItemStack itemstack = this.P();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity);
                if (itemstack.count <= 0) {
                    itemstack.a(this);
                    this.Q();
                }
            }
        }
    }

    public void a(ItemStack itemstack) {}

    public void q() {
        super.q();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }
}
