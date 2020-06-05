package net.minecraft.server;

import org.apache.commons.lang3.mutable.MutableInt;

public final class LightEngineBlock extends LightEngineLayer<LightEngineStorageBlock.a, LightEngineStorageBlock> {

    private static final EnumDirection[] e = EnumDirection.values();
    private final BlockPosition.MutableBlockPosition f = new BlockPosition.MutableBlockPosition();
    private final MutableInt mutableint = new MutableInt(); // Paper

    public LightEngineBlock(ILightAccess ilightaccess) {
        super(ilightaccess, EnumSkyBlock.BLOCK, new LightEngineStorageBlock(ilightaccess));
    }

    private int d(long i) {
        // Paper start - inline math
        int j = (int) (i >> 38);
        int k = (int) ((i << 52) >> 52);
        int l = (int) ((i << 26) >> 38);
        // Paper end
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
            // Paper start - reuse math - credit to JellySquid for idea
            int jx = (int) (j >> 38);
            int jy = (int) ((j << 52) >> 52);
            int jz = (int) ((j << 26) >> 38);
            int ix = (int) (i >> 38);
            int iy = (int) ((i << 52) >> 52);
            int iz = (int) ((i << 26) >> 38);
            int l = Integer.signum(jx - ix);
            int i1 = Integer.signum(jy - iy);
            int j1 = Integer.signum(jz - iz);
            // Paper end
            EnumDirection enumdirection = EnumDirection.a(l, i1, j1);

            if (enumdirection == null) {
                return 15;
            } else {
                //MutableInt mutableint = new MutableInt(); // Paper - share mutableint, single threaded
                IBlockData iblockdata = this.getBlockOptimized(jx, jy, jz, mutableint); // Paper
                int blockedLight = mutableint.getValue(); // Paper
                if (blockedLight >= 15) { // Paper
                    return 15;
                } else {
                    IBlockData iblockdata1 = this.getBlockOptimized(ix, iy, iz); // Paper
                    VoxelShape voxelshape = this.a(iblockdata1, i, enumdirection);
                    VoxelShape voxelshape1 = this.a(iblockdata, j, enumdirection.opposite());

                    return VoxelShapes.b(voxelshape, voxelshape1) ? 15 : k + Math.max(1, blockedLight); // Paper
                }
            }
        }
    }

    @Override
    protected void a(long i, int j, boolean flag) {
        // Paper start - reuse unpacking, credit to JellySquid (Didn't do full optimization though)
        int x = (int) (i >> 38);
        int y = (int) ((i << 52) >> 52);
        int z = (int) ((i << 26) >> 38);
        long k = SectionPosition.blockPosAsSectionLong(x, y, z);
        // Paper end
        EnumDirection[] aenumdirection = LightEngineBlock.e;
        int l = aenumdirection.length;

        for (int i1 = 0; i1 < l; ++i1) {
            EnumDirection enumdirection = aenumdirection[i1];
            long j1 = BlockPosition.getAdjacent(x, y, z, enumdirection); // Paper
            long k1 = SectionPosition.getAdjacentFromBlockPos(x, y, z, enumdirection); // Paper

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

        // Paper start
        int baseX = (int) (i >> 38);
        int baseY = (int) ((i << 52) >> 52);
        int baseZ = (int) ((i << 26) >> 38);
        long j1 = SectionPosition.blockPosAsSectionLong(baseX, baseY, baseZ);
        NibbleArray nibblearray = this.c.updating.getUpdatingOptimized(j1);
        // Paper end
        EnumDirection[] aenumdirection = LightEngineBlock.e;
        int k1 = aenumdirection.length;

        for (int l1 = 0; l1 < k1; ++l1) {
            EnumDirection enumdirection = aenumdirection[l1];
            // Paper start
            int newX = baseX + enumdirection.getAdjacentX();
            int newY = baseY + enumdirection.getAdjacentY();
            int newZ = baseZ + enumdirection.getAdjacentZ();
            long i2 = BlockPosition.asLong(newX, newY, newZ);

            if (i2 != j) {
                long j2 = SectionPosition.blockPosAsSectionLong(newX, newY, newZ);
                // Paper end
                NibbleArray nibblearray1;

                if (j1 == j2) {
                    nibblearray1 = nibblearray;
                } else {
                    nibblearray1 = ((LightEngineStorageBlock) this.c).updating.getUpdatingOptimized(j2); // Paper
                }

                if (nibblearray1 != null) {
                    int k2 = this.b(i2, i, this.getNibbleLightInverse(nibblearray1, newX, newY, newZ)); // Paper

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
