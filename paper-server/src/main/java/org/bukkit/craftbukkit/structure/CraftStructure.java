package org.bukkit.craftbukkit.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPosition;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnumBlockMirror;
import net.minecraft.world.level.block.EnumBlockRotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureInfo;
import net.minecraft.world.level.levelgen.structure.templatesystem.DefinedStructureProcessorRotation;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.RegionAccessor;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.craftbukkit.CraftRegionAccessor;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.structure.Palette;
import org.bukkit.structure.Structure;
import org.bukkit.util.BlockVector;

public class CraftStructure implements Structure {

    private final DefinedStructure structure;

    public CraftStructure(DefinedStructure structure) {
        this.structure = structure;
    }

    @Override
    public void place(Location location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
        location.checkFinite();
        World world = location.getWorld();
        Validate.notNull(world, "location#getWorld() cannot be null");

        BlockVector blockVector = new BlockVector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        place(world, blockVector, includeEntities, structureRotation, mirror, palette, integrity, random);
    }

    @Override
    public void place(RegionAccessor regionAccessor, BlockVector location, boolean includeEntities, StructureRotation structureRotation, Mirror mirror, int palette, float integrity, Random random) {
        Validate.notNull(regionAccessor, "regionAccessor can not be null");
        location.checkFinite();

        if (integrity < 0F || integrity > 1F) {
            throw new IllegalArgumentException("Integrity must be between 0 and 1 inclusive. Was \"" + integrity + "\"");
        }

        RandomSource randomSource = new RandomSourceWrapper(random);
        DefinedStructureInfo definedstructureinfo = new DefinedStructureInfo()
                .setMirror(EnumBlockMirror.valueOf(mirror.name()))
                .setRotation(EnumBlockRotation.valueOf(structureRotation.name()))
                .setIgnoreEntities(!includeEntities)
                .addProcessor(new DefinedStructureProcessorRotation(integrity))
                .setRandom(randomSource);
        definedstructureinfo.palette = palette;

        BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        structure.placeInWorld(((CraftRegionAccessor) regionAccessor).getHandle(), blockPosition, blockPosition, definedstructureinfo, randomSource, 2);
    }

    @Override
    public void fill(Location corner1, Location corner2, boolean includeEntities) {
        Validate.notNull(corner1, "corner1 cannot be null");
        Validate.notNull(corner2, "corner2 cannot be null");
        World world = corner1.getWorld();
        Validate.notNull(world, "corner1#getWorld() cannot be null");

        Location origin = new Location(world, Math.min(corner1.getBlockX(), corner2.getBlockX()), Math.min(corner1.getBlockY(), corner2.getBlockY()), Math.min(corner1.getBlockZ(), corner2.getBlockZ()));
        BlockVector size = new BlockVector(Math.abs(corner1.getBlockX() - corner2.getBlockX()), Math.abs(corner1.getBlockY() - corner2.getBlockY()), Math.abs(corner1.getBlockZ() - corner2.getBlockZ()));
        fill(origin, size, includeEntities);
    }

    @Override
    public void fill(Location origin, BlockVector size, boolean includeEntities) {
        Validate.notNull(origin, "origin cannot be null");
        World world = origin.getWorld();
        Validate.notNull(world, "origin#getWorld() cannot be null");
        Validate.notNull(size, "size cannot be null");
        if (size.getBlockX() < 1 || size.getBlockY() < 1 || size.getBlockZ() < 1) {
            throw new IllegalArgumentException("Size must be at least 1x1x1 but was " + size.getBlockX() + "x" + size.getBlockY() + "x" + size.getBlockZ());
        }

        structure.fillFromWorld(((CraftWorld) world).getHandle(), new BlockPosition(origin.getBlockX(), origin.getBlockY(), origin.getBlockZ()), new BlockPosition(size.getBlockX(), size.getBlockY(), size.getBlockZ()), includeEntities, Blocks.STRUCTURE_VOID);
    }

    @Override
    public BlockVector getSize() {
        return new BlockVector(structure.getSize().getX(), structure.getSize().getY(), structure.getSize().getZ());
    }

    @Override
    public List<Entity> getEntities() {
        List<Entity> entities = new ArrayList<>();
        for (DefinedStructure.EntityInfo entity : structure.entityInfoList) {
            EntityTypes.create(entity.nbt, ((CraftWorld) Bukkit.getServer().getWorlds().get(0)).getHandle()).ifPresent(dummyEntity -> {
                dummyEntity.setPos(entity.pos.x, entity.pos.y, entity.pos.z);
                entities.add(dummyEntity.getBukkitEntity());
            });
        }
        return Collections.unmodifiableList(entities);
    }

    @Override
    public int getEntityCount() {
        return structure.entityInfoList.size();
    }

    @Override
    public List<Palette> getPalettes() {
        return structure.palettes.stream().map(CraftPalette::new).collect(Collectors.toList());
    }

    @Override
    public int getPaletteCount() {
        return structure.palettes.size();
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return getHandle().persistentDataContainer;
    }

    public DefinedStructure getHandle() {
        return structure;
    }
}
