package io.papermc.paper.registry.entry;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Either;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.NamespacedKey;

public final class RegistryTypeMapper<M, A> {

    final Either<BiFunction<? super NamespacedKey, M, ? extends A>, Function<Holder<M>, ? extends A>> minecraftToBukkit;

    public RegistryTypeMapper(final BiFunction<? super NamespacedKey, M, ? extends A> byValueCreator) {
        this.minecraftToBukkit = Either.left(byValueCreator);
    }

    public RegistryTypeMapper(final Function<Holder<M>, ? extends A> byHolderCreator) {
        this.minecraftToBukkit = Either.right(byHolderCreator);
    }

    public A createBukkit(final NamespacedKey key, final Holder<M> minecraft) {
        return this.minecraftToBukkit.map(
            minecraftToBukkit -> minecraftToBukkit.apply(key, minecraft.value()),
            minecraftToBukkit -> minecraftToBukkit.apply(minecraft)
        );
    }

    public boolean supportsDirectHolders() {
        return this.minecraftToBukkit.right().isPresent();
    }

    public A convertDirectHolder(final Holder<M> directHolder) {
        Preconditions.checkArgument(this.supportsDirectHolders() && directHolder.kind() == Holder.Kind.DIRECT);
        return this.minecraftToBukkit.right().orElseThrow().apply(directHolder);
    }
}
