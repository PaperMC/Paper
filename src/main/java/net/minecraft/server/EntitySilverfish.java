package net.minecraft.server;

public class EntitySilverfish extends EntityMonster {

    private int a;

    public EntitySilverfish(World world) {
        super(world);
        this.texture = "/mob/silverfish.png";
        this.b(0.3F, 0.7F);
        this.aU = 0.6F;
        this.damage = 1; // CraftBukkit - 0 -> 1, temporary fix for Silverfish base damage.
    }

    protected boolean e_() {
        return false;
    }

    protected Entity findTarget() {
        double d0 = 8.0D;

        return this.world.findNearbyPlayer(this, d0);
    }

    protected String h() {
        return "mob.spider";
    }

    protected String i() {
        return "mob.spider";
    }

    protected String j() {
        return "mob.spiderdeath";
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

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    protected int k() {
        return 0;
    }

    public void s_() {
        this.U = this.yaw;
        super.s_();
    }

    protected void c_() {
        super.c_();
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
                                    this.world.e(2001, i + i1, j + l, k + j1, Block.MONSTER_EGGS.id + this.world.getData(i + i1, j + l, k + j1) * 256);
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

            if (this.target == null && !this.B()) {
                i = MathHelper.floor(this.locX);
                j = MathHelper.floor(this.locY + 0.5D);
                k = MathHelper.floor(this.locZ);
                int l1 = this.random.nextInt(6);

                l = this.world.getTypeId(i + PistonBlockTextures.b[l1], j + PistonBlockTextures.c[l1], k + PistonBlockTextures.d[l1]);
                if (BlockMonsterEggs.c(l)) {
                    this.world.setTypeIdAndData(i + PistonBlockTextures.b[l1], j + PistonBlockTextures.c[l1], k + PistonBlockTextures.d[l1], Block.MONSTER_EGGS.id, BlockMonsterEggs.d(l));
                    this.ab();
                    this.die();
                } else {
                    this.A();
                }
            } else if (this.target != null && !this.B()) {
                this.target = null;
            }
        }
    }

    protected float a(int i, int j, int k) {
        return this.world.getTypeId(i, j - 1, k) == Block.STONE.id ? 10.0F : super.a(i, j, k);
    }
}
