package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.IAttribute;
import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class ItemFactoryTest extends AbstractTestingBase {

    @Test
    public void testKnownAttributes() throws Throwable {
        final ZipFile nmsZipFile = new ZipFile(CommandAbstract.class /* Magic class that isn't imported! */.getProtectionDomain().getCodeSource().getLocation().getFile());
        final Collection<String> names = new HashSet<String>();
        for (final ZipEntry clazzEntry : Collections.list(nmsZipFile.entries())) {
            final String entryName = clazzEntry.getName();
            if (!(entryName.endsWith(".class") && entryName.startsWith("net/minecraft/server/"))) {
                continue;
            }

            final Class<?> clazz = Class.forName(entryName.substring(0, entryName.length() - ".class".length()).replace('/', '.'));
            assertThat(entryName, clazz, is(not(nullValue())));
            for (final Field field : clazz.getDeclaredFields()) {
                if (IAttribute.class.isAssignableFrom(field.getType()) && Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    final String attributeName = ((IAttribute) field.get(null)).a();
                    assertThat("Logical error: duplicate name `" + attributeName + "' in " + clazz.getName(), names.add(attributeName), is(true));
                    assertThat(clazz.getName(), CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES, hasItem(attributeName));
                }
            }
        }

        assertThat("Extra values detected", CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES, is(names));
    }
}
