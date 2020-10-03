package net.minecraft.server;

import org.apache.commons.lang3.mutable.MutableInt;

public final class LightEngineBlock extends LightEngineLayer<LightEngineStorageBlock.a, LightEngineStorageBlock> {

    private static final EnumDirection[] e = EnumDirection.values();
    private final BlockPosition.MutableBlockPosition f = new BlockPosition.MutableBlockPosition();

    public LightEngineBlock(ILightAccess ilightaccess) {
        super(ilightaccess, EnumSkyBlock.BLOCK, new LightEngineStorageBlock(ilightaccess));
    }

    private int d(long i) {
        int j = BlockPosition.b(i);
        int k = BlockPosition.c(i);
        int l = BlockPosition.d(i);
        IBlockAccess iblockaccess = this.a.c(j >> 4, l >> 4);

        return iblockaccess != null ? iblockaccess.g(this.f.d(j, k, l)) : 0;
    }

    @Override
    protected int b(long i, long j, int k) {
        if (j == Long.MAX_VALUE) {
            return 15;
        } else if (i == Long.MAX_VALUE) {
            return k + 15 - this.d(j);
        } else if (k >= 15) {
            return k;
        } else {
            int l = Integer.signum(BlockPosition.b(j) - BlockPosition.b(i));
            int i1 = Integer.signum(BlockPosition.c(j) - BlockPosition.c(i));
            int j1 = Integer.signum(BlockPosition.d(j) - BlockPosition.d(i));
            EnumDirection enumdirection = EnumDirection.a(l, i1, j1);

            if (enumdirection == null) {
                return 15;
            } else {
                MutableInt mutableint = new MutableInt();
                IBlockData iblockdata = this.a(j, mutableint);

                if (mutableint.getValue() >= 15) {
                    return 15;
                } else {
                    IBlockData iblockdata1 = this.a(i, (MutableInt) null);
                    VoxelShape voxelshape = this.a(iblockdata1, i, enumdirection);
                    VoxelShape voxelshape1 = this.a(iblockdata, j, enumdirection.opposite());

                    return VoxelShapes.b(voxelshape, voxelshape1) ? 15 : k + Math.max(1, mutableint.getValue());
                }
            }
        }
    }

    @Override
    protected void a(long i, int j, boolean flag) {
        long k = SectionPosition.e(i);
        EnumDirection[] aenumdirection = LightEngineBlock.e;
        int l = aenumdirection.length;

        for (int i1 = 0; i1 < l; ++i1) {
            EnumDirection enumdirection = aenumdirection[i1];
            long j1 = BlockPosition.a(i, enumdirection);
            long k1 = SectionPosition.e(j1);

            if (k == k1 || ((LightEngineStorageBlock) this.c).g(k1)) {
                this.b(i, j1, j, flag);
            }
        }

    }

    @Override
    protected int a(long i, long j, int k) {
        int l = k;

        if (Long.MAX_VALUE != j) {
            int i1 = this.b(Long.MAX_VALUE, i, 0);

            if (k > i1) {
                l = i1;
            }

            if (l == 0) {
                return l;
            }
        }

        long j1 = SectionPosition.e(i);
        NibbleArray nibblearray = ((LightEngineStorageBlock) this.c).a(j1, true);
        EnumDirection[] aenumdirection = LightEngineBlock.e;
        int k1 = aenumdirection.length;

        for (int l1 = 0; l1 < k1; ++l1) {
            EnumDirection enumdirection = aenumdirection[l1];
            long i2 = BlockPosition.a(i, enumdirection);

            if (i2 != j) {
                long j2 = SectionPosition.e(i2);
                NibbleArray nibblearray1;

                if (j1 == j2) {
                    nibblearray1 = nibblearray;
                } else {
                    nibblearray1 = ((LightEngineStorageBlock) this.c).a(j2, true);
                }

                if (nibblearray1 != null) {
                    int k2 = this.b(i2, i, this.a(nibblearray1, i2));

                    if (l > k2) {
                        l = k2;
                    }

                    if (l == 0) {
                        return l;
                    }
                }
            }
        }

        return l;
    }

    @Override
    public void a(BlockPosition blockposition, int i) {
        ((LightEngineStorageBlock) this.c).d();
        this.a(Long.MAX_VALUE, blockposition.asLong(), 15 - i, true);
    }
}
