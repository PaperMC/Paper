package net.minecraft.server;

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
        this.a(itemfood.k(), itemfood.l());
    }

    public void a(EntityHuman entityhuman) {
        int i = entityhuman.world.spawnMonsters;

        this.e = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if (i > 0) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }

        if (this.foodLevel >= 18 && entityhuman.W()) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                entityhuman.c(1);
                this.foodTickTimer = 0;
            }
        } else if (this.foodLevel <= 0) {
            ++this.foodTickTimer;
            if (this.foodTickTimer >= 80) {
                if (entityhuman.health > 10 || i >= 3 || entityhuman.health > 1 && i >= 2) {
                    entityhuman.damageEntity(DamageSource.STARVE, 1);
                }

                this.foodTickTimer = 0;
            }
        } else {
            this.foodTickTimer = 0;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("foodLevel")) {
            this.foodLevel = nbttagcompound.e("foodLevel");
            this.foodTickTimer = nbttagcompound.e("foodTickTimer");
            this.saturationLevel = nbttagcompound.g("foodSaturationLevel");
            this.exhaustionLevel = nbttagcompound.g("foodExhaustionLevel");
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
