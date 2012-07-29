package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CraftBukkit - We import this because the compiler hates package-private methods in an external jar
class MinecartTrackLogic {

    private World b;
    private int c;
    private int d;
    private int e;
    private final boolean f;
    private List g;

    final BlockMinecartTrack a;

    public MinecartTrackLogic(BlockMinecartTrack blockminecarttrack, World world, int i, int j, int k) {
        this.a = blockminecarttrack;
        this.g = new ArrayList();
        this.b = world;
        this.c = i;
        this.d = j;
        this.e = k;
        int l = world.getTypeId(i, j, k);
        int i1 = world.getData(i, j, k);

        if (BlockMinecartTrack.a((BlockMinecartTrack) Block.byId[l])) {
            this.f = true;
            i1 &= -9;
        } else {
            this.f = false;
        }

        this.a(i1);
    }

    private void a(int i) {
        this.g.clear();
        if (i == 0) {
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        } else if (i == 1) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
        } else if (i == 2) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d + 1, this.e));
        } else if (i == 3) {
            this.g.add(new ChunkPosition(this.c - 1, this.d + 1, this.e));
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
        } else if (i == 4) {
            this.g.add(new ChunkPosition(this.c, this.d + 1, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        } else if (i == 5) {
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
            this.g.add(new ChunkPosition(this.c, this.d + 1, this.e + 1));
        } else if (i == 6) {
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        } else if (i == 7) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e + 1));
        } else if (i == 8) {
            this.g.add(new ChunkPosition(this.c - 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
        } else if (i == 9) {
            this.g.add(new ChunkPosition(this.c + 1, this.d, this.e));
            this.g.add(new ChunkPosition(this.c, this.d, this.e - 1));
        }
    }

    private void a() {
        for (int i = 0; i < this.g.size(); ++i) {
            MinecartTrackLogic minecarttracklogic = this.a((ChunkPosition) this.g.get(i));

            if (minecarttracklogic != null && minecarttracklogic.b(this)) {
                this.g.set(i, new ChunkPosition(minecarttracklogic.c, minecarttracklogic.d, minecarttracklogic.e));
            } else {
                this.g.remove(i--);
            }
        }
    }

    private boolean a(int i, int j, int k) {
        return BlockMinecartTrack.d_(this.b, i, j, k) ? true : (BlockMinecartTrack.d_(this.b, i, j + 1, k) ? true : BlockMinecartTrack.d_(this.b, i, j - 1, k));
    }

    private MinecartTrackLogic a(ChunkPosition chunkposition) {
        return BlockMinecartTrack.d_(this.b, chunkposition.x, chunkposition.y, chunkposition.z) ? new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y, chunkposition.z) : (BlockMinecartTrack.d_(this.b, chunkposition.x, chunkposition.y + 1, chunkposition.z) ? new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y + 1, chunkposition.z) : (BlockMinecartTrack.d_(this.b, chunkposition.x, chunkposition.y - 1, chunkposition.z) ? new MinecartTrackLogic(this.a, this.b, chunkposition.x, chunkposition.y - 1, chunkposition.z) : null));
    }

    private boolean b(MinecartTrackLogic minecarttracklogic) {
        Iterator iterator = this.g.iterator();

        ChunkPosition chunkposition;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            chunkposition = (ChunkPosition) iterator.next();
        } while (chunkposition.x != minecarttracklogic.c || chunkposition.z != minecarttracklogic.e);

        return true;
    }

    private boolean b(int i, int j, int k) {
        Iterator iterator = this.g.iterator();

        ChunkPosition chunkposition;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            chunkposition = (ChunkPosition) iterator.next();
        } while (chunkposition.x != i || chunkposition.z != k);

        return true;
    }

    private int b() {
        int i = 0;

        if (this.a(this.c, this.d, this.e - 1)) {
            ++i;
        }

        if (this.a(this.c, this.d, this.e + 1)) {
            ++i;
        }

        if (this.a(this.c - 1, this.d, this.e)) {
            ++i;
        }

        if (this.a(this.c + 1, this.d, this.e)) {
            ++i;
        }

        return i;
    }

    private boolean c(MinecartTrackLogic minecarttracklogic) {
        if (this.b(minecarttracklogic)) {
            return true;
        } else if (this.g.size() == 2) {
            return false;
        } else if (this.g.isEmpty()) {
            return true;
        } else {
            ChunkPosition chunkposition = (ChunkPosition) this.g.get(0);

            return true;
        }
    }

    private void d(MinecartTrackLogic minecarttracklogic) {
        this.g.add(new ChunkPosition(minecarttracklogic.c, minecarttracklogic.d, minecarttracklogic.e));
        boolean flag = this.b(this.c, this.d, this.e - 1);
        boolean flag1 = this.b(this.c, this.d, this.e + 1);
        boolean flag2 = this.b(this.c - 1, this.d, this.e);
        boolean flag3 = this.b(this.c + 1, this.d, this.e);
        byte b0 = -1;

        if (flag || flag1) {
            b0 = 0;
        }

        if (flag2 || flag3) {
            b0 = 1;
        }

        if (!this.f) {
            if (flag1 && flag3 && !flag && !flag2) {
                b0 = 6;
            }

            if (flag1 && flag2 && !flag && !flag3) {
                b0 = 7;
            }

            if (flag && flag2 && !flag1 && !flag3) {
                b0 = 8;
            }

            if (flag && flag3 && !flag1 && !flag2) {
                b0 = 9;
            }
        }

        if (b0 == 0) {
            if (BlockMinecartTrack.d_(this.b, this.c, this.d + 1, this.e - 1)) {
                b0 = 4;
            }

            if (BlockMinecartTrack.d_(this.b, this.c, this.d + 1, this.e + 1)) {
                b0 = 5;
            }
        }

        if (b0 == 1) {
            if (BlockMinecartTrack.d_(this.b, this.c + 1, this.d + 1, this.e)) {
                b0 = 2;
            }

            if (BlockMinecartTrack.d_(this.b, this.c - 1, this.d + 1, this.e)) {
                b0 = 3;
            }
        }

        if (b0 < 0) {
            b0 = 0;
        }

        int i = b0;

        if (this.f) {
            i = this.b.getData(this.c, this.d, this.e) & 8 | b0;
        }

        this.b.setData(this.c, this.d, this.e, i);
    }

    private boolean c(int i, int j, int k) {
        MinecartTrackLogic minecarttracklogic = this.a(new ChunkPosition(i, j, k));

        if (minecarttracklogic == null) {
            return false;
        } else {
            minecarttracklogic.a();
            return minecarttracklogic.c(this);
        }
    }

    public void a(boolean flag, boolean flag1) {
        boolean flag2 = this.c(this.c, this.d, this.e - 1);
        boolean flag3 = this.c(this.c, this.d, this.e + 1);
        boolean flag4 = this.c(this.c - 1, this.d, this.e);
        boolean flag5 = this.c(this.c + 1, this.d, this.e);
        byte b0 = -1;

        if ((flag2 || flag3) && !flag4 && !flag5) {
            b0 = 0;
        }

        if ((flag4 || flag5) && !flag2 && !flag3) {
            b0 = 1;
        }

        if (!this.f) {
            if (flag3 && flag5 && !flag2 && !flag4) {
                b0 = 6;
            }

            if (flag3 && flag4 && !flag2 && !flag5) {
                b0 = 7;
            }

            if (flag2 && flag4 && !flag3 && !flag5) {
                b0 = 8;
            }

            if (flag2 && flag5 && !flag3 && !flag4) {
                b0 = 9;
            }
        }

        if (b0 == -1) {
            if (flag2 || flag3) {
                b0 = 0;
            }

            if (flag4 || flag5) {
                b0 = 1;
            }

            if (!this.f) {
                if (flag) {
                    if (flag3 && flag5) {
                        b0 = 6;
                    }

                    if (flag4 && flag3) {
                        b0 = 7;
                    }

                    if (flag5 && flag2) {
                        b0 = 9;
                    }

                    if (flag2 && flag4) {
                        b0 = 8;
                    }
                } else {
                    if (flag2 && flag4) {
                        b0 = 8;
                    }

                    if (flag5 && flag2) {
                        b0 = 9;
                    }

                    if (flag4 && flag3) {
                        b0 = 7;
                    }

                    if (flag3 && flag5) {
                        b0 = 6;
                    }
                }
            }
        }

        if (b0 == 0) {
            if (BlockMinecartTrack.d_(this.b, this.c, this.d + 1, this.e - 1)) {
                b0 = 4;
            }

            if (BlockMinecartTrack.d_(this.b, this.c, this.d + 1, this.e + 1)) {
                b0 = 5;
            }
        }

        if (b0 == 1) {
            if (BlockMinecartTrack.d_(this.b, this.c + 1, this.d + 1, this.e)) {
                b0 = 2;
            }

            if (BlockMinecartTrack.d_(this.b, this.c - 1, this.d + 1, this.e)) {
                b0 = 3;
            }
        }

        if (b0 < 0) {
            b0 = 0;
        }

        this.a(b0);
        int i = b0;

        if (this.f) {
            i = this.b.getData(this.c, this.d, this.e) & 8 | b0;
        }

        if (flag1 || this.b.getData(this.c, this.d, this.e) != i) {
            this.b.setData(this.c, this.d, this.e, i);
            Iterator iterator = this.g.iterator();

            while (iterator.hasNext()) {
                ChunkPosition chunkposition = (ChunkPosition) iterator.next();
                MinecartTrackLogic minecarttracklogic = this.a(chunkposition);

                if (minecarttracklogic != null) {
                    minecarttracklogic.a();
                    if (minecarttracklogic.c(this)) {
                        minecarttracklogic.d(this);
                    }
                }
            }
        }
    }

    static int a(MinecartTrackLogic minecarttracklogic) {
        return minecarttracklogic.b();
    }
}
