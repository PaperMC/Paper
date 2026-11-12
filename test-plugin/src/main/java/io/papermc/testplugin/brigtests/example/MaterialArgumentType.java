package io.papermc.testplugin.brigtests.example;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;

import static net.kyori.adventure.text.Component.translatable;

public final class MaterialArgumentType implements CustomArgumentType.Converted<Material, NamespacedKey> {

    private static final ComponentCommandExceptionType ERROR_INVALID = new ComponentCommandExceptionType(translatable("argument.id.invalid"));

    private final Predicate<Material> check;

    private MaterialArgumentType(Predicate<Material> check) {
        this.check = check;
    }

    public static MaterialArgumentType item() {
        return new MaterialArgumentType(Material::isItem);
    }

    public static MaterialArgumentType block() {
        return new MaterialArgumentType(Material::isBlock);
    }

    @Override
    public Material convert(final NamespacedKey nativeType) throws CommandSyntaxException {
        final Material material = Registry.MATERIAL.get(nativeType);
        if (material == null) {
            throw ERROR_INVALID.create();
        }
        if (!this.check.test(material)) {
            throw ERROR_INVALID.create();
        }
        return material;
    }

    static boolean matchesSubStr(String remaining, String candidate) {
        for (int i = 0; !candidate.startsWith(remaining, i); ++i) {
            i = candidate.indexOf('_', i);
            if (i < 0) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ArgumentType<NamespacedKey> getNativeType() {
        return ArgumentTypes.namespacedKey();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(final CommandContext<S> context, final SuggestionsBuilder builder) {
        final Stream<Material> stream = StreamSupport.stream(Registry.MATERIAL.spliterator(), false);
        final String remaining = builder.getRemaining();
        boolean containsColon = remaining.indexOf(':') > -1;
        stream.filter(this.check)
            .map(Keyed::key)
            .forEach(key -> {
                final String keyAsString = key.asString();
                if (containsColon) {
                    if (matchesSubStr(remaining, keyAsString)) {
                        builder.suggest(keyAsString);
                    }
                } else if (matchesSubStr(remaining, key.namespace()) || "minecraft".equals(key.namespace()) && matchesSubStr(remaining, key.value())) {
                    builder.suggest(keyAsString);
                }
            });
        return builder.buildFuture();
    }

}
