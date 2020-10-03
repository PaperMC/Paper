package net.minecraft.server;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.event.block.BlockFormEvent;
// CraftBukkit end

public class BlockConcretePowder extends BlockFalling {

    private final IBlockData a;

    public BlockConcretePowder(Block block, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.a = block.getBlockData();
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, IBlockData iblockdata1, EntityFallingBlock entityfallingblock) {
        if (canHarden(world, blockposition, iblockdata1)) {
            org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockFormEvent(world, blockposition, this.a, 3); // CraftBukkit
        }

    }

    @Override
    public IBlockData getPlacedState(BlockActionContext blockactioncontext) {
        World world = blockactioncontext.getWorld();
        BlockPosition blockposition = blockactioncontext.getClickPosition();
        IBlockData iblockdata = world.getType(blockposition);

        // CraftBukkit start
        if (!canHarden(world, blockposition, iblockdata)) {
            return super.getPlacedState(blockactioncontext);
        }

        // TODO: An event factory call for methods like this
        CraftBlockState blockState = CraftBlockState.getBlockState(world, blockposition);
        blockState.setData(this.a);

        BlockFormEvent event = new BlockFormEvent(blockState.getBlock(), blockState);
        world.getMinecraftServer().server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            return blockState.getHandle();
        }

        return super.getPlacedState(blockactioncontext);
        // CraftBukkit end
    }

    private static boolean canHarden(IBlockAccess iblockaccess, BlockPosition blockposition, IBlockData iblockdata) {
        return l(iblockdata) || a(iblockaccess, blockposition);
    }

    private static boolean a(IBlockAccess iblockaccess, BlockPosition blockposition) {
        boolean flag = false;
        BlockPosition.MutableBlockPosition blockposition_mutableblockposition = blockposition.i();
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];
            IBlockData iblockdata = iblockaccess.getType(blockposition_mutableblockposition);

            if (enumdirection != EnumDirection.DOWN || l(iblockdata)) {
                blockposition_mutableblockposition.a((BaseBlockPosition) blockposition, enumdirection);
                iblockdata = iblockaccess.getType(blockposition_mutableblockposition);
                if (l(iblockdata) && !iblockdata.d(iblockaccess, blockposition, enumdirection.opposite())) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    private static boolean l(IBlockData iblockdata) {
        return iblockdata.getFluid().a((Tag) TagsFluid.WATER);
    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        // CraftBukkit start
        if (a((IBlockAccess) generatoraccess, blockposition)) {
            // Suppress during worldgen
            if (!(generatoraccess instanceof World)) {
                return this.a;
            }
            CraftBlockState blockState = CraftBlockState.getBlockState(generatoraccess, blockposition);
            blockState.setData(this.a);

            BlockFormEvent event = new BlockFormEvent(blockState.getBlock(), blockState);
            ((World) generatoraccess).getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                return blockState.getHandle();
            }
        }

        return super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
        // CraftBukkit end
    }
}
