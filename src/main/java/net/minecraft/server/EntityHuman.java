package net.minecraft.server;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.util.com.google.common.base.Charsets;
import net.minecraft.util.com.mojang.authlib.GameProfile;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.entity.CraftItem;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
// CraftBukkit end

public abstract class EntityHuman extends EntityLiving implements ICommandListener {

    public PlayerInventory inventory = new PlayerInventory(this);
    private InventoryEnderChest enderChest = new InventoryEnderChest();
    public Container defaultContainer;
    public Container activeContainer;
    protected FoodMetaData foodData = new FoodMetaData(this); // CraftBukkit - add "this" to constructor
    protected int br;
    public float bs;
    public float bt;
    public int bu;
    public double bv;
    public double bw;
    public double bx;
    public double by;
    public double bz;
    public double bA;
    // CraftBukkit start
    public boolean sleeping; // protected -> public
    public boolean fauxSleeping;
    public String spawnWorld = "";

    @Override
    public CraftHumanEntity getBukkitEntity() {
        return (CraftHumanEntity) super.getBukkitEntity();
    }
    // CraftBukkit end

    public ChunkCoordinates bC;
    public int sleepTicks; // CraftBukkit - private -> public
    public float bD;
    public float bE;
    private ChunkCoordinates c;
    private boolean d;
    private ChunkCoordinates e;
    public PlayerAbilities abilities = new PlayerAbilities();
    public int oldLevel = -1; // CraftBukkit
    public int expLevel;
    public int expTotal;
    public float exp;
    private ItemStack f;
    private int g;
    protected float bJ = 0.1F;
    protected float bK = 0.02F;
    private int h;
    private final GameProfile i;
    public EntityFishingHook hookedFish;

    public EntityHuman(World world, GameProfile gameprofile) {
        super(world);
        this.uniqueID = a(gameprofile);
        this.i = gameprofile;
        this.defaultContainer = new ContainerPlayer(this.inventory, !world.isStatic, this);
        this.activeContainer = this.defaultContainer;
        this.height = 1.62F;
        ChunkCoordinates chunkcoordinates = world.getSpawn();

        this.setPositionRotation((double) chunkcoordinates.x + 0.5D, (double) (chunkcoordinates.y + 1), (double) chunkcoordinates.z + 0.5D, 0.0F, 0.0F);
        this.ba = 180.0F;
        this.maxFireTicks = 20;
    }

    protected void aD() {
        super.aD();
        this.bc().b(GenericAttributes.e).setValue(1.0D);
    }

    protected void c() {
        super.c();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
        this.datawatcher.a(17, Float.valueOf(0.0F));
        this.datawatcher.a(18, Integer.valueOf(0));
    }

    public boolean bw() {
        return this.f != null;
    }

    public void by() {
        if (this.f != null) {
            this.f.b(this.world, this, this.g);
        }

        this.bz();
    }

    public void bz() {
        this.f = null;
        this.g = 0;
        if (!this.world.isStatic) {
            this.e(false);
        }
    }

    public boolean isBlocking() {
        return this.bw() && this.f.getItem().d(this.f) == EnumAnimation.BLOCK;
    }

    public void h() {
        if (this.f != null) {
            ItemStack itemstack = this.inventory.getItemInHand();

            if (itemstack == this.f) {
                if (this.g <= 25 && this.g % 4 == 0) {
                    this.c(itemstack, 5);
                }

                if (--this.g == 0 && !this.world.isStatic) {
                    this.p();
                }
            } else {
                this.bz();
            }
        }

        if (this.bu > 0) {
            --this.bu;
        }

        if (this.isSleeping()) {
            ++this.sleepTicks;
            if (this.sleepTicks > 100) {
                this.sleepTicks = 100;
            }

            if (!this.world.isStatic) {
                if (!this.j()) {
                    this.a(true, true, false);
                } else if (this.world.v()) {
                    this.a(false, true, true);
                }
            }
        } else if (this.sleepTicks > 0) {
            ++this.sleepTicks;
            if (this.sleepTicks >= 110) {
                this.sleepTicks = 0;
            }
        }

        super.h();
        if (!this.world.isStatic && this.activeContainer != null && !this.activeContainer.a(this)) {
            this.closeInventory();
            this.activeContainer = this.defaultContainer;
        }

        if (this.isBurning() && this.abilities.isInvulnerable) {
            this.extinguish();
        }

        this.bv = this.by;
        this.bw = this.bz;
        this.bx = this.bA;
        double d0 = this.locX - this.by;
        double d1 = this.locY - this.bz;
        double d2 = this.locZ - this.bA;
        double d3 = 10.0D;

        if (d0 > d3) {
            this.bv = this.by = this.locX;
        }

        if (d2 > d3) {
            this.bx = this.bA = this.locZ;
        }

        if (d1 > d3) {
            this.bw = this.bz = this.locY;
        }

        if (d0 < -d3) {
            this.bv = this.by = this.locX;
        }

        if (d2 < -d3) {
            this.bx = this.bA = this.locZ;
        }

        if (d1 < -d3) {
            this.bw = this.bz = this.locY;
        }

        this.by += d0 * 0.25D;
        this.bA += d2 * 0.25D;
        this.bz += d1 * 0.25D;
        if (this.vehicle == null) {
            this.e = null;
        }

        if (!this.world.isStatic) {
            this.foodData.a(this);
            this.a(StatisticList.g, 1);
        }
    }

    public int D() {
        return this.abilities.isInvulnerable ? 0 : 80;
    }

    protected String H() {
        return "game.player.swim";
    }

    protected String O() {
        return "game.player.swim.splash";
    }

    public int ai() {
        return 10;
    }

    public void makeSound(String s, float f, float f1) {
        this.world.a(this, s, f, f1);
    }

    protected void c(ItemStack itemstack, int i) {
        if (itemstack.o() == EnumAnimation.DRINK) {
            this.makeSound("random.drink", 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (itemstack.o() == EnumAnimation.EAT) {
            for (int j = 0; j < i; ++j) {
                Vec3D vec3d = this.world.getVec3DPool().create(((double) this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);

                vec3d.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d.b(-this.yaw * 3.1415927F / 180.0F);
                Vec3D vec3d1 = this.world.getVec3DPool().create(((double) this.random.nextFloat() - 0.5D) * 0.3D, (double) (-this.random.nextFloat()) * 0.6D - 0.3D, 0.6D);

                vec3d1.a(-this.pitch * 3.1415927F / 180.0F);
                vec3d1.b(-this.yaw * 3.1415927F / 180.0F);
                vec3d1 = vec3d1.add(this.locX, this.locY + (double) this.getHeadHeight(), this.locZ);
                String s = "iconcrack_" + Item.b(itemstack.getItem());

                if (itemstack.usesData()) {
                    s = s + "_" + itemstack.getData();
                }

                this.world.addParticle(s, vec3d1.c, vec3d1.d, vec3d1.e, vec3d.c, vec3d.d + 0.05D, vec3d.e);
            }

            this.makeSound("random.eat", 0.5F + 0.5F * (float) this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected void p() {
        if (this.f != null) {
            this.c(this.f, 16);
            int i = this.f.count;

            // CraftBukkit start
            org.bukkit.inventory.ItemStack craftItem = CraftItemStack.asBukkitCopy(this.f);
            PlayerItemConsumeEvent event = new PlayerItemConsumeEvent((Player) this.getBukkitEntity(), craftItem);
            world.getServer().getPluginManager().callEvent(event);

            if (event.isCancelled()) {
                // Update client
                if (this instanceof EntityPlayer) {
                    ((EntityPlayer) this).playerConnection.sendPacket(new PacketPlayOutSetSlot((byte) 0, activeContainer.a((IInventory) this.inventory, this.inventory.itemInHandIndex).index, this.f));
                }
                return;
            }

            // Plugin modified the item, process it but don't remove it
            if (!craftItem.equals(event.getItem())) {
                CraftItemStack.asNMSCopy(event.getItem()).b(this.world, this);

                // Update client
                if (this instanceof EntityPlayer) {
                    ((EntityPlayer) this).playerConnection.sendPacket(new PacketPlayOutSetSlot((byte) 0, activeContainer.a((IInventory) this.inventory, this.inventory.itemInHandIndex).index, this.f));
                }
                return;
            }
            // CraftBukkit end

            ItemStack itemstack = this.f.b(this.world, this);

            if (itemstack != this.f || itemstack != null && itemstack.count != i) {
                this.inventory.items[this.inventory.itemInHandIndex] = itemstack;
                if (itemstack.count == 0) {
                    this.inventory.items[this.inventory.itemInHandIndex] = null;
                }
            }

            this.bz();
        }
    }

    protected boolean bh() {
        return this.getHealth() <= 0.0F || this.isSleeping();
    }

    // CraftBukkit - protected -> public
    public void closeInventory() {
        this.activeContainer = this.defaultContainer;
    }

    public void mount(Entity entity) {
        // CraftBukkit start - mirror Entity mount changes
        this.setPassengerOf(entity);
    }

    public void setPassengerOf(Entity entity) {
        // CraftBukkit end
        if (this.vehicle != null && entity == null) {
            // CraftBukkit start - use parent method instead to correctly fire VehicleExitEvent
            Entity originalVehicle = this.vehicle;
            // First statement moved down, second statement handled in parent method.
            /*
            if (!this.world.isStatic) {
                this.l(this.vehicle);
            }

            if (this.vehicle != null) {
                this.vehicle.passenger = null;
            }

            this.vehicle = null;
            */
            super.setPassengerOf(entity);
            if (!this.world.isStatic && this.vehicle == null) {
                this.l(originalVehicle);
            }
            // CraftBukkit end
        } else {
            super.setPassengerOf(entity); // CraftBukkit - call new parent
        }
    }

    public void ab() {
        if (!this.world.isStatic && this.isSneaking()) {
            this.mount((Entity) null);
            this.setSneaking(false);
        } else {
            double d0 = this.locX;
            double d1 = this.locY;
            double d2 = this.locZ;
            float f = this.yaw;
            float f1 = this.pitch;

            super.ab();
            this.bs = this.bt;
            this.bt = 0.0F;
            this.l(this.locX - d0, this.locY - d1, this.locZ - d2);
            if (this.vehicle instanceof EntityPig) {
                this.pitch = f1;
                this.yaw = f;
                this.aN = ((EntityPig) this.vehicle).aN;
            }
        }
    }

    protected void bq() {
        super.bq();
        this.bb();
    }

    public void e() {
        if (this.br > 0) {
            --this.br;
        }

        if (this.world.difficulty == EnumDifficulty.PEACEFUL && this.getHealth() < this.getMaxHealth() && this.world.getGameRules().getBoolean("naturalRegeneration") && this.ticksLived % 20 * 12 == 0) {
            // CraftBukkit - added regain reason of "REGEN" for filtering purposes.
            this.heal(1.0F, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.REGEN);
        }

        this.inventory.k();
        this.bs = this.bt;
        super.e();
        AttributeInstance attributeinstance = this.getAttributeInstance(GenericAttributes.d);

        if (!this.world.isStatic) {
            attributeinstance.setValue((double) this.abilities.b());
        }

        this.aR = this.bK;
        if (this.isSprinting()) {
            this.aR = (float) ((double) this.aR + (double) this.bK * 0.3D);
        }

        this.i((float) attributeinstance.getValue());
        float f = MathHelper.sqrt(this.motX * this.motX + this.motZ * this.motZ);
        // CraftBukkit - Math -> TrigMath
        float f1 = (float) org.bukkit.craftbukkit.TrigMath.atan(-this.motY * 0.20000000298023224D) * 15.0F;

        if (f > 0.1F) {
            f = 0.1F;
        }

        if (!this.onGround || this.getHealth() <= 0.0F) {
            f = 0.0F;
        }

        if (this.onGround || this.getHealth() <= 0.0F) {
            f1 = 0.0F;
        }

        this.bt += (f - this.bt) * 0.4F;
        this.aK += (f1 - this.aK) * 0.8F;
        if (this.getHealth() > 0.0F) {
            AxisAlignedBB axisalignedbb = null;

            if (this.vehicle != null && !this.vehicle.dead) {
                axisalignedbb = this.boundingBox.a(this.vehicle.boundingBox).grow(1.0D, 0.0D, 1.0D);
            } else {
                axisalignedbb = this.boundingBox.grow(1.0D, 0.5D, 1.0D);
            }

            List list = this.world.getEntities(this, axisalignedbb);

            if (list != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity) list.get(i);

                    if (!entity.dead) {
                        this.r(entity);
                    }
                }
            }
        }
    }

    private void r(Entity entity) {
        entity.b_(this);
    }

    public int getScore() {
        return this.datawatcher.getInt(18);
    }

    public void setScore(int i) {
        this.datawatcher.watch(18, Integer.valueOf(i));
    }

    public void addScore(int i) {
        int j = this.getScore();

        this.datawatcher.watch(18, Integer.valueOf(j + i));
    }

    public void die(DamageSource damagesource) {
        super.die(damagesource);
        this.a(0.2F, 0.2F);
        this.setPosition(this.locX, this.locY, this.locZ);
        this.motY = 0.10000000149011612D;
        if (this.getName().equals("Notch")) {
            this.a(new ItemStack(Items.APPLE, 1), true, false);
        }

        if (!this.world.getGameRules().getBoolean("keepInventory")) {
            this.inventory.m();
        }

        if (damagesource != null) {
            this.motX = (double) (-MathHelper.cos((this.aA + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
            this.motZ = (double) (-MathHelper.sin((this.aA + this.yaw) * 3.1415927F / 180.0F) * 0.1F);
        } else {
            this.motX = this.motZ = 0.0D;
        }

        this.height = 0.1F;
        this.a(StatisticList.v, 1);
    }

    protected String aT() {
        return "game.player.hurt";
    }

    protected String aU() {
        return "game.player.die";
    }

    public void b(Entity entity, int i) {
        this.addScore(i);
        // CraftBukkit - Get our scores instead
        Collection<ScoreboardScore> collection = this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.e, this.getName(), new java.util.ArrayList<ScoreboardScore>());

        if (entity instanceof EntityHuman) {
            this.a(StatisticList.y, 1);
            // CraftBukkit - Get our scores instead
            this.world.getServer().getScoreboardManager().getScoreboardScores(IScoreboardCriteria.d, this.getName(), collection);
        } else {
            this.a(StatisticList.w, 1);
        }

        Iterator iterator = collection.iterator();

        while (iterator.hasNext()) {
            ScoreboardScore scoreboardscore = (ScoreboardScore) iterator.next(); // CraftBukkit - Use our scores instead

            scoreboardscore.incrementScore();
        }
    }

    public EntityItem a(boolean flag) {
        return this.a(this.inventory.splitStack(this.inventory.itemInHandIndex, flag && this.inventory.getItemInHand() != null ? this.inventory.getItemInHand().count : 1), false, true);
    }

    public EntityItem drop(ItemStack itemstack, boolean flag) {
        return this.a(itemstack, false, false);
    }

    public EntityItem a(ItemStack itemstack, boolean flag, boolean flag1) {
        if (itemstack == null) {
            return null;
        } else if (itemstack.count == 0) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.world, this.locX, this.locY - 0.30000001192092896D + (double) this.getHeadHeight(), this.locZ, itemstack);

            entityitem.pickupDelay = 40;
            if (flag1) {
                entityitem.b(this.getName());
            }

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
            this.a(StatisticList.s, 1);
            return entityitem;
        }
    }

    protected void a(EntityItem entityitem) {
        this.world.addEntity(entityitem);
    }

    public float a(Block block, boolean flag) {
        float f = this.inventory.a(block);

        if (f > 1.0F) {
            int i = EnchantmentManager.getDigSpeedEnchantmentLevel(this);
            ItemStack itemstack = this.inventory.getItemInHand();

            if (i > 0 && itemstack != null) {
                float f1 = (float) (i * i + 1);

                if (!itemstack.b(block) && f <= 1.0F) {
                    f += f1 * 0.08F;
                } else {
                    f += f1;
                }
            }
        }

        if (this.hasEffect(MobEffectList.FASTER_DIG)) {
            f *= 1.0F + (float) (this.getEffect(MobEffectList.FASTER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.hasEffect(MobEffectList.SLOWER_DIG)) {
            f *= 1.0F - (float) (this.getEffect(MobEffectList.SLOWER_DIG).getAmplifier() + 1) * 0.2F;
        }

        if (this.a(Material.WATER) && !EnchantmentManager.hasWaterWorkerEnchantment(this)) {
            f /= 5.0F;
        }

        if (!this.onGround) {
            f /= 5.0F;
        }

        return f;
    }

    public boolean a(Block block) {
        return this.inventory.b(block);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.uniqueID = a(this.i);
        NBTTagList nbttaglist = nbttagcompound.getList("Inventory", 10);

        this.inventory.b(nbttaglist);
        this.inventory.itemInHandIndex = nbttagcompound.getInt("SelectedItemSlot");
        this.sleeping = nbttagcompound.getBoolean("Sleeping");
        this.sleepTicks = nbttagcompound.getShort("SleepTimer");
        this.exp = nbttagcompound.getFloat("XpP");
        this.expLevel = nbttagcompound.getInt("XpLevel");
        this.expTotal = nbttagcompound.getInt("XpTotal");
        this.setScore(nbttagcompound.getInt("Score"));
        if (this.sleeping) {
            this.bC = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
            this.a(true, true, false);
        }

        // CraftBukkit start
        this.spawnWorld = nbttagcompound.getString("SpawnWorld");
        if ("".equals(spawnWorld)) {
            this.spawnWorld = this.world.getServer().getWorlds().get(0).getName();
        }
        // CraftBukkit end

        if (nbttagcompound.hasKeyOfType("SpawnX", 99) && nbttagcompound.hasKeyOfType("SpawnY", 99) && nbttagcompound.hasKeyOfType("SpawnZ", 99)) {
            this.c = new ChunkCoordinates(nbttagcompound.getInt("SpawnX"), nbttagcompound.getInt("SpawnY"), nbttagcompound.getInt("SpawnZ"));
            this.d = nbttagcompound.getBoolean("SpawnForced");
        }

        this.foodData.a(nbttagcompound);
        this.abilities.b(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("EnderItems", 9)) {
            NBTTagList nbttaglist1 = nbttagcompound.getList("EnderItems", 10);

            this.enderChest.a(nbttaglist1);
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.set("Inventory", this.inventory.a(new NBTTagList()));
        nbttagcompound.setInt("SelectedItemSlot", this.inventory.itemInHandIndex);
        nbttagcompound.setBoolean("Sleeping", this.sleeping);
        nbttagcompound.setShort("SleepTimer", (short) this.sleepTicks);
        nbttagcompound.setFloat("XpP", this.exp);
        nbttagcompound.setInt("XpLevel", this.expLevel);
        nbttagcompound.setInt("XpTotal", this.expTotal);
        nbttagcompound.setInt("Score", this.getScore());
        if (this.c != null) {
            nbttagcompound.setInt("SpawnX", this.c.x);
            nbttagcompound.setInt("SpawnY", this.c.y);
            nbttagcompound.setInt("SpawnZ", this.c.z);
            nbttagcompound.setBoolean("SpawnForced", this.d);
            nbttagcompound.setString("SpawnWorld", spawnWorld); // CraftBukkit - fixes bed spawns for multiworld worlds
        }

        this.foodData.b(nbttagcompound);
        this.abilities.a(nbttagcompound);
        nbttagcompound.set("EnderItems", this.enderChest.h());
    }

    public void openContainer(IInventory iinventory) {}

    public void openHopper(TileEntityHopper tileentityhopper) {}

    public void openMinecartHopper(EntityMinecartHopper entityminecarthopper) {}

    public void openHorseInventory(EntityHorse entityhorse, IInventory iinventory) {}

    public void startEnchanting(int i, int j, int k, String s) {}

    public void openAnvil(int i, int j, int k) {}

    public void startCrafting(int i, int j, int k) {}

    public float getHeadHeight() {
        return 0.12F;
    }

    protected void e_() {
        this.height = 1.62F;
    }

    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable()) {
            return false;
        } else if (this.abilities.isInvulnerable && !damagesource.ignoresInvulnerability()) {
            return false;
        } else {
            this.aV = 0;
            if (this.getHealth() <= 0.0F) {
                return false;
            } else {
                if (this.isSleeping() && !this.world.isStatic) {
                    this.a(true, true, false);
                }

                if (damagesource.r()) {
                    if (this.world.difficulty == EnumDifficulty.PEACEFUL) {
                        return false; // CraftBukkit - f = 0.0f -> return false
                    }

                    if (this.world.difficulty == EnumDifficulty.EASY) {
                        f = f / 2.0F + 1.0F;
                    }

                    if (this.world.difficulty == EnumDifficulty.HARD) {
                        f = f * 3.0F / 2.0F;
                    }
                }

                if (false && f == 0.0F) { // CraftBukkit - Don't filter out 0 damage
                    return false;
                } else {
                    Entity entity = damagesource.getEntity();

                    if (entity instanceof EntityArrow && ((EntityArrow) entity).shooter != null) {
                        entity = ((EntityArrow) entity).shooter;
                    }

                    this.a(StatisticList.u, Math.round(f * 10.0F));
                    return super.damageEntity(damagesource, f);
                }
            }
        }
    }

    public boolean a(EntityHuman entityhuman) {
        // CraftBukkit start - Change to check OTHER player's scoreboard team according to API
        // To summarize this method's logic, it's "Can parameter hurt this"
        org.bukkit.scoreboard.Team team;
        if (entityhuman instanceof EntityPlayer) {
            EntityPlayer thatPlayer = (EntityPlayer) entityhuman;
            team = thatPlayer.getBukkitEntity().getScoreboard().getPlayerTeam(thatPlayer.getBukkitEntity());
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        } else {
            // This should never be called, but is implemented anyway
            org.bukkit.OfflinePlayer thisPlayer = entityhuman.world.getServer().getOfflinePlayer(entityhuman.getName());
            team = entityhuman.world.getServer().getScoreboardManager().getMainScoreboard().getPlayerTeam(thisPlayer);
            if (team == null || team.allowFriendlyFire()) {
                return true;
            }
        }

        if (this instanceof EntityPlayer) {
            return !team.hasPlayer(((EntityPlayer) this).getBukkitEntity());
        }
        return !team.hasPlayer(this.world.getServer().getOfflinePlayer(this.getName()));
        // CraftBukkit end
    }

    protected void h(float f) {
        this.inventory.a(f);
    }

    public int aV() {
        return this.inventory.l();
    }

    public float bC() {
        int i = 0;
        ItemStack[] aitemstack = this.inventory.armor;
        int j = aitemstack.length;

        for (int k = 0; k < j; ++k) {
            ItemStack itemstack = aitemstack[k];

            if (itemstack != null) {
                ++i;
            }
        }

        return (float) i / (float) this.inventory.armor.length;
    }

    protected void d(DamageSource damagesource, float f) {
        if (!this.isInvulnerable()) {
            if (!damagesource.ignoresArmor() && this.isBlocking() && f > 0.0F) {
                f = (1.0F + f) * 0.5F;
            }

            f = this.b(damagesource, f);
            f = this.c(damagesource, f);
            float f1 = f;

            f = Math.max(f - this.bs(), 0.0F);
            this.m(this.bs() - (f1 - f));
            if (f != 0.0F) {
                this.a(damagesource.f());
                float f2 = this.getHealth();

                this.setHealth(this.getHealth() - f);
                this.aW().a(damagesource, f2, f);
            }
        }
    }

    public void openFurnace(TileEntityFurnace tileentityfurnace) {}

    public void openDispenser(TileEntityDispenser tileentitydispenser) {}

    public void a(TileEntity tileentity) {}

    public void a(CommandBlockListenerAbstract commandblocklistenerabstract) {}

    public void openBrewingStand(TileEntityBrewingStand tileentitybrewingstand) {}

    public void openBeacon(TileEntityBeacon tileentitybeacon) {}

    public void openTrade(IMerchant imerchant, String s) {}

    public void b(ItemStack itemstack) {}

    public boolean p(Entity entity) {
        ItemStack itemstack = this.bD();
        ItemStack itemstack1 = itemstack != null ? itemstack.cloneItemStack() : null;

        if (!entity.c(this)) {
            if (itemstack != null && entity instanceof EntityLiving) {
                if (this.abilities.canInstantlyBuild) {
                    itemstack = itemstack1;
                }

                if (itemstack.a(this, (EntityLiving) entity)) {
                    // CraftBukkit - bypass infinite items; <= 0 -> == 0
                    if (itemstack.count == 0 && !this.abilities.canInstantlyBuild) {
                        this.bE();
                    }

                    return true;
                }
            }

            return false;
        } else {
            if (itemstack != null && itemstack == this.bD()) {
                if (itemstack.count <= 0 && !this.abilities.canInstantlyBuild) {
                    this.bE();
                } else if (itemstack.count < itemstack1.count && this.abilities.canInstantlyBuild) {
                    itemstack.count = itemstack1.count;
                }
            }

            return true;
        }
    }

    public ItemStack bD() {
        return this.inventory.getItemInHand();
    }

    public void bE() {
        this.inventory.setItem(this.inventory.itemInHandIndex, (ItemStack) null);
    }

    public double ad() {
        return (double) (this.height - 0.5F);
    }

    public void attack(Entity entity) {
        if (entity.av()) {
            if (!entity.i(this)) {
                float f = (float) this.getAttributeInstance(GenericAttributes.e).getValue();
                int i = 0;
                float f1 = 0.0F;

                if (entity instanceof EntityLiving) {
                    f1 = EnchantmentManager.a((EntityLiving) this, (EntityLiving) entity);
                    i += EnchantmentManager.getKnockbackEnchantmentLevel(this, (EntityLiving) entity);
                }

                if (this.isSprinting()) {
                    ++i;
                }

                if (f > 0.0F || f1 > 0.0F) {
                    boolean flag = this.fallDistance > 0.0F && !this.onGround && !this.h_() && !this.M() && !this.hasEffect(MobEffectList.BLINDNESS) && this.vehicle == null && entity instanceof EntityLiving;

                    if (flag && f > 0.0F) {
                        f *= 1.5F;
                    }

                    f += f1;
                    boolean flag1 = false;
                    int j = EnchantmentManager.getFireAspectEnchantmentLevel(this);

                    if (entity instanceof EntityLiving && j > 0 && !entity.isBurning()) {
                        flag1 = true;
                        entity.setOnFire(1);
                    }

                    boolean flag2 = entity.damageEntity(DamageSource.playerAttack(this), f);

                    if (flag2) {
                        if (i > 0) {
                            entity.g((double) (-MathHelper.sin(this.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F), 0.1D, (double) (MathHelper.cos(this.yaw * 3.1415927F / 180.0F) * (float) i * 0.5F));
                            this.motX *= 0.6D;
                            this.motZ *= 0.6D;
                            this.setSprinting(false);
                        }

                        if (flag) {
                            this.b(entity);
                        }

                        if (f1 > 0.0F) {
                            this.c(entity);
                        }

                        if (f >= 18.0F) {
                            this.a((Statistic) AchievementList.F);
                        }

                        this.k(entity);
                        if (entity instanceof EntityLiving) {
                            EnchantmentManager.a((EntityLiving) entity, (Entity) this);
                        }

                        EnchantmentManager.b(this, entity);
                        ItemStack itemstack = this.bD();
                        Object object = entity;

                        if (entity instanceof EntityComplexPart) {
                            IComplex icomplex = ((EntityComplexPart) entity).owner;

                            if (icomplex != null && icomplex instanceof EntityLiving) {
                                object = (EntityLiving) icomplex;
                            }
                        }

                        if (itemstack != null && object instanceof EntityLiving) {
                            itemstack.a((EntityLiving) object, this);
                            // CraftBukkit - bypass infinite items; <= 0 -> == 0
                            if (itemstack.count == 0) {
                                this.bE();
                            }
                        }

                        if (entity instanceof EntityLiving) {
                            this.a(StatisticList.t, Math.round(f * 10.0F));
                            if (j > 0) {
                                // CraftBukkit start - Call a combust event when somebody hits with a fire enchanted item
                                EntityCombustByEntityEvent combustEvent = new EntityCombustByEntityEvent(this.getBukkitEntity(), entity.getBukkitEntity(), j * 4);
                                org.bukkit.Bukkit.getPluginManager().callEvent(combustEvent);

                                if (!combustEvent.isCancelled()) {
                                    entity.setOnFire(combustEvent.getDuration());
                                }
                                // CraftBukkit end
                            }
                        }

                        this.a(0.3F);
                    } else if (flag1) {
                        entity.extinguish();
                    }
                }
            }
        }
    }

    public void b(Entity entity) {}

    public void c(Entity entity) {}

    public void die() {
        super.die();
        this.defaultContainer.b(this);
        if (this.activeContainer != null) {
            this.activeContainer.b(this);
        }
    }

    public boolean inBlock() {
        return !this.sleeping && super.inBlock();
    }

    public GameProfile getProfile() {
        return this.i;
    }

    public EnumBedResult a(int i, int j, int k) {
        if (!this.world.isStatic) {
            if (this.isSleeping() || !this.isAlive()) {
                return EnumBedResult.OTHER_PROBLEM;
            }

            if (!this.world.worldProvider.d()) {
                return EnumBedResult.NOT_POSSIBLE_HERE;
            }

            if (this.world.v()) {
                return EnumBedResult.NOT_POSSIBLE_NOW;
            }

            if (Math.abs(this.locX - (double) i) > 3.0D || Math.abs(this.locY - (double) j) > 2.0D || Math.abs(this.locZ - (double) k) > 3.0D) {
                return EnumBedResult.TOO_FAR_AWAY;
            }

            double d0 = 8.0D;
            double d1 = 5.0D;
            List list = this.world.a(EntityMonster.class, AxisAlignedBB.a().a((double) i - d0, (double) j - d1, (double) k - d0, (double) i + d0, (double) j + d1, (double) k + d0));

            if (!list.isEmpty()) {
                return EnumBedResult.NOT_SAFE;
            }
        }

        if (this.am()) {
            this.mount((Entity) null);
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

        this.a(0.2F, 0.2F);
        this.height = 0.2F;
        if (this.world.isLoaded(i, j, k)) {
            int l = this.world.getData(i, j, k);
            int i1 = BlockBed.l(l);
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

            this.w(i1);
            this.setPosition((double) ((float) i + f), (double) ((float) j + 0.9375F), (double) ((float) k + f1));
        } else {
            this.setPosition((double) ((float) i + 0.5F), (double) ((float) j + 0.9375F), (double) ((float) k + 0.5F));
        }

        this.sleeping = true;
        this.sleepTicks = 0;
        this.bC = new ChunkCoordinates(i, j, k);
        this.motX = this.motZ = this.motY = 0.0D;
        if (!this.world.isStatic) {
            this.world.everyoneSleeping();
        }

        return EnumBedResult.OK;
    }

    private void w(int i) {
        this.bD = 0.0F;
        this.bE = 0.0F;
        switch (i) {
        case 0:
            this.bE = -1.8F;
            break;

        case 1:
            this.bD = 1.8F;
            break;

        case 2:
            this.bE = 1.8F;
            break;

        case 3:
            this.bD = -1.8F;
        }
    }

    public void a(boolean flag, boolean flag1, boolean flag2) {
        this.a(0.6F, 1.8F);
        this.e_();
        ChunkCoordinates chunkcoordinates = this.bC;
        ChunkCoordinates chunkcoordinates1 = this.bC;

        if (chunkcoordinates != null && this.world.getType(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Blocks.BED) {
            BlockBed.a(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, false);
            chunkcoordinates1 = BlockBed.a(this.world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);
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
            this.setRespawnPosition(this.bC, false);
        }
    }

    private boolean j() {
        return this.world.getType(this.bC.x, this.bC.y, this.bC.z) == Blocks.BED;
    }

    public static ChunkCoordinates getBed(World world, ChunkCoordinates chunkcoordinates, boolean flag) {
        IChunkProvider ichunkprovider = world.K();

        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z - 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x - 3 >> 4, chunkcoordinates.z + 3 >> 4);
        ichunkprovider.getChunkAt(chunkcoordinates.x + 3 >> 4, chunkcoordinates.z + 3 >> 4);
        if (world.getType(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z) == Blocks.BED) {
            ChunkCoordinates chunkcoordinates1 = BlockBed.a(world, chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, 0);

            return chunkcoordinates1;
        } else {
            Material material = world.getType(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z).getMaterial();
            Material material1 = world.getType(chunkcoordinates.x, chunkcoordinates.y + 1, chunkcoordinates.z).getMaterial();
            boolean flag1 = !material.isBuildable() && !material.isLiquid();
            boolean flag2 = !material1.isBuildable() && !material1.isLiquid();

            return flag && flag1 && flag2 ? chunkcoordinates : null;
        }
    }

    public boolean isSleeping() {
        return this.sleeping;
    }

    public boolean isDeeplySleeping() {
        return this.sleeping && this.sleepTicks >= 100;
    }

    protected void b(int i, boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1 << i)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & ~(1 << i))));
        }
    }

    public void b(IChatBaseComponent ichatbasecomponent) {}

    public ChunkCoordinates getBed() {
        return this.c;
    }

    public boolean isRespawnForced() {
        return this.d;
    }

    public void setRespawnPosition(ChunkCoordinates chunkcoordinates, boolean flag) {
        if (chunkcoordinates != null) {
            this.c = new ChunkCoordinates(chunkcoordinates);
            this.d = flag;
            this.spawnWorld = this.world.worldData.getName(); // CraftBukkit
        } else {
            this.c = null;
            this.d = false;
            this.spawnWorld = ""; // CraftBukkit
        }
    }

    public void a(Statistic statistic) {
        this.a(statistic, 1);
    }

    public void a(Statistic statistic, int i) {}

    public void bj() {
        super.bj();
        this.a(StatisticList.r, 1);
        if (this.isSprinting()) {
            this.a(0.8F);
        } else {
            this.a(0.2F);
        }
    }

    public void e(float f, float f1) {
        double d0 = this.locX;
        double d1 = this.locY;
        double d2 = this.locZ;

        if (this.abilities.isFlying && this.vehicle == null) {
            double d3 = this.motY;
            float f2 = this.aR;

            this.aR = this.abilities.a();
            super.e(f, f1);
            this.motY = d3 * 0.6D;
            this.aR = f2;
        } else {
            super.e(f, f1);
        }

        this.checkMovement(this.locX - d0, this.locY - d1, this.locZ - d2);
    }

    public float bl() {
        return (float) this.getAttributeInstance(GenericAttributes.d).getValue();
    }

    public void checkMovement(double d0, double d1, double d2) {
        if (this.vehicle == null) {
            int i;

            if (this.a(Material.WATER)) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.m, i);
                    this.a(0.015F * (float) i * 0.01F);
                }
            } else if (this.M()) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.i, i);
                    this.a(0.015F * (float) i * 0.01F);
                }
            } else if (this.h_()) {
                if (d1 > 0.0D) {
                    this.a(StatisticList.k, (int) Math.round(d1 * 100.0D));
                }
            } else if (this.onGround) {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 0) {
                    this.a(StatisticList.h, i);
                    if (this.isSprinting()) {
                        this.a(0.099999994F * (float) i * 0.01F);
                    } else {
                        this.a(0.01F * (float) i * 0.01F);
                    }
                }
            } else {
                i = Math.round(MathHelper.sqrt(d0 * d0 + d2 * d2) * 100.0F);
                if (i > 25) {
                    this.a(StatisticList.l, i);
                }
            }
        }
    }

    private void l(double d0, double d1, double d2) {
        if (this.vehicle != null) {
            int i = Math.round(MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) * 100.0F);

            if (i > 0) {
                if (this.vehicle instanceof EntityMinecartAbstract) {
                    this.a(StatisticList.n, i);
                    if (this.e == null) {
                        this.e = new ChunkCoordinates(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ));
                    } else if ((double) this.e.e(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)) >= 1000000.0D) {
                        this.a((Statistic) AchievementList.q, 1);
                    }
                } else if (this.vehicle instanceof EntityBoat) {
                    this.a(StatisticList.o, i);
                } else if (this.vehicle instanceof EntityPig) {
                    this.a(StatisticList.p, i);
                } else if (this.vehicle instanceof EntityHorse) {
                    this.a(StatisticList.q, i);
                }
            }
        }
    }

    protected void b(float f) {
        if (!this.abilities.canFly) {
            if (f >= 2.0F) {
                this.a(StatisticList.j, (int) Math.round((double) f * 100.0D));
            }

            super.b(f);
        }
    }

    protected String o(int i) {
        return i > 4 ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
    }

    public void a(EntityLiving entityliving) {
        if (entityliving instanceof IMonster) {
            this.a((Statistic) AchievementList.s);
        }

        int i = EntityTypes.a(entityliving);
        MonsterEggInfo monsteregginfo = (MonsterEggInfo) EntityTypes.a.get(Integer.valueOf(i));

        if (monsteregginfo != null) {
            this.a(monsteregginfo.d, 1);
        }
    }

    public void as() {
        if (!this.abilities.isFlying) {
            super.as();
        }
    }

    public ItemStack r(int i) {
        return this.inventory.d(i);
    }

    public void giveExp(int i) {
        this.addScore(i);
        int j = Integer.MAX_VALUE - this.expTotal;

        if (i > j) {
            i = j;
        }

        this.exp += (float) i / (float) this.getExpToLevel();

        for (this.expTotal += i; this.exp >= 1.0F; this.exp /= (float) this.getExpToLevel()) {
            this.exp = (this.exp - 1.0F) * (float) this.getExpToLevel();
            this.levelDown(1);
        }
    }

    public void levelDown(int i) {
        this.expLevel += i;
        if (this.expLevel < 0) {
            this.expLevel = 0;
            this.exp = 0.0F;
            this.expTotal = 0;
        }

        if (i > 0 && this.expLevel % 5 == 0 && (float) this.h < (float) this.ticksLived - 100.0F) {
            float f = this.expLevel > 30 ? 1.0F : (float) this.expLevel / 30.0F;

            this.world.makeSound(this, "random.levelup", f * 0.75F, 1.0F);
            this.h = this.ticksLived;
        }
    }

    public int getExpToLevel() {
        return this.expLevel >= 30 ? 62 + (this.expLevel - 30) * 7 : (this.expLevel >= 15 ? 17 + (this.expLevel - 15) * 3 : 17);
    }

    public void a(float f) {
        if (!this.abilities.isInvulnerable) {
            if (!this.world.isStatic) {
                this.foodData.a(f);
            }
        }
    }

    public FoodMetaData getFoodData() {
        return this.foodData;
    }

    public boolean g(boolean flag) {
        return (flag || this.foodData.c()) && !this.abilities.isInvulnerable;
    }

    public boolean bP() {
        return this.getHealth() > 0.0F && this.getHealth() < this.getMaxHealth();
    }

    public void a(ItemStack itemstack, int i) {
        if (itemstack != this.f) {
            this.f = itemstack;
            this.g = i;
            if (!this.world.isStatic) {
                this.e(true);
            }
        }
    }

    public boolean d(int i, int j, int k) {
        if (this.abilities.mayBuild) {
            return true;
        } else {
            Block block = this.world.getType(i, j, k);

            if (block.getMaterial() != Material.AIR) {
                if (block.getMaterial().q()) {
                    return true;
                }

                if (this.bD() != null) {
                    ItemStack itemstack = this.bD();

                    if (itemstack.b(block) || itemstack.a(block) > 1.0F) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public boolean a(int i, int j, int k, int l, ItemStack itemstack) {
        return this.abilities.mayBuild ? true : (itemstack != null ? itemstack.z() : false);
    }

    protected int getExpValue(EntityHuman entityhuman) {
        if (this.world.getGameRules().getBoolean("keepInventory")) {
            return 0;
        } else {
            int i = this.expLevel * 7;

            return i > 100 ? 100 : i;
        }
    }

    protected boolean alwaysGivesExp() {
        return true;
    }

    public void copyTo(EntityHuman entityhuman, boolean flag) {
        if (flag) {
            this.inventory.b(entityhuman.inventory);
            this.setHealth(entityhuman.getHealth());
            this.foodData = entityhuman.foodData;
            this.expLevel = entityhuman.expLevel;
            this.expTotal = entityhuman.expTotal;
            this.exp = entityhuman.exp;
            this.setScore(entityhuman.getScore());
            this.ar = entityhuman.ar;
        } else if (this.world.getGameRules().getBoolean("keepInventory")) {
            this.inventory.b(entityhuman.inventory);
            this.expLevel = entityhuman.expLevel;
            this.expTotal = entityhuman.expTotal;
            this.exp = entityhuman.exp;
            this.setScore(entityhuman.getScore());
        }

        this.enderChest = entityhuman.enderChest;
    }

    protected boolean g_() {
        return !this.abilities.isFlying;
    }

    public void updateAbilities() {}

    public void a(EnumGamemode enumgamemode) {}

    public String getName() {
        return this.i.getName();
    }

    public World getWorld() {
        return this.world;
    }

    public InventoryEnderChest getEnderChest() {
        return this.enderChest;
    }

    public ItemStack getEquipment(int i) {
        return i == 0 ? this.inventory.getItemInHand() : this.inventory.armor[i - 1];
    }

    public ItemStack be() {
        return this.inventory.getItemInHand();
    }

    public void setEquipment(int i, ItemStack itemstack) {
        this.inventory.armor[i] = itemstack;
    }

    public ItemStack[] getEquipment() {
        return this.inventory.armor;
    }

    public boolean aC() {
        return !this.abilities.isFlying;
    }

    public Scoreboard getScoreboard() {
        return this.world.getScoreboard();
    }

    public ScoreboardTeamBase getScoreboardTeam() {
        return this.getScoreboard().getPlayerTeam(this.getName());
    }

    public IChatBaseComponent getScoreboardDisplayName() {
        // CraftBukkit todo: fun
        ChatComponentText chatcomponenttext = new ChatComponentText(ScoreboardTeam.getPlayerDisplayName(this.getScoreboardTeam(), this.getName()));

        chatcomponenttext.b().a(new ChatClickable(EnumClickAction.SUGGEST_COMMAND, "/msg " + this.getName() + " "));
        return chatcomponenttext;
    }

    public void m(float f) {
        if (f < 0.0F) {
            f = 0.0F;
        }

        this.getDataWatcher().watch(17, Float.valueOf(f));
    }

    public float bs() {
        return this.getDataWatcher().getFloat(17);
    }

    public static UUID a(GameProfile gameprofile) {
        UUID uuid = UtilUUID.b(gameprofile.getId());

        if (uuid == null) {
            uuid = UUID.nameUUIDFromBytes(("OfflinePlayer:" + gameprofile.getName()).getBytes(Charsets.UTF_8));
        }

        return uuid;
    }
}
