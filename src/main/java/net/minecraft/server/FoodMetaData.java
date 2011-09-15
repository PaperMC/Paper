package net.minecraft.server;

public class FoodMetaData {

    // CraftBukkit start - all made public
    public int a = 20;
    public float b = 5.0F;
    public float c;
    public int d = 0;
    // CraftBukkit end
    private int e = 20;

    public FoodMetaData() {}

    public void a(int i, float f) {
        this.a = Math.min(i + this.a, 20);
        this.b = Math.min(this.b + (float) i * f * 2.0F, (float) this.a);
    }

    public void a(ItemFood itemfood) {
        this.a(itemfood.k(), itemfood.l());
    }

    public void a(EntityHuman entityhuman) {
        int i = entityhuman.world.spawnMonsters;

        this.e = this.a;
        if (this.c > 4.0F) {
            this.c -= 4.0F;
            if (this.b > 0.0F) {
                this.b = Math.max(this.b - 1.0F, 0.0F);
            } else if (i > 0) {
                this.a = Math.max(this.a - 1, 0);
            }
        }

        if (this.a >= 18 && entityhuman.W()) {
            ++this.d;
            if (this.d >= 80) {
                entityhuman.c(1);
                this.d = 0;
            }
        } else if (this.a <= 0) {
            ++this.d;
            if (this.d >= 80) {
                if (entityhuman.health > 10 || i >= 3 || entityhuman.health > 1 && i >= 2) {
                    entityhuman.damageEntity(DamageSource.f, 1);
                }

                this.d = 0;
            }
        } else {
            this.d = 0;
        }
    }

    public void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("foodLevel")) {
            this.a = nbttagcompound.e("foodLevel");
            this.d = nbttagcompound.e("foodTickTimer");
            this.b = nbttagcompound.g("foodSaturationLevel");
            this.c = nbttagcompound.g("foodExhaustionLevel");
        }
    }

    public void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("foodLevel", this.a);
        nbttagcompound.a("foodTickTimer", this.d);
        nbttagcompound.a("foodSaturationLevel", this.b);
        nbttagcompound.a("foodExhaustionLevel", this.c);
    }

    public int a() {
        return this.a;
    }

    public boolean b() {
        return this.a < 20;
    }

    public void a(float f) {
        this.c = Math.min(this.c + f, 40.0F);
    }

    public float c() {
        return this.b;
    }
}