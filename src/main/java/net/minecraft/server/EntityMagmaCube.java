package net.minecraft.server;

public class EntityMagmaCube extends EntitySlime {

    public EntityMagmaCube(World world) {
        super(world);
        this.texture = "/mob/lava.png";
        this.fireProof = true;
        this.ak = 0.2F;
    }

    public boolean g() {
        return this.world.difficulty > 0 && this.world.containsEntity(this.boundingBox) && this.world.getEntities(this, this.boundingBox).size() == 0 && !this.world.c(this.boundingBox);
    }

    protected int O() {
        return this.getSize() * 3;
    }

    public float a(float f) {
        return 1.0F;
    }

    protected String w() {
        return "flame";
    }

    protected EntitySlime y() {
        return new EntityMagmaCube(this.world);
    }

    protected int e() {
        return 0;
    }

    public boolean z() {
        return false;
    }

    protected int A() {
        return super.A() * 4;
    }

    protected void B() {
        this.a *= 0.9F;
    }

    protected void X() {
        this.motY = (double) (0.42F + (float) this.getSize() * 0.1F);
        this.cb = true;
    }

    protected void b(float f) {}

    protected boolean C() {
        return true;
    }

    protected int D() {
        return super.D() + 2;
    }

    protected String m() {
        return "mob.slime";
    }

    protected String n() {
        return "mob.slime";
    }

    protected String E() {
        return this.getSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    public boolean aA() {
        return false;
    }

    protected boolean G() {
        return true;
    }
}
