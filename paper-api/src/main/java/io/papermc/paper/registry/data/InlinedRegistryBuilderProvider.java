package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilderFactory;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.function.Consumer;
import org.bukkit.Art;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface InlinedRegistryBuilderProvider {

    static InlinedRegistryBuilderProvider instance() {
        class Holder {
            static final Optional<InlinedRegistryBuilderProvider> INSTANCE = ServiceLoader.load(InlinedRegistryBuilderProvider.class).findFirst();
        }
        return Holder.INSTANCE.orElseThrow();
    }

}
