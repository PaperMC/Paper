package net.minecraft.server;

public class EntitySpectralArrow extends EntityArrow {

    public int duration = 200;

    public EntitySpectralArrow(EntityTypes<? extends EntitySpectralArrow> entitytypes, World world) {
        super(entitytypes, world);
    }

    public EntitySpectralArrow(World world, EntityLiving entityliving) {
        super(EntityTypes.SPECTRAL_ARROW, entityliving, world);
    }

    public EntitySpectralArrow(World world, double d0, double d1, double d2) {
        super(EntityTypes.SPECTRAL_ARROW, d0, d1, d2, world);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.world.isClientSide && !this.inGround) {
            this.world.addParticle(Particles.INSTANT_EFFECT, this.locX(), this.locY(), this.locZ(), 0.0D, 0.0D, 0.0D);
        }

    }

    @Override
    protected ItemStack getItemStack() {
        return new ItemStack(Items.SPECTRAL_ARROW);
    }

    @Override
    protected void a(EntityLiving entityliving) {
        super.a(entityliving);
        MobEffect mobeffect = new MobEffect(MobEffects.GLOWING, this.duration, 0);

        entityliving.addEffect(mobeffect);
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        super.loadData(nbttagcompound);
        if (nbttagcompound.hasKey("Duration")) {
            this.duration = nbttagcompound.getInt("Duration");
        }

    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        super.saveData(nbttagcompound);
        nbttagcompound.setInt("Duration", this.duration);
    }
}
