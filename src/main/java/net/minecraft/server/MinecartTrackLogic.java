package net.minecraft.server;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

public class MinecartTrackLogic {

    private final World a;
    private final BlockPosition b;
    private final BlockMinecartTrackAbstract c;
    private IBlockData d;
    private final boolean e;
    private final List<BlockPosition> f = Lists.newArrayList();

    public MinecartTrackLogic(World world, BlockPosition blockposition, IBlockData iblockdata) {
        this.a = world;
        this.b = blockposition;
        this.d = iblockdata;
        this.c = (BlockMinecartTrackAbstract) iblockdata.getBlock();
        BlockPropertyTrackPosition blockpropertytrackposition = (BlockPropertyTrackPosition) iblockdata.get(this.c.d());

        this.e = this.c.c();
        this.a(blockpropertytrackposition);
    }

    public List<BlockPosition> a() {
        return this.f;
    }

    private void a(BlockPropertyTrackPosition blockpropertytrackposition) {
        this.f.clear();
        switch (blockpropertytrackposition) {
            case NORTH_SOUTH:
                this.f.add(this.b.north());
                this.f.add(this.b.south());
                break;
            case EAST_WEST:
                this.f.add(this.b.west());
                this.f.add(this.b.east());
                break;
            case ASCENDING_EAST:
                this.f.add(this.b.west());
                this.f.add(this.b.east().up());
                break;
            case ASCENDING_WEST:
                this.f.add(this.b.west().up());
                this.f.add(this.b.east());
                break;
            case ASCENDING_NORTH:
                this.f.add(this.b.north().up());
                this.f.add(this.b.south());
                break;
            case ASCENDING_SOUTH:
                this.f.add(this.b.north());
                this.f.add(this.b.south().up());
                break;
            case SOUTH_EAST:
                this.f.add(this.b.east());
                this.f.add(this.b.south());
                break;
            case SOUTH_WEST:
                this.f.add(this.b.west());
                this.f.add(this.b.south());
                break;
            case NORTH_WEST:
                this.f.add(this.b.west());
                this.f.add(this.b.north());
                break;
            case NORTH_EAST:
                this.f.add(this.b.east());
                this.f.add(this.b.north());
        }

    }

    private void d() {
        for (int i = 0; i < this.f.size(); ++i) {
            MinecartTrackLogic minecarttracklogic = this.b((BlockPosition) this.f.get(i));

            if (minecarttracklogic != null && minecarttracklogic.a(this)) {
                this.f.set(i, minecarttracklogic.b);
            } else {
                this.f.remove(i--);
            }
        }

    }

    private boolean a(BlockPosition blockposition) {
        return BlockMinecartTrackAbstract.a(this.a, blockposition) || BlockMinecartTrackAbstract.a(this.a, blockposition.up()) || BlockMinecartTrackAbstract.a(this.a, blockposition.down());
    }

    @Nullable
    private MinecartTrackLogic b(BlockPosition blockposition) {
        IBlockData iblockdata = this.a.getType(blockposition);

        if (BlockMinecartTrackAbstract.g(iblockdata)) {
            return new MinecartTrackLogic(this.a, blockposition, iblockdata);
        } else {
            BlockPosition blockposition1 = blockposition.up();

            iblockdata = this.a.getType(blockposition1);
            if (BlockMinecartTrackAbstract.g(iblockdata)) {
                return new MinecartTrackLogic(this.a, blockposition1, iblockdata);
            } else {
                blockposition1 = blockposition.down();
                iblockdata = this.a.getType(blockposition1);
                return BlockMinecartTrackAbstract.g(iblockdata) ? new MinecartTrackLogic(this.a, blockposition1, iblockdata) : null;
            }
        }
    }

    private boolean a(MinecartTrackLogic minecarttracklogic) {
        return this.c(minecarttracklogic.b);
    }

    private boolean c(BlockPosition blockposition) {
        for (int i = 0; i < this.f.size(); ++i) {
            BlockPosition blockposition1 = (BlockPosition) this.f.get(i);

            if (blockposition1.getX() == blockposition.getX() && blockposition1.getZ() == blockposition.getZ()) {
                return true;
            }
        }

        return false;
    }

    protected int b() {
        int i = 0;
        Iterator iterator = EnumDirection.EnumDirectionLimit.HORIZONTAL.iterator();

        while (iterator.hasNext()) {
            EnumDirection enumdirection = (EnumDirection) iterator.next();

            if (this.a(this.b.shift(enumdirection))) {
                ++i;
            }
        }

        return i;
    }

    private boolean b(MinecartTrackLogic minecarttracklogic) {
        return this.a(minecarttracklogic) || this.f.size() != 2;
    }

    private void c(MinecartTrackLogic minecarttracklogic) {
        this.f.add(minecarttracklogic.b);
        BlockPosition blockposition = this.b.north();
        BlockPosition blockposition1 = this.b.south();
        BlockPosition blockposition2 = this.b.west();
        BlockPosition blockposition3 = this.b.east();
        boolean flag = this.c(blockposition);
        boolean flag1 = this.c(blockposition1);
        boolean flag2 = this.c(blockposition2);
        boolean flag3 = this.c(blockposition3);
        BlockPropertyTrackPosition blockpropertytrackposition = null;

        if (flag || flag1) {
            blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_SOUTH;
        }

        if (flag2 || flag3) {
            blockpropertytrackposition = BlockPropertyTrackPosition.EAST_WEST;
        }

        if (!this.e) {
            if (flag1 && flag3 && !flag && !flag2) {
                blockpropertytrackposition = BlockPropertyTrackPosition.SOUTH_EAST;
            }

            if (flag1 && flag2 && !flag && !flag3) {
                blockpropertytrackposition = BlockPropertyTrackPosition.SOUTH_WEST;
            }

            if (flag && flag2 && !flag1 && !flag3) {
                blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_WEST;
            }

            if (flag && flag3 && !flag1 && !flag2) {
                blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_EAST;
            }
        }

        if (blockpropertytrackposition == BlockPropertyTrackPosition.NORTH_SOUTH) {
            if (BlockMinecartTrackAbstract.a(this.a, blockposition.up())) {
                blockpropertytrackposition = BlockPropertyTrackPosition.ASCENDING_NORTH;
            }

            if (BlockMinecartTrackAbstract.a(this.a, blockposition1.up())) {
                blockpropertytrackposition = BlockPropertyTrackPosition.ASCENDING_SOUTH;
            }
        }

        if (blockpropertytrackposition == BlockPropertyTrackPosition.EAST_WEST) {
            if (BlockMinecartTrackAbstract.a(this.a, blockposition3.up())) {
                blockpropertytrackposition = BlockPropertyTrackPosition.ASCENDING_EAST;
            }

            if (BlockMinecartTrackAbstract.a(this.a, blockposition2.up())) {
                blockpropertytrackposition = BlockPropertyTrackPosition.ASCENDING_WEST;
            }
        }

        if (blockpropertytrackposition == null) {
            blockpropertytrackposition = BlockPropertyTrackPosition.NORTH_SOUTH;
        }

        this.d = (IBlockData) this.d.set(this.c.d(), blockpropertytrackposition);
        this.a.setTypeAndData(this.b, this.d, 3);
    }

    private boolean d(BlockPosition blockposition) {
        MinecartTrackLogic minecarttracklogic = this.b(blockposition);

        if (minecarttracklogic == null) {
            return false;
        } else {
            minecarttracklogic.d();
            return minecarttracklogic.b(this);
        }
    }

    public MinecartTrackLogic a(boolean flag, boolean flag1, BlockPropertyTrackPosition blockpropertytrackposition) {
        BlockPosition blockposition = this.b.north();
        BlockPosition blockposition1 = this.b.south();
        BlockPosition blockposition2 = this.b.west();
        BlockPosition blockposition3 = this.b.east();
        boolean flag2 = this.d(blockposition);
        boolean flag3 = this.d(blockposition1);
        boolean flag4 = this.d(blockposition2);
        boolean flag5 = this.d(blockposition3);
        BlockPropertyTrackPosition blockpropertytrackposition1 = null;
        boolean flag6 = flag2 || flag3;
        boolean flag7 = flag4 || flag5;

        if (flag6 && !flag7) {
            blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_SOUTH;
        }

        if (flag7 && !flag6) {
            blockpropertytrackposition1 = BlockPropertyTrackPosition.EAST_WEST;
        }

        boolean flag8 = flag3 && flag5;
        boolean flag9 = flag3 && flag4;
        boolean flag10 = flag2 && flag5;
        boolean flag11 = flag2 && flag4;

        if (!this.e) {
            if (flag8 && !flag2 && !flag4) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_EAST;
            }

            if (flag9 && !flag2 && !flag5) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_WEST;
            }

            if (flag11 && !flag3 && !flag5) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_WEST;
            }

            if (flag10 && !flag3 && !flag4) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_EAST;
            }
        }

        if (blockpropertytrackposition1 == null) {
            if (flag6 && flag7) {
                blockpropertytrackposition1 = blockpropertytrackposition;
            } else if (flag6) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_SOUTH;
            } else if (flag7) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.EAST_WEST;
            }

            if (!this.e) {
                if (flag) {
                    if (flag8) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_EAST;
                    }

                    if (flag9) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_WEST;
                    }

                    if (flag10) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_EAST;
                    }

                    if (flag11) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_WEST;
                    }
                } else {
                    if (flag11) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_WEST;
                    }

                    if (flag10) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.NORTH_EAST;
                    }

                    if (flag9) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_WEST;
                    }

                    if (flag8) {
                        blockpropertytrackposition1 = BlockPropertyTrackPosition.SOUTH_EAST;
                    }
                }
            }
        }

        if (blockpropertytrackposition1 == BlockPropertyTrackPosition.NORTH_SOUTH) {
            if (BlockMinecartTrackAbstract.a(this.a, blockposition.up())) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.ASCENDING_NORTH;
            }

            if (BlockMinecartTrackAbstract.a(this.a, blockposition1.up())) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.ASCENDING_SOUTH;
            }
        }

        if (blockpropertytrackposition1 == BlockPropertyTrackPosition.EAST_WEST) {
            if (BlockMinecartTrackAbstract.a(this.a, blockposition3.up())) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.ASCENDING_EAST;
            }

            if (BlockMinecartTrackAbstract.a(this.a, blockposition2.up())) {
                blockpropertytrackposition1 = BlockPropertyTrackPosition.ASCENDING_WEST;
            }
        }

        if (blockpropertytrackposition1 == null) {
            blockpropertytrackposition1 = blockpropertytrackposition;
        }

        this.a(blockpropertytrackposition1);
        this.d = (IBlockData) this.d.set(this.c.d(), blockpropertytrackposition1);
        if (flag1 || this.a.getType(this.b) != this.d) {
            this.a.setTypeAndData(this.b, this.d, 3);

            for (int i = 0; i < this.f.size(); ++i) {
                MinecartTrackLogic minecarttracklogic = this.b((BlockPosition) this.f.get(i));

                if (minecarttracklogic != null) {
                    minecarttracklogic.d();
                    if (minecarttracklogic.b(this)) {
                        minecarttracklogic.c(this);
                    }
                }
            }
        }

        return this;
    }

    public IBlockData c() {
        return this.d;
    }
}
