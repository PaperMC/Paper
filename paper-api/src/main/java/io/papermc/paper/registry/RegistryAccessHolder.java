package io.papermc.paper.registry;

import java.util.Optional;
import java.util.ServiceLoader;

final class RegistryAccessHolder {

    static final Optional<RegistryAccess> INSTANCE = ServiceLoader.load(RegistryAccess.class, RegistryAccess.class.getClassLoader()).findFirst();

    private RegistryAccessHolder() {
    }
}
