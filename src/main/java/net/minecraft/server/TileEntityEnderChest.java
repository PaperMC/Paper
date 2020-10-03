package net.minecraft.server;

public class TileEntityEnderChest extends TileEntity implements ITickable {

    public float a;
    public float b;
    public int c;
    private int g;

    public TileEntityEnderChest() {
        super(TileEntityTypes.ENDER_CHEST);
    }

    @Override
    public void tick() {
        if (++this.g % 20 * 4 == 0) {
            this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.c);
        }

        this.b = this.a;
        int i = this.position.getX();
        int j = this.position.getY();
        int k = this.position.getZ();
        float f = 0.1F;
        double d0;

        if (this.c > 0 && this.a == 0.0F) {
            double d1 = (double) i + 0.5D;

            d0 = (double) k + 0.5D;
            this.world.playSound((EntityHuman) null, d1, (double) j + 0.5D, d0, SoundEffects.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
        }

        if (this.c == 0 && this.a > 0.0F || this.c > 0 && this.a < 1.0F) {
            float f1 = this.a;

            if (this.c > 0) {
                this.a += 0.1F;
            } else {
                this.a -= 0.1F;
            }

            if (this.a > 1.0F) {
                this.a = 1.0F;
            }

            float f2 = 0.5F;

            if (this.a < 0.5F && f1 >= 0.5F) {
                d0 = (double) i + 0.5D;
                double d2 = (double) k + 0.5D;

                this.world.playSound((EntityHuman) null, d0, (double) j + 0.5D, d2, SoundEffects.BLOCK_ENDER_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }

            if (this.a < 0.0F) {
                this.a = 0.0F;
            }
        }

    }

    @Override
    public boolean setProperty(int i, int j) {
        if (i == 1) {
            this.c = j;
            return true;
        } else {
            return super.setProperty(i, j);
        }
    }

    @Override
    public void al_() {
        this.invalidateBlockCache();
        super.al_();
    }

    public void d() {
        ++this.c;
        this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.c);
    }

    public void f() {
        --this.c;
        this.world.playBlockAction(this.position, Blocks.ENDER_CHEST, 1, this.c);
    }

    public boolean a(EntityHuman entityhuman) {
        return this.world.getTileEntity(this.position) != this ? false : entityhuman.h((double) this.position.getX() + 0.5D, (double) this.position.getY() + 0.5D, (double) this.position.getZ() + 0.5D) <= 64.0D;
    }
}
