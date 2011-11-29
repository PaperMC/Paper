package net.minecraft.server;

public class EntitySmallFireball extends EntityFireball {

    public EntitySmallFireball(World world) {
        super(world);
        this.b(0.3125F, 0.3125F);
    }

    public EntitySmallFireball(World world, EntityLiving entityliving, double d0, double d1, double d2) {
        super(world, entityliving, d0, d1, d2);
        this.b(0.3125F, 0.3125F);
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null) {
                if (!movingobjectposition.entity.ax() && movingobjectposition.entity.damageEntity(DamageSource.fireball(this, this.shooter), 5)) {
                    movingobjectposition.entity.j(5);
                }
            } else {
                int i = movingobjectposition.b;
                int j = movingobjectposition.c;
                int k = movingobjectposition.d;

                switch (movingobjectposition.face) {
                case 0:
                    --j;
                    break;

                case 1:
                    ++j;
                    break;

                case 2:
                    --k;
                    break;

                case 3:
                    ++k;
                    break;

                case 4:
                    --i;
                    break;

                case 5:
                    ++i;
                }

                if (this.world.isEmpty(i, j, k)) {
                    this.world.setTypeId(i, j, k, Block.FIRE.id);
                }
            }

            this.die();
        }
    }

    public boolean e_() {
        return false;
    }

    public boolean damageEntity(DamageSource damagesource, int i) {
        return false;
    }
}
