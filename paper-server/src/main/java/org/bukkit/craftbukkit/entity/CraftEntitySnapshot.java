package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.function.Function;
import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;
import org.slf4j.Logger;

public class CraftEntitySnapshot implements EntitySnapshot {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final CompoundTag data;
    private final EntityType type;

    private CraftEntitySnapshot(CompoundTag data, EntityType type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public EntityType getEntityType() {
        return this.type;
    }

    @Override
    public Entity createEntity(World world) {
        net.minecraft.world.entity.Entity internal = this.createInternal(world);

        return internal.getBukkitEntity();
    }

    @Override
    public Entity createEntity(Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Location has no world");

        net.minecraft.world.entity.Entity internal = this.createInternal(location.getWorld());

        internal.setPos(location.getX(), location.getY(), location.getZ());
        return location.getWorld().addEntity(internal.getBukkitEntity());
    }

    @Override
    public String getAsString() {
        return this.data.toString();
    }

    private net.minecraft.world.entity.Entity createInternal(World world) {
        net.minecraft.world.level.Level level = ((CraftWorld) world).getHandle();
        net.minecraft.world.entity.Entity internal = net.minecraft.world.entity.EntityType.loadEntityRecursive(this.data, level, EntitySpawnReason.LOAD, Function.identity());
        if (internal == null) { // Try creating by type
            internal = CraftEntityType.bukkitToMinecraft(this.type).create(level, EntitySpawnReason.LOAD);
        }

        Preconditions.checkArgument(internal != null, "Error creating new entity."); // This should only fail if the stored CompoundTag is malformed.
        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "EntitySnapshot#createEntity", LOGGER
        )) {
            internal.load(TagValueInput.createGlobal(problemReporter, this.data));
        }

        return internal;
    }

    public CompoundTag getData() {
        return this.data;
    }

    public static CraftEntitySnapshot create(CraftEntity entity) {
        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "create@" + entity.getUniqueId(), LOGGER
        )) {
            final TagValueOutput output = TagValueOutput.createWithContext(
                problemReporter,
                CraftRegistry.getMinecraftRegistry()
            );
            if (!entity.getHandle().saveAsPassenger(output, false, false, false)) {
                return null;
            }

            return new CraftEntitySnapshot(output.buildResult(), entity.getType());
        }
    }

    public static CraftEntitySnapshot create(CompoundTag tag, EntityType type) {
        if (tag == null || tag.isEmpty() || type == null) {
            return null;
        }

        return new CraftEntitySnapshot(tag, type);
    }

    public static CraftEntitySnapshot create(CompoundTag tag) {
        try (final ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(
            () -> "create", LOGGER
        )) {
            EntityType type = net.minecraft.world.entity.EntityType.by(
                TagValueInput.createGlobal(problemReporter, tag)
            ).map(CraftEntityType::minecraftToBukkit).orElse(null);
            return CraftEntitySnapshot.create(tag, type);
        }
    }
}
