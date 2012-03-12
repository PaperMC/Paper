package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.TrigMath;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
// CraftBukkit end

public abstract class EntityHuman extends EntityLiving {

    public PlayerInventory inventory = new PlayerInventory(this);
    public Container defaultContainer;
    public Container activeContainer;
    protected FoodMetaData foodData = new FoodMetaData();
    protected int o = 0;
    public byte p = 0;
    public int q = 0;
    public float r;
    public float s;
    public boolean t = false;
    public int u = 0;
    public String name;
    public int dimension;
    public int x = 0;
    public double y;
    public double z;
    public double A;
    public double B;
    public double C;
    public double D;
    // CraftBukkit start
    public boolean sleeping;
    public boolean fauxSleeping;
    public String spawnWorld = "";

    public HumanEntity getBukkitEntity() {
        return (HumanEntity) super.getBukkitEntity();
    }
    // CraftBukkit end

    public ChunkCoordinates F;
    public int sleepTicks; // CraftBukkit - private -> public
    public float G;
    public float H;
    private ChunkCoordinates b;
    private ChunkCoordinates c;
    public int I = 20;
    protected boolean J = false;
    public float K;
    public PlayerAbilities abilities = new PlayerAbilities();
    public int oldLevel = -1; // CraftBukkit
    public int expLevel;
    public int expTotal;
    public float exp;
    private ItemStack d;
    private int e;
    protected float P = 0.1F;
    protected float Q = 0.02F;
    public EntityFishingHook hookedFish = null;

    public EntityHuman(World world) {
        super(world);
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawn();

        this.setPositionRotation((double) chunkcoordinates.x + 0.5D, (double) (chunkcoordinates.y + 1), (double) chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
        this.ah = "humanoid";
        this.ag = 180.0F;
        this.maxFireTicks = 20;
        this.texture = "/mob/char.png";
    }

    public int getMaxHealth() {
        return 20;
    }

    protected void b() {
        super.b();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, Byte.valueOf((byte) 0));
    }

    public boolean L() {
        return this.d != null;
    }

    public void M() {
        if (this.d != null) {
            this.d.b(this.world, this, this.e);
        }

        this.N();
    }

    public void N() {
        this.d = null;
        this.e = 0;
        if (!this.world.isStatic) {
            this.i(false);
        }
    }

    public boolean O() {
        return this.L() && Item.byId[this.d.id].d(this.d) == EnumAnimation.d;
    }

    public void G_() {
        if (this.d != null) {
            ItemStack itemstack = this.inventory.getItemInHand();

            if (itemstack != this.d) {
                this.N();
            } else {
                if (this.e <= 25 && this.e % 4 == 0) {
                    this.b(itemstack, 5);
                }

                if (--this.e == 0 && !this.world.isStatic) {
                    this.K();
                }
            }
        }

        if (this.x > 0) {
            --this.x;
        }

        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.world.isStatic) {
                if (!this.G()) {
                    this.a(true, true, false);
                } else if (this.world.e()) {
                    this.a(false, true, true);
                }
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.G_();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.b(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        if (this.abilities.isFlying) {
            for (int i = 0; i < 8; ++i) {
                ;
            }
        }

        if (this.isBurning() && this.abilities.isInvulnerable) {
            this.extinguish();
        }

        this.y = this.B;
        this.z = this.C;
        this.A = this.D;
        double d0 = this.locX - this.B;
        double d1 = this.locY - this.C;
        double d2 = this.locZ - this.D;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.y = this.B = this.locX;
        }

        if (d2 > d3) {
            this.A = this.D = this.locZ;
        }

        if (d1 > d3) {
            this.z = this.C = this.locY;
        }

        if (d0 < -d3) {
            this.y = this.B = this.locX;
        }

        if (d2 < -d3) {
            this.A = this.D = this.locZ;
        }

        if (d1 < -d3) {
            this.z = this.C = this.locY;
        }

        this.B += d0 * 0.25D;
        this.D += d2 * 0.25D;
        this.C += d1 * 0.25D;
        this.a(StatisticList.k, 1);
        if (this.vehicle == null) {
            this.c = null;
        }

        if (!this.world.isStatic) {
            this.foodData.a(this);
        }
    }

    protected void b(ItemStack itemstack, int i) {
        if (itemstack.m() == EnumAnimation.c) {
            this.world.makeSound(this, "random.drink", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (itemstack.m() == EnumAnimation.b) {
            for (int j = 0; j < i; ++j) {
                Vec3D vec3d = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                vec3d.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d.b(-this.yaw * 3.1415927F / 180.0F);
                Vec3D vec3d1 = Vec3D.create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

                vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
                vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ);
                this.world.a("iconcrack_" + itemstack.getItem().id, vec3d1.a, vec3d1.b, vec3d1.c, vec3d.a, vec3d.b + 0.05D, vec3d.c);
            }

            this.world.makeSound(this, "random.eat", 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected void K() {
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

            this.N();
        }
    }

    protected boolean P() {
        return this.getHealth() <= 0 || this.isSleeping();
    }

    // CraftBukkit - protected -> public
    public void closeInventory() {
        this.activeContainer = this.defaultContainer;
    }

    public void Q() {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        super.Q();
        this.r = this.s;
        this.s = 0.0F;
        this.h(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    private int E() {
        return this.hasEffect(MobEffectList.FASTER_DIG) ? 6 - (1 + this.getEffect(MobEffectList.FASTER_DIG).getAmplifier()) * 1 : (this.hasEffect(MobEffectList.SLOWER_DIG) ? 6 + (1 + this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier()) * 2 : 6);
    }

    protected void d_() {
        int i = this.E();

        if (this.t) {
            ++this.u;
            if (this.u >= i) {
                this.u = 0;
                this.t = false;
            }
        } else {
            this.u = 0;
        }

        this.ao = (float) this.u / (float) i;
    }

    public void e() {
        if (this.o > 0) {
            --this.o;
        }

        if (this.world.difficulty == 0 && this.getHealth() < this.getMaxHealth() && this.ticksLived % 20 * 12 == 0) {
            // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
            this.heal(1, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
        }

        this.inventory.i();
        this.r = this.s;
        super.e();
        this.al = this.P;
        this.am = this.Q;
        if (this.isSprinting()) {
            this.al = (float) ((double) this.al + (double) this.P * 0.3D);
            this.am = (float) ((double) this.am + (double) this.Q * 0.3D);
        }

        float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
        // CraftBukkit - Math -> TrigMath
        float f1 = (float) TrigMath.atan(-this.motY * 0.20000000298023224D) * 15.0F;

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0) {
            f = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0) {
            f1 = 0.0F;
        }

        this.s += (f - this.s) * 0.4F;
        this.ay += (f1 - this.ay) * 0.8F;
        if (this.getHealth() > 0) {
            List list = this.world.getEntities(this, this.boundingBox.grow(1.0D, 0.0D, 1.0D));

            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (!entity.dead) {
                        this.l(entity);
                    }
                }
            }
        }
    }

    private void l(Entity entity) {
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

        this.inventory.k();
        if (damagesource != null) {
            this.motX = (double) (-MathHelper.cos((this.au + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.sin((this.au + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
        this.a(StatisticList.y, 1);
    }

    public void b(Entity entity, int i) {
        this.q += i;
        if (entity instanceof EntityHuman) {
            this.a(StatisticList.A, 1);
        } else {
            this.a(StatisticList.z, 1);
        }
    }

    protected int b_(int i) {
        int j = EnchantmentManager.getOxygenEnchantmentLevel(this.inventory);

        return j > 0 && this.random.nextInt(j + 1) > 0 ? i : super.b_(i);
    }

    public EntityItem R() {
        return this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, 1), false);
    }

    public EntityItem drop(ItemStack itemstack) {
        return this.a(itemstack, false);
    }

    public EntityItem a(ItemStack itemstack, boolean flag) {
        if (itemstack == null) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.getHeadHeight(), this.locZ, itemstack);

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
                return null;
            }
            // CraftBukkit end

            this.a(entityitem);
            this.a(StatisticList.v, 1);
            return entityitem;
        }
    }

    protected void a(EntityItem entityitem) {
        this.world.addEntity(entityitem);
    }

    public float a(Block block) {
        float f = this.inventory.a(block);
        float f1 = f;
        int i = EnchantmentManager.getDigSpeedEnchantmentLevel(this.inventory);

        if (i > 0 && this.inventory.b(block)) {
            f1 = f + (float) (i * i + 1);
        }

        if (this.hasEffect(MobEffectList.FASTER_DIG)) {
            f1 *= 1.0F + (float) (this.getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffectList.SLOWER_DIG)) {
            f1 *= 1.0F - (float) (this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.a(Material.WATER) && !EnchantmentManager.hasWaterWorkerEnchantment(this.inventory)) {
            f1 /= 5.0F;
        }

        if (!this.onGround) {
            f1 /= 5.0F;
        }

        return f1;
    }

    public boolean b(Block block) {
        return this.inventory.b(block);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        NBTTagList nbttaglist = nbttagcompound.getList("Inventory");

        this.inventory.b(nbttaglist);
        this.dimension = nbttagcompound.getInt("Dimension");
        this.sleeping = nbttagcompound.getBoolean("Sleeping");
        this.sleepTicks = nbttagcompound.getShort("SleepTimer");
        this.exp = nbttagcompound.getFloat("XpP");
        this.expLevel = nbttagcompound.getInt("XpLevel");
        this.expTotal = nbttagcompound.getInt("XpTotal");
        if (this.sleeping) {
            this.F = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
            this.a(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.world.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.hasKey("SpawnX") && nbttagcompound.hasKey("SpawnY") && nbttagcompound.hasKey("SpawnZ")) {
            this.b = new ChunkCoordinates(nbttagcompound.getInt("SpawnX"), nbttagcompound.getInt("SpawnY"), nbttagcompound.getInt("SpawnZ"));
        }

        this.foodData.a(nbttagcompound);
        this.abilities.b(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.set("Inventory", this.inventory.a(new NBTTagList()));
        nbttagcompound.setInt("Dimension", this.dimension);
        nbttagcompound.setBoolean("Sleeping", this.sleeping);
        nbttagcompound.setShort("SleepTimer", (short) this.sleepTicks);
        nbttagcompound.setFloat("XpP", this.exp);
        nbttagcompound.setInt("XpLevel", this.expLevel);
        nbttagcompound.setInt("XpTotal", this.expTotal);
        if (this.b != null) {
            nbttagcompound.setInt("SpawnX", this.b.x);
            nbttagcompound.setInt("SpawnY", this.b.y);
            nbttagcompound.setInt("SpawnZ", this.b.z);
            nbttagcompound.setString("SpawnWorld", spawnWorld); // CraftBukkit - fixes bed spawns for multiworld worlds
        }

        this.foodData.b(nbttagcompound);
        this.abilities.a(nbttagcompound);
    }

    public void openContainer(IInventory iinventory) {}

    public void startEnchanting(int i, int j, int k) {}

    public void startCrafting(int i, int j, int k) {}

    public void receive(Entity entity, int i) {}

    public float getHeadHeight() {
        return 0.12F;
    }

    protected void A() {
        this.height = 1.62F;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.abilities.isInvulnerable && !damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            this.aV = 0;
            if (this.getHealth() <= 0) {
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
                        i = i / 2 + 1;
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
                        this.a((EntityLiving) entity1, false);
                    }

                    this.a(StatisticList.x, i);
                    return super.damageEntity(damagesource, i);
                }
            }
        }
    }

    protected int b(DamageSource damagesource, int i) {
        int j = super.b(damagesource, i);

        if (j <= 0) {
            return 0;
        } else {
            int k = EnchantmentManager.a(this.inventory, damagesource);

            if (k > 20) {
                k = 20;
            }

            if (k > 0 && k <= 20) {
                int l = 25 - k;
                int i1 = j * l + this.ar;

                j = i1 / 25;
                this.ar = i1 % 25;
            }

            return j;
        }
    }

    protected boolean C_() {
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

            if (!(entityliving instanceof EntityHuman) || this.C_()) {
                List list = this.world.a(EntityWolf.class, AxisAlignedBB.b(this.locX, this.locY, this.locZ, this.locX + 1.0D, this.locY + 1.0D, this.locZ + 1.0D).grow(16.0D, 4.0D, 16.0D));
                Iterator iterator = list.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();
                    EntityWolf entitywolf1 = (EntityWolf) entity;

                    if (entitywolf1.isTamed() && entitywolf1.H() == null && this.name.equals(entitywolf1.getOwnerName()) && (!flag || !entitywolf1.isSitting())) {
                        entitywolf1.setSitting(false);
                        entitywolf1.setTarget(entityliving);
                    }
                }
            }
        }
    }

    protected void f(int i) {
        this.inventory.e(i);
    }

    public int S() {
        return this.inventory.j();
    }

    protected void c(DamageSource damagesource, int i) {
        if (!damagesource.ignoresArmor() && this.O()) {
            i = 1 + i >> 1;
        }

        i = this.d(damagesource, i);
        i = this.b(damagesource, i);
        this.c(damagesource.f());
        this.health -= i;
    }

    public void openFurnace(TileEntityFurnace tileentityfurnace) {}

    public void openDispenser(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntitySign tileentitysign) {}

    public void openBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {}

    public void e(Entity entity) {
        if (!entity.b(this)) {
            ItemStack itemstack = this.T();

            if (itemstack != null && entity instanceof EntityLiving) {
                itemstack.a((EntityLiving) entity);
                // CraftBukkit - bypass infinite items; <= 0 -> == 0
                if (itemstack.count == 0) {
                    itemstack.a(this);
                    this.U();
                }
            }
        }
    }

    public ItemStack T() {
        return this.inventory.getItemInHand();
    }

    public void U() {
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack) null);
    }

    public double V() {
        return (double) (this.height - 0.5F);
    }

    public void D() {
        if (!this.t || this.u >= this.E() / 2 || this.u < 0) {
            this.u = -1;
            this.t = true;
        }
    }

    public void attack(Entity entity) {
        if (entity.k_()) {
            int i = this.inventory.a(entity);

            if (this.hasEffect(MobEffectList.INCREASE_DAMAGE)) {
                i += 3 << this.getEffect(MobEffectList.INCREASE_DAMAGE).getAmplifier();
            }

            if (this.hasEffect(MobEffectList.WEAKNESS)) {
                i -= 2 << this.getEffect(MobEffectList.WEAKNESS).getAmplifier();
            }

            int j = 0;
            int k = 0;

            if (entity instanceof EntityLiving) {
                k = EnchantmentManager.a(this.inventory, (EntityLiving) entity);
                j += EnchantmentManager.getKnockbackEnchantmentLevel(this.inventory, (EntityLiving) entity);
            }

            if (this.isSprinting()) {
                ++j;
            }

            if (i > 0 || k > 0) {
                boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.t() && !this.aT() && !this.hasEffect(MobEffectList.BLINDNESS) && this.vehicle == null && entity instanceof EntityLiving;

                if (flag) {
                    i += this.random.nextInt(i / 2 + 2);
                }

                i += k;
                boolean flag1 = entity.damageEntity(DamageSource.playerAttack(this), i);

                // CraftBukkit start - Return when the damage fails so that the item will not lose durability
                if (!flag1) {
                    return;
                }
                // CraftBukkit end

                if (flag1) {
                    if (j > 0) {
                        entity.b_((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float) j * 0.5F));
                        this.motX *= 0.6D;
                        this.motZ *= 0.6D;
                        this.setSprinting(false);
                    }

                    if (flag) {
                        this.c(entity);
                    }

                    if (k > 0) {
                        this.d(entity);
                    }

                    if (i >= 18) {
                        this.a((Statistic) AchievementList.E);
                    }

                    this.g(entity);
                }

                ItemStack itemstack = this.T();

                if (itemstack != null && entity instanceof EntityLiving) {
                    itemstack.a((EntityLiving) entity, this);
                    // CraftBukkit - bypass infinite items; <= 0 -> == 0
                    if (itemstack.count == 0) {
                        itemstack.a(this);
                        this.U();
                    }
                }

                if (entity instanceof EntityLiving) {
                    if (entity.isAlive()) {
                        this.a((EntityLiving) entity, true);
                    }

                    this.a(StatisticList.w, i);
                    int l = EnchantmentManager.getFireAspectEnchantmentLevel(this.inventory, (EntityLiving) entity);

                    if (l > 0) {
                        // CraftBukkit start - raise a combust event when somebody hits with a fire enchanted item
                        EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), l * 4);
                        Bukkit.getPluginManager().callEvent(combustEvent);

                        if (!combustEvent.isCancelled()) {
                            entity.setOnFire(combustEvent.getDuration());
                        }
                        // CraftBukkit end
                    }
                }

                this.c(0.3F);
            }
        }
    }

    public void c(Entity entity) {}

    public void d(Entity entity) {}

    public void carriedChanged(ItemStack itemstack) {}

    public void die() {
        super.die();
        this.defaultContainer.a(this);
        if (this.activeContainer != null) {
            this.activeContainer.a(this);
        }
    }

    public boolean inBlock() {
        return !this.sleeping && super.inBlock();
    }

    public EnumBedResult a(int i, int j, int k) {
        if (!this.world.isStatic) {
            if (this.isSleeping() || !this.isAlive()) {
                return EnumBedResult.OTHER_PROBLEM;
            }

            if (!this.world.worldProvider.d()) {
                return EnumBedResult.NOT_POSSIBLE_HERE;
            }

            if (this.world.e()) {
                return EnumBedResult.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.locX - (double) i) > 3.0D || Math.abs(this.locY - (double) j) > 2.0D || Math.abs(this.locZ - (double) k) > 3.0D) {
                return EnumBedResult.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            List list = this.world.a(EntityMonster.class, AxisAlignedBB.b((double) i - d0, (double) j - d1, (double) k - d0, (double) i + d0, (double) j + d1, (double) k + d0));

            if (!list.isEmpty()) {
                return EnumBedResult.NOT_SAFE;
            }
        }

        // CraftBukkit start
        if (this.getBukkitEntity() instanceof Player) {
            Player player = (Player) this.getBukkitEntity();
            org.bukkit.block.Block bed = this.world.getWorld().getBlockAt(i, j, k);

            PlayerBedEnterEvent event = new PlayerBedEnterEvent(player, bed);
            this.world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                return EnumBedResult.OTHER_PROBLEM;
            }
        }
        // CraftBukkit end

        this.b(0.2F, 0.2F);
        this.height = 0.2F;
        if (this.world.isLoaded(i, j, k)) {
            int l = this.world.getData(i, j, k);
            int i1 = BlockBed.b(l);
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

            this.c(i1);
            this.setPosition((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
        } else {
            this.setPosition((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
        }

        this.sleeping = true;
        this.sleepTicks = 0;
        this.F = new ChunkCoordinates(i, j, k);
        this.motX = this.motZ = this.motY = 0.0D;
        if (!this.world.isStatic) {
            this.world.everyoneSleeping();
        }

        return EnumBedResult.OK;
    }

    private void c(int i) {
        this.G = 0.0F;
        this.H = 0.0F;
        switch (i) {
        case 0:
            this.H = -1.8F;
            break;

        case 1:
            this.G = 1.8F;
            break;

        case 2:
            this.H = 1.8F;
            break;

        case 3:
            this.G = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        if (this.fauxSleeping && !this.sleeping) return; // CraftBukkit - Can't leave bed if not in one!

        this.b(0.6F, 1.8F);
        this.A();
        ChunkCoordinates chunkcoordinates = this.F;
        ChunkCoordinates chunkcoordinates1 = this.F;

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
            this.setRespawnPosition(this.F);
        }
    }

    private boolean G() {
        return this.world.getTypeId(this.F.x, this.F.y, this.F.z) == Block.BED.id;
    }

    public static ChunkCoordinates getBed(World world, ChunkCoordinates chunkcoordinates) {
        IChunkProvider ichunkprovider = world.q();

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

    public void setRespawnPosition(ChunkCoordinates chunkcoordinates) {
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

    protected void ab() {
        super.ab();
        this.a(StatisticList.u, 1);
        if (this.isSprinting()) {
            this.c(0.8F);
        } else {
            this.c(0.2F);
        }
    }

    public void a(float f, float f1) {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        if (this.abilities.isFlying) {
            double d3 = this.motY;
            float f2 = this.am;

            this.am = 0.05F;
            super.a(f, f1);
            this.motY = d3 * 0.6D;
            this.am = f2;
        } else {
            super.a(f, f1);
        }

        this.checkMovement(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    public void checkMovement(double d0, double d1, double d2) {
        if (this.vehicle == null) {
            int i;

            if (this.a(Material.WATER)) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.q, i);
                    this.c(0.015F * (float) i * 0.01F);
                }
            } else if (this.aT()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                    this.c(0.015F * (float) i * 0.01F);
                }
            } else if (this.t()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.o, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.l, i);
                    if (this.isSprinting()) {
                        this.c(0.099999994F * (float) i * 0.01F);
                    } else {
                        this.c(0.01F * (float) i * 0.01F);
                    }
                }
            } else {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.p, i);
                }
            }
        }
    }

    private void h(double d0, double d1, double d2) {
        if (this.vehicle != null) {
            int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.vehicle instanceof EntityMinecart) {
                    this.a(StatisticList.r, i);
                    if (this.c == null) {
                        this.c = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    } else if (this.c.b(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000.0D) {
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

    public void c(EntityLiving entityliving) {
        if (entityliving instanceof EntityMonster) {
            this.a((Statistic) AchievementList.s);
        }
    }

    public void ac() {
        if (this.I > 0) {
            this.I = 10;
        } else {
            this.J = true;
        }
    }

    public void giveExp(int i) {
        this.q += i;
        int j = Integer.MAX_VALUE - this.expTotal;

        if (i > j) {
            i = j;
        }

        this.exp += (float) i / (float) this.getExpToLevel();

        for (this.expTotal += i; this.exp >= 1.0F; this.exp /= (float) this.getExpToLevel()) {
            this.exp = (this.exp - 1.0F) * (float) this.getExpToLevel();
            this.levelUp();
        }
    }

    public void levelDown(int i) {
        this.expLevel -= i;
        if (this.expLevel < 0) {
            this.expLevel = 0;
        }
    }

    public int getExpToLevel() {
        return 7 + (this.expLevel * 7 >> 1);
    }

    private void levelUp() {
        ++this.expLevel;
    }

    public void c(float f) {
        if (!this.abilities.isInvulnerable) {
            if (!this.world.isStatic) {
                this.foodData.a(f);
            }
        }
    }

    public FoodMetaData getFoodData() {
        return this.foodData;
    }

    public boolean b(boolean flag) {
        return (flag || this.foodData.b()) && !this.abilities.isInvulnerable;
    }

    public boolean af() {
        return this.getHealth() > 0 && this.getHealth() < this.getMaxHealth();
    }

    public void a(ItemStack itemstack, int i) {
        if (itemstack != this.d) {
            this.d = itemstack;
            this.e = i;
            if (!this.world.isStatic) {
                this.i(true);
            }
        }
    }

    public boolean d(int i, int j, int k) {
        return true;
    }

    protected int getExpValue(EntityHuman entityhuman) {
        int i = this.expLevel * 7;

        return i > 100 ? 100 : i;
    }

    protected boolean alwaysGivesExp() {
        return true;
    }

    public String getLocalizedName() {
        return this.name;
    }

    public void e(int i) {}

    public void copyTo(EntityHuman entityhuman) {
        this.inventory.a(entityhuman.inventory);
        this.health = entityhuman.health;
        this.foodData = entityhuman.foodData;
        this.expLevel = entityhuman.expLevel;
        this.expTotal = entityhuman.expTotal;
        this.exp = entityhuman.exp;
        this.q = entityhuman.q;
    }

    protected boolean g_() {
        return !this.abilities.isFlying;
    }
}
