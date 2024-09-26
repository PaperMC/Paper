package org.bukkit.craftbukkit.inventory;

import static org.bukkit.support.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import org.bukkit.Material;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

@AllFeatures
public class ItemMetaCloneTest {

    @Test
    public void testClone() throws Throwable {
        for (Material material : ItemStackTest.COMPOUND_MATERIALS) {
            Class<?> clazz = CraftItemFactory.instance().getItemMeta(material).getClass();

            Method clone = clazz.getDeclaredMethod("clone");
            assertNotNull(clone, "Class " + clazz + " does not override clone()");
            assertThat(clone.getReturnType(), is(equalTo(clazz)), "Class " + clazz + " clone return type does not match");
        }
    }
}
