package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.google.common.collect.LinkedHashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import javax.annotation.Nullable;

public class ItemWorldMap extends ItemWorldMapBase {

    public ItemWorldMap(Item.Info item_info) {
        super(item_info);
    }

    public static ItemStack createFilledMapView(World world, int i, int j, byte b0, boolean flag, boolean flag1) {
        ItemStack itemstack = new ItemStack(Items.FILLED_MAP);

        a(itemstack, world, i, j, b0, flag, flag1, world.getDimensionKey());
        return itemstack;
    }

    @Nullable
    public static WorldMap a(ItemStack itemstack, World world) {
        return world.a(a(d(itemstack)));
    }

    @Nullable
    public static WorldMap getSavedMap(ItemStack itemstack, World world) {
        WorldMap worldmap = a(itemstack, world);

        if (worldmap == null && world instanceof WorldServer) {
            worldmap = a(itemstack, world, world.getWorldData().a(), world.getWorldData().c(), 3, false, false, world.getDimensionKey());
        }

        return worldmap;
    }

    public static int d(ItemStack itemstack) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        return nbttagcompound != null && nbttagcompound.hasKeyOfType("map", 99) ? nbttagcompound.getInt("map") : 0;
    }

    private static WorldMap a(ItemStack itemstack, World world, int i, int j, int k, boolean flag, boolean flag1, ResourceKey<World> resourcekey) {
        int l = world.getWorldMapCount();
        WorldMap worldmap = new WorldMap(a(l));

        worldmap.a(i, j, k, flag, flag1, resourcekey);
        world.a(worldmap);
        itemstack.getOrCreateTag().setInt("map", l);
        return worldmap;
    }

    public static String a(int i) {
        return "map_" + i;
    }

    public void a(World world, Entity entity, WorldMap worldmap) {
        if (world.getDimensionKey() == worldmap.map && entity instanceof EntityHuman) {
            int i = 1 << worldmap.scale;
            int j = worldmap.centerX;
            int k = worldmap.centerZ;
            int l = MathHelper.floor(entity.locX() - (double) j) / i + 64;
            int i1 = MathHelper.floor(entity.locZ() - (double) k) / i + 64;
            int j1 = 128 / i;

            if (world.getDimensionManager().hasCeiling()) {
                j1 /= 2;
            }

            WorldMap.WorldMapHumanTracker worldmap_worldmaphumantracker = worldmap.a((EntityHuman) entity);

            ++worldmap_worldmaphumantracker.b;
            boolean flag = false;

            for (int k1 = l - j1 + 1; k1 < l + j1; ++k1) {
                if ((k1 & 15) == (worldmap_worldmaphumantracker.b & 15) || flag) {
                    flag = false;
                    double d0 = 0.0D;

                    for (int l1 = i1 - j1 - 1; l1 < i1 + j1; ++l1) {
                        if (k1 >= 0 && l1 >= -1 && k1 < 128 && l1 < 128) {
                            int i2 = k1 - l;
                            int j2 = l1 - i1;
                            boolean flag1 = i2 * i2 + j2 * j2 > (j1 - 2) * (j1 - 2);
                            int k2 = (j / i + k1 - 64) * i;
                            int l2 = (k / i + l1 - 64) * i;
                            Multiset<MaterialMapColor> multiset = LinkedHashMultiset.create();
                            Chunk chunk = world.getChunkAtWorldCoords(new BlockPosition(k2, 0, l2));

                            if (!chunk.isEmpty()) {
                                ChunkCoordIntPair chunkcoordintpair = chunk.getPos();
                                int i3 = k2 & 15;
                                int j3 = l2 & 15;
                                int k3 = 0;
                                double d1 = 0.0D;

                                if (world.getDimensionManager().hasCeiling()) {
                                    int l3 = k2 + l2 * 231871;

                                    l3 = l3 * l3 * 31287121 + l3 * 11;
                                    if ((l3 >> 20 & 1) == 0) {
                                        multiset.add(Blocks.DIRT.getBlockData().d(world, BlockPosition.ZERO), 10);
                                    } else {
                                        multiset.add(Blocks.STONE.getBlockData().d(world, BlockPosition.ZERO), 100);
                                    }

                                    d1 = 100.0D;
                                } else {
                                    BlockPosition.MutableBlockPosition blockposition_mutableblockposition = new BlockPosition.MutableBlockPosition();
                                    BlockPosition.MutableBlockPosition blockposition_mutableblockposition1 = new BlockPosition.MutableBlockPosition();

                                    for (int i4 = 0; i4 < i; ++i4) {
                                        for (int j4 = 0; j4 < i; ++j4) {
                                            int k4 = chunk.getHighestBlock(HeightMap.Type.WORLD_SURFACE, i4 + i3, j4 + j3) + 1;
                                            IBlockData iblockdata;

                                            if (k4 > 1) {
                                                do {
                                                    --k4;
                                                    blockposition_mutableblockposition.d(chunkcoordintpair.d() + i4 + i3, k4, chunkcoordintpair.e() + j4 + j3);
                                                    iblockdata = chunk.getType(blockposition_mutableblockposition);
                                                } while (iblockdata.d(world, blockposition_mutableblockposition) == MaterialMapColor.b && k4 > 0);

                                                if (k4 > 0 && !iblockdata.getFluid().isEmpty()) {
                                                    int l4 = k4 - 1;

                                                    blockposition_mutableblockposition1.g(blockposition_mutableblockposition);

                                                    IBlockData iblockdata1;

                                                    do {
                                                        blockposition_mutableblockposition1.p(l4--);
                                                        iblockdata1 = chunk.getType(blockposition_mutableblockposition1);
                                                        ++k3;
                                                    } while (l4 > 0 && !iblockdata1.getFluid().isEmpty());

                                                    iblockdata = this.a(world, iblockdata, (BlockPosition) blockposition_mutableblockposition);
                                                }
                                            } else {
                                                iblockdata = Blocks.BEDROCK.getBlockData();
                                            }

                                            worldmap.a(world, chunkcoordintpair.d() + i4 + i3, chunkcoordintpair.e() + j4 + j3);
                                            d1 += (double) k4 / (double) (i * i);
                                            multiset.add(iblockdata.d(world, blockposition_mutableblockposition));
                                        }
                                    }
                                }

                                k3 /= i * i;
                                double d2 = (d1 - d0) * 4.0D / (double) (i + 4) + ((double) (k1 + l1 & 1) - 0.5D) * 0.4D;
                                byte b0 = 1;

                                if (d2 > 0.6D) {
                                    b0 = 2;
                                }

                                if (d2 < -0.6D) {
                                    b0 = 0;
                                }

                                MaterialMapColor materialmapcolor = (MaterialMapColor) Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MaterialMapColor.b);

                                if (materialmapcolor == MaterialMapColor.n) {
                                    d2 = (double) k3 * 0.1D + (double) (k1 + l1 & 1) * 0.2D;
                                    b0 = 1;
                                    if (d2 < 0.5D) {
                                        b0 = 2;
                                    }

                                    if (d2 > 0.9D) {
                                        b0 = 0;
                                    }
                                }

                                d0 = d1;
                                if (l1 >= 0 && i2 * i2 + j2 * j2 < j1 * j1 && (!flag1 || (k1 + l1 & 1) != 0)) {
                                    byte b1 = worldmap.colors[k1 + l1 * 128];
                                    byte b2 = (byte) (materialmapcolor.aj * 4 + b0);

                                    if (b1 != b2) {
                                        worldmap.colors[k1 + l1 * 128] = b2;
                                        worldmap.flagDirty(k1, l1);
                                        flag = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private IBlockData a(World world, IBlockData iblockdata, BlockPosition blockposition) {
        Fluid fluid = iblockdata.getFluid();

        return !fluid.isEmpty() && !iblockdata.d(world, blockposition, EnumDirection.UP) ? fluid.getBlockData() : iblockdata;
    }

    private static boolean a(BiomeBase[] abiomebase, int i, int j, int k) {
        return abiomebase[j * i + k * i * 128 * i].h() >= 0.0F;
    }

    public static void applySepiaFilter(WorldServer worldserver, ItemStack itemstack) {
        WorldMap worldmap = getSavedMap(itemstack, worldserver);

        if (worldmap != null) {
            if (worldserver.getDimensionKey() == worldmap.map) {
                int i = 1 << worldmap.scale;
                int j = worldmap.centerX;
                int k = worldmap.centerZ;
                BiomeBase[] abiomebase = new BiomeBase[128 * i * 128 * i];

                int l;
                int i1;

                for (l = 0; l < 128 * i; ++l) {
                    for (i1 = 0; i1 < 128 * i; ++i1) {
                        abiomebase[l * 128 * i + i1] = worldserver.getBiome(new BlockPosition((j / i - 64) * i + i1, 0, (k / i - 64) * i + l));
                    }
                }

                for (l = 0; l < 128; ++l) {
                    for (i1 = 0; i1 < 128; ++i1) {
                        if (l > 0 && i1 > 0 && l < 127 && i1 < 127) {
                            BiomeBase biomebase = abiomebase[l * i + i1 * i * 128 * i];
                            int j1 = 8;

                            if (a(abiomebase, i, l - 1, i1 - 1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l - 1, i1 + 1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l - 1, i1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l + 1, i1 - 1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l + 1, i1 + 1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l + 1, i1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l, i1 - 1)) {
                                --j1;
                            }

                            if (a(abiomebase, i, l, i1 + 1)) {
                                --j1;
                            }

                            int k1 = 3;
                            MaterialMapColor materialmapcolor = MaterialMapColor.b;

                            if (biomebase.h() < 0.0F) {
                                materialmapcolor = MaterialMapColor.q;
                                if (j1 > 7 && i1 % 2 == 0) {
                                    k1 = (l + (int) (MathHelper.sin((float) i1 + 0.0F) * 7.0F)) / 8 % 5;
                                    if (k1 == 3) {
                                        k1 = 1;
                                    } else if (k1 == 4) {
                                        k1 = 0;
                                    }
                                } else if (j1 > 7) {
                                    materialmapcolor = MaterialMapColor.b;
                                } else if (j1 > 5) {
                                    k1 = 1;
                                } else if (j1 > 3) {
                                    k1 = 0;
                                } else if (j1 > 1) {
                                    k1 = 0;
                                }
                            } else if (j1 > 0) {
                                materialmapcolor = MaterialMapColor.B;
                                if (j1 > 3) {
                                    k1 = 1;
                                } else {
                                    k1 = 3;
                                }
                            }

                            if (materialmapcolor != MaterialMapColor.b) {
                                worldmap.colors[l + i1 * 128] = (byte) (materialmapcolor.aj * 4 + k1);
                                worldmap.flagDirty(l, i1);
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void a(ItemStack itemstack, World world, Entity entity, int i, boolean flag) {
        if (!world.isClientSide) {
            WorldMap worldmap = getSavedMap(itemstack, world);

            if (worldmap != null) {
                if (entity instanceof EntityHuman) {
                    EntityHuman entityhuman = (EntityHuman) entity;

                    worldmap.a(entityhuman, itemstack);
                }

                if (!worldmap.locked && (flag || entity instanceof EntityHuman && ((EntityHuman) entity).getItemInOffHand() == itemstack)) {
                    this.a(world, entity, worldmap);
                }

            }
        }
    }

    @Nullable
    @Override
    public Packet<?> a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        return getSavedMap(itemstack, world).a(itemstack, world, entityhuman);
    }

    @Override
    public void b(ItemStack itemstack, World world, EntityHuman entityhuman) {
        NBTTagCompound nbttagcompound = itemstack.getTag();

        if (nbttagcompound != null && nbttagcompound.hasKeyOfType("map_scale_direction", 99)) {
            a(itemstack, world, nbttagcompound.getInt("map_scale_direction"));
            nbttagcompound.remove("map_scale_direction");
        } else if (nbttagcompound != null && nbttagcompound.hasKeyOfType("map_to_lock", 1) && nbttagcompound.getBoolean("map_to_lock")) {
            a(world, itemstack);
            nbttagcompound.remove("map_to_lock");
        }

    }

    protected static void a(ItemStack itemstack, World world, int i) {
        WorldMap worldmap = getSavedMap(itemstack, world);

        if (worldmap != null) {
            a(itemstack, world, worldmap.centerX, worldmap.centerZ, MathHelper.clamp(worldmap.scale + i, 0, 4), worldmap.track, worldmap.unlimitedTracking, worldmap.map);
        }

    }

    public static void a(World world, ItemStack itemstack) {
        WorldMap worldmap = getSavedMap(itemstack, world);

        if (worldmap != null) {
            WorldMap worldmap1 = a(itemstack, world, 0, 0, worldmap.scale, worldmap.track, worldmap.unlimitedTracking, worldmap.map);

            worldmap1.a(worldmap);
        }

    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        IBlockData iblockdata = itemactioncontext.getWorld().getType(itemactioncontext.getClickPosition());

        if (iblockdata.a((Tag) TagsBlock.BANNERS)) {
            if (!itemactioncontext.getWorld().isClientSide) {
                WorldMap worldmap = getSavedMap(itemactioncontext.getItemStack(), itemactioncontext.getWorld());

                worldmap.a((GeneratorAccess) itemactioncontext.getWorld(), itemactioncontext.getClickPosition());
            }

            return EnumInteractionResult.a(itemactioncontext.getWorld().isClientSide);
        } else {
            return super.a(itemactioncontext);
        }
    }
}
