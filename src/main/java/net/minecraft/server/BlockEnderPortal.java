package net.minecraft.server;

// CraftBukkit start
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
// CraftBukkit end

public class BlockEnderPortal extends BlockTileEntity {

    protected static final VoxelShape a = Block.a(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    protected BlockEnderPortal(BlockBase.Info blockbase_info) {
        super(blockbase_info);
    }

    @Override
    public TileEntity createTile(IBlockAccess iblockaccess) {
        return new TileEntityEnderPortal();
    }

    @Override
    public VoxelShape b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, VoxelShapeCollision voxelshapecollision) {
        return BlockEnderPortal.a;
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Entity entity) {
        if (world instanceof WorldServer && !entity.isPassenger() && !entity.isVehicle() && entity.canPortal() && VoxelShapes.c(VoxelShapes.a(entity.getBoundingBox().d((double) (-blockposition.getX()), (double) (-blockposition.getY()), (double) (-blockposition.getZ()))), iblockdata.getShape(world, blockposition), OperatorBoolean.AND)) {
            ResourceKey<World> resourcekey = world.getTypeKey() == DimensionManager.THE_END ? World.OVERWORLD : World.THE_END; // CraftBukkit - SPIGOT-6152: send back to main overworld in custom ends
            WorldServer worldserver = ((WorldServer) world).getMinecraftServer().getWorldServer(resourcekey);

            if (worldserver == null) {
                // return; // CraftBukkit - always fire event in case plugins wish to change it
            }

            // CraftBukkit start - Entity in portal
            EntityPortalEnterEvent event = new EntityPortalEnterEvent(entity.getBukkitEntity(), new org.bukkit.Location(world.getWorld(), blockposition.getX(), blockposition.getY(), blockposition.getZ()));
            world.getServer().getPluginManager().callEvent(event);

            if (entity instanceof EntityPlayer) {
                ((EntityPlayer) entity).b(worldserver, PlayerTeleportEvent.TeleportCause.END_PORTAL);
                return;
            }
            // CraftBukkit end
            entity.b(worldserver);
        }

    }

    @Override
    public boolean a(IBlockData iblockdata, FluidType fluidtype) {
        return false;
    }
}
