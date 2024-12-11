package org.bukkit.craftbukkit.legacy.reroute;

import static org.junit.jupiter.api.Assertions.*;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;

public abstract class AbstractRerouteTest {

    void test(Class<?> testClass, Map<String, RerouteMethodData> dataMap) {
        this.test(testClass, Predicates.alwaysTrue(), dataMap);
    }

    void test(Class<?> testClass, Predicate<String> predicate, Map<String, RerouteMethodData> dataMap) {
        this.test(testClass, Predicates.and(predicate), dataMap.entrySet().stream().map(entry -> new TestDataHolder(entry.getKey(), List.of(entry.getValue()))).toList());
    }

    void test(Class<?> testClass, Predicate<String> predicate, List<TestDataHolder> dataList) {
        Reroute reroute = RerouteBuilder
                .create(predicate)
                .forClass(testClass)
                .build();

        assertEquals(dataList.size(), reroute.rerouteDataMap.size(), String.format("First layer reroute data amount are not the same. Expected: %s, Actual: %s", dataList, reroute.rerouteDataMap));

        for (TestDataHolder testHolder : dataList) {
            Reroute.RerouteDataHolder holder = reroute.rerouteDataMap.get(testHolder.methodKey());
            assertEquals(testHolder.rerouteMethodDataList().size(), holder.rerouteMethodDataMap.size(), String.format("Second layer reroute data amount are not the same. Expected: %s, Actual: %s", testHolder.rerouteMethodDataList, holder.rerouteMethodDataMap));

            for (RerouteMethodData testData : testHolder.rerouteMethodDataList()) {
                RerouteMethodData actual = holder.rerouteMethodDataMap.get(testData.sourceOwner().getInternalName());
                assertNotNull(actual, String.format("No reroute method data found for %s", testData));

                this.check(actual, testData);
            }
        }
    }

    RerouteArgument create(String type, String sourceType, boolean injectPluginName, boolean injectPluginVersion, String injectCompatibility) {
        return new RerouteArgument(Type.getType(type), Type.getType(sourceType), injectPluginName, injectPluginVersion, injectCompatibility);
    }

    List<RerouteArgument> create(RerouteArgument... arguments) {
        return Arrays.asList(arguments);
    }

    RerouteMethodData create(String methodKey, String sourceDesc, String sourceOwner, String sourceName,
                             boolean staticReroute, String targetType, String targetOwner, String targetName,
                             List<RerouteArgument> arguments, String rerouteReturn, boolean isInBukkit,
                             RequirePluginVersionData requiredPluginVersion) {
        return new RerouteMethodData(methodKey, Type.getType(sourceDesc), Type.getObjectType(sourceOwner), sourceName,
                staticReroute, Type.getType(targetType), targetOwner, targetName, arguments, new RerouteReturn(Type.getType(rerouteReturn)), isInBukkit, requiredPluginVersion);
    }

    private void check(RerouteMethodData actual, RerouteMethodData expected) {
        assertEquals(expected.methodKey(), actual.methodKey(), "Method key are not the same");
        assertEquals(expected.sourceDesc(), actual.sourceDesc(), "Source desc are not the same");
        assertEquals(expected.sourceOwner(), actual.sourceOwner(), "Source owner are not the same");
        assertEquals(expected.sourceName(), actual.sourceName(), "Source name are not the same");
        assertEquals(expected.staticReroute(), actual.staticReroute(), "Static reroute flag are not the same");
        assertEquals(expected.targetType(), actual.targetType(), "Target type are not the same");
        assertEquals(expected.targetOwner(), actual.targetOwner(), "Target owner are not the same");
        assertEquals(expected.targetName(), actual.targetName(), "Target name are not the same");
        assertEquals(expected.arguments().size(), actual.arguments().size(), "Arguments count are not the same");
        assertEquals(expected.arguments(), actual.arguments(), "Arguments are not the same");
        assertEquals(expected.rerouteReturn(), actual.rerouteReturn(), "Reroute return flag are not the same");
        assertEquals(expected.isInBukkit(), actual.isInBukkit(), "In Bukkit flag are not the same");
        assertEquals(expected.requiredPluginVersion(), actual.requiredPluginVersion(), "Required plugin version are not the same");
    }

    public record TestDataHolder(String methodKey, List<RerouteMethodData> rerouteMethodDataList) {
    }
}
