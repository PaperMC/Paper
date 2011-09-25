package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
// CraftBukkit end

public abstract class EntityHuman extends EntityLiving {

    public InventoryPlayer inventory = new InventoryPlayer(this);
    public Container defaultContainer;
    public Container activeContainer;
    protected FoodMetaData foodData = new FoodMetaData();
    protected int n = 0;
    public byte o = 0;
    public int p = 0;
    public float q;
    public float r;
    public boolean s = false;
    public int t = 0;
    public String name;
    public int dimension;
    public int w = 0;
    public double x;
    public double y;
    public double z;
    public double A;
    public double B;
    public double C;
    // CraftBukkit start
    public boolean sleeping;
    public boolean fauxSleeping;
    public String spawnWorld = "";
    public int sleepTicks;
    // CraftBukkit end
    public ChunkCoordinates E;
    public float F;
    public float G;
    private ChunkCoordinates b;
    private ChunkCoordinates c;
    public int H = 20;
    protected boolean I = false;
    public float J;
    public PlayerAbilities abilities = new PlayerAbilities();
    public int exp;
    public int expLevel;
    public int expTotal;
    private ItemStack d;
    private int e;
    protected float O = 0.1F;
    protected float P = 0.02F;
    private int f = 0;
    public EntityFishingHook hookedFish = null;

    public EntityHuman(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawn();

        this.setPositionRotation((double) chunkcoordinates.x + 0.5D, (double) (chunkcoordinates.y + 1), (double) chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
        this.health = 20;
        this.ae = "humanoid";
        this.ad = 180.0F;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    public boolean o_() {
        return this.d != null;
    }

    public void E() {
        if (this.d != null) {
            this.d.a(this.world, this, this.e);
        }

        this.F();
    }

    public void F() {
        this.d = null;
        this.e = 0;
        if (!this.world.isStatic) {
            this.h(false);
        }
    }

    public boolean G() {
        return this.o_() && Item.byId[this.d.id].b(this.d) == EnumAnimation.c;
    }

    public void s_() {
        if (this.d != null) {
            ItemStack itemstack = this.inventory.getItemInHand();

            if (itemstack != this.d) {
                this.F();
            } else {
                if (this.e <= 25 && this.e % 4 == 0) {
                    this.b(itemstack, 5);
                }

                if (--this.e == 0 && !this.world.isStatic) {
                    this.C();
                }
            }
        }

        if (this.w > 0) {
            --this.w;
        }

        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.world.isStatic) {
                if (!this.w()) {
                    this.a(true, true, false);
                } else if (this.world.d()) {
                    this.a(false, true, true);
                }
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.s_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        if (this.abilities.isFlying) {
            for (int i = 0; i < 8; ++i) {
                ;
            }
        }

        if (this.fireTicks > 0 && this.abilities.isInvulnerable) {
            this.fireTicks = 0;
        }

        this.x = this.A;
        this.y = this.B;
        this.z = this.C;
        double d0 = this.locX - this.A;
        double d1 = this.locY - this.B;
        double d2 = this.locZ - this.C;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.x = this.A = this.locX;
        }

        if (d2 > d3) {
            this.z = this.C = this.locZ;
        }

        if (d1 > d3) {
            this.y = this.B = this.locY;
        }

        if (d0 < -d3) {
            this.x = this.A = this.locX;
        }

        if (d2 < -d3) {
            this.z = this.C = this.locZ;
        }

        if (d1 < -d3) {
            this.y = this.B = this.locY;
        }

        this.A += d0 * 0.25D;
        this.C += d2 * 0.25D;
        this.B += d1 * 0.25D;
        this.a(StatisticList.k, 1);
        if (this.vehicle == null) {
            this.c = null;
        }

        if (!this.world.isStatic) {
            this.foodData.a(this);
        }
    }

    protected void b(ItemStack itemstack, int i) {
        if (itemstack.m() == EnumAnimation.b) {
            for (int j = 0; j < i; ++j) {
                Vec3D vec3d = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                vec3d.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d.b(-this.yaw * 3.1415927F / 180.0F);
                Vec3D vec3d1 = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

                vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
                vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.t(), this.locZ);
                this.world.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
            }

            this.world.makeSound(this, "mob.eat", 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected void C() {
        if (this.d != null) {
            this.b(this.d, 16);
            int i = this.d.count;
            ItemStack itemstack = this.d.b(this.world, this);

            if (itemstack != this.d || itemstack != null && itemstack.count != i) {
                this.inventory.items[this.inventory.itemInHandIndex] = itemstack;
                if (itemstack.count == 0) {
                    this.inventory.items[this.inventory.itemInHandIndex] = null;
                }
            }

            this.F();
        }
    }

    protected boolean H() {
        return this.health <= 0 || this.isSleeping();
    }

    protected void closeInventory() {
        this.activeContainer = this.defaultContainer;
    }

    public void I() {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        super.I();
        this.q = this.r;
        this.r = 0.0F;
        this.h(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    private int o() {
        return this.hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    protected void c_() {
        int i = this.o();

        if (this.s) {
            ++this.t;
            if (this.t >= i) {
                this.t = 0;
                this.s = false;
            }
        } else {
            this.t = 0;
        }

        this.am = (float) this.t / (float) i;
    }

    public void s() {
        if (this.n > 0) {
            --this.n;
        }

        if (this.world.difficulty == 0 && this.health < 20 && this.ticksLived % 20 * 12 == 0) {
            // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
            this.c(1, RegainReason.REGEN);
        }

        this.inventory.h();
        this.q = this.r;
        super.s();
        this.aj = this.O;
        this.ak = this.P;
        if (this.isSprinting()) {
            this.aj = (float) ((double) this.aj + (double) this.O * 0.3D);
            this.ak = (float) ((double) this.ak + (double) this.P * 0.3D);
        }

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

        this.r += (f - this.r) * 0.4F;
        this.av += (f1 - this.av) * 0.8F;
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
        entity.a_(this);
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.b(0.2F, 0.2F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612D;
        if (this.name.equals("Notch")) {
            this.a(new ItemStack(Item.APPLE, 1), true);
        }

        this.inventory.j();
        if (damagesource != null) {
            this.motX = (double) (-MathHelper.cos((this.ar + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.sin((this.ar + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
        this.a(StatisticList.y, 1);
    }

    public void b(Entity entity, int i) {
        this.p += i;
        if (entity instanceof EntityHuman) {
            this.a(StatisticList.A, 1);
        } else {
            this.a(StatisticList.z, 1);
        }
    }

    public void J() {
        this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, 1), false);
    }

    public void b(ItemStack itemstack) {
        this.a(itemstack, false);
    }

    public void a(ItemStack itemstack, boolean flag) {
        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.t(), this.locZ, itemstack);

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
            CraftItem drop = new CraftItem(this.world.getServer(), entityitem);

            PlayerDropItemEvent event = new PlayerDropItemEvent(player, drop);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                player.getInventory().addItem(drop.getItemStack());
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

        if (this.hasEffect(MobEffectList.FASTER_DIG)) {
            f *= 1.0F + (float) (this.getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffectList.SLOWER_DIG)) {
            f *= 1.0F - (float) (this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier() + 1) * 0.2F;
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
        this.exp = nbttagcompound.e("Xp");
        this.expLevel = nbttagcompound.e("XpLevel");
        this.expTotal = nbttagcompound.e("XpTotal");
        if (this.sleeping) {
            this.E = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
            this.a(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.world.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            this.b = new ChunkCoordinates(nbttagcompound.e("SpawnX"), nbttagcompound.e("SpawnY"), nbttagcompound.e("SpawnZ"));
        }

        this.foodData.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Inventory", (NBTBase) this.inventory.a(new NBTTagList()));
        nbttagcompound.a("Dimension", this.dimension);
        nbttagcompound.a("Sleeping", this.sleeping);
        nbttagcompound.a("SleepTimer", (short) this.sleepTicks);
        nbttagcompound.a("Xp", this.exp);
        nbttagcompound.a("XpLevel", this.expLevel);
        nbttagcompound.a("XpTotal", this.expTotal);
        if (this.b != null) {
            nbttagcompound.a("SpawnX", this.b.x);
            nbttagcompound.a("SpawnY", this.b.y);
            nbttagcompound.a("SpawnZ", this.b.z);
        }

        this.foodData.b(nbttagcompound);
    }

    public void a(IInventory iinventory) {}

    public void b(int i, int j, int k) {}

    public void receive(Entity entity, int i) {}

    public float t() {
        return 0.12F;
    }

    protected void m_() {
        this.height = 1.62F;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.abilities.isInvulnerable && !damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            this.aO = 0;
            if (this.health <= 0) {
                return false;
            } else {
                if (this.isSleeping() && !this.world.isStatic) {
                    this.a(true, true, false);
                }

                Entity entity = damagesource.getEntity();

                if (entity instanceof EntityMonster || entity instanceof EntityArrow) {
                    if (this.world.difficulty == 0) {
                        i = 0;
                    }

                    if (this.world.difficulty == 1) {
                        i = i / 3 + 1;
                    }

                    if (this.world.difficulty == 3) {
                        i = i * 3 / 2;
                    }
                }

                if (i == 0) {
                    return false;
                } else {
                    Entity entity1 = entity;

                    if (entity instanceof EntityArrow && ((EntityArrow) entity).shooter != null) {
                        entity1 = ((EntityArrow) entity).shooter;
                    }

                    if (entity1 instanceof EntityLiving) {
                        // CraftBukkit start - this is here instead of EntityMonster because EntityLiving(s) that aren't monsters
                        // also damage the player in this way. For example, EntitySlime.

                        // We handle projectiles in their individual classes!
                        if (!(entity.getBukkitEntity() instanceof Projectile)) {
                            org.bukkit.entity.Entity damager = ((Entity) entity1).getBukkitEntity();
                            org.bukkit.entity.Entity damagee = this.getBukkitEntity();

                            EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, i);
                            this.world.getServer().getPluginManager().callEvent(event);

                            if (event.isCancelled() || event.getDamage() == 0) {
                                return false;
                            }

                            i = event.getDamage();
                        }
                        // CraftBukkit end

                        this.a((EntityLiving) entity1, false);
                    }

                    this.a(StatisticList.x, i);
                    return super.damageEntity(damagesource, i);
                }
            }
        }
    }

    protected boolean n_() {
        return false;
    }

    protected void a(EntityLiving entityliving, boolean flag) {
        if (!(entityliving instanceof EntityCreeper) && !(entityliving instanceof EntityGhast)) {
            if (entityliving instanceof EntityWolf) {
                EntityWolf entitywolf = (EntityWolf) entityliving;

                if (entitywolf.isTamed() && this.name.equals(entitywolf.getOwnerName())) {
                    return;
                }
            }

            if (!(entityliving instanceof EntityHuman) || this.n_()) {
                List list = this.world.a(EntityWolf.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).b(16.0D, 4.0D, 16.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    EntityWolf entitywolf1 = (EntityWolf) entity;

                    if (entitywolf1.isTamed() && entitywolf1.C() == null && this.name.equals(entitywolf1.getOwnerName()) && (!flag || !entitywolf1.isSitting())) {
                        // CraftBukkit start
                        org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entityliving.getBukkitEntity();

                        EntityTargetEvent event;
                        if (flag) {
                            event = new EntityTargetEvent(entitywolf1.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET);
                        } else {
                            event = new EntityTargetEvent(entitywolf1.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER);
                        }
                        this.world.getServer().getPluginManager().callEvent(event);

                        if (event.isCancelled()) {
                            continue;
                        }
                        // CraftBukkit end

                        entitywolf1.setSitting(false);
                        entitywolf1.setTarget(entityliving);
                    }
                }
            }
        }
    }

    protected void b(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor() && this.G()) {
            i = 1 + i >> 1;
        }

        if (!damagesource.ignoresArmor()) {
            int j = 25 - this.inventory.i();
            int k = i * j + this.f;

            this.inventory.d(i);
            i = k / 25;
            this.f = k % 25;
        }

        this.b(damagesource.c());
        super.b(damagesource, i);
    }

    public void a(TileEntityFurnace tileentityfurnace) {}

    public void a(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void c(Entity entity) {
        if (!entity.b(this)) {
            ItemStack itemstack = this.K();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity);
                // CraftBukkit - bypass infinite items; <= 0 -> == 0
                if (itemstack.count == 0) {
                    itemstack.a(this);
                    this.L();
                }
            }
        }
    }

    public ItemStack K() {
        return this.inventory.getItemInHand();
    }

    public void L() {
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack) null);
    }

    public double M() {
        return (double) (this.height - 0.5F);
    }

    public void v() {
        if (!this.s || this.t >= this.o() / 2 || this.t < 0) {
            this.t = -1;
            this.s = true;
        }
    }

    public void d(Entity entity) {
        int i = this.inventory.a(entity);

        if (i > 0) {
            boolean flag = this.motY < 0.0D && !this.onGround && !this.p() && !this.ao();

            if (flag) {
                i = i * 3 / 2 + 1;
            }

            // CraftBukkit start - Don't call the event when the entity is human since it will be called with damageEntity
            if (entity instanceof EntityLiving && !(entity instanceof EntityHuman)) {
                org.bukkit.entity.Entity damager = this.getBukkitEntity();
                org.bukkit.entity.Entity damagee = (entity == null) ? null : entity.getBukkitEntity();

                EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(damager, damagee, EntityDamageEvent.DamageCause.ENTITY_ATTACK, i);
                this.world.getServer().getPluginManager().callEvent(event);

                if (event.isCancelled() || event.getDamage() == 0) {
                    return;
                }

                i = event.getDamage();
            }
            // CraftBukkit end

            boolean flag1 = entity.damageEntity(DamageSource.playerAttack(this), i);

            // CraftBukkit start - Return when the damage fails so that the item will not lose durability
            if (!flag1) {
                return;
            }
            // CraftBukkit end

            if (flag1) {
                if (this.isSprinting()) {
                    entity.b((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * 1.0F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * 1.0F));
                    this.motX *= 0.6D;
                    this.motZ *= 0.6D;
                    this.setSprinting(false);
                }

                if (flag) {
                    this.e(entity);
                }
            }

            ItemStack itemstack = this.K();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity, this);
                // CraftBukkit - bypass infinite items; <= 0 -> == 0
                if (itemstack.count == 0) {
                    itemstack.a(this);
                    this.L();
                }
            }

            if (entity instanceof EntityLiving) {
                if (entity.ac()) {
                    this.a((EntityLiving) entity, true);
                }

                this.a(StatisticList.w, i);
            }

            this.b(0.3F);
        }
    }

    public void e(Entity entity) {}

    public void a(ItemStack itemstack) {}

    public void die() {
        super.die();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }

    public boolean O() {
        return !this.sleeping && super.O();
    }

    public EnumBedError a(int i, int j, int k) {
        if (!this.world.isStatic) {
            if (this.isSleeping() || !this.ac()) {
                return EnumBedError.OTHER_PROBLEM;
            }

            if (this.world.worldProvider.c) {
                return EnumBedError.NOT_POSSIBLE_HERE;
            }

            if (this.world.d()) {
                return EnumBedError.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.locX - (double) i) > 3.0D || Math.abs(this.locY - (double) j) > 2.0D || Math.abs(this.locZ - (double) k) > 3.0D) {
                return EnumBedError.TOO_FAR_AWAY;
            }
        }

        // CraftBukkit start
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            org.bukkit.block.Block bed = this.world.getWorld().getBlockAt(i, j, k);

            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent(event);

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

            this.b(i1);
            this.setPosition((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
        } else {
            this.setPosition((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
        }

        this.sleeping = true;
        this.sleepTicks = 0;
        this.E = new ChunkCoordinates(i, j, k);
        this.motX = this.motZ = this.motY = 0.0D;
        if (!this.world.isStatic) {
            this.world.everyoneSleeping();
        }

        return EnumBedError.OK;
    }

    private void b(int i) {
        this.F = 0.0F;
        this.G = 0.0F;
        switch (i) {
        case 0:
            this.G = -1.8F;
            break;

        case 1:
            this.F = 1.8F;
            break;

        case 2:
            this.G = 1.8F;
            break;

        case 3:
            this.F = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.fauxSleeping) return; // CraftBukkit - Can't leave bed if not in one!

        this.b(0.6F, 1.8F);
        this.m_();
        ChunkCoordinates chunkcoordinates = this.E;
        ChunkCoordinates chunkcoordinates1 = this.E;

        if (chunkcoordinates != null && this.world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Block.BED.id) {
            BlockBed.a(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
            chunkcoordinates1 = BlockBed.f(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
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

            org.bukkit.block.Block bed;
            if (chunkcoordinates != null) {
                bed = this.world.getWorld().getBlockAt(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z);
            } else {
                bed = this.world.getWorld().getBlockAt(player.getLocation());
            }

            PlayerBedLeaveEvent event = new PlayerBedLeaveEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent(event);
        }
        // CraftBukkit end

        if (flag) {
            this.sleepTicks = 0;
        } else {
            this.sleepTicks = 100;
        }

        if (flag2) {
            this.a(this.E);
        }
    }

    private boolean w() {
        return this.world.getTypeId(this.E.x, this.E.y, this.E.z) == Block.BED.id;
    }

    public static ChunkCoordinates getBed(World world, ChunkCoordinates chunkcoordinates) {
        IChunkProvider ichunkprovider = world.n();

        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
        if (world.getTypeId(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) != Block.BED.id) {
            return null;
        } else {
            ChunkCoordinates chunkcoordinates1 = BlockBed.f(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);

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

    public ChunkCoordinates getBed() {
        return this.b;
    }

    public void a(ChunkCoordinates chunkcoordinates) {
        if (chunkcoordinates != null) {
            this.b = new ChunkCoordinates(chunkcoordinates);
            this.spawnWorld = this.world.worldData.name; // CraftBukkit
        } else {
            this.b = null;
        }
    }

    public void a(Statistic statistic) {
        this.a(statistic, 1);
    }

    public void a(Statistic statistic, int i) {}

    protected void S() {
        super.S();
        this.a(StatisticList.u, 1);
        if (this.isSprinting()) {
            this.b(0.8F);
        } else {
            this.b(0.2F);
        }
    }

    public void a(float f, float f1) {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        if (this.abilities.isFlying) {
            double d3 = this.motY;
            float f2 = this.ak;

            this.ak = 0.05F;
            super.a(f, f1);
            this.motY = d3 * 0.6D;
            this.ak = f2;
        } else {
            super.a(f, f1);
        }

        this.a(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    public void a(double d0, double d1, double d2) {
        if (this.vehicle == null) {
            int i;

            if (this.a(Material.WATER)) {
                i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.q, i);
                    this.b(0.015F * (float) i * 0.01F);
                }
            } else if (this.ao()) {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                    this.b(0.015F * (float) i * 0.01F);
                }
            } else if (this.p()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.o, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.l, i);
                    if (this.isSprinting()) {
                        this.b(0.099999994F * (float) i * 0.01F);
                    } else {
                        this.b(0.01F * (float) i * 0.01F);
                    }
                }
            } else {
                i = Math.round(MathHelper.a(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.p, i);
                }
            }
        }
    }

    private void h(double d0, double d1, double d2) {
        if (this.vehicle != null) {
            int i = Math.round(MathHelper.a(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.vehicle instanceof EntityMinecart) {
                    this.a(StatisticList.r, i);
                    if (this.c == null) {
                        this.c = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    } else if (this.c.a(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000.0D) {
                        this.a((Statistic) AchievementList.q, 1);
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
        if (!this.abilities.canFly) {
            if (f >= 2.0F) {
                this.a(StatisticList.n, (int) Math.round((double) f * 100.0D));
            }

            super.a(f);
        }
    }

    public void a(EntityLiving entityliving) {
        if (entityliving instanceof EntityMonster) {
            this.a((Statistic) AchievementList.s);
        }
    }

    public void T() {
        if (this.H > 0) {
            this.H = 10;
        } else {
            this.I = true;
        }
    }

    public void d(int i) {
        this.exp += i;
        this.expTotal += i;

        while (this.exp >= this.U()) {
            this.exp -= this.U();
            this.y();
        }
    }

    public int U() {
        return (this.expLevel + 1) * 10;
    }

    private void y() {
        ++this.expLevel;
    }

    public void b(float f) {
        if (!this.abilities.isInvulnerable) {
            if (!this.world.isStatic) {
                this.foodData.a(f);
            }
        }
    }

    public FoodMetaData getFoodData() {
        return this.foodData;
    }

    public boolean c(boolean flag) {
        return (flag || this.foodData.b()) && !this.abilities.isInvulnerable;
    }

    public boolean W() {
        return this.health > 0 && this.health < 20;
    }

    public void a(ItemStack itemstack, int i) {
        if (itemstack != this.d) {
            this.d = itemstack;
            this.e = i;
            if (!this.world.isStatic) {
                this.h(true);
            }
        }
    }

    public boolean c(int i, int j, int k) {
        return true;
    }

    protected int a(EntityHuman entityhuman) {
        return this.expTotal >> 1;
    }

    protected boolean X() {
        return true;
    }

    public String Y() {
        return this.name;
    }
}
