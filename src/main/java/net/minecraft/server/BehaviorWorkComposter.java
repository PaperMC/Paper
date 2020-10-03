package net.minecraft.server;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;

public class BehaviorWorkComposter extends BehaviorWork {

    private static final List<Item> b = ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);

    public BehaviorWorkComposter() {}

    @Override
    protected void a(WorldServer worldserver, EntityVillager entityvillager) {
        Optional<GlobalPos> optional = entityvillager.getBehaviorController().getMemory(MemoryModuleType.JOB_SITE);

        if (optional.isPresent()) {
            GlobalPos globalpos = (GlobalPos) optional.get();
            IBlockData iblockdata = worldserver.getType(globalpos.getBlockPosition());

            if (iblockdata.a(Blocks.COMPOSTER)) {
                this.a(entityvillager);
                this.a(worldserver, entityvillager, globalpos, iblockdata);
            }

        }
    }

    private void a(WorldServer worldserver, EntityVillager entityvillager, GlobalPos globalpos, IBlockData iblockdata) {
        BlockPosition blockposition = globalpos.getBlockPosition();

        if ((Integer) iblockdata.get(BlockComposter.a) == 8) {
            iblockdata = BlockComposter.d(iblockdata, (World) worldserver, blockposition);
        }

        int i = 20;
        boolean flag = true;
        int[] aint = new int[BehaviorWorkComposter.b.size()];
        InventorySubcontainer inventorysubcontainer = entityvillager.getInventory();
        int j = inventorysubcontainer.getSize();
        IBlockData iblockdata1 = iblockdata;

        for (int k = j - 1; k >= 0 && i > 0; --k) {
            ItemStack itemstack = inventorysubcontainer.getItem(k);
            int l = BehaviorWorkComposter.b.indexOf(itemstack.getItem());

            if (l != -1) {
                int i1 = itemstack.getCount();
                int j1 = aint[l] + i1;

                aint[l] = j1;
                int k1 = Math.min(Math.min(j1 - 10, i), i1);

                if (k1 > 0) {
                    i -= k1;

                    for (int l1 = 0; l1 < k1; ++l1) {
                        iblockdata1 = BlockComposter.a(iblockdata1, worldserver, itemstack, blockposition);
                        if ((Integer) iblockdata1.get(BlockComposter.a) == 7) {
                            this.a(worldserver, iblockdata, blockposition, iblockdata1);
                            return;
                        }
                    }
                }
            }
        }

        this.a(worldserver, iblockdata, blockposition, iblockdata1);
    }

    private void a(WorldServer worldserver, IBlockData iblockdata, BlockPosition blockposition, IBlockData iblockdata1) {
        worldserver.triggerEffect(1500, blockposition, iblockdata1 != iblockdata ? 1 : 0);
    }

    private void a(EntityVillager entityvillager) {
        InventorySubcontainer inventorysubcontainer = entityvillager.getInventory();

        if (inventorysubcontainer.a(Items.BREAD) <= 36) {
            int i = inventorysubcontainer.a(Items.WHEAT);
            boolean flag = true;
            boolean flag1 = true;
            int j = Math.min(3, i / 3);

            if (j != 0) {
                int k = j * 3;

                inventorysubcontainer.a(Items.WHEAT, k);
                ItemStack itemstack = inventorysubcontainer.a(new ItemStack(Items.BREAD, j));

                if (!itemstack.isEmpty()) {
                    entityvillager.a(itemstack, 0.5F);
                }

            }
        }
    }
}
