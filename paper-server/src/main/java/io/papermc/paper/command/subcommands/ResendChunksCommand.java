package io.papermc.paper.command.subcommands;

import io.papermc.paper.command.PaperSubcommand;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.world.level.ChunkPos;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

@DefaultQualifier(NonNull.class)
public final class ResendChunksCommand implements PaperSubcommand {
    @Override
    public boolean execute(final CommandSender sender, final String subCommand, final String[] args) {
        if (sender instanceof Player player) {
            final ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
            PlayerChunkSender chunkSender = serverPlayer.connection.chunkSender;
            int resent = 0;

            for (ChunkPos chunkPos : serverPlayer.getBukkitEntity().getSentChunks().stream().map(ResendChunksCommand::bukkitChunk2ChunkPos).collect(Collectors.toSet())) {
                chunkSender.dropChunk(serverPlayer, chunkPos);
                PlayerChunkSender.sendChunk(serverPlayer.connection, serverPlayer.level(), serverPlayer.level().getChunk(chunkPos.x, chunkPos.z));
                resent++;
            }
            serverPlayer.sendSystemMessage(Component.literal("Resent " + resent + " chunks to client"));
        } else {
            sender.sendMessage("Only a player can execute this command.");
        }
        return true;
    }

    public static @NotNull ChunkPos bukkitChunk2ChunkPos(@NotNull Chunk chunk) {
        return new ChunkPos(chunk.getX(), chunk.getZ());
    }
}
