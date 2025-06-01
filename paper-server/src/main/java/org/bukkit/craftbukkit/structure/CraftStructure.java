package org.bukkit.craftbukkit.structure;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.TagValueInput;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.CraftRegionAccessor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.CraftBlockVector;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.CraftStructureTransformer;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.craftbukkit.util.TransformerGeneratorAccess;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.util.BlockTransformer;
import org.bukkit.util.BlockVector;
import org.bukkit.util.EntityTransformer;

public class CraftStructure implements Structure {

    private final StructureTemplate structure;
    private final RegistryAccess registry;

    public CraftStructure(StructureTemplate structure, RegistryAccess registry) {
        this.structure = structure;
        this.registry = registry;
    }

    @Override
    public void place(Location location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
        this.place(location, includeEntities, structureRotation, mirror, palette, integrity, random, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void place(Location location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random, Collection<BlockTransformer> blockTransformers, Collection<EntityTransformer> entityTransformers) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        location.checkFinite();
        World world = location.getWorld();
        Preconditions.checkArgument(world != null, "The World of Location cannot be null");

        BlockVector blockVector = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        this.place(world, blockVector, includeEntities, structureRotation, mirror, palette, integrity, random, blockTransformers, entityTransformers);
    }

    @Override
    public void place(RegionAccessor regionAccessor, BlockVector location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
       this.place(regionAccessor, location, includeEntities, structureRotation, mirror, palette, integrity, random, Collections.emptyList(), Collections.emptyList());
    }

    @Override
    public void place(RegionAccessor regionAccessor, BlockVector location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random, Collection<BlockTransformer> blockTransformers, Collection<EntityTransformer> entityTransformers) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(regionAccessor != null, "RegionAccessor cannot be null");
        Preconditions.checkArgument(blockTransformers != null, "BlockTransformers cannot be null");
        Preconditions.checkArgument(entityTransformers != null, "EntityTransformers cannot be null");
        location.checkFinite();

        Preconditions.checkArgument(integrity >= 0F && integrity <= 1F, "Integrity value (%S) must be between 0 and 1 inclusive", integrity);

        RandomSource randomSource = new RandomSourceWrapper(random);
        StructurePlaceSettings definedstructureinfo = new StructurePlaceSettings()
                .setMirror(net.minecraft.world.level.block.Mirror.valueOf(mirror.name()))
                .setRotation(Rotation.valueOf(structureRotation.name()))
                .setIgnoreEntities(!includeEntities)
                .addProcessor(new BlockRotProcessor(integrity))
                .setRandom(randomSource);
        definedstructureinfo.palette = palette;

        BlockPos pos = CraftBlockVector.toBlockPosition(location);
        WorldGenLevel handle = ((CraftRegionAccessor) regionAccessor).getHandle();

        TransformerGeneratorAccess access = new TransformerGeneratorAccess();
        access.setDelegate(handle);
        access.setStructureTransformer(new CraftStructureTransformer(handle, new ChunkPos(pos), blockTransformers, entityTransformers));

        this.structure.placeInWorld(access, pos, pos, definedstructureinfo, randomSource, 2);
        access.getStructureTransformer().discard();
    }

    @Override
    public void fill(Location corner1, Location corner2, boolean includeEntities) {
        Preconditions.checkArgument(corner1 != null, "Location corner1 cannot be null");
        Preconditions.checkArgument(corner2 != null, "Location corner2 cannot be null");
        World world = corner1.getWorld();
        Preconditions.checkArgument(world != null, "World of corner1 Location cannot be null");

        Location origin = new Location(world, Math.min(corner1.getBlockX(), corner2.getBlockX()), Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
        BlockVector size = new BlockVector(Math.abs(corner1.getBlockX() - corner2.getBlockX()), Math.abs(corner1.getBlockY() - corner2.getBlockY()), Math.abs(corner1.getBlockZ() - corner2.getBlockZ()));
        this.fill(origin, size, includeEntities);
    }

    @Override
    public void fill(Location origin, BlockVector size, boolean includeEntities) {
        Preconditions.checkArgument(origin != null, "Location origin cannot be null");
        World world = origin.getWorld();
        Preconditions.checkArgument(world != null, "World of Location origin cannot be null");
        Preconditions.checkArgument(size != null, "BlockVector size cannot be null");
        Preconditions.checkArgument(size.getBlockX() >= 1 && size.getBlockY() >= 1 && size.getBlockZ() >= 1, "Size must be at least 1x1x1 but was %sx%sx%s", size.getBlockX(), size.getBlockY(), size.getBlockZ());

        this.structure.fillFromWorld(((CraftWorld) world).getHandle(), CraftLocation.toBlockPosition(origin), CraftBlockVector.toBlockPosition(size), includeEntities, List.of(Blocks.STRUCTURE_VOID));
    }

    @Override
    public BlockVector getSize() {
        return CraftBlockVector.toBukkit(this.structure.getSize());
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        for (StructureTemplate.StructureEntityInfo entity : this.structure.entityInfoList) {
            EntityType.create(TagValueInput.createGlobalDiscarding(entity.nbt), ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle(), EntitySpawnReason.STRUCTURE).ifPresent(dummyEntity -> {
                dummyEntity.setPos(entity.pos.x, entity.pos.y, entity.pos.z);
                entities.add(dummyEntity.getBukkitEntity());
            });
        }
        return Collections.unmodifiableList(entities);
    }

    @Override
    public int getEntityCount() {
        return this.structure.entityInfoList.size();
    }

    @Override
    public List<Palette> getPalettes() {
        return this.structure.palettes.stream().map((palette) -> new CraftPalette(palette, this.registry)).collect(Collectors.toList());
    }

    @Override
    public int getPaletteCount() {
        return this.structure.palettes.size();
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.getHandle().persistentDataContainer;
    }

    public StructureTemplate getHandle() {
        return this.structure;
    }
}
