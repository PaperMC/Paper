package org.bukkit.craftbukkit.legacy.reroute;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;

public record RerouteMethodData(String source, Type sourceDesc, Type sourceOwner, String sourceName,
                                boolean staticReroute, Type targetType, String targetOwner, String targetName,
                                List<RerouteArgument> arguments, RerouteReturn rerouteReturn, boolean isInBukkit,
                                @Nullable String requiredCompatibility) {
}
