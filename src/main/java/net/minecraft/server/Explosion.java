package net.minecraft.server;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import javax.annotation.Nullable;

public class Explosion {

    private static final ExplosionDamageCalculator a = new ExplosionDamageCalculator();
    private final boolean b;
    private final Explosion.Effect c;
    private final Random d = new Random();
    private final World world;
    private final double posX;
    private final double posY;
    private final double posZ;
    @Nullable
    public final Entity source;
    private final float size;
    private final DamageSource k;
    private final ExplosionDamageCalculator l;
    private final List<BlockPosition> blocks = Lists.newArrayList();
    private final Map<EntityHuman, Vec3D> n = Maps.newHashMap();

    public Explosion(World world, @Nullable Entity entity, @Nullable DamageSource damagesource, @Nullable ExplosionDamageCalculator explosiondamagecalculator, double d0, double d1, double d2, float f, boolean flag, Explosion.Effect explosion_effect) {
        this.world = world;
        this.source = entity;
        this.size = f;
        this.posX = d0;
        this.posY = d1;
        this.posZ = d2;
        this.b = flag;
        this.c = explosion_effect;
        this.k = damagesource == null ? DamageSource.explosion(this) : damagesource;
        this.l = explosiondamagecalculator == null ? this.a(entity) : explosiondamagecalculator;
    }

    private ExplosionDamageCalculator a(@Nullable Entity entity) {
        return (ExplosionDamageCalculator) (entity == null ? Explosion.a : new ExplosionDamageCalculatorEntity(entity));
    }

    public static float a(Vec3D vec3d, Entity entity) {
        AxisAlignedBB axisalignedbb = entity.getBoundingBox();
        double d0 = 1.0D / ((axisalignedbb.maxX - axisalignedbb.minX) * 2.0D + 1.0D);
        double d1 = 1.0D / ((axisalignedbb.maxY - axisalignedbb.minY) * 2.0D + 1.0D);
        double d2 = 1.0D / ((axisalignedbb.maxZ - axisalignedbb.minZ) * 2.0D + 1.0D);
        double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
        double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;

        if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
            int i = 0;
            int j = 0;

            for (float f = 0.0F; f <= 1.0F; f = (float) ((double) f + d0)) {
                for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float) ((double) f1 + d1)) {
                    for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float) ((double) f2 + d2)) {
                        double d5 = MathHelper.d((double) f, axisalignedbb.minX, axisalignedbb.maxX);
                        double d6 = MathHelper.d((double) f1, axisalignedbb.minY, axisalignedbb.maxY);
                        double d7 = MathHelper.d((double) f2, axisalignedbb.minZ, axisalignedbb.maxZ);
                        Vec3D vec3d1 = new Vec3D(d5 + d3, d6, d7 + d4);

                        if (entity.world.rayTrace(new RayTrace(vec3d1, vec3d, RayTrace.BlockCollisionOption.COLLIDER, RayTrace.FluidCollisionOption.NONE, entity)).getType() == MovingObjectPosition.EnumMovingObjectType.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float) i / (float) j;
        } else {
            return 0.0F;
        }
    }

    public void a() {
        Set<BlockPosition> set = Sets.newHashSet();
        boolean flag = true;

        int i;
        int j;

        for (int k = 0; k < 16; ++k) {
            for (i = 0; i < 16; ++i) {
                for (j = 0; j < 16; ++j) {
                    if (k == 0 || k == 15 || i == 0 || i == 15 || j == 0 || j == 15) {
                        double d0 = (double) ((float) k / 15.0F * 2.0F - 1.0F);
                        double d1 = (double) ((float) i / 15.0F * 2.0F - 1.0F);
                        double d2 = (double) ((float) j / 15.0F * 2.0F - 1.0F);
                        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);

                        d0 /= d3;
                        d1 /= d3;
                        d2 /= d3;
                        float f = this.size * (0.7F + this.world.random.nextFloat() * 0.6F);
                        double d4 = this.posX;
                        double d5 = this.posY;
                        double d6 = this.posZ;

                        for (float f1 = 0.3F; f > 0.0F; f -= 0.22500001F) {
                            BlockPosition blockposition = new BlockPosition(d4, d5, d6);
                            IBlockData iblockdata = this.world.getType(blockposition);
                            Fluid fluid = this.world.getFluid(blockposition);
                            Optional<Float> optional = this.l.a(this, this.world, blockposition, iblockdata, fluid);

                            if (optional.isPresent()) {
                                f -= ((Float) optional.get() + 0.3F) * 0.3F;
                            }

                            if (f > 0.0F && this.l.a(this, this.world, blockposition, iblockdata, f)) {
                                set.add(blockposition);
                            }

                            d4 += d0 * 0.30000001192092896D;
                            d5 += d1 * 0.30000001192092896D;
                            d6 += d2 * 0.30000001192092896D;
                        }
                    }
                }
            }
        }

        this.blocks.addAll(set);
        float f2 = this.size * 2.0F;

        i = MathHelper.floor(this.posX - (double) f2 - 1.0D);
        j = MathHelper.floor(this.posX + (double) f2 + 1.0D);
        int l = MathHelper.floor(this.posY - (double) f2 - 1.0D);
        int i1 = MathHelper.floor(this.posY + (double) f2 + 1.0D);
        int j1 = MathHelper.floor(this.posZ - (double) f2 - 1.0D);
        int k1 = MathHelper.floor(this.posZ + (double) f2 + 1.0D);
        List<Entity> list = this.world.getEntities(this.source, new AxisAlignedBB((double) i, (double) l, (double) j1, (double) j, (double) i1, (double) k1));
        Vec3D vec3d = new Vec3D(this.posX, this.posY, this.posZ);

        for (int l1 = 0; l1 < list.size(); ++l1) {
            Entity entity = (Entity) list.get(l1);

            if (!entity.ch()) {
                double d7 = (double) (MathHelper.sqrt(entity.e(vec3d)) / f2);

                if (d7 <= 1.0D) {
                    double d8 = entity.locX() - this.posX;
                    double d9 = (entity instanceof EntityTNTPrimed ? entity.locY() : entity.getHeadY()) - this.posY;
                    double d10 = entity.locZ() - this.posZ;
                    double d11 = (double) MathHelper.sqrt(d8 * d8 + d9 * d9 + d10 * d10);

                    if (d11 != 0.0D) {
                        d8 /= d11;
                        d9 /= d11;
                        d10 /= d11;
                        double d12 = (double) a(vec3d, entity);
                        double d13 = (1.0D - d7) * d12;

                        entity.damageEntity(this.b(), (float) ((int) ((d13 * d13 + d13) / 2.0D * 7.0D * (double) f2 + 1.0D)));
                        double d14 = d13;

                        if (entity instanceof EntityLiving) {
                            d14 = EnchantmentProtection.a((EntityLiving) entity, d13);
                        }

                        entity.setMot(entity.getMot().add(d8 * d14, d9 * d14, d10 * d14));
                        if (entity instanceof EntityHuman) {
                            EntityHuman entityhuman = (EntityHuman) entity;

                            if (!entityhuman.isSpectator() && (!entityhuman.isCreative() || !entityhuman.abilities.isFlying)) {
                                this.n.put(entityhuman, new Vec3D(d8 * d13, d9 * d13, d10 * d13));
                            }
                        }
                    }
                }
            }
        }

    }

    public void a(boolean flag) {
        if (this.world.isClientSide) {
            this.world.a(this.posX, this.posY, this.posZ, SoundEffects.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }

        boolean flag1 = this.c != Explosion.Effect.NONE;

        if (flag) {
            if (this.size >= 2.0F && flag1) {
                this.world.addParticle(Particles.EXPLOSION_EMITTER, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            } else {
                this.world.addParticle(Particles.EXPLOSION, this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            }
        }

        if (flag1) {
            ObjectArrayList<Pair<ItemStack, BlockPosition>> objectarraylist = new ObjectArrayList();

            Collections.shuffle(this.blocks, this.world.random);
            Iterator iterator = this.blocks.iterator();

            while (iterator.hasNext()) {
                BlockPosition blockposition = (BlockPosition) iterator.next();
                IBlockData iblockdata = this.world.getType(blockposition);
                Block block = iblockdata.getBlock();

                if (!iblockdata.isAir()) {
                    BlockPosition blockposition1 = blockposition.immutableCopy();

                    this.world.getMethodProfiler().enter("explosion_blocks");
                    if (block.a(this) && this.world instanceof WorldServer) {
                        TileEntity tileentity = block.isTileEntity() ? this.world.getTileEntity(blockposition) : null;
                        LootTableInfo.Builder loottableinfo_builder = (new LootTableInfo.Builder((WorldServer) this.world)).a(this.world.random).set(LootContextParameters.ORIGIN, Vec3D.a((BaseBlockPosition) blockposition)).set(LootContextParameters.TOOL, ItemStack.b).setOptional(LootContextParameters.BLOCK_ENTITY, tileentity).setOptional(LootContextParameters.THIS_ENTITY, this.source);

                        if (this.c == Explosion.Effect.DESTROY) {
                            loottableinfo_builder.set(LootContextParameters.EXPLOSION_RADIUS, this.size);
                        }

                        iblockdata.a(loottableinfo_builder).forEach((itemstack) -> {
                            a(objectarraylist, itemstack, blockposition1);
                        });
                    }

                    this.world.setTypeAndData(blockposition, Blocks.AIR.getBlockData(), 3);
                    block.wasExploded(this.world, blockposition, this);
                    this.world.getMethodProfiler().exit();
                }
            }

            ObjectListIterator objectlistiterator = objectarraylist.iterator();

            while (objectlistiterator.hasNext()) {
                Pair<ItemStack, BlockPosition> pair = (Pair) objectlistiterator.next();

                Block.a(this.world, (BlockPosition) pair.getSecond(), (ItemStack) pair.getFirst());
            }
        }

        if (this.b) {
            Iterator iterator1 = this.blocks.iterator();

            while (iterator1.hasNext()) {
                BlockPosition blockposition2 = (BlockPosition) iterator1.next();

                if (this.d.nextInt(3) == 0 && this.world.getType(blockposition2).isAir() && this.world.getType(blockposition2.down()).i(this.world, blockposition2.down())) {
                    this.world.setTypeUpdate(blockposition2, BlockFireAbstract.a((IBlockAccess) this.world, blockposition2));
                }
            }
        }

    }

    private static void a(ObjectArrayList<Pair<ItemStack, BlockPosition>> objectarraylist, ItemStack itemstack, BlockPosition blockposition) {
        int i = objectarraylist.size();

        for (int j = 0; j < i; ++j) {
            Pair<ItemStack, BlockPosition> pair = (Pair) objectarraylist.get(j);
            ItemStack itemstack1 = (ItemStack) pair.getFirst();

            if (EntityItem.a(itemstack1, itemstack)) {
                ItemStack itemstack2 = EntityItem.a(itemstack1, itemstack, 16);

                objectarraylist.set(j, Pair.of(itemstack2, pair.getSecond()));
                if (itemstack.isEmpty()) {
                    return;
                }
            }
        }

        objectarraylist.add(Pair.of(itemstack, blockposition));
    }

    public DamageSource b() {
        return this.k;
    }

    public Map<EntityHuman, Vec3D> c() {
        return this.n;
    }

    @Nullable
    public EntityLiving getSource() {
        if (this.source == null) {
            return null;
        } else if (this.source instanceof EntityTNTPrimed) {
            return ((EntityTNTPrimed) this.source).getSource();
        } else if (this.source instanceof EntityLiving) {
            return (EntityLiving) this.source;
        } else {
            if (this.source instanceof IProjectile) {
                Entity entity = ((IProjectile) this.source).getShooter();

                if (entity instanceof EntityLiving) {
                    return (EntityLiving) entity;
                }
            }

            return null;
        }
    }

    public void clearBlocks() {
        this.blocks.clear();
    }

    public List<BlockPosition> getBlocks() {
        return this.blocks;
    }

    public static enum Effect {

        NONE, BREAK, DESTROY;

        private Effect() {}
    }
}
