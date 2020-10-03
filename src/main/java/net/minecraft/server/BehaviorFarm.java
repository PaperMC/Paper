package net.minecraft.server;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.List;
import javax.annotation.Nullable;

public class BehaviorFarm extends Behavior<EntityVillager> {

    @Nullable
    private BlockPosition farmBlock;
    private long c;
    private int d;
    private final List<BlockPosition> e = Lists.newArrayList();

    public BehaviorFarm() {
        super(ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.SECONDARY_JOB_SITE, MemoryStatus.VALUE_PRESENT));
    }

    protected boolean a(WorldServer worldserver, EntityVillager entityvillager) {
        if (!worldserver.getGameRules().getBoolean(GameRules.MOB_GRIEFING)) {
            return false;
        } else if (entityvillager.getVillagerData().getProfession() != VillagerProfession.FARMER) {
            return false;
        } else {
            BlockPosition.MutableBlockPosition blockposition_mutableblockposition = entityvillager.getChunkCoordinates().i();

            this.e.clear();

            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    for (int k = -1; k <= 1; ++k) {
                        blockposition_mutableblockposition.c(entityvillager.locX() + (double) i, entityvillager.locY() + (double) j, entityvillager.locZ() + (double) k);
                        if (this.a((BlockPosition) blockposition_mutableblockposition, worldserver)) {
                            this.e.add(new BlockPosition(blockposition_mutableblockposition));
                        }
                    }
                }
            }

            this.farmBlock = this.a(worldserver);
            return this.farmBlock != null;
        }
    }

    @Nullable
    private BlockPosition a(WorldServer worldserver) {
        return this.e.isEmpty() ? null : (BlockPosition) this.e.get(worldserver.getRandom().nextInt(this.e.size()));
    }

    private boolean a(BlockPosition blockposition, WorldServer worldserver) {
        IBlockData iblockdata = worldserver.getType(blockposition);
        Block block = iblockdata.getBlock();
        Block block1 = worldserver.getType(blockposition.down()).getBlock();

        return block instanceof BlockCrops && ((BlockCrops) block).isRipe(iblockdata) || iblockdata.isAir() && block1 instanceof BlockSoil;
    }

    protected void a(WorldServer worldserver, EntityVillager entityvillager, long i) {
        if (i > this.c && this.farmBlock != null) {
            entityvillager.getBehaviorController().setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorTarget(this.farmBlock))); // CraftBukkit - decompile error
            entityvillager.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, (new MemoryTarget(new BehaviorTarget(this.farmBlock), 0.5F, 1))); // CraftBukkit - decompile error
        }

    }

    protected void c(WorldServer worldserver, EntityVillager entityvillager, long i) {
        entityvillager.getBehaviorController().removeMemory(MemoryModuleType.LOOK_TARGET);
        entityvillager.getBehaviorController().removeMemory(MemoryModuleType.WALK_TARGET);
        this.d = 0;
        this.c = i + 40L;
    }

    protected void d(WorldServer worldserver, EntityVillager entityvillager, long i) {
        if (this.farmBlock == null || this.farmBlock.a((IPosition) entityvillager.getPositionVector(), 1.0D)) {
            if (this.farmBlock != null && i > this.c) {
                IBlockData iblockdata = worldserver.getType(this.farmBlock);
                Block block = iblockdata.getBlock();
                Block block1 = worldserver.getType(this.farmBlock.down()).getBlock();

                if (block instanceof BlockCrops && ((BlockCrops) block).isRipe(iblockdata)) {
                    // CraftBukkit start
                    if (!org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entityvillager, this.farmBlock, Blocks.AIR.getBlockData()).isCancelled()) {
                        worldserver.a(this.farmBlock, true, entityvillager);
                    }
                    // CraftBukkit end
                }

                if (iblockdata.isAir() && block1 instanceof BlockSoil && entityvillager.canPlant()) {
                    InventorySubcontainer inventorysubcontainer = entityvillager.getInventory();

                    for (int j = 0; j < inventorysubcontainer.getSize(); ++j) {
                        ItemStack itemstack = inventorysubcontainer.getItem(j);
                        boolean flag = false;

                        if (!itemstack.isEmpty()) {
                            // CraftBukkit start
                            Block planted = null;
                            if (itemstack.getItem() == Items.WHEAT_SEEDS) {
                                planted = Blocks.WHEAT;
                                flag = true;
                            } else if (itemstack.getItem() == Items.POTATO) {
                                planted = Blocks.POTATOES;
                                flag = true;
                            } else if (itemstack.getItem() == Items.CARROT) {
                                planted = Blocks.CARROTS;
                                flag = true;
                            } else if (itemstack.getItem() == Items.BEETROOT_SEEDS) {
                                planted = Blocks.BEETROOTS;
                                flag = true;
                            }

                            if (planted != null && !org.bukkit.craftbukkit.event.CraftEventFactory.callEntityChangeBlockEvent(entityvillager, this.farmBlock, planted.getBlockData()).isCancelled()) {
                                worldserver.setTypeAndData(this.farmBlock, planted.getBlockData(), 3);
                            } else {
                                flag = false;
                            }
                            // CraftBukkit end
                        }

                        if (flag) {
                            worldserver.playSound((EntityHuman) null, (double) this.farmBlock.getX(), (double) this.farmBlock.getY(), (double) this.farmBlock.getZ(), SoundEffects.ITEM_CROP_PLANT, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            itemstack.subtract(1);
                            if (itemstack.isEmpty()) {
                                inventorysubcontainer.setItem(j, ItemStack.b);
                            }
                            break;
                        }
                    }
                }

                if (block instanceof BlockCrops && !((BlockCrops) block).isRipe(iblockdata)) {
                    this.e.remove(this.farmBlock);
                    this.farmBlock = this.a(worldserver);
                    if (this.farmBlock != null) {
                        this.c = i + 20L;
                        entityvillager.getBehaviorController().setMemory(MemoryModuleType.WALK_TARGET, (new MemoryTarget(new BehaviorTarget(this.farmBlock), 0.5F, 1))); // CraftBukkit - decompile error
                        entityvillager.getBehaviorController().setMemory(MemoryModuleType.LOOK_TARGET, (new BehaviorTarget(this.farmBlock))); // CraftBukkit - decompile error
                    }
                }
            }

            ++this.d;
        }
    }

    protected boolean b(WorldServer worldserver, EntityVillager entityvillager, long i) {
        return this.d < 200;
    }
}
