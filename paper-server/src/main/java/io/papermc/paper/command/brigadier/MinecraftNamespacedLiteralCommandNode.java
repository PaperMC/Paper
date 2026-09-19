package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import org.jspecify.annotations.NullMarked;

/**
 * The {@code minecraft:}-prefixed copy of a vanilla command's root literal.
 * <p>
 * Besides its own literal, this node also parses the un-prefixed spelling so that
 * {@link VanillaRootCommandNode} can hand it un-prefixed input from functions and command blocks.
 */
@NullMarked
public final class MinecraftNamespacedLiteralCommandNode extends LiteralCommandNode<CommandSourceStack> {

    public static final String PREFIX = "minecraft:";

    private final String nonPrefixed;

    private MinecraftNamespacedLiteralCommandNode(final String literal, final LiteralCommandNode<CommandSourceStack> source) {
        super(literal, source.getCommand(), source.getRequirement(), source.getContextRequirement(), source.getRedirect(), source.getRedirectModifier(), source.isFork());
        if (!literal.startsWith(PREFIX)) {
            throw new IllegalArgumentException("Literal '" + literal + "' is not prefixed with " + PREFIX);
        }
        this.nonPrefixed = literal.substring(PREFIX.length());
    }

    /**
     * Creates a full copy of the given vanilla command node under the {@code minecraft:} namespace,
     * sharing its children.
     */
    public static MinecraftNamespacedLiteralCommandNode copyOf(final LiteralCommandNode<CommandSourceStack> source) {
        final MinecraftNamespacedLiteralCommandNode copy = new MinecraftNamespacedLiteralCommandNode(PREFIX + source.getLiteral(), source);
        for (final CommandNode<CommandSourceStack> child : source.getChildren()) {
            copy.addChild(child);
        }
        return copy;
    }

    /**
     * Creates a flattened redirect to the given vanilla command node under the {@code minecraft:} namespace
     * (the legacy behavior, used by CommandArgumentUpgrader).
     */
    public static MinecraftNamespacedLiteralCommandNode redirectTo(final LiteralCommandNode<CommandSourceStack> source) {
        CommandNode<CommandSourceStack> flattenedTarget = source;
        while (flattenedTarget.getRedirect() != null) {
            flattenedTarget = flattenedTarget.getRedirect();
        }
        final LiteralCommandNode<CommandSourceStack> template = LiteralArgumentBuilder.<CommandSourceStack>literal(PREFIX + source.getLiteral())
            .executes(flattenedTarget.getCommand())
            .requires(flattenedTarget.getRequirement())
            .redirect(flattenedTarget)
            .build();
        return new MinecraftNamespacedLiteralCommandNode(template.getLiteral(), template);
    }

    @Override
    public void parse(final StringReader reader, final CommandContextBuilder<CommandSourceStack> contextBuilder) throws CommandSyntaxException {
        final int start = reader.getCursor();
        int end = matchLiteral(reader, this.getLiteral());
        if (end == -1) {
            end = matchLiteral(reader, this.nonPrefixed);
        }
        if (end > -1) {
            contextBuilder.withNode(this, StringRange.between(start, end));
            return;
        }

        throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(reader, this.getLiteral());
    }
}
