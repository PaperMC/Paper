package net.minecraft.server;

// CraftBukkit start
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
// CraftBukkit end

public class EntityItem extends Entity {

    public ItemStack a;
    private int e;
    public int b = 0;
    public int c;
    private int f = 5;
    public float d = (float) (Math.random() * 3.141592653589793D * 2.0D);

    public EntityItem(World world, double d0, double d1, double d2, ItemStack itemstack) {
        super(world);
        this.a(0.25F, 0.25F);
        this.height = this.width / 2.0F;
        this.a(d0, d1, d2);
        this.a = itemstack;
        this.yaw = (float) (Math.random() * 360.0D);
        this.motX = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.motY = 0.20000000298023224D;
        this.motZ = (double) ((float) (Math.random() * 0.20000000298023224D - 0.10000000149011612D));
        this.bg = false;
    }

    public EntityItem(World world) {
        super(world);
        this.a(0.25F, 0.25F);
        this.height = this.width / 2.0F;
    }

    protected void a() {}

    public void f_() {
        super.f_();
        if (this.c > 0) {
            --this.c;
        }

        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.03999999910593033D;
        if (this.world.getMaterial(MathHelper.b(this.locX), MathHelper.b(this.locY), MathHelper.b(this.locZ)) == Material.LAVA) {
            this.motY = 0.20000000298023224D;
            this.motX = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.motZ = (double) ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            this.world.a(this, "random.fizz", 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }

        this.g(this.locX, this.locY, this.locZ);
        this.c(this.motX, this.motY, this.motZ);
        float f = 0.98F;

        if (this.onGround) {
            f = 0.58800006F;
            int i = this.world.getTypeId(MathHelper.b(this.locX), MathHelper.b(this.boundingBox.b) - 1, MathHelper.b(this.locZ));

            if (i > 0) {
                f = Block.byId[i].frictionFactor * 0.98F;
            }
        }

        this.motX *= (double) f;
        this.motY *= 0.9800000190734863D;
        this.motZ *= (double) f;
        if (this.onGround) {
            this.motY *= -0.5D;
        }

        ++this.e;
        ++this.b;
        if (this.b >= 6000) {
            this.C();
        }
    }

    public boolean g_() {
        return this.world.a(this.boundingBox, Material.WATER, this);
    }

    private boolean g(double d0, double d1, double d2) {
        int i = MathHelper.b(d0);
        int j = MathHelper.b(d1);
        int k = MathHelper.b(d2);
        double d3 = d0 - (double) i;
        double d4 = d1 - (double) j;
        double d5 = d2 - (double) k;

        if (Block.o[this.world.getTypeId(i, j, k)]) {
            boolean flag = !Block.o[this.world.getTypeId(i - 1, j, k)];
            boolean flag1 = !Block.o[this.world.getTypeId(i + 1, j, k)];
            boolean flag2 = !Block.o[this.world.getTypeId(i, j - 1, k)];
            boolean flag3 = !Block.o[this.world.getTypeId(i, j + 1, k)];
            boolean flag4 = !Block.o[this.world.getTypeId(i, j, k - 1)];
            boolean flag5 = !Block.o[this.world.getTypeId(i, j, k + 1)];
            byte b0 = -1;
            double d6 = 9999.0D;

            if (flag && d3 < d6) {
                d6 = d3;
                b0 = 0;
            }

            if (flag1 && 1.0D - d3 < d6) {
                d6 = 1.0D - d3;
                b0 = 1;
            }

            if (flag2 && d4 < d6) {
                d6 = d4;
                b0 = 2;
            }

            if (flag3 && 1.0D - d4 < d6) {
                d6 = 1.0D - d4;
                b0 = 3;
            }

            if (flag4 && d5 < d6) {
                d6 = d5;
                b0 = 4;
            }

            if (flag5 && 1.0D - d5 < d6) {
                d6 = 1.0D - d5;
                b0 = 5;
            }

            float f = this.random.nextFloat() * 0.2F + 0.1F;

            if (b0 == 0) {
                this.motX = (double) (-f);
            }

            if (b0 == 1) {
                this.motX = (double) f;
            }

            if (b0 == 2) {
                this.motY = (double) (-f);
            }

            if (b0 == 3) {
                this.motY = (double) f;
            }

            if (b0 == 4) {
                this.motZ = (double) (-f);
            }

            if (b0 == 5) {
                this.motZ = (double) f;
            }
        }

        return false;
    }

    protected void a(int i) {
        this.a((Entity) null, i);
    }

    public boolean a(Entity entity, int i) {
        this.R();
        this.f -= i;
        if (this.f <= 0) {
            this.C();
        }

        return false;
    }

    public void a(NBTTagCompound nbttagcompound) {
        nbttagcompound.a("Health", (short) ((byte) this.f));
        nbttagcompound.a("Age", (short) this.b);
        nbttagcompound.a("Item", this.a.a(new NBTTagCompound()));
    }

    public void b(NBTTagCompound nbttagcompound) {
        this.f = nbttagcompound.d("Health") & 255;
        this.b = nbttagcompound.d("Age");
        NBTTagCompound nbttagcompound1 = nbttagcompound.k("Item");

        this.a = new ItemStack(nbttagcompound1);
    }

    public void b(EntityHuman entityhuman) {
        if (!this.world.isStatic) {
            int i = this.a.count;

            // CraftBukkit start
            if (this.c == 0) {
                Player player = (Player) entityhuman.getBukkitEntity();
                PlayerPickupItemEvent event = new PlayerPickupItemEvent(player, (Item) this.getBukkitEntity());
                ((WorldServer) world).getServer().getPluginManager().callEvent(event);

                if (!event.isCancelled() && entityhuman.inventory.a(this.a)) {
                    this.world.a(this, "random.pop", 0.2F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    entityhuman.b(this, i);
                    this.C();
                }
            }
            // CraftBukkit end
        }
    }
}
