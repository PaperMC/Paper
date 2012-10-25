package net.minecraft.server;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.server.MapInitializeEvent;
// CraftBukkit end

public class ItemWorldMap extends ItemWorldMapBase {

    protected ItemWorldMap(int i) {
        super(i);
        this.a(true);
    }

    public WorldMap getSavedMap(ItemStack itemstack, World world) {
        String s = "map_" + itemstack.getData();
        WorldMap worldmap = (WorldMap) world.a(WorldMap.class, s);

        if (worldmap == null && !world.isStatic) {
            itemstack.setData(world.b("map"));
            s = "map_" + itemstack.getData();
            worldmap = new WorldMap(s);
            worldmap.scale = 3;
            int i = 128 * (1 << worldmap.scale);

            worldmap.centerX = Math.round((float) world.getWorldData().c() / (float) i) * i;
            worldmap.centerZ = Math.round((float) (world.getWorldData().e() / i)) * i;
            worldmap.map = (byte) ((WorldServer) world).dimension; // CraftBukkit - fixes Bukkit multiworld maps
            worldmap.c();
            world.a(s, (WorldMapBase) worldmap);

            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }

        return worldmap;
    }

    public void a(World world, Entity entity, WorldMap worldmap) {
        // CraftBukkit
        if (((WorldServer) world).dimension == worldmap.map && entity instanceof EntityHuman) {
            short short1 = 128;
            short short2 = 128;
            int i = 1 << worldmap.scale;
            int j = worldmap.centerX;
            int k = worldmap.centerZ;
            int l = MathHelper.floor(entity.locX - (double) j) / i + short1 / 2;
            int i1 = MathHelper.floor(entity.locZ - (double) k) / i + short2 / 2;
            int j1 = 128 / i;

            if (world.worldProvider.f) {
                j1 /= 2;
            }

            WorldMapHumanTracker worldmaphumantracker = worldmap.a((EntityHuman) entity);

            ++worldmaphumantracker.d;

            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (worldmaphumantracker.d & 15)) {
                    int l1 = 255;
                    int i2 = 0;
                    double d0 = 0.0D;

                    for (int j2 = i1 - j1 - 1; j2 < i1 + j1; ++j2) {
                        if (k1 >= 0 && j2 >= -1 && k1 < short1 && j2 < short2) {
                            int k2 = k1 - l;
                            int l2 = j2 - i1;
                            boolean flag = k2 * k2 + l2 * l2 > (j1 - 2) * (j1 - 2);
                            int i3 = (j / i + k1 - short1 / 2) * i;
                            int j3 = (k / i + j2 - short2 / 2) * i;
                            int[] aint = new int[256];
                            Chunk chunk = world.getChunkAtWorldCoords(i3, j3);

                            if (!chunk.isEmpty()) {
                                int k3 = i3 & 15;
                                int l3 = j3 & 15;
                                int i4 = 0;
                                double d1 = 0.0D;
                                int j4;
                                int k4;
                                int l4;
                                int i5;

                                if (world.worldProvider.f) {
                                    j4 = i3 + j3 * 231871;
                                    j4 = j4 * j4 * 31287121 + j4 * 11;
                                    if ((j4 >> 20 & 1) == 0) {
                                        aint[Block.DIRT.id] += 10;
                                    } else {
                                        aint[Block.STONE.id] += 10;
                                    }

                                    d1 = 100.0D;
                                } else {
                                    for (j4 = 0; j4 < i; ++j4) {
                                        for (k4 = 0; k4 < i; ++k4) {
                                            l4 = chunk.b(j4 + k3, k4 + l3) + 1;
                                            int j5 = 0;

                                            if (l4 > 1) {
                                                boolean flag1;

                                                do {
                                                    flag1 = true;
                                                    j5 = chunk.getTypeId(j4 + k3, l4 - 1, k4 + l3);
                                                    if (j5 == 0) {
                                                        flag1 = false;
                                                    } else if (l4 > 0 && j5 > 0 && Block.byId[j5].material.G == MaterialMapColor.b) {
                                                        flag1 = false;
                                                    }

                                                    if (!flag1) {
                                                        --l4;
                                                        if (l4 <= 0) {
                                                            break;
                                                        }

                                                        j5 = chunk.getTypeId(j4 + k3, l4 - 1, k4 + l3);
                                                    }
                                                } while (l4 > 0 && !flag1);

                                                if (l4 > 0 && j5 != 0 && Block.byId[j5].material.isLiquid()) {
                                                    i5 = l4 - 1;
                                                    boolean flag2 = false;

                                                    int k5;

                                                    do {
                                                        k5 = chunk.getTypeId(j4 + k3, i5--, k4 + l3);
                                                        ++i4;
                                                    } while (i5 > 0 && k5 != 0 && Block.byId[k5].material.isLiquid());
                                                }
                                            }

                                            d1 += (double) l4 / (double) (i * i);
                                            ++aint[j5];
                                        }
                                    }
                                }

                                i4 /= i * i;
                                j4 = 0;
                                k4 = 0;

                                for (l4 = 0; l4 < 256; ++l4) {
                                    if (aint[l4] > j4) {
                                        k4 = l4;
                                        j4 = aint[l4];
                                    }
                                }

                                double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + j2 & 1) - 0.5D) * 0.4D;
                                byte b0 = 1;

                                if (d2 > 0.6D) {
                                    b0 = 2;
                                }

                                if (d2 < -0.6D) {
                                    b0 = 0;
                                }

                                i5 = 0;
                                if (k4 > 0) {
                                    MaterialMapColor materialmapcolor = Block.byId[k4].material.G;

                                    if (materialmapcolor == MaterialMapColor.n) {
                                        d2 = (double) i4 * 0.1D + (double) (k1 + j2 & 1) * 0.2D;
                                        b0 = 1;
                                        if (d2 < 0.5D) {
                                            b0 = 2;
                                        }

                                        if (d2 > 0.9D) {
                                            b0 = 0;
                                        }
                                    }

                                    i5 = materialmapcolor.q;
                                }

                                d0 = d1;
                                if (j2 >= 0 && k2 * k2 + l2 * l2 < j1 * j1 && (!flag || (k1 + j2 & 1) != 0)) {
                                    byte b1 = worldmap.colors[k1 + j2 * short1];
                                    byte b2 = (byte) (i5 * 4 + b0);

                                    if (b1 != b2) {
                                        if (l1 > j2) {
                                            l1 = j2;
                                        }

                                        if (i2 < j2) {
                                            i2 = j2;
                                        }

                                        worldmap.colors[k1 + j2 * short1] = b2;
                                    }
                                }
                            }
                        }
                    }

                    if (l1 <= i2) {
                        worldmap.flagDirty(k1, l1, i2);
                    }
                }
            }
        }
    }

    public void a(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        if (!world.isStatic) {
            WorldMap worldmap = this.getSavedMap(itemstack, world);

            if (entity instanceof EntityHuman) {
                EntityHuman entityhuman = (EntityHuman) entity;

                worldmap.a(entityhuman, itemstack);
            }

            if (flag) {
                this.a(world, entity, worldmap);
            }
        }
    }

    public Packet c(ItemStack itemstack, World world, EntityHuman entityhuman) {
        byte[] abyte = this.getSavedMap(itemstack, world).getUpdatePacket(itemstack, world, entityhuman);

        return abyte == null ? null : new Packet131ItemData((short) Item.MAP.id, (short) itemstack.getData(), abyte);
    }

    public void d(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (itemstack.hasTag() && itemstack.getTag().getBoolean("map_is_scaling")) {
            WorldMap worldmap = Item.MAP.getSavedMap(itemstack, world);

            itemstack.setData(world.b("map"));
            WorldMap worldmap1 = new WorldMap("map_" + itemstack.getData());

            worldmap1.scale = (byte) (worldmap.scale + 1);
            if (worldmap1.scale > 4) {
                worldmap1.scale = 4;
            }

            worldmap1.centerX = worldmap.centerX;
            worldmap1.centerZ = worldmap.centerZ;
            worldmap1.map = worldmap.map;
            worldmap1.c();
            world.a("map_" + itemstack.getData(), (WorldMapBase) worldmap1);

            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap1.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }
    }
}
