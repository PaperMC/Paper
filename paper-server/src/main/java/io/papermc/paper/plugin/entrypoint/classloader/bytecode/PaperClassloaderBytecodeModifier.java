package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.pluginremap.reflect.ReflectionRemapper;

// Stub, implement in future.
public class PaperClassloaderBytecodeModifier implements ClassloaderBytecodeModifier {

    @Override
    public byte[] modify(PluginMeta configuration, byte[] bytecode) {
        bytecode = ReflectionRemapper.processClass(bytecode);
        bytecode = ClassToInterfaceRules.processClass(bytecode);
        return bytecode;
    }
}
