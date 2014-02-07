package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class WorldGenVillagePieces {

    public static void a() {
        WorldGenFactory.a(WorldGenVillageLibrary.class, "ViBH");
        WorldGenFactory.a(WorldGenVillageFarm2.class, "ViDF");
        WorldGenFactory.a(WorldGenVillageFarm.class, "ViF");
        WorldGenFactory.a(WorldGenVillageLight.class, "ViL");
        WorldGenFactory.a(WorldGenVillageButcher.class, "ViPH");
        WorldGenFactory.a(WorldGenVillageHouse.class, "ViSH");
        WorldGenFactory.a(WorldGenVillageHut.class, "ViSmH");
        WorldGenFactory.a(WorldGenVillageTemple.class, "ViST");
        WorldGenFactory.a(WorldGenVillageBlacksmith.class, "ViS");
        WorldGenFactory.a(WorldGenVillageStartPiece.class, "ViStart");
        WorldGenFactory.a(WorldGenVillageRoad.class, "ViSR");
        WorldGenFactory.a(WorldGenVillageHouse2.class, "ViTRH");
        WorldGenFactory.a(WorldGenVillageWell.class, "ViW");
    }

    public static List a(Random random, int i) {
        ArrayList arraylist = new ArrayList();

        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageHouse.class, 4, MathHelper.nextInt(random, 2 + i, 4 + i * 2)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageTemple.class, 20, MathHelper.nextInt(random, 0 + i, 1 + i)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageLibrary.class, 20, MathHelper.nextInt(random, 0 + i, 2 + i)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageHut.class, 3, MathHelper.nextInt(random, 2 + i, 5 + i * 3)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageButcher.class, 15, MathHelper.nextInt(random, 0 + i, 2 + i)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageFarm2.class, 3, MathHelper.nextInt(random, 1 + i, 4 + i)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageFarm.class, 3, MathHelper.nextInt(random, 2 + i, 4 + i * 2)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageBlacksmith.class, 15, MathHelper.nextInt(random, 0, 1 + i)));
        arraylist.add(new WorldGenVillagePieceWeight(WorldGenVillageHouse2.class, 8, MathHelper.nextInt(random, 0 + i, 3 + i * 2)));
        Iterator iterator = arraylist.iterator();

        while (iterator.hasNext()) {
            if (((WorldGenVillagePieceWeight) iterator.next()).d == 0) {
                iterator.remove();
            }
        }

        return arraylist;
    }

    private static int a(List list) {
        boolean flag = false;
        int i = 0;

        WorldGenVillagePieceWeight worldgenvillagepieceweight;

        for (Iterator iterator = list.iterator(); iterator.hasNext(); i += worldgenvillagepieceweight.b) {
            worldgenvillagepieceweight = (WorldGenVillagePieceWeight) iterator.next();
            if (worldgenvillagepieceweight.d > 0 && worldgenvillagepieceweight.c < worldgenvillagepieceweight.d) {
                flag = true;
            }
        }

        return flag ? i : -1;
    }

    private static WorldGenVillagePiece a(WorldGenVillageStartPiece worldgenvillagestartpiece, WorldGenVillagePieceWeight worldgenvillagepieceweight, List list, Random random, int i, int j, int k, int l, int i1) {
        Class oclass = worldgenvillagepieceweight.a;
        Object object = null;

        if (oclass == WorldGenVillageHouse.class) {
            object = WorldGenVillageHouse.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageTemple.class) {
            object = WorldGenVillageTemple.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageLibrary.class) {
            object = WorldGenVillageLibrary.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageHut.class) {
            object = WorldGenVillageHut.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageButcher.class) {
            object = WorldGenVillageButcher.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageFarm2.class) {
            object = WorldGenVillageFarm2.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageFarm.class) {
            object = WorldGenVillageFarm.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageBlacksmith.class) {
            object = WorldGenVillageBlacksmith.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        } else if (oclass == WorldGenVillageHouse2.class) {
            object = WorldGenVillageHouse2.a(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
        }

        return (WorldGenVillagePiece) object;
    }

    private static WorldGenVillagePiece c(WorldGenVillageStartPiece worldgenvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
        int j1 = a(worldgenvillagestartpiece.e);

        if (j1 <= 0) {
            return null;
        } else {
            int k1 = 0;

            while (k1 < 5) {
                ++k1;
                int l1 = random.nextInt(j1);
                Iterator iterator = worldgenvillagestartpiece.e.iterator();

                while (iterator.hasNext()) {
                    WorldGenVillagePieceWeight worldgenvillagepieceweight = (WorldGenVillagePieceWeight) iterator.next();

                    l1 -= worldgenvillagepieceweight.b;
                    if (l1 < 0) {
                        if (!worldgenvillagepieceweight.a(i1) || worldgenvillagepieceweight == worldgenvillagestartpiece.d && worldgenvillagestartpiece.e.size() > 1) {
                            break;
                        }

                        WorldGenVillagePiece worldgenvillagepiece = a(worldgenvillagestartpiece, worldgenvillagepieceweight, list, random, i, j, k, l, i1);

                        if (worldgenvillagepiece != null) {
                            ++worldgenvillagepieceweight.c;
                            worldgenvillagestartpiece.d = worldgenvillagepieceweight;
                            if (!worldgenvillagepieceweight.a()) {
                                worldgenvillagestartpiece.e.remove(worldgenvillagepieceweight);
                            }

                            return worldgenvillagepiece;
                        }
                    }
                }
            }

            StructureBoundingBox structureboundingbox = WorldGenVillageLight.a(worldgenvillagestartpiece, list, random, i, j, k, l);

            if (structureboundingbox != null) {
                return new WorldGenVillageLight(worldgenvillagestartpiece, i1, random, structureboundingbox, l);
            } else {
                return null;
            }
        }
    }

    private static StructurePiece d(WorldGenVillageStartPiece worldgenvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
        if (i1 > 50) {
            return null;
        } else if (Math.abs(i - worldgenvillagestartpiece.c().a) <= 112 && Math.abs(k - worldgenvillagestartpiece.c().c) <= 112) {
            WorldGenVillagePiece worldgenvillagepiece = c(worldgenvillagestartpiece, list, random, i, j, k, l, i1 + 1);

            if (worldgenvillagepiece != null) {
                int j1 = (worldgenvillagepiece.f.a + worldgenvillagepiece.f.d) / 2;
                int k1 = (worldgenvillagepiece.f.c + worldgenvillagepiece.f.f) / 2;
                int l1 = worldgenvillagepiece.f.d - worldgenvillagepiece.f.a;
                int i2 = worldgenvillagepiece.f.f - worldgenvillagepiece.f.c;
                int j2 = l1 > i2 ? l1 : i2;

                if (worldgenvillagestartpiece.e().a(j1, k1, j2 / 2 + 4, WorldGenVillage.e)) {
                    list.add(worldgenvillagepiece);
                    worldgenvillagestartpiece.i.add(worldgenvillagepiece);
                    return worldgenvillagepiece;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    private static StructurePiece e(WorldGenVillageStartPiece worldgenvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
        if (i1 > 3 + worldgenvillagestartpiece.c) {
            return null;
        } else if (Math.abs(i - worldgenvillagestartpiece.c().a) <= 112 && Math.abs(k - worldgenvillagestartpiece.c().c) <= 112) {
            StructureBoundingBox structureboundingbox = WorldGenVillageRoad.a(worldgenvillagestartpiece, list, random, i, j, k, l);

            if (structureboundingbox != null && structureboundingbox.b > 10) {
                WorldGenVillageRoad worldgenvillageroad = new WorldGenVillageRoad(worldgenvillagestartpiece, i1, random, structureboundingbox, l);
                int j1 = (worldgenvillageroad.f.a + worldgenvillageroad.f.d) / 2;
                int k1 = (worldgenvillageroad.f.c + worldgenvillageroad.f.f) / 2;
                int l1 = worldgenvillageroad.f.d - worldgenvillageroad.f.a;
                int i2 = worldgenvillageroad.f.f - worldgenvillageroad.f.c;
                int j2 = l1 > i2 ? l1 : i2;

                if (worldgenvillagestartpiece.e().a(j1, k1, j2 / 2 + 4, WorldGenVillage.e)) {
                    list.add(worldgenvillageroad);
                    worldgenvillagestartpiece.j.add(worldgenvillageroad);
                    return worldgenvillageroad;
                }
            }

            return null;
        } else {
            return null;
        }
    }

    static StructurePiece a(WorldGenVillageStartPiece worldgenvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
        return d(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
    }

    static StructurePiece b(WorldGenVillageStartPiece worldgenvillagestartpiece, List list, Random random, int i, int j, int k, int l, int i1) {
        return e(worldgenvillagestartpiece, list, random, i, j, k, l, i1);
    }
}
