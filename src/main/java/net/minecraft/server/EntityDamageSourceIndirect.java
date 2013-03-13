package net.minecraft.server;

public class EntityDamageSourceIndirect extends EntityDamageSource {

    private Entity owner;

    public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1) {
        super(s, entity);
        this.owner = entity1;
    }

    public Entity h() {
        return this.p;
    }

    public Entity getEntity() {
        return this.owner;
    }

    public String getLocalizedDeathMessage(EntityLiving entityliving) {
        String s = this.owner == null ? this.p.getScoreboardDisplayName() : this.owner.getScoreboardDisplayName();
        ItemStack itemstack = this.owner instanceof EntityLiving ? ((EntityLiving) this.owner).bG() : null;
        String s1 = "death.attack." + this.translationIndex;
        String s2 = s1 + ".item";

        return itemstack != null && itemstack.hasName() && LocaleI18n.b(s2) ? LocaleI18n.get(s2, new Object[] { entityliving.getScoreboardDisplayName(), s, itemstack.getName()}) : LocaleI18n.get(s1, new Object[] { entityliving.getScoreboardDisplayName(), s});
    }

    // CraftBukkit start
    public Entity getProximateDamageSource() {
        return super.getEntity();
    }
    // CraftBukkit end
}
