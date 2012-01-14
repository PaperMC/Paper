package net.minecraft.server;

public class EntityFallingBlock extends Entity {

    public int id;
    public int data; // CraftBukkit
    public int b = 0;

    public EntityFallingBlock(World world) {
        super(world);
    }

    // CraftBukkit - changed method signature
    public EntityFallingBlock(World world, double d0, double d1, double d2, int i, int data) {
        super(world);
        this.id = i;
        this.bf = true;
        this.data = data; // CraftBukkit
        this.b(0.98F, 0.98F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected boolean g_() {
        return false;
    }

    protected void b() {}

    public boolean e_() {
        return !this.dead;
    }

    public void y_() {
        if (this.id == 0) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.b;
            this.motY -= 0.03999999910593033D;
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.9800000190734863D;
            this.motY *= 0.9800000190734863D;
            this.motZ *= 0.9800000190734863D;
            int i = MathHelper.floor(this.locX);
            int j = MathHelper.floor(this.locY);
            int k = MathHelper.floor(this.locZ);

            if (this.b == 1 && this.world.getTypeId(i, j, k) == this.id) {
                this.world.setTypeId(i, j, k, 0);
            } else if (!this.world.isStatic && this.b == 1) {
                this.die();
            }

            if (this.onGround) {
                this.motX *= 0.699999988079071D;
                this.motZ *= 0.699999988079071D;
                this.motY *= -0.5D;
                if (this.world.getTypeId(i, j, k) != Block.PISTON_MOVING.id) {
                    this.die();
                    // CraftBukkit - setTypeId => setTypeIdAndData
                    if ((!this.world.mayPlace(this.id, i, j, k, true, 1) || BlockSand.canFall(this.world, i, j - 1, k) || !this.world.setTypeIdAndData(i, j, k, this.id, this.data)) && !this.world.isStatic) {
                        this.b(this.id, 1);
                    }
                }
            } else if (this.b > 100 && !this.world.isStatic) {
                this.b(this.id, 1);
                this.die();
            }
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Tile", (byte) this.id);
        nbttagcompound.setByte("Data", (byte) this.data); // CraftBukkit
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.getByte("Tile") & 255;
        this.data = nbttagcompound.getByte("Data") & 15; // CraftBukkit
    }
}
