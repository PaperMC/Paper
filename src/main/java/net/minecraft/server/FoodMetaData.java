package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
// CraftBukkit end

public class FoodMetaData {

    // CraftBukkit start - all made public
    public int foodLevel = 20;
    public float saturationLevel = 5.0F;
    public float exhaustionLevel;
    public int foodTickTimer = 0;
    // CraftBukkit end
    private int e = 20;

    public FoodMetaData() {}

    public void a(int i, float f) {
        this.foodLevel = Math.min(i + this.foodLevel, 20);
        this.saturationLevel = Math.min(this.saturationLevel + (float) i * f * 2.0F, (float) this.foodLevel);
    }

    public void a(ItemFood itemfood) {
        this.a(itemfood.n(), itemfood.o());
    }

    public void a(EntityHuman entityhuman) {
        int i = entityhuman.world.difficulty;

        this.e = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (i > 0) {
                // CraftBukkit start
                FoodLevelChangeEvent event = new FoodLevelChangeEvent(entityhuman.getBukkitEntity(), Math.max(this.foodLevel - 1, 0));
                entityhuman.world.getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled()) {
                    this.foodLevel = event.getFoodLevel();
                }
                // CraftBukkit end
            }
        }

        if (this.foodLevel >= 18 && entityhuman.ab()) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                // CraftBukkit - added RegainReason.
                entityhuman.d(1, org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason.SATIATED);
                this.foodTickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                if (entityhuman.getHealth() > 10 || i >= 3 || entityhuman.getHealth() > 1 && i >= 2) {
                    // CraftBukkit start
                    EntityDamageEvent event = new EntityDamageEvent(entityhuman.getBukkitEntity(), EntityDamageEvent.DamageCause.STARVATION, 1);
                    entityhuman.world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        entityhuman.damageEntity(DamageSource.STARVE, event.getDamage());
                    }
                    // CraftBukkit end
                }

                this.foodTickTimer = 0;
            }
        } else {
            this.foodTickTimer = 0;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("foodLevel")) {
            this.foodLevel = nbttagcompound.f("foodLevel");
            this.foodTickTimer = nbttagcompound.f("foodTickTimer");
            this.saturationLevel = nbttagcompound.h("foodSaturationLevel");
            this.exhaustionLevel = nbttagcompound.h("foodExhaustionLevel");
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("foodLevel", this.foodLevel);
        nbttagcompound.a("foodTickTimer", this.foodTickTimer);
        nbttagcompound.a("foodSaturationLevel", this.saturationLevel);
        nbttagcompound.a("foodExhaustionLevel", this.exhaustionLevel);
    }

    public int a() {
        return this.foodLevel;
    }

    public boolean b() {
        return this.foodLevel < 20;
    }

    public void a(float f) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + f, 40.0F);
    }

    public float c() {
        return this.saturationLevel;
    }
}
