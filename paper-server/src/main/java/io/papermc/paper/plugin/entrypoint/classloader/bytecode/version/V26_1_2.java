package io.papermc.paper.plugin.entrypoint.classloader.bytecode.version;

import io.papermc.asm.rules.RewriteRule;
import io.papermc.asm.rules.classes.EnumToInterfaceRule;
import io.papermc.paper.plugin.ApiVersion;
import io.papermc.paper.plugin.entrypoint.classloader.bytecode.VersionedClassloaderBytecodeModifier;
import java.util.Map;
import java.util.stream.Collectors;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.potion.PotionType;

public class V26_1_2 extends VersionedClassloaderBytecodeModifier {

    public static final ApiVersion VERSION = ApiVersion.getOrCreateVersion("26.1.2");

    private static final Map<Class<?>, Class<?>> REGISTRY_TYPES_TO_INTERFACES = Map.of(
        PotionType.class, CraftPotionType.class
    );

    public V26_1_2(final int api) {
        super(api);
    }

    @Override
    protected RewriteRule createRule() {
        return new EnumToInterfaceRule(REGISTRY_TYPES_TO_INTERFACES.entrySet().stream().collect(Collectors.toMap(
            entry -> entry.getKey().describeConstable().orElseThrow(),
            entry -> entry.getValue().describeConstable().orElseThrow()
        )));
    }
}
