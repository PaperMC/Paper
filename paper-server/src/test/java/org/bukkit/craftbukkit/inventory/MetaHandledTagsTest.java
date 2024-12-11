package org.bukkit.craftbukkit.inventory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// in cb package because of package-private stuff
@AllFeatures
class MetaHandledTagsTest {

    @Test
    public void checkAllMetasHaveHandledTags() {
        try (final ScanResult result = new ClassGraph()
            .whitelistPackages("org.bukkit.craftbukkit.inventory")
            .enableClassInfo().scan()) {
            final ClassInfoList subclasses = result.getSubclasses(CraftMetaItem.class.getName());
            assertFalse(subclasses.isEmpty(), "found 0 sub types");
            for (final ClassInfo subclass : subclasses) {
                final Class<CraftMetaItem> clazz = subclass.loadClass(CraftMetaItem.class);
                CraftMetaItem.getTopLevelHandledDcts(clazz); // load into map
                assertTrue(CraftMetaItem.HANDLED_DCTS_PER_TYPE.containsKey(clazz), subclass.getName() + " not found in handled tags map");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
