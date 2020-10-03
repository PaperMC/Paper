package net.minecraft.server;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public interface ContainerAccess {

    ContainerAccess a = new ContainerAccess() {
        @Override
        public <T> Optional<T> a(BiFunction<World, BlockPosition, T> bifunction) {
            return Optional.empty();
        }
    };

    static ContainerAccess at(final World world, final BlockPosition blockposition) {
        return new ContainerAccess() {
            @Override
            public <T> Optional<T> a(BiFunction<World, BlockPosition, T> bifunction) {
                return Optional.of(bifunction.apply(world, blockposition));
            }
        };
    }

    <T> Optional<T> a(BiFunction<World, BlockPosition, T> bifunction);

    default <T> T a(BiFunction<World, BlockPosition, T> bifunction, T t0) {
        return this.a(bifunction).orElse(t0);
    }

    default void a(BiConsumer<World, BlockPosition> biconsumer) {
        this.a((world, blockposition) -> {
            biconsumer.accept(world, blockposition);
            return Optional.empty();
        });
    }
}
