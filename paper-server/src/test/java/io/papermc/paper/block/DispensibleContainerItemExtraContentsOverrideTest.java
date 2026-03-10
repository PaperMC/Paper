package io.papermc.paper.block;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.MethodInfoList;
import io.github.classgraph.MethodParameterInfo;
import io.github.classgraph.ScanResult;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.world.item.DispensibleContainerItem;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DispensibleContainerItemExtraContentsOverrideTest {

    public static Stream<ClassInfo> parameters() {
        final List<ClassInfo> classInfo = new ArrayList<>();
        try (final ScanResult scanResult = new ClassGraph()
            .enableClassInfo()
            .enableMethodInfo()
            .whitelistPackages("net.minecraft")
            .scan()
        ) {
            final ClassInfoList classesImplementing = scanResult.getClassesImplementing(DispensibleContainerItem.class.getName());
            for (final ClassInfo info : classesImplementing) {
                if (info.hasDeclaredMethod("checkExtraContent")) {
                    classInfo.add(info);
                }
            }
        }
        return classInfo.stream();
    }

    @ParameterizedTest
    @MethodSource("parameters")
    public void checkCheckExtraContentOverride(final ClassInfo implementsDispensibleContainerItem) {
        final MethodInfoList checkExtraContent = implementsDispensibleContainerItem.getDeclaredMethodInfo("checkExtraContent");
        assertEquals(1, checkExtraContent.size(), implementsDispensibleContainerItem.getName() + " has multiple checkExtraContent methods");
        final MethodInfo next = checkExtraContent.iterator().next();
        final MethodParameterInfo[] parameterInfo = next.getParameterInfo();
        assertEquals(6, parameterInfo.length, implementsDispensibleContainerItem.getName() + " doesn't have 6 params for checkExtraContent");
        assertEquals("InteractionHand", parameterInfo[parameterInfo.length - 2].getTypeDescriptor().toStringWithSimpleNames(), implementsDispensibleContainerItem.getName() + " needs to change its override of checkExtraContent");
        assertEquals("Direction", parameterInfo[parameterInfo.length - 1].getTypeDescriptor().toStringWithSimpleNames(), implementsDispensibleContainerItem.getName() + " needs to change its override of checkExtraContent");
    }
}
