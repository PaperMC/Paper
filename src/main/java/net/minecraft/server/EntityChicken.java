package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftChicken;
// CraftBukkit end

public class EntityChicken extends EntityAnimal {

    public boolean a = false;
    public float b = 0.0F;
    public float c = 0.0F;
    public float f;
    public float ak;
    public float al = 1.0F;
    public int am;

    public EntityChicken(World world) {
        super(world);
        this.texture = "/mob/chicken.png";
        this.a(0.3F, 0.4F);
        this.health = 4;
        this.am = this.random.nextInt(6000) + 6000;

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftChicken(server, this);
        // CraftBukkit end
    }

    public void o() {
        super.o();
        this.ak = this.b;
        this.f = this.c;
        this.c = (float) ((double) this.c + (double) (this.onGround ? -1 : 4) * 0.3D);
        if (this.c < 0.0F) {
            this.c = 0.0F;
        }

        if (this.c > 1.0F) {
            this.c = 1.0F;
        }

        if (!this.onGround && this.al < 1.0F) {
            this.al = 1.0F;
        }

        this.al = (float) ((double) this.al * 0.9D);
        if (!this.onGround && this.motY < 0.0D) {
            this.motY *= 0.6D;
        }

        this.b += this.al * 2.0F;
        if (!this.world.isStatic && --this.am <= 0) {
            this.world.a(this, "mob.chickenplop", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.a(Item.EGG.id, 1);
            this.am = this.random.nextInt(6000) + 6000;
        }
    }

    protected void a(float f) {}

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    protected String e() {
        return "mob.chicken";
    }

    protected String f() {
        return "mob.chickenhurt";
    }

    protected String g() {
        return "mob.chickenhurt";
    }

    protected int h() {
        return Item.FEATHER.id;
    }
}
