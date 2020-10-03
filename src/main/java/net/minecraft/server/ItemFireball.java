package net.minecraft.server;

public class ItemFireball extends Item {

    public ItemFireball(Item.Info item_info) {
        super(item_info);
    }

    @Override
    public EnumInteractionResult a(ItemActionContext itemactioncontext) {
        World world = itemactioncontext.getWorld();
        BlockPosition blockposition = itemactioncontext.getClickPosition();
        IBlockData iblockdata = world.getType(blockposition);
        boolean flag = false;

        if (BlockCampfire.h(iblockdata)) {
            this.a(world, blockposition);
            world.setTypeUpdate(blockposition, (IBlockData) iblockdata.set(BlockCampfire.b, true));
            flag = true;
        } else {
            blockposition = blockposition.shift(itemactioncontext.getClickedFace());
            if (BlockFireAbstract.a(world, blockposition, itemactioncontext.f())) {
                this.a(world, blockposition);
                world.setTypeUpdate(blockposition, BlockFireAbstract.a((IBlockAccess) world, blockposition));
                flag = true;
            }
        }

        if (flag) {
            itemactioncontext.getItemStack().subtract(1);
            return EnumInteractionResult.a(world.isClientSide);
        } else {
            return EnumInteractionResult.FAIL;
        }
    }

    private void a(World world, BlockPosition blockposition) {
        world.playSound((EntityHuman) null, blockposition, SoundEffects.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0F, (ItemFireball.RANDOM.nextFloat() - ItemFireball.RANDOM.nextFloat()) * 0.2F + 1.0F);
    }
}
