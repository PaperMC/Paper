package io.papermc.paper.command.brigadier;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.google.common.base.Preconditions;
import io.papermc.paper.adventure.PaperAdventure;
import net.kyori.adventure.text.ComponentLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static java.util.Objects.requireNonNull;

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
    default @NonNull CommandSender getSender() {
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
    default @NonNull CommandSourceStack withExecutor(@NonNull Entity executor) {
        Preconditions.checkNotNull(executor, "Executor cannot be null.");
        return this.getHandle().withEntity(((CraftEntity) executor).getHandle());
    }

    @Override
    default void sendToTarget(final @NonNull ComponentLike message) {
        requireNonNull(message, "message");
        this.getHandle().sendSystemMessage(PaperAdventure.asVanilla(message.asComponent()));
    }

    @Override
    default void sendSuccess(final @NonNull ComponentLike message, final boolean allowInformingAdmins) {
        requireNonNull(message, "message");
        this.getHandle().sendSuccess(() -> PaperAdventure.asVanilla(message.asComponent()), allowInformingAdmins);
    }

    @Override
    default void sendFailure(final @NonNull ComponentLike message) {
        requireNonNull(message, "message");
        this.getHandle().sendFailure(PaperAdventure.asVanilla(message.asComponent()), false);
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
