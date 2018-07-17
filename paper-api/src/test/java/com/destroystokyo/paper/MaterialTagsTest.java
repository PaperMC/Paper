/*
 * Copyright (c) 2018 Daniel Ennis (Aikar) MIT License
 */

package com.destroystokyo.paper;

import io.papermc.paper.tag.BaseTag;
import io.papermc.paper.tag.EntityTags;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.support.AbstractTestingBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MaterialTagsTest extends AbstractTestingBase {

    @Test
    public void testInitialize() {
        try {
            MaterialTags.SHULKER_BOXES.getValues();
            assert true;
        } catch (Throwable e) {
            Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
            assert false;
        }
    }

    @Test
    public void testLocked() {
        testLocked(MaterialTags.class);
        testLocked(EntityTags.class);
    }

    private static void testLocked(Class<?> clazz) {
        for (BaseTag<?, ?> tag : collectTags(clazz)) {
            assertTrue(tag.isLocked(), "Tag " + tag.key() + " is not locked");
        }
    }

    private static Set<BaseTag<?, ?>> collectTags(Class<?> clazz) {
        Set<BaseTag<?, ?>> tags = new HashSet<>();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers()) && BaseTag.class.isAssignableFrom(field.getType())) {
                    tags.add((BaseTag<?, ?>) field.get(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tags;
    }
}
