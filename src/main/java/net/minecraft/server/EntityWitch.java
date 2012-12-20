package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

public class EntityWitch extends EntityMonster implements IRangedEntity {

    private static final int[] d = new int[] { Item.GLOWSTONE_DUST.id, Item.SUGAR.id, Item.REDSTONE.id, Item.SPIDER_EYE.id, Item.GLASS_BOTTLE.id, Item.SULPHUR.id, Item.STICK.id, Item.STICK.id};
    private int e = 0;

    public EntityWitch(World world) {
        super(world);
        this.texture = "/mob/villager/witch.png";
        this.bH = 0.25F;
        this.goalSelector.a(1, new PathfinderGoalFloat(this));
        this.goalSelector.a(2, new PathfinderGoalArrowAttack(this, this.bH, 60, 10.0F));
        this.goalSelector.a(2, new PathfinderGoalRandomStroll(this, this.bH));
        this.goalSelector.a(3, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 16.0F, 0, true));
    }

    protected void a() {
        super.a();
        this.getDataWatcher().a(21, Byte.valueOf((byte) 0));
    }

    protected String aY() {
        return "mob.witch.idle";
    }

    protected String aZ() {
        return "mob.witch.hurt";
    }

    protected String ba() {
        return "mob.witch.death";
    }

    public void f(boolean flag) {
        this.getDataWatcher().watch(21, Byte.valueOf((byte) (flag ? 1 : 0)));
    }

    public boolean m() {
        return this.getDataWatcher().getByte(21) == 1;
    }

    public int getMaxHealth() {
        return 26;
    }

    public boolean be() {
        return true;
    }

    public void c() {
        if (!this.world.isStatic) {
            if (this.m()) {
                if (this.e-- <= 0) {
                    this.f(false);
                    ItemStack itemstack = this.bD();

                    this.setEquipment(0, (ItemStack) null);
                    if (itemstack != null && itemstack.id == Item.POTION.id) {
                        List list = Item.POTION.g(itemstack);

                        if (list != null) {
                            Iterator iterator = list.iterator();

                            while (iterator.hasNext()) {
                                MobEffect mobeffect = (MobEffect) iterator.next();

                                this.addEffect(new MobEffect(mobeffect));
                            }
                        }
                    }
                }
            } else {
                short short1 = -1;

                if (this.random.nextFloat() < 0.15F && this.isBurning() && !this.hasEffect(MobEffectList.FIRE_RESISTANCE)) {
                    short1 = 16307;
                } else if (this.random.nextFloat() < 0.05F && this.health < this.getMaxHealth()) {
                    short1 = 16341;
                } else if (this.random.nextFloat() < 0.25F && this.aG() != null && !this.hasEffect(MobEffectList.FASTER_MOVEMENT) && this.aG().e(this) > 121.0D) {
                    short1 = 16274;
                } else if (this.random.nextFloat() < 0.25F && this.aG() != null && !this.hasEffect(MobEffectList.FASTER_MOVEMENT) && this.aG().e(this) > 121.0D) {
                    short1 = 16274;
                }

                if (short1 > -1) {
                    this.setEquipment(0, new ItemStack(Item.POTION, 1, short1));
                    this.e = this.bD().m();
                    this.f(true);
                }
            }

            if (this.random.nextFloat() < 7.5E-4F) {
                this.world.broadcastEntityEffect(this, (byte) 15);
            }
        }

        super.c();
    }

    protected int c(DamageSource damagesource, int i) {
        i = super.c(damagesource, i);
        if (damagesource.getEntity() == this) {
            i = 0;
        }

        if (damagesource.o()) {
            i = (int) ((double) i * 0.15D);
        }

        return i;
    }

    public float bB() {
        float f = super.bB();

        if (this.m()) {
            f *= 0.75F;
        }

        return f;
    }

    protected void dropDeathLoot(boolean flag, int i) {
        // CraftBukkit start
        java.util.List<org.bukkit.inventory.ItemStack> loot = new java.util.ArrayList<org.bukkit.inventory.ItemStack>();

        int j = this.random.nextInt(3) + 1;

        for (int k = 0; k < j; ++k) {
            int l = this.random.nextInt(3);
            int i1 = d[this.random.nextInt(d.length)];

            if (i > 0) {
                l += this.random.nextInt(i + 1);
            }

            loot.add(new org.bukkit.inventory.ItemStack(i1, l));
        }

        org.bukkit.craftbukkit.event.CraftEventFactory.callEntityDeathEvent(this, loot);
        // CraftBukkit end
    }

    public void d(EntityLiving entityliving) {
        if (!this.m()) {
            EntityPotion entitypotion = new EntityPotion(this.world, this, 32732);

            entitypotion.pitch -= -20.0F;
            double d0 = entityliving.locX + entityliving.motX - this.locX;
            double d1 = entityliving.locY + (double) entityliving.getHeadHeight() - 1.100000023841858D - this.locY;
            double d2 = entityliving.locZ + entityliving.motZ - this.locZ;
            float f = MathHelper.sqrt(d0 * d0 + d2 * d2);

            if (f >= 8.0F && !entityliving.hasEffect(MobEffectList.SLOWER_MOVEMENT)) {
                entitypotion.setPotionValue(32698);
            } else if (entityliving.getHealth() >= 8 && !entityliving.hasEffect(MobEffectList.POISON)) {
                entitypotion.setPotionValue(32660);
            } else if (f <= 3.0F && !entityliving.hasEffect(MobEffectList.WEAKNESS) && this.random.nextFloat() < 0.25F) {
                entitypotion.setPotionValue(32696);
            }

            entitypotion.shoot(d0, d1 + (double) (f * 0.2F), d2, 0.75F, 8.0F);
            this.world.addEntity(entitypotion);
        }
    }
}
