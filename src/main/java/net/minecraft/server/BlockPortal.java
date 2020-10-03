package net.minecraft.server;

import java.util.Random;

// CraftBukkit start
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.world.PortalCreateEvent;
// CraftBukkit end

public class BlockPortal extends Block {

    public static final BlockStateEnum<EnumDirection.EnumAxis> AXIS = BlockProperties.E;
    protected static final VoxelShape b = Block.a(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape c = Block.a(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public BlockPortal(BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPortal.AXIS, EnumDirection.EnumAxis.X));
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        switch ((EnumDirection.EnumAxis) iblockdata.get(BlockPortal.AXIS)) {
            case Z:
                return BlockPortal.c;
            case X:
            default:
                return BlockPortal.b;
        }
    }

    @Override
    public void tick(IBlockData iblockdata, WorldServer worldserver, BlockPosition blockposition, Random random) {
        if (worldserver.getDimensionManager().isNatural() && worldserver.getGameRules().getBoolean(GameRules.DO_MOB_SPAWNING) && random.nextInt(2000) < worldserver.getDifficulty().a()) {
            while (worldserver.getType(blockposition).a((Block) this)) {
                blockposition = blockposition.down();
            }

            if (worldserver.getType(blockposition).a((IBlockAccess) worldserver, blockposition, EntityTypes.ZOMBIFIED_PIGLIN)) {
                // CraftBukkit - set spawn reason to NETHER_PORTAL
                Entity entity = EntityTypes.ZOMBIFIED_PIGLIN.spawnCreature(worldserver, (NBTTagCompound) null, (IChatBaseComponent) null, (EntityHuman) null, blockposition.up(), EnumMobSpawn.STRUCTURE, false, false, org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NETHER_PORTAL);

                if (entity != null) {
                    entity.resetPortalCooldown();
                }
            }
        }

    }

    @Override
    public IBlockData updateState(IBlockData iblockdata, EnumDirection enumdirection, IBlockData iblockdata1, GeneratorAccess generatoraccess, BlockPosition blockposition, BlockPosition blockposition1) {
        EnumDirection.EnumAxis enumdirection_enumaxis = enumdirection.n();
        EnumDirection.EnumAxis enumdirection_enumaxis1 = (EnumDirection.EnumAxis) iblockdata.get(BlockPortal.AXIS);
        boolean flag = enumdirection_enumaxis1 != enumdirection_enumaxis && enumdirection_enumaxis.d();

        return !flag && !iblockdata1.a((Block) this) && !(new BlockPortalShape(generatoraccess, blockposition, enumdirection_enumaxis1)).c() ? Blocks.AIR.getBlockData() : super.updateState(iblockdata, enumdirection, iblockdata1, generatoraccess, blockposition, blockposition1);
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (!entity.isPassenger() && !entity.isVehicle() && entity.canPortal()) {
            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            world.getServer().getPluginManager().callEvent(event);
            // CraftBukkit end
            entity.d(blockposition);
        }

    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        switch (enumblockrotation) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch ((EnumDirection.EnumAxis) iblockdata.get(BlockPortal.AXIS)) {
                    case Z:
                        return (IBlockData) iblockdata.set(BlockPortal.AXIS, EnumDirection.EnumAxis.X);
                    case X:
                        return (IBlockData) iblockdata.set(BlockPortal.AXIS, EnumDirection.EnumAxis.Z);
                    default:
                        return iblockdata;
                }
            default:
                return iblockdata;
        }
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPortal.AXIS);
    }
}
