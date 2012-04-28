package net.minecraft.server;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class EntitySilverfish extends EntityMonster {

    private int a;

    public EntitySilverfish(World world) {
        super(world);
        this.texture = "/mob/silverfish.png";
        this.b(0.3F, 0.7F);
        this.bb = 0.6F;
        this.damage = 1;
    }

    public int getMaxHealth() {
        return 8;
    }

    protected boolean g_() {
        return false;
    }

    protected Entity findTarget() {
        double d0 = 8.0D;

        return this.world.findNearbyVulnerablePlayer(this, d0);
    }

    protected String i() {
        return "mob.silverfish.say";
    }

    protected String j() {
        return "mob.silverfish.hit";
    }

    protected String k() {
        return "mob.silverfish.kill";
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        if (this.a <= 0 && damagesource instanceof EntityDamageSource) {
            this.a = 20;
        }

        return super.damageEntity(damagesource, i);
    }

    protected void a(Entity entity, float f) {
        if (this.attackTicks <= 0 && f < 1.2F && entity.boundingBox.e > this.boundingBox.b && entity.boundingBox.b < this.boundingBox.e) {
            this.attackTicks = 20;
            entity.damageEntity(DamageSource.mobAttack(this), this.damage);
        }
    }

    protected void a(int i, int j, int k, int l) {
        this.world.makeSound(this, "mob.silverfish.step", 1.0F, 1.0F);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int getLootId() {
        return 0;
    }

    public void F_() {
        this.V = this.yaw;
        super.F_();
    }

    protected void d_() {
        super.d_();
        if (!this.world.isStatic) {
            int i;
            int j;
            int k;
            int l;

            if (this.a > 0) {
                --this.a;
                if (this.a == 0) {
                    i = MathHelper.floor(this.locX);
                    j = MathHelper.floor(this.locY);
                    k = MathHelper.floor(this.locZ);
                    boolean flag = false;

                    for (l = 0; !flag && l <= 5 && l >= -5; l = l <= 0 ? 1 - l : 0 - l) {
                        for (int i1 = 0; !flag && i1 <= 10 && i1 >= -10; i1 = i1 <= 0 ? 1 - i1 : 0 - i1) {
                            for (int j1 = 0; !flag && j1 <= 10 && j1 >= -10; j1 = j1 <= 0 ? 1 - j1 : 0 - j1) {
                                int k1 = this.world.getTypeId(i + i1, j + l, k + j1);

                                if (k1 == Block.MONSTER_EGGS.id) {
                                    // CraftBukkit start
                                    if (CraftEventFactory.callEntityChangeBlockEvent(this, i + i1, j + l, k + j1, 0).isCancelled()) {
                                        continue;
                                    }
                                    // CraftBukkit end
                                    this.world.triggerEffect(2001, i + i1, j + l, k + j1, Block.MONSTER_EGGS.id + (this.world.getData(i + i1, j + l, k + j1) << 12));
                                    this.world.setTypeId(i + i1, j + l, k + j1, 0);
                                    Block.MONSTER_EGGS.postBreak(this.world, i + i1, j + l, k + j1, 0);
                                    if (this.random.nextBoolean()) {
                                        flag = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (this.target == null && !this.H()) {
                i = MathHelper.floor(this.locX);
                j = MathHelper.floor(this.locY + 0.5D);
                k = MathHelper.floor(this.locZ);
                int l1 = this.random.nextInt(6);

                l = this.world.getTypeId(i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1]);
                if (BlockMonsterEggs.d(l)) {
                    // CraftBukkit start
                    if (CraftEventFactory.callEntityChangeBlockEvent(this, i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1], Block.MONSTER_EGGS.id).isCancelled()) {
                        return;
                    }
                    // CraftBukkit end
                    this.world.setTypeIdAndData(i + Facing.b[l1], j + Facing.c[l1], k + Facing.d[l1], Block.MONSTER_EGGS.id, BlockMonsterEggs.e(l));
                    this.aC();
                    this.die();
                } else {
                    this.G();
                }
            } else if (this.target != null && !this.H()) {
                this.target = null;
            }
        }
    }

    public float a(int i, int j, int k) {
        return this.world.getTypeId(i, j - 1, k) == Block.STONE.id ? 10.0F : super.a(i, j, k);
    }

    protected boolean C() {
        return true;
    }

    public boolean canSpawn() {
        if (super.canSpawn()) {
            EntityHuman entityhuman = this.world.findNearbyPlayer(this, 5.0D);

            return entityhuman == null;
        } else {
            return false;
        }
    }

    public MonsterType getMonsterType() {
        return MonsterType.ARTHROPOD;
    }
}
