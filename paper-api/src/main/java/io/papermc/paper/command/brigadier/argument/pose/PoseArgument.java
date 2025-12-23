package io.papermc.paper.command.brigadier.argument.pose;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Mannequin;
import org.bukkit.entity.Pose;

public class PoseArgument implements CustomArgumentType.Converted<Pose, String> {

    private final Set<Pose> validPoses;

    private PoseArgument(final Set<Pose> poses) {
        validPoses = poses;
    }

    public static PoseArgument pose() {
        return new PoseArgument(new HashSet<>(Arrays.asList(Pose.values())));
    }

    public static PoseArgument mannequin() {
        return new PoseArgument(Mannequin.validPoses());
    }

    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(value -> {
        return MessageComponentSerializer.message().serialize(Component.text(value + " is not a valid pose!"));
    });

    @Override
    public ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public Pose convert(String nativeType) throws CommandSyntaxException {
        try {
            return Pose.valueOf(nativeType.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ignored) {
            throw ERROR_INVALID.create(nativeType);
        }
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        for (Pose pose : this.validPoses) {
            String name = pose.toString().toLowerCase(Locale.ROOT);

            if (name.startsWith(builder.getRemainingLowerCase())) {
                builder.suggest(pose.toString());
            }
        }

        return builder.buildFuture();
    }
}
