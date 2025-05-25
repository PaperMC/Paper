package io.papermc.paper.registry.entry;

import com.mojang.datafixers.util.Either;
import io.papermc.paper.util.MCUtil;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.NamespacedKey;

public final class RegistryTypeMapper<M, A> {

    private static <M, A> Function<Holder<M>, ? extends A> wrap(final BiFunction<? super NamespacedKey, M, ? extends A> byValueCreator) {
        return holder -> {
            if (!(holder instanceof final Holder.Reference<M> reference)) {
                throw new IllegalArgumentException("This type does not support direct holders: " + holder);
            }
            return byValueCreator.apply(MCUtil.fromResourceKey(reference.key()), reference.value());
        };
    }

    final Either<Function<Holder<M>, ? extends A>, BiFunction<? super NamespacedKey, M, ? extends A>> minecraftToBukkit;
    private final boolean supportsDirectHolders;

    public RegistryTypeMapper(final BiFunction<? super NamespacedKey, M, ? extends A> byValueCreator) {
        this.minecraftToBukkit = Either.right(byValueCreator);
        this.supportsDirectHolders = false;
    }

    public RegistryTypeMapper(final Function<Holder<M>, ? extends A> byHolderCreator, final boolean supportsDirectHolders) {
        this.minecraftToBukkit = Either.left(byHolderCreator);
        this.supportsDirectHolders = supportsDirectHolders;
    }

    public A createBukkit(final Holder<M> minecraft) {
        return this.minecraftToBukkit.<Function<Holder<M>, ? extends A>>map(
            Function.identity(),
            RegistryTypeMapper::wrap
        ).apply(minecraft);
    }

    public boolean supportsDirectHolders() {
        return this.supportsDirectHolders;
    }

    public boolean constructorUsesHolder() {
        return this.minecraftToBukkit.left().isPresent();
    }
}
