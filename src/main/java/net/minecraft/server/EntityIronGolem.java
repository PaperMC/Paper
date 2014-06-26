package net.minecraft.server;

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

    protected void c() {
        super.c();
        this.datawatcher.a(16, Byte.valueOf((byte) 0));
    }

    public boolean bk() {
        return true;
    }

    protected void bp() {
        if (--this.bq <= 0) {
            this.bq = 70 + this.random.nextInt(50);
            this.bp = this.world.villages.getClosestVillage(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ), 32);
            if (this.bp == null) {
                this.bX();
            } else {
                ChunkCoordinates chunkcoordinates = this.bp.getCenter();

                this.a(chunkcoordinates.x, chunkcoordinates.y, chunkcoordinates.z, (int) ((float) this.bp.getSize() * 0.6F));
            }
        }

        super.bp();
    }

    protected void aD() {
        super.aD();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
        this.getAttributeInstance(GenericAttributes.d).setValue(0.25D);
    }

    protected int j(int i) {
        return i;
    }

    protected void o(Entity entity) {
        if (entity instanceof IMonster && this.aI().nextInt(20) == 0) {
            // CraftBukkit start
            org.bukkit.event.entity.EntityTargetLivingEntityEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callEntityTargetLivingEvent(this, (EntityLiving) entity, org.bukkit.event.entity.EntityTargetEvent.TargetReason.COLLISION);
            if (!event.isCancelled()) {
                if (event.getTarget() == null) {
                    this.setGoalTarget(null);
                } else {
                    this.setGoalTarget(((org.bukkit.craftbukkit.entity.CraftLivingEntity) event.getTarget()).getHandle());
                }
            }
            // CraftBukkit end
        }

        super.o(entity);
    }

    public void e() {
        super.e();
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
            Block block = this.world.getType(i, j, k);

            if (block.getMaterial() != Material.AIR) {
                this.world.addParticle("blockcrack_" + Block.getId(block) + "_" + this.world.getData(i, j, k), this.locX + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, this.boundingBox.b + 0.1D, this.locZ + ((double) this.random.nextFloat() - 0.5D) * (double) this.width, 4.0D * ((double) this.random.nextFloat() - 0.5D), 0.5D, ((double) this.random.nextFloat() - 0.5D) * 4.0D);
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

    public boolean n(Entity entity) {
        this.br = 10;
        this.world.broadcastEntityEffect(this, (byte) 4);
        boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (float) (7 + this.random.nextInt(15)));

        if (flag) {
            entity.motY += 0.4000000059604645D;
        }

        this.makeSound("mob.irongolem.throw", 1.0F, 1.0F);
        return flag;
    }

    public Village bZ() {
        return this.bp;
    }

    public void a(boolean flag) {
        this.bs = flag ? 400 : 0;
        this.world.broadcastEntityEffect(this, (byte) 11);
    }

    protected String aT() {
        return "mob.irongolem.hit";
    }

    protected String aU() {
        return "mob.irongolem.death";
    }

    protected void a(int i, int j, int k, Block block) {
        this.makeSound("mob.irongolem.walk", 1.0F, 1.0F);
    }

    protected void dropDeathLoot(boolean flag, int i) {
        int j = this.random.nextInt(3);

        int k;

        for (k = 0; k < j; ++k) {
            this.a(Item.getItemOf(Blocks.RED_ROSE), 1, 0.0F);
        }

        k = 3 + this.random.nextInt(3);

        for (int l = 0; l < k; ++l) {
            this.a(Items.IRON_INGOT, 1);
        }
    }

    public int cb() {
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
