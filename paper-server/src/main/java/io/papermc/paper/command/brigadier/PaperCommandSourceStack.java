package io.papermc.paper.command.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.google.common.base.Preconditions;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PaperCommandSourceStack extends CommandSourceStack, BukkitBrigadierCommandSource {

    net.minecraft.commands.CommandSourceStack getHandle();

    @Override
    default Location getLocation() {
        Vec2 rot = this.getHandle().getRotation();
        Vec3 pos = this.getHandle().getPosition();
        Level level = this.getHandle().getLevel();

        return new Location(level.getWorld(), pos.x, pos.y, pos.z, rot.y, rot.x);
    }

    @Override
    default CommandSender getSender() {
        return this.getHandle().getBukkitSender();
    }

    @Override
    default @Nullable Entity getExecutor() {
        net.minecraft.world.entity.Entity nmsEntity = this.getHandle().getEntity();
        if (nmsEntity == null) {
            return null;
        }

        return nmsEntity.getBukkitEntity();
    }

    @Override
    default CommandSourceStack withExecutor(Entity executor) {
        Preconditions.checkNotNull(executor, "Executor cannot be null.");
        return this.getHandle().withEntity(((CraftEntity) executor).getHandle());
    }

    @Override
    default Player getPlayerOrThrow() throws CommandSyntaxException {
        return this.getHandle().getPlayerOrException().getBukkitEntity();
    }

    @Override
    default Entity getEntityOrThrow() throws CommandSyntaxException {
        return this.getHandle().getEntityOrException().getBukkitEntity();
    }

    // OLD METHODS
    @Override
    default @Nullable Entity getBukkitEntity() {
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
