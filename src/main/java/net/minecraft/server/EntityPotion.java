package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class EntityPotion extends EntityProjectile {

    private int d;

    public EntityPotion(World world) {
        super(world);
    }

    public EntityPotion(World world, EntityLiving entityliving, int i) {
        super(world, entityliving);
        this.d = i;
    }

    public EntityPotion(World world, double d0, double d1, double d2, int i) {
        super(world, d0, d1, d2);
        this.d = i;
    }

    protected float e() {
        return 0.05F;
    }

    protected float c() {
        return 0.5F;
    }

    protected float d() {
        return -20.0F;
    }

    public int f() {
        return this.d;
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            List list = Item.POTION.b(this.d);

            if (list != null && !list.isEmpty()) {
                AxisAlignedBB axisalignedbb = this.boundingBox.grow(4.0D, 2.0D, 4.0D);
                List list1 = this.world.a(EntityLiving.class, axisalignedbb);

                if (list1 != null && !list1.isEmpty()) {
                    Iterator iterator = list1.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        double d0 = this.i(entity);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entity == movingobjectposition.entity) {
                                d1 = 1.0D;
                            }

                            Iterator iterator1 = list.iterator();

                            while (iterator1.hasNext()) {
                                MobEffect mobeffect = (MobEffect) iterator1.next();
                                int i = mobeffect.getEffectId();

                                if (MobEffectList.byId[i].b()) {
                                    MobEffectList.byId[i].a(this.shooter, (EntityLiving) entity, mobeffect.getAmplifier(), d1);
                                } else {
                                    int j = (int) (d1 * (double) mobeffect.getDuration() + 0.5D);

                                    if (j > 20) {
                                        ((EntityLiving) entity).addEffect(new MobEffect(i, j, mobeffect.getAmplifier()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            this.world.f(2002, (int) Math.round(this.locX), (int) Math.round(this.locY), (int) Math.round(this.locZ), this.d);
            this.die();
        }
    }
}
