package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.pluginremap.reflect.ReflectionRemapper;
import org.bukkit.craftbukkit.util.ApiVersion;

public class PaperClassloaderBytecodeModifier implements ClassloaderBytecodeModifier {

    @Override
    public byte[] modify(PluginMeta configuration, byte[] bytecode) {
        bytecode = ReflectionRemapper.processClass(bytecode);

        ApiVersion version = ApiVersion.getOrCreateVersion(configuration.getAPIVersion());
        if (version.isOlderThanOrSameAs(ApiVersion.CLASS_TO_INTERFACE)) {
            bytecode = ClassToInterfaceRules.processClass(bytecode);
        }
        return bytecode;
    }
}
