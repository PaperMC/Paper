package io.papermc.paper.commands;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.ServerCommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class FeedbackForwardingSender extends ServerCommandSender {
    private final Consumer<? super Component> feedback;
    private final CraftServer server;

    public FeedbackForwardingSender(final Consumer<? super Component> feedback, final CraftServer server) {
        super(((ServerCommandSender) server.getConsoleSender()).perm);
        this.server = server;
        this.feedback = feedback;
    }

    @Override
    public void sendMessage(final String message) {
        this.sendMessage(LegacyComponentSerializer.legacySection().deserialize(message));
    }

    @Override
    public void sendMessage(final String... messages) {
        for (final String message : messages) {
            this.sendMessage(message);
        }
    }

    @Override
    public void sendMessage(final Identity identity, final Component message, final MessageType type) {
        this.feedback.accept(message);
    }

    @Override
    public String getName() {
        return "FeedbackForwardingSender";
    }

    @Override
    public Component name() {
        return Component.text(this.getName());
    }

    @Override
    public boolean isOp() {
        return true;
    }

    @Override
    public void setOp(final boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of " + this.getClass().getName());
    }

    public CommandSourceStack asVanilla() {
        final @Nullable ServerLevel overworld = this.server.getServer().overworld();
        return new CommandSourceStack(
            new Source(this),
            overworld == null ? Vec3.ZERO : Vec3.atLowerCornerOf(overworld.getSharedSpawnPos()),
            Vec2.ZERO,
            overworld,
            4,
            this.getName(),
            net.minecraft.network.chat.Component.literal(this.getName()),
            this.server.getServer(),
            null
        );
    }

    private record Source(FeedbackForwardingSender sender) implements CommandSource {
        @Override
        public void sendSystemMessage(final net.minecraft.network.chat.Component message) {
            this.sender.sendMessage(Identity.nil(), PaperAdventure.asAdventure(message));
        }

        @Override
        public boolean acceptsSuccess() {
            return true;
        }

        @Override
        public boolean acceptsFailure() {
            return true;
        }

        @Override
        public boolean shouldInformAdmins() {
            return false;
        }

        @Override
        public CommandSender getBukkitSender(final CommandSourceStack stack) {
            return this.sender;
        }
    }
}
