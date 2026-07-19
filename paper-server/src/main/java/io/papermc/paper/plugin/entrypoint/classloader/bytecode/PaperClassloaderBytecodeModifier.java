package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import io.papermc.paper.plugin.configuration.PluginMeta;
import org.bukkit.craftbukkit.util.ApiVersion;

public class PaperClassloaderBytecodeModifier implements ClassloaderBytecodeModifier {

    @Override
    public byte[] modify(PluginMeta configuration, byte[] bytecode) {
        ApiVersion version = ApiVersion.getOrCreateVersion(configuration.getAPIVersion());
        if (version.isOlderThanOrSameAs(ApiVersion.CLASS_TO_INTERFACE)) {
            bytecode = EntityTypeRules.processClass(bytecode);
        }
        return bytecode;
    }
}
