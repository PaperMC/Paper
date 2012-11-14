package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityFallingBlock extends Entity {

    public int id;
    public int data;
    public int c;
    public boolean dropItem;
    private boolean e;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;

    public EntityFallingBlock(World world) {
        super(world);
        this.c = 0;
        this.dropItem = true;
        this.e = false;
        this.hurtEntities = false;
        this.fallHurtMax = 20;
        this.fallHurtAmount = 2.0F;
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, int i) {
        this(world, d0, d1, d2, i, 0);
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, int i, int j) {
        super(world);
        this.c = 0;
        this.dropItem = true;
        this.e = false;
        this.hurtEntities = false;
        this.fallHurtMax = 20;
        this.fallHurtAmount = 2.0F;
        this.id = i;
        this.data = j;
        this.m = true;
        this.a(0.98F, 0.98F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected boolean f_() {
        return false;
    }

    protected void a() {}

    public boolean L() {
        return !this.dead;
    }

    public void j_() {
        if (this.id == 0) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.c;
            this.motY -= 0.03999999910593033D;
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.9800000190734863D;
            this.motY *= 0.9800000190734863D;
            this.motZ *= 0.9800000190734863D;
            if (!this.world.isStatic) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);

                if (this.c == 1) {
                    if (this.c == 1 && this.world.getTypeId(i, j, k) == this.id) {
                        this.world.setTypeId(i, j, k, 0);
                    } else {
                        this.die();
                    }
                }

                if (this.onGround) {
                    this.motX *= 0.699999988079071D;
                    this.motZ *= 0.699999988079071D;
                    this.motY *= -0.5D;
                    if (this.world.getTypeId(i, j, k) != Block.PISTON_MOVING.id) {
                        this.die();
                        if (!this.e && this.world.mayPlace(this.id, i, j, k, true, 1, (Entity) null) && !BlockSand.canFall(this.world, i, j - 1, k) && this.world.setTypeIdAndData(i, j, k, this.id, this.data)) {
                            if (Block.byId[this.id] instanceof BlockSand) {
                                ((BlockSand) Block.byId[this.id]).a_(this.world, i, j, k, this.data);
                            }
                        } else if (this.dropItem && !this.e) {
                            this.a(new ItemStack(this.id, 1, Block.byId[this.id].getDropData(this.data)), 0.0F);
                        }
                    }
                } else if (this.c > 100 && !this.world.isStatic && (j < 1 || j > 256) || this.c > 600) {
                    if (this.dropItem) {
                        this.a(new ItemStack(this.id, 1, Block.byId[this.id].getDropData(this.data)), 0.0F);
                    }

                    this.die();
                }
            }
        }
    }

    protected void a(float f) {
        if (this.hurtEntities) {
            int i = MathHelper.f(f - 1.0F);

            if (i > 0) {
                ArrayList arraylist = new ArrayList(this.world.getEntities(this, this.boundingBox));
                DamageSource damagesource = this.id == Block.ANVIL.id ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.damageEntity(damagesource, Math.min(MathHelper.d((float) i * this.fallHurtAmount), this.fallHurtMax));
                }

                if (this.id == Block.ANVIL.id && (double) this.random.nextFloat() < 0.05000000074505806D + (double) i * 0.05D) {
                    int j = this.data >> 2;
                    int k = this.data & 3;

                    ++j;
                    if (j > 2) {
                        this.e = true;
                    } else {
                        this.data = k | j << 2;
                    }
                }
            }
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Tile", (byte) this.id);
        nbttagcompound.setByte("Data", (byte) this.data);
        nbttagcompound.setByte("Time", (byte) this.c);
        nbttagcompound.setBoolean("DropItem", this.dropItem);
        nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nbttagcompound.setInt("FallHurtMax", this.fallHurtMax);
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.id = nbttagcompound.getByte("Tile") & 255;
        this.data = nbttagcompound.getByte("Data") & 255;
        this.c = nbttagcompound.getByte("Time") & 255;
        if (nbttagcompound.hasKey("HurtEntities")) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nbttagcompound.getInt("FallHurtMax");
        } else if (this.id == Block.ANVIL.id) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.hasKey("DropItem")) {
            this.dropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (this.id == 0) {
            this.id = Block.SAND.id;
        }
    }

    public void e(boolean flag) {
        this.hurtEntities = flag;
    }
}
