package org.bukkit.craftbukkit.inventory;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.lang.reflect.Method;
import org.bukkit.Material;
import org.junit.Test;

public class ItemMetaCloneTest {

    @Test
    public void testClone() throws Throwable {
        for (Material material : ItemStackTest.COMPOUND_MATERIALS) {
            Class<?> clazz = CraftItemFactory.instance().getItemMeta(material).getClass();

            Method clone = clazz.getDeclaredMethod("clone");
            assertNotNull("Class " + clazz + " does not override clone()", clone);
            assertThat("Class " + clazz + " clone return type does not match", clone.getReturnType(), is(equalTo(clazz)));
        }
    }
}
