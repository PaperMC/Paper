package io.papermc.paper.world.block;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.MethodParameterInfo;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.level.block.Block;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
public class BlockPlayerDestroyOverrideTest {

    public static Stream<ClassInfo> parameters() {
        final List<ClassInfo> classInfo = new ArrayList<>();
        try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .enableMethodInfo()
            .whitelistPackages("net.minecraft")
            .scan()
        ) {
            for (final ClassInfo subclass : scanResult.getSubclasses(Block.class.getName())) {
                final MethodInfoList playerDestroy = subclass.getDeclaredMethodInfo("playerDestroy");
                if (!playerDestroy.isEmpty()) {
                    classInfo.add(subclass);
                }
            }
        }
        return classInfo.stream();
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void checkPlayerDestroyOverrides(ClassInfo overridesPlayerDestroy) {
        final MethodInfoList playerDestroy = overridesPlayerDestroy.getDeclaredMethodInfo("playerDestroy");
        assertEquals(1, playerDestroy.size(), overridesPlayerDestroy.getName() + " has multiple playerDestroy methods");
        final MethodInfo next = playerDestroy.iterator().next();
        final MethodParameterInfo[] parameterInfo = next.getParameterInfo();
        assertEquals("boolean", parameterInfo[parameterInfo.length - 1].getTypeDescriptor().toStringWithSimpleNames(), overridesPlayerDestroy.getName() + " needs to change its override of playerDestroy");
    }
}
