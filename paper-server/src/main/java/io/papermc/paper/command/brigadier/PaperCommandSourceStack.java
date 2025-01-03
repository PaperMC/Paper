package io.papermc.paper.command.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.google.common.base.Preconditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public interface PaperCommandSourceStack extends CommandSourceStack, BukkitBrigadierCommandSource {

    net.minecraft.commands.CommandSourceStack getHandle();

    @Override
    default @NonNull Location getLocation() {
        Vec2 rot = this.getHandle().getRotation();
        Vec3 pos = this.getHandle().getPosition();
        Level level = this.getHandle().getLevel();

        return new Location(level.getWorld(), pos.x, pos.y, pos.z, rot.y, rot.x);
    }

    @Override
    @NonNull
    default CommandSender getSender() {
        return this.getHandle().getBukkitSender();
    }

    @Override
    @Nullable
    default Entity getExecutor() {
        net.minecraft.world.entity.Entity nmsEntity = this.getHandle().getEntity();
        if (nmsEntity == null) {
            return null;
        }

        return nmsEntity.getBukkitEntity();
    }

    @Override
    default CommandSourceStack withExecutor(@NonNull Entity executor) {
        Preconditions.checkNotNull(executor, "Executor cannot be null.");
        return this.getHandle().withEntity(((CraftEntity) executor).getHandle());
    }

    // OLD METHODS
    @Override
    default org.bukkit.entity.Entity getBukkitEntity() {
        return this.getExecutor();
    }

    @Override
    default org.bukkit.World getBukkitWorld() {
        return this.getLocation().getWorld();
    }

    @Override
    default org.bukkit.Location getBukkitLocation() {
        return this.getLocation();
    }

    @Override
    default CommandSender getBukkitSender() {
        return this.getSender();
    }
}
