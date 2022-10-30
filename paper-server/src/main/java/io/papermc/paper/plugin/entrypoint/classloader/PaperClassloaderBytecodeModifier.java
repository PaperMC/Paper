package io.papermc.paper.plugin.entrypoint.classloader;

import io.papermc.paper.plugin.configuration.PluginMeta;

// Stub, implement in future.
public class PaperClassloaderBytecodeModifier implements ClassloaderBytecodeModifier {

    @Override
    public byte[] modify(PluginMeta configuration, byte[] bytecode) {
        return io.papermc.paper.pluginremap.reflect.ReflectionRemapper.processClass(bytecode);
    }
}
