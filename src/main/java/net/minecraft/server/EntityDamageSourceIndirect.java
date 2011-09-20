package net.minecraft.server;

public class EntityDamageSourceIndirect extends EntityDamageSource {

    private Entity n;

    public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1) {
        super(s, entity);
        this.n = entity1;
    }

    public Entity getEntity() {
        return this.n;
    }

    public String a(EntityHuman entityhuman) {
        // CraftBukkit start
        String source = (this.n == null) ? "Herobrine" : this.n.Y();
        return StatisticCollector.a("death." + this.m, new Object[] { entityhuman.name, source});
        // CraftBukkit end
    }
}