package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class BlockCoral extends Block {

    private final Block a;

    public BlockCoral(Block block, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.a = block;
    }

    @Override
    public void tickAlways(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (!this.a((IBlockAccess) worldserver, blockposition)) {
            worldserver.setTypeAndData(blockposition, this.a.getBlockData(), 2);
        }

    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        if (!this.a((IBlockAccess) generatoraccess, blockposition)) {
            generatoraccess.getBlockTickList().a(blockposition, this, 60 + generatoraccess.getRandom().nextInt(40));
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    protected boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];
            Fluid fluid = iblockaccess.getFluid(blockposition.shift(enumdirection));

            if (fluid.a((Tag) TagsFluid.WATER)) {
                return true;
            }
        }

        return false;
    }

    @Nullable
    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        if (!this.a((IBlockAccess) blockactioncontext.getWorld(), blockactioncontext.getClickPosition())) {
            blockactioncontext.getWorld().getBlockTickList().a(blockactioncontext.getClickPosition(), this, 60 + blockactioncontext.getWorld().getRandom().nextInt(40));
        }

        return this.getBlockData();
    }
}
