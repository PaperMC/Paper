package io.papermc.paper.brigadier.arguments;

import com.destroystokyo.paper.brigadier.BukkitBrigadierCommandSource;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.brigadier.MinecraftArgumentType;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public abstract class SimpleArgument<P> extends MinecraftArgumentType {

    private final Class<P> paperType;

    protected SimpleArgument(String id, Class<P> paperType) {
        super(id);
        this.paperType = paperType;
    }

    public abstract ArgumentType<P> create();

    public abstract P getFromContext(@NotNull CommandContext<BukkitBrigadierCommandSource> context, @NotNull String name);

    public @NotNull Class<P> getPaperType() {
        return paperType;
    }

    public static <P> Wrapper<P> wrapper(@NotNull String id, Class<P> paperType) {
        return new Wrapper<>(id, paperType);
    }

    public static class Wrapper<P> extends SimpleArgument<P> {

        private SimpleArgument<P> delegate;

        private Wrapper(String id, Class<P> paperType) {
            super(id, paperType);
        }

        @Override
        public ArgumentType<P> create() {
            checkDelegate();
            return this.delegate.create();
        }

        @Override
        public P getFromContext(@NotNull CommandContext<BukkitBrigadierCommandSource> context, @NotNull String name) {
            checkDelegate();
            return this.delegate.getFromContext(context, name);
        }

        private void checkDelegate() {
            if (this.delegate == null) {
                this.delegate = (SimpleArgument<P>) ARGUMENT_TYPE_MAP.get(this.getKey());
                if (this.delegate == null) {
                    throw new IllegalStateException("delegate was null for " + this.getKey());
                }
            }
        }
    }
}
