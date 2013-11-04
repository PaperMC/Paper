package net.minecraft.server;

import net.minecraft.util.com.google.common.collect.HashMultiset;
import net.minecraft.util.com.google.common.collect.Iterables;
import net.minecraft.util.com.google.common.collect.Multisets;

// CraftBukkit start
import org.bukkit.Bukkit;
import org.bukkit.event.server.MapInitializeEvent;
// CraftBukkit end

public class ItemWorldMap extends ItemWorldMapBase {

    protected ItemWorldMap() {
        this.a(true);
    }

    public WorldMap getSavedMap(ItemStack itemstack, World world) {
        World worldMain = world.getServer().getServer().worlds.get(0); // CraftBukkit - store reference to primary world
        String s = "map_" + itemstack.getData();
        WorldMap worldmap = (WorldMap) worldMain.a(WorldMap.class, s); // CraftBukkit - use primary world for maps

        if (worldmap == null && !world.isStatic) {
            itemstack.setData(worldMain.b("map")); // CraftBukkit - use primary world for maps
            s = "map_" + itemstack.getData();
            worldmap = new WorldMap(s);
            worldmap.scale = 3;
            int i = 128 * (1 << worldmap.scale);

            worldmap.centerX = Math.round((float) world.getWorldData().c() / (float) i) * i;
            worldmap.centerZ = Math.round((float) (world.getWorldData().e() / i)) * i;
            worldmap.map = (byte) ((WorldServer) world).dimension; // CraftBukkit - fixes Bukkit multiworld maps
            worldmap.c();
            worldMain.a(s, (PersistentBase) worldmap); // CraftBukkit - use primary world for maps

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
            int i = 1 << worldmap.scale;
            int j = worldmap.centerX;
            int k = worldmap.centerZ;
            int l = MathHelper.floor(entity.locX - (double) j) / i + 64;
            int i1 = MathHelper.floor(entity.locZ - (double) k) / i + 64;
            int j1 = 128 / i;

            if (world.worldProvider.g) {
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
                        if (k1 >= 0 && j2 >= -1 && k1 < 128 && j2 < 128) {
                            int k2 = k1 - l;
                            int l2 = j2 - i1;
                            boolean flag = k2 * k2 + l2 * l2 > (j1 - 2) * (j1 - 2);
                            int i3 = (j / i + k1 - 64) * i;
                            int j3 = (k / i + j2 - 64) * i;
                            HashMultiset hashmultiset = HashMultiset.create();
                            Chunk chunk = world.getChunkAtWorldCoords(i3, j3);

                            if (!chunk.isEmpty()) {
                                int k3 = i3 & 15;
                                int l3 = j3 & 15;
                                int i4 = 0;
                                double d1 = 0.0D;
                                int j4;

                                if (world.worldProvider.g) {
                                    j4 = i3 + j3 * 231871;
                                    j4 = j4 * j4 * 31287121 + j4 * 11;
                                    if ((j4 >> 20 & 1) == 0) {
                                        hashmultiset.add(Blocks.DIRT.f(0), 10);
                                    } else {
                                        hashmultiset.add(Blocks.STONE.f(0), 100);
                                    }

                                    d1 = 100.0D;
                                } else {
                                    for (j4 = 0; j4 < i; ++j4) {
                                        for (int k4 = 0; k4 < i; ++k4) {
                                            int l4 = chunk.b(j4 + k3, k4 + l3) + 1;
                                            Block block = Blocks.AIR;
                                            int i5 = 0;

                                            if (l4 > 1) {
                                                do {
                                                    --l4;
                                                    block = chunk.getType(j4 + k3, l4, k4 + l3);
                                                    i5 = chunk.getData(j4 + k3, l4, k4 + l3);
                                                } while (block.f(i5) == MaterialMapColor.b && l4 > 0);

                                                if (l4 > 0 && block.getMaterial().isLiquid()) {
                                                    int j5 = l4 - 1;

                                                    Block block1;

                                                    do {
                                                        block1 = chunk.getType(j4 + k3, j5--, k4 + l3);
                                                        ++i4;
                                                    } while (j5 > 0 && block1.getMaterial().isLiquid());
                                                }
                                            }

                                            d1 += (double) l4 / (double) (i * i);
                                            hashmultiset.add(block.f(i5));
                                        }
                                    }
                                }

                                i4 /= i * i;
                                double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + j2 & 1) - 0.5D) * 0.4D;
                                byte b0 = 1;

                                if (d2 > 0.6D) {
                                    b0 = 2;
                                }

                                if (d2 < -0.6D) {
                                    b0 = 0;
                                }

                                MaterialMapColor materialmapcolor = (MaterialMapColor) Iterables.getFirst(Multisets.copyHighestCountFirst(hashmultiset), MaterialMapColor.b);

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

                                d0 = d1;
                                if (j2 >= 0 && k2 * k2 + l2 * l2 < j1 * j1 && (!flag || (k1 + j2 & 1) != 0)) {
                                    byte b1 = worldmap.colors[k1 + j2 * 128];
                                    byte b2 = (byte) (materialmapcolor.M * 4 + b0);

                                    if (b1 != b2) {
                                        if (l1 > j2) {
                                            l1 = j2;
                                        }

                                        if (i2 < j2) {
                                            i2 = j2;
                                        }

                                        worldmap.colors[k1 + j2 * 128] = b2;
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

        return abyte == null ? null : new PacketPlayOutMap(itemstack.getData(), abyte);
    }

    public void d(ItemStack itemstack, World world, EntityHuman entityhuman) {
        if (itemstack.hasTag() && itemstack.getTag().getBoolean("map_is_scaling")) {
            WorldMap worldmap = Items.MAP.getSavedMap(itemstack, world);

            world = world.getServer().getServer().worlds.get(0); // CraftBukkit - use primary world for maps

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
            world.a("map_" + itemstack.getData(), (PersistentBase) worldmap1);

            // CraftBukkit start
            MapInitializeEvent event = new MapInitializeEvent(worldmap1.mapView);
            Bukkit.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
        }
    }
}
