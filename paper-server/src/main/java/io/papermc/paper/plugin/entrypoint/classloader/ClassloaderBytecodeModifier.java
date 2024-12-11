package io.papermc.paper.plugin.entrypoint.classloader;

import io.papermc.paper.plugin.configuration.PluginMeta;
import net.kyori.adventure.util.Services;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ClassloaderBytecodeModifier {

    static ClassloaderBytecodeModifier bytecodeModifier() {
        return Provider.INSTANCE;
    }

    byte[] modify(PluginMeta config, byte[] bytecode);

    class Provider {

        private static final ClassloaderBytecodeModifier INSTANCE = Services.service(ClassloaderBytecodeModifier.class).orElseThrow();

    }

}
