package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftSlime;
// CraftBukkit stop

public class EntitySlime extends EntityLiving implements IMonster {

    public float a;
    public float b;
    private int d = 0;
    public int c = 1;

    public EntitySlime(World world) {
        super(world);
        this.texture = "/mob/slime.png";
        this.c = 1 << this.random.nextInt(3);
        this.height = 0.0F;
        this.d = this.random.nextInt(20) + 10;
        this.a(this.c);

        // CraftBukkit start
        CraftServer server = ((WorldServer) this.world).getServer();
        this.bukkitEntity = new CraftSlime(server, this);
        // CraftBukkit end
    }

    public void a(int i) {
        this.c = i;
        this.a(0.6F * (float) i, 0.6F * (float) i);
        this.health = i * i;
        this.a(this.locX, this.locY, this.locZ);
    }

    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        nbttagcompound.a("Size", this.c - 1);
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        this.c = nbttagcompound.d("Size") + 1;
    }

    public void b_() {
        this.b = this.a;
        boolean flag = this.onGround;

        super.b_();
        if (this.onGround && !flag) {
            for (int i = 0; i < this.c * 8; ++i) {
                float f = this.random.nextFloat() * 3.1415927F * 2.0F;
                float f1 = this.random.nextFloat() * 0.5F + 0.5F;
                float f2 = MathHelper.a(f) * (float) this.c * 0.5F * f1;
                float f3 = MathHelper.b(f) * (float) this.c * 0.5F * f1;

                this.world.a("slime", this.locX + (double) f2, this.boundingBox.b, this.locZ + (double) f3, 0.0D, 0.0D, 0.0D);
            }

            if (this.c > 2) {
                this.world.a(this, "mob.slime", this.i(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.a = -0.5F;
        }

        this.a *= 0.6F;
    }

    protected void d() {
        EntityHuman entityhuman = this.world.a(this, 16.0D);

        if (entityhuman != null) {
            this.b(entityhuman, 10.0F);
        }

        if (this.onGround && this.d-- <= 0) {
            this.d = this.random.nextInt(20) + 10;
            if (entityhuman != null) {
                this.d /= 3;
            }

            this.bA = true;
            if (this.c > 1) {
                this.world.a(this, "mob.slime", this.i(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
            }

            this.a = 1.0F;
            this.bx = 1.0F - this.random.nextFloat() * 2.0F;
            this.by = (float) (1 * this.c);
        } else {
            this.bA = false;
            if (this.onGround) {
                this.bx = this.by = 0.0F;
            }
        }
    }

    public void q() {
        if (this.c > 1 && this.health == 0) {
            for (int i = 0; i < 4; ++i) {
                float f = ((float) (i % 2) - 0.5F) * (float) this.c / 4.0F;
                float f1 = ((float) (i / 2) - 0.5F) * (float) this.c / 4.0F;
                EntitySlime entityslime = new EntitySlime(this.world);

                entityslime.a(this.c / 2);
                entityslime.c(this.locX + (double) f, this.locY + 0.5D, this.locZ + (double) f1, this.random.nextFloat() * 360.0F, 0.0F);
                this.world.a((Entity) entityslime);
            }
        }

        super.q();
    }

    public void b(EntityHuman entityhuman) {
        // CraftBukkit - add cast to Entity                      VVVVVVVV
        if (this.c > 1 && this.i(entityhuman) && (double) this.a((Entity) entityhuman) < 0.6D * (double) this.c && entityhuman.a(this, this.c)) {
            this.world.a(this, "mob.slimeattack", 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
        }
    }

    protected String f() {
        return "mob.slime";
    }

    protected String g() {
        return "mob.slime";
    }

    protected int h() {
        return this.c == 1 ? Item.SLIME_BALL.id : 0;
    }

    public boolean b() {
        Chunk chunk = this.world.b(MathHelper.b(this.locX), MathHelper.b(this.locZ));

        return (this.c == 1 || this.world.k > 0) && this.random.nextInt(10) == 0 && chunk.a(987234911L).nextInt(10) == 0 && this.locY < 16.0D;
    }

    protected float i() {
        return 0.6F;
    }
}
