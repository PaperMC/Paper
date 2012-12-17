package org.bukkit.craftbukkit.inventory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.Overridden;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ItemMetaImplementationOverrideTest {
    static final Class<CraftMetaItem> parent = CraftMetaItem.class;
    static final Class<Overridden> annotation = Overridden.class;

    static final List<Object[]> testData = new ArrayList<Object[]>();
    static final Method[] methods;

    static final Class<? extends CraftMetaItem>[] subclasses;

    static {
        List<Class<? extends CraftMetaItem>> classes = new ArrayList<Class<? extends CraftMetaItem>>();

        for (Material material : ItemStackTest.COMPOUND_MATERIALS) {
            Class<? extends CraftMetaItem> clazz = CraftItemFactory.instance().getItemMeta(material).getClass().asSubclass(parent);
            if (clazz != parent) {
                classes.add(clazz);
            }
        }
        subclasses = classes.toArray(new Class[0]);


        List<Method> list = new ArrayList<Method>();

        for (Method method: parent.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                list.add(method);
            }
        }

        for (Class<?> clazz : subclasses) {
            for (Method method : list) {
                testData.add(new Object[]{clazz, method, clazz.getSimpleName() + " contains " + method.getName()});
            }
        }

        methods = list.toArray(new Method[list.size()]);
    }

    @Parameters(name="[{index}]:{2}")
    public static List<Object[]> data() {
        return testData;
    }

    @Parameter(0) public Class clazz;
    @Parameter(1) public Method method;
    @Parameter(2) public String name;

    @Test
    public void testClass() throws Throwable {
        clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
    }
}
