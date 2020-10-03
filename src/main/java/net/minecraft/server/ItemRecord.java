package net.minecraft.server;

import com.google.common.collect.Maps;
import java.util.Map;

public class ItemRecord extends Item {

    private static final Map<SoundEffect, ItemRecord> a = Maps.newHashMap();
    private final int b;
    private final SoundEffect c;

    protected ItemRecord(int i, SoundEffect soundeffect, Item.Info item_info) {
        super(item_info);
        this.b = i;
        this.c = soundeffect;
        ItemRecord.a.put(this.c, this);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        IBlockData iblockdata = world.getType(blockposition);

        if (iblockdata.a(Blocks.JUKEBOX) && !(Boolean) iblockdata.get(BlockJukeBox.HAS_RECORD)) {
            ItemStack itemstack = itemactioncontext.getItemStack();

            if (!world.isClientSide) {
                if (true) return EnumInteractionResult.SUCCESS; // CraftBukkit - handled in ItemStack
                ((BlockJukeBox) Blocks.JUKEBOX).a((GeneratorAccess) world, blockposition, iblockdata, itemstack);
                world.a((EntityHuman) null, 1010, blockposition, Item.getId(this));
                itemstack.subtract(1);
                EntityHuman entityhuman = itemactioncontext.getEntity();

                if (entityhuman != null) {
                    entityhuman.a(StatisticList.PLAY_RECORD);
                }
            }

            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return EnumInteractionResult.PASS;
        }
    }

    public int f() {
        return this.b;
    }
}
