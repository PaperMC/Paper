package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityAreaEffectCloud;
import net.minecraft.world.entity.EntityExperienceOrb;
import net.minecraft.world.entity.EntityInsentient;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumMobSpawn;
import net.minecraft.world.entity.GroupDataEntity;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.decoration.EntityHanging;
import net.minecraft.world.entity.decoration.EntityItemFrame;
import net.minecraft.world.entity.decoration.EntityLeash;
import net.minecraft.world.entity.decoration.EntityPainting;
import net.minecraft.world.entity.item.EntityFallingBlock;
import net.minecraft.world.entity.item.EntityTNTPrimed;
import net.minecraft.world.entity.monster.EntityZombie;
import net.minecraft.world.entity.projectile.EntityEgg;
import net.minecraft.world.entity.projectile.EntityEnderSignal;
import net.minecraft.world.entity.projectile.EntityEvokerFangs;
import net.minecraft.world.entity.projectile.EntityFireball;
import net.minecraft.world.entity.projectile.EntityFireworks;
import net.minecraft.world.entity.projectile.EntityPotion;
import net.minecraft.world.entity.projectile.EntitySnowball;
import net.minecraft.world.entity.projectile.EntityTippedArrow;
import net.minecraft.world.entity.vehicle.EntityMinecartChest;
import net.minecraft.world.entity.vehicle.EntityMinecartCommandBlock;
import net.minecraft.world.entity.vehicle.EntityMinecartFurnace;
import net.minecraft.world.entity.vehicle.EntityMinecartHopper;
import net.minecraft.world.entity.vehicle.EntityMinecartMobSpawner;
import net.minecraft.world.entity.vehicle.EntityMinecartRideable;
import net.minecraft.world.entity.vehicle.EntityMinecartTNT;
import net.minecraft.world.level.GeneratorAccessSeed;
import net.minecraft.world.level.biome.BiomeBase;
import net.minecraft.world.level.block.BlockChorusFlower;
import net.minecraft.world.level.block.BlockDiodeAbstract;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.WorldGenFeatureConfigured;
import net.minecraft.world.phys.AxisAlignedBB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.RegionAccessor;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.block.CraftBlock;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.BlockStateListPopulator;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.entity.AbstractArrow;
import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.AbstractSkeleton;
import org.bukkit.entity.AbstractVillager;
import org.bukkit.entity.Allay;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Axolotl;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Camel;
import org.bukkit.entity.Cat;
import org.bukkit.entity.CaveSpider;
import org.bukkit.entity.ChestBoat;
import org.bukkit.entity.ChestedHorse;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cod;
import org.bukkit.entity.ComplexLivingEntity;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Display;
import org.bukkit.entity.Dolphin;
import org.bukkit.entity.Donkey;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ElderGuardian;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EnderSignal;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Endermite;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Fox;
import org.bukkit.entity.Frog;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.GlowItemFrame;
import org.bukkit.entity.GlowSquid;
import org.bukkit.entity.Goat;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Guardian;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Hoglin;
import org.bukkit.entity.Husk;
import org.bukkit.entity.Illager;
import org.bukkit.entity.Illusioner;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.IronGolem;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LeashHitch;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LingeringPotion;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Llama;
import org.bukkit.entity.LlamaSpit;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Marker;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Mule;
import org.bukkit.entity.MushroomCow;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Panda;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.PiglinBrute;
import org.bukkit.entity.Pillager;
import org.bukkit.entity.Player;
import org.bukkit.entity.PolarBear;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.PufferFish;
import org.bukkit.entity.Rabbit;
import org.bukkit.entity.Ravager;
import org.bukkit.entity.Salmon;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.Silverfish;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.SkeletonHorse;
import org.bukkit.entity.Slime;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Sniffer;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.Snowman;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.Spellcaster;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Stray;
import org.bukkit.entity.Strider;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Tadpole;
import org.bukkit.entity.Tameable;
import org.bukkit.entity.TextDisplay;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.TraderLlama;
import org.bukkit.entity.Trident;
import org.bukkit.entity.TropicalFish;
import org.bukkit.entity.Turtle;
import org.bukkit.entity.Vex;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Vindicator;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.entity.Warden;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.WitherSkull;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zoglin;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.entity.minecart.CommandMinecart;
import org.bukkit.entity.minecart.ExplosiveMinecart;
import org.bukkit.entity.minecart.HopperMinecart;
import org.bukkit.entity.minecart.PoweredMinecart;
import org.bukkit.entity.minecart.SpawnerMinecart;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

public abstract class CraftRegionAccessor implements RegionAccessor {

    public abstract GeneratorAccessSeed getHandle();

    public boolean isNormalWorld() {
        return getHandle() instanceof net.minecraft.server.level.WorldServer;
    }

    @Override
    public Biome getBiome(Location location) {
        return getBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Biome getBiome(int x, int y, int z) {
        return CraftBlock.biomeBaseToBiome(getHandle().registryAccess().registryOrThrow(Registries.BIOME), getHandle().getNoiseBiome(x >> 2, y >> 2, z >> 2));
    }

    @Override
    public void setBiome(Location location, Biome biome) {
        setBiome(location.getBlockX(), location.getBlockY(), location.getBlockZ(), biome);
    }

    @Override
    public void setBiome(int x, int y, int z, Biome biome) {
        Preconditions.checkArgument(biome != Biome.CUSTOM, "Cannot set the biome to %s", biome);
        Holder<BiomeBase> biomeBase = CraftBlock.biomeToBiomeBase(getHandle().registryAccess().registryOrThrow(Registries.BIOME), biome);
        setBiome(x, y, z, biomeBase);
    }

    public abstract void setBiome(int x, int y, int z, Holder<BiomeBase> biomeBase);

    @Override
    public BlockState getBlockState(Location location) {
        return getBlockState(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return CraftBlock.at(getHandle(), new BlockPosition(x, y, z)).getState();
    }

    @Override
    public BlockData getBlockData(Location location) {
        return getBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public BlockData getBlockData(int x, int y, int z) {
        return CraftBlockData.fromData(getData(x, y, z));
    }

    @Override
    public Material getType(Location location) {
        return getType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    @Override
    public Material getType(int x, int y, int z) {
        return CraftMagicNumbers.getMaterial(getData(x, y, z).getBlock());
    }

    private IBlockData getData(int x, int y, int z) {
        return getHandle().getBlockState(new BlockPosition(x, y, z));
    }

    @Override
    public void setBlockData(Location location, BlockData blockData) {
        setBlockData(location.getBlockX(), location.getBlockY(), location.getBlockZ(), blockData);
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockData blockData) {
        GeneratorAccessSeed world = getHandle();
        BlockPosition pos = new BlockPosition(x, y, z);
        IBlockData old = getHandle().getBlockState(pos);

        CraftBlock.setTypeAndData(world, pos, old, ((CraftBlockData) blockData).getState(), true);
    }

    @Override
    public void setType(Location location, Material material) {
        setType(location.getBlockX(), location.getBlockY(), location.getBlockZ(), material);
    }

    @Override
    public void setType(int x, int y, int z, Material material) {
        setBlockData(x, y, z, material.createBlockData());
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType) {
        BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        return generateTree(getHandle(), getHandle().getMinecraftWorld().getChunkSource().getGenerator(), pos, new RandomSourceWrapper(random), treeType);
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Consumer<BlockState> consumer) {
        return generateTree(location, random, treeType, (consumer == null) ? null : (block) -> {
            consumer.accept(block);
            return true;
        });
    }

    @Override
    public boolean generateTree(Location location, Random random, TreeType treeType, Predicate<BlockState> predicate) {
        BlockPosition pos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        BlockStateListPopulator populator = new BlockStateListPopulator(getHandle());
        boolean result = generateTree(populator, getHandle().getMinecraftWorld().getChunkSource().getGenerator(), pos, new RandomSourceWrapper(random), treeType);
        populator.refreshTiles();

        for (BlockState blockState : populator.getList()) {
            if (predicate == null || predicate.test(blockState)) {
                blockState.update(true, true);
            }
        }

        return result;
    }

    public boolean generateTree(GeneratorAccessSeed access, ChunkGenerator chunkGenerator, BlockPosition pos, RandomSource random, TreeType treeType) {
        ResourceKey<WorldGenFeatureConfigured<?, ?>> gen;
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
                gen = TreeFeatures.MEGA_PINE;
                break;
            case TALL_BIRCH:
                gen = TreeFeatures.SUPER_BIRCH_BEES_0002;
                break;
            case CHORUS_PLANT:
                ((BlockChorusFlower) Blocks.CHORUS_FLOWER).generatePlant(access, pos, random, 8);
                return true;
            case CRIMSON_FUNGUS:
                gen = TreeFeatures.CRIMSON_FUNGUS_PLANTED;
                break;
            case WARPED_FUNGUS:
                gen = TreeFeatures.WARPED_FUNGUS_PLANTED;
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
            case TREE:
            default:
                gen = TreeFeatures.OAK;
                break;
        }

        Holder<WorldGenFeatureConfigured<?, ?>> holder = access.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(gen).orElse(null);
        return (holder != null) ? holder.value().place(access, chunkGenerator, random, pos) : false;
    }

    @Override
    public Entity spawnEntity(Location location, EntityType entityType) {
        return spawn(location, entityType.getEntityClass());
    }

    @Override
    public Entity spawnEntity(Location loc, EntityType type, boolean randomizeData) {
        return spawn(loc, type.getEntityClass(), null, CreatureSpawnEvent.SpawnReason.CUSTOM, randomizeData);
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> list = new ArrayList<Entity>();

        getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity != null && (!isNormalWorld() || bukkitEntity.isValid())) {
                list.add(bukkitEntity);
            }
        });

        return list;
    }

    @Override
    public List<LivingEntity> getLivingEntities() {
        List<LivingEntity> list = new ArrayList<LivingEntity>();

        getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            // Assuming that bukkitEntity isn't null
            if (bukkitEntity != null && bukkitEntity instanceof LivingEntity && (!isNormalWorld() || bukkitEntity.isValid())) {
                list.add((LivingEntity) bukkitEntity);
            }
        });

        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Entity> Collection<T> getEntitiesByClass(Class<T> clazz) {
        Collection<T> list = new ArrayList<T>();

        getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity == null) {
                return;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();

            if (clazz.isAssignableFrom(bukkitClass) && (!isNormalWorld() || bukkitEntity.isValid())) {
                list.add((T) bukkitEntity);
            }
        });

        return list;
    }

    @Override
    public Collection<Entity> getEntitiesByClasses(Class<?>... classes) {
        Collection<Entity> list = new ArrayList<Entity>();

        getNMSEntities().forEach(entity -> {
            Entity bukkitEntity = entity.getBukkitEntity();

            if (bukkitEntity == null) {
                return;
            }

            Class<?> bukkitClass = bukkitEntity.getClass();

            for (Class<?> clazz : classes) {
                if (clazz.isAssignableFrom(bukkitClass)) {
                    if (!isNormalWorld() || bukkitEntity.isValid()) {
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
    public <T extends Entity> T spawn(Location location, Class<T> clazz) throws IllegalArgumentException {
        return spawn(location, clazz, null, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function) throws IllegalArgumentException {
        return spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM);
    }

    @Override
    public <T extends Entity> T spawn(Location location, Class<T> clazz, boolean randomizeData, Consumer<T> function) throws IllegalArgumentException {
        return spawn(location, clazz, function, CreatureSpawnEvent.SpawnReason.CUSTOM, randomizeData);
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return spawn(location, clazz, function, reason, true);
    }

    public <T extends Entity> T spawn(Location location, Class<T> clazz, Consumer<T> function, CreatureSpawnEvent.SpawnReason reason, boolean randomizeData) throws IllegalArgumentException {
        net.minecraft.world.entity.Entity entity = createEntity(location, clazz, randomizeData);

        return addEntity(entity, reason, function, randomizeData);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason) throws IllegalArgumentException {
        return addEntity(entity, reason, null, true);
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> T addEntity(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason, Consumer<T> function, boolean randomizeData) throws IllegalArgumentException {
        Preconditions.checkArgument(entity != null, "Cannot spawn null entity");

        if (randomizeData && entity instanceof EntityInsentient) {
            ((EntityInsentient) entity).finalizeSpawn(getHandle(), getHandle().getCurrentDifficultyAt(entity.blockPosition()), EnumMobSpawn.COMMAND, (GroupDataEntity) null, null);
        }

        if (!isNormalWorld()) {
            entity.generation = true;
        }

        if (function != null) {
            function.accept((T) entity.getBukkitEntity());
        }

        addEntityToWorld(entity, reason);
        return (T) entity.getBukkitEntity();
    }

    public abstract void addEntityToWorld(net.minecraft.world.entity.Entity entity, CreatureSpawnEvent.SpawnReason reason);

    @SuppressWarnings("unchecked")
    public net.minecraft.world.entity.Entity createEntity(Location location, Class<? extends Entity> clazz) throws IllegalArgumentException {
        return createEntity(location, clazz, true);
    }

    @SuppressWarnings("unchecked")
    public net.minecraft.world.entity.Entity createEntity(Location location, Class<? extends Entity> clazz, boolean randomizeData) throws IllegalArgumentException {
        if (location == null || clazz == null) {
            throw new IllegalArgumentException("Location or entity class cannot be null");
        }

        net.minecraft.world.entity.Entity entity = null;
        net.minecraft.world.level.World world = getHandle().getMinecraftWorld();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        float pitch = location.getPitch();
        float yaw = location.getYaw();

        // order is important for some of these
        if (Boat.class.isAssignableFrom(clazz)) {
            if (ChestBoat.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.CHEST_BOAT.create(world);
            } else {
                entity = EntityTypes.BOAT.create(world);
            }
            entity.moveTo(x, y, z, yaw, pitch);
        } else if (FallingBlock.class.isAssignableFrom(clazz)) {
            BlockPosition pos = BlockPosition.containing(x, y, z);
            entity = EntityFallingBlock.fall(world, pos, getHandle().getBlockState(pos));
        } else if (Projectile.class.isAssignableFrom(clazz)) {
            if (Snowball.class.isAssignableFrom(clazz)) {
                entity = new EntitySnowball(world, x, y, z);
            } else if (Egg.class.isAssignableFrom(clazz)) {
                entity = new EntityEgg(world, x, y, z);
            } else if (AbstractArrow.class.isAssignableFrom(clazz)) {
                if (TippedArrow.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.ARROW.create(world);
                    ((EntityTippedArrow) entity).setPotionType(CraftPotionUtil.fromBukkit(new PotionData(PotionType.WATER, false, false)));
                } else if (SpectralArrow.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SPECTRAL_ARROW.create(world);
                } else if (Trident.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.TRIDENT.create(world);
                } else {
                    entity = EntityTypes.ARROW.create(world);
                }
                entity.moveTo(x, y, z, 0, 0);
            } else if (ThrownExpBottle.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.EXPERIENCE_BOTTLE.create(world);
                entity.moveTo(x, y, z, 0, 0);
            } else if (EnderPearl.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ENDER_PEARL.create(world);
                entity.moveTo(x, y, z, 0, 0);
            } else if (ThrownPotion.class.isAssignableFrom(clazz)) {
                if (LingeringPotion.class.isAssignableFrom(clazz)) {
                    entity = new EntityPotion(world, x, y, z);
                    ((EntityPotion) entity).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.LINGERING_POTION, 1)));
                } else {
                    entity = new EntityPotion(world, x, y, z);
                    ((EntityPotion) entity).setItem(CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.SPLASH_POTION, 1)));
                }
            } else if (Fireball.class.isAssignableFrom(clazz)) {
                if (SmallFireball.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SMALL_FIREBALL.create(world);
                } else if (WitherSkull.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.WITHER_SKULL.create(world);
                } else if (DragonFireball.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.DRAGON_FIREBALL.create(world);
                } else {
                    entity = EntityTypes.FIREBALL.create(world);
                }
                entity.moveTo(x, y, z, yaw, pitch);
                Vector direction = location.getDirection().multiply(10);
                ((EntityFireball) entity).setDirection(direction.getX(), direction.getY(), direction.getZ());
            } else if (ShulkerBullet.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.SHULKER_BULLET.create(world);
                entity.moveTo(x, y, z, yaw, pitch);
            } else if (LlamaSpit.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.LLAMA_SPIT.create(world);
                entity.moveTo(x, y, z, yaw, pitch);
            } else if (Firework.class.isAssignableFrom(clazz)) {
                entity = new EntityFireworks(world, x, y, z, net.minecraft.world.item.ItemStack.EMPTY);
            }
        } else if (Minecart.class.isAssignableFrom(clazz)) {
            if (PoweredMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartFurnace(world, x, y, z);
            } else if (StorageMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartChest(world, x, y, z);
            } else if (ExplosiveMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartTNT(world, x, y, z);
            } else if (HopperMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartHopper(world, x, y, z);
            } else if (SpawnerMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartMobSpawner(world, x, y, z);
            } else if (CommandMinecart.class.isAssignableFrom(clazz)) {
                entity = new EntityMinecartCommandBlock(world, x, y, z);
            } else { // Default to rideable minecart for pre-rideable compatibility
                entity = new EntityMinecartRideable(world, x, y, z);
            }
        } else if (EnderSignal.class.isAssignableFrom(clazz)) {
            entity = new EntityEnderSignal(world, x, y, z);
        } else if (EnderCrystal.class.isAssignableFrom(clazz)) {
            entity = EntityTypes.END_CRYSTAL.create(world);
            entity.moveTo(x, y, z, 0, 0);
        } else if (LivingEntity.class.isAssignableFrom(clazz)) {
            if (Chicken.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.CHICKEN.create(world);
            } else if (Cow.class.isAssignableFrom(clazz)) {
                if (MushroomCow.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.MOOSHROOM.create(world);
                } else {
                    entity = EntityTypes.COW.create(world);
                }
            } else if (Golem.class.isAssignableFrom(clazz)) {
                if (Snowman.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SNOW_GOLEM.create(world);
                } else if (IronGolem.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.IRON_GOLEM.create(world);
                } else if (Shulker.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SHULKER.create(world);
                }
            } else if (Creeper.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.CREEPER.create(world);
            } else if (Ghast.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.GHAST.create(world);
            } else if (Pig.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.PIG.create(world);
            } else if (Player.class.isAssignableFrom(clazz)) {
                // need a net server handler for this one
            } else if (Sheep.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.SHEEP.create(world);
            } else if (AbstractHorse.class.isAssignableFrom(clazz)) {
                if (ChestedHorse.class.isAssignableFrom(clazz)) {
                    if (Donkey.class.isAssignableFrom(clazz)) {
                        entity = EntityTypes.DONKEY.create(world);
                    } else if (Mule.class.isAssignableFrom(clazz)) {
                        entity = EntityTypes.MULE.create(world);
                    } else if (Llama.class.isAssignableFrom(clazz)) {
                        if (TraderLlama.class.isAssignableFrom(clazz)) {
                            entity = EntityTypes.TRADER_LLAMA.create(world);
                        } else {
                            entity = EntityTypes.LLAMA.create(world);
                        }
                    }
                } else if (SkeletonHorse.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SKELETON_HORSE.create(world);
                } else if (ZombieHorse.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.ZOMBIE_HORSE.create(world);
                } else if (Camel.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.CAMEL.create(world);
                } else {
                    entity = EntityTypes.HORSE.create(world);
                }
            } else if (AbstractSkeleton.class.isAssignableFrom(clazz)) {
                if (Stray.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.STRAY.create(world);
                } else if (WitherSkeleton.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.WITHER_SKELETON.create(world);
                } else if (Skeleton.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SKELETON.create(world);
                }
            } else if (Slime.class.isAssignableFrom(clazz)) {
                if (MagmaCube.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.MAGMA_CUBE.create(world);
                } else {
                    entity = EntityTypes.SLIME.create(world);
                }
            } else if (Spider.class.isAssignableFrom(clazz)) {
                if (CaveSpider.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.CAVE_SPIDER.create(world);
                } else {
                    entity = EntityTypes.SPIDER.create(world);
                }
            } else if (Squid.class.isAssignableFrom(clazz)) {
                if (GlowSquid.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.GLOW_SQUID.create(world);
                } else {
                    entity = EntityTypes.SQUID.create(world);
                }
            } else if (Tameable.class.isAssignableFrom(clazz)) {
                if (Wolf.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.WOLF.create(world);
                } else if (Parrot.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.PARROT.create(world);
                } else if (Cat.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.CAT.create(world);
                }
            } else if (PigZombie.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ZOMBIFIED_PIGLIN.create(world);
            } else if (Zombie.class.isAssignableFrom(clazz)) {
                if (Husk.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.HUSK.create(world);
                } else if (ZombieVillager.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.ZOMBIE_VILLAGER.create(world);
                } else if (Drowned.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.DROWNED.create(world);
                } else {
                    entity = new EntityZombie(world);
                }
            } else if (Giant.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.GIANT.create(world);
            } else if (Silverfish.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.SILVERFISH.create(world);
            } else if (Enderman.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ENDERMAN.create(world);
            } else if (Blaze.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.BLAZE.create(world);
            } else if (AbstractVillager.class.isAssignableFrom(clazz)) {
                if (Villager.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.VILLAGER.create(world);
                } else if (WanderingTrader.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.WANDERING_TRADER.create(world);
                }
            } else if (Witch.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.WITCH.create(world);
            } else if (Wither.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.WITHER.create(world);
            } else if (ComplexLivingEntity.class.isAssignableFrom(clazz)) {
                if (EnderDragon.class.isAssignableFrom(clazz)) {
                    if (isNormalWorld()) {
                        entity = EntityTypes.ENDER_DRAGON.create(getHandle().getMinecraftWorld());
                    } else {
                        throw new IllegalArgumentException("Cannot spawn entity " + clazz.getName() + " during world generation");
                    }
                }
            } else if (Ambient.class.isAssignableFrom(clazz)) {
                if (Bat.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.BAT.create(world);
                }
            } else if (Rabbit.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.RABBIT.create(world);
            } else if (Endermite.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ENDERMITE.create(world);
            } else if (Guardian.class.isAssignableFrom(clazz)) {
                if (ElderGuardian.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.ELDER_GUARDIAN.create(world);
                } else {
                    entity = EntityTypes.GUARDIAN.create(world);
                }
            } else if (ArmorStand.class.isAssignableFrom(clazz)) {
                entity = new EntityArmorStand(world, x, y, z);
            } else if (PolarBear.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.POLAR_BEAR.create(world);
            } else if (Vex.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.VEX.create(world);
            } else if (Illager.class.isAssignableFrom(clazz)) {
                if (Spellcaster.class.isAssignableFrom(clazz)) {
                    if (Evoker.class.isAssignableFrom(clazz)) {
                        entity = EntityTypes.EVOKER.create(world);
                    } else if (Illusioner.class.isAssignableFrom(clazz)) {
                        entity = EntityTypes.ILLUSIONER.create(world);
                    }
                } else if (Vindicator.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.VINDICATOR.create(world);
                } else if (Pillager.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.PILLAGER.create(world);
                }
            } else if (Turtle.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.TURTLE.create(world);
            } else if (Phantom.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.PHANTOM.create(world);
            } else if (Fish.class.isAssignableFrom(clazz)) {
                if (Cod.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.COD.create(world);
                } else if (PufferFish.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.PUFFERFISH.create(world);
                } else if (Salmon.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.SALMON.create(world);
                } else if (TropicalFish.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.TROPICAL_FISH.create(world);
                } else if (Tadpole.class.isAssignableFrom(clazz)) {
                    entity = EntityTypes.TADPOLE.create(world);
                }
            } else if (Dolphin.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.DOLPHIN.create(world);
            } else if (Ocelot.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.OCELOT.create(world);
            } else if (Ravager.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.RAVAGER.create(world);
            } else if (Panda.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.PANDA.create(world);
            } else if (Fox.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.FOX.create(world);
            } else if (Bee.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.BEE.create(world);
            } else if (Hoglin.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.HOGLIN.create(world);
            } else if (Piglin.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.PIGLIN.create(world);
            } else if (PiglinBrute.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.PIGLIN_BRUTE.create(world);
            } else if (Strider.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.STRIDER.create(world);
            } else if (Zoglin.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ZOGLIN.create(world);
            } else if (Axolotl.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.AXOLOTL.create(world);
            } else if (Goat.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.GOAT.create(world);
            } else if (Allay.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ALLAY.create(world);
            } else if (Frog.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.FROG.create(world);
            } else if (Warden.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.WARDEN.create(world);
            } else if (Sniffer.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.SNIFFER.create(world);
            }

            if (entity != null) {
                entity.absMoveTo(x, y, z, yaw, pitch);
                entity.setYHeadRot(yaw); // SPIGOT-3587
            }
        } else if (Hanging.class.isAssignableFrom(clazz)) {
            if (LeashHitch.class.isAssignableFrom(clazz)) {
                // SPIGOT-5732: LeashHitch has no direction and is always centered at a block
                entity = new EntityLeash(world, BlockPosition.containing(x, y, z));
            } else {
                BlockFace face = BlockFace.SELF;
                BlockFace[] faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH};

                int width = 16; // 1 full block, also painting smallest size.
                int height = 16; // 1 full block, also painting smallest size.

                if (ItemFrame.class.isAssignableFrom(clazz)) {
                    width = 12;
                    height = 12;
                    faces = new BlockFace[]{BlockFace.EAST, BlockFace.NORTH, BlockFace.WEST, BlockFace.SOUTH, BlockFace.UP, BlockFace.DOWN};
                }

                final BlockPosition pos = BlockPosition.containing(x, y, z);
                for (BlockFace dir : faces) {
                    IBlockData nmsBlock = getHandle().getBlockState(pos.relative(CraftBlock.blockFaceToNotch(dir)));
                    if (nmsBlock.getMaterial().isSolid() || BlockDiodeAbstract.isDiode(nmsBlock)) {
                        boolean taken = false;
                        AxisAlignedBB bb = (ItemFrame.class.isAssignableFrom(clazz))
                                ? EntityItemFrame.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height)
                                : EntityHanging.calculateBoundingBox(null, pos, CraftBlock.blockFaceToNotch(dir).getOpposite(), width, height);
                        List<net.minecraft.world.entity.Entity> list = (List<net.minecraft.world.entity.Entity>) getHandle().getEntities(null, bb);
                        for (Iterator<net.minecraft.world.entity.Entity> it = list.iterator(); !taken && it.hasNext(); ) {
                            net.minecraft.world.entity.Entity e = it.next();
                            if (e instanceof EntityHanging) {
                                taken = true; // Hanging entities do not like hanging entities which intersect them.
                            }
                        }

                        if (!taken) {
                            face = dir;
                            break;
                        }
                    }
                }

                // No valid face found
                if (face == BlockFace.SELF) {
                    // SPIGOT-6387: Allow hanging entities to be placed in midair
                    face = BlockFace.SOUTH;
                    randomizeData = false; // Don't randomize if no valid face is found, prevents null painting
                }

                EnumDirection dir = CraftBlock.blockFaceToNotch(face).getOpposite();
                if (Painting.class.isAssignableFrom(clazz)) {
                    if (isNormalWorld() && randomizeData) {
                        entity = EntityPainting.create(world, pos, dir).orElse(null);
                    } else {
                        entity = new EntityPainting(EntityTypes.PAINTING, getHandle().getMinecraftWorld());
                        entity.absMoveTo(x, y, z, yaw, pitch);
                        ((EntityPainting) entity).setDirection(dir);
                    }
                } else if (ItemFrame.class.isAssignableFrom(clazz)) {
                    if (GlowItemFrame.class.isAssignableFrom(clazz)) {
                        entity = new net.minecraft.world.entity.decoration.GlowItemFrame(world, BlockPosition.containing(x, y, z), dir);
                    } else {
                        entity = new EntityItemFrame(world, BlockPosition.containing(x, y, z), dir);
                    }
                }
            }
        } else if (TNTPrimed.class.isAssignableFrom(clazz)) {
            entity = new EntityTNTPrimed(world, x, y, z, null);
        } else if (ExperienceOrb.class.isAssignableFrom(clazz)) {
            entity = new EntityExperienceOrb(world, x, y, z, 0);
        } else if (LightningStrike.class.isAssignableFrom(clazz)) {
            entity = EntityTypes.LIGHTNING_BOLT.create(world);
            entity.moveTo(location.getX(), location.getY(), location.getZ());
        } else if (AreaEffectCloud.class.isAssignableFrom(clazz)) {
            entity = new EntityAreaEffectCloud(world, x, y, z);
        } else if (EvokerFangs.class.isAssignableFrom(clazz)) {
            entity = new EntityEvokerFangs(world, x, y, z, (float) Math.toRadians(yaw), 0, null);
        } else if (Marker.class.isAssignableFrom(clazz)) {
            entity = EntityTypes.MARKER.create(world);
            entity.setPos(x, y, z);
        } else if (Interaction.class.isAssignableFrom(clazz)) {
            entity = EntityTypes.INTERACTION.create(world);
            entity.setPos(x, y, z);
        } else if (Display.class.isAssignableFrom(clazz)) {
            if (BlockDisplay.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.BLOCK_DISPLAY.create(world);
            } else if (ItemDisplay.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.ITEM_DISPLAY.create(world);
            } else if (TextDisplay.class.isAssignableFrom(clazz)) {
                entity = EntityTypes.TEXT_DISPLAY.create(world);
            }

            if (entity != null) {
                entity.setPos(x, y, z);
            }
        }

        if (entity != null) {
            return entity;
        }

        throw new IllegalArgumentException("Cannot spawn an entity for " + clazz.getName());
    }
}
