package net.minecraft.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.util.LongBaseHashtable;
import org.bukkit.craftbukkit.util.EntryBase;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
// CraftBukkit end

public final class SpawnerCreature {
    // CraftBukkit start
    // private static HashMap b = new HashMap(); // Moved local to spawnEntities
    static private class ChunkEntry extends EntryBase {
        public boolean spawn;

        public ChunkEntry(int x, int z, boolean spawn) {
            super(LongHash.toLong(x, z));
            this.spawn = spawn;
        }

        int getX() {
            return LongHash.msw(key);
        }

        int getZ() {
            return LongHash.lsw(key);
        }
    }
    // CraftBukkit end

    protected static final Class[] a = new Class[] { EntitySpider.class, EntityZombie.class, EntitySkeleton.class};

    public SpawnerCreature() {}

    protected static ChunkPosition getRandomPosition(World world, int i, int j) {
        Chunk chunk = world.getChunkAt(i, j);
        int k = i * 16 + world.random.nextInt(16);
        int l = world.random.nextInt(chunk == null ? 128 : Math.max(128, chunk.g()));
        int i1 = j * 16 + world.random.nextInt(16);

        return new ChunkPosition(k, l, i1);
    }

    public static final int spawnEntities(World world, boolean flag, boolean flag1) {
        if (!flag && !flag1) {
            return 0;
        } else {
            // CraftBukkit start
            // b.clear();
            LongBaseHashtable chunkCoords = new LongBaseHashtable();
            // CraftBukkit end

            int i;
            int j;

            for (i = 0; i < world.players.size(); ++i) {
                EntityHuman entityhuman = (EntityHuman) world.players.get(i);
                int k = MathHelper.floor(entityhuman.locX / 16.0D);

                j = MathHelper.floor(entityhuman.locZ / 16.0D);
                byte b0 = 8;

                for (int l = -b0; l <= b0; ++l) {
                    for (int i1 = -b0; i1 <= b0; ++i1) {
                        boolean flag2 = l == -b0 || l == b0 || i1 == -b0 || i1 == b0;
                        // CraftBukkit start
                        long chunkCoord = LongHash.toLong(l + k, i1 + j);

                        if (!flag2) {
                            chunkCoords.put(new ChunkEntry(l + k, i1 + j, false));
                        } else if (!chunkCoords.containsKey(chunkCoord)) {
                            chunkCoords.put(new ChunkEntry(l + k, i1 + j, true));
                        }
                        // CraftBukkit end
                    }
                }
            }

            i = 0;
            ChunkCoordinates chunkcoordinates = world.getSpawn();
            java.util.ArrayList<EntryBase> b = chunkCoords.entries(); // CraftBukkit
            EnumCreatureType[] aenumcreaturetype = EnumCreatureType.values();

            j = aenumcreaturetype.length;

            for (int j1 = 0; j1 < j; ++j1) {
                EnumCreatureType enumcreaturetype = aenumcreaturetype[j1];

                // CraftBukkit start - use per-world spawn limits
                int limit = 0;
                switch (enumcreaturetype) {
                    case MONSTER:
                        limit = world.getWorld().getMonsterSpawnLimit();
                        break;
                    case CREATURE:
                        limit = world.getWorld().getAnimalSpawnLimit();
                        break;
                    case WATER_CREATURE:
                        limit = world.getWorld().getWaterAnimalSpawnLimit();
                        break;
                }

                if (limit == 0) {
                    return 0;
                }
                // CraftBukkit end

                if ((!enumcreaturetype.d() || flag1) && (enumcreaturetype.d() || flag) && world.a(enumcreaturetype.a()) <= limit * b.size() / 256) { // CraftBukkit - use per-world limits

                    // CraftBukkit start
                    label108:
                    for (EntryBase base : b) {
                        ChunkEntry entry = (SpawnerCreature.ChunkEntry) base;
                        if (!entry.spawn) {
                            ChunkPosition chunkposition = getRandomPosition(world, entry.getX(), entry.getZ());
                            // CraftBukkit end
                            int k1 = chunkposition.x;
                            int l1 = chunkposition.y;
                            int i2 = chunkposition.z;

                            if (!world.e(k1, l1, i2) && world.getMaterial(k1, l1, i2) == enumcreaturetype.c()) {
                                int j2 = 0;
                                int k2 = 0;

                                while (k2 < 3) {
                                    int l2 = k1;
                                    int i3 = l1;
                                    int j3 = i2;
                                    byte b1 = 6;
                                    BiomeMeta biomemeta = null;
                                    int k3 = 0;

                                    while (true) {
                                        if (k3 < 4) {
                                            label101: {
                                                l2 += world.random.nextInt(b1) - world.random.nextInt(b1);
                                                i3 += world.random.nextInt(1) - world.random.nextInt(1);
                                                j3 += world.random.nextInt(b1) - world.random.nextInt(b1);
                                                if (a(enumcreaturetype, world, l2, i3, j3)) {
                                                    float f = (float) l2 + 0.5F;
                                                    float f1 = (float) i3;
                                                    float f2 = (float) j3 + 0.5F;

                                                    if (world.findNearbyPlayer((double) f, (double) f1, (double) f2, 24.0D) == null) {
                                                        float f3 = f - (float) chunkcoordinates.x;
                                                        float f4 = f1 - (float) chunkcoordinates.y;
                                                        float f5 = f2 - (float) chunkcoordinates.z;
                                                        float f6 = f3 * f3 + f4 * f4 + f5 * f5;

                                                        if (f6 >= 576.0F) {
                                                            if (biomemeta == null) {
                                                                biomemeta = world.a(enumcreaturetype, l2, i3, j3);
                                                                if (biomemeta == null) {
                                                                    break label101;
                                                                }
                                                            }

                                                            EntityLiving entityliving;

                                                            try {
                                                                entityliving = (EntityLiving) biomemeta.a.getConstructor(new Class[] { World.class}).newInstance(new Object[] { world});
                                                            } catch (Exception exception) {
                                                                exception.printStackTrace();
                                                                return i;
                                                            }

                                                            // CraftBukkit - made slimes spawn less often in FLAT worlds.
                                                            if (entityliving instanceof EntitySlime && world.worldData.getType() == WorldType.FLAT && world.random.nextInt(200) == 0) return 0;

                                                            entityliving.setPositionRotation((double) f, (double) f1, (double) f2, world.random.nextFloat() * 360.0F, 0.0F);
                                                            if (entityliving.canSpawn()) {
                                                                ++j2;
                                                                // CraftBukkit - added a reason for spawning this creature
                                                                world.addEntity(entityliving, SpawnReason.NATURAL);
                                                                a(entityliving, world, f, f1, f2);
                                                                if (j2 >= entityliving.q()) {
                                                                    continue label108;
                                                                }
                                                            }

                                                            i += j2;
                                                        }
                                                    }
                                                }

                                                ++k3;
                                                continue;
                                            }
                                        }

                                        ++k2;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return i;
        }
    }

    public static boolean a(EnumCreatureType enumcreaturetype, World world, int i, int j, int k) {
        if (enumcreaturetype.c() == Material.WATER) {
            return world.getMaterial(i, j, k).isLiquid() && !world.e(i, j + 1, k);
        } else {
            int l = world.getTypeId(i, j - 1, k);

            return Block.g(l) && l != Block.BEDROCK.id && !world.e(i, j, k) && !world.getMaterial(i, j, k).isLiquid() && !world.e(i, j + 1, k);
        }
    }

    private static void a(EntityLiving entityliving, World world, float f, float f1, float f2) {
        if (entityliving.dead) return; // CraftBukkit
        if (entityliving instanceof EntitySpider && world.random.nextInt(100) == 0) {
            EntitySkeleton entityskeleton = new EntitySkeleton(world);

            entityskeleton.setPositionRotation((double) f, (double) f1, (double) f2, entityliving.yaw, 0.0F);
            // CraftBukkit - added a reason for spawning this creature
            world.addEntity(entityskeleton, SpawnReason.JOCKEY);
            entityskeleton.mount(entityliving);
        } else if (entityliving instanceof EntitySheep) {
            ((EntitySheep) entityliving).setColor(EntitySheep.a(world.random));
        } else if (entityliving instanceof EntityOcelot && world.random.nextInt(7) == 0) {
            for (int i = 0; i < 2; ++i) {
                EntityOcelot entityocelot = new EntityOcelot(world);

                entityocelot.setPositionRotation((double) f, (double) f1, (double) f2, entityliving.yaw, 0.0F);
                entityocelot.setAge(-24000);
                world.addEntity(entityocelot, SpawnReason.NATURAL); // CraftBukkit - SpawnReason
            }
        }
    }

    public static void a(World world, BiomeBase biomebase, int i, int j, int k, int l, Random random) {
        List list = biomebase.getMobs(EnumCreatureType.CREATURE);

        if (!list.isEmpty()) {
            while (random.nextFloat() < biomebase.f()) {
                BiomeMeta biomemeta = (BiomeMeta) WeightedRandom.a(world.random, (Collection) list);
                int i1 = biomemeta.b + random.nextInt(1 + biomemeta.c - biomemeta.b);
                int j1 = i + random.nextInt(k);
                int k1 = j + random.nextInt(l);
                int l1 = j1;
                int i2 = k1;

                for (int j2 = 0; j2 < i1; ++j2) {
                    boolean flag = false;

                    for (int k2 = 0; !flag && k2 < 4; ++k2) {
                        int l2 = world.g(j1, k1);

                        if (a(EnumCreatureType.CREATURE, world, j1, l2, k1)) {
                            float f = (float) j1 + 0.5F;
                            float f1 = (float) l2;
                            float f2 = (float) k1 + 0.5F;

                            EntityLiving entityliving;

                            try {
                                entityliving = (EntityLiving) biomemeta.a.getConstructor(new Class[] { World.class}).newInstance(new Object[] { world});
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                continue;
                            }

                            entityliving.setPositionRotation((double) f, (double) f1, (double) f2, random.nextFloat() * 360.0F, 0.0F);
                            // CraftBukkit - added a reason for spawning this creature
                            world.addEntity(entityliving, SpawnReason.CHUNK_GEN);
                            a(entityliving, world, f, f1, f2);
                            flag = true;
                        }

                        j1 += random.nextInt(5) - random.nextInt(5);

                        for (k1 += random.nextInt(5) - random.nextInt(5); j1 < i || j1 >= i + k || k1 < j || k1 >= j + k; k1 = i2 + random.nextInt(5) - random.nextInt(5)) {
                            j1 = l1 + random.nextInt(5) - random.nextInt(5);
                        }
                    }
                }
            }
        }
    }
}
