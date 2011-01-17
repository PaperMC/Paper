package net.minecraft.server;

public class EntityZombieSimple extends EntityMobs {

    public EntityZombieSimple(World world) {
        super(world);
        aP = "/mob/zombie.png";
        bC = 0.5F;
        c = 50;
        aZ *= 10;
        H *= 6F;
        a(I * 6F, J * 6F);
    }

    protected float a(int i, int j, int k) {
        return l.l(i, j, k) - 0.5F;
    }
}
