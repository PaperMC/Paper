package org.bukkit.craftbukkit.entity;

import com.google.common.base.Preconditions;
import java.util.function.Function;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntitySnapshot;
import org.bukkit.entity.EntityType;

public class CraftEntitySnapshot implements EntitySnapshot {
    private final NBTTagCompound data;
    private final EntityType type;

    private CraftEntitySnapshot(NBTTagCompound data, EntityType type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public EntityType getEntityType() {
        return type;
    }

    @Override
    public Entity createEntity(World world) {
        net.minecraft.world.entity.Entity internal = createInternal(world);

        return internal.getBukkitEntity();
    }

    @Override
    public Entity createEntity(Location location) {
        Preconditions.checkArgument(location.getWorld() != null, "Location has no world");

        net.minecraft.world.entity.Entity internal = createInternal(location.getWorld());

        internal.setPos(location.getX(), location.getY(), location.getZ());
        return location.getWorld().addEntity(internal.getBukkitEntity());
    }

    private net.minecraft.world.entity.Entity createInternal(World world) {
        net.minecraft.world.level.World nms = ((CraftWorld) world).getHandle();
        net.minecraft.world.entity.Entity internal = EntityTypes.loadEntityRecursive(data, nms, Function.identity());
        if (internal == null) { // Try creating by type
            internal = CraftEntityType.bukkitToMinecraft(type).create(nms);
        }

        Preconditions.checkArgument(internal != null, "Error creating new entity."); // This should only fail if the stored NBTTagCompound is malformed.
        internal.load(data);

        return internal;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public static CraftEntitySnapshot create(CraftEntity entity) {
        NBTTagCompound tag = new NBTTagCompound();
        if (!entity.getHandle().saveAsPassenger(tag, false)) {
            return null;
        }

        return new CraftEntitySnapshot(tag, entity.getType());
    }

    public static CraftEntitySnapshot create(NBTTagCompound tag, EntityType type) {
        if (tag == null || tag.isEmpty() || type == null) {
            return null;
        }

        return new CraftEntitySnapshot(tag, type);
    }

    public static CraftEntitySnapshot create(NBTTagCompound tag) {
        EntityType type = EntityTypes.by(tag).map(CraftEntityType::minecraftToBukkit).orElse(null);
        return create(tag, type);
    }
}
