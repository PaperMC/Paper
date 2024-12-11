package io.papermc.paper.entity;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import net.minecraft.world.entity.Shearable;
import org.bukkit.support.environment.Normal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Normal
class ShearableDropsTest {

    static Iterable<ClassInfo> parameters() {
        try (ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .enableMethodInfo()
            .whitelistPackages("net.minecraft")
            .scan()
        ) {
            return new ArrayList<>(scanResult.getClassesImplementing(Shearable.class.getName()));
        }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    void checkShearableDropOverrides(final ClassInfo classInfo) {
        final MethodInfoList generateDefaultDrops = classInfo.getDeclaredMethodInfo("generateDefaultDrops");
        assertEquals(1, generateDefaultDrops.size(), classInfo.getName() + " doesn't implement Shearable#generateDefaultDrops");
    }
}
