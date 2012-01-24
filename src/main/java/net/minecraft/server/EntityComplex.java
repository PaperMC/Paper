package net.minecraft.server;

public class EntityComplex extends EntityLiving {

    protected int t = 100;

    public EntityComplex(World world) {
        super(world);
    }

    public int getMaxHealth() {
        return this.t;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
        return this.damageEntity(damagesource, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    public boolean e(DamageSource damagesource, int i) { // CraftBukkit - protected -> public
        return super.damageEntity(damagesource, i);
    }
}
