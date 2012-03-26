package net.minecraft.server;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PathfinderGoalBreed extends PathfinderGoal {

    private EntityAnimal d;
    World a;
    private EntityAnimal e;
    int b = 0;
    float c;

    public PathfinderGoalBreed(EntityAnimal entityanimal, float f) {
        this.d = entityanimal;
        this.a = entityanimal.world;
        this.c = f;
        this.a(3);
    }

    public boolean a() {
        if (!this.d.r_()) {
            return false;
        } else {
            this.e = this.f();
            return this.e != null;
        }
    }

    public boolean b() {
        return this.e.isAlive() && this.e.r_() && this.b < 60;
    }

    public void d() {
        this.e = null;
        this.b = 0;
    }

    public void e() {
        this.d.getControllerLook().a(this.e, 10.0F, (float) this.d.D());
        this.d.al().a((EntityLiving) this.e, this.c);
        ++this.b;
        if (this.b == 60) {
            this.i();
        }
    }

    private EntityAnimal f() {
        float f = 8.0F;
        List list = this.a.a(this.d.getClass(), this.d.boundingBox.grow((double) f, (double) f, (double) f));
        Iterator iterator = list.iterator();

        EntityAnimal entityanimal;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            Entity entity = (Entity) iterator.next();

            entityanimal = (EntityAnimal) entity;
        } while (!this.d.mate(entityanimal));

        return entityanimal;
    }

    private void i() {
        EntityAnimal entityanimal = this.d.createChild(this.e);

        if (entityanimal != null) {
            this.d.setAge(6000);
            this.e.setAge(6000);
            this.d.s_();
            this.e.s_();
            entityanimal.setAge(-24000);
            entityanimal.setPositionRotation(this.d.locX, this.d.locY, this.d.locZ, 0.0F, 0.0F);
            this.a.addEntity(entityanimal, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.BREEDING); // CraftBukkit - Added SpawnReason
            Random random = this.d.an();

            for (int i = 0; i < 7; ++i) {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;

                this.a.a("heart", this.d.locX + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, this.d.locY + 0.5D + (double) (random.nextFloat() * this.d.length), this.d.locZ + (double) (random.nextFloat() * this.d.width * 2.0F) - (double) this.d.width, d0, d1, d2);
            }
        }
    }
}
