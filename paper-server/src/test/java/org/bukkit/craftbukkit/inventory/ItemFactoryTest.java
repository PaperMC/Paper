package org.bukkit.craftbukkit.inventory;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.minecraft.server.CommandAbstract;
import net.minecraft.server.IAttribute;

import org.bukkit.support.AbstractTestingBase;
import org.junit.Test;

public class ItemFactoryTest extends AbstractTestingBase {

    @Test
    public void testKnownAttributes() throws Throwable {
        final ZipInputStream nmsZipStream = new ZipInputStream(CommandAbstract.class/* Magic class that isn't imported! */.getProtectionDomain().getCodeSource().getLocation().openStream());
        final Collection<String> names = new HashSet<String>();
        for (ZipEntry clazzEntry; (clazzEntry = nmsZipStream.getNextEntry()) != null; ) {
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

        nmsZipStream.close();

        assertThat("Extra values detected", CraftItemFactory.KNOWN_NBT_ATTRIBUTE_NAMES, is(names));
    }
}
