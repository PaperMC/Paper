package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

// CraftBukkit start
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.PotionSplashEvent;
// CraftBukkit end

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

                    // CraftBukkit
                    HashMap<LivingEntity, Double> affected = new HashMap<LivingEntity, Double>();

                    while (iterator.hasNext()) {
                        Entity entity = (Entity) iterator.next();
                        double d0 = this.i(entity);

                        if (d0 < 16.0D) {
                            double d1 = 1.0D - Math.sqrt(d0) / 4.0D;

                            if (entity == movingobjectposition.entity) {
                                d1 = 1.0D;
                            }

                            // CraftBukkit start
                            affected.put((LivingEntity) entity.getBukkitEntity(), d1);
                        }
                    }

                    PotionSplashEvent event = CraftEventFactory.callPotionSplashEvent(this, affected);
                    if (!event.isCancelled()) {
                        for (LivingEntity victim : event.getAffectedEntities()) {
                            if (!(victim instanceof CraftLivingEntity)) {
                                continue;
                            }
                            EntityLiving entity = ((CraftLivingEntity) victim).getHandle();
                            double d1 = event.getIntensity(victim);
                            // CraftBukkit end

                            Iterator iterator1 = list.iterator();

                            while (iterator1.hasNext()) {
                                MobEffect mobeffect = (MobEffect) iterator1.next();
                                int i = mobeffect.getEffectId();

                                // CraftBukkit start - abide by PVP settings
                                if (!this.world.pvpMode && entity instanceof EntityPlayer && entity != this.shooter) {
                                    // Block SLOWER_MOVEMENT, SLOWER_DIG, HARM, BLINDNESS, HUNGER, WEAKNESS and POISON potions
                                    if (i == 2 || i == 4 || i == 7 || i == 15 || i == 17 || i == 18 || i == 19) continue;
                                }
                                // CraftBukkit end

                                if (MobEffectList.byId[i].b()) {
                                    MobEffectList.byId[i].a(this.shooter, (EntityLiving) entity, mobeffect.getAmplifier(), d1, this); // CraftBukkit - added 'this'
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
