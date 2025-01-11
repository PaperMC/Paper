package io.papermc.paper.command.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public interface PaperCommandSourceStack extends CommandSourceStack, BukkitBrigadierCommandSource {

    net.minecraft.commands.CommandSourceStack getHandle();

    @Override
    default @NotNull Location getLocation() {
        Vec2 rot = this.getHandle().getRotation();
        Vec3 pos = this.getHandle().getPosition();
        Level level = this.getHandle().getLevel();

        return new Location(level.getWorld(), pos.x, pos.y, pos.z, rot.y, rot.x);
    }

    @Override
    @NotNull
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
    @NonNull
    default CommandSourceStack withExecutor(@Nullable Entity executor) {
        return executor == null ? this.getHandle().withEntity(null) : this.getHandle().withEntity(((CraftEntity) executor).getHandle());
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
