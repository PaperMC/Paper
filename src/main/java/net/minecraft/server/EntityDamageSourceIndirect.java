package net.minecraft.server;

public class EntityDamageSourceIndirect extends EntityDamageSource {

    private Entity o;

    public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1) {
        super(s, entity);
        this.o = entity1;
    }

    public Entity b() {
        return this.a;
    }

    public Entity getEntity() {
        return this.o;
    }

    public String a(EntityHuman entityhuman) {
        // CraftBukkit start
        String source = (this.o == null) ? "Herobrine" : this.o.ad();
        return LocaleI18n.a("death." + this.n, new Object[] { entityhuman.name, source});
    }

    public Entity getProximateDamageSource() {
        return super.getEntity();
        // CraftBukkit end
    }
}
