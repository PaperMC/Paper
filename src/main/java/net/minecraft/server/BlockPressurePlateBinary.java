package net.minecraft.server;

import java.util.Iterator;
import java.util.List;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

public class BlockPressurePlateBinary extends BlockPressurePlateAbstract {

    public static final BlockStateBoolean POWERED = BlockProperties.w;
    private final BlockPressurePlateBinary.EnumMobType e;

    protected BlockPressurePlateBinary(BlockPressurePlateBinary.EnumMobType blockpressureplatebinary_enummobtype, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPressurePlateBinary.POWERED, false));
        this.e = blockpressureplatebinary_enummobtype;
    }

    @Override
    protected int getPower(IBlockData iblockdata) {
        return (Boolean) iblockdata.get(BlockPressurePlateBinary.POWERED) ? 15 : 0;
    }

    @Override
    protected IBlockData a(IBlockData iblockdata, int i) {
        return (IBlockData) iblockdata.set(BlockPressurePlateBinary.POWERED, i > 0);
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        if (this.material != Material.WOOD && this.material != Material.NETHER_WOOD) {
            generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_STONE_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
        } else {
            generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.8F);
        }

    }

    @Override
    protected void b(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        if (this.material != Material.WOOD && this.material != Material.NETHER_WOOD) {
            generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_STONE_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
        } else {
            generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_WOODEN_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.7F);
        }

    }

    @Override
    protected int b(World world, BlockPosition blockposition) {
        AxisAlignedBB axisalignedbb = BlockPressurePlateBinary.c.a(blockposition);
        List list;

        switch (this.e) {
            case EVERYTHING:
                list = world.getEntities((Entity) null, axisalignedbb);
                break;
            case MOBS:
                list = world.a(EntityLiving.class, axisalignedbb);
                break;
            default:
                return 0;
        }

        if (!list.isEmpty()) {
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                Entity entity = (Entity) iterator.next();

                // CraftBukkit start - Call interact event when turning on a pressure plate
                if (this.getPower(world.getType(blockposition)) == 0) {
                    org.bukkit.World bworld = world.getWorld();
                    org.bukkit.plugin.PluginManager manager = world.getServer().getPluginManager();
                    org.bukkit.event.Cancellable cancellable;

                    if (entity instanceof EntityHuman) {
                        cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
                    } else {
                        cancellable = new EntityInteractEvent(entity.getBukkitEntity(), bworld.getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                        manager.callEvent((EntityInteractEvent) cancellable);
                    }

                    // We only want to block turning the plate on if all events are cancelled
                    if (cancellable.isCancelled()) {
                        continue;
                    }
                }
                // CraftBukkit end

                if (!entity.isIgnoreBlockTrigger()) {
                    return 15;
                }
            }
        }

        return 0;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPressurePlateBinary.POWERED);
    }

    public static enum EnumMobType {

        EVERYTHING, MOBS;

        private EnumMobType() {}
    }
}
