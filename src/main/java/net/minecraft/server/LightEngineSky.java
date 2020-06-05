package net.minecraft.server;

import org.apache.commons.lang3.mutable.MutableInt;

public final class LightEngineSky extends LightEngineLayer<LightEngineStorageSky.a, LightEngineStorageSky> {

    private static final EnumDirection[] e = EnumDirection.values();
    private static final EnumDirection[] f = new EnumDirection[]{EnumDirection.NORTH, EnumDirection.SOUTH, EnumDirection.WEST, EnumDirection.EAST};
    private final MutableInt mutableint = new MutableInt(); // Paper

    public LightEngineSky(ILightAccess ilightaccess) {
        super(ilightaccess, EnumSkyBlock.SKY, new LightEngineStorageSky(ilightaccess));
    }

    @Override
    protected int b(long i, long j, int k) {
        if (j == Long.MAX_VALUE) {
            return 15;
        } else {
            if (i == Long.MAX_VALUE) {
                if (!((LightEngineStorageSky) this.c).m(j)) {
                    return 15;
                }

                k = 0;
            }

            if (k >= 15) {
                return k;
            } else {
                //MutableInt mutableint = new MutableInt(); // Paper - share mutableint, single threaded
                // Paper start - use x/y/z and optimized block lookup
                int jx = (int) (j >> 38);
                int jy = (int) ((j << 52) >> 52);
                int jz = (int) ((j << 26) >> 38);
                IBlockData iblockdata = this.getBlockOptimized(jx, jy, jz, mutableint);
                int blockedLight = mutableint.getValue();
                if (blockedLight >= 15) {
                    // Paper end
                    return 15;
                } else {
                    // Paper start - inline math
                    int ix = (int) (i >> 38);
                    int iy = (int) ((i << 52) >> 52);
                    int iz = (int) ((i << 26) >> 38);
                    boolean flag = ix == jx && iz == jz;
                    int j2 = Integer.signum(jx - ix);
                    int k2 = Integer.signum(jy - iy);
                    int l2 = Integer.signum(jz - iz);
                    // Paper end
                    EnumDirection enumdirection;

                    if (i == Long.MAX_VALUE) {
                        enumdirection = EnumDirection.DOWN;
                    } else {
                        enumdirection = EnumDirection.a(j2, k2, l2);
                    }

                    IBlockData iblockdata1 = i == Long.MAX_VALUE ? Blocks.AIR.getBlockData() : this.getBlockOptimized(ix, iy, iz); // Paper
                    VoxelShape voxelshape;

                    if (enumdirection != null) {
                        voxelshape = this.a(iblockdata1, i, enumdirection);
                        VoxelShape voxelshape1 = this.a(iblockdata, j, enumdirection.opposite());

                        if (VoxelShapes.b(voxelshape, voxelshape1)) {
                            return 15;
                        }
                    } else {
                        voxelshape = this.a(iblockdata1, i, EnumDirection.DOWN);
                        if (VoxelShapes.b(voxelshape, VoxelShapes.a())) {
                            return 15;
                        }

                        int i3 = flag ? -1 : 0;
                        EnumDirection enumdirection1 = EnumDirection.a(j2, i3, l2);

                        if (enumdirection1 == null) {
                            return 15;
                        }

                        VoxelShape voxelshape2 = this.a(iblockdata, j, enumdirection1.opposite());

                        if (VoxelShapes.b(VoxelShapes.a(), voxelshape2)) {
                            return 15;
                        }
                    }

                    boolean flag1 = i == Long.MAX_VALUE || flag && iy > jy; // Paper rename vars to iy > jy

                    return flag1 && k == 0 && blockedLight == 0 ? 0 : k + Math.max(1, blockedLight); // Paper
                }
            }
        }
    }

    @Override
    protected void a(long i, int j, boolean flag) {
        // Paper start
        int baseX = (int) (i >> 38);
        int baseY = (int) ((i << 52) >> 52);
        int baseZ = (int) ((i << 26) >> 38);
        long k = SectionPosition.blockPosAsSectionLong(baseX, baseY, baseZ);
        int i1 = baseY & 15;
        int j1 = baseY >> 4;
        // Paper end
        int k1;

        if (i1 != 0) {
            k1 = 0;
        } else {
            int l1;

            for (l1 = 0; !((LightEngineStorageSky) this.c).g(SectionPosition.a(k, 0, -l1 - 1, 0)) && ((LightEngineStorageSky) this.c).a(j1 - l1 - 1); ++l1) {
                ;
            }

            k1 = l1;
        }

        int newBaseY = baseY + (-1 - k1 * 16); // Paper
        long i2 = BlockPosition.asLong(baseX, newBaseY, baseZ); // Paper
        long j2 = SectionPosition.blockPosAsSectionLong(baseX, newBaseY, baseZ); // Paper

        if (k == j2 || ((LightEngineStorageSky) this.c).g(j2)) {
            this.b(i, i2, j, flag);
        }

        long k2 = BlockPosition.asLong(baseX, baseY + 1, baseZ); // Paper
        long l2 = SectionPosition.blockPosAsSectionLong(baseX, baseY + 1, baseZ); // Paper

        if (k == l2 || ((LightEngineStorageSky) this.c).g(l2)) {
            this.b(i, k2, j, flag);
        }

        EnumDirection[] aenumdirection = LightEngineSky.f;
        int i3 = aenumdirection.length;
        int j3 = 0;

        while (j3 < i3) {
            EnumDirection enumdirection = aenumdirection[j3];
            int k3 = 0;

            while (true) {
                long l3 = BlockPosition.asLong(baseX + enumdirection.getAdjacentX(), baseY - k3, baseZ + enumdirection.getAdjacentZ()); // Paper
                long i4 = SectionPosition.blockPosAsSectionLong(baseX + enumdirection.getAdjacentX(), baseY - k3, baseZ + enumdirection.getAdjacentZ()); // Paper

                if (k == i4) {
                    this.b(i, l3, j, flag);
                } else {
                    if (((LightEngineStorageSky) this.c).g(i4)) {
                        this.b(i, l3, j, flag);
                    }

                    ++k3;
                    if (k3 <= k1 * 16) {
                        continue;
                    }
                }

                ++j3;
                break;
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
        EnumDirection[] aenumdirection = LightEngineSky.e;
        int k1 = aenumdirection.length;

        for (int l1 = 0; l1 < k1; ++l1) {
            EnumDirection enumdirection = aenumdirection[l1];
            // Paper start
            int newX = baseX + enumdirection.getAdjacentX();
            int newY = baseY + enumdirection.getAdjacentY();
            int newZ = baseZ + enumdirection.getAdjacentZ();
            long i2 = BlockPosition.asLong(newX, newY, newZ);
            long j2 = SectionPosition.blockPosAsSectionLong(newX, newY, newZ);
            // Paper end
            NibbleArray nibblearray1;

            if (j1 == j2) {
                nibblearray1 = nibblearray;
            } else {
                nibblearray1 = ((LightEngineStorageSky) this.c).updating.getUpdatingOptimized(j2); // Paper
            }

            if (nibblearray1 != null) {
                if (i2 != j) {
                    int k2 = this.b(i2, i, this.getNibbleLightInverse(nibblearray1, newX, newY, newZ)); // Paper

                    if (l > k2) {
                        l = k2;
                    }

                    if (l == 0) {
                        return l;
                    }
                }
            } else if (enumdirection != EnumDirection.DOWN) {
                for (i2 = BlockPosition.f(i2); !((LightEngineStorageSky) this.c).g(j2) && !((LightEngineStorageSky) this.c).n(j2); i2 = BlockPosition.a(i2, 0, 16, 0)) {
                    j2 = SectionPosition.a(j2, EnumDirection.UP);
                }

                NibbleArray nibblearray2 = this.c.updating.getUpdatingOptimized(j2); // Paper

                if (i2 != j) {
                    int l2;

                    if (nibblearray2 != null) {
                        l2 = this.b(i2, i, this.a(nibblearray2, i2));
                    } else {
                        l2 = ((LightEngineStorageSky) this.c).o(j2) ? 0 : 15;
                    }

                    if (l > l2) {
                        l = l2;
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
    protected void f(long i) {
        ((LightEngineStorageSky) this.c).d();
        long j = SectionPosition.e(i);

        if (((LightEngineStorageSky) this.c).g(j)) {
            super.f(i);
        } else {
            for (i = BlockPosition.f(i); !((LightEngineStorageSky) this.c).g(j) && !((LightEngineStorageSky) this.c).n(j); i = BlockPosition.a(i, 0, 16, 0)) {
                j = SectionPosition.a(j, EnumDirection.UP);
            }

            if (((LightEngineStorageSky) this.c).g(j)) {
                super.f(i);
            }
        }

    }
}
