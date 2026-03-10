package io.papermc.paper.plugin.entrypoint.classloader.bytecode;

import com.google.common.collect.Iterators;
import io.papermc.paper.plugin.ApiVersion;
import io.papermc.paper.plugin.configuration.PluginMeta;
import io.papermc.paper.plugin.entrypoint.classloader.ClassloaderBytecodeModifier;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.util.Util;
import org.objectweb.asm.Opcodes;

public class PaperClassloaderBytecodeModifier implements ClassloaderBytecodeModifier {

    private static final Map<ApiVersion, List<ModifierFactory>> MODIFIERS = Util.make(new LinkedHashMap<>(), map -> {
    });

    private final Map<ApiVersion, List<VersionedClassloaderBytecodeModifier>> constructedModifiers = MODIFIERS.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
            return entry.getValue().stream().map(factory -> factory.create(Opcodes.ASM9)).toList();
        }));

    @Override
    public byte[] modify(final PluginMeta configuration, byte[] bytecode) {
        int start = -1;
        if (configuration.getAPIVersion() != null) {
            int i = 0;
            for (final Map.Entry<ApiVersion, List<VersionedClassloaderBytecodeModifier>> entry : this.constructedModifiers.entrySet()) {
                final ApiVersion apiVersion = ApiVersion.getOrCreateVersion(configuration.getAPIVersion());
                final ApiVersion modifierApiVersion = entry.getKey();
                if (apiVersion.isOlderThanOrSameAs(modifierApiVersion)) {
                    start = i;
                    break;
                }
                i++;
            }
        } else {
            start = 0;
        }
        if (start == -1) {
            return bytecode; // no modification needed. The plugin version is newer than all versioned modifiers
        }

        final Iterator<Map.Entry<ApiVersion, List<VersionedClassloaderBytecodeModifier>>> iter = this.constructedModifiers.entrySet().iterator();
        Iterators.advance(iter, start);
        while (iter.hasNext()) {
            for (final VersionedClassloaderBytecodeModifier modifier : iter.next().getValue()) {
                bytecode = modifier.modify(configuration, bytecode);
            }
        }
        return bytecode;
    }

    private interface ModifierFactory {

        VersionedClassloaderBytecodeModifier create(int api);
    }
}
