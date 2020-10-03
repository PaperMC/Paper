package net.minecraft.server;

import org.bukkit.event.entity.EntityInteractEvent; // CraftBukkit

public class BlockPressurePlateWeighted extends BlockPressurePlateAbstract {

    public static final BlockStateInteger POWER = BlockProperties.az;
    private final int weight;

    protected BlockPressurePlateWeighted(int i, BlockBase.Info blockbase_info) {
        super(blockbase_info);
        this.j((IBlockData) ((IBlockData) this.blockStateList.getBlockData()).set(BlockPressurePlateWeighted.POWER, 0));
        this.weight = i;
    }

    @Override
    protected int b(World world, BlockPosition blockposition) {
        // CraftBukkit start
        // int i = Math.min(world.a(Entity.class, BlockPressurePlateWeighted.c.a(blockposition)).size(), this.weight);
        int i = 0;
        java.util.Iterator iterator = world.a(Entity.class, BlockPressurePlateWeighted.c.a(blockposition)).iterator();

        while (iterator.hasNext()) {
            Entity entity = (Entity) iterator.next();

            org.bukkit.event.Cancellable cancellable;

            if (entity instanceof EntityHuman) {
                cancellable = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent((EntityHuman) entity, org.bukkit.event.block.Action.PHYSICAL, blockposition, null, null, null);
            } else {
                cancellable = new EntityInteractEvent(entity.getBukkitEntity(), world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ()));
                world.getServer().getPluginManager().callEvent((EntityInteractEvent) cancellable);
            }

            // We only want to block turning the plate on if all events are cancelled
            if (!cancellable.isCancelled()) {
                i++;
            }
        }

        i = Math.min(i, this.weight);
        // CraftBukkit end

        if (i > 0) {
            float f = (float) Math.min(this.weight, i) / (float) this.weight;

            return MathHelper.f(f * 15.0F);
        } else {
            return 0;
        }
    }

    @Override
    protected void a(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_METAL_PRESSURE_PLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.90000004F);
    }

    @Override
    protected void b(GeneratorAccess generatoraccess, BlockPosition blockposition) {
        generatoraccess.playSound((EntityHuman) null, blockposition, SoundEffects.BLOCK_METAL_PRESSURE_PLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.75F);
    }

    @Override
    protected int getPower(IBlockData iblockdata) {
        return (Integer) iblockdata.get(BlockPressurePlateWeighted.POWER);
    }

    @Override
    protected IBlockData a(IBlockData iblockdata, int i) {
        return (IBlockData) iblockdata.set(BlockPressurePlateWeighted.POWER, i);
    }

    @Override
    protected int c() {
        return 10;
    }

    @Override
    protected void a(BlockStateList.a<Block, IBlockData> blockstatelist_a) {
        blockstatelist_a.a(BlockPressurePlateWeighted.POWER);
    }
}
