package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.portal.TeleportTransition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.RegionAccessor;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBiome;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.CraftBlockType;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftEntityTypes;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractCow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Horse;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.SizedFireball;
import org.bukkit.entity.SplashPotion;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.minecart.RideableMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionType;

public abstract class CraftRegionAccessor implements RegionAccessor {

    public abstract WorldGenLevel getHandle();

    public boolean isNormalWorld() {
        return this.getHandle() instanceof net.minecraft.server.level.ServerLevel;
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return CraftBiome.minecraftHolderToBukkit(this.getHandle().getNoiseBiome(x >> 2, y >> 2, z >> 2));
    }

    @Override
    public Biome getComputedBiome(int x, int y, int z) {
        return CraftBiome.minecraftHolderToBukkit(this.getHandle().getBiome(new BlockPos(x, y, z)));
    }

    @Override
    public void setBiome(int x, int y, int z, Biome biome) {
        Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot set the biome to %s", biome);
        Holder<net.minecraft.world.level.biome.Biome> biomeBase = CraftBiome.bukkitToMinecraftHolder(biome);
        this.setBiome(x, y, z, biomeBase);
    }

    public abstract void setBiome(int x, int y, int z, Holder<net.minecraft.world.level.biome.Biome> biomeBase);

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return CraftBlock.at(this.getHandle(), new BlockPos(x, y, z)).getState();
    }

    @Override
    public io.papermc.paper.block.fluid.FluidData getFluidData(final int x, final int y, final int z) {
        return io.papermc.paper.block.fluid.PaperFluidData.createData(getHandle().getFluidState(new BlockPos(x, y, z)));
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        return CraftBlockData.fromData(this.getData(x, y, z));
    }

    @Override
    public Material getType(int x, int y, int z) {
        return CraftBlockType.minecraftToBukkit(this.getData(x, y, z).getBlock());
    }

    private net.minecraft.world.level.block.state.BlockState getData(int x, int y, int z) {
        return this.getHandle().getBlockState(new BlockPos(x, y, z));
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        WorldGenLevel world = this.getHandle();
        BlockPos pos = new BlockPos(x, y, z);
        net.minecraft.world.level.block.state.BlockState old = this.getHandle().getBlockState(pos);

        CraftBlock.setBlockState(world, pos, old, ((CraftBlockData) blockData).getState(), true);
    }

    @Override
    public int getHighestBlockYAt(int x, int z) {
        return this.getHighestBlockYAt(x, z, org.bukkit.HeightMap.MOTION_BLOCKING);
    }

    @Override
    public int getHighestBlockYAt(Location location) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ());
    }

    @Override
    public int getHighestBlockYAt(int x, int z, org.bukkit.HeightMap heightMap) {
        return this.getHandle().getHeight(CraftHeightMap.toNMS(heightMap), x, z);
    }

    @Override
    public int getHighestBlockYAt(Location location, org.bukkit.HeightMap heightMap) {
        return this.getHighestBlockYAt(location.getBlockX(), location.getBlockZ(), heightMap);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType) {
        BlockPos pos = CraftLocation.toBlockPosition(location);
        return this.generateTree(this.getHandle(), this.getHandle().getMinecraftWorld().getChunkSource().getGenerator(), pos, new RandomSourceWrapper(random), treeType);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Consumer<? super BlockState> consumer) {
        return this.generateTree(location, random, treeType, (consumer == null) ? null : (block) -> {
            consumer.accept(block);
            return true;
        });
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Predicate<? super BlockState> predicate) {
        BlockPos pos = CraftLocation.toBlockPosition(location);
        BlockStateListPopulator populator = new BlockStateListPopulator(this.getHandle());
        boolean result = this.generateTree(populator, this.getHandle().getMinecraftWorld().getChunkSource().getGenerator(), pos, new RandomSourceWrapper(random), treeType);
        populator.placeSomeBlocks(predicate == null ? ($ -> true) : predicate);
        return result;
    }

    public boolean generateTree(WorldGenLevel access, ChunkGenerator chunkGenerator, BlockPos pos, RandomSource random, TreeType treeType) {
        ResourceKey<ConfiguredFeature<?, ?>> gen;
        switch (treeType) {
            case BIG_TREE:
                gen = TreeFeatures.FANCY_OAK;
                break;
            case BIRCH:
                gen = TreeFeatures.BIRCH;
                break;
            case REDWOOD:
                gen = TreeFeatures.SPRUCE;
                break;
            case TALL_REDWOOD:
                gen = TreeFeatures.PINE;
                break;
            case JUNGLE:
                gen = TreeFeatures.MEGA_JUNGLE_TREE;
                break;
            case SMALL_JUNGLE:
                gen = TreeFeatures.JUNGLE_TREE_NO_VINE;
                break;
            case COCOA_TREE:
                gen = TreeFeatures.JUNGLE_TREE;
                break;
            case JUNGLE_BUSH:
                gen = TreeFeatures.JUNGLE_BUSH;
                break;
            case RED_MUSHROOM:
                gen = TreeFeatures.HUGE_RED_MUSHROOM;
                break;
            case BROWN_MUSHROOM:
                gen = TreeFeatures.HUGE_BROWN_MUSHROOM;
                break;
            case SWAMP:
                gen = TreeFeatures.SWAMP_OAK;
                break;
            case ACACIA:
                gen = TreeFeatures.ACACIA;
                break;
            case DARK_OAK:
                gen = TreeFeatures.DARK_OAK;
                break;
            case MEGA_REDWOOD:
                gen = TreeFeatures.MEGA_SPRUCE;
                break;
            case MEGA_PINE:
                gen = TreeFeatures.MEGA_PINE;
                break;
            case TALL_BIRCH:
                gen = TreeFeatures.SUPER_BIRCH_BEES_0002;
                break;
            case CHORUS_PLANT:
                ChorusFlowerBlock.generatePlant(access, pos, random, 8);
                return true;
            case CRIMSON_FUNGUS:
                gen = this.isNormalWorld() ? TreeFeatures.CRIMSON_FUNGUS_PLANTED : TreeFeatures.CRIMSON_FUNGUS; // Paper - Fix async entity add due to fungus trees; if world gen, don't use planted version
                break;
            case WARPED_FUNGUS:
                gen = this.isNormalWorld() ? TreeFeatures.WARPED_FUNGUS_PLANTED : TreeFeatures.WARPED_FUNGUS; // Paper - Fix async entity add due to fungus trees; if world gen, don't use planted version
                break;
            case AZALEA:
                gen = TreeFeatures.AZALEA_TREE;
                break;
            case MANGROVE:
                gen = TreeFeatures.MANGROVE;
                break;
            case TALL_MANGROVE:
                gen = TreeFeatures.TALL_MANGROVE;
                break;
            case CHERRY:
                gen = TreeFeatures.CHERRY;
                break;
            case PALE_OAK:
                gen = TreeFeatures.PALE_OAK;
                break;
            case PALE_OAK_CREAKING:
                gen = TreeFeatures.PALE_OAK_CREAKING;
                break;
            case TREE:
            default:
                gen = TreeFeatures.OAK;
                break;
        }

        Holder<ConfiguredFeature<?, ?>> holder = access.registryAccess().lookupOrThrow(Registries.CONFIGURED_FEATURE).get(gen).orElse(null);
        return holder != null && holder.value().place(access, chunkGenerator, random, pos);
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type, boolean randomizeData) {
        return this.spawn(loc, type.getEntityClass(), null, CreatureSpawnEvent.SpawnReason.CUSTOM, randomizeData);
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<>();

        this.getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity != null && (!this.isNormalWorld() || bukkitEntity.isValid())) {
                list.add(bukkitEntity);
            }
        });

        return list;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<>();

        this.getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity instanceof LivingEntity && (!this.isNormalWorld() || bukkitEntity.isValid())) {
                list.add((LivingEntity) bukkitEntity);
            }
        });

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        this.getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity == null) {
                return;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();

            if (clazz.isAssignableFrom(bukkitClass) && (!this.isNormalWorld() || bukkitEntity.isValid())) {
                list.add((T) bukkitEntity);
            }
        });

        return list;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<>();

        this.getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity == null) {
                return;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();

            for (Class<?> clazz : classes) {
                if (clazz.isAssignableFrom(bukkitClass)) {
                    if (!this.isNormalWorld() || bukkitEntity.isValid()) {
                        list.add(bukkitEntity);
                    }
                    break;
                }
            }
        });

        return list;
    }

    public abstract Iterable<net.minecraft.world.entity.Entity> getNMSEntities();

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T createEntity(Location location, Class<T> clazz) throws IllegalArgumentException {
        net.minecraft.world.entity.Entity entity = this.createEntity(location, clazz, true);

        if (!this.isNormalWorld()) {
            entity.generation = true;
        }

        return (T) entity.getBukkitEntity();
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, boolean randomizeData, Consumer<? super T> function) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM, randomizeData);
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return this.spawn(location, clazz, function, reason, true);
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<? super T> function, CreatureSpawnEvent.SpawnReason reason, boolean randomizeData) throws IllegalArgumentException {
        net.minecraft.world.entity.Entity entity = this.createEntity(location, clazz, randomizeData);

        return this.addEntity(entity, reason, function, randomizeData);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(T entity) {
        Preconditions.checkArgument(!entity.isInWorld(), "Entity has already been added to a world");
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        if (nmsEntity.level() != this.getHandle().getLevel()) {
            throw new IllegalArgumentException(entity + " wasn't created with this world, you must create the entity with the world you want to add it to.");
        }

        this.addEntityWithPassengers(nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (T) nmsEntity.getBukkitEntity();
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return this.addEntity(entity, reason, null, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason, Consumer<? super T> function, boolean randomizeData) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (randomizeData && entity instanceof Mob) {
            ((Mob) entity).finalizeSpawn(this.getHandle(), this.getHandle().getCurrentDifficultyAt(entity.blockPosition()), EntitySpawnReason.COMMAND, (SpawnGroupData) null);
        }

        if (!this.isNormalWorld()) {
            entity.generation = true;
        }

        if (function != null) {
            function.accept((T) entity.getBukkitEntity());
        }

        this.addEntityToWorld(entity, reason);
        return (T) entity.getBukkitEntity();
    }

    public abstract void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

    public abstract void addEntityWithPassengers(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

    @SuppressWarnings("unchecked")
    public net.minecraft.world.entity.Entity createEntity(Location location, Class<? extends Entity> clazz, boolean randomizeData) throws IllegalArgumentException {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(clazz != null, "Entity class cannot be null");

        // Convert classes which have no direct entity type, but where spawn able by the if cases
        Consumer<net.minecraft.world.entity.Entity> runOld = other -> { };
        if (clazz == AbstractArrow.class) {
            clazz = Arrow.class;
        } else if (clazz == AbstractHorse.class) {
            clazz = Horse.class;
        } else if (clazz == AbstractCow.class) {
            clazz = Cow.class;
        } else if (clazz == Fireball.class) {
            clazz = LargeFireball.class;
        } else if (clazz == ThrownPotion.class) {
            clazz = SplashPotion.class;
        } else if (clazz == Minecart.class) {
            clazz = RideableMinecart.class;
        } else if (clazz == SizedFireball.class) {
            clazz = LargeFireball.class;
        } else if (clazz == TippedArrow.class) {
            clazz = Arrow.class;
            runOld = other -> ((Arrow) other.getBukkitEntity()).setBasePotionType(PotionType.WATER);
        }

        CraftEntityTypes.EntityTypeData<?, ?> entityTypeData = CraftEntityTypes.getEntityTypeData(clazz);

        if (entityTypeData == null || entityTypeData.spawnFunction() == null) {
            if (CraftEntity.class.isAssignableFrom(clazz)) {
                // SPIGOT-7565: Throw a more descriptive error message when a developer tries to spawn an entity from a CraftBukkit class
                throw new IllegalArgumentException(String.format("Cannot spawn an entity from its CraftBukkit implementation class '%s' use the Bukkit class instead. "
                        + "You can get the Bukkit representation via Entity#getType()#getEntityClass()", clazz.getName()));
            } else {
                throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
            }
        }

        if (!this.isEnabled(entityTypeData.entityType())) {
            throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName() + " because it is not an enabled feature");
        }

        net.minecraft.world.entity.Entity entity = entityTypeData.spawnFunction().apply(new CraftEntityTypes.SpawnData(this.getHandle(), location, randomizeData, this.isNormalWorld()));

        if (entity != null) {
            runOld.accept(entity);
            return entity;
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }

    @Override
    public io.papermc.paper.world.MoonPhase getMoonPhase() {
        return io.papermc.paper.world.MoonPhase.getPhase(this.getHandle().dayTime() / Level.TICKS_PER_DAY);
    }

    @Override
    public org.bukkit.NamespacedKey getKey() {
        return org.bukkit.craftbukkit.util.CraftNamespacedKey.fromMinecraft(this.getHandle().getLevel().dimension().location());
    }

    public boolean lineOfSightExists(Location from, Location to) {
        Preconditions.checkArgument(from != null, "from parameter in lineOfSightExists cannot be null");
        Preconditions.checkArgument(to != null, "to parameter in lineOfSightExists cannot be null");
        if (from.getWorld() != to.getWorld()) {
            return false;
        }

        net.minecraft.world.phys.Vec3 start = new net.minecraft.world.phys.Vec3(from.getX(), from.getY(), from.getZ());
        net.minecraft.world.phys.Vec3 end = new net.minecraft.world.phys.Vec3(to.getX(), to.getY(), to.getZ());
        if (end.distanceToSqr(start) > Mth.square(128D)) {
            return false; // Return early if the distance is greater than 128 blocks
        }

        return this.getHandle().clip(new net.minecraft.world.level.ClipContext(start, end, net.minecraft.world.level.ClipContext.Block.COLLIDER, net.minecraft.world.level.ClipContext.Fluid.NONE, net.minecraft.world.phys.shapes.CollisionContext.empty())).getType() == net.minecraft.world.phys.HitResult.Type.MISS;
    }

    @Override
    public boolean hasCollisionsIn(@org.jetbrains.annotations.NotNull org.bukkit.util.BoundingBox boundingBox) {
        net.minecraft.world.phys.AABB aabb = new net.minecraft.world.phys.AABB(boundingBox.getMinX(), boundingBox.getMinY(), boundingBox.getMinZ(), boundingBox.getMaxX(), boundingBox.getMaxY(), boundingBox.getMaxZ());

        return !this.getHandle().noCollision(aabb);
    }

    @Override
    public java.util.Set<org.bukkit.FeatureFlag> getFeatureFlags() {
        return io.papermc.paper.world.flag.PaperFeatureFlagProviderImpl.fromNms(this.getHandle().enabledFeatures());
    }
}
