package net.minecraft.server;

import org.bukkit.craftbukkit.inventory.CraftItemStack; // CraftBukkit

public class EntityIronGolem extends EntityGolem {

    private int bq;
    Village bp;
    private int br;
    private int bs;

    public EntityIronGolem(World world) {
        super(world);
        this.a(1.4F, 2.9F);
        this.getNavigation().a(true);
        this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1.0D, true));
        this.goalSelector.a(2, new PathfinderGoalMoveTowardsTarget(this, 0.9D, 32.0F));
        this.goalSelector.a(3, new PathfinderGoalMoveThroughVillage(this, 0.6D, true));
        this.goalSelector.a(4, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
        this.goalSelector.a(5, new PathfinderGoalOfferFlower(this));
        this.goalSelector.a(6, new PathfinderGoalRandomStroll(this, 0.6D));
        this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
        this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalDefendVillage(this));
        this.targetSelector.a(2, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget(this, EntityInsentient.class, 0, false, true, IMonster.a));
    }

    protected void a() {
        super.a();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public boolean bf() {
        return true;
    }

    protected void bk() {
        if (--this.bq <= 0) {
            this.bq = 70 + this.random.nextInt(50);
            this.bp = this.world.villages.getClosestVillage(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ), 32);
            if (this.bp == null) {
                this.bR();
            } else {
                ChunkCoordinates chunkcoordinates = this.bp.getCenter();

                this.b(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, (int) ((float) this.bp.getSize() * 0.6F));
            }
        }

        super.bk();
    }

    protected void az() {
        super.az();
        this.getAttributeInstance(GenericAttributes.a).setValue(100.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.25D);
    }

    protected int h(int i) {
        return i;
    }

    protected void n(Entity entity) {
        if (entity instanceof IMonster && this.aD().nextInt(20) == 0) {
            this.setGoalTarget((EntityLiving) entity);
        }

        super.n(entity);
    }

    public void c() {
        super.c();
        if (this.br > 0) {
            --this.br;
        }

        if (this.bs > 0) {
            --this.bs;
        }

        if (this.motX * this.motX + this.motZ * this.motZ > 2.500000277905201E-7D && this.random.nextInt(5) == 0) {
            int i = MathHelper.floor(this.locX);
            int j = MathHelper.floor(this.locY - 0.20000000298023224D - (double) this.height);
            int k = MathHelper.floor(this.locZ);
            int l = this.world.getTypeId(i, j, k);

            if (l > 0) {
                this.world.addParticle("tilecrack_" + l + "_" + this.world.getData(i, j, k), this.locX + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.b + 0.1D, this.locZ + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, 4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
            }
        }
    }

    public boolean a(Class oclass) {
        return this.isPlayerCreated() && EntityHuman.class.isAssignableFrom(oclass) ? false : super.a(oclass);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("PlayerCreated", this.isPlayerCreated());
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.setPlayerCreated(nbttagcompound.getBoolean("PlayerCreated"));
    }

    public boolean m(Entity entity) {
        this.br = 10;
        this.world.broadcastEntityEffect(this, (byte) 4);
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (float) (7 + this.random.nextInt(15)));

        if (flag) {
            entity.motY += 0.4000000059604645D;
        }

        this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    public Village bT() {
        return this.bp;
    }

    public void a(boolean flag) {
        this.bs = flag ? 400 : 0;
        this.world.broadcastEntityEffect(this, (byte) 11);
    }

    protected String r() {
        return "none";
    }

    protected String aO() {
        return "mob.irongolem.hit";
    }

    protected String aP() {
        return "mob.irongolem.death";
    }

    protected void a(int i, int j, int k, int l) {
        this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();
        int j = this.random.nextInt(3);

        int k;

        if (j > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Item.byId[Block.RED_ROSE.id], j));
        }

        k = 3 + this.random.nextInt(3);

        if (k > 0) {
            loot.add(CraftItemStack.asNewCraftStack(Item.IRON_INGOT, k));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public int bV() {
        return this.bs;
    }

    public boolean isPlayerCreated() {
        return (this.datawatcher.getByte(16) & 1) != 0;
    }

    public void setPlayerCreated(boolean flag) {
        byte b0 = this.datawatcher.getByte(16);

        if (flag) {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 | 1)));
        } else {
            this.datawatcher.watch(16, Byte.valueOf((byte) (b0 & -2)));
        }
    }

    public void die(DamageSource damagesource) {
        if (!this.isPlayerCreated() && this.killer != null && this.bp != null) {
            this.bp.a(this.killer.getName(), -5);
        }

        super.die(damagesource);
    }
}
