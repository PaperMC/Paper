package net.minecraft.server;

import java.util.Map.Entry;

public class EntityExperienceOrb extends Entity {

    public int b;
    public int c;
    public int d;
    private int e;
    public int value;
    private EntityHuman targetPlayer;
    private int targetTime;

    public EntityExperienceOrb(World world, double d0, double d1, double d2, int i) {
        this(EntityTypes.EXPERIENCE_ORB, world);
        this.setPosition(d0, d1, d2);
        this.yaw = (float) (this.random.nextDouble() * 360.0D);
        this.setMot((this.random.nextDouble() * 0.20000000298023224D - 0.10000000149011612D) * 2.0D, this.random.nextDouble() * 0.2D * 2.0D, (this.random.nextDouble() * 0.20000000298023224D - 0.10000000149011612D) * 2.0D);
        this.value = i;
    }

    public EntityExperienceOrb(EntityTypes<? extends EntityExperienceOrb> entitytypes, World world) {
        super(entitytypes, world);
        this.e = 5;
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected void initDatawatcher() {}

    @Override
    public void tick() {
        super.tick();
        if (this.d > 0) {
            --this.d;
        }

        this.lastX = this.locX();
        this.lastY = this.locY();
        this.lastZ = this.locZ();
        if (this.a((Tag) TagsFluid.WATER)) {
            this.i();
        } else if (!this.isNoGravity()) {
            this.setMot(this.getMot().add(0.0D, -0.03D, 0.0D));
        }

        if (this.world.getFluid(this.getChunkCoordinates()).a((Tag) TagsFluid.LAVA)) {
            this.setMot((double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F), 0.20000000298023224D, (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F));
            this.playSound(SoundEffects.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        if (!this.world.b(this.getBoundingBox())) {
            this.l(this.locX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0D, this.locZ());
        }

        double d0 = 8.0D;

        if (this.targetTime < this.b - 20 + this.getId() % 100) {
            if (this.targetPlayer == null || this.targetPlayer.h((Entity) this) > 64.0D) {
                this.targetPlayer = this.world.findNearbyPlayer(this, 8.0D);
            }

            this.targetTime = this.b;
        }

        if (this.targetPlayer != null && this.targetPlayer.isSpectator()) {
            this.targetPlayer = null;
        }

        if (this.targetPlayer != null) {
            Vec3D vec3d = new Vec3D(this.targetPlayer.locX() - this.locX(), this.targetPlayer.locY() + (double) this.targetPlayer.getHeadHeight() / 2.0D - this.locY(), this.targetPlayer.locZ() - this.locZ());
            double d1 = vec3d.g();

            if (d1 < 64.0D) {
                double d2 = 1.0D - Math.sqrt(d1) / 8.0D;

                this.setMot(this.getMot().e(vec3d.d().a(d2 * d2 * 0.1D)));
            }
        }

        this.move(EnumMoveType.SELF, this.getMot());
        float f = 0.98F;

        if (this.onGround) {
            f = this.world.getType(new BlockPosition(this.locX(), this.locY() - 1.0D, this.locZ())).getBlock().getFrictionFactor() * 0.98F;
        }

        this.setMot(this.getMot().d((double) f, 0.98D, (double) f));
        if (this.onGround) {
            this.setMot(this.getMot().d(1.0D, -0.9D, 1.0D));
        }

        ++this.b;
        ++this.c;
        if (this.c >= 6000) {
            this.die();
        }

    }

    private void i() {
        Vec3D vec3d = this.getMot();

        this.setMot(vec3d.x * 0.9900000095367432D, Math.min(vec3d.y + 5.000000237487257E-4D, 0.05999999865889549D), vec3d.z * 0.9900000095367432D);
    }

    @Override
    protected void aL() {}

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else {
            this.velocityChanged();
            this.e = (int) ((float) this.e - f);
            if (this.e <= 0) {
                this.die();
            }

            return false;
        }
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        nbttagcompound.setShort("Health", (short) this.e);
        nbttagcompound.setShort("Age", (short) this.c);
        nbttagcompound.setShort("Value", (short) this.value);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        this.e = nbttagcompound.getShort("Health");
        this.c = nbttagcompound.getShort("Age");
        this.value = nbttagcompound.getShort("Value");
    }

    @Override
    public void pickup(EntityHuman entityhuman) {
        if (!this.world.isClientSide) {
            if (this.d == 0 && entityhuman.bu == 0) {
                entityhuman.bu = 2;
                entityhuman.receive(this, 1);
                Entry<EnumItemSlot, ItemStack> entry = EnchantmentManager.a(Enchantments.MENDING, (EntityLiving) entityhuman, ItemStack::f);

                if (entry != null) {
                    ItemStack itemstack = (ItemStack) entry.getValue();

                    if (!itemstack.isEmpty() && itemstack.f()) {
                        int i = Math.min(this.c(this.value), itemstack.getDamage());

                        this.value -= this.b(i);
                        itemstack.setDamage(itemstack.getDamage() - i);
                    }
                }

                if (this.value > 0) {
                    entityhuman.giveExp(this.value);
                }

                this.die();
            }

        }
    }

    private int b(int i) {
        return i / 2;
    }

    private int c(int i) {
        return i * 2;
    }

    public int g() {
        return this.value;
    }

    public static int getOrbValue(int i) {
        return i >= 2477 ? 2477 : (i >= 1237 ? 1237 : (i >= 617 ? 617 : (i >= 307 ? 307 : (i >= 149 ? 149 : (i >= 73 ? 73 : (i >= 37 ? 37 : (i >= 17 ? 17 : (i >= 7 ? 7 : (i >= 3 ? 3 : 1)))))))));
    }

    @Override
    public boolean bK() {
        return false;
    }

    @Override
    public Packet<?> P() {
        return new PacketPlayOutSpawnEntityExperienceOrb(this);
    }
}
