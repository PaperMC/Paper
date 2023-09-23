package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Stream;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.Overridden;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class ItemMetaImplementationOverrideTest {
    static final Class<CraftMetaItem> parent = CraftMetaItem.class;

    public static Stream<Arguments> data() {
        final List<Arguments> testData = new ArrayList<>();
        List<Class<? extends CraftMetaItem>> classes = new ArrayList<Class<? extends CraftMetaItem>>();

        for (Material material : ItemStackTest.COMPOUND_MATERIALS) {
            Class<? extends CraftMetaItem> clazz = CraftItemFactory.instance().getItemMeta(material).getClass().asSubclass(parent);
            if (clazz != parent) {
                classes.add(clazz);
            }
        }

        List<Method> list = new ArrayList<Method>();

        for (Method method: parent.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Overridden.class)) {
                list.add(method);
            }
        }

        for (final Class<?> clazz : classes) {
            for (final Method method : list) {
                testData.add(
                    Arguments.of(
                        new Callable<Method>() {
                            @Override
                            public Method call() throws Exception {
                                return clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
                            }
                        },
                        clazz.getSimpleName() + " contains " + method.getName()
                    )
                );
            }

            testData.add(
                    Arguments.of(
                    new Callable<DelegateDeserialization>() {
                        @Override
                        public DelegateDeserialization call() throws Exception {
                            return clazz.getAnnotation(DelegateDeserialization.class);
                        }
                    },
                    clazz.getSimpleName() + " contains annotation " + DelegateDeserialization.class
                    )
            );
        }

        return testData.stream();
    }

    @ParameterizedTest
    @MethodSource("data")
    public void testClass(Callable<?> test, String name) throws Throwable {
        assertThat(test.call(), is(not(nullValue())), name);
    }
}
