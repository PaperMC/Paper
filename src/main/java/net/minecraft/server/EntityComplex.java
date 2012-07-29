package net.minecraft.server;

public class EntityComplex extends EntityLiving {

    protected int a = 100;

    public EntityComplex(World world) {
        super(world);
    }

    public int getMaxHealth() {
        return this.a;
    }

    public boolean a(EntityComplexPart entitycomplexpart, DamageSource damagesource, int i) {
        return this.damageEntity(damagesource, i);
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }

    public boolean dealDamage(DamageSource damagesource, int i) { // CraftBukkit - protected -> public
        return super.damageEntity(damagesource, i);
    }
}
