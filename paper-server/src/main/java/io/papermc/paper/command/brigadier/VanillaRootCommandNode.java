package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.BaseCommandBlock;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The root node of the vanilla dispatcher.
 * <p>
 * Prioritizes Minecraft commands (the {@code minecraft:}-prefixed copies of vanilla commands, see
 * {@link MinecraftNamespacedLiteralCommandNode}) over commands registered by plugins under the same name
 * when parsing functions, and for command blocks when configured through {@code command-block-overrides}.
 *
 * @param <S> the source type, in practice always {@link CommandSourceStack}; kept generic because callers
 *            (and tests) do parse with raw, unrelated source objects
 */
@NullMarked
public final class VanillaRootCommandNode<S> extends RootCommandNode<S> {

    @Override
    protected @Nullable LiteralCommandNode<S> findRelevantLiteral(final String word, final @Nullable S source) {
        if (source instanceof final CommandSourceStack stack && !word.contains(":") && prefersMinecraftCommands(stack, word)) {
            final LiteralCommandNode<S> minecraftLiteral = this.getLiteral(MinecraftNamespacedLiteralCommandNode.PREFIX + word);
            if (minecraftLiteral != null) {
                return minecraftLiteral;
            }
        }
        return super.findRelevantLiteral(word, source);
    }

    private static boolean prefersMinecraftCommands(final CommandSourceStack source, final String word) {
        if (source.source == CommandSource.NULL) {
            return true; // functions
        }
        return source.source instanceof BaseCommandBlock.CloseableCommandBlockSource
            && source.getServer().server.getCommandBlockOverride(word);
    }
}
