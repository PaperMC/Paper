package net.minecraft.server;

import java.util.List;

// CraftBukkit start
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.event.entity.EntityTargetEvent;
// CraftBukkit end

public class EntityPigZombie extends EntityZombie {

    public int angerLevel = 0; // CraftBukkit - private -> public
    private int soundDelay = 0;
    private static final ItemStack g = new ItemStack(Item.GOLD_SWORD, 1);

    public EntityPigZombie(World world) {
        super(world);
        this.texture = "/mob/pigzombie.png";
        this.aU = 0.5F;
        this.damage = 5;
        this.fireProof = true;
    }

    public void s_() {
        this.aU = this.target != null ? 0.95F : 0.5F;
        if (this.soundDelay > 0 && --this.soundDelay == 0) {
            this.world.makeSound(this, "mob.zombiepig.zpigangry", this.l() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        super.s_();
    }

    public boolean d() {
        return this.world.spawnMonsters > 0 && this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("Anger", (short) this.angerLevel);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        this.angerLevel = nbttagcompound.d("Anger");
    }

    protected Entity findTarget() {
        return this.angerLevel == 0 ? null : super.findTarget();
    }

    public void s() {
        super.s();
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        Entity entity = damagesource.getEntity();

        if (entity instanceof EntityHuman) {
            List list = this.world.b((Entity) this, this.boundingBox.b(32.0D, 32.0D, 32.0D));

            for (int j = 0; j < list.size(); ++j) {
                Entity entity1 = (Entity) list.get(j);

                if (entity1 instanceof EntityPigZombie) {
                    EntityPigZombie entitypigzombie = (EntityPigZombie) entity1;

                    entitypigzombie.e(entity);
                }
            }

            this.e(entity);
        }

        return super.damageEntity(damagesource, i);
    }

    private void e(Entity entity) {
        // CraftBukkit start
        org.bukkit.entity.Entity bukkitTarget = entity == null ? null : entity.getBukkitEntity();

        EntityTargetEvent event = new EntityTargetEvent(this.getBukkitEntity(), bukkitTarget, EntityTargetEvent.TargetReason.PIG_ZOMBIE_TARGET);
        this.world.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) {
            return;
        }

        if (event.getTarget() == null) {
            this.target = null;
            return;
        }
        entity = ((CraftEntity) event.getTarget()).getHandle();
        // CraftBukkit end

        this.target = entity;
        this.angerLevel = 400 + this.random.nextInt(400);
        this.soundDelay = this.random.nextInt(40);
    }

    protected String h() {
        return "mob.zombiepig.zpig";
    }

    protected String i() {
        return "mob.zombiepig.zpighurt";
    }

    protected String j() {
        return "mob.zombiepig.zpigdeath";
    }

    protected int k() {
        return Item.GRILLED_PORK.id;
    }
}
